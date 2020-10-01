package load.and.apply

import load.and.apply.model.ContentRecord
import org.springframework.stereotype.Service
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable
import software.amazon.awssdk.enhanced.dynamodb.Expression
import software.amazon.awssdk.enhanced.dynamodb.TableSchema
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest
import software.amazon.awssdk.enhanced.dynamodb.model.ScanEnhancedRequest
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.AttributeValue

@Service
class RecordsService (val dynamoDbEnhancedClient: DynamoDbEnhancedClient, val dynamoDbClient: DynamoDbClient) {

    fun listTables(): MutableList<String> {
        return dynamoDbClient.listTables().tableNames()
    }

    fun insert(content: ContentRecord) {
        recordsTable.putItem(content)
    }

    fun find(prefix: String, activeLoads: List<String>): ContentRecord {
        val conditions:QueryConditional = QueryConditional.sortBeginsWith{ kb ->
            kb.partitionValue("1")
            kb.sortValue(prefix)
        }
        val recordsIterable = recordsTable.query(  QueryEnhancedRequest.builder().queryConditional( conditions).build())
        val candidates = recordsIterable.items().map { r -> Pair(r, indexOfLoad( r.loadId, activeLoads)) }
                .filter { pair -> pair.second >=0 }
        val initial = candidates.first()
        val res = candidates.foldRight(initial){ pair, acc->
            if( pair.second > acc.second){
                pair
            }else{
                acc
            }
        }
        return res.first
    }

    private fun indexOfLoad(loadId: String, activeLoads: List<String>): Int {
        activeLoads.forEachIndexed{ idx, r ->
            if( loadId == r) return idx
        }
        return -1
    }

    fun deleteRecordsByLoad(loadId: String) {
        val myExpression = Expression.builder()
                                            .expression("loadId = :l")
                                             .putExpressionValue(":l", AttributeValue.builder().s(loadId).build() )
                                             .build()
        val scanFilter = ScanEnhancedRequest.builder().filterExpression( myExpression )
                .addAttributeToProject("partitionId")
                .addAttributeToProject("sortKey").build()
        recordsTable.scan( scanFilter ).items().forEach {
            println("will delete record: $it")
        }
    }

    private lateinit var recordsTable: DynamoDbTable<ContentRecord>

    init {
        recordsTable = dynamoDbEnhancedClient.table("records", TableSchema.fromBean(ContentRecord::class.java))
        try {
            recordsTable.createTable()
        }catch (e: Exception ){
            println("ignoring:$e")
        }
    }




}
