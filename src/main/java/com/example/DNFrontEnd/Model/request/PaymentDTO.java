package com.example.DNFrontEnd.Model.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentDTO {

    private String vnp_BankCode;
    private String vnp_Amount;
    private String vnp_OrderInfo;
    public static String convertToString(PaymentDTO paymentDTO) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String result = objectMapper.writeValueAsString(paymentDTO);
        return result;
    }
}
