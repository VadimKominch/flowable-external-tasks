package by.example.process.jobworker;

import by.example.process.client.dto.JobDto;
import by.example.service.ProcessClientService;
import org.springframework.stereotype.Component;

@Component
public class ThrowErrorTaskJobWorker extends AbstractTask {
    public static final String THROW_ERROR_TOPIC = "throw-error-topic";

    public ThrowErrorTaskJobWorker(ProcessClientService processService) {
        super(processService);
    }

    @Override
    public String getTopic() {
        return THROW_ERROR_TOPIC;
    }

    @Override
    public void execute(JobDto task) throws Exception {
        System.out.println("result from block ThrowErrorExternalTask");
    }
}
