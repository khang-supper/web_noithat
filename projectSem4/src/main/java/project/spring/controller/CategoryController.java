package project.spring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import project.spring.model.Category;
import project.spring.repositories.CategoryRepository;

import java.util.List;

@Controller
@RequestMapping("/admin/categories")
public class CategoryController {

    @GetMapping("")
    public String getAllCategories(Model model) {
    List<Category> categories = CategoryRepository.Instance().findAll();
    model.addAttribute("categories", categories);
    return "Admin/categories";
    }

    // @GetMapping("")
    // public String getAllCategories(@RequestParam(defaultValue = "1") int page, Model model) {
    //     int pageSize = 1; // Số lượng mục trên mỗi trang
    //     List<Category> categories = CategoryRepository.Instance().findAllByPage(page - 1, pageSize);
    //     int totalPages = CategoryRepository.Instance().getTotalPages(pageSize);
    //     model.addAttribute("categories", categories);
    //     model.addAttribute("totalPages", totalPages);
    //     model.addAttribute("currentPage", page); // Trang hiện tại
    //     return "Admin/categories";
    // }
    
    

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("category", new Category());
        return "Admin/add-category";
    }

    // @PostMapping("/add")
    // public String addCategory(@ModelAttribute("category") Category category) {
    //     CategoryRepository.Instance().insert(category);
    //     return "redirect:/admin/categories";
    // }

    @GetMapping("/edit")
    public String showEditForm(@RequestParam("id") int id, Model model) {
        Category category = CategoryRepository.Instance().findById(id);
        model.addAttribute("category", category);
        return "Admin/edit-category";
    }

    @PostMapping("/edit")
    public String updateCategory(@RequestParam("id") int id,
            @ModelAttribute("category") Category category) {
        category.setId(id);
        CategoryRepository.Instance().update(category);
        return "redirect:/admin/categories";
    }

    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable("id") int id) {
        CategoryRepository.Instance().deleteById(id);
        return "redirect:/admin/categories";
    }
}