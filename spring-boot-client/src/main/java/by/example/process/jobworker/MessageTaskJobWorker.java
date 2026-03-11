package by.example.process.jobworker;

import by.example.service.ProcessClientService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Component
public class MessageTaskJobWorker {
    public static final String MESSAGE_TOPIC = "message-topic";
    private static final String EXTERNAL_TASK_WORKER_ID = "123456789";

    private final ProcessClientService processService;

    public MessageTaskJobWorker(ProcessClientService processService) {
        this.processService = processService;
    }

    @Scheduled(fixedRate = 5000, scheduler = "flowablePool")
    public void helloTaskProcess() {
        List<?> messageTopicTasks = processService.getTasks(EXTERNAL_TASK_WORKER_ID, MESSAGE_TOPIC);
        List<Map<String, Object>> tasks = new ArrayList<>();
        tasks.addAll((Collection<? extends Map<String, Object>>) messageTopicTasks);


        if (tasks.isEmpty()) {
            return;
        }

        for (Map task : tasks) {
            String taskId = (String) task.get("id");
            // business logic
            System.out.println("processing message");
            // end of business logic
            processService.completeTask(
                    EXTERNAL_TASK_WORKER_ID,
                    taskId,
                    List.of()
            );
        }
    }
}
