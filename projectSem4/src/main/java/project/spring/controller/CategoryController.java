package project.spring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import project.spring.model.Category;
import project.spring.repositories.CategoryRepository;

import java.util.List;

@Controller
@RequestMapping("/admin/categories")
public class CategoryController {@GetMapping("/")
public String getAllCategories(Model model) {
    List<Category> categories = CategoryRepository.Instance().findAll();
    model.addAttribute("categories", categories);
    return "Admin/categories";
}

// @GetMapping("/add")
// public String showAddForm(Model model) {
// model.addAttribute("category", new Category());
// return "add-category";
// }

// @PostMapping("/add")
// public String addCategory(@ModelAttribute("category") Category category) {
// categoryRepository.insert(category);
// return "redirect:/categories";
// }

// @GetMapping("/edit/{id}")
// public String showEditForm(@PathVariable("id") int id, Model model) {
// Category category = categoryRepository.findById(id);
// model.addAttribute("category", category);
// return "edit-category";
// }

// @PostMapping("/edit/{id}")
// public String updateCategory(@PathVariable("id") int id,
// @ModelAttribute("category") Category category) {
// category.setId(id);
// categoryRepository.update(category);
// return "redirect:/categories";
// }

// @GetMapping("/delete/{id}")
// public String deleteCategory(@PathVariable("id") int id) {
// categoryRepository.deleteById(id);
// return "redirect:/categories";
// }
}