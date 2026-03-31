package by.example.process;

import org.flowable.engine.ManagementService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.test.Deployment;
import org.flowable.engine.test.FlowableTest;
import org.flowable.job.api.ExternalWorkerJob;
import org.flowable.spring.impl.test.FlowableSpringExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(FlowableSpringExtension.class)
@SpringBootTest
class SchemeWithMessageCorrelationTest {

    @Autowired
    private RuntimeService runtimeService;
    @Autowired private ManagementService managementService;

    @Test
    @Deployment(resources = "processes/externalTaskWithCorrelationExample.bpmn")
    void testFullProcessPath() {
        // 1. Start the process
        ProcessInstance pi = runtimeService.startProcessInstanceByKey("externalTaskWithCorrelationExample");

        // 2. Handle 'ExternalWorkerTask_3' (print-topic)
        // We find the job to ensure the process actually reached this node
        ExternalWorkerJob job1 = managementService.createExternalWorkerJobQuery()
                .processInstanceId(pi.getId())
                .elementId("ExternalWorkerTask_3")
                .singleResult();
        assertNotNull(job1, "Process should be at print-topic");

        runtimeService.setVariable(job1.getId(), "result", "Error");
        runtimeService
                .createExecutionQuery()
                .processInstanceId(pi.getId())
                .executionId(job1.getExecutionId())
                .singleResult();
        // Move the process forward. Since we can't use ExternalWorkerService,
        // we trigger the execution directly to simulate completion.
//        runtimeService.trigger(job1.getExecutionId(), Map.of("result", "DEFAULT"));

        // 3. Handle 'IntermediateMessageEventCatching_10'
        // Because result was not "SUCCESS" or "ERROR", it hits the default flow to the message catch
        Execution messageExecution = runtimeService.createExecutionQuery()
                .processInstanceId(pi.getId())
                .messageEventSubscriptionName("TestMessage")
                .singleResult();
        assertNotNull(messageExecution, "Process should be waiting for TestMessage");

        // Correlate the message (Simulating Service B)
        runtimeService.messageEventReceived("TestMessage", messageExecution.getId());

        // 4. Handle 'ExternalWorkerTask_4' (hello-topic)
        // After the message, it moves to Gateway 10 then hello-topic
        ExternalWorkerJob job2 = managementService.createExternalWorkerJobQuery()
                .processInstanceId(pi.getId())
//                .topic("hello-topic")
                .singleResult();
        assertNotNull(job2, "Process should have reached hello-topic");

        runtimeService.trigger(job2.getExecutionId());
//    managementService.createExternalWorkerCompletionBuilder().complete();
        // 5. Verify it entered the Subprocess
        // The process should now have an active task inside the 'Error subprocess'
        long subProcessTasks = managementService.createExternalWorkerJobQuery()
                .processInstanceId(pi.getId())
                .count();
        // It should be 2 because you have a Parallel Gateway inside the subprocess
        assertEquals(2, subProcessTasks, "Subprocess should have started two parallel external tasks");
    }
}