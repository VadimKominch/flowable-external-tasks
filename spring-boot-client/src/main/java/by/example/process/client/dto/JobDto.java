package by.example.process.client.dto;

import java.time.OffsetDateTime;
import java.util.List;

public record JobDto(
    String id,
    String url,
    String correlationId,
    String processInstanceId,
    String processDefinitionId,
    String executionId,
    String scopeId,
    String subScopeId,
    String scopeDefinitionId,
    String scopeType,
    String elementId,
    String elementName,
    int retries,
    String exceptionMessage,
    OffsetDateTime dueDate,
    OffsetDateTime createTime,
    String tenantId,
    String lockOwner,
    OffsetDateTime lockExpirationTime,
    List<ProcessInstanceVariable> variables) {}
