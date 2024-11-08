package com.newsportal.newsportal.repository;

// import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.data.jpa.repository.Query;

import com.newsportal.newsportal.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Employee findByEmailAndPassword(String email, String password);  // For login
    // @Query("SELECT e FROM Employee e WHERE e.approved = true")
    // List<Employee> findApprovedEmployees();
}

