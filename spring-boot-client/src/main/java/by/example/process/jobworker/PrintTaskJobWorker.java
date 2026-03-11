package by.example.process.jobworker;

import by.example.process.client.dto.FlowableVariable;
import by.example.service.ProcessClientService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Component
public class PrintTaskJobWorker {
    public static final String PRINT_TOPIC = "print-topic";
    private static final String EXTERNAL_TASK_WORKER_ID = "123456789";

    private final ProcessClientService processService;

    public PrintTaskJobWorker(ProcessClientService processService) {
        this.processService = processService;
    }

    @Scheduled(fixedRate = 5000, scheduler = "flowablePool")
    public void helloTaskProcess() {
        List<?> helloTopicTasks = processService.getTasks(EXTERNAL_TASK_WORKER_ID, PRINT_TOPIC);
        List<Map<String, Object>> tasks = new ArrayList<>();
        tasks.addAll((Collection<? extends Map<String, Object>>) helloTopicTasks);


        if (tasks.isEmpty()) {
            return;
        }

        for (Map task : tasks) {
            String taskId = (String) task.get("id");
            // business logic
            System.out.println("print");

            processService.completeTask(
                    EXTERNAL_TASK_WORKER_ID,
                    taskId,
                    List.of(new FlowableVariable("result", "string", "FAIL"))
            );
        }
    }
}
