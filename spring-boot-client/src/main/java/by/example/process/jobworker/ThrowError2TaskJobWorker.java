package by.example.process.jobworker;

import by.example.process.client.dto.JobDto;
import by.example.process.client.dto.ProcessInstanceVariable;
import by.example.service.ProcessClientService;

import java.util.List;

@ExternalTask(topic = "throw-error-2-topic")
public class ThrowError2TaskJobWorker extends AbstractTask {

    public ThrowError2TaskJobWorker(ProcessClientService processService) {
        super(processService);
    }

    @Override
    public List<ProcessInstanceVariable> execute(JobDto task) {
        System.out.println("result from block ThrowError2ExternalTask");
        return null;
    }
}
