package by.example.process.jobworker.multiinstance;

import by.example.process.client.dto.JobDto;
import by.example.process.client.dto.ProcessInstanceVariable;
import by.example.process.jobworker.AbstractTask;
import by.example.process.jobworker.ExternalTask;
import by.example.service.ProcessClientService;

import java.util.List;

@ExternalTask(topic = "worker-topic")
public class ProcessTaskJobWorker extends AbstractTask {

    public ProcessTaskJobWorker(ProcessClientService processService) {
        super(processService);
    }

    @Override
    public List<ProcessInstanceVariable> execute(JobDto job) {
        System.out.println("Executing process task job worker");

        return List.of(new ProcessInstanceVariable("age", "integer", getAge()));
    }

    private int getAge() {
        return Math.toIntExact(Math.round(Math.random() * 100));
    }
}
