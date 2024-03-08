package project.pring.websevice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller

public class HomeController {
	@RequestMapping("")
	public String index() {
		return "Client/index";
	}	
	@RequestMapping("/login")
	public String login() {
		return "forderClient/login";
	}	
	@RequestMapping("/pages")
	public String pages() {
		return "forderClient/pages";
	}
	@RequestMapping("/shop")
	public String shop() {
		return "forderClient/shop";
	}
	@RequestMapping("/details")
	public String details() {
		return "forderClient/shop-details";
	}
	@RequestMapping("/cart")
	public String cart() {
		return "forderClient/shoping-cart";
	}
	@RequestMapping("/checkout")
	public String checkout() {
		return "forderClient/checkout";
	}
}
