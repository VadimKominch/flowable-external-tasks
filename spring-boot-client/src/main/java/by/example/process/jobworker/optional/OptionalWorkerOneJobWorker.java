package by.example.process.jobworker.optional;

import by.example.process.client.dto.JobDto;
import by.example.process.client.dto.ProcessInstanceVariable;
import by.example.process.jobworker.AbstractTask;
import by.example.process.jobworker.ExternalTask;
import by.example.service.ProcessClientService;

import java.util.List;

@ExternalTask(topic = "optional-job-worker-1-topic")
public class OptionalWorkerOneJobWorker extends AbstractTask {

    public OptionalWorkerOneJobWorker(ProcessClientService processService) {
        super(processService);
    }

    @Override
    public List<ProcessInstanceVariable> execute(JobDto job) {
        System.out.println("Optional worker 1");
        return List.of();
    }
}
