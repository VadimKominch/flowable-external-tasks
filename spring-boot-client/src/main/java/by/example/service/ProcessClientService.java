package by.example.service;

import by.example.client.FlowableRestClient;
import by.example.client.dto.FlowableVariable;
import by.example.process.DelegateExternalTask;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProcessClientService {
    public static final String EXTERNAL_TASK_DEFINITION_KEY = "externalTaskWithCorrelationExample";
    private static final String EXTERNAL_TASK_WORKER_ID = UUID.randomUUID().toString();
    public static final String PRINT_TOPIC = "print-topic";
    public static final String HELLO_TOPIC = "hello-topic";
    private final FlowableRestClient restClient;
    private final List<DelegateExternalTask> externalTasks;

    public ProcessClientService(FlowableRestClient restClient, List<DelegateExternalTask> externalTasks) {
        this.restClient = restClient;
        this.externalTasks = externalTasks;
    }

    public String startProcess() {
        String businessKey = UUID.randomUUID().toString();
        System.out.println("Business key is " + businessKey);

        restClient.startProcess(EXTERNAL_TASK_DEFINITION_KEY, businessKey);

        return businessKey;
    }

    // is processed inside
    @Scheduled(fixedRate = 5000, scheduler = "flowablePool")
    public void printTaskProcess() {
        List<?> printTopicTasks = restClient.getTasks(EXTERNAL_TASK_WORKER_ID, PRINT_TOPIC);
        List<Map<String, Object>> tasks = new ArrayList<>();
        tasks.addAll((Collection<? extends Map<String, Object>>) printTopicTasks);


        if (tasks.isEmpty()) {
            return;
        }

        for (Map task : tasks) {
            String taskId = (String) task.get("id");
            System.out.println("Task " + taskId);
            processTask(taskId, externalTasks
                    .stream()
                    .filter(el -> el.getTopicName().equals(PRINT_TOPIC))
                    .findFirst().get(),
                    List.of(new FlowableVariable("result", "string", "SUCCESS")));
        }
    }

    @Scheduled(fixedRate = 5000, scheduler = "flowablePool")
    public void helloTaskProcess() {
        List<?> helloTopicTasks = restClient.getTasks(EXTERNAL_TASK_WORKER_ID, HELLO_TOPIC);
        List<Map<String, Object>> tasks = new ArrayList<>();
        tasks.addAll((Collection<? extends Map<String, Object>>) helloTopicTasks);


        if (tasks.isEmpty()) {
            return;
        }

        for (Map task : tasks) {
            String taskId = (String) task.get("id");
            processTask(taskId, externalTasks
                    .stream()
                    .filter(el -> el.getTopicName().equals(HELLO_TOPIC))
                    .findFirst().get(),
                    List.of());
        }
    }

    private void processTask(String taskId, DelegateExternalTask executor, List<FlowableVariable> variables) {
        try {
            executor.execute();
            restClient.complete(EXTERNAL_TASK_WORKER_ID, taskId, variables);

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}

