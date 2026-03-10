package by.example.client.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskDecorator;
import org.springframework.stereotype.Component;

@Component
public class LoggingTaskDecorator implements TaskDecorator {

    @Value("${server.logging}")
    boolean isLoggingEnabled;

    @Override
    public Runnable decorate(Runnable runnable) {
        return () -> {
            long start = System.currentTimeMillis();
            try {
                runnable.run();
            } finally {
                long end = System.currentTimeMillis();
                if(isLoggingEnabled) {
                    System.out.println("Task completed in " + (end - start) + "ms");
                }
            }
        };
    }
}
