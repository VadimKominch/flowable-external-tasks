package by.example.process.jobworker;

import by.example.process.client.FlowableRestClient;
import by.example.service.ProcessClientService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

public abstract class AbstractTask {
   private final ProcessClientService processService;
   private final String EXTERNAL_TASK_WORKER_ID;

    public AbstractTask(ProcessClientService processService) {
        this.processService = processService;
        this.EXTERNAL_TASK_WORKER_ID = UUID.randomUUID().toString();
    }

    public abstract String getTopic();
    public abstract void execute(Supplier<?> taskReceiver) throws Exception;

    @Scheduled(fixedRate = 5000, scheduler = "flowablePool")
    public void run() {
        List<?> tasks = processService.getTasks(EXTERNAL_TASK_WORKER_ID, getTopic());
        tasks.forEach(task -> {
            // replace with dto
            var taskMap = (Map<String, Object>) task;
            String taskId = (String) taskMap.get("id");
            try {
                execute(() -> task);
                processService.completeTask(EXTERNAL_TASK_WORKER_ID, taskId, List.of());
            } catch (Exception e) {
                int retries = (Integer) taskMap.get("retries");
                retries = retries >0 ? retries - 1 : 0;
                processService.failTask(EXTERNAL_TASK_WORKER_ID, taskId, List.of(), retries);
            }
        });
    }
}
