package project.spring.repositories;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import project.spring.model.Image;

public interface FilesStorageService {
    public void init();

    public void save(MultipartFile file, String filename);
  
    public Resource load(String filename);
  
    public boolean delete(String filename);
    
    public void deleteAll();
  
    public Stream<Path> loadAll();

    int saveImageInfo(Image image,String filename); // Add this method

    List<Image> getAllImageInfos(); 
    
}
