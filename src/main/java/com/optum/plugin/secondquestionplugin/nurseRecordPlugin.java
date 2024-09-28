package com.optum.plugin.secondquestionplugin;

import com.microsoft.semantickernel.semanticfunctions.annotations.DefineKernelFunction;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.stream.Collectors;

public class nurseRecordPlugin {

    private String apiendpoint = null;

    public nurseRecordPlugin(String endpoint) {
        apiendpoint = endpoint;
    }

    @DefineKernelFunction(name = "ReadNurseRecord", description = "Gets full document")
    public String readDocumentAsync(String apiURL) throws Exception {
        String jsonResponse = null;
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
                InputStream inputStream = connection.getInputStream();
                jsonResponse = new BufferedReader(new InputStreamReader(inputStream))
                        .lines().collect(Collectors.joining("\n"));
            } else {
                System.out.println("GET request failed");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsonResponse;
    }
}