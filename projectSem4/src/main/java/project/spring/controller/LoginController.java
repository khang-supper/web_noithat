package project.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import project.spring.model.Account;

@Controller
public class LoginController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/login")
    public String showLogin() {
        return "forderClient/login"; // Trả về trang đăng nhập
    }
   
    @PostMapping("/admin")
    public String login(@ModelAttribute(name = "loginForm") Account accounts, Model model, HttpSession session, HttpServletRequest request) {
        String username = accounts.getUsername();
        String password = accounts.getPassword();

        String role = checkUserRole(username, password);
        if (role != null) {
            model.addAttribute("username", username);
            session.setAttribute("username", username); // Lưu username vào session
            session.setAttribute("role", role);
            return (role.equals("1")) ? "/admin/indexadmin" : "/Client/index";
        } else {
            model.addAttribute("error", "Incorrect UserName & Password");
            return "/forderClient/login";
        }
        
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    @PostMapping("/logout")
    public String logoutPost(HttpSession session) {
        session.invalidate();
        return "/forderClient/login";
    }

    // Phương thức kiểm tra thông tin đăng nhập
    private String checkUserRole(String username, String password) {
        String sql = "SELECT role FROM accounts WHERE username = ? AND password = ?";
        try {
            return jdbcTemplate.queryForObject(sql, String.class, username, password);
        } catch (Exception e) {
            return null;
        }
    }
    //////////
   

}
