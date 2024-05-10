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
    return "forderAdmin/categories";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        // List<Image> images = img.getAllImageInfos();
        // model.addAttribute("images", images);
        model.addAttribute("category", new Category());
        return "forderAdmin/add-category";
    }

    @PostMapping("/add")
    public String addCategory(@ModelAttribute("category") Category category) {
        CategoryRepository.Instance().insert(category);
        return "redirect:/admin/categories";
    }

    @GetMapping("/edit")
    public String showEditForm(@RequestParam("id") int id, Model model) {
        Category category = CategoryRepository.Instance().findById(id);
        model.addAttribute("category", category);
        return "forderAdmin/edit-category";
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