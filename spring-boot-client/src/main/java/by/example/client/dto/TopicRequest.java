package by.example.client.dto;

public class TopicRequest {
    private String topicName;
    private long lockDuration;

    public TopicRequest() {
    }

    public TopicRequest(String topicName, long lockDuration) {
        this.topicName = topicName;
        this.lockDuration = lockDuration;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public long getLockDuration() {
        return lockDuration;
    }

    public void setLockDuration(long lockDuration) {
        this.lockDuration = lockDuration;
    }
}
