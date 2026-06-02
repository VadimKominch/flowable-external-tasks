package by.example.process.jobworker.subprocess.multiinstance;

import by.example.process.client.dto.JobDto;
import by.example.process.client.dto.ProcessInstanceVariable;
import by.example.process.jobworker.AbstractTask;
import by.example.process.jobworker.ExternalTask;
import by.example.service.ProcessClientService;

import java.util.List;
import java.util.Map;

@ExternalTask(topic = "print-results-topic")
public class PrintResultsJobWorker extends AbstractTask {

    public PrintResultsJobWorker(ProcessClientService processService) {
        super(processService);
    }

    @Override
    public List<ProcessInstanceVariable> execute(JobDto job) {
        List<Map<String, Object>> value = getExecutionVariable(job, "process_result");
        List<String> results = value.stream()
                .map(entry -> (String) entry.get("result"))  // extract value by key name
                .toList();

        System.out.println("process result is " + results);
        return List.of();
    }
}
