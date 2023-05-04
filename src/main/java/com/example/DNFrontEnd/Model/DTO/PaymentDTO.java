package com.example.DNFrontEnd.Model.DTO;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentDTO {
    private Long id;
    private String vnp_BankCode;
    private String vnp_Amount;
    private String vnp_BankTranNo;
    private String vnp_CardType;
    private String vnp_OrderInfo;
    private String vnp_PayDate;
    private String vnp_ResponseCode;
    private String vnp_TmnCode;
    private String vnp_TransactionNo;
    private String vnp_TransactionStatus;
    private String vnp_TxnRef;
    private String vnp_SecureHash;
    private Long user_id;
    private Boolean hook_status;
    private Long scheduleId;

    public static String convertToString(PaymentDTO paymentDTO) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String result = objectMapper.writeValueAsString(paymentDTO);
        return result;
    }
}
