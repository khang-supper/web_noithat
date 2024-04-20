package project.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import project.spring.model.Account;
import project.spring.model.Cart;
import project.spring.repositories.AccountRepository;
import project.spring.repositories.CartRepository;


@Controller
public class CartController {

    @Autowired
    AccountRepository accountRepository;

    @PostMapping("/add-to-cart/{productId}")
    public String addToCart(@PathVariable int productId, HttpSession session, Model model) {
        Integer accountId = (Integer) session.getAttribute("accountId");
        if (accountId == null) {
            model.addAttribute("error", "Bạn cần đăng nhập để thêm sản phẩm vào giỏ hàng.");
            return "redirect:/login"; // Chuyển hướng đến trang đăng nhập
        }
        // Lấy số lượng sản phẩm trong giỏ hàng của người dùng
        Cart existingCart = CartRepository.Instance().findCartByProductIdAndAccountId(productId, accountId);
        if (existingCart != null) {
            // Nếu sản phẩm đã có trong giỏ hàng, tăng số lượng lên 1
            existingCart.setQuantity(existingCart.getQuantity() + 1);
            CartRepository.Instance().update(existingCart);
        } else {
            // Nếu sản phẩm chưa có trong giỏ hàng, thêm mới vào giỏ hàng với số lượng là 1
            Cart newCart = new Cart();
            newCart.setAccountId(accountId);
            newCart.setProductId(productId);
            newCart.setQuantity(1);
            CartRepository.Instance().insert(newCart);
        }
        return "redirect:/"; // Chuyển hướng đến trang chính sau khi thêm sản phẩm vào giỏ hàng
    }
    @GetMapping("/cart-item-count")
    @ResponseBody
    public int getCartItemCount(HttpSession session) {
        Integer accountId = (Integer) session.getAttribute("accountId");
        if (accountId != null) {
            return CartRepository.Instance().getCartItemCount(accountId);
        }
        return 0;
    }
}

