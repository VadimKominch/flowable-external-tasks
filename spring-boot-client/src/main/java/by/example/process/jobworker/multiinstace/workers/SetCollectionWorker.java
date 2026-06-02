package by.example.process.jobworker.multiinstace.workers;

import by.example.process.client.dto.JobDto;
import by.example.process.client.dto.ProcessInstanceVariable;
import by.example.process.jobworker.AbstractTask;
import by.example.process.jobworker.ExternalTask;
import by.example.service.ProcessClientService;

import java.util.List;

@ExternalTask(topic = "set-collections-topic")
public class SetCollectionWorker extends AbstractTask {

    public SetCollectionWorker(ProcessClientService processService) {
        super(processService);
    }

    @Override
    public List<ProcessInstanceVariable> execute(JobDto job) throws RuntimeException {
        ProcessInstanceVariable namesVariable = new ProcessInstanceVariable("names", "json", List.of("banana", "apple","grape"));
        System.out.println("Variable names initialized");
        return List.of(namesVariable);
    }
}
