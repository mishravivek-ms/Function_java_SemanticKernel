package com.aiaudit.function;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.microsoft.azure.functions.annotation.*;
import com.aiaudit.DTO.response;
import com.aiaudit.openai.kernelBuilder;
import com.microsoft.azure.functions.*;
import com.aiaudit.DTO.kernelRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Azure Functions with Timer trigger.
 */
public class auditTimeTrigger {
    /**
     * This function will be invoked periodically according to the specified schedule.
     */
    @FunctionName("AuditTimeTrigger")
    public void run(
        @TimerTrigger(name = "timerInfo", schedule = "*/30 * * * * *") String timerInfo,
        @CosmosDBOutput(name = "Items",
              databaseName = "auditreport",
              containerName  = "Items",
              connection  = "CosmosDBConnectionString")
            OutputBinding<String> outputItem,
        final ExecutionContext context
    ) throws IOException {
        context.getLogger().info("Java Timer trigger function executed at: " + LocalDateTime.now());

        kernelBuilder kernelBuilder = new kernelBuilder();
        kernelRequest kernelrequest = new kernelRequest();
        kernelrequest.setMRN_number("123456");
        kernelrequest.setDocument_name("sample.pdf");

        response result=kernelBuilder.invokeOpenAI(kernelrequest);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, false);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String dtoJson = objectMapper.writeValueAsString(result);
        context.getLogger().info("Response from AI: "+dtoJson);
        outputItem.setValue(dtoJson);
        context.getLogger().info("Record updated into Database for : "+result.getId());
    }
}
