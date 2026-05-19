package com.example.spaceflow.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WhatsAppService {

    @Value("${TOKEN}")
    private String token;

    @Value("${INSTANCE}")
    private String instance;

    public void sendWhatsApp(String to, String message){
        try {
            String url = "https://api.ultramsg.com/" + instance + "/messages/chat";
            String finalUrl = url + "?token=" + token + "&to=" + to + "&body=" + message;

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getForObject(finalUrl, String.class);

        } catch (Exception e) {
            System.out.println("WhatsApp failed: " + e.getMessage());
        }
    }
}
