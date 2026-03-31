package by.example.process;

import org.flowable.engine.HistoryService;
import org.flowable.engine.ManagementService;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.test.Deployment;
import org.flowable.job.api.AcquiredExternalWorkerJob;
import org.flowable.job.api.ExternalWorkerJob;
import org.flowable.spring.impl.test.FlowableSpringExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(FlowableSpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class SchemeWithMessageCorrelationTest {

    @Autowired
    private RuntimeService runtimeService;
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
    void testFullProcessPath() {
        // 1. Start the process
        ProcessInstance pi = runtimeService.startProcessInstanceByKey("externalTaskWithCorrelationExample");

        // 2. Acquire and complete ExternalWorkerTask_3 (print-topic)
        List<AcquiredExternalWorkerJob> acquiredJobs = managementService
                .createExternalWorkerJobAcquireBuilder()
                .topic("print-topic", Duration.ofSeconds(1))
                .acquireAndLock(1, "test-worker");

        assertFalse(acquiredJobs.isEmpty(), "Process should be at print-topic");

        // Use the acquired job's ID directly — no need for a separate query
        managementService.createExternalWorkerCompletionBuilder(acquiredJobs.get(0).getId(), "test-worker")
                .variable("result", "ERROR")
                .complete();

        waitForExternalJob(pi.getId(), "error-topic");

        // 3. Acquire and complete ExternalWorkerTask_20 (error-topic)
        List<AcquiredExternalWorkerJob> acquiredJobs2 = managementService
                .createExternalWorkerJobAcquireBuilder()
                .topic("error-topic", Duration.ofSeconds(10))
                .acquireAndLock(1, "test-worker-2");

        assertFalse(acquiredJobs2.isEmpty(), "Process should be at error-topic");

        managementService.createExternalWorkerCompletionBuilder(acquiredJobs2.get(0).getId(), "test-worker-2")
                .complete();

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

    private void waitForExternalJob(String processInstanceId, String topic) {
        long timeout = 30000; // 5 seconds
        long interval = 200;
        long elapsed = 0;

        while (elapsed < timeout) {
            ExternalWorkerJob job = managementService.createExternalWorkerJobQuery()
                    .processInstanceId(processInstanceId)
                    .singleResult();

            if (job != null && topic.equals(job.getJobHandlerConfiguration())) {
                return; // job is ready
            }

            try {
                Thread.sleep(interval);
                elapsed += interval;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Interrupted while waiting for job", e);
            }
        }

        throw new AssertionError("Timed out waiting for external job on topic: " + topic);
    }
}