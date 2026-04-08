package by.example.process.jobworker;

import by.example.process.client.dto.JobDto;
import by.example.process.client.dto.ProcessInstanceVariable;
import by.example.service.ProcessClientService;
import jakarta.annotation.PostConstruct;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;
import java.util.UUID;

public abstract class AbstractTask {
    protected final ProcessClientService processService;
    private final String EXTERNAL_TASK_WORKER_ID;
    private String topic;

    public AbstractTask(ProcessClientService processService) {
        this.processService = processService;
        this.EXTERNAL_TASK_WORKER_ID = UUID.randomUUID().toString();
    }

    public abstract List<ProcessInstanceVariable> execute(JobDto job) throws Exception;

    @PostConstruct
    public void init() {
        ExternalTask annotation = AnnotationUtils.findAnnotation(getClass(), ExternalTask.class);

        if (annotation == null) {
            throw new IllegalStateException("Missing @ExternalTask on " + getClass());
        }

        this.topic = annotation.topic();
    }

    @Scheduled(fixedRate = 5000, scheduler = "flowablePool")
    public void run() {
        List<JobDto> tasks = processService.getTasks(EXTERNAL_TASK_WORKER_ID, topic);
        tasks.forEach(
                task -> {
                    String taskId = task.id();
                    try {
                        var variables = execute(task);
                        processService.completeTask(EXTERNAL_TASK_WORKER_ID, taskId, variables);
                    } catch (Exception e) {
                        int retries = Math.max(task.retries() - 1, 0);
                        processService.failTask(EXTERNAL_TASK_WORKER_ID, taskId, retries);
                    }
                });
    }

    /**
     * Method to extract variable from flowable process. Should only be used if businessKey was passed
     * as variable at the moment of process start.
     */
    protected String extractBusinessKeyFromJob(JobDto task) {
        return (String)
                task.variables().stream()
                        .filter(el -> el.name().equals("businessKey"))
                        .findFirst()
                        .orElseThrow()
                        .value();
    }

    protected <T> T getExecutionVariable(JobDto task, String variableName) {
        var result = task.variables().stream().filter(el -> el.name().equals(variableName)).toList();
        if(result.isEmpty()) {
            return null;
        }
        return (T) result.get(0).value();
    }
}
