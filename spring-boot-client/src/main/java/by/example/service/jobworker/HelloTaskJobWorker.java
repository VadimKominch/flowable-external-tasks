package by.example.service.jobworker;

import by.example.process.DelegateExternalTask;
import by.example.service.ProcessClientService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class HelloTaskJobWorker {
    public static final String HELLO_TOPIC = "hello-topic";
    private static final String EXTERNAL_TASK_WORKER_ID = "123456789";

    private final ProcessClientService processService;
    private final List<DelegateExternalTask> externalTasks;

    public HelloTaskJobWorker(ProcessClientService processService, List<DelegateExternalTask> externalTasks) {
        this.processService = processService;
        this.externalTasks = externalTasks;
    }

    @Scheduled(fixedRate = 5000, scheduler = "flowablePool")
    public void helloTaskProcess() {
        List<?> helloTopicTasks = processService.getTasks(EXTERNAL_TASK_WORKER_ID, HELLO_TOPIC);
        List<Map<String, Object>> tasks = new ArrayList<>();
        tasks.addAll((Collection<? extends Map<String, Object>>) helloTopicTasks);


        if (tasks.isEmpty()) {
            return;
        }

        for (Map task : tasks) {
            String taskId = (String) task.get("id");
            externalTasks
                .stream()
                .filter(el -> el.getTopicName().equals(HELLO_TOPIC))
                .findFirst().get().execute();
            processService.completeTask(
                    EXTERNAL_TASK_WORKER_ID,
                    taskId,
                    List.of()
            );
        }
    }
}
