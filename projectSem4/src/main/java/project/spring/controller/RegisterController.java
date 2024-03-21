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
            model.addAttribute("account", new Account());
            return "forderClient/register"; 
        }

        @PostMapping("/processRegister")
        public String register(@ModelAttribute(name = "register") Account account, Model model) {
            boolean registrationSuccess = registerUser(account);
            if (registrationSuccess) {
                model.addAttribute("message", "Registration successful. You can now login.");
                return "forderClient/login"; 
            } else {
                model.addAttribute("error", "Registration failed. Please try again.");
                return "forderClient/register"; 
            }
        }

        private boolean registerUser(Account account) {
            String sql = "INSERT INTO account (username, password, email, isAdmin) VALUES (?, ?, ?)";
            try {
                jdbcTemplate.update(sql, account.getUsername(), account.getPassword(), account.getEmail());
                return true;
            } catch (Exception e) {
                return false;
            }
        }
        
    }
