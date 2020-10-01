package load.and.apply.model;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@DynamoDbBean
public class ContentRecord {

    String partitionId;
    String sortKey;
    String loadId;
    String content;


    @DynamoDbPartitionKey
    public String getPartitionId() {
        return partitionId;
    }


    @DynamoDbSortKey
    public String getSortKey() {
        return sortKey;
    }



    public String getLoadId() {
        return loadId;
    }


    public String getContent() {
        return content;
    }

    public void setPartitionId(String partitionId) {
        this.partitionId = partitionId;
    }

    public void setSortKey(String sortKey) {
        this.sortKey = sortKey;
    }

    public void setLoadId(String loadId) {
        this.loadId = loadId;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ContentRecord _setPartitionId(String partitionId) {
        this.partitionId = partitionId;
        return this;
    }

    public ContentRecord _setSortKey(String sortKey) {
        this.sortKey = sortKey;
        return this;
    }

    public ContentRecord _setLoadId(String loadId) {
        this.loadId = loadId;
        return this;
    }

    public ContentRecord _setContent(String content) {
        this.content = content;
        return this;
    }

    @Override
    public String toString() {
        return "ContentRecord{" +
                "partitionId='" + partitionId + '\'' +
                ", sortKey='" + sortKey + '\'' +
                ", loadId='" + loadId + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
