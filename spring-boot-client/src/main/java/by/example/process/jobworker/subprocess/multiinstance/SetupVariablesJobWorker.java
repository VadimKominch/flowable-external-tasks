package by.example.process.jobworker.subprocess.multiinstance;

import by.example.process.client.dto.JobDto;
import by.example.process.client.dto.ProcessInstanceVariable;
import by.example.process.jobworker.AbstractTask;
import by.example.process.jobworker.ExternalTask;
import by.example.service.ProcessClientService;

import java.util.List;
import java.util.Set;

@ExternalTask(topic = "set-up-variables-topic")
public class SetupVariablesJobWorker extends AbstractTask {

    public SetupVariablesJobWorker(ProcessClientService processService) {
        super(processService);
    }

    @Override
    public List<ProcessInstanceVariable> execute(JobDto job) {
        ProcessInstanceVariable multiinstanceVariables = new ProcessInstanceVariable("external_user_ids", "json", List.of("id_1", "id_2", "id_3"));
        System.out.println("multiinstanceVariables: " + multiinstanceVariables);
        return List.of(multiinstanceVariables);
    }
}
