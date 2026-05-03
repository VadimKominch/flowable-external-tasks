package by.example.process.jobworker;

import by.example.process.client.dto.JobDto;
import by.example.process.client.dto.ProcessInstanceVariable;
import by.example.service.ProcessClientService;

import java.util.List;

@ExternalTask(topic = "message-topic")
public class MessageTaskJobWorker extends AbstractTask{

    public MessageTaskJobWorker(ProcessClientService processService) {
        super(processService);
    }

    @Override
    public List<ProcessInstanceVariable> execute(JobDto task) {
        System.out.println("processing message");
        return List.of();
    }
}
