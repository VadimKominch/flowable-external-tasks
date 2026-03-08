package by.example.client.config;

import org.springframework.core.task.TaskDecorator;

public class LoggingTaskDecorator implements TaskDecorator {
    @Override
    public Runnable decorate(Runnable runnable) {
        return () -> {
            long start = System.currentTimeMillis();
            try {
                runnable.run();
            } finally {
                long end = System.currentTimeMillis();
                System.out.println("Task completed in " + (end - start) + "ms");
            }
        };
    }
}
