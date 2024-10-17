package com.aiaudit.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.semantickernel.orchestration.FunctionResult;
import com.microsoft.semantickernel.semanticfunctions.HandlebarsPromptTemplateFactory;
import com.microsoft.semantickernel.semanticfunctions.KernelFunction;
import com.microsoft.semantickernel.semanticfunctions.KernelFunctionYaml;
import com.aiaudit.DTO.answer;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class kernelUtil {
    public static String getJsonString(FunctionResult<InputStream> apiresultValue) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (InputStream inputStream = apiresultValue.getResult()) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        inputStreamWrapper inputStreamWrapper = new inputStreamWrapper(byteArrayOutputStream.toByteArray());
        ObjectMapper objectMapper = new ObjectMapper();
        String json = null;
        try {
            json = objectMapper.writeValueAsString(inputStreamWrapper);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return json;
    }

    public KernelFunction getPromptData(String filepath) {

        ClassLoader classLoader = getClass().getClassLoader();
        StringBuilder yamlStringBuilder = new StringBuilder();

        try (InputStream inputStream = classLoader.getResourceAsStream(filepath);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

            String line;
            while ((line = reader.readLine()) != null) {
                yamlStringBuilder.append(line).append("\n");  // Append newline to preserve formatting
            }

            return KernelFunctionYaml.fromPromptYaml(yamlStringBuilder.toString(),
                    new HandlebarsPromptTemplateFactory());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public answer getAnswerReply(FunctionResult reply, ObjectMapper objectMapper) throws JsonProcessingException {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonreply= (String) reply.getResult();
        jsonreply=jsonreply.replaceAll("```json", "").replaceAll("```", "");
        answer document = objectMapper.readValue(jsonreply, answer.class);
        System.out.println(reply.getResult());
        return document;
    }
}
