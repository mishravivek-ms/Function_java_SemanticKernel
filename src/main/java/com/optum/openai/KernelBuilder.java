package com.optum.openai;

import java.io.*;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.core.credential.AzureKeyCredential;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.aiservices.openai.chatcompletion.OpenAIChatCompletion;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import com.optum.DTO.KernelRequest;
import com.optum.DTO.answer;
import com.optum.DTO.response;
import com.optum.questions.firstQuestionImp;
import com.optum.questions.secondQuestionImp;

public class KernelBuilder {


    public static response invokeOpenAI(KernelRequest kernelrequest) throws IOException {
        OpenAIAsyncClient client = new OpenAIClientBuilder()
                .credential(new AzureKeyCredential(System.getenv("openai_key")))
                .endpoint(System.getenv("openai_url"))
                .buildAsyncClient();

        Kernel sKernel= createSemantickernel(client);

        response response = new response();
        firstQuestionImp firstQuestionImp = new firstQuestionImp();
        answer firstQuestionReply=firstQuestionImp.invokeOpenAI(sKernel,client);
        response.setFirstQuestionReply(firstQuestionReply);

        secondQuestionImp secondQuestionImp = new secondQuestionImp();
        answer secondQuestionImpReply=secondQuestionImp.invokeOpenAI(sKernel,client);
        response.setSecondQuestionReply(secondQuestionImpReply);



        return response;
    }

    private static Kernel createSemantickernel(OpenAIAsyncClient client) {


        ChatCompletionService chatCompletionService = OpenAIChatCompletion.builder()
                .withModelId(System.getenv("openai_model"))
                .withOpenAIAsyncClient(client)
                .build();

        Kernel kernel = Kernel.builder()
                .withAIService(ChatCompletionService.class, chatCompletionService)
                .build();
        return kernel;
    }


}
