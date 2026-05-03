package by.example.process.jobworker;

import by.example.process.client.dto.JobDto;
import by.example.process.client.dto.ProcessInstanceVariable;
import by.example.service.ProcessClientService;

import java.util.List;

@ExternalTask(topic = "error-topic")
public class ErrorTaskJobWorker extends AbstractTask{

    public ErrorTaskJobWorker(ProcessClientService processService) {
        super(processService);
    }

    @Override
    public List<ProcessInstanceVariable> execute(JobDto job) {
        System.out.println("error while executing external task");
        return List.of();
    }
}
