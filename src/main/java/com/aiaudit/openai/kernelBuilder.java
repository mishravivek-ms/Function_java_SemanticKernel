package com.aiaudit.openai;

import java.io.*;
import java.util.Random;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.core.credential.AzureKeyCredential;

import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.aiservices.openai.chatcompletion.OpenAIChatCompletion;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import com.aiaudit.DTO.kernelRequest;
import com.aiaudit.DTO.answer;
import com.aiaudit.DTO.response;
import com.aiaudit.questions.firstQuestionImp;
import com.aiaudit.questions.secondQuestionImp;

public class kernelBuilder {


    public static response invokeOpenAI(kernelRequest kernelrequest) throws IOException {
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


        response.setId(String.valueOf(new Random().nextInt(1000)));
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
