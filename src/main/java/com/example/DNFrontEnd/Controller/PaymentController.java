package com.example.DNFrontEnd.Controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping({"/api"})
public class PaymentController {
    @GetMapping("/vnpay/update-payment")
    public String updatePayment(@RequestParam("vnp_BankCode") String vnpBankCode,
                                      @RequestParam(name = "vnp_BankTranNo", required = false) String vnpBankTranNo,
                                      @RequestParam("vnp_CardType") String vnpCardType,
                                      @RequestParam("vnp_PayDate") String vnpPayDate,
                                      @RequestParam("vnp_ResponseCode") String vnpResponseCode,
                                      @RequestParam("vnp_SecureHash") String vnpSecureHash,
                                      @RequestParam("vnp_TmnCode") String vnpTmnCode,
                                      @RequestParam("vnp_TransactionNo") String vnpTransactionNo,
                                      @RequestParam("vnp_TransactionStatus")String vnpTransactionStatus,
                                      @RequestParam("vnp_TxnRef") String vnpTxnRef,
                                      @RequestParam("vnp_Amount") String vnpAmount,
                                      @RequestParam("vnp_OrderInfo") String vnpOrderInfo) {
        System.out.println(vnpResponseCode);
        if(vnpResponseCode.equalsIgnoreCase("00")){
            return "redirect:/paymentSuccess";
        }

        return "redirect:/";
    }
}
