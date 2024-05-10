package project.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/admin")
public class AdminController {
	  @Autowired
    private HttpServletRequest request;
	private String getRole() {
        return (String) request.getSession().getAttribute("role");
    }
    private boolean isAdmin() {
        String role = getRole();
        return role != null && role.equals("1");
    }
    @RequestMapping("")
    public String index(Model model) {
	// 	if (!isAdmin()) {
	// 		model.addAttribute("title", "Error");
	// 		model.addAttribute("error", "Bạn không có quyền truy cập!");
	// 		return "forderAdmin/error"; 
	// 	}
		model.addAttribute("title", "Trang admin");
		return "Admin/index";
	}	
}
