package com.optum.function;

import com.fasterxml.jackson.databind.JsonNode;
import com.microsoft.azure.functions.annotation.*;
import com.optum.DTO.response;
import com.optum.openai.KernelBuilder;
import com.microsoft.azure.functions.*;
import com.optum.DTO.KernelRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Azure Functions with Timer trigger.
 */
public class nurseAuditTimeTrigger {
    /**
     * This function will be invoked periodically according to the specified schedule.
     */
    @FunctionName("nurseAuditTimeTrigger")
    public void run(
        @TimerTrigger(name = "timerInfo", schedule = "*/60 * * * * *") String timerInfo,
        /*@CosmosDBOutput(name = "Items",
              databaseName = "ToDoList",
              containerName  = "Items",
              connection  = "CosmosDBConnectionString")
            OutputBinding<String> outputItem,*/
        final ExecutionContext context
    ) throws IOException {
        context.getLogger().info("Java Timer trigger function executed at: " + LocalDateTime.now());

        /*String localRoot = System.getenv("AzureWebJobsScriptRoot");
        String azureRoot = System.getenv("HOME") + "/site/wwwroot";
        String functionAppRoot = localRoot != null ? localRoot : azureRoot;*/

        KernelBuilder kernelBuilder = new KernelBuilder();
        KernelRequest kernelrequest = new KernelRequest();
        kernelrequest.setMRN_number("123456");
        kernelrequest.setDocument_name("sample.pdf");

        response result=kernelBuilder.invokeOpenAI(kernelrequest);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, false);
        String dtoJson = objectMapper.writeValueAsString(result);
        context.getLogger().info("Response from AI: "+dtoJson);
    }
}
