package project.spring.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import project.spring.dao.ProductDao;
import project.spring.model.Account;
import project.spring.model.Category;
import project.spring.model.Image;
import project.spring.model.News;
import project.spring.model.Product;
import project.spring.repositories.AccountRepository;
import project.spring.repositories.CategoryRepository;
import project.spring.repositories.NewsRepository;
import project.spring.repositories.ProductRepository;

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
	@Autowired
	private AccountRepository accountRepository;
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


	@GetMapping("/tin-tuc/{path}") //Trang chi tiết tin tức
	public String tintuc_detail(@PathVariable("path") String path, Model model) {
		News news = NewsRepository.Instance().findByPath(path);
		List<News> newsRecent = NewsRepository.Instance().findRecent();//4 bài viết mới nhất
		List<News> newsRand = NewsRepository.Instance().findRand();//3 bài viết ngẫu nhiên
		Account account = accountRepository.findById(news.getId()); //Lấy người dùng tạo bài viết này 
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

	@Autowired
	private ProductRepository productRepository;

	@GetMapping("/search/product") //Trang tìm kiếm sản phẩm
	public String search_product(@RequestParam(required = false) String keyword, Model model) {
		List<Category> categories = CategoryRepository.Instance().findAll();
		List<Product> products;
		int totalProduct;

		if (keyword != null && !keyword.isEmpty()) {
			products = productRepository.search(keyword);
			totalProduct = products.size();
		} else {
			products = productRepository.findAll();
			totalProduct = products.size();
		}
		model.addAttribute("categories", categories); // Danh sách Loại sản phẩm
		model.addAttribute("keyword", keyword);
		model.addAttribute("products", products);
		model.addAttribute("totalProduct", totalProduct);
		return "forderClient/search-product";
	}
	
	@GetMapping("/san-pham") // Trang tất cả sản phẩm
	public String san_pham(@RequestParam(defaultValue = "1") int page,
						@RequestParam(required = false) String sortPrice,
						@RequestParam(required = false) Double priceFrom,
						@RequestParam(required = false) Double priceTo,
						Model model) {
		List<Category> categories = CategoryRepository.Instance().findAll();
		List<Product> productNew = productRepository.findNewProduct();					

		int pageSize = 12;
		List<Product> products;
		int totalPages;
		int totalProduct;

		if ((sortPrice != null && !sortPrice.isEmpty()) || (priceFrom != null && priceTo != null)) {
			// Lọc theo giá hoặc thứ tự giá
			products = productRepository.findAllByParams(sortPrice, priceFrom, priceTo);
			totalProduct = productRepository.getProductCountByParams(sortPrice, priceFrom, priceTo);
			totalPages = 1;
		} else {
			// Phân trang khi không có lọc
			products = productRepository.findAllByPage(page - 1, pageSize);
			totalProduct = productRepository.getProductCount();
			totalPages = productRepository.getTotalPages(pageSize);
		}

		model.addAttribute("categories", categories); // Danh sách Loại sản phẩm
		model.addAttribute("productNew", productNew); // Danh sách 6 sản phẩm mới
		model.addAttribute("products", products); // Danh sách sản phẩm
		model.addAttribute("totalPages", totalPages); // Tổng số trang
		model.addAttribute("currentPage", page); // Trang hiện tại
		model.addAttribute("totalProduct", totalProduct); // Tổng số sản phẩm
		return "forderClient/san-pham";
	}

	@GetMapping("/{path}")//Trang loại sản phẩm
	public String category(@PathVariable("path") String path, @RequestParam(defaultValue = "1") int page,
							@RequestParam(required = false) String sortPrice,
							@RequestParam(required = false) Double priceFrom,
							@RequestParam(required = false) Double priceTo,
							Model model) {
		List<Category> categories = CategoryRepository.Instance().findAll();
		List<Product> productNew = productRepository.findNewProduct();				
		Category category = CategoryRepository.Instance().findByPath(path);
		int pageSize = 3;
		List<Product> products;
		int totalPages;
		int totalProduct;

		if ((sortPrice != null && !sortPrice.isEmpty()) || (priceFrom != null && priceTo != null)) {
			// Lọc theo giá hoặc thứ tự giá
			products = productRepository.findAllByParamsAndCategory(sortPrice, priceFrom, priceTo, category.getId());
			totalProduct = productRepository.getProductCountByParamsAndCategory(sortPrice, priceFrom, priceTo, category.getId());
			totalPages = 1;
		} else {
			// Phân trang khi không có lọc
			products = productRepository.findAllByPageAndCategory(page - 1, pageSize, category.getId());
			totalProduct = productRepository.getProductCountByCategory(category.getId());
			totalPages = productRepository.getTotalPagesByCategory(pageSize, category.getId());
		}

		model.addAttribute("categoryDetail", category); // Loại sản phẩm
		model.addAttribute("categories", categories); // Danh sách Loại sản phẩm
		model.addAttribute("productNew", productNew); // Danh sách 6 sản phẩm mới
		model.addAttribute("products", products); // Danh sách sản phẩm
		model.addAttribute("totalPages", totalPages); // Tổng số trang
		model.addAttribute("currentPage", page); // Trang hiện tại
		model.addAttribute("totalProduct", totalProduct); // Tổng số sản phẩm


		return "forderClient/loai-san-pham";
	}
	//Trang chi tiết sản phẩm
	@GetMapping("/san-pham/{path}") //Trang chi tiết tin tức
	public String sanpham_detail(@PathVariable("path") String path, Model model) {
		News news = NewsRepository.Instance().findByPath(path);
		List<News> newsRecent = NewsRepository.Instance().findRecent();//4 bài viết mới nhất
		List<News> newsRand = NewsRepository.Instance().findRand();//3 bài viết ngẫu nhiên
		Account account = accountRepository.findById(news.getId()); //Lấy người dùng tạo bài viết này 
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

	@GetMapping("/") // Trang home
	public String home(@RequestParam(defaultValue = "1") int page,
						@RequestParam(required = false) String sortPrice,
						@RequestParam(required = false) Double priceFrom,
						@RequestParam(required = false) Double priceTo,
						Model model) {
		List<Category> categories = CategoryRepository.Instance().findAll();
		List<Product> productNew = productRepository.findNewProduct();		
		List<Product> products = productRepository.find4ProductByCategory();		


		model.addAttribute("categories", categories); // Danh sách Loại sản phẩm
		model.addAttribute("productNew", productNew); // Danh sách 6 sản phẩm mới
		model.addAttribute("products", products); // Danh sách sản phẩm

		return "Client/index";
	}
//////
@GetMapping("/categories/{path}")//Trang loại sản phẩm
	public String categorys(@PathVariable("path") String path, @RequestParam(defaultValue = "1") int page,
							@RequestParam(required = false) String sortPrice,
							@RequestParam(required = false) Double priceFrom,
							@RequestParam(required = false) Double priceTo,
							Model model) {
		List<Category> categories = CategoryRepository.Instance().findAll();
		List<Product> productNew = productRepository.findNewProduct();				
		Category category = CategoryRepository.Instance().findByPath(path);
		int pageSize = 6;
		List<Product> products;
		int totalPages;
		int totalProduct;

		if ((sortPrice != null && !sortPrice.isEmpty()) || (priceFrom != null && priceTo != null)) {
			// Lọc theo giá hoặc thứ tự giá
			products = productRepository.findAllByParamsAndCategory(sortPrice, priceFrom, priceTo, category.getId());
			totalProduct = productRepository.getProductCountByParamsAndCategory(sortPrice, priceFrom, priceTo, category.getId());
			totalPages = 1;
		} else {
			// Phân trang khi không có lọc
			products = productRepository.findAllByPageAndCategory(page - 1, pageSize, category.getId());
			totalProduct = productRepository.getProductCountByCategory(category.getId());
			totalPages = productRepository.getTotalPagesByCategory(pageSize, category.getId());
		}
		model.addAttribute("categoryDetail", category); // Loại sản phẩm
		model.addAttribute("categories", categories); // Danh sách Loại sản phẩm
		model.addAttribute("productNew", productNew); // Danh sách 6 sản phẩm mới
		model.addAttribute("products", products); // Danh sách sản phẩm
		model.addAttribute("totalPages", totalPages); // Tổng số trang
		model.addAttribute("currentPage", page); // Trang hiện tại
		model.addAttribute("totalProduct", totalProduct); // Tổng số sản phẩm


		return "Client/index";
	}



}
