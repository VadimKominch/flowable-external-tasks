package by.example.client.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Configuration
@EnableScheduling
public class ApplicationConfig {
    @Bean("flowablePool")
    public ScheduledExecutorService taskExecutor() {
        return Executors.newScheduledThreadPool(10);
    }
}
