package by.example.config;

import io.cucumber.spring.CucumberContextConfiguration;
import org.flowable.spring.impl.test.FlowableSpringExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@CucumberContextConfiguration
@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(FlowableSpringExtension.class)
public class CucumberSpringConfig {
}
