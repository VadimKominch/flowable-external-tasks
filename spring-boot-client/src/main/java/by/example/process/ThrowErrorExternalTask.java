package by.example.process;

import org.springframework.stereotype.Component;

@Component
public class ThrowErrorExternalTask extends DelegateExternalTask {
    @Override
    public void execute() {
        System.out.println("result from block ThrowErrorExternalTask");
    }

    @Override
    public String getTopicName() {
        return "throw-error-topic";
    }
}
