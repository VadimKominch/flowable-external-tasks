package by.example.controller;

import by.example.controller.dto.ProcessViewResponse;
import by.example.service.ProcessViewService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/process")
public class ProcessController {

    private final ProcessViewService service;

    public ProcessController(ProcessViewService service) {
        this.service = service;
    }

    @GetMapping("/{businessKey}")
    public ProcessViewResponse getProcess(
            @PathVariable String businessKey) {

        return service.getByBusinessKey(businessKey);
    }
}
