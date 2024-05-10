package project.spring.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import project.spring.model.Discount;
import project.spring.repositories.DiscountRepository;
import project.spring.repositories.ProductRepository;

@Controller
@RequestMapping("/admin/discount")
public class DiscountController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("")
    public String getAlldiscounts(Model model) {
        List<Discount> discounts = DiscountRepository.Instance().findAll();
        model.addAttribute("discounts", discounts);
        return "forderAdmin/discount/discounts";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("discount", new Discount());
        return "forderAdmin/discount/add-discount";
    }

    @PostMapping("/add")
    public String adddiscount(@ModelAttribute("discount") Discount discount) {
        DiscountRepository.Instance().insert(discount);
        return "redirect:/admin/discount";
    }

    // @GetMapping("/edit")
    // public String showEditForm(@RequestParam("id") int id, Model model) {
    //     Discount discount = DiscountRepository.Instance().findById(id);
    //     List<Map<String, Object>> products = productRepository.findAll2();
    //     model.addAttribute("products", products);
    //     model.addAttribute("discount", discount);
    //     return "forderAdmin/discount/edit-discount";
    // }
    @GetMapping("/edit")
    public String showEditForm(@RequestParam("id") int id, Model model) {
        Discount discount = DiscountRepository.Instance().findById(id);
        List<Map<String, Object>> products = productRepository.findAll2();
        
        // Kiểm tra xem sản phẩm đã được thêm vào chương trình khuyến mãi hay chưa
        for (Map<String, Object> product : products) {
            boolean isAdded = DiscountRepository.Instance().isProductAddedToDiscount(discount.getId(), (int) product.get("id"));
            product.put("isAddedToDiscount", isAdded);
        }

        model.addAttribute("products", products);
        model.addAttribute("discount", discount);
        return "forderAdmin/discount/edit-discount";
    }


    @PostMapping("/edit")
    public String updatediscount(@RequestParam("id") int id,
            @ModelAttribute("discount") Discount discount) {
        discount.setId(id);
        DiscountRepository.Instance().update(discount);
        return "redirect:/admin/discount";
    }

    @PostMapping("/add-product-to-discount")
    @ResponseBody
    public String addProductToDiscount(@RequestParam("productId") int productId, @RequestParam("discountId") int discountId) {
        int rowsAffected = DiscountRepository.Instance().updateDiscountId(discountId, productId);
        if (rowsAffected > 0) {
            return "success"; 
        } else {
            return "failed"; 
        }
    }


    @GetMapping("/delete/{id}")
    public String deletediscount(@PathVariable("id") int id) {
        DiscountRepository.Instance().deleteById(id);
        return "redirect:/admin/discount";
    }

}
