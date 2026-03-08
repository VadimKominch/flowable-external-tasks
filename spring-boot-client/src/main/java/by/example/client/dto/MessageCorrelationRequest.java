package by.example.client.dto;

import java.util.List;

public record MessageCorrelationRequest(
        String action,
        String messageName,
        List<FlowableVariable> variables
){
     public static MessageCorrelationRequest messageReceived(
             String messageName,
             List<FlowableVariable> variables) {

         return new MessageCorrelationRequest(
                 "messageEventReceived", messageName, variables);
     }
}
