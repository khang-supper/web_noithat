package project.spring.controller;




import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


@Controller
public class Uploadfile {
     @Autowired
    private JdbcTemplate jdbcTemplate; 

    @GetMapping("/upload")
    public String uploadForm() {
        return "forderAdmin/UploadImages"; 
    }
    @Value("${upload.directory}") 
    private String uploadDir;

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return "uploadFailure";
        }
        try {
            byte[] bytes = file.getBytes();
            Path path = Paths.get(uploadDir + file.getOriginalFilename());
            Files.createDirectories(path.getParent());
            Files.write(path, bytes);
            String imagePath = path.toString();
            String sql = "INSERT INTO images (path) VALUES (?)";
            jdbcTemplate.update(sql, imagePath);
            return "Client/index";
        } catch (IOException e) {
            e.printStackTrace();
            return "uploadFailure";
        }
    }
}
