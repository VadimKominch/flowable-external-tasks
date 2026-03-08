package by.example.service;

import by.example.controller.dto.ProcessViewResponse;
import org.flowable.bpmn.converter.BpmnXMLConverter;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProcessViewService {

    private final RuntimeService runtimeService;
    private final RepositoryService repositoryService;

    public ProcessViewService(RuntimeService runtimeService, RepositoryService repositoryService) {
        this.runtimeService = runtimeService;
        this.repositoryService = repositoryService;
    }

    public ProcessViewResponse getByBusinessKey(String businessKey) {

        ProcessInstance instance =
                runtimeService.createProcessInstanceQuery()
                        .processInstanceBusinessKey(businessKey)
                        .singleResult();

        if (instance == null) {
            throw new RuntimeException("Process not found");
        }

        BpmnModel model =
                repositoryService.getBpmnModel(instance.getProcessDefinitionId());

        String xml = new BpmnXMLConverter()
                .convertToXML(model)
                .toString();

        List<String> activeActivities =
                runtimeService.getActiveActivityIds(instance.getId());

        return new ProcessViewResponse(xml, activeActivities);
    }
}
