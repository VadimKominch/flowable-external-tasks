package by.example.process.jobworker;

import by.example.process.client.dto.JobDto;
import by.example.service.ProcessClientService;
import org.springframework.stereotype.Component;

@Component
public class PrintTaskJobWorker extends AbstractTask{
    public static final String PRINT_TOPIC = "print-topic";

    public PrintTaskJobWorker(ProcessClientService processService) {
        super(processService);
    }

    @Override
    public String getTopic() {
        return PRINT_TOPIC;
    }

    @Override
    public void execute(JobDto task) throws Exception {
        var variable = processService.getVariable(task.processInstanceId(), "processId");
        System.out.println("print from process with process instance id" + variable );
    }
}
