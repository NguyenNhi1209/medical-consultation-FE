package com.example.DNFrontEnd.Service;

import com.example.DNFrontEnd.Model.BaseResponse;
import com.example.DNFrontEnd.Model.request.ActivateUserRequest;
import com.example.DNFrontEnd.Model.request.AddUserRequest;
import com.example.DNFrontEnd.Model.request.ListDoctorRequest;
import com.example.DNFrontEnd.Model.request.UpdateScheduleRequest;
import com.example.DNFrontEnd.Model.response.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Service
public class AdminService {
    @Value("${admin.rest.url:}")
    private String adminUrl;

    private Logger logger = Logger.getLogger(getClass().getName());
    ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);

    public BasePaginationResponse getDoctors(ListDoctorRequest request, String token, String page) {
        BasePaginationResponse basePaginationResponse;
        List<SchedulesResponse> schedulesResponseList = new ArrayList<>();
        try {
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(adminUrl+"/admin/doctors?page="+page+"&size=10"))
                    .header("accept", "application/json")
                    .header("Authorization", "Bearer " + token)
                    .header("content-type", "application/json")
                    .method("GET", HttpRequest.BodyPublishers.ofString(ListDoctorRequest.convertToString(request)))
                    .build();
            HttpResponse response = HttpClient.newHttpClient().send(httpRequest, HttpResponse.BodyHandlers.ofString());
            ObjectMapper objectMapper = new ObjectMapper()
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
            basePaginationResponse = objectMapper.readValue(response.body().toString(),BasePaginationResponse.class);
            System.out.println(basePaginationResponse);
            schedulesResponseList = objectMapper.readValue(objectMapper.writeValueAsString(basePaginationResponse.getData()).toString(), List.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return basePaginationResponse;
    }

    public DoctorResponse getDoctor(String doctorId, String token) {
        BaseResponse baseResponse;
        DoctorResponse doctorResponse = new DoctorResponse();
        try {
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(adminUrl+"/admin/doctor/" + doctorId))
                    .header("accept", "application/json")
                    .header("Authorization", "Bearer " + token)
                    .header("content-type", "application/json")
                    .method("GET",HttpRequest.BodyPublishers.noBody() )
                    .build();
            HttpResponse response = HttpClient.newHttpClient().send(httpRequest, HttpResponse.BodyHandlers.ofString());
            ObjectMapper objectMapper = new ObjectMapper()
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
            baseResponse = objectMapper.readValue(response.body().toString(),BaseResponse.class);
            System.out.println(baseResponse);
            doctorResponse = objectMapper.readValue(objectMapper.writeValueAsString(baseResponse.getData()).toString(), DoctorResponse.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return doctorResponse;
    }
    public BaseResponse addUser(AddUserRequest request, String token) {
        BaseResponse baseResponse = new BaseResponse<>();
        try {
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(adminUrl+"/admin/add-user"))
                    .header("accept", "application/json")
                    .header("Authorization", "Bearer " + token)
                    .header("content-type", "application/json")
                    .method("POST", HttpRequest.BodyPublishers.ofString(AddUserRequest.convertToString(request)))
                    .build();
            HttpResponse response = HttpClient.newHttpClient().send(httpRequest, HttpResponse.BodyHandlers.ofString());
            ObjectMapper objectMapper = new ObjectMapper()
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
            baseResponse = objectMapper.readValue(response.body().toString(),BaseResponse.class);
            System.out.println(baseResponse);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return baseResponse;
    }

    public BaseResponse activate(ActivateUserRequest request, String token) {
        BaseResponse baseResponse = new BaseResponse<>();
        try {
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(adminUrl+"/admin/activate"))
                    .header("accept", "application/json")
                    .header("Authorization", "Bearer " + token)
                    .header("content-type", "application/json")
                    .method("POST", HttpRequest.BodyPublishers.ofString(ActivateUserRequest.convertToString(request)))
                    .build();
            HttpResponse response = HttpClient.newHttpClient().send(httpRequest, HttpResponse.BodyHandlers.ofString());
            ObjectMapper objectMapper = new ObjectMapper()
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
            baseResponse = objectMapper.readValue(response.body().toString(),BaseResponse.class);
            System.out.println(baseResponse);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return baseResponse;
    }
}
