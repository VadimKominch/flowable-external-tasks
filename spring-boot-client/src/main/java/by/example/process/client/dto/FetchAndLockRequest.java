package by.example.process.client.dto;

public class FetchAndLockRequest {
    private String workerId;
    private String lockDuration;
    private String topic;

    public FetchAndLockRequest() {
    }

    public FetchAndLockRequest(String workerId, String lockDuration, String topic) {
        this.workerId = workerId;
        this.lockDuration = lockDuration;
        this.topic = topic;
    }

    public String getLockDuration() {
        return lockDuration;
    }

    public void setLockDuration(String lockDuration) {
        this.lockDuration = lockDuration;
    }

    public String getWorkerId() {
        return workerId;
    }

    public void setWorkerId(String workerId) {
        this.workerId = workerId;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}
