package com.kanchan.studentmanagementsystem;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.kanchan.studentmanagementsystem.entity.Student;
import com.kanchan.studentmanagementsystem.entity.User;
import com.kanchan.studentmanagementsystem.service.EmailService;
import com.kanchan.studentmanagementsystem.service.ExcelService;
import com.kanchan.studentmanagementsystem.service.OtpService;
import com.kanchan.studentmanagementsystem.service.StudentService;
import com.kanchan.studentmanagementsystem.service.UserService;
import com.kanchan.studentmanagementsystem.util.QRGenerator;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;


@Controller
public class HomeController {

    @Autowired
    private UserService userService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private ExcelService excelService;
    @Autowired
    private EmailService emailService;

    @Autowired
    private OtpService otpService;

    // Login Page
    @GetMapping("/")
    public String home() {
        return "login";
    }

    // Login
    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {

        User user = userService.login(username, password);

        if (user != null) {

            session.setAttribute("user", user);

            return "redirect:/dashboard";
        }

        model.addAttribute("error", "Invalid Username or Password");

        return "login";
    }
    @GetMapping("/register")
    public String registerPage(Model model) {

        model.addAttribute("user", new User());

        return "register";
    }
    @PostMapping("/register")
    public String register(User user,
                        Model model,
                        RedirectAttributes redirectAttributes) {

        User existingUser = userService.getUserByUsername(user.getUsername());

        if (existingUser != null) {

            model.addAttribute("error", "Username already exists");

            model.addAttribute("user", user);

            return "register";
        }

        userService.register(user);

        redirectAttributes.addFlashAttribute(
                "success",
                "Registration Successful. Please Login");

        return "redirect:/";
    }

    // Add Student Page
    @GetMapping("/addStudent")
    public String addStudentPage(HttpSession session, Model model){

        if(session.getAttribute("user")==null){
            return "redirect:/";
        }

        model.addAttribute("student", new Student());

        return "addStudent";
    }

    // Save Student
    @PostMapping("/saveStudent")
    public String saveStudent(@Valid Student student,
                            BindingResult result,
                            @RequestParam("photoFile") MultipartFile photoFile,
                            RedirectAttributes redirectAttributes,
                            HttpSession session) throws IOException {

        if (session.getAttribute("user") == null) {
            return "redirect:/";
        }

        if (result.hasErrors()) {
            return "addStudent";
        }

        Student existingStudent = studentService.getStudentByEmail(student.getEmail());

        if (existingStudent != null) {
            result.rejectValue("email", "error.student", "Email already exists");
            return "addStudent";
        }

        if (!photoFile.isEmpty()) {

            String uploadPath = System.getProperty("user.dir")
                    + File.separator + "uploads";

            File uploadDir = new File(uploadPath);

            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            String fileName = UUID.randomUUID() + "_"
                    + photoFile.getOriginalFilename();

            File destination = new File(uploadDir, fileName);

            photoFile.transferTo(destination);

            student.setPhoto(fileName);

            System.out.println("Project Path : " + System.getProperty("user.dir"));
            System.out.println("Upload Path : " + uploadPath);
            System.out.println("Saved File : " + fileName);
            System.out.println("Full Path : " + destination.getAbsolutePath());
        }

        studentService.saveStudent(student);
            emailService.sendStudentWelcomeEmail(
            student.getEmail(),
            student.getName()
            );

        redirectAttributes.addFlashAttribute(
                "success",
                "Student Saved Successfully.");

        return "redirect:/addStudent";
    }
    

    // View Students
    @GetMapping("/viewStudents")
    public String viewStudents(@RequestParam(defaultValue = "0") int page,
                            HttpSession session,
                            Model model){

        if(session.getAttribute("user")==null){
            return "redirect:/";
        }

        var studentPage = studentService.getStudentsPage(page);

        model.addAttribute("students", studentPage.getContent());

        model.addAttribute("currentPage", page);

        model.addAttribute("totalPages", studentPage.getTotalPages());

        return "viewStudents";
    }

    // Edit Student
    @GetMapping("/editStudent/{id}")
    public String editStudent(@PathVariable int id,
                            Model model,
                            HttpSession session) {

        if(session.getAttribute("user")==null){
            return "redirect:/";
        }

        Student student = studentService.getStudentById(id);

        model.addAttribute("student", student);

        return "updateStudent";
    }
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {

        if (session.getAttribute("user") == null) {
            return "redirect:/";
        }

        User user = (User) session.getAttribute("user");

        model.addAttribute("username", user.getUsername());

        model.addAttribute("totalStudents",
                studentService.getStudentCount());

        model.addAttribute("totalCourses",
                studentService.getCourseCount());

        model.addAttribute("totalUsers",
                userService.getUserCount());

                List<Object[]> courseStats = studentService.getCourseStatistics();

                List<String> labels = new ArrayList<>();
                List<Long> values = new ArrayList<>();

                for (Object[] obj : courseStats) {

                    labels.add((String) obj[0]);

                    values.add((Long) obj[1]);

                }
                model.addAttribute("labels", labels);
                model.addAttribute("values", values);
                model.addAttribute(
                        "recentStudents",
                        studentService.getRecentStudents()
                );
                model.addAttribute("latestStudents",
                    studentService.getRecentStudents());

        return "dashboard";
    }

