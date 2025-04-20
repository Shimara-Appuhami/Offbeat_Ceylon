package lk.ijse.offbeatceylon.service.impl;

import lk.ijse.offbeatceylon.dto.CategoryDTO;
import lk.ijse.offbeatceylon.entity.Category;
import lk.ijse.offbeatceylon.repo.AddCategoryRepo;
import lk.ijse.offbeatceylon.service.AddCategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class AddCategoryServiceImpl implements AddCategoryService {

    @Autowired
    private AddCategoryRepo categoryRepo;

//    @Autowired
//    private CategoryDTO categoryDTO;

    @Autowired
    private ModelMapper modelMapper;

    @Override
   public boolean saveCategory(Category category, MultipartFile image) {
        try {
        if (image != null && !image.isEmpty()) {
            String imagePath = saveImage(image);
            if (imagePath != null) {
                category.setCategoryImage(imagePath);
            } else {
                return false;
            }
        }

        categoryRepo.save(category);
        return true;
    } catch (Exception e) {
        System.err.println("Error while adding category: " + e.getMessage());
        return false;
    }
}
    public String saveImage(MultipartFile image) throws IOException {
        try {
            String fileName = UUID.randomUUID().toString();

            String fileExtension = getFileExtension(image);
            if (fileExtension == null) {
                return null;
            }

            String projectRootPath = System.getProperty("user.dir");

            Path path = Paths.get(projectRootPath, "uploads", "images", fileName + fileExtension);

            Files.createDirectories(path.getParent());

            image.transferTo(path.toFile());

            return path.toString();
        } catch (IOException e) {
            System.err.println("Error saving image: " + e.getMessage());
        }
        return null;
    }

    private String getFileExtension(MultipartFile image) {
        String contentType = image.getContentType();
        if (contentType != null) {
            switch (contentType) {
                case "image/jpeg":
                    return ".jpg";
                case "image/png":
                    return ".png";
                case "image/gif":
                    return ".gif";
                case "image/webp":
                    return ".webp";
                default:
                    return null;
            }
        }
        return null;
}


    @Override
    public List<Category> getAllCategories() {
        return categoryRepo.findAll();
    }

    @Override
    public Category getCategoryById(int categoryId) {
        return categoryRepo.findById(categoryId).orElse(null);

    }
    @Override
    public boolean existsByCategoryName(String categoryName) {
        return categoryRepo.existsByCategoryName(categoryName);
    }

    @Override
    public boolean deleteCategory(int categoryId) {
        if (categoryRepo.existsById(categoryId)) {
            categoryRepo.deleteById(categoryId);
            return true;
        }
        return false;
    }
    @Override
    public Category getCategoryByName(String categoryName) {
        return categoryRepo.findByCategoryName(categoryName);
    }

    @Override
    public Long getTotalCategories() {
        return categoryRepo.count();
    }

}
