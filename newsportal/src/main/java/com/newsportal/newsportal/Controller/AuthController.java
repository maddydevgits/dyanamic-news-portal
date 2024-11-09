package com.newsportal.newsportal.Controller;

import com.newsportal.newsportal.model.Employee;
import com.newsportal.newsportal.model.JobApplication;
import com.newsportal.newsportal.model.News;
import com.newsportal.newsportal.model.User;
import com.newsportal.newsportal.repository.EmployeeRepository;
import com.newsportal.newsportal.repository.JobApplicationRepository;
import com.newsportal.newsportal.repository.NewsRepository;
import com.newsportal.newsportal.service.EmailService;
import com.newsportal.newsportal.service.JobApplicationService;
import com.newsportal.newsportal.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;


@Controller
public class AuthController {

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserService userService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private JobApplicationRepository jobApplicationRepository;

    private static final String ADMIN_USERNAME = "admin@gmail.com"; // Static admin username
    private static final String ADMIN_PASSWORD = "password123"; // Static admin password

    @Autowired
    private JobApplicationRepository repo;

    @GetMapping("/")
    public String homePage() {
        return "home";
    }

    @GetMapping("/register")
    public String registerPage(){
        return "register";
    }

    @GetMapping("/login")
    public String loginPage(){
        return "login";
    }

    @GetMapping("/adminlogin")
    public String adminPage() {
        return "adminlogin";
    }

    @GetMapping("/adminDashboard")
    public String userdashboardpage(HttpSession session,Model model){
        String adminEmail = (String) session.getAttribute("adminEmail");
        if(adminEmail!=null){
            List<JobApplication> applications = repo.findByStatus("Pending");
            model.addAttribute("applications", applications);

            List<Employee> employees = employeeRepository.findAll();
            model.addAttribute("employees", employees);
            return "adminDashboard";
        } else {
            return "redirect:/adminlogin";
        }
    }

    @GetMapping("/applyjob")
    public String applyjobpage(HttpSession session){
        String userEmail = (String) session.getAttribute("email");
        if(jobApplicationRepository.existsByEmail(userEmail)){
            return "redirect:/alreadyApplied";
        }
        return "applyjob";
    }

    @GetMapping("/alreadyApplied")
    public String showAppliedJobPage(HttpSession session, Model model) {
        String userEmail = (String) session.getAttribute("email");

        if (userEmail != null) {
            // Retrieve the job application data for the user
            Optional<JobApplication> jobApplication = jobApplicationRepository.findByEmail(userEmail);
            jobApplication.ifPresent(application -> model.addAttribute("jobApplication", application));
        }

        return "alreadyApplied";
    }

    @PostMapping("/login")
    public String loginUser(HttpSession session, String email, String password, Model model) {
        User user = userService.loginUser(email, password);
        if (user != null) {
            session.setAttribute("user",user.getName());
            session.setAttribute("email", user.getEmail());
            return "redirect:/userDashboard"; // Redirect to home page
        }
        model.addAttribute("error", "Invalid credentials");
        return "login"; // Redirect back to login page with error
    }

    @PostMapping("/register")
    public String registerUser(User user, Model model) {
        
        User user1=userService.registerUser(user);
        if(user1==null){
            model.addAttribute("error","Account already exist");
            return "register";
        } else {
            String subject = "Welcome to News Portal";
            String body = "<div><b>Thank you for registering with News Portal.</b></div>";
            String response = emailService.sendEmail(user.getEmail(), user.getName(), subject, body);
            System.out.println(response);
            model.addAttribute("success", "Registration successful! Please log in.");
            return "redirect:/login"; // Redirect to login page
        }
    }

    @PostMapping("/adminlogin")
    public String adminloginUser(HttpSession session,String email, String password, Model model) {
        if (ADMIN_USERNAME.equals(email) && ADMIN_PASSWORD.equals(password)) {
            model.addAttribute("admin", true); // Indicate that the admin is logged in
            session.setAttribute("adminEmail", email);
            return "redirect:/adminDashboard"; // Redirect to admin dashboard
        }
        model.addAttribute("error", "Invalid admin credentials");
        return "adminlogin"; // Redirect back to admin login page with error
    }

    @GetMapping("/employeeDetail/{id}")
    public String viewEmployeeDetail(@PathVariable("id") Long id, Model model) {
        // Fetch the employee details based on the id
        Employee employee = employeeRepository.findById(id).get();
        // If the employee is found, add it to the model; otherwise, handle error or redirect
        if (employee != null) {
            model.addAttribute("name", employee.getName());
            model.addAttribute("email", employee.getEmail());
            model.addAttribute("phone", employee.getPhone());
            model.addAttribute("qualifications", employee.getQualifications());
            model.addAttribute("experience", employee.getExperience());
            model.addAttribute("location", employee.getLocation());
            model.addAttribute("id", employee.getId());
            return "employeeDetail"; // Redirect to employee detail page
        } else {
            return "redirect:/employee"; // Redirect back if employee not found
        }
    }

    @Transactional
    @GetMapping("/employee/delete/{id}")
    public String deleteEmployeeGet(@PathVariable("id") Long id) {
        Employee employee = employeeRepository.findById(id).get();
        jobApplicationRepository.deleteByEmail(employee.getEmail());
        employeeRepository.deleteById(id);
        return "redirect:/adminDashboard";
    }

    @GetMapping("/news")
    public String showAllNews(HttpSession session,Model model) {
        List<News> newsList = newsRepository.findAll();
        model.addAttribute("newsList", newsList);

        Employee author = (Employee) session.getAttribute("employee");
        model.addAttribute("author", author);
        return "news";
    }

    @GetMapping("/employeenews")
    public String showAllNews1(HttpSession session,Model model) {
        List<News> newsList = newsRepository.findAll();
        model.addAttribute("newsList", newsList);

        String author = (String) session.getAttribute("employee");
        model.addAttribute("author", author);
        return "employeenews";
    }

    @GetMapping("/news/{id}")
    public String viewNewsDetails(@PathVariable Long id, Model model) {
        Optional<News> news = newsRepository.findById(id);
        if (news.isPresent()) {
            model.addAttribute("news", news.get());
            return "newsDetails"; // Redirect to the news detail page
        }
        return "error"; // Redirect to an error page if news not found
    }
    @GetMapping("/adminnews/{id}")
    public String viewadminNewsDetails(@PathVariable Long id, Model model) {
        Optional<News> news = newsRepository.findById(id);
        if (news.isPresent()) {
            model.addAttribute("news", news.get());
            return "usernews"; // Redirect to the news detail page
        }
        return "error"; // Redirect to an error page if news not found
    }
    @GetMapping("/employeenews1/{id}")
    public String viewadminNewsDetails1(@PathVariable Long id, Model model) {
        Optional<News> news = newsRepository.findById(id);
        if (news.isPresent()) {
            model.addAttribute("news", news.get());
            return "employeenews1"; // Redirect to the news detail page
        }
        return "error"; // Redirect to an error page if news not found
    }

    @GetMapping("/userDashboard")
    public String showUserDashboard(HttpSession session, Model model) {
        String username = (String) session.getAttribute("user");
        if (username == null) {
            return "redirect:/";
        }
        
        List<News> newsList = newsRepository.findAll();
        if (newsList == null) {
            newsList = new ArrayList<>();
        }
        
        model.addAttribute("newsList", newsList);
        return "userDashboard";
    }
    
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // Invalidate the session
        return "redirect:/"; // Redirect to home page
    }
    
}
