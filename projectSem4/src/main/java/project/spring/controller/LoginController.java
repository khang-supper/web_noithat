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
import project.spring.repositories.AccountRepository;

@Controller
public class LoginController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private AccountRepository accountRepository;

    @GetMapping("/login")
    public String showLogin(Model model) {
        model.addAttribute("title", "Trang login");
        return "forderClient/login"; // Trả về trang đăng nhập
    }
   
    @PostMapping("/login")
    public String login(@ModelAttribute(name = "loginForm") Account accounts, Model model, HttpSession session, HttpServletRequest request) {
        String username = accounts.getUsername();
        String password = accounts.getPassword();

        String role = checkUserRole(username, password);    
        if (role != null) {
            Account account = accountRepository.findByUserName(username);
            model.addAttribute("username", username);
            session.setAttribute("username", username); // Lưu username vào session
            session.setAttribute("accountId", account.getId());
            session.setAttribute("role", role);
            return (role.equals("1")) ? "redirect:/admin" : "redirect:/";
        } else {
            model.addAttribute("error", "Tên người dùng hoặc mật khẩu không chính xác");
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
