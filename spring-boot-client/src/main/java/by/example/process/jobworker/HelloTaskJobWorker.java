package by.example.process.jobworker;

import by.example.process.client.dto.JobDto;
import by.example.service.ProcessClientService;
import org.springframework.stereotype.Component;

@Component
public class HelloTaskJobWorker extends AbstractTask {
    public static final String HELLO_TOPIC = "hello-topic";


    public HelloTaskJobWorker(ProcessClientService processService) {
        super(processService);
    }

    @Override
    public String getTopic() {
        return HELLO_TOPIC;
    }

    @Override
    public void execute(JobDto task) throws Exception {
        System.out.println("hello");
    }
}
