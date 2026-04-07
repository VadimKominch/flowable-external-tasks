package by.example.process;

import by.example.config.ProcessTestContext;
import io.cucumber.java.en.*;
import org.flowable.engine.*;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.test.Deployment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.junit.jupiter.api.Assertions.*;

@Deployment(resources = "processes/externalTaskWithCorrelationExample.bpmn")
public class ProcessSteps {

    @Autowired private RuntimeService runtimeService;
    @Autowired private HistoryService historyService;
    @Autowired private ProcessEngineConfiguration processEngineConfiguration;
    @Autowired private ProcessTestContext context;

    @Given("process is started")
    public void start_process() {
        processEngineConfiguration.getAsyncExecutor().shutdown();

        ProcessInstance pi = runtimeService
                .startProcessInstanceByKey("externalTaskWithCorrelationExample");

        context.setProcessInstance(pi);
    }

    @When("I complete task {string} with result {string}")
    public void complete_task_with_result(String activityId, String result) {
        var execution = findExecution(activityId);

        runtimeService.setVariable(execution.getId(), "result", result);
        runtimeService.trigger(execution.getId());
    }

    @When("I complete task {string} with variable {string} = {string}")
    public void complete_task_with_variable(String activityId, String var, String value) {
        var execution = findExecution(activityId);

        runtimeService.setVariable(execution.getId(), var, value);
        runtimeService.trigger(execution.getId());
    }

    @When("I trigger task {string}")
    public void trigger_task(String activityId) {
        var execution = findExecution(activityId);
        runtimeService.trigger(execution.getId());
    }

    @Then("process should be finished")
    public void process_should_be_finished() {
        String processId = context.getProcessInstance().getId();

        var runtime = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processId)
                .singleResult();

        var history = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(processId)
                .singleResult();

        assertNull(runtime, "Process should be finished");
        assertNotNull(history, "Process should exist in history");
    }

    // helper
    private org.flowable.engine.runtime.Execution findExecution(String activityId) {
        return runtimeService.createExecutionQuery()
                .processInstanceId(context.getProcessInstance().getId())
                .activityId(activityId)
                .singleResult();
    }
}
