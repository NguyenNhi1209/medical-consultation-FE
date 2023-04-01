package com.example.DNFrontEnd.Service;

import com.example.DNFrontEnd.Model.BaseResponse;
import com.example.DNFrontEnd.Model.request.LoginRequest;
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
public class PatientService {
    @Value("${admin.rest.url:}")
    private String adminUrl;

    private Logger logger = Logger.getLogger(getClass().getName());

    public BaseResponse getPatient(String token)  {
        BaseResponse baseResponse;
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(adminUrl+"/patient/detail"))
                    .header("accept", "application/json")
                    .header("Authorization", "Bearer " + token)
                    .header("content-type", "application/json")
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();
            HttpResponse response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            ObjectMapper objectMapper = new ObjectMapper()
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);

            baseResponse = objectMapper.readValue(response.body().toString(),BaseResponse.class);
            return  baseResponse;
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public BaseResponse savePatient(SavePatientRequest savePatientRequest,String token)  {
        BaseResponse baseResponse;
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(adminUrl+"/patient/save"))
                    .header("accept", "application/json")
                    .header("Authorization", "Bearer " + token)
                    .header("content-type", "application/json")
                    .method("POST", HttpRequest.BodyPublishers.ofString(SavePatientRequest.convertToString(savePatientRequest)))
                    .build();
            HttpResponse response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            ObjectMapper objectMapper = new ObjectMapper()
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);

            baseResponse = objectMapper.readValue(response.body().toString(),BaseResponse.class);
            System.out.println(baseResponse);
            return  baseResponse;
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
