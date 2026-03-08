package by.example.client.dto;

import java.util.List;

public record ProcessInstanceQueryResponse (
        String url, String total, List<ProcessInstance> data
){
    public record ProcessInstance (
         String id,
         String url,
         String businessKey,
         String processDefinitionId,
         String processDefinitionName,
         String processDefinitionKey,
         String processDefinitionCategory,
         int processDefinitionVersion,
         String deploymentId,
         String rootActivityId,
         boolean suspended,
         String tenantId,
         String startUserId,
         String startTime,
         String superProcessInstanceId
    ) {}
}
