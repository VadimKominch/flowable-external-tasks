package by.example.process.jobworker;

import by.example.process.client.dto.JobDto;
import by.example.process.client.dto.ProcessInstanceVariable;
import by.example.service.ProcessClientService;

import java.util.List;

@ExternalTask(topic = "hello-topic")
public class HelloTaskJobWorker extends AbstractTask {


    public HelloTaskJobWorker(ProcessClientService processService) {
        super(processService);
    }

    @Override
    public List<ProcessInstanceVariable> execute(JobDto task) {
        System.out.println("hello");
        return List.of();
    }
}
