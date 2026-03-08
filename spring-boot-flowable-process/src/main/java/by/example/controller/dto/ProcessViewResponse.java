package by.example.controller.dto;

import java.util.List;

public record ProcessViewResponse(String bpmnXml, List<String> activeActivities) {

}
