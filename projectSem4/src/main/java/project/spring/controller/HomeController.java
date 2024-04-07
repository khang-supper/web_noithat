package project.spring.controller;

import java.util.List;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import ch.qos.logback.classic.Logger;
import project.spring.model.News;
import project.spring.repositories.NewsRepository;

@Controller
public class HomeController {
	@RequestMapping("/")
	public String index(Model model) {
		model.addAttribute("title", "Trang home");
		return "Client/index";
	}	
	
	@RequestMapping("/pages")
	public String pages(Model model) {
		model.addAttribute("title", "Trang pages");
		return "forderClient/pages";
	}
	@RequestMapping("/shop")
	public String shop(Model model) {
		model.addAttribute("title", "Trang shop");
		return "forderClient/shop";
	}
	@RequestMapping("/details")
	public String details(Model model) {
		model.addAttribute("title", "Trang shop-details");
		return "forderClient/shop-details";
	}
	@RequestMapping("/cart")
	public String cart(Model model) {
		model.addAttribute("title", "Trang shoping-cart");
		return "forderClient/shoping-cart";
	}
	@RequestMapping("/checkout")
	public String checkout(Model model) {
		model.addAttribute("title", "Trang checkout");
		return "forderClient/checkout";
	}
	@GetMapping("/tin-tuc")
	public String tintuc(Model model) {
		List<News> news = NewsRepository.Instance().findAll();
        model.addAttribute("news", news);
        return "forderClient/news-c";
	}
	@GetMapping("/tin-tuc/{path}")
	public String tintuc_detail(@PathVariable("path") String path, Model model) {
		News news = NewsRepository.Instance().findByPath(path);
		if(news != null) {
			model.addAttribute("newsDetail", news); 
			return "forderClient/news-detail";
		} else {
			// Xử lý trường hợp không tìm thấy tin tức
			return "error-page"; // Thay thế "error-page" bằng trang lỗi hoặc chuyển hướng tới trang khác
		}
	}

}
