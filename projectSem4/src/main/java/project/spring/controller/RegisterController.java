package project.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import project.spring.model.Account;

@Controller
public class RegisterController {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

   
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("title", "Trang register");
        model.addAttribute("account", new Account());
        return "forderClient/register"; 
    }
    @PostMapping("/processRegister")
    public String register(@ModelAttribute("account") Account account, Model model) {// kt tai khoan neu tao thanh con thi vao login
        boolean registrationSuccess = registerUser(account);
        if (registrationSuccess) {
            model.addAttribute("message", "Registration successful. You can now login.");
            return "forderClient/login";
        } else {// ko thanh cong van ô trang đó  và in lỗi
            model.addAttribute("error", "Registration failed. Please try again.");
            return "forderClient/register";
        }
    }

    public boolean registerUser(Account account) {// truy vấn csdl db
        try {
            String sql = "INSERT INTO accounts (username, password, fullName, email, phone, address, avatar, status, role) " +
                         "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            jdbcTemplate.update(sql, account.getUsername(), account.getPassword(), account.getFullName(),
                                 account.getEmail(), account.getPhone(), account.getAddress(), account.getAvatar(),
                                 true, false); 
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
}
