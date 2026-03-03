package by.example.client;

import by.example.client.dto.FetchAndLockRequest;
import by.example.client.dto.FlowableVariable;
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
        request.setLockDuration("PT10S");
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
}
