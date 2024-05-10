package project.spring.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import project.spring.model.Account;
import project.spring.model.Category;
import project.spring.model.Comment;
import project.spring.model.News;
import project.spring.model.Order;
import project.spring.model.Product;
import project.spring.repositories.AccountRepository;
import project.spring.repositories.CategoryRepository;
import project.spring.repositories.CommentRepository;
import project.spring.repositories.DiscountRepository;
import project.spring.repositories.ImageRepository;
import project.spring.repositories.NewsRepository;
import project.spring.repositories.OrderRepository;
import project.spring.repositories.ProductRepository;

@Controller
public class HomeController {
	@Autowired
	private AccountRepository accountRepository;
	@GetMapping("/tin-tuc")
	public String tintuc(@RequestParam(defaultValue = "1") int page,
						@RequestParam(required = false) String keyword,
						Model model) {
		List<Category> categories = CategoryRepository.Instance().findAll();
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

		model.addAttribute("categories", categories);
		model.addAttribute("news", news);
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("currentPage", page);
		model.addAttribute("newsRecent", newsRecent);
		return "forderClient/news-c";
	}


	@GetMapping("/tin-tuc/{path}") //Trang chi tiết tin tức
	public String tintuc_detail(@PathVariable("path") String path, Model model) {
		List<Category> categories = CategoryRepository.Instance().findAll();
		News news = NewsRepository.Instance().findByPath(path);
		List<News> newsRecent = NewsRepository.Instance().findRecent();//4 bài viết mới nhất
		List<News> newsRand = NewsRepository.Instance().findRand();//3 bài viết ngẫu nhiên
		Account account = accountRepository.findById(news.getId()); //Lấy người dùng tạo bài viết này 
		if(news != null) {
			model.addAttribute("categories", categories);
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
		List<Map<String, Object>> productNew = productRepository.findNewProduct();
		List<Map<String, Object>> products;
		int totalProduct;

		if (keyword != null && !keyword.isEmpty()) {
			products = productRepository.search(keyword);
			totalProduct = products.size();
		} else {
			products = productRepository.findAll2();
			totalProduct = products.size();
		}
		model.addAttribute("categories", categories); // Danh sách Loại sản phẩm
		model.addAttribute("keyword", keyword);
		model.addAttribute("productNew", productNew);
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
		List<Map<String, Object>> productNew = productRepository.findNewProduct();					

		int pageSize = 12;
		List<Map<String, Object>> products;
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
		List<Map<String, Object>> productNew = productRepository.findNewProduct();				
		Category category = CategoryRepository.Instance().findByPath(path);
		int pageSize = 6;
		List<Map<String, Object>> products;
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
	@GetMapping("/san-pham/{path}") 
	public String sanpham_detail(@PathVariable("path") String path, Model model) {
		Product product = productRepository.findByPath(path);
		Map<String, Object> discount = productRepository.findByPath2(path);
		List<Map<String, Object>> productRand = productRepository.findRand(product.getCategoryId());//4 sản phẩm thuộc loại ngẫu nhiên
		Category categoryDetail = CategoryRepository.Instance().findById(product.getCategoryId());
		List<Category> categories = CategoryRepository.Instance().findAll();
		List<Map<String, Object>> image = ImageRepository.Instance().getImageByProduct(product.getId());
		Map<String, Object> mainImage = ImageRepository.Instance().getMainImage(product.getId());

		//bình luận đánh giá
		List<Map<String, Object>> comments = CommentRepository.Instance().findByProduct(product.getId());
		double avgStart = CommentRepository.Instance().avgStart(product.getId());
		model.addAttribute("comments", comments); 
		model.addAttribute("avgStart", avgStart); 

		if(product != null) {
			model.addAttribute("discount", discount); 
			model.addAttribute("product", product); 
			model.addAttribute("image", image); 
			model.addAttribute("mainImage", mainImage); 
        	model.addAttribute("productRand", productRand);
			model.addAttribute("categoryDetail", categoryDetail); 
			model.addAttribute("categories", categories); // Danh sách Loại sản phẩm
		return "forderClient/san-pham-detail";
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
		List<News> newsRand = NewsRepository.Instance().findRand();//3 bài viết ngẫu nhiên
		List<Category> categories = CategoryRepository.Instance().findAll();
		List<Map<String, Object>> productNew = productRepository.findNewProduct();		
		List<Map<String, Object>> products = productRepository.find4ProductByCategory();		

		model.addAttribute("newsRand", newsRand);
		model.addAttribute("categories", categories); // Danh sách Loại sản phẩm
		model.addAttribute("productNew", productNew); // Danh sách 6 sản phẩm mới
		model.addAttribute("products", products); // Danh sách sản phẩm

		return "Client/index";
	}

	@GetMapping("/profile/{username}")
	public String profile(@PathVariable String username, Model model) {
		Account accounts = accountRepository.findByUserName(username);
		// List<Order> order = orderRepository.
		List<Map<String, Object>> orders = OrderRepository.Instance().getOrdersAccount(username);
		
		model.addAttribute("orders", orders);
		model.addAttribute("accounts", accounts);
		return "forderClient/profileUser/profile";
	
	}

	@GetMapping("/khuyen-mai")
	public String discount(Model model) {
		List<Map<String, Object>> discounts = productRepository.findProductByDiscount();
		List<Category> categories = CategoryRepository.Instance().findAll();
		List<Map<String, Object>> productNew = productRepository.findNewProduct();
		model.addAttribute("productNew", productNew);
		model.addAttribute("products", discounts);
		model.addAttribute("categories", categories);
		return "forderClient/khuyen-mai";
	
	}







}
