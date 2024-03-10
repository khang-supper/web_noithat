package project.spring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {
	@RequestMapping("")
	public String index() {
		return("Admin/indexadmin");
	}
	
	@RequestMapping("/students")
	public String students() {
		return("forderClient/students");
	}

}
