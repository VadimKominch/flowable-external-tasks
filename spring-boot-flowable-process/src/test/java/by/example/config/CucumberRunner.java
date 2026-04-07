package by.example.config;

import org.junit.platform.suite.api.*;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
@ConfigurationParameter(
        key = "cucumber.glue",
        value = "by.example.process"
)
public class CucumberRunner {
}
