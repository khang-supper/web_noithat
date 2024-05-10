package project.spring.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.servlet.http.HttpSession;
import project.spring.model.Comment;
import project.spring.repositories.CommentRepository;

@Controller
//@RequestMapping("/admin/comments")
public class CommentControler {
	@GetMapping("/admin/comment")
    public String getAllContacts(Model model) {
	    List<Comment> comments = CommentRepository.Instance().findAll();
	    model.addAttribute("comments", comments);
	    return "forderAdmin/comment";
    }
	@GetMapping("/comment")
    public String showAddForm(Model model) {
        model.addAttribute("comments", new Comment());
        return "forderClient/comment";
    }

    @PostMapping("/comment")
    public String addComment(@ModelAttribute("comment") Comment comment) {
    	CommentRepository.Instance().insert(comment);
        return "redirect:/comment";
    }
    //hàm đánh giá sản phẩm
    @PostMapping("/api/comments/add")
    public ResponseEntity<String> addReview(@RequestBody Comment comment, HttpSession session) {
        int accountId = (int) session.getAttribute("accountId");
        
        // Kiểm tra xem người dùng đã đánh giá sản phẩm này chưa
        boolean hasReviewed = CommentRepository.Instance().hasReviewedProduct(accountId, comment.getProductId());
        if (hasReviewed) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Bạn đã đánh giá sản phẩm này.");
        }
    
        // Kiểm tra xem người dùng đã mua sản phẩm này chưa
        boolean hasPurchased = CommentRepository.Instance().hasPurchasedProduct(accountId, comment.getProductId());
        if (hasPurchased) {
            int rowsAffected = CommentRepository.Instance().insert(comment);
            if (rowsAffected > 0) {
                return ResponseEntity.ok().body("Đánh giá thành công.");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Thêm đánh giá thất bại.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Bạn cần mua sản phẩm mới được đánh giá.");
        }
}


    
    

}
