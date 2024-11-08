package com.newsportal.newsportal.Controller;

import com.newsportal.newsportal.model.Employee;
import com.newsportal.newsportal.model.JobApplication;
import com.newsportal.newsportal.model.News;
import com.newsportal.newsportal.model.User;
import com.newsportal.newsportal.repository.EmployeeRepository;
import com.newsportal.newsportal.repository.JobApplicationRepository;
import com.newsportal.newsportal.repository.NewsRepository;
import com.newsportal.newsportal.service.UserService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import jakarta.servlet.http.HttpSession;


@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private NewsRepository newsRepository;

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
    public String userdashboardpage(Model model){
        List<JobApplication> applications = repo.findAll();
        model.addAttribute("applications", applications);

        List<Employee> employees = employeeRepository.findAll();
        model.addAttribute("employees", employees);
        return "adminDashboard";
    }

    

    @GetMapping("/applyjob")
    public String applyjobpage(){
        return "applyjob";
    }
  

    @PostMapping("/login")
    public String loginUser(HttpSession session, String email, String password, Model model) {
        User user = userService.loginUser(email, password);
        if (user != null) {
            session.setAttribute("user",user);
            return "redirect:/userDashboard"; // Redirect to home page
        }
        model.addAttribute("error", "Invalid credentials");
        return "login"; // Redirect back to login page with error
    }

    @PostMapping("/register")
    public String registerUser(User user, Model model) {
        userService.registerUser(user);
        model.addAttribute("success", "Registration successful! Please log in.");
        return "redirect:/login"; // Redirect to login page
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

    @GetMapping("/employee/delete/{id}")
    public String deleteEmployeeGet(@PathVariable("id") Long id) {
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

    @GetMapping("/userDashboard")
    public String showUserDashboard(HttpSession session,Model model) {
        List<News> newsList = newsRepository.findAll();
        System.out.println(newsList);
        model.addAttribute("newsList", newsList);
        return "userDashboard";
    }
    
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // Invalidate the session
        return "redirect:/"; // Redirect to home page
    }
    
}
