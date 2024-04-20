package project.spring.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

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
}
