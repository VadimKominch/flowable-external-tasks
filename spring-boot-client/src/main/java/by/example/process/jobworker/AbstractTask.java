package by.example.process.jobworker;

import org.springframework.scheduling.annotation.Scheduled;

public abstract class AbstractTask {
    public abstract String getTopic();
    public abstract void execute();

    @Scheduled(fixedRate = 5000, scheduler = "flowablePool")
    public void run() {

    }
}
