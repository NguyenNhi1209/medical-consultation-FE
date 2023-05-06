package com.example.DNFrontEnd.Service;

import com.example.DNFrontEnd.Model.BaseResponse;
import com.example.DNFrontEnd.Model.request.*;
import com.example.DNFrontEnd.Model.response.BasePaginationResponse;
import com.example.DNFrontEnd.Model.response.DetailScheduleResponse;
import com.example.DNFrontEnd.Model.response.DoctorProfileResponse;
import com.example.DNFrontEnd.Model.response.SchedulesResponse;
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
public class DoctorService {
    @Value("${admin.rest.url:}")
    private String adminUrl;

    private Logger logger = Logger.getLogger(getClass().getName());

    public BasePaginationResponse getSchedule(ListDoctorScheduleRequest request, String token, String page) {
        BasePaginationResponse basePaginationResponse;
        List<SchedulesResponse> schedulesResponseList = new ArrayList<>();
        try {
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(adminUrl+"/doctor/schedules?page="+page+"&size=10"))
                    .header("accept", "application/json")
                    .header("Authorization", "Bearer " + token)
                    .header("content-type", "application/json")
                    .method("POST", HttpRequest.BodyPublishers.ofString(ListDoctorScheduleRequest.convertToString(request)))
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

    public SchedulesResponse getScheduleDetail(DetailDoctorScheduleRequest request, String token) {
        BaseResponse baseResponse;
        SchedulesResponse schedulesResponse = new SchedulesResponse();
        try {
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(adminUrl+"/doctor/schedule/detail"))
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

    public BaseResponse updateScheduleDetail(UpdateScheduleRequest request, String token) {
        BaseResponse baseResponse = new BaseResponse<>();
        try {
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(adminUrl+"/doctor/schedule/update"))
                    .header("accept", "application/json")
                    .header("Authorization", "Bearer " + token)
                    .header("content-type", "application/json")
                    .method("POST", HttpRequest.BodyPublishers.ofString(UpdateScheduleRequest.convertToString(request)))
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

    public DoctorProfileResponse getDoctorProfile(String token)  {
        BaseResponse baseResponse;
        DoctorProfileResponse doctorProfileResponse = new DoctorProfileResponse();
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(adminUrl+"/doctor/profile"))
                    .header("accept", "application/json")
                    .header("Authorization", "Bearer " + token)
                    .header("content-type", "application/json")
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();
            HttpResponse response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            ObjectMapper objectMapper = new ObjectMapper()
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);

            baseResponse = objectMapper.readValue(response.body().toString(),BaseResponse.class);
            doctorProfileResponse = objectMapper.readValue(objectMapper.writeValueAsString(baseResponse.getData()).toString(), DoctorProfileResponse.class);

            return  doctorProfileResponse;
        } catch (Exception e){
            e.printStackTrace();
        }
        return doctorProfileResponse;
    }

    public BaseResponse saveDoctorProfile(SaveProfileRequest saveProfileRequest, String token)  {
        BaseResponse baseResponse;
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(adminUrl+"/doctor/profile/update"))
                    .header("accept", "application/json")
                    .header("Authorization", "Bearer " + token)
                    .header("content-type", "application/json")
                    .method("POST", HttpRequest.BodyPublishers.ofString(SaveProfileRequest.convertToString(saveProfileRequest)))
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
