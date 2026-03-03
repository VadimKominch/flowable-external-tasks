package by.example.process;

import org.springframework.stereotype.Component;

@Component
public class PrintExternalTask extends DelegateExternalTask {
    @Override
    public void execute() {
        System.out.println("print");
    }

    @Override
    public String getTopicName() {
        return "print-topic";
    }
}
