package com.example.DNFrontEnd.Model.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class VNPayPaymentResponse {
    private Long id;
    private String vnp_ResponseId;
    private String vnp_Command;
    private String vnp_ResponseCode;
    private String vnp_Message;
    private String vnp_TmnCode;
    private String vnp_TxnRef;
    private BigDecimal vnp_Amount = BigDecimal.valueOf(0);
    private String vnp_OrderInfo;
    private String vnp_BankCode;
    private String vnp_PayDate;
    private String vnp_TransactionNo;
    private String vnp_TransactionType;
    private String vnp_TransactionStatus;
    private String vnp_CardNumber;
    private String vnp_Trace;
    private String vnp_CardHolder;
    private String vnp_FeeAmount;
    private String vnp_SecureHash;
    private Boolean is_done = false;
    private Long paymentId;
}
