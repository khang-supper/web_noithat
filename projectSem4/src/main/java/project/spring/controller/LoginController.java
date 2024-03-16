// package project.spring.controller;

// import org.springframework.stereotype.Controller;
// import org.springframework.ui.Model;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.ModelAttribute;
// import org.springframework.web.bind.annotation.PostMapping;

// import project.spring.model.Account;
// @Controller
// public class LoginController {
//  @GetMapping("/login")
//  public String showLogin() {
//   return "forderClient/login";
// }
//  @PostMapping("/login")
//  public String login(@ModelAttribute(name="loginForm") Account account, Model m) {
//   String uname = account.getUserName();
//   String pass = account.getPassword();
//   if(uname.equals("Admin") && pass.equals("123")) {
//    m.addAttribute("uname", uname);
//    m.addAttribute("pass", pass);
//    return "/Client/index";
//   }
//   m.addAttribute("error", "Incorrect UserName & Password");
//   return "/forderClient/login";
//  }
// }
package project.spring.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

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

    @PostMapping("/login")
public String login(@ModelAttribute(name="loginForm") Account account, Model model, HttpSession session) {
    String username = account.getUserName();
    String password = account.getPassword();
    
  
    String role = checkUserRole(username, password);
    if (role != null) {
        if (role.equals("1")) {
            // Nếu người dùng có vai trò là "admin", chuyển hướng đến trang quản trị
            model.addAttribute("username", username);
              session.setAttribute("role", role);
            return "/Admin/indexadmin"; 
        } else {
            // Nếu người dùng không phải là "admin", chuyển hướng đến trang chính
            model.addAttribute("username", username);
              session.setAttribute("role", role);
            return "/Client/index"; 
        }
    } else {
        //  đăng nhập không hợp lệ, trả về trang đăng nhập với thông báo lỗi
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
        // Xóa session để đăng xuất người dùng
        session.invalidate();
        return "/forderClient/login";
    }
    
    // Phương thức kiểm tra thông tin đăng nhập
    private String checkUserRole(String username, String password) {
      String sql = "SELECT role FROM account WHERE username = ? AND password = ?";
      try {
          return jdbcTemplate.queryForObject(sql, String.class, username, password);
      } catch (Exception e) {
          return null; 
      }
  }
  
  
}
