package com.optum.questions;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.core.credential.AzureKeyCredential;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.aiservices.openai.chatcompletion.OpenAIChatCompletion;
import com.microsoft.semantickernel.contextvariables.converters.CollectionVariableContextVariableTypeConverter;
import com.microsoft.semantickernel.orchestration.FunctionResult;
import com.microsoft.semantickernel.orchestration.InvocationContext;
import com.microsoft.semantickernel.orchestration.InvocationReturnMode;
import com.microsoft.semantickernel.orchestration.ToolCallBehavior;
import com.microsoft.semantickernel.plugin.KernelPlugin;
import com.microsoft.semantickernel.plugin.KernelPluginFactory;
import com.microsoft.semantickernel.semanticfunctions.HandlebarsPromptTemplateFactory;
import com.microsoft.semantickernel.semanticfunctions.KernelFunction;
import com.microsoft.semantickernel.semanticfunctions.KernelFunctionArguments;
import com.microsoft.semantickernel.semanticfunctions.KernelFunctionYaml;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import com.optum.DTO.agreementDTO;
import com.optum.DTO.answer;
import com.optum.plugin.firstquestionplugin.ConversationSummaryPlugin;
import com.optum.plugin.firstquestionplugin.agreementPlugin;
import com.optum.plugin.firstquestionplugin.documentReaderPlugin;
import com.optum.plugin.secondquestionplugin.nurseRecordPlugin;
import com.optum.util.kernelUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;

public class secondQuestionImp {

    public static answer invokeOpenAI(Kernel kernel,OpenAIAsyncClient client) throws IOException {

    KernelPlugin nurseRecordPlugin = KernelPluginFactory.createFromObject(new nurseRecordPlugin("http://localhost:57614/api/sendquestionAPI"),
                "ReadNurseRecord");

        ChatCompletionService chatCompletionService = OpenAIChatCompletion.builder()
                .withModelId(System.getenv("openai_model"))
                .withOpenAIAsyncClient(client)
                .build();

        kernel = Kernel.builder()
                .withAIService(ChatCompletionService.class, chatCompletionService)
                .withPlugin(nurseRecordPlugin)
                .build();


        FunctionResult<String> apiresultValue = null;
        try {
            KernelFunctionArguments arguments = KernelFunctionArguments.builder()
                    .withInput("http://localhost:57614/api/sendquestionAPI")
                    .build();

            apiresultValue = kernel.invokeAsync(
                            nurseRecordPlugin.<String>get("ReadNurseRecord"))
                    .withArguments(arguments)
                    .block();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        String json = apiresultValue.getResult();


        kernelUtil util=new kernelUtil();
        KernelFunction prompt=util.getPromptData("Prompts/secondquestion.prompt.yaml");

        KernelFunctionArguments arguments =KernelFunctionArguments.builder()
                .withVariable("request",json)
                .build();

        FunctionResult reply = (FunctionResult) kernel.invokeAsync(prompt)
                .withArguments(arguments)
                .withResultType(String.class)
                .block();
        ObjectMapper objectMapper = new ObjectMapper();
        answer document = util.getAnswerReply(reply, objectMapper);
        return document;
    }
}
