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
public class ThrowErrorTaskJobWorker {
    public static final String THROW_ERROR_TOPIC = "throw-error-topic";
    private static final String EXTERNAL_TASK_WORKER_ID = "123456789";

    private final ProcessClientService processService;

    public ThrowErrorTaskJobWorker(ProcessClientService processService) {
        this.processService = processService;
    }

    @Scheduled(fixedRate = 5000, scheduler = "flowablePool")
    public void throwError2TaskProcess() {
        List<?> helloTopicTasks = processService.getTasks(EXTERNAL_TASK_WORKER_ID, THROW_ERROR_TOPIC);
        List<Map<String, Object>> tasks = new ArrayList<>();
        tasks.addAll((Collection<? extends Map<String, Object>>) helloTopicTasks);


        if (tasks.isEmpty()) {
            return;
        }

        for (Map task : tasks) {
            String taskId = (String) task.get("id");
            //business logic
            System.out.println("result from block ThrowErrorExternalTask");

            processService.completeTask(
                    EXTERNAL_TASK_WORKER_ID,
                    taskId,
                    List.of(new FlowableVariable("subprocessProcess", "string", "FAIL"))
            );
        }
    }
}
