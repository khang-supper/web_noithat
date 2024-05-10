package project.spring.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import project.spring.model.Account;
import project.spring.model.Cart;
import project.spring.model.Category;
import project.spring.model.Order;
import project.spring.model.OrderDetail;
import project.spring.repositories.AccountRepository;
import project.spring.repositories.CartRepository;
import project.spring.repositories.CategoryRepository;
import project.spring.repositories.OrderDetailRepository;
import project.spring.repositories.OrderRepository;
import project.spring.repositories.ProductRepository;


@Controller
public class CartController {
    @Autowired
    private HttpServletRequest request;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    ProductRepository productRepository;

    @PostMapping("/add-to-cart")
    public String addToCart(@RequestParam("productId") int productId, @RequestParam("quantity") int quantity, HttpSession session, Model model) {
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
            newCart.setQuantity(quantity);
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
    @GetMapping("/gio-hang")
    public String cart(Model model){
        Integer accountId = (Integer) request.getSession().getAttribute("accountId");
        if(accountId != null){
            List<Category> categories = CategoryRepository.Instance().findAll();
            List<Map<String, Object>> cart = CartRepository.Instance().find(accountId);
            if(cart != null){
                double total = CartRepository.Instance().getTotal(accountId);
                double totalBefore = CartRepository.Instance().getTotal2(accountId);
                model.addAttribute("carts", cart);
                model.addAttribute("totalBefore", totalBefore);
                model.addAttribute("total", total);
            } else{
                model.addAttribute("carts", null);
                model.addAttribute("total", 0.0); 
            }
            model.addAttribute("categories", categories);
            return "forderClient/shoping-cart";
        }else{
            return "redirect:/login";
        }
    }

    //cập nhật giỏ hàng
    @PostMapping("/update-cart")
    @ResponseBody
    public Map<String, Object> updateCart(@RequestBody List<CartItemRequest> cartItems, HttpSession session) {
        Integer accountId = (Integer) session.getAttribute("accountId");
        Map<String, Object> response = new HashMap<>();

        if (accountId != null) {
            // Xóa tất cả các mục trong giỏ hàng cũ của người dùng
            CartRepository.Instance().deleteByAccountId(accountId);

            // Cập nhật giỏ hàng mới từ danh sách cartItems
            double total = 0.0;
            for (CartItemRequest item : cartItems) {
                int productId = item.getProductId();
                int quantity = item.getQuantity();

                Cart newCart = new Cart();
                newCart.setAccountId(accountId);
                newCart.setProductId(productId);
                newCart.setQuantity(quantity);
                CartRepository.Instance().insert(newCart);
            }

            // Tính tổng tiền lại sau khi cập nhật giỏ hàng
            total = CartRepository.Instance().getTotal(accountId);

            // Trả về kết quả là tổng tiền và số lượng sản phẩm mới
            response.put("total", total);
            response.put("cartItemCount", cartItems.size());
        }

        return response;
    }

    @GetMapping("/dat-hang")
    public String order(Model model, HttpSession session){
        Integer accountId = (Integer) request.getSession().getAttribute("accountId");
        if(accountId != null){
            List<Category> categories = CategoryRepository.Instance().findAll();
            List<Map<String, Object>> cart = CartRepository.Instance().find(accountId);
            Account account = accountRepository.findById(accountId);
            double total = CartRepository.Instance().getTotal(accountId);
            model.addAttribute("account", account);
            model.addAttribute("carts", cart);
            model.addAttribute("total", total);
            session.setAttribute("totalOrder", total);
            model.addAttribute("categories", categories);
            return "forderClient/checkout";
        }else{
            return "redirect:/login";
        }
    }

    @PostMapping("/create-order")
    @ResponseBody
    public Map<String, Object> createOrder(@RequestBody OrderRequest orderRequest, HttpSession session) {
        Integer accountId = (Integer) request.getSession().getAttribute("accountId");
        Date date = new Date();
        // Tạo mã đơn hàng không trùng lặp
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String orderCode = "OR" + dateFormat.format(date);
        // Thực hiện tạo đơn hàng
        Order newOrder = new Order();
        newOrder.setCode(orderCode);
        newOrder.setAccountId(accountId);
        newOrder.setCustomerName(orderRequest.getCustomerName());
        newOrder.setShippingAddress(orderRequest.getShippingAddress());
        newOrder.setShippingPhone(orderRequest.getShippingPhone());
        newOrder.setTotal(orderRequest.getTotal());
        newOrder.setStatus("Đơn hàng mới");
        newOrder.setPaymentStatus(false);
        newOrder.setPaymentGateway("COD");
        int orderId = OrderRepository.Instance().insert(newOrder);

        session.setAttribute("orderId", orderId);
        
        // Trả về ID của đơn hàng
        Map<String, Object> response = new HashMap<>();
        response.put("orderId", orderId);
        return response;
    }

    @PostMapping("/create-order-detail")
    @ResponseBody
    public Map<String, Object> createOrderDetail(@RequestBody OrderDetail orderDetail) {
        int productId = orderDetail.getProductId();
        int quantity = orderDetail.getQuantity();
        int currentStock = productRepository.findStock(productId);
        int updatedStock = currentStock - quantity; // Cập nhật số lượng tồn kho
        productRepository.updateStock(productId, updatedStock); // Cập nhật số lượng tồn kho của sản phẩm
        
        // Thực hiện tạo chi tiết đơn hàng
        int result = OrderDetailRepository.Instance().insertDetail(orderDetail);

        // Trả về kết quả
        Map<String, Object> response = new HashMap<>();
        response.put("result", result);
        return response;
    }

    
    @PostMapping("/clear-cart")
    @ResponseBody
    public Map<String, String> clearCart(HttpSession session) {
        Integer accountId = (Integer) session.getAttribute("accountId");
        // Xóa tất cả sản phẩm trong giỏ hàng của người dùng
        CartRepository.Instance().deleteByAccountId(accountId);
        
        // Trả về kết quả
        Map<String, String> response = new HashMap<>();
        response.put("message", "Cart cleared successfully");
        return response;
    }

    @GetMapping("/dat-hang/vnpay_return")
    public String getVnpay(@RequestParam(value = "vnp_BankCode") String bankCode,
                           @RequestParam(value = "vnp_TxnRef") String vnpCode,
                           @RequestParam(value = "vnp_Amount") String amount, Model model, HttpSession session
    ){
        Integer orderId = (Integer) session.getAttribute("orderId");
        OrderRepository.Instance().updatePaymentOrder(orderId, vnpCode);
        Order order = OrderRepository.Instance().findById(orderId);
        List<Map<String, Object>> orderDetail = OrderRepository.Instance().getProuctWithOrderDetail(orderId);
        model.addAttribute("order", order);
        model.addAttribute("orderDetail", orderDetail);
        model.addAttribute("bankCode", bankCode);
        model.addAttribute("vnpCode", vnpCode);
        model.addAttribute("amount", amount);
        return "forderClient/vnpay_return";
    }

    @GetMapping("/dat-hang/order_return")
    public String getOrderReturn(@RequestParam(value = "orderId") int orderId, Model model) {
        Order order = OrderRepository.Instance().findById(orderId);
        List<Map<String, Object>> orderDetail = OrderRepository.Instance().getProuctWithOrderDetail(orderId);
        model.addAttribute("order", order);
        model.addAttribute("orderDetail", orderDetail);
        return "forderClient/order_return";
    }

    @PostMapping("/dat-hang/cancel_order")
    @ResponseBody
    public String cancelOrder(@RequestParam(value = "orderId") int orderId) {
        Order order = OrderRepository.Instance().findById(orderId);
        if (order.getStatus().equals("Đơn hàng mới")) {
            // Lấy danh sách các chi tiết đơn hàng
            List<OrderDetail> orderDetails = OrderDetailRepository.Instance().findByOrderId(orderId);
            
            // Tiến hành cập nhật lại số lượng tồn kho của sản phẩm và xóa các chi tiết đơn hàng
            for (OrderDetail orderDetail : orderDetails) {
                int productId = orderDetail.getProductId();
                int quantity = orderDetail.getQuantity();
                int currentStock = productRepository.findStock(productId);
                int updatedStock = currentStock + quantity; // Cập nhật số lượng tồn kho
                productRepository.updateStock(productId, updatedStock); // Cập nhật số lượng tồn kho của sản phẩm
                OrderDetailRepository.Instance().deleteOrderDetails(orderId); // Xóa các chi tiết đơn hàng
            }
            
            // Tiến hành xóa đơn hàng chính
            OrderRepository.Instance().deleteById(orderId);
            
            return "Đã hủy đơn hàng thành công!";
        } else {
            return "Không thể hủy đơn hàng!";
        }
    }


    @PostMapping("/dat-hang/edit_order")
    @ResponseBody
    public String editOrder(@RequestParam(value = "orderId") int orderId, 
                            @RequestParam(value = "customerName") String customerName, 
                            @RequestParam(value = "shippingPhone") String shippingPhone, 
                            @RequestParam(value = "shippingAddress") String shippingAddress) {
        Order order = OrderRepository.Instance().findById(orderId);
        order.setCustomerName(customerName);
        order.setShippingPhone(shippingPhone);
        order.setShippingAddress(shippingAddress);
        OrderRepository.Instance().updateShipmentDetails(order);
        return "Cập nhật thông tin đơn hàng thành công!";
    }


}
class CartItemRequest {
    private int productId;
    private int quantity;

    public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
}

class OrderRequest{
    private String customerName;
    private String shippingAddress;  // Địa chỉ giao hàng
    private String shippingPhone;  // Số điện thoại
    private double total;  // Tổng tiền

    public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getShippingAddress() {
		return shippingAddress;
	}

	public void setShippingAddress(String shippingAddress) {
		this.shippingAddress = shippingAddress;
	}

	public String getShippingPhone() {
		return shippingPhone;
	}

	public void setShippingPhone(String shippingPhone) {
		this.shippingPhone = shippingPhone;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}
}