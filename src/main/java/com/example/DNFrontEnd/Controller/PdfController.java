package com.example.DNFrontEnd.Controller;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import com.example.DNFrontEnd.Model.request.DetailDoctorScheduleRequest;
import com.example.DNFrontEnd.Model.response.SchedulesResponse;
import com.example.DNFrontEnd.Service.DoctorService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;


@Controller
@RequestMapping({"/pdf"})
public class PdfController {

    @Autowired
    private TemplateEngine templateEngine;
    @Autowired
    private DoctorService doctorService;


    @GetMapping("/pdf")
    public String getForm() {
        return "pdf-form";
    }

//    @PostMapping("/export")
//    public String generatePdf(HttpServletRequest request, RedirectAttributes redirectAttrs) throws Exception {
//        String medicalDate = request.getParameter("medicalDate1");
//        redirectAttrs.addFlashAttribute("medicalDate", medicalDate);
//
//        // Prepare the data to be passed to the template
//        String scheduleId = request.getParameter("scheduleId1");
//        Map<String, Object> data = new HashMap<>();
//        data.put("name", "nhi");
//        data.put("age", "23");
//
//
//        // Create a Thymeleaf context object with the data
//        Context context = new Context();
//        context.setVariables(data);
//
//        // Render the Thymeleaf template as an HTML string
//        String html = templateEngine.process("scheduleDoctorDetail", context);
//
//        // Convert the HTML string to a PDF file
//        ITextRenderer renderer = new ITextRenderer();
//        renderer.setDocumentFromString(html);
//        renderer.layout();
//        OutputStream outputStream = new FileOutputStream("D:/example.pdf");
//        renderer.createPDF(outputStream);
//        outputStream.close();
//
//        return "redirect:/doctor/schedule";
//    }
    @PostMapping("/export")
    public void downloadPdf(HttpServletResponse response, HttpSession session, HttpServletRequest request) throws IOException {
        String medicalDate = request.getParameter("medicalDate1");

        // Prepare the data to be passed to the template
        String scheduleId = request.getParameter("scheduleId1");
        DetailDoctorScheduleRequest detailDoctorScheduleRequest = new DetailDoctorScheduleRequest();
        detailDoctorScheduleRequest.setScheduleId(Long.parseLong(scheduleId));
        SchedulesResponse schedulesResponse = doctorService.getScheduleDetail(detailDoctorScheduleRequest, session.getAttribute("token").toString());


        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=\"example.pdf\"");
        try {
            OutputStream outputStream = response.getOutputStream();
            generatePdf(outputStream,schedulesResponse);
            outputStream.flush();
            outputStream.close();
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
        }
    }
    private void generatePdf(OutputStream outputStream,SchedulesResponse schedulesResponse) throws DocumentException, IOException {
        DateTimeFormatter format1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter format2 = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        // Load font file
        InputStream inputStream = getClass().getResourceAsStream("/static/fonts/ARIALUNI.TTF");
        BaseFont unicode = BaseFont.createFont("ARIALUNI.TTF", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, BaseFont.CACHED, inputStream.readAllBytes(), null);
        Font font = new Font(unicode, 18);
        Font font1 = new Font(unicode, 12);
        Font font2 = new Font(unicode, 12,Font.BOLD);


        Document document = new Document();
        PdfWriter.getInstance(document, outputStream);

        document.open();
        //header
        Paragraph paragraphH = new Paragraph("Phòng khám đa khoa DN", font1);
        paragraphH.add("\nĐịa chỉ: 12 Nguyễn Văn Bảo p1 quận Gò Vấp\n");
        paragraphH.setAlignment(Element.ALIGN_LEFT);
        document.add(paragraphH);

        // Tạo đối tượng hình ảnh

        File file = new File(getClass().getResource("/static/images/logo1.png").getFile());

        Image image = Image.getInstance(file.getAbsolutePath());

        // Thiết lập kích thước của hình ảnh
        image.scaleToFit(40, 40);
        image.setAlignment(Element.ALIGN_CENTER);

        // Thêm hình ảnh vào tài liệu
        document.add(image);
        document.add(new Paragraph("\n\n"));

        Paragraph paragraph = new Paragraph("BÁO CÁO KẾT QUẢ", font);
        paragraph.setAlignment(Element.ALIGN_CENTER);
        document.add(paragraph);
        document.add(new Paragraph("\n\n\n"));
        PdfPTable table = new PdfPTable(4);
        table.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.setPaddingTop(50);
        table.getDefaultCell().setBorderWidth(0);
        table.getDefaultCell().setPadding(10);


// Thêm tiêu đề cho các cột
        PdfPCell cell1 = new PdfPCell(new Phrase("Tên bệnh nhân", font2));
        cell1.setBorder(0);
        table.addCell(cell1);
        PdfPCell cell2 = new PdfPCell(new Phrase(schedulesResponse.getPatientProfile().getName(), font2));
        cell2.setBorder(0);
        table.addCell(cell2);

        PdfPCell cell3 = new PdfPCell(new Phrase("Ngày sinh", font2));
        cell3.setBorder(0);
        table.addCell(cell3);

        LocalDate birthday = LocalDate.parse(schedulesResponse.getPatientProfile().getBirthday(), format1);
        PdfPCell cell4 = new PdfPCell(new Phrase(format2.format(birthday), font1));
        cell4.setBorder(0);
        table.addCell(cell4);

        PdfPCell cell5 = new PdfPCell(new Phrase("Giới tính", font2));
        cell5.setBorder(0);
        table.addCell(cell5);
        PdfPCell cell6 = new PdfPCell(new Phrase(schedulesResponse.getPatientProfile().getSex().equalsIgnoreCase("F") ? "Nữ" : "Nam", font1));
        cell6.setBorder(0);
        table.addCell(cell6);
        PdfPCell cell7 = new PdfPCell(new Phrase("Số điện thoại", font2));
        cell7.setBorder(0);
        table.addCell(cell7);
        PdfPCell cell8 = new PdfPCell(new Phrase(schedulesResponse.getPatientProfile().getPhoneNumber(), font1));
        cell8.setBorder(0);
        table.addCell(cell8);

        PdfPCell cell9 = new PdfPCell(new Phrase("Triệu chứng", font2));
        cell9.setBorder(0);
        table.addCell(cell9);
        PdfPCell cell10 = new PdfPCell(new Phrase(schedulesResponse.getPatientProfile().getSymptom(), font1));
        cell10.setBorder(0);
        cell10.setColspan(3);
        table.addCell(cell10);


        PdfPCell cell13 = new PdfPCell(new Phrase("Bác sĩ chỉ định", font2));
        cell13.setBorder(0);
        table.addCell(cell13);
        PdfPCell cell14 = new PdfPCell(new Phrase(schedulesResponse.getDoctor().getFullName(), font1));
        cell14.setBorder(0);
        table.addCell(cell14);
        PdfPCell cell15 = new PdfPCell(new Phrase("Khoa khám bệnh", font2));
        cell15.setBorder(0);
        table.addCell(cell15);
        PdfPCell cell16 = new PdfPCell(new Phrase(schedulesResponse.getDoctor().getDepartment().getName(), font1));
        cell16.setBorder(0);
        table.addCell(cell16);


        PdfPCell cell17 = new PdfPCell(new Phrase("Chuẩn đoán", font2));
        cell17.setBorder(0);
        table.addCell(cell17);
        PdfPCell cell18 = new PdfPCell(new Phrase(schedulesResponse.getPatientProfile().getDiagnostic(), font1));
        cell18.setBorder(0);
        table.addCell(cell18);
        PdfPCell cell19 = new PdfPCell(new Phrase("Ngày thực hiện", font2));
        cell19.setBorder(0);
        table.addCell(cell19);

        LocalDate medicalDate = LocalDate.parse(schedulesResponse.getMedicalDate(), format1);
        PdfPCell cell20 = new PdfPCell(new Phrase(format2.format(medicalDate), font1));
        cell20.setBorder(0);
        table.addCell(cell20);

        document.add(table);

        //footer
        document.add(new Paragraph("\n\n\n"));
        Paragraph paragraphF = new Paragraph("Bác sĩ xác nhận", font1);
        paragraphF.add("\n");
        paragraphF.add("\n");
        paragraphF.add("\n");
        paragraphF.add(schedulesResponse.getDoctor().getFullName());
        paragraphF.setAlignment(Element.ALIGN_RIGHT);
        document.add(paragraphF);

        document.close();
    }
}
