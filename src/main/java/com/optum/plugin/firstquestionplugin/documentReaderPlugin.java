package com.optum.plugin.firstquestionplugin;

import com.azure.ai.formrecognizer.documentanalysis.DocumentAnalysisClient;
import com.azure.ai.formrecognizer.documentanalysis.DocumentAnalysisClientBuilder;
import com.azure.ai.formrecognizer.documentanalysis.models.AnalyzeResult;
import com.azure.ai.formrecognizer.documentanalysis.models.DocumentKeyValuePair;
import com.azure.core.credential.AzureKeyCredential;
import com.azure.core.util.BinaryData;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.semantickernel.semanticfunctions.annotations.DefineKernelFunction;
import com.azure.ai.formrecognizer.documentanalysis.models.DocumentLine;
import com.azure.ai.formrecognizer.documentanalysis.models.DocumentPage;
import com.optum.DTO.agreementDTO;
import com.optum.util.InputStreamWrapper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.concurrent.CompletableFuture;

// I need to create a sementic kernal plugin that read document and return the content of the document

public class documentReaderPlugin {

    private ObjectMapper objectMapper = new ObjectMapper();
    private final DocumentAnalysisClient client;
    public documentReaderPlugin(String endpoint, String key) {
        AzureKeyCredential credential = new AzureKeyCredential(key);
        this.client = new DocumentAnalysisClientBuilder()
                .endpoint(endpoint)
                .credential(credential)
                .buildClient();
    }


    @DefineKernelFunction(name = "ReadDocument", description = "Gets full document")
    public agreementDTO readDocumentAsync(String json) throws Exception {
        InputStreamWrapper wrapper = objectMapper.readValue(json, InputStreamWrapper.class);
        BinaryData binaryData = BinaryData.fromBytes(wrapper.getData());
        AnalyzeResult result = client.beginAnalyzeDocument("prebuilt-document", binaryData)
                .getFinalResult();
        agreementDTO agreementDTO = new agreementDTO();
        String content = "";
        int counter=0;
        int signatureCounter=0;
        for (DocumentKeyValuePair kvp : result.getKeyValuePairs()) {
            if (kvp.getValue() == null) {
                System.out.println("  Found key with no value: '" + kvp.getKey().getContent() + "'");
            } else {
                if( kvp.getKey().getContent().equals("Patient/Legal Guardian/Patient's Legal Representative")){
                    agreementDTO.setSignature(kvp.getValue().getContent());
                    signatureCounter=counter;
                }
                if( kvp.getKey().getContent().equals("Date") && counter>signatureCounter){
                    agreementDTO.setSignaturedate(kvp.getValue().getContent());
                }
                content=content+ kvp.getKey().getContent()+" : "+kvp.getValue().getContent()+"\n";
            }
            counter++;
        }
        return agreementDTO;

    }

}
