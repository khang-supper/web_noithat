package project.spring.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import project.spring.model.Product;
import project.spring.model.ProductDao;

@Controller
@RequestMapping("/admin")
public class AdminController {
	@Autowired
	private ProductDao dao;

	@RequestMapping("")
	public String index() {
		return ("Admin/index");
	}

	// @RequestMapping("/")
	// public String lint() {
	// return ("forderAdmin/Create");
	// }
	@RequestMapping("/Product")
	public String product(Model model) {
		List<Product> listProduct = dao.list();
		model.addAttribute("listproduct", listProduct);
		return "/Admin/indexadmin";
	}

	@RequestMapping("/create")
	public String create(Model model) {
		Product product = new Product();
		model.addAttribute("product", product);

		return "forderAdmin/create";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(@ModelAttribute("product") Product product) {
		dao.save(product);

		return "/Admin/indexadmin";
	}
}
