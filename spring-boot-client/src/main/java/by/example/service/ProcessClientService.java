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
    public static final String ERROR_TOPIC = "error-topic";
    public static final String THROW_ERROR_TOPIC = "throw-error-topic";
    public static final String THROW_ERROR_2_TOPIC = "throw-error-2-topic";
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

    public void correlateMessage(String businessKey, String messageName) {
        System.out.println("correlateMessage businessKey is " + businessKey);
        var processInstanceQueryResponse = restClient.getProcessInstanceId(businessKey);
        if (processInstanceQueryResponse == null || processInstanceQueryResponse.data().isEmpty()) {
            throw new IllegalStateException(
                    "No execution waiting for message=" + messageName +
                            " processInstance=" + businessKey);
        }
        String processInstanceId = processInstanceQueryResponse.data().getFirst().id();
        var executionResponse = restClient.getExecutions(processInstanceId, messageName);

        String executionId = executionResponse.data().getFirst().id();
        restClient.correlateMessage(executionId, messageName);
    }

    @Scheduled(fixedRate = 5000, scheduler = "flowablePool")
    public void throwError2TaskProcess() {
        List<?> errorTopicTasks = restClient.getTasks(EXTERNAL_TASK_WORKER_ID, THROW_ERROR_2_TOPIC);
        List<Map<String, Object>> tasks = new ArrayList<>();
        tasks.addAll((Collection<? extends Map<String, Object>>) errorTopicTasks);


        if (tasks.isEmpty()) {
            return;
        }

        for (Map task : tasks) {
            String taskId = (String) task.get("id");
            System.out.println("Task " + taskId);
            processTask(taskId, externalTasks
                            .stream()
                            .filter(el -> el.getTopicName().equals(THROW_ERROR_2_TOPIC))
                            .findFirst().get(),
                    List.of());
        }
    }

    @Scheduled(fixedRate = 5000, scheduler = "flowablePool")
    public void throwErrorTaskProcess() {
        List<?> errorTopicTasks = restClient.getTasks(EXTERNAL_TASK_WORKER_ID, THROW_ERROR_TOPIC);
        List<Map<String, Object>> tasks = new ArrayList<>();
        tasks.addAll((Collection<? extends Map<String, Object>>) errorTopicTasks);


        if (tasks.isEmpty()) {
            return;
        }

        for (Map task : tasks) {
            String taskId = (String) task.get("id");
            System.out.println("Task " + taskId);
            processTask(taskId, externalTasks
                            .stream()
                            .filter(el -> el.getTopicName().equals(THROW_ERROR_TOPIC))
                            .findFirst().get(),
                    List.of(new FlowableVariable("subprocessProcess", "string", "FAIL")));
        }
    }

    @Scheduled(fixedRate = 5000, scheduler = "flowablePool")
    public void errorTaskProcess() {
        List<?> errorTopicTasks = restClient.getTasks(EXTERNAL_TASK_WORKER_ID, ERROR_TOPIC);
        List<Map<String, Object>> tasks = new ArrayList<>();
        tasks.addAll((Collection<? extends Map<String, Object>>) errorTopicTasks);


        if (tasks.isEmpty()) {
            return;
        }

        for (Map task : tasks) {
            String taskId = (String) task.get("id");
            System.out.println("Task " + taskId);
            processTask(taskId, externalTasks
                            .stream()
                            .filter(el -> el.getTopicName().equals(ERROR_TOPIC))
                            .findFirst().get(),
                    List.of());
        }
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
                    List.of(new FlowableVariable("result", "string", "FAIL")));
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

