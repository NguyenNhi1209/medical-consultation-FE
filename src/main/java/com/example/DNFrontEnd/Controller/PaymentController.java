package com.example.DNFrontEnd.Controller;


import com.example.DNFrontEnd.Model.BaseResponse;
import com.example.DNFrontEnd.Model.request.DetailDoctorScheduleRequest;
import com.example.DNFrontEnd.Model.request.SaveScheduleRequest;
import com.example.DNFrontEnd.Model.response.PatientResponse;
import com.example.DNFrontEnd.Model.response.SchedulesResponse;
import com.example.DNFrontEnd.Service.PatientService;
import com.example.DNFrontEnd.Service.PaymentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    @Autowired
    PatientService patientService;

    ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
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
                                HttpSession session, RedirectAttributes redirectAttrs) throws JsonProcessingException {
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


        DetailDoctorScheduleRequest detailDoctorScheduleRequest = new DetailDoctorScheduleRequest();
        detailDoctorScheduleRequest.setScheduleId(Long.parseLong(scheduleId));
        SchedulesResponse schedulesResponse = patientService.getScheduleDetail(detailDoctorScheduleRequest,session.getAttribute("token").toString());

        SaveScheduleRequest saveScheduleRequest = new SaveScheduleRequest();
        saveScheduleRequest.setSymptom(schedulesResponse.getPatientProfile().getSymptom());
        saveScheduleRequest.setHours(schedulesResponse.getHours());
        saveScheduleRequest.setDoctorId(schedulesResponse.getDoctorId());
        saveScheduleRequest.setMedicalDate(schedulesResponse.getMedicalDate());
        saveScheduleRequest.setDepartmentId(schedulesResponse.getDoctor().getDepartment().getId());

        BaseResponse baseResponse = patientService.getPatient(session.getAttribute("token").toString());
        PatientResponse patientResponse = objectMapper.readValue(objectMapper.writeValueAsString(baseResponse.getData()).toString(), PatientResponse.class);

        redirectAttrs.addFlashAttribute("patientResponse", patientResponse);
        redirectAttrs.addFlashAttribute("saveScheduleRequest", saveScheduleRequest);
        redirectAttrs.addFlashAttribute("departmentName", schedulesResponse.getDoctor().getDepartment().getName());
        redirectAttrs.addFlashAttribute("price", schedulesResponse.getPrice());
        redirectAttrs.addFlashAttribute("symptom", schedulesResponse.getPatientProfile().getSymptom());
        session.setAttribute("chooseTime",false);
        redirectAttrs.addFlashAttribute("message","Thanh toán thất bại");
        return "redirect:/booking/confirm";
    }
}
