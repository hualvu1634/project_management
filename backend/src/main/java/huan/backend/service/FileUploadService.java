package huan.backend.service;

import org.springframework.stereotype.Service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@Service
public class FileUploadService {

    private final String upload = "uploads/";

    public String uploadFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File rỗng, không thể tải lên!");
        }


        Path uploadPath = Paths.get(upload);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

            Path filePath = Paths.get(upload + file.getOriginalFilename());
        Files.write(filePath, file.getBytes());
        return file.getOriginalFilename();
    }
}