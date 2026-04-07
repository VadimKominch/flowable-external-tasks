Feature: External task process with correlation

  Scenario: Error path
    Given process is started
    When I complete task "ExternalWorkerTask_3" with result "ERROR"
    And I trigger task "ExternalWorkerTask_20"
    Then process should be finished

  Scenario: Success path
    Given process is started
    When I complete task "ExternalWorkerTask_3" with result "SUCCESS"
    And I complete task "ExternalWorkerTask_4" with variable "subprocessProcess" = "SUCCESS"
    And I trigger task "ExternalWorkerTask_32"
    And I trigger task "ExternalWorkerTask_38"
    Then process should be finished

  Scenario: Message path
    Given process is started
    When I complete task "ExternalWorkerTask_3" with result "FAILURE"
    And I trigger task "IntermediateMessageEventCatching_10"
    And I trigger task "ExternalWorkerTask_32"
    And I complete task "ExternalWorkerTask_38" with variable "subprocessProcess" = "SUCCESS"
    Then process should be finished