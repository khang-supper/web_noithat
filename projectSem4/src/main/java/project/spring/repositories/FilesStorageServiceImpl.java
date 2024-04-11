package project.spring.repositories;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import project.spring.model.Image;

@Service
public class FilesStorageServiceImpl implements FilesStorageService {

  private final Path root = Paths.get("./uploads");

  @Autowired
  private JdbcTemplate jdbcTemplate;

  public void init() {
    try {
      if (!Files.exists(root)) {
        Files.createDirectories(root);
        System.out.println("Thư mục 'uploads' đã được tạo ra thành công.");
      } else {
        System.out.println("Thư mục 'uploads' đã tồn tại.");
      }
    } catch (IOException e) {
      throw new RuntimeException("Could not initialize folder for upload!", e);
    }
  }

  @Override
  @Transactional
  public void save(MultipartFile file, String filename) {
      try {
          Path filePath = this.root.resolve(filename);
          Files.copy(file.getInputStream(), filePath);
      } catch (IOException e) {
          throw new RuntimeException("Failed to store file.", e);
      }
  }

  @Override
  public Resource load(String filename) {
    try {
      Path file = root.resolve(filename);
      Resource resource = new UrlResource(file.toUri());

      if (resource.exists() || resource.isReadable()) {
        return resource;
      } else {
        throw new RuntimeException("Could not read the file!");
      }
    } catch (IOException e) {
      throw new RuntimeException("Error: " + e.getMessage());
    }
  }

  @Override
  public boolean delete(String filename) {
    try {
      Path file = root.resolve(filename);
      return Files.deleteIfExists(file);
    } catch (IOException e) {
      throw new RuntimeException("Error: " + e.getMessage());
    }
  }

  @Override
  public void deleteAll() {
    FileSystemUtils.deleteRecursively(root.toFile());
  }

  @Override
  public Stream<Path> loadAll() {
    try {
      return Files.walk(this.root, 1).filter(path -> !path.equals(this.root)).map(this.root::relativize);
    } catch (IOException e) {
      throw new RuntimeException("Could not load the files!");
    }
  }

  @Override
  public void saveImageInfo(Image image, String filename) {
    String sql = "INSERT INTO images (path) VALUES (?)";
    jdbcTemplate.update(sql, filename);
  }

  @Override
  public List<Image> getAllImageInfos() {
    String sql = "SELECT * FROM images";
    return jdbcTemplate.query(sql, (rs, rowNum) -> {
      Image image = new Image();
      image.setId(rs.getInt("id"));
      image.setPath(rs.getString("path"));
      return image;
    });
  }
}
