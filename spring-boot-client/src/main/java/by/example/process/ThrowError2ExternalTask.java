package by.example.process;

import org.springframework.stereotype.Component;

@Component
public class ThrowError2ExternalTask extends DelegateExternalTask {
    @Override
    public void execute() {
        System.out.println("result from block ThrowError2ExternalTask");
    }

    @Override
    public String getTopicName() {
        return "throw-error-2-topic";
    }
}
