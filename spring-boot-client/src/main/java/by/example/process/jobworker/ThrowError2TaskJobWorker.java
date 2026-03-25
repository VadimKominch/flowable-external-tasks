package by.example.process.jobworker;

import by.example.process.client.dto.JobDto;
import by.example.service.ProcessClientService;
import org.springframework.stereotype.Component;

@Component
public class ThrowError2TaskJobWorker extends AbstractTask {
    public static final String THROW_ERROR_2_TOPIC = "throw-error-2-topic";

    public ThrowError2TaskJobWorker(ProcessClientService processService) {
        super(processService);
    }

    @Override
    public String getTopic() {
        return THROW_ERROR_2_TOPIC;
    }

    @Override
    public void execute(JobDto task) throws Exception {
        System.out.println("result from block ThrowError2ExternalTask");
    }
}
