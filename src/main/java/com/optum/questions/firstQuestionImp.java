package com.optum.questions;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.core.credential.AzureKeyCredential;
import com.fasterxml.jackson.core.JsonProcessingException;
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
import com.optum.util.kernelUtil;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class firstQuestionImp {


    public static answer invokeOpenAI(Kernel kernel,OpenAIAsyncClient client) throws IOException {



        KernelPlugin agreementAPIPlugin = KernelPluginFactory.createFromObject(new agreementPlugin("http://localhost:57614/api/readPdfFromBlob"),
                "agreementPlugin");

        KernelPlugin DocumentReaderPlugin = KernelPluginFactory.createFromObject(new documentReaderPlugin(System.getenv("documentintent_url"),System.getenv("documentintent_key")),
                "DocumentReaderPlugin");

        KernelPlugin ConversationSummaryPlugin = KernelPluginFactory.createFromObject(new ConversationSummaryPlugin(),
                "ConversationSummaryPlugin");

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
                .withPlugin(ConversationSummaryPlugin)
                .build();

        InvocationContext invocationContext = new InvocationContext.Builder()
                .withReturnMode(InvocationReturnMode.LAST_MESSAGE_ONLY)
                .withToolCallBehavior(ToolCallBehavior.allowAllKernelFunctions(true))
                .build();

        FunctionResult<InputStream> apiresultValue = null;
        try {
            KernelFunctionArguments arguments = KernelFunctionArguments.builder()
                    .withInput("http://localhost:57614/api/readPdfFromBlob")
                    .build();

            apiresultValue = kernel.invokeAsync(
                            agreementAPIPlugin.<InputStream>get("ReadAgreement"))
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
        //System.out.println(resultValue.getResult());
        ObjectMapper objectMapper = new ObjectMapper();
        String dtoJson = objectMapper.writeValueAsString(dto);
        // Create a final AI call OPENAI
        // Load prompts
        /*var prompts = KernelPluginFactory.importPluginFromDirectory(
                Path.of(PLUGINS_DIR), "Prompts", null);

        // <LoadPromptFromYAML>
        var getIntent = KernelFunctionYaml.fromPromptYaml(
                Files.readString(Path.of(PLUGINS_DIR, "Prompts", "getIntent.prompt.yaml")),
                new HandlebarsPromptTemplateFactory());
        // </LoadPromptFromYAML>


        KernelFunctionArguments arguments =KernelFunctionArguments.builder()
                .withVariable("request",dtoJson)
                .build();

        var reply = kernel.invokeAsync(prompts.get("Chat"))
                .withArguments(arguments)
                .withResultType(String.class)
                .block().getResult();*/

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
