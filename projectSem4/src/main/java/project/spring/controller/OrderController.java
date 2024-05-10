package project.spring.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import project.spring.model.Order;
import project.spring.repositories.OrderRepository;

@Controller
@RequestMapping("/admin/order")
public class OrderController {
    @GetMapping("")
    public String getAllOrders(Model model) {
        List<Map<String, Object>> orders = OrderRepository.Instance().getAllOrdersWithProducts();

        model.addAttribute("orders", orders);
        return "forderAdmin/orders";
    }

    // @GetMapping("/add")
    // public String showAddForm(Model model) {
    // model.addAttribute("Order", new Order());
    // return "forderAdmin/add-Order";
    // }

    // @PostMapping("/add")
    // public String addOrder(@ModelAttribute("Order") Order Order) {
    // OrderRepository.Instance().insert(Order);
    // return "redirect:/admin/categories";
    // }

    @GetMapping("/edit")
    public String showEditForm(@RequestParam("id") int id, Model model) {
        Order order = OrderRepository.Instance().findById(id);
        List<Map<String, Object>> orderDetail = OrderRepository.Instance().getProuctWithOrderDetail(id);
        List<String> orderStatusList = Arrays.asList("Đơn hàng mới", "Chờ xác nhận", "Đang vận chuyển", "Hoàn tất");

        model.addAttribute("order", order);
        model.addAttribute("orderDetail", orderDetail);
        model.addAttribute("orderStatusList", orderStatusList);

        return "forderAdmin/order-detail";
    }

    @PostMapping("/edit")
    public String updateOrder(@RequestParam("id") int id, @ModelAttribute("Order") Order order) {
        order.setId(id);
        OrderRepository.Instance().updateStatus(order);
        return "redirect:/admin/order";
    }

    @GetMapping("/delete")
    public String deleteOrder(@RequestParam("id") int id) {
        OrderRepository.Instance().deleteById(id);
        return "redirect:/admin/order";
    }

    // trang thống kê
    @GetMapping("/report")
    public String orderReport(Model model) {
        // List<Integer> weeklyOrders = calculateWeeklyOrders(); // Tính toán số đơn hàng hàng ngày trong tuần
        // int totalWeeklyOrders = weeklyOrders.stream().reduce(0, (a, b) -> a + b); // Tổng số đơn hàng trong tuần
        // model.addAttribute("weeklyOrders", weeklyOrders);
        // model.addAttribute("totalWeeklyOrders", totalWeeklyOrders);
        List<Order> orders = OrderRepository.Instance().findAll();
        model.addAttribute("order", orders);

        return "forderAdmin/order-report";
    }

    // Hàm tính toán số đơn hàng hàng ngày trong tuần
    private List<Integer> calculateWeeklyOrders() {
        List<Integer> weeklyOrders = new ArrayList<>();
        LocalDate today = LocalDate.now();
        for (int i = 0; i < 7; i++) {
            LocalDate date = today.minusDays(i);
            int orders = OrderRepository.Instance().getDailyOrderCount(date); // Số đơn hàng của mỗi ngày
            weeklyOrders.add(orders);
        }
        Collections.reverse(weeklyOrders); // Đảo ngược danh sách để đưa về thứ tự từ trái sang phải
        return weeklyOrders;
    }

    // trang thống kê
    @GetMapping("/report-test")
    public String orderReportTest(Model model) {
        List<Order> orders = OrderRepository.Instance().findAll();
        model.addAttribute("order", orders);

        return "Admin/test-report";
    }
}
