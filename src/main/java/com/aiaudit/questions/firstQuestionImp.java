package com.aiaudit.questions;

import com.azure.ai.openai.OpenAIAsyncClient;
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
import com.microsoft.semantickernel.semanticfunctions.KernelFunction;
import com.microsoft.semantickernel.semanticfunctions.KernelFunctionArguments;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import com.aiaudit.DTO.agreementDTO;
import com.aiaudit.DTO.answer;
import com.aiaudit.plugin.firstquestionplugin.conversationSummaryPlugin;
import com.aiaudit.plugin.firstquestionplugin.agreementPlugin;
import com.aiaudit.plugin.firstquestionplugin.documentReaderPlugin;
import com.aiaudit.util.kernelUtil;

import java.io.IOException;
import java.io.InputStream;

public class firstQuestionImp {


    public static answer invokeOpenAI(Kernel kernel,OpenAIAsyncClient client) throws IOException {



        KernelPlugin agreementAPIPlugin = KernelPluginFactory.createFromObject(new agreementPlugin("http://localhost:57397/api/readPdfFromBlob"),
                "agreementPlugin");

        KernelPlugin DocumentReaderPlugin = KernelPluginFactory.createFromObject(new documentReaderPlugin(System.getenv("documentintent_url"),System.getenv("documentintent_key")),
                "DocumentReaderPlugin");


        // Create few-shot examples

        // Customise the type converters toPromptString for ChatHistory to serialize the messages as "author: content"
        CollectionVariableContextVariableTypeConverter collectionConverter = new CollectionVariableContextVariableTypeConverter(
                "\n");

        ChatCompletionService chatCompletionService = OpenAIChatCompletion.builder()
                .withModelId(System.getenv("openai_model"))
                .withOpenAIAsyncClient(client)
                .build();


        kernel = Kernel.builder()
                .withAIService(ChatCompletionService.class, chatCompletionService)
                .withPlugin(agreementAPIPlugin)
                .withPlugin(DocumentReaderPlugin)
                .build();

        InvocationContext invocationContext = new InvocationContext.Builder()
                .withReturnMode(InvocationReturnMode.LAST_MESSAGE_ONLY)
                .withToolCallBehavior(ToolCallBehavior.allowAllKernelFunctions(true))
                .build();

        FunctionResult<InputStream> apiresultValue = null;
        try {
            KernelFunctionArguments arguments = KernelFunctionArguments.builder()
                    .withInput("optumSample_sign.pdf")
                    .build();

            apiresultValue = kernel.invokeAsync(
                            agreementAPIPlugin.<InputStream>get("ReadAgreementfromLocal"))
                    .withArguments(arguments)
                    .block();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        String json = kernelUtil.getJsonString(apiresultValue);
        FunctionResult<agreementDTO> resultValue = null;
        try {
            KernelFunctionArguments arguments = KernelFunctionArguments.builder()
                    .withInput(json)
                    .build();

            resultValue = kernel.invokeAsync(
                            DocumentReaderPlugin.<agreementDTO>get("ReadDocument"))
                    .withArguments(arguments)
                    .block();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        agreementDTO dto=resultValue.getResult();

        ObjectMapper objectMapper = new ObjectMapper();
        String dtoJson = objectMapper.writeValueAsString(dto);


        kernelUtil util=new kernelUtil();
        KernelFunction prompt=util.getPromptData("Prompts/firstquestion.prompt.yaml");

        KernelFunctionArguments arguments =KernelFunctionArguments.builder()
                .withVariable("request",dtoJson)
                .build();

        FunctionResult reply = (FunctionResult) kernel.invokeAsync(prompt)
                .withArguments(arguments)
                .withResultType(String.class)
                .block();

        answer document = util.getAnswerReply(reply, objectMapper);
        return document;
    }


}
