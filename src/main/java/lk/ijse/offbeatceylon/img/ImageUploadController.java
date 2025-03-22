package lk.ijse.offbeatceylon.img;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

@RequestMapping("api/v1/img")
@RestController
public class ImageUploadController {


    @PostMapping("/upload")
    public ResponseEntity<String> saveImage(@RequestParam("files") MultipartFile[] files) {

        String uploadDir = "imageFolder";

        try {
            Arrays.asList(files).forEach(file -> {
                String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
                System.out.println("Uploading file: " + fileName);
                try {
                    FileUploadUtil.saveFile(uploadDir, fileName, file);
                } catch (IOException e) {
                    throw new RuntimeException("Failed to save file: " + fileName, e);
                }
            });
            return ResponseEntity.ok("Images uploaded successfully!");
        } catch (RuntimeException e) {
            System.err.println("Error while uploading: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload images!");
        }
    }



}