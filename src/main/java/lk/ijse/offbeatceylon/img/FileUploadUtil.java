package lk.ijse.offbeatceylon.img;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;


public class FileUploadUtil {
    public  static  void saveFile(String uploadDir, String fileName, MultipartFile multipartFile) throws IOException {

        Path uploadPath = Paths.get("src/main/resources/static/" + uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Saved file: " + filePath);
        } catch (IOException ex) {
            throw new IOException("Could not save file: " + fileName, ex);
        }
    }

}