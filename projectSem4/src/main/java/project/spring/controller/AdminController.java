package project.spring.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;
import project.spring.dao.ProductDao;
import project.spring.model.Product;

@Controller
@RequestMapping("/admin")
public class AdminController {
	@Autowired
	private ProductDao dao;
	
  @Autowired
    private HttpServletRequest request;
	@RequestMapping("")
	public String index() {
		// String role = (String) request.getSession().getAttribute("role");
        // if (role == null || !role.equals("1")) {
           
        //     return "redirect:/login"; // Ví dụ: chuyển hướng đến trang đăng nhập
        // }
        return "Admin/index";
	}

	@RequestMapping("/Product")
	public String product(Model model) {
		String role = (String) request.getSession().getAttribute("role");
        if (role == null || !role.equals("1")) {
            return "redirect:/login"; // Chuyển hướng đến trang đăng nhập nếu không phải là admin
        }
		List<Product> listProduct = dao.list();
		model.addAttribute("listproduct", listProduct);
		return "/Admin/indexadmin";
	}

	@RequestMapping("/create")
	public String create(Model model) {
		String role = (String) request.getSession().getAttribute("role");
        if (role == null || !role.equals("1")) {
            return "redirect:/login"; // Chuyển hướng đến trang đăng nhập nếu không phải là admin
        }
		Product products = new Product();
		model.addAttribute("products", products);
		return "forderAdmin/CreateProduct";
	}

	@RequestMapping("/save")
	public String save(@ModelAttribute("products") Product products) {
		String role = (String) request.getSession().getAttribute("role");
        if (role == null || !role.equals("1")) {
            return "redirect:/login"; // Chuyển hướng đến trang đăng nhập nếu không phải là admin
        }
		dao.save(products);
		return "redirect:/admin/Product";
	}

	 @RequestMapping("/delete/{id}")
    public String delete(@PathVariable("id") int id) {
		String role = (String) request.getSession().getAttribute("role");
        if (role == null || !role.equals("1")) {
            return "redirect:/login"; // Chuyển hướng đến trang đăng nhập nếu không phải là admin
        }
        dao.delete(id);
        return "redirect:/admin/Product";
    }
}
