package by.example.process.jobworker.multiinstance;

import by.example.process.client.dto.JobDto;
import by.example.process.client.dto.ProcessInstanceVariable;
import by.example.process.jobworker.AbstractTask;
import by.example.process.jobworker.ExternalTask;
import by.example.service.ProcessClientService;

import java.util.List;

@ExternalTask(topic = "post-decision-topic")
public class PostProcessTaskJobWorker extends AbstractTask {

    public PostProcessTaskJobWorker(ProcessClientService processService) {
        super(processService);
    }

    @Override
    public List<ProcessInstanceVariable> execute(JobDto job) {
        System.out.println("Executing post process task job worker");
        return List.of();
    }
}
