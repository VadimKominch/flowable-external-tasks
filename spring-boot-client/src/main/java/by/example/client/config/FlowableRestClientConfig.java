package by.example.client.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class FlowableRestClientConfig {

    @Bean("flowableRestClientBase")
    public RestClient flowableRestClientBase() {
        return RestClient.builder()
                .baseUrl("http://localhost:8080/process-api")
                .defaultHeaders(headers ->
                        headers.setBasicAuth("rest-admin", "test"))
                .build();
    }

    @Bean("flowableRestClientExternalJob")
    public RestClient flowableRestClientExternalJobs() {
        return RestClient.builder()
                .baseUrl("http://localhost:8080/external-job-api")
                .defaultHeaders(headers ->
                        headers.setBasicAuth("rest-admin", "test"))
                .build();
    }
}
