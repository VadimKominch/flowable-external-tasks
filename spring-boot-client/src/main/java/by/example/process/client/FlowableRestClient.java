package by.example.process.client;

import by.example.process.client.dto.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class FlowableRestClient {

    private final RestClient restClient;
    private final RestClient externalJobRestClient;

    public FlowableRestClient(
            @Qualifier("flowableRestClientBase") RestClient restClient,
            @Qualifier("flowableRestClientExternalJob") RestClient externalJobRestClient) {
        this.restClient = restClient;
        this.externalJobRestClient = externalJobRestClient;
    }

    public void startProcess(String processDefinitionKey, String businessKey) {

        List<StartProcessInstanceVariable> variables = new ArrayList<>(){{
            add(new StartProcessInstanceVariable("processId", "string", businessKey));
        }};
        Map<String, Object> request = Map.of(
                "processDefinitionKey", processDefinitionKey,
                "businessKey", businessKey,
                "variables", variables
        );

        var result = restClient.post()
                .uri("/runtime/process-instances")
                .body(request)
                .retrieve()
                .body(String.class);
        System.out.println(result);
    }

    public List<StartProcessInstanceVariable> getVariables(String processInstanceId) {
        return restClient.get()
                .uri("/runtime/process-instances/{processInstanceId}/variables", processInstanceId)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }

    public void setVariables(String processInstanceId, List<StartProcessInstanceVariable> variables) {
        restClient.post()
                .uri("/runtime/process-instances/{processInstanceId}/variables", processInstanceId)
                .body(variables)
                .retrieve()
                .toBodilessEntity();
    }

    public List<?> getTasks(String workerId, String topic) {
        FetchAndLockRequest request = new FetchAndLockRequest();
        request.setWorkerId(workerId);
        request.setLockDuration("PT30S");
        request.setTopic(topic);

        return externalJobRestClient.post()
                .uri("/acquire/jobs")
                .body(request)
                .retrieve()
                .body(List.class);
    }

    public void complete(String workerId, String taskId, List<FlowableVariable> variables) {
        externalJobRestClient.post()
                .uri("/acquire/jobs/{jobId}/complete", taskId)
                .body(Map.of("workerId", workerId,"variables", variables))
                .retrieve()
                .toBodilessEntity();
    }

    public void fail(String workerId, String taskId, List<FlowableVariable> variables, int remainingRetries) {
        externalJobRestClient.post()
                .uri("/acquire/jobs/{jobId}/fail", taskId)
                .body(Map.of("workerId", workerId,"variables", variables, "retries", remainingRetries))
                .retrieve()
                .toBodilessEntity();
    }

    public ProcessInstanceQueryResponse getProcessInstanceId(String businessKey) {
        return restClient.get()
                .uri("/runtime/process-instances","businessKey",businessKey)
                .retrieve()
                .body(ProcessInstanceQueryResponse.class);
    }

    public ExecutionQueryResponse getExecutions(String processInstanceId, String messageName) {
        return restClient.get()
            .uri(uriBuilder -> uriBuilder
                .path("/runtime/executions")
                .queryParam("processInstanceId", processInstanceId)
                .queryParam("messageEventSubscriptionName", messageName)
                .build())
            .retrieve()
            .body(ExecutionQueryResponse.class);
    }

    public void correlateMessage(String executionId, String messageName) {
        MessageCorrelationRequest request = MessageCorrelationRequest.messageReceived(messageName, List.of());
        restClient.put()
                .uri("/runtime/executions/{executionId}", executionId)
                .body(request)
                .retrieve()
                .body(String.class);
    }
}
