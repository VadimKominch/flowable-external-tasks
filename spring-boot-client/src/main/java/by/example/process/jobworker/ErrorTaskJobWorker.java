package by.example.process.jobworker;

import by.example.process.client.dto.JobDto;
import by.example.service.ProcessClientService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@Component
public class ErrorTaskJobWorker extends AbstractTask{
    public static final String ERROR_TOPIC = "error-topic";

    public ErrorTaskJobWorker(ProcessClientService processService) {
        super(processService);
    }

    @Override
    public String getTopic() {
        return ERROR_TOPIC;
    }

    @Override
    public void execute(JobDto job) throws Exception {
        System.out.println("error while executing external task");
    }
}
