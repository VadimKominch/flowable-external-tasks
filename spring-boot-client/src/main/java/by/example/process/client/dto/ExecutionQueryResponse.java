package by.example.process.client.dto;

import java.util.List;

public record ExecutionQueryResponse(List<ExecutionDto> data) {
     public record ExecutionDto(
             String id,
             String processInstanceId,
             String activityId
     ) {}
}
