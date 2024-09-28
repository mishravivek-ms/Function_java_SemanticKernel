package com.optum.plugin.firstquestionplugin;

import com.azure.ai.formrecognizer.documentanalysis.DocumentAnalysisClient;
import com.azure.ai.formrecognizer.documentanalysis.DocumentAnalysisClientBuilder;
import com.azure.ai.formrecognizer.documentanalysis.models.AnalyzeResult;
import com.azure.core.credential.AzureKeyCredential;
import com.azure.core.util.BinaryData;
import com.microsoft.semantickernel.semanticfunctions.annotations.DefineKernelFunction;
import com.azure.ai.formrecognizer.documentanalysis.models.DocumentLine;
import com.azure.ai.formrecognizer.documentanalysis.models.DocumentPage;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class agreementPlugin {


    private String apiendpoint=null;
    public agreementPlugin(String endpoint) {
        apiendpoint=endpoint;
    }


    @DefineKernelFunction(name = "ReadAgreement", description = "Gets full document")
public InputStream readDocumentAsync(String apiURL) throws Exception {
    InputStream inputStream = null;
    try {
        // Create a URL object with the target URL
        URL url = new URL(apiURL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        // Set the request method (GET, POST, etc.)
        connection.setRequestMethod("GET");

        // Set request headers (optional)
        connection.setRequestProperty("Content-Type", "application/json");

        // Get the response code
        int responseCode = connection.getResponseCode();
        System.out.println("Response Code: " + responseCode);

        // If the response code is 200 (HTTP OK)
        if (responseCode == HttpURLConnection.HTTP_OK) {
            inputStream = connection.getInputStream();
        } else {
            System.out.println("GET request failed");
        }
    } catch (Exception e) {
        e.printStackTrace();
    }

/*//--------------------------------------------
        AzureKeyCredential credential = new AzureKeyCredential(System.getenv("documentintent_key"));
        DocumentAnalysisClient client = new DocumentAnalysisClientBuilder()
                .endpoint(System.getenv("documentintent_url"))
                .credential(credential)
                .buildClient();

        BinaryData binaryData = BinaryData.fromBytes(convertInputStreamToByteArray(inputStream));
        AnalyzeResult result = client.beginAnalyzeDocument("prebuilt-read", binaryData)
                .getFinalResult();

        StringBuilder content = new StringBuilder();
        for (DocumentPage page : result.getPages()) {
            for (DocumentLine line : page.getLines()) {
                content.append(line.getContent()).append("\n");
            }
        }
        System.out.println(content.toString());
        //-----------------------------------------*/
    return inputStream;
}

    /*public static byte[] convertInputStreamToByteArray(InputStream inputStream) throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            byteArrayOutputStream.write(buffer, 0, length);
        }
        return byteArrayOutputStream.toByteArray();
    }*/

}
