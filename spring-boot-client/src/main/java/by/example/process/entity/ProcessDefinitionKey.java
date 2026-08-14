package by.example.process.entity;

public enum ProcessDefinitionKey {
    EXTERNAL_TASK("externalTaskWithCorrelationExample"),
    EXCLUSIVE_GATEWAY("exclusiveGateway"),
    DMN_SCHEME_INTEGRATION("multiinstance"),
    MULTIINSTANCE_SUBPROCESS_DYNAMIC_CORRELATION_KEY("subprocess"),
    MULTIINSTANCE("mutiinstance"),
    DYNAMIC_KEY("dynamicKey"),
    OPTIONAL_EXECUTION("optionalExecution"),
    ;

    private final String schemeName;

    ProcessDefinitionKey(String schemeName) {
        this.schemeName = schemeName;
    }

    public String getSchemeName() {
        return schemeName;
    }
}
