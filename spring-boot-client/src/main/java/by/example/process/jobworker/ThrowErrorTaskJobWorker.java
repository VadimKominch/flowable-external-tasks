package by.example.process.jobworker;

import by.example.process.client.dto.JobDto;
import by.example.process.client.dto.ProcessInstanceVariable;
import by.example.service.ProcessClientService;

import java.util.List;

@ExternalTask(topic = "throw-error-topic")
public class ThrowErrorTaskJobWorker extends AbstractTask {

    public ThrowErrorTaskJobWorker(ProcessClientService processService) {
        super(processService);
    }

    @Override
    public List<ProcessInstanceVariable> execute(JobDto task) {
        System.out.println("result from block ThrowErrorExternalTask");
        return List.of();
    }
}
