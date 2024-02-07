package com.example.DNFrontEnd.Service;

import com.example.DNFrontEnd.Model.BaseResponse;
import com.example.DNFrontEnd.Model.request.*;
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

    public BaseResponse getPatientById(Long patientId, String token)  {
        BaseResponse baseResponse;
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(adminUrl+"/patient/detail/" + patientId))
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

    public PatientProfileResponse createPatientProfile(CreatePatientProfileRequest request, String token) {
        BaseResponse baseResponse;
        PatientProfileResponse patientProfileResponse = null;
        try {
            HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(adminUrl+"/patient/profile/save"))
                .header("accept", "application/json")
                .header("Authorization", "Bearer " + token)
                .header("content-type", "application/json")
                .method("POST", HttpRequest.BodyPublishers.ofString(CreatePatientProfileRequest.convertToString(request)))
                .build();
            HttpResponse response = HttpClient.newHttpClient().send(httpRequest, HttpResponse.BodyHandlers.ofString());
            ObjectMapper objectMapper = new ObjectMapper()
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);

            baseResponse = objectMapper.readValue(response.body().toString(),BaseResponse.class);
            patientProfileResponse = objectMapper.readValue(objectMapper.writeValueAsString(baseResponse.getData()).toString(), PatientProfileResponse.class);
            System.out.println(patientProfileResponse);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return patientProfileResponse;
    }

    public BaseResponse fetchDepartment(FetchDepartmentRequest request, String token) {
        BaseResponse baseResponse;
        try {
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(adminUrl+"/department/fetch"))
                    .header("accept", "application/json")
                    .header("Authorization", "Bearer " + token)
                    .header("content-type", "application/json")
                    .method("POST", HttpRequest.BodyPublishers.ofString(FetchDepartmentRequest.convertToString(request)))
                    .build();
            HttpResponse response = HttpClient.newHttpClient().send(httpRequest, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.body().toString());
            ObjectMapper objectMapper = new ObjectMapper()
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);

            baseResponse = objectMapper.readValue(response.body().toString(),BaseResponse.class);

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
    public DetailScheduleResponse saveSchedule(SaveScheduleRequest request, String token) {
        BaseResponse baseResponse;
        DetailScheduleResponse detailScheduleResponse = null;
        try {
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(adminUrl+"/schedule/save"))
                    .header("accept", "application/json")
                    .header("Authorization", "Bearer " + token)
                    .header("content-type", "application/json")
                    .method("POST", HttpRequest.BodyPublishers.ofString(SaveScheduleRequest.convertToString(request)))
                    .build();
            HttpResponse response = HttpClient.newHttpClient().send(httpRequest, HttpResponse.BodyHandlers.ofString());
            ObjectMapper objectMapper = new ObjectMapper()
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);

            baseResponse = objectMapper.readValue(response.body().toString(),BaseResponse.class);
            detailScheduleResponse = objectMapper.readValue(objectMapper.writeValueAsString(baseResponse.getData()).toString(), DetailScheduleResponse.class);
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
        return detailScheduleResponse;
    }

    public BasePaginationResponse getSchedule(ListPatientScheduleRequest request, String token, String page) {
        BasePaginationResponse basePaginationResponse;
        List<SchedulesResponse> schedulesResponseList = new ArrayList<>();
        try {
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(adminUrl+"/patient/schedules?page="+page+"&size=10"))
                    .header("accept", "application/json")
                    .header("Authorization", "Bearer " + token)
                    .header("content-type", "application/json")
                    .method("POST", HttpRequest.BodyPublishers.ofString(ListPatientScheduleRequest.convertToString(request)))
                    .build();
            HttpResponse response = HttpClient.newHttpClient().send(httpRequest, HttpResponse.BodyHandlers.ofString());
            ObjectMapper objectMapper = new ObjectMapper()
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
            basePaginationResponse = objectMapper.readValue(response.body().toString(),BasePaginationResponse.class);
            System.out.println(basePaginationResponse);
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

    public SchedulesResponse getScheduleDetail(DetailDoctorScheduleRequest request, String token) {
        BaseResponse baseResponse;
        SchedulesResponse schedulesResponse = new SchedulesResponse();
        try {
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(adminUrl+"/patient/schedule/detail"))
                    .header("accept", "application/json")
                    .header("Authorization", "Bearer " + token)
                    .header("content-type", "application/json")
                    .method("POST", HttpRequest.BodyPublishers.ofString(DetailDoctorScheduleRequest.convertToString(request)))
                    .build();
            HttpResponse response = HttpClient.newHttpClient().send(httpRequest, HttpResponse.BodyHandlers.ofString());
            ObjectMapper objectMapper = new ObjectMapper()
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
            baseResponse = objectMapper.readValue(response.body().toString(),BaseResponse.class);
            System.out.println(baseResponse);
            schedulesResponse = objectMapper.readValue(objectMapper.writeValueAsString(baseResponse.getData()).toString(), SchedulesResponse.class);
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
        return schedulesResponse;
    }

    public BaseResponse searchParent(String phoneNumber, String token)  {
        SearchPatientResponse searchPatientResponse = new SearchPatientResponse();
        BaseResponse baseResponse = new BaseResponse();
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(adminUrl+"/patient/search?phone=" + phoneNumber))
                    .header("accept", "application/json")
                    .header("Authorization", "Bearer " + token)
                    .header("content-type", "application/json")
                    .method("POST", HttpRequest.BodyPublishers.noBody())
                    .build();
            HttpResponse response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            ObjectMapper objectMapper = new ObjectMapper()
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
            baseResponse = objectMapper.readValue(response.body().toString(),BaseResponse.class);
        } catch (Exception e){
            e.printStackTrace();
        }
        return baseResponse;
    }

    public BaseResponse savePatient(CreatePatientRequest request, String token) {
        BaseResponse baseResponse = new BaseResponse<>();
        DetailScheduleResponse detailScheduleResponse = null;
        try {
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(adminUrl+"/patient/create"))
                    .header("accept", "application/json")
                    .header("Authorization", "Bearer " + token)
                    .header("content-type", "application/json")
                    .method("POST", HttpRequest.BodyPublishers.ofString(CreatePatientRequest.convertToString(request)))
                    .build();
            HttpResponse response = HttpClient.newHttpClient().send(httpRequest, HttpResponse.BodyHandlers.ofString());
            ObjectMapper objectMapper = new ObjectMapper()
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);

            baseResponse = objectMapper.readValue(response.body().toString(),BaseResponse.class);
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
