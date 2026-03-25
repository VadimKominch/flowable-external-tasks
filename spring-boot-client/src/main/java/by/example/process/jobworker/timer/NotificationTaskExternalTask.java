package by.example.process.jobworker.timer;


import by.example.process.client.dto.JobDto;
import by.example.process.jobworker.AbstractTask;
import by.example.process.jobworker.ExternalTask;
import by.example.service.ProcessClientService;

import java.time.LocalDateTime;

@ExternalTask(topic = "notification-topic")
public class NotificationTaskExternalTask extends AbstractTask {
    public NotificationTaskExternalTask(ProcessClientService processService) {
        super(processService);
    }

    @Override
    public void execute(JobDto job) throws Exception {
        System.out.println("Process finished at " +  LocalDateTime.now());
    }
}
