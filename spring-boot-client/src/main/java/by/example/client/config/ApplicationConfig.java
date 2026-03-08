package by.example.client.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
@EnableScheduling
@EnableAsync
public class ApplicationConfig {
    @Bean("flowablePool")
    public ThreadPoolTaskScheduler taskExecutor() {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(10);
        taskScheduler.setVirtualThreads(true);
        taskScheduler.setTaskDecorator(new LoggingTaskDecorator());
        taskScheduler.initialize();
        return taskScheduler;
    }
}
