package project.spring.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import project.spring.model.Account;
import project.spring.model.News;
import project.spring.repositories.AccountRepository;
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
	// @GetMapping("/tin-tuc")
	// public String tintuc(@RequestParam(defaultValue = "1") int page, Model model) {
	// 	List<News> newsRecent = NewsRepository.Instance().findRecent(); //lấy bài viết gần nhất
	// 	int pageSize = 4; // Số lượng mục trên mỗi trang
    //     List<News> news = NewsRepository.Instance().findAllByPage(page - 1, pageSize);
    //     int totalPages = NewsRepository.Instance().getTotalPages(pageSize);
    //     model.addAttribute("news", news);
    //     model.addAttribute("totalPages", totalPages);
    //     model.addAttribute("currentPage", page); // Trang hiện tại
    //     model.addAttribute("newsRecent", newsRecent);
    //     return "forderClient/news-c";
	// }

	@GetMapping("/tin-tuc")
	public String tintuc(@RequestParam(defaultValue = "1") int page,
						@RequestParam(required = false) String keyword,
						Model model) {
		List<News> newsRecent = NewsRepository.Instance().findRecent();
		int pageSize = 4;
		List<News> news;
		int totalPages;

		if (keyword != null && !keyword.isEmpty()) {
			news = NewsRepository.Instance().search(keyword);
			totalPages = 1; // Không phân trang cho kết quả tìm kiếm
		} else {
			news = NewsRepository.Instance().findAllByPage(page - 1, pageSize);
			totalPages = NewsRepository.Instance().getTotalPages(pageSize);
		}

		model.addAttribute("news", news);
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("currentPage", page);
		model.addAttribute("newsRecent", newsRecent);
		return "forderClient/news-c";
	}


	@GetMapping("/tin-tuc/{path}")
	public String tintuc_detail(@PathVariable("path") String path, Model model) {
		News news = NewsRepository.Instance().findByPath(path);
		List<News> newsRecent = NewsRepository.Instance().findRecent();//4 bài viết mới nhất
		List<News> newsRand = NewsRepository.Instance().findRand();//3 bài viết ngẫu nhiên
		Account account = AccountRepository.Instance().findById(news.getId()); //Lấy người dùng tạo bài viết này 
		if(news != null) {
			model.addAttribute("newsDetail", news); 
        	model.addAttribute("newsRecent", newsRecent);
        	model.addAttribute("newsRand", newsRand);
        	model.addAttribute("account", account);
		return "forderClient/news-detail";
		} 
		else {
			// Xử lý trường hợp không tìm thấy tin tức
			return "error-page"; // Thay thế "error-page" bằng trang lỗi hoặc chuyển hướng tới trang khác
		}
	}

}
