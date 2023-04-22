package com.example.DNFrontEnd.Service;

import com.example.DNFrontEnd.Model.BaseResponse;
import com.example.DNFrontEnd.Model.request.PaymentDTO;
import com.example.DNFrontEnd.Model.request.SavePatientRequest;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.logging.Logger;

@Service
public class PaymentService {
    @Value("${admin.rest.url:}")
    private String adminUrl;

    private Logger logger = Logger.getLogger(getClass().getName());
    public String payment(PaymentDTO paymentDTO, String token)  {
        BaseResponse baseResponse;
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(adminUrl+"/vnpay/create-payment"))
                    .header("accept", "application/json")
                    .header("Authorization", "Bearer " + token)
                    .header("content-type", "application/json")
                    .method("POST", HttpRequest.BodyPublishers.ofString(PaymentDTO.convertToString(paymentDTO)))
                    .build();
            HttpResponse response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            ObjectMapper objectMapper = new ObjectMapper()
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);

            baseResponse = objectMapper.readValue(response.body().toString(),BaseResponse.class);
            System.out.println(baseResponse);
            if(baseResponse.getData() != null) {
                return baseResponse.getData().toString();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return "error";
    }
}
