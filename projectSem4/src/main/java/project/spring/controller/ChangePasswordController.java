package project.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import project.spring.model.Account;
@Controller
public class ChangePasswordController {
    @Autowired  
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/changePassword")
public String showChangePasswordForm(Model model, HttpSession session) {
    String username = (String) session.getAttribute("username");
    if (username == null) {
        return "redirect:/login"; // Redirect to login page if not logged in
    }
    model.addAttribute("account", new Account());
    return "forderClient/changePassword";   
}
@PostMapping("/processChangePassword")
public String processChangePassword(
    @RequestParam("oldPassword") String oldPassword,
    @RequestParam("newPassword") String newPassword,
    HttpSession session,
    Model model) {
    
    String username = (String) session.getAttribute("username");
    if (username == null) {
        return "redirect:/login";
    }

    if (!validateOldPassword(username, oldPassword)) {
        model.addAttribute("error", "Current password is incorrect.");
        return "forderClient/changePassword";
    }

    updatePassword(username, newPassword);
    model.addAttribute("message", "Password successfully updated.");
    return "redirect:/login"; // Assuming there's a profile page to redirect to
}

private boolean validateOldPassword(String username, String oldPassword) {
    String sql = "SELECT password FROM accounts WHERE username = ?";
    String storedPassword = jdbcTemplate.queryForObject(sql, new Object[]{username}, String.class);
    return storedPassword != null && storedPassword.equals(oldPassword);
}

private void updatePassword(String username, String newPassword) {
    String sql = "UPDATE accounts SET password = ? WHERE username = ?";
    jdbcTemplate.update(sql, newPassword, username);
}

}
