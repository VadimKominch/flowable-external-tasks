package by.example.process;

import org.springframework.stereotype.Component;

@Component
public class ErrorExternalTask extends DelegateExternalTask {
    @Override
    public void execute() {
        System.out.println("error while executing external task");
    }

    @Override
    public String getTopicName() {
        return "error-topic";
    }
}
