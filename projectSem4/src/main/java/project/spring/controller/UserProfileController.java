package project.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpSession;
import project.spring.model.Account;

@Controller
public class UserProfileController {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/editUser")
    public String showEditProfileForm(HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        if (username == null) {
            return "redirect:/login";
        }
        Account account = fetchAccountByUsername(username);
        model.addAttribute("account", account);
        return "forderClient/EditUser";
    }

    // @PostMapping("/updateProfile")
    // public String processUpdateProfile(@RequestParam String fullName, 
    //                                    @RequestParam String email, 
    //                                    @RequestParam int phone, 
    //                                    @RequestParam String address, 
    //                                    HttpSession session) {
    //     String username = (String) session.getAttribute("username");
    //     if (username == null) {
    //         return "redirect:/login";
    //     }
    //     updateAccountDetails(username, fullName, email, phone, address);
    //     return "redirect:/";  // Redirect to the profile page or confirmation page
    // }

    @PostMapping("/updateProfile")
public String processUpdateProfile(@RequestParam String newUsername, 
                                   @RequestParam String fullName, 
                                   @RequestParam String email, 
                                   @RequestParam int phone, 
                                   @RequestParam String address, 
                                   HttpSession session) {
    String oldUsername = (String) session.getAttribute("username");
    if (oldUsername == null) {
        return "redirect:/login";
    }

    if (!oldUsername.equals(newUsername)) {
        // Optional: Check if the new username is already taken
        if (fetchAccountByUsername(newUsername) != null) {
            // Handle the case where the new username is already in use
            // This could redirect back to the form with an error message
            return "redirect:/editUser";
        }
    }

    updateAccountDetails(oldUsername, newUsername, fullName, email, phone, address);
    session.setAttribute("username", newUsername);  // Update the session to the new username
    return "redirect:/";  // Redirect to a confirmation or profile page
}


    private Account fetchAccountByUsername(String username) {
        String sql = "SELECT * FROM accounts WHERE username = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{username}, (rs, rowNum) ->
                new Account(rs.getInt("id"), rs.getString("username"),
                            rs.getString("password"), rs.getString("fullName"),
                            rs.getString("email"), rs.getInt("phone"),
                            rs.getString("address"), rs.getString("address"),
                            rs.getBoolean("status"), rs.getBoolean("role"))
            );
        } catch (EmptyResultDataAccessException e) {
            return null; // or handle differently if needed
        }
    }
        

    // private void updateAccountDetails(String username, String fullName, String email, int phone, String address) {
    //     String sql = "UPDATE accounts SET fullName = ?, email = ?, phone = ?, address = ? WHERE username = ?";
    //     jdbcTemplate.update(sql, fullName, email, phone, address, username);
    // }
    private void updateAccountDetails(String oldUsername, String newUsername, String fullName, String email, int phone, String address) {
        String sql = "UPDATE accounts SET username = ?, fullName = ?, email = ?, phone = ?, address = ? WHERE username = ?";
        jdbcTemplate.update(sql, newUsername, fullName, email, phone, address, oldUsername);
    }
    
}
