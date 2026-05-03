package by.example.process.jobworker;

import by.example.process.client.dto.JobDto;
import by.example.process.client.dto.ProcessInstanceVariable;
import by.example.service.ProcessClientService;

import java.util.List;

@ExternalTask(topic = "print-topic")
public class PrintTaskJobWorker extends AbstractTask {
    public static final String PRINT_VAR_VARIABLE = "printVar";

    public PrintTaskJobWorker(ProcessClientService processService) {
        super(processService);
    }

    @Override
    public List<ProcessInstanceVariable> execute(JobDto task) {
        var variable = processService.getVariable(task.processInstanceId(), "processId");
        String innerVar = getExecutionVariable(task, PRINT_VAR_VARIABLE);
        System.out.println("Value of inner variable is " + innerVar);
        System.out.println("print from process with process instance id" + variable);
        return List.of();
    }
}
