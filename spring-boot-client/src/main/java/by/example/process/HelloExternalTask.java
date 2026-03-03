package by.example.process;

import org.springframework.stereotype.Component;

@Component
public class HelloExternalTask extends DelegateExternalTask {
    @Override
    public void execute() {
        System.out.println("hello");
    }

    @Override
    public String getTopicName() {
        return "hello-topic";
    }
}
