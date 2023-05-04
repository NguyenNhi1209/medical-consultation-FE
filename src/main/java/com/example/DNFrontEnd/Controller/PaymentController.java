package com.example.DNFrontEnd.Controller;


import com.example.DNFrontEnd.Service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping({"/api"})
public class PaymentController {
    @Autowired
    PaymentService paymentService;
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
                                @RequestParam("vnp_OrderInfo") String vnpOrderInfo,
                                HttpSession session, RedirectAttributes redirectAttrs) {
        Boolean result = paymentService.getPayment(vnpTxnRef,session.getAttribute("token").toString());
        System.out.println(vnpTransactionStatus);
        String scheduleId = session.getAttribute("scheduleId").toString();
        if(result){
            redirectAttrs.addFlashAttribute("message","Thanh toán thành công");
            return "redirect:/patient/schedule/detail1?scheduleId=" + scheduleId;
        }
//        model.addAttribute("saveScheduleRequest", saveScheduleRequest);
//        model.addAttribute("departmentName", detailScheduleResponse.getDepartmentName());
//        model.addAttribute("price", detailScheduleResponse.getPrice());
//        model.addAttribute("symptom", detailScheduleResponse.getSymptom());
//        session.setAttribute("chooseTime",false);
//        return "redirect:/booking/confirm";

        return "redirect:/booking/confirm";
    }
}
