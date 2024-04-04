package project.spring.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import project.spring.model.News;
import project.spring.repositories.NewsRepository;
import project.spring.repositories.OrderRepository;

@Controller
@RequestMapping("/admin/news")
public class NewsController {
    @GetMapping("")
    public String getAllNews(Model model) {
    List<News> news = NewsRepository.Instance().findAll();
    model.addAttribute("news", news);
    return "forderAdmin/news";
    }

    // @GetMapping("")
    // public String getAllnews(@RequestParam(defaultValue = "1") int page, Model model) {
    //     int pageSize = 1; // Số lượng mục trên mỗi trang
    //     List<News> news = NewsRepository.Instance().findAllByPage(page - 1, pageSize);
    //     int totalPages = NewsRepository.Instance().getTotalPages(pageSize);
    //     model.addAttribute("news", news);
    //     model.addAttribute("totalPages", totalPages);
    //     model.addAttribute("currentPage", page); // Trang hiện tại
    //     return "Admin/news";
    // }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("news", new News());
        return "forderAdmin/add-news";
    }

    @PostMapping("/add")
    public String addNews(@ModelAttribute("news") News news) {
        news.setAccountId(1);
        NewsRepository.Instance().insert(news);
        return "redirect:/admin/news";
    }

    @GetMapping("/edit")
    public String showEditForm(@RequestParam("id") int id, Model model) {
        News news = NewsRepository.Instance().findById(id);
        model.addAttribute("news", news);
        return "forderAdmin/edit-news";
    }

    @PostMapping("/edit")
    public String updateNews(@RequestParam("id") int id, @ModelAttribute("news") News news) {
        news.setId(id);
        NewsRepository.Instance().update(news);
        return "redirect:/admin/news";
    }

    @GetMapping("/delete/{id}")
    public String deleteNews(@PathVariable("id") int id) {
        NewsRepository.Instance().deleteById(id);
        return "redirect:/admin/news";
    }
    // @PostMapping("/delete")
    // @ResponseBody
    // public ResponseEntity<String> deleteOrder(@RequestParam("id") int id) {
    //     int rowsAffected = NewsRepository.Instance().deleteById(id);
    //     if (rowsAffected > 0) {
    //         return new ResponseEntity<>("Đã xóa bài viết thành công", HttpStatus.OK);
    //     } else {
    //         return new ResponseEntity<>("Không thể xóa bài viết", HttpStatus.INTERNAL_SERVER_ERROR);
    //     }
    // }

}
