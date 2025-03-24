package lk.ijse.offbeatceylon.service;

import lk.ijse.offbeatceylon.entity.Category;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface  AddCategoryService {

    boolean saveCategory(Category category, MultipartFile image);

    List<Category> getAllCategories();

    Category getCategoryById(int categoryId);

//    Category createCategory(Category category);

//    Category updateCategory(int categoryId, Category category);

    boolean existsByCategoryName(String categoryName);

    boolean deleteCategory(int categoryId);

    Category getCategoryByName(String placeName);
}