    @GetMapping("/deleteStudent/{id}")
    public String deleteStudent(@PathVariable int id,
                                RedirectAttributes redirectAttributes,
                                HttpSession session) {

        if (session.getAttribute("user") == null) {
            return "redirect:/";
        }

        Student student = studentService.getStudentById(id);

        if (student != null && student.getPhoto() != null) {

            String uploadPath = System.getProperty("user.dir")
                    + File.separator + "uploads";

            File file = new File(uploadPath, student.getPhoto());

            if (file.exists()) {
                file.delete();
                System.out.println("Deleted Photo : " + file.getAbsolutePath());
            }
        }

        studentService.deleteStudent(id);

        redirectAttributes.addFlashAttribute(
                "success",
                "Student Deleted Successfully.");

        return "redirect:/viewStudents";
    }
    @PostMapping("/updateStudent")
    public String updateStudent(@Valid Student student,
                                BindingResult result,
                                @RequestParam("photoFile") MultipartFile photoFile,
                                RedirectAttributes redirectAttributes,
                                HttpSession session) throws IOException {

        if (session.getAttribute("user") == null) {
            return "redirect:/";
        }

        if (result.hasErrors()) {
            return "updateStudent";
        }

        Student oldStudent = studentService.getStudentById(student.getId());

        if (!photoFile.isEmpty()) {

            String uploadPath = System.getProperty("user.dir")
                    + File.separator + "uploads";

            File uploadDir = new File(uploadPath);

            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            // Delete old photo
            if (oldStudent.getPhoto() != null) {

                File oldFile = new File(uploadDir, oldStudent.getPhoto());

                if (oldFile.exists()) {
                    oldFile.delete();
                }
            }

            // Save new photo
            String fileName = UUID.randomUUID() + "_"
                    + photoFile.getOriginalFilename();

            photoFile.transferTo(new File(uploadDir, fileName));

            student.setPhoto(fileName);

        } else {

            // Keep old photo
            student.setPhoto(oldStudent.getPhoto());
        }

        studentService.saveStudent(student);

        redirectAttributes.addFlashAttribute(
                "success",
                "Student Updated Successfully.");

        return "redirect:/viewStudents";
    }
    // Search Student Page
    @GetMapping("/searchStudent")
    public String searchStudentPage(HttpSession session){

        if(session.getAttribute("user")==null){
            return "redirect:/";
        }

        return "searchStudent";
    }

    // Search Student
    @PostMapping("/searchStudent")
    public String searchStudent(
            @RequestParam String name,
            Model model,
            HttpSession session) {

        if (session.getAttribute("user") == null) {
            return "redirect:/";
        }

        List<Student> students = studentService.searchStudents(name);

        model.addAttribute("students", students);

        return "searchResult";
    }
    @PostMapping("/searchStudentById")
    public String searchStudentById(@RequestParam int id,
                                    Model model,
                                    HttpSession session) {

        if(session.getAttribute("user")==null){
            return "redirect:/";
        }

        Student student = studentService.searchStudentById(id);

        model.addAttribute("student", student);

        return "searchResult";

    }
    @GetMapping("/student/{id}")
    public String studentProfile(@PathVariable int id, Model model) {

        Student student = studentService.getStudentById(id);

        model.addAttribute("student", student);

        return "student-profile";
    }
    
    @GetMapping("/logout")
    public String logout(HttpSession session){

        session.invalidate();

        return "redirect:/";
    }

    @GetMapping("/forgotPassword")
    public String forgotPasswordPage() {
        return "forgot-password";
    }
    @PostMapping("/forgotPassword")
    public String verifyEmail(@RequestParam String email,
                            Model model) {

        User user = userService.getUserByEmail(email);

        if (user == null) {

            model.addAttribute("error", "Email not found!");

            return "forgot-password";
        }

        // Generate OTP
        String otp = otpService.generateOtp(email);

        // Send OTP Email
        emailService.sendOtpEmail(email, otp);

        model.addAttribute("email", email);

        return "verify-otp";
    }
    @PostMapping("/verifyOtp")
    public String verifyOtp(@RequestParam String email,
                            @RequestParam String otp,
                            Model model) {

        if (!otpService.verifyOtp(email, otp)) {

            model.addAttribute("error", "Invalid OTP");
            model.addAttribute("email", email);

            return "verify-otp";
        }

        otpService.removeOtp(email);

        model.addAttribute("email", email);

        return "reset-password";
    }
    @PostMapping("/resetPassword")
    public String resetPassword(@RequestParam String email,
                                @RequestParam String password,
                                RedirectAttributes redirectAttributes) {

        userService.resetPassword(email, password);

        redirectAttributes.addFlashAttribute(
                "success",
                "Password Updated Successfully.");

        return "redirect:/";
    }

