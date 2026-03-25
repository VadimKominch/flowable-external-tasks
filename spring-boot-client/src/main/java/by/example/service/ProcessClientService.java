package by.example.service;

import by.example.process.client.FlowableRestClient;
import by.example.process.client.dto.FlowableVariable;
import by.example.process.client.dto.StartProcessInstanceVariable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProcessClientService {
    public static final String EXTERNAL_TASK_DEFINITION_KEY = "externalTaskWithCorrelationExample";
    private final FlowableRestClient restClient;

    public ProcessClientService(FlowableRestClient restClient) {
        this.restClient = restClient;
    }

    public String startProcess(String businessKey) {
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

    public void completeTask(String workerId, String taskId, List<FlowableVariable> variables) {
        restClient.complete(workerId, taskId, variables);
    }

    public void failTask(String workerId, String taskId, List<FlowableVariable> variables, int remainingRetries) {
        restClient.fail(workerId, taskId, variables, remainingRetries);
    }

    public List<?> getTasks(String workerId, String topicName) {
        return restClient.getTasks(workerId, topicName);
    }

    public StartProcessInstanceVariable getVariable(String processId, String variableName) {
        return restClient
                .getVariables(processId)
                .stream()
                .filter(el -> el.name().equals(variableName))
                .findFirst()
                .orElse(null);
    }
}

