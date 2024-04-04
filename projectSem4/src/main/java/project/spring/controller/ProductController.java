package project.spring.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;
import project.spring.dao.ProductDao;
import project.spring.model.Category;
import project.spring.model.Product;

@Controller
@RequestMapping("/admin/product")
public class ProductController {
	@Autowired
    private ProductDao productDao;
    
  @Autowired
    private HttpServletRequest request;
/////
	private String getRole() {
        return (String) request.getSession().getAttribute("role");
    }
    private boolean isAdmin() {
        String role = getRole();
        return role != null && role.equals("1");
    }
////////

   @GetMapping("")
    public String getAllProducts(Model model) {
       if (!isAdmin()) {
        return "error"; // hoặc return "redirect:/admin-login"; nếu muốn chuyển hướng đến trang đăng nhập của admin
    }
    List<Product> products = productDao.findAll();
    model.addAttribute("products", products);
        
    return "forderAdmin/newProduct";
    }

	 @GetMapping("/add")
    public String getProductById( Model model) {
        model.addAttribute("product", new Product());
        return "forderAdmin/CreateProduct";
    }

    @PostMapping("/add")
    public String createProduct(Product product) {
        if (product.getName() != null) {
            productDao.insert(product);
            return "redirect:/admin/product/CreateProduct";
        } else {
            // Xử lý trường hợp tên sản phẩm là null
            return "redirect:/error";
        }
    }

    @PostMapping("/product/{id}")
    public String updateProduct(@RequestParam("id") int id, Product updatedProduct) {
        updatedProduct.setId(id);
     productDao.update(updatedProduct);
        return "redirect:/product";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@RequestParam("id") int id) {
     productDao.deleteById(id);
        return "redirect:/admin/product";
    }
}
