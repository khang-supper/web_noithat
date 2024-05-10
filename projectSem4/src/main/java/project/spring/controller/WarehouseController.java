package project.spring.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import project.spring.repositories.ProductRepository;


@Controller
@RequestMapping("/admin/warehouse")
public class WarehouseController {

    @Autowired
    ProductRepository productRepository;
    @GetMapping("")
    public String show(Model model) {
        int getProductCount = productRepository.getProductCount();
        int getTotalStock = productRepository.getTotalStock();
        double getTotalStockValue = productRepository.getTotalStockValue();
        List<Map<String, Object>> productOutStock10 = productRepository.productOutStock10();
        List<Map<String, Object>> productOutStock = productRepository.productOutStock();
        List<Map<String, Object>> productSale = productRepository.productSale();
        model.addAttribute("getProductCount", getProductCount);
        model.addAttribute("getTotalStock", getTotalStock);
        model.addAttribute("getTotalStockValue", getTotalStockValue);
        model.addAttribute("productOutStock10", productOutStock10);
        model.addAttribute("productOutStock", productOutStock);
        model.addAttribute("productSale", productSale);
        return "forderAdmin/warehouse";
    }

    
}