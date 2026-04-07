package by.example.controller;

import by.example.controller.dto.ProcessCorrelateRequest;
import by.example.process.client.dto.ProcessInstanceVariable;
import by.example.process.entity.ProcessDefinitionKey;
import by.example.service.ProcessClientService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
public class ProcessController {

    private final ProcessClientService processClientService;

    public ProcessController(ProcessClientService processClientService) {
        this.processClientService = processClientService;
    }

    @PostMapping
    public String startProcess(@RequestParam(name = "name") String key) {
        String businessKey = UUID.randomUUID().toString();
        List<ProcessInstanceVariable> vars = new ArrayList<>();
        vars.add(new ProcessInstanceVariable("firstEnabled", "boolean", true));
        vars.add(new ProcessInstanceVariable("secondEnabled", "boolean", false));
        return processClientService.startProcess(ProcessDefinitionKey.valueOf(key), businessKey, vars);
    }

    @PostMapping("/correlate")
    public String correlateProcess(@RequestBody ProcessCorrelateRequest request) {
        processClientService.correlateMessage(request.businessKey(), request.messageName());
        return request.businessKey();
    }
}
