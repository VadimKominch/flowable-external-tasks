package by.example.process.entity;

public enum ProcessDefinitionKey {
    EXTERNAL_TASK("externalTaskWithCorrelationExample"),
    EXCLUSIVE_GATEWAY("exclusiveGateway");

    private final String schemeName;

    ProcessDefinitionKey(String schemeName) {
        this.schemeName = schemeName;
    }

    public String getSchemeName() {
        return schemeName;
    }
}
