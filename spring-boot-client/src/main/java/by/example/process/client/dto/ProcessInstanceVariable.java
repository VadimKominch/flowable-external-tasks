package by.example.process.client.dto;

public record ProcessInstanceVariable(
        String name,
        String type,
        Object value
) {}
