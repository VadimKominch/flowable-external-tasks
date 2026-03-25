package by.example.process.jobworker;

import by.example.process.client.dto.JobDto;
import by.example.service.ProcessClientService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Component
public class MessageTaskJobWorker extends AbstractTask{
    public static final String MESSAGE_TOPIC = "message-topic";

    public MessageTaskJobWorker(ProcessClientService processService) {
        super(processService);
    }

    @Override
    public String getTopic() {
        return MESSAGE_TOPIC;
    }

    @Override
    public void execute(JobDto task) throws Exception {
        System.out.println("processing message");
    }
}
