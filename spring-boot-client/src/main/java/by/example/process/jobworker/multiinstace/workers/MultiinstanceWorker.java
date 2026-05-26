package by.example.process.jobworker.multiinstace.workers;

import by.example.process.client.dto.JobDto;
import by.example.process.client.dto.ProcessInstanceVariable;
import by.example.process.jobworker.AbstractTask;
import by.example.process.jobworker.ExternalTask;
import by.example.service.ProcessClientService;

import java.util.List;

@ExternalTask(topic = "multiinstance-worker-topic")
public class MultiinstanceWorker extends AbstractTask {
    public MultiinstanceWorker(ProcessClientService processService) {
        super(processService);
    }

    @Override
    public List<ProcessInstanceVariable> execute(JobDto job) throws RuntimeException {
        String value = getExecutionVariable(job, "name");
        System.out.println("value of variable is " + value);
        return List.of();
    }
}
