package by.example.client;

import by.example.client.dto.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

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

        Map<String, Object> request = Map.of(
                "processDefinitionKey", processDefinitionKey,
                "businessKey", businessKey
        );

        restClient.post()
                .uri("/runtime/process-instances")
                .body(request)
                .retrieve()
                .body(String.class);
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
