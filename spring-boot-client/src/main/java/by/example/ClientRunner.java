package by.example;

import by.example.process.entity.ProcessDefinitionKey;
import by.example.service.ProcessClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.UUID;

@SpringBootApplication
public class ClientRunner implements CommandLineRunner {

    @Autowired
    private ProcessClientService processService;

    public static void main(String[] args) {
        SpringApplication.run(ClientRunner.class, args);
    }

    @Override
    public void run(String... args) {
        processService.startProcess(ProcessDefinitionKey.MULTIINSTANCE_SUBPROCESS_DYNAMIC_CORRELATION_KEY, UUID.randomUUID().toString(), new ArrayList<>());
    }
}
