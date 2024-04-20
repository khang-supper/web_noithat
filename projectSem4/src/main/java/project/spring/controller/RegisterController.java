package project.spring.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
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
    public String register(@ModelAttribute("account") Account account, Model model) {// kt tai khoan neu tao thanh cong
                                                                                     // thi vao login
        boolean registrationSuccess = registerUser(account);
        if (registrationSuccess) {
            model.addAttribute("message", "đăng ký thành công.");
            return "redirect:/login";
        } else {
            if (isUsernameExists(account.getUsername())) {
                model.addAttribute("error", "đăng ký không thành công tên này đã có người dùng.");
            } else {
                model.addAttribute("error", "đăng ký không thành công. vui lòng thử lại.");
            }
            return "forderClient/register";
        }
    }

    public boolean isUsernameExists(String username) {
        String sql = "SELECT COUNT(*) FROM accounts WHERE username = ?";
        int count = jdbcTemplate.queryForObject(sql, Integer.class, username);
        return count > 0;
    }

    public boolean registerUser(Account account) {
        try {
            // Kiểm tra xem tên người dùng đã tồn tại chưa
            if (isUsernameExists(account.getUsername())) {
                return false; // Trả về false nếu tên người dùng đã tồn tại
            }
            // Nếu chưa tồn tại, thêm tài khoản mới vào cơ sở dữ liệu
            String sql = "INSERT INTO accounts (username, password, fullName, email, phone, address, avatar, status, role) "
                    +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            jdbcTemplate.update(sql, account.getUsername(), account.getPassword(), account.getFullName(),
                    account.getEmail(), account.getPhone(), account.getAddress(), account.getAvatar(),
                    true, false);
            return true; // Trả về true khi đăng ký thành công
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
