/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cartshare;

/**
 *
 * @author lev
 */
import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import java.io.FileInputStream;
import java.util.Properties;

public class GenerateTextFromTextInput {
    public String GenerateTextFromTextInput(String textvalue) throws Exception {
        Properties props = new Properties();
        props.load(new FileInputStream("config.properties"));
        String apiKey = props.getProperty("GEMINI_API_KEY");

            // Pass it directly to the client
        Client client = Client.builder().apiKey(apiKey).build();
        GenerateContentResponse response =
            client.models.generateContent(
                "gemini-2.5-flash",
                "Create a string using the following format of the following receipt: " +
                "retailername,datepurchased,itemA~pricewithtaxcombined,itemB,pricewithtaxcombined,.."+textvalue,
                null);
        return response.text();
  }
}
