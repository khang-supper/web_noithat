package project.spring.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.UUID;

import project.spring.model.Image;
import project.spring.repositories.FilesStorageService;

@Controller
public class ImageController {
    @Autowired
    private FilesStorageService filesStorageService;

    @GetMapping("/upload")
    public String home() {
        return "/forderAdmin/images";
    }

    @GetMapping("/images/new")
    public String newImage(Model model) {
        return "/forderAdmin/upload_form";
    }

    @PostMapping("/images/upload")
    public String uploadImage(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        String message = "";
        try {
            // Kiểm tra xem tệp đã được chọn hay chưa
            if (file.isEmpty()) {
                message = "Please select a file to upload.";
                redirectAttributes.addFlashAttribute("message", message);
                return "redirect:/images/new";
            }
            
            // Tạo tên mới cho tệp
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String filename = UUID.randomUUID().toString() + extension;
            
            // Lưu tệp vào thư mục uploads với tên mới
            filesStorageService.save(file, filename);
            
            // Tạo đường dẫn URL cho ảnh
            String url = MvcUriComponentsBuilder
                            .fromMethodName(ImageController.class, "getImage", filename).build().toString();
            
            // Lưu thông tin ảnh vào cơ sở dữ liệu
            Image image = new Image();
            image.setPath(filename);
            filesStorageService.saveImageInfo(image, filename);
    
            // Thêm thông báo thành công
            message = "Uploaded the image successfully: " + originalFilename;
            redirectAttributes.addFlashAttribute("message", message);
            redirectAttributes.addFlashAttribute("url", url);
        } catch (Exception e) {
            // Xử lý nếu có lỗi xảy ra trong quá trình tải lên
            message = "Could not upload the image: " + file.getOriginalFilename() + ". Error: " + e.getMessage();
            redirectAttributes.addFlashAttribute("message", message);
        }
        return "redirect:/upload";
    }

    @GetMapping("/images")
    public String getListImages(Model model) {
        List<Image> imageInfos = filesStorageService.loadAll().map(path -> {
            String filename = path.getFileName().toString();
            String url = MvcUriComponentsBuilder.fromMethodName(ImageController.class, "getImage", filename).build()
                    .toString();
            Image image = new Image();
            image.setId(0);
            image.setPath(url);
            return image;
        }).collect(Collectors.toList());

        model.addAttribute("images", imageInfos);

        return "/forderAdmin/images"; 
    }


    
    

    @GetMapping("/images/{filename:.+}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) {
        Resource file = filesStorageService.load(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }

    @GetMapping("/images/delete/{filename:.+}")
    public String deleteImage(@PathVariable String filename, RedirectAttributes redirectAttributes) {
        try {
            boolean existed = filesStorageService.delete(filename);

            if (existed) {
                redirectAttributes.addFlashAttribute("message", "Delete the image successfully: " + filename);
            } else {
                redirectAttributes.addFlashAttribute("message", "The image does not exist!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message",
                    "Could not delete the image: " + filename + ". Error: " + e.getMessage());
        }

        return "redirect:/images";
    }
}
