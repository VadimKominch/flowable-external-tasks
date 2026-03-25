package by.example.process.jobworker;

import by.example.process.client.dto.JobDto;
import by.example.service.ProcessClientService;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;
import java.util.UUID;

public abstract class AbstractTask {
   protected final ProcessClientService processService;
   private final String EXTERNAL_TASK_WORKER_ID;

    public AbstractTask(ProcessClientService processService) {
        this.processService = processService;
        this.EXTERNAL_TASK_WORKER_ID = UUID.randomUUID().toString();
    }

    public abstract String getTopic();
    public abstract void execute(JobDto task) throws Exception;

    @Scheduled(fixedRate = 5000, scheduler = "flowablePool")
    public void run() {
        List<JobDto> tasks = processService.getTasks(EXTERNAL_TASK_WORKER_ID, getTopic());
        tasks.forEach(task -> {
            String taskId = task.id();
            try {
                execute(task);
                processService.completeTask(EXTERNAL_TASK_WORKER_ID, taskId, List.of());
            } catch (Exception e) {
                int retries = task.retries();
                retries = retries > 0 ? retries - 1 : 0;
                processService.failTask(EXTERNAL_TASK_WORKER_ID, taskId, List.of(), retries);
            }
        });
    }
}
