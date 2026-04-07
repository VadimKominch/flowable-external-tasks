package by.example.config;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.stereotype.Component;

@Component
public class ProcessTestContext {
    private ProcessInstance processInstance;

    public ProcessInstance getProcessInstance() {
        return processInstance;
    }

    public void setProcessInstance(ProcessInstance processInstance) {
        this.processInstance = processInstance;
    }
}
