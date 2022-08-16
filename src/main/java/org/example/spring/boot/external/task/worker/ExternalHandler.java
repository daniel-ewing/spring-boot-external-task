package org.example.spring.boot.external.task.worker;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.Variables;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@ExternalTaskSubscription("external") // create a subscription for this topic name
@Slf4j
public class ExternalHandler implements ExternalTaskHandler {

    @Override
    public void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {
        if (log.isDebugEnabled()) log.debug("-----> execute: Enter");

        // Get variables
        String nextTaskName = externalTask.getVariable("nextTaskName");
        // only for the sake of this demonstration, we generate random data
        // in a real-world scenario, we would load the data from a database
        String customerId = "C-" + UUID.randomUUID().toString().substring(32);
        int creditScore = (int) (Math.random() * 11);

        VariableMap variables = Variables.createVariables();
        variables.put("customerId", customerId);
        variables.put("creditScore", creditScore);

        // complete the external task
        externalTaskService.complete(externalTask, variables);

        if (log.isDebugEnabled()) log.debug( "Credit score {} for customer {} provided at {}!", creditScore, customerId, nextTaskName);
        if (log.isDebugEnabled()) log.debug("-----> execute: Exit");
    }
}