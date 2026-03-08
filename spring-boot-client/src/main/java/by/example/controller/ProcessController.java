package by.example.controller;

import by.example.controller.dto.ProcessCorrelateRequest;
import by.example.service.ProcessClientService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProcessController {

    private final ProcessClientService processClientService;

    public ProcessController(ProcessClientService processClientService) {
        this.processClientService = processClientService;
    }

    @PostMapping
    public String startProcess() {
        return processClientService.startProcess();
    }

    @PostMapping("/correlate")
    public String correlateProcess(@RequestBody ProcessCorrelateRequest request) {
        processClientService.correlateMessage(request.businessKey(), request.messageName());
        return request.businessKey();
    }
}
