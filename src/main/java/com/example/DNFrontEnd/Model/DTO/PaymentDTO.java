package com.example.DNFrontEnd.Model.DTO;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

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
