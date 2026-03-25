package by.example.process.jobworker.timer;

import by.example.process.client.dto.JobDto;
import by.example.process.client.dto.ProcessInstanceVariable;
import by.example.process.jobworker.AbstractTask;
import by.example.process.jobworker.ExternalTask;
import by.example.service.ProcessClientService;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ExternalTask(topic = "something-topic")
public class SomethingTaskExternalTask extends AbstractTask {
    public static final String TIMER_EXPRESSION_VAR = "timerExpression";
    public static final String RETRY_VAR = "retries";
    public SomethingTaskExternalTask(ProcessClientService processService) {
        super(processService);
    }

    @Override
    public void execute(JobDto job) throws Exception {
        ProcessInstanceVariable retriesFromProcess = processService.getVariable(
                job.processInstanceId(),
                RETRY_VAR);
        int retries;
        if(retriesFromProcess != null) {
            retries = Integer.parseInt((String) retriesFromProcess.value());
        } else {
            retries = 0;
        }
        System.out.println("Current retry is: " + retries);

        long baseDelayMs = 30_000;
        long nextDelayMs = Math.min(5 * 60_000, baseDelayMs * (long)Math.pow(2, retries));

        long minutes = nextDelayMs / 60_000;
        long seconds = (nextDelayMs % 60_000) / 1000;

        String isoDuration = String.format("PT%dM%dS", minutes, seconds);

        Map<String, Object> vars = new HashMap<>();
        vars.put(RETRY_VAR, String.valueOf(retries + 1));
        vars.put(TIMER_EXPRESSION_VAR, isoDuration);

        System.out.println("Current time is " +  LocalDateTime.now());
        processService.updateVariable(job.processInstanceId(), RETRY_VAR, String.valueOf(retries + 1));
        processService.updateVariable(job.processInstanceId(), TIMER_EXPRESSION_VAR, isoDuration);
    }
}
