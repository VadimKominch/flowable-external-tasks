package by.example.process.jobworker.multiinstance;

import by.example.process.client.dto.JobDto;
import by.example.process.client.dto.ProcessInstanceVariable;
import by.example.process.jobworker.AbstractTask;
import by.example.process.jobworker.ExternalTask;
import by.example.service.ProcessClientService;

import java.util.List;

@ExternalTask(topic = "post-decision-error-topic")
public class PostProcessErrorTaskJobWorker extends AbstractTask {

    public PostProcessErrorTaskJobWorker(ProcessClientService processService) {
        super(processService);
    }

    @Override
    public List<ProcessInstanceVariable> execute(JobDto job) {
        System.out.println("Executing post process task job worker");
        job
            .variables()
            .stream()
            .filter(el -> el.name().equals("result"))
            .findFirst()
            .ifPresent(variable -> System.out.println(variable.value()));
        return List.of();
    }
}
