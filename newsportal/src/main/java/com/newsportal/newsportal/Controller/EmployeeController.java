package com.newsportal.newsportal.Controller;

import com.newsportal.newsportal.model.Employee;
import com.newsportal.newsportal.model.News;
import com.newsportal.newsportal.repository.EmployeeRepository;
import com.newsportal.newsportal.repository.NewsRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private NewsRepository newsRepository;

    
        @GetMapping("/login")
        public String showLoginForm() {
        return "employee"; // Thymeleaf template for login
        }
        
        // Employee Login
        @PostMapping("/login")
        public String login( String email, String password, HttpSession session, Model model) {
            Employee employee = employeeRepository.findByEmailAndPassword(email, password);
            
            if (employee != null) {
                session.setAttribute("employee", employee.getEmail());  // Store employee in session
                return "redirect:/employee/employeeDashboard";       // Redirect to employee dashboard
            } else {
                model.addAttribute("error", "Invalid email or password");
                return "employee";                       // Return to login page with error
            }
        }
    
        // Employee Dashboard
        @GetMapping("/employeeDashboard")
        public String dashboard(HttpSession session, Model model) {
            String employee = (String) session.getAttribute("employee");
            if (employee == null) {
                return "redirect:/login";                    // Redirect if not logged in
            }
            model.addAttribute("newsList", newsRepository.findAll());  // Load all news for display
            return "employeeDashboard";                      // Render employee dashboard view
        }
    
        // Upload News
        @PostMapping("/uploadnews")
        public String uploadNews(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("image") MultipartFile imageFile,
            HttpSession session) {
    
        String employee = (String) session.getAttribute("employee");
        if (employee == null) {
            return "redirect:/login"; // Redirect if not logged in
        }
    
        // Set the directory where files will be stored
        String uploadDir = "uploads/news_files/";
        
        try {
            // Ensure the directory exists
            File directory = new File(uploadDir);
            if (!directory.exists()) {
                directory.mkdirs();
            }
    
            // Generate a unique file name to avoid collisions
            String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
            Path filePath = Paths.get(uploadDir, fileName);
    
            // Save the file to the server
            Files.write(filePath, imageFile.getBytes());
    
            // Create and save the news object
            News news = new News();
            news.setTitle(title);
            news.setDescription(description);
            news.setImageUrl("/" + uploadDir + fileName);  // Save relative path to access the file later
            newsRepository.save(news);  // Save news to the database

        } catch (IOException e) {
            e.printStackTrace();
            return "error"; // Redirect to error page if the file fails to save
        }

        return "employeeDashboard"; // Redirect to dashboard after upload
    }

}