package project.spring.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import project.spring.model.Category;
import project.spring.model.Product;
import project.spring.repositories.CategoryRepository;
import project.spring.repositories.ProductRepository;

@Controller
@RequestMapping("/admin/product")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;
    
@GetMapping("")
public String getAllCategories(Model model) {
    List<Map<String, Object>> products = productRepository.findAll2();
    model.addAttribute("products", products);
return "forderAdmin/product/products";
}



@GetMapping("/add")
public String showAddForm(Model model) {
    model.addAttribute("product", new Product());
    List<Category> categories = CategoryRepository.Instance().findAll(); // Lấy danh sách loại sản phẩm
    model.addAttribute("categories", categories);
    return "forderAdmin/product/add-product";
}


@PostMapping("/add")
public String addProduct(@ModelAttribute("product") Product product) {
    productRepository.insert(product);
    return "redirect:/admin/product";
}

@GetMapping("/edit")
public String showEditForm(@RequestParam("id") int id, Model model) {
    Product product = productRepository.findById(id);
    model.addAttribute("product", product);
    return "forderAdmin/product/edit-product";
}

@PostMapping("/edit")
public String updateCategory(@RequestParam("id") int id,
        @ModelAttribute("product") Product product) {
    product.setId(id);
    productRepository.update(product);
    return "redirect:/admin/product";
}

@GetMapping("/delete/{id}")
public String deleteCategory(@PathVariable("id") int id) {
    productRepository.deleteById(id);
    return "redirect:/admin/product";
}
}
