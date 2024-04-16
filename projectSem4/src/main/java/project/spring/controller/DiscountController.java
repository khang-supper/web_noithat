package project.spring.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;
import project.spring.model.Account;
import project.spring.model.Discount;
import project.spring.repositories.AccountRepository;
import project.spring.repositories.DiscountRepository;

@Controller
@RequestMapping("/admin/discount")
public class DiscountController {
    @Autowired
    private HttpServletRequest request;

    @Autowired
    private DiscountRepository discountRepository;

    @Autowired
private AccountRepository accountRepository;
    
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
public String getAlldiscounts(Model model) {
    List<Discount> discounts = discountRepository.findAll();
    model.addAttribute("discounts", discounts);
return "forderAdmin/discount/discounts";
}


@GetMapping("/add")
public String showAddForm(Model model) {
    model.addAttribute("discount", new Discount());
     List<Account> accounts = accountRepository.findAll(); // Lấy danh sách loại sản phẩm
    model.addAttribute("accounts", accounts);
    return "forderAdmin/discount/add-discount";
}

@PostMapping("/add")
public String adddiscount(@ModelAttribute("discount") Discount discount) {
    discountRepository.insert(discount);
    return "redirect:/admin/discount";
}

@GetMapping("/edit")
public String showEditForm(@RequestParam("id") int id, Model model) {
    Discount discount = discountRepository.findById(id);
    model.addAttribute("discount", discount);
    return "forderAdmin/discount/edit-discount";
}

@PostMapping("/edit")
public String updatediscount(@RequestParam("id") int id,
        @ModelAttribute("discount") Discount discount) {
    discount.setId(id);
    discountRepository.update(discount);
    return "redirect:/admin/discount";
}

@GetMapping("/delete/{id}")
public String deletediscount(@PathVariable("id") int id) {
    discountRepository.deleteById(id);
    return "redirect:/admin/discount";
}
@GetMapping("/role/delete/{id}")
public String deletediscounts(@PathVariable("id") int id) {
    discountRepository.deleteById(id);
    return "redirect:/admin/discount/admin";
}
}
