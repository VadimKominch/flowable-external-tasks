package by.example.process;

import org.flowable.engine.*;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.test.Deployment;
import org.flowable.spring.impl.test.FlowableSpringExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(FlowableSpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class SchemeWithMessageCorrelationTest {

    @Autowired private RuntimeService runtimeService;
    @Autowired private ManagementService managementService;
    @Autowired private HistoryService historyService;
    @Autowired private ProcessEngineConfiguration processEngineConfiguration;

    @BeforeEach
    void disableAsyncExecutor() {
        processEngineConfiguration.getAsyncExecutor().shutdown();
    }

    @Test
    @Transactional
    @Deployment(resources = "processes/externalTaskWithCorrelationExample.bpmn")
    void testFullErrorProcessPath() {
        // 1. Start the process
        ProcessInstance pi = runtimeService.startProcessInstanceByKey("externalTaskWithCorrelationExample");

        var printJob = runtimeService
                .createExecutionQuery()
                .processInstanceId(pi.getId())
                .activityId("ExternalWorkerTask_3")
                .singleResult();

        // Use the acquired printJob's ID directly — no need for a separate query
        runtimeService.setVariable(printJob.getId(), "result", "ERROR");
        runtimeService.trigger(printJob.getId());

        var errorTask = runtimeService
                .createExecutionQuery()
                .processInstanceId(pi.getId())
                .activityId("ExternalWorkerTask_20")
                .singleResult();

        runtimeService.trigger(errorTask.getId());

        // 4. Verify process completed
        ProcessInstance runningProcessInst = runtimeService.createProcessInstanceQuery()
                .processInstanceId(pi.getId())
                .singleResult();
        var historyProcessInst = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(pi.getId())
                .singleResult();

        assertNull(runningProcessInst, "Process should be finished");
        assertNotNull(historyProcessInst, "Process should exist in history");
    }

    @Test
    @Transactional
    @Deployment(resources = "processes/externalTaskWithCorrelationExample.bpmn")
    void testFullProcessPath() {
        // 1. Start the process
        ProcessInstance pi = runtimeService.startProcessInstanceByKey("externalTaskWithCorrelationExample");

        var printJob = runtimeService
                .createExecutionQuery()
                .processInstanceId(pi.getId())
                .activityId("ExternalWorkerTask_3")
                .singleResult();

        // Use the acquired printJob's ID directly — no need for a separate query
        runtimeService.setVariable(printJob.getId(), "result", "SUCCESS");
        runtimeService.trigger(printJob.getId());

        var errorTask = runtimeService
                .createExecutionQuery()
                .processInstanceId(pi.getId())
                .activityId("ExternalWorkerTask_4")
                .singleResult();

        runtimeService.setVariable(errorTask.getId(), "subprocessProcess", "SUCCESS");
        runtimeService.trigger(errorTask.getId());

        var throw2Task = runtimeService
                .createExecutionQuery()
                .processInstanceId(pi.getId())
                .activityId("ExternalWorkerTask_32")
                .singleResult();

        runtimeService.trigger(throw2Task.getId());

        var throwTask = runtimeService
                .createExecutionQuery()
                .processInstanceId(pi.getId())
                .activityId("ExternalWorkerTask_38")
                .singleResult();

        runtimeService.trigger(throwTask.getId());

        // 4. Verify process completed
        ProcessInstance runningProcessInst = runtimeService.createProcessInstanceQuery()
                .processInstanceId(pi.getId())
                .singleResult();
        var historyProcessInst = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(pi.getId())
                .singleResult();

        assertNull(runningProcessInst, "Process should be finished");
        assertNotNull(historyProcessInst, "Process should exist in history");
    }

    @Test
    @Transactional
    @Deployment(resources = "processes/externalTaskWithCorrelationExample.bpmn")
    void testFullMessageProcessPath() {
        // 1. Start the process
        ProcessInstance pi = runtimeService.startProcessInstanceByKey("externalTaskWithCorrelationExample");

        var printJob = runtimeService
                .createExecutionQuery()
                .processInstanceId(pi.getId())
                .activityId("ExternalWorkerTask_3")
                .singleResult();

        // Use the acquired printJob's ID directly — no need for a separate query
        runtimeService.setVariable(printJob.getId(), "result", "FAILURE");
        runtimeService.trigger(printJob.getId());

        var messageEvent = runtimeService
                .createExecutionQuery()
                .processInstanceId(pi.getId())
                .activityId("IntermediateMessageEventCatching_10")
                .singleResult();

        runtimeService.trigger(messageEvent.getId());

        var throw2Task = runtimeService
                .createExecutionQuery()
                .processInstanceId(pi.getId())
                .activityId("ExternalWorkerTask_32")
                .singleResult();

        runtimeService.trigger(throw2Task.getId());

        var throwTask = runtimeService
                .createExecutionQuery()
                .processInstanceId(pi.getId())
                .activityId("ExternalWorkerTask_38")
                .singleResult();

        runtimeService.setVariable(throwTask.getId(), "subprocessProcess", "SUCCESS");

        runtimeService.trigger(throwTask.getId());

        // 4. Verify process completed
        ProcessInstance runningProcessInst = runtimeService.createProcessInstanceQuery()
                .processInstanceId(pi.getId())
                .singleResult();
        var historyProcessInst = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(pi.getId())
                .singleResult();

        assertNull(runningProcessInst, "Process should be finished");
        assertNotNull(historyProcessInst, "Process should exist in history");
    }
}