    @GetMapping("/export/pdf")
    public void exportPdf(HttpServletResponse response) throws Exception {

        response.setContentType("application/pdf");

        response.setHeader("Content-Disposition",
                "attachment; filename=students.pdf");

        List<Student> students = studentService.getAllStudents();

        Document document = new Document();

        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();

        document.add(new Paragraph("Student List"));
        document.add(new Paragraph(" "));

        PdfPTable table = new PdfPTable(5);

        table.addCell("ID");
        table.addCell("Name");
        table.addCell("Email");
        table.addCell("Course");
        table.addCell("Mobile");

        for(Student s : students){

            table.addCell(String.valueOf(s.getId()));
            table.addCell(s.getName());
            table.addCell(s.getEmail());
            table.addCell(s.getCourse());
            table.addCell(s.getMobile());

        }

        document.add(table);

        document.close();

    }
    @GetMapping("/export/excel")
    public void exportExcel(HttpServletResponse response) throws Exception {

        response.setContentType(
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        response.setHeader(
                "Content-Disposition",
                "attachment; filename=students.xlsx");

        Workbook workbook = new XSSFWorkbook();

        Sheet sheet = workbook.createSheet("Students");

        Row header = sheet.createRow(0);

        header.createCell(0).setCellValue("ID");
        header.createCell(1).setCellValue("Name");
        header.createCell(2).setCellValue("Email");
        header.createCell(3).setCellValue("Course");
        header.createCell(4).setCellValue("Mobile");

        List<Student> students = studentService.getAllStudents();

        int rowNum = 1;

        for (Student s : students) {

            Row row = sheet.createRow(rowNum++);

            row.createCell(0).setCellValue(s.getId());
            row.createCell(1).setCellValue(s.getName());
            row.createCell(2).setCellValue(s.getEmail());
            row.createCell(3).setCellValue(s.getCourse());
            row.createCell(4).setCellValue(s.getMobile());
        }

        for (int i = 0; i < 5; i++) {
            sheet.autoSizeColumn(i);
        }

        workbook.write(response.getOutputStream());

        workbook.close();
    }
    @GetMapping("/exportExcel")
    public ResponseEntity<InputStreamResource> exportExcel() throws IOException {

        ByteArrayInputStream in = excelService.exportStudents(
                studentService.getAllStudents());

        HttpHeaders headers = new HttpHeaders();

        headers.add("Content-Disposition",
                "attachment; filename=students.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new InputStreamResource(in));
    }
    
    @GetMapping("/filterCourse")
    public String filterCourse(@RequestParam String course,
                            Model model,
                            HttpSession session) {

        if (session.getAttribute("user") == null) {
            return "redirect:/";
        }

        List<Student> students = studentService.getStudentsByCourse(course);

        model.addAttribute("students", students);

        model.addAttribute("currentPage", 0);
        model.addAttribute("totalPages", 1);

        return "viewStudents";
    }
    @GetMapping("/sortStudents")
    public String sortStudents(@RequestParam String sortBy,
                            Model model,
                            HttpSession session) {

        if (session.getAttribute("user") == null) {
            return "redirect:/";
        }

        model.addAttribute(
                "students",
                studentService.getSortedStudents(sortBy));

        model.addAttribute("currentPage", 0);
        model.addAttribute("totalPages", 1);

        return "viewStudents";
    }
    @GetMapping("/student/idcard/{id}")
    public String studentIdCard(@PathVariable int id,
                                Model model,
                                HttpSession session) {

        if (session.getAttribute("user") == null) {
            return "redirect:/";
        }

        Student student = studentService.getStudentById(id);

        // QR Code Content
        String qrText =
                "ID : " + student.getId()
                + "\nName : " + student.getName()
                + "\nCourse : " + student.getCourse()
                + "\nEmail : " + student.getEmail()
                + "\nMobile : " + student.getMobile();

        // Generate QR
        String qrFile =
                QRGenerator.generateQRCode(
                        qrText,
                        "student_" + student.getId());

        model.addAttribute("student", student);
        model.addAttribute("qrFile", qrFile);

        return "student-id-card";
    }

}