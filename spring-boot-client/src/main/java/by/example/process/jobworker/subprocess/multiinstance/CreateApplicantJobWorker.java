package by.example.process.jobworker.subprocess.multiinstance;

import by.example.process.client.dto.JobDto;
import by.example.process.client.dto.ProcessInstanceVariable;
import by.example.process.jobworker.AbstractTask;
import by.example.process.jobworker.ExternalTask;
import by.example.service.ProcessClientService;

import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@ExternalTask(topic = "create-applicant-topic")
public class CreateApplicantJobWorker extends AbstractTask {

    private ScheduledExecutorService service = Executors.newScheduledThreadPool(1);

    public CreateApplicantJobWorker(ProcessClientService processService) {
        super(processService);
    }

    @Override
    public List<ProcessInstanceVariable> execute(JobDto job) {
        String value = getExecutionVariable(job, "external_user_id");
        Integer index = getExecutionVariable(job, "elementIndex");
        System.out.println("value is " + value);
        System.out.println("index is " + index);
        ProcessInstanceVariable multiinstanceVariables = new ProcessInstanceVariable("correlationKey", "string", value);
        service.schedule(() -> {
            String businessKey = processService.getVariable(job.processInstanceId(), "processId").value().toString();
            processService.correlateMessage(businessKey, value);
        }, 7, TimeUnit.SECONDS);
        return List.of(multiinstanceVariables);
    }
}
