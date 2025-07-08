package com.vinay.ecomm.service;

import com.vinay.ecomm.entity.Product;
import com.vinay.ecomm.repoistory.PaymentRepo;
import com.vinay.ecomm.repoistory.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Component
public class GeminiWebSocketHandler extends TextWebSocketHandler {

    @Value("${gemini.api.key}")
    private String api_key;

    @Autowired
    private ProductRepo productRepo;

    private final String GEMINI_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=";


    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        String userInput = message.getPayload().trim().toLowerCase();
        System.out.println("Original Input Message: " + userInput);

        String response;

        if (isEcommerceQuery(userInput)) {
            response = handleEcommerceQuery(userInput);
        } else {
            response = callGeminiForAnswer(userInput);
        }

        session.sendMessage(new TextMessage(response));

    }

    private boolean isEcommerceQuery(String userInput) {
        String[] productCategories = {"ac", "refrigerator", "fridge", "washing machine", "tv", "television"};

        for (String category : productCategories) {
            if (userInput.contains(category) && (userInput.contains("under") || userInput.contains("below"))) {
                return true;
            }
        }
        return false;
    }

    private String handleEcommerceQuery(String userInput) {
        try {
            String[] productCategories = {"ac", "refrigerator", "fridge", "washing machine", "tv", "television"};
            String detectedCategory = null;

            for (String category : productCategories) {
                if (userInput.contains(category)) {
                    if (category.equals("fridge")) detectedCategory = "Refrigerator";
                    else if (category.equals("tv") || category.equals("television")) detectedCategory = "Television";
                    else detectedCategory = capitalize(category);
                    break;
                }
            }

            if (detectedCategory == null) return "Sorry, I couldn't identify product category.";

            // Extract price
            Pattern pricePattern = Pattern.compile("(under|below)\\s*(\\d+)");
            Matcher matcher = pricePattern.matcher(userInput);
            double maxPrice = 0;

            if (matcher.find()) {
                maxPrice = Double.parseDouble(matcher.group(2));
            }
            // Query DB for correct category and price
            List<Product> products = productRepo.findByCategoryAndPriceLessThan(detectedCategory, maxPrice);
            System.out.println(products);
            if (products.isEmpty()) {
                return "No " + detectedCategory + " found under ₹" + maxPrice;
            }

            // Correctly format the response
            StringBuilder response = new StringBuilder("Here are some " + detectedCategory + "s under ₹" + maxPrice + ":");
            for (Product product : products) {
                response.append(" || ").append(product.getName())
                        .append(" (₹").append(product.getPrice()).append(")");
            }
            System.out.println(response);
            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();  // For debugging
            return "Error processing your request.";
        }
    }

    private String capitalize(String text) {
        return text.substring(0, 1).toUpperCase() + text.substring(1).toLowerCase();
    }
    private String callGeminiForAnswer(String userInput) {
        try {
            RestTemplate restTemplate = new RestTemplate();

            // ✅ Wrap user input in a valid JSON format
            String jsonPayload = "{ \"contents\": [{ \"parts\": [{ \"text\": \"" + userInput + "\" }]}]}";

            // ✅ Ensure headers are correct
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> entity = new HttpEntity<>(jsonPayload, headers);

            // ✅ Correct API URL
            String url = GEMINI_URL + api_key;

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

            System.out.println("🔹 Sent JSON to Gemini: " + jsonPayload);
            System.out.println("🔹 Raw Response from Gemini: " + response.getBody());

            return response.getBody();
        } catch (Exception e) {
            System.err.println("❌ API Call Failed: " + e.getMessage());
            return "{\"error\":\"API call failed\"}";
        }

}


}

