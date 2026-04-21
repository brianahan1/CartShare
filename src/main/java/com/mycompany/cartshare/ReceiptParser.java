package com.mycompany.cartshare;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.FileInputStream;
import java.util.Properties;
import org.json.JSONObject;
import org.json.JSONArray;

public class ReceiptParser {

    private static final String VISION_API_KEY;

    static {
        Properties props = new Properties();
        try {
            props.load(new FileInputStream("config.properties"));
        } catch (Exception e) {
            System.out.println("Could not load config.properties: " + e.getMessage());
        }
        VISION_API_KEY = props.getProperty("VISION_API_KEY");
    }

    public String parseReceipt(String imagePath) throws Exception {
        // Read and encode image to base64
        byte[] imageBytes = Files.readAllBytes(Paths.get(imagePath));
        String base64Image = Base64.getEncoder().encodeToString(imageBytes);

        // Build request body
        String requestBody = """
            {
              "requests": [{
                "image": { "content": "%s" },
                "features": [{ "type": "DOCUMENT_TEXT_DETECTION" }]
              }]
            }
            """.formatted(base64Image);

        // Call the API
        URL url = new URL("https://vision.googleapis.com/v1/images:annotate?key=" + VISION_API_KEY);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);
        conn.getOutputStream().write(requestBody.getBytes());

        // Read raw response
        String rawResponse = new String(conn.getInputStream().readAllBytes());

        // Extract just the text from the JSON
        JSONObject json = new JSONObject(rawResponse);
        String extractedText = json.getJSONArray("responses")
                                   .getJSONObject(0)
                                   .getJSONArray("textAnnotations")
                                   .getJSONObject(0)
                                   .getString("description");

        return extractedText;
    }

    public static void main(String[] args) throws Exception {
        ReceiptParser parser = new ReceiptParser();
        String result = parser.parseReceipt("receipt.png");
        System.out.println(result);
    }
}