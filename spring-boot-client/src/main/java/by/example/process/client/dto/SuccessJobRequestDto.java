package by.example.process.client.dto;

import java.util.List;

public record SuccessJobRequestDto(String workerId, List<ProcessInstanceVariable> variables) {}
