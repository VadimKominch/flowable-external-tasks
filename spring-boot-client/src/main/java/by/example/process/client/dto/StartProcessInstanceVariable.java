package by.example.process.client.dto;

public record StartProcessInstanceVariable(
        String name,
        String type,
        Object value
) {}
