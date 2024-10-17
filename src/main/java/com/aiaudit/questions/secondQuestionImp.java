package com.aiaudit.questions;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.aiservices.openai.chatcompletion.OpenAIChatCompletion;
import com.microsoft.semantickernel.orchestration.FunctionResult;
import com.microsoft.semantickernel.plugin.KernelPlugin;
import com.microsoft.semantickernel.plugin.KernelPluginFactory;
import com.microsoft.semantickernel.semanticfunctions.KernelFunction;
import com.microsoft.semantickernel.semanticfunctions.KernelFunctionArguments;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import com.aiaudit.DTO.answer;
import com.aiaudit.plugin.secondquestionplugin.recordPlugin;
import com.aiaudit.util.kernelUtil;

import java.io.IOException;

public class secondQuestionImp {

    public static answer invokeOpenAI(Kernel kernel,OpenAIAsyncClient client) throws IOException {

    KernelPlugin RecordPlugin = KernelPluginFactory.createFromObject(new recordPlugin("http://localhost:57397/api/sendquestionAPI"),
                "ReadAuditRecord");

        ChatCompletionService chatCompletionService = OpenAIChatCompletion.builder()
                .withModelId(System.getenv("openai_model"))
                .withOpenAIAsyncClient(client)
                .build();

        kernel = Kernel.builder()
                .withAIService(ChatCompletionService.class, chatCompletionService)
                .withPlugin(RecordPlugin)
                .build();


        FunctionResult<String> apiresultValue = null;
        try {
            KernelFunctionArguments arguments = KernelFunctionArguments.builder()
                    .withInput("http://localhost:57397/api/sendquestionAPI")
                    .build();

            apiresultValue = kernel.invokeAsync(
                            RecordPlugin.<String>get("ReadAuditRecord"))
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
