package com.aiaudit.function;

import com.aiaudit.DTO.response;
import com.microsoft.azure.functions.*;
import com.microsoft.azure.functions.annotation.*;

import java.util.List;
import java.util.Optional;

public class auditassessmentreportGet {
  @FunctionName("AuditassessmentreportGet")
  public HttpResponseMessage run(
          @HttpTrigger(name = "req", methods = {HttpMethod.GET}, authLevel = AuthorizationLevel.ANONYMOUS) 
          HttpRequestMessage<Optional<String>> request,
          @CosmosDBInput(name = "Items",
                  databaseName = "auditreport",
                  containerName  = "Items",
                  sqlQuery = "SELECT * FROM c",
                  connection  = "CosmosDBConnectionString")
          List<response> items,
          final ExecutionContext context) {

      context.getLogger().info("Retrieved items from Cosmos DB.");

      if (items.isEmpty()) {
          return request.createResponseBuilder(HttpStatus.NOT_FOUND)
                        .body("No items found.")
                        .build();
      } else {
          return request.createResponseBuilder(HttpStatus.OK)
                        .header("Content-Type", "application/json")
                        .body(items)
                        .build();
      }
  }
}
