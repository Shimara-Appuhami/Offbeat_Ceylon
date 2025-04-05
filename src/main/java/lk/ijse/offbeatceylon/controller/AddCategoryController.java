package lk.ijse.offbeatceylon.controller;

import lk.ijse.offbeatceylon.dto.ResponseDTO;
import lk.ijse.offbeatceylon.entity.Category;
import lk.ijse.offbeatceylon.service.AddCategoryService;
import lk.ijse.offbeatceylon.service.AddPlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/category")
@CrossOrigin("http://localhost:63342")
public class AddCategoryController {
    //save the category

        private final AddCategoryService categoryService;
        private final ResponseDTO responseDTO;

    public AddCategoryController(AddCategoryService categoryService, ResponseDTO responseDTO) {
        this.categoryService = categoryService;
        this.responseDTO = responseDTO;
    }

        @PostMapping("/save")
        public ResponseEntity<ResponseDTO> saveCategory(
                @RequestPart("categoryName") String categoryName,
                @RequestPart("description") String description,
                @RequestPart(value = "categoryImage", required = false) MultipartFile categoryImage) throws IOException {

//            if (categoryName.isEmpty() || description.isEmpty()) {
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Category name and description are required.");
//            }
//
//            if (categoryService.existsByCategoryName(categoryName)) {
//                return ResponseEntity.status(HttpStatus.CONFLICT).body("Category with this name already exists.");
//            }



            Category category = new Category();
            category.setCategoryName(categoryName);
            category.setDescription(description);

            boolean isAdded=categoryService.saveCategory(category,categoryImage);
            if (isAdded) {
                responseDTO.setMessage("Item listed for sale successfully!");
                responseDTO.setData(HttpStatus.CREATED);
                return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
            } else {
                responseDTO.setMessage("Failed to list item for sale.");
                responseDTO.setData(HttpStatus.BAD_REQUEST);
                return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
}        }



//    private String saveImage(String fileName, MultipartFile image) throws IOException {
//        String directoryPath = "C:\\projects\\2nd semester\\Internet Technologies module\\Offbeat-Ceylon - 2\\src\\uploads\\images";
//
//        File directory = new File(directoryPath);
//        if (!directory.exists()) {
//            directory.mkdirs();
//        }
//
//        File file = new File(directory, fileName);
//
//        image.transferTo(file);
//
//        String relativePath = "/resources/static/imageFolder/" + fileName;
//        return relativePath;
//
//    }
//    @GetMapping("/resources/static/imageFolder")
//    @ResponseBody
//    public ResponseEntity<Resource> getImage(@PathVariable String filename) {
//        File file = new File("resources/static/imageFolder/" + filename);
//        if (file.exists()) {
//            return ResponseEntity.ok()
//                    .contentType(MediaType.ALL)// or the appropriate image type
//                    .body(new FileSystemResource(file));
//        }
//        return ResponseEntity.notFound().build();
//    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/getById/{categoryId}")
    public ResponseEntity<Category> getCategoryById(@PathVariable int categoryId) {
        Category category = categoryService.getCategoryById(categoryId);
        if (category != null) {
            return ResponseEntity.ok(category);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

//    @PutMapping("/update/{categoryId}")
//    public ResponseEntity<String> updateCategory(
//            @PathVariable int categoryId,
//            @RequestParam("categoryName") String categoryName,
//            @RequestParam("description") String description,
//            @RequestParam(value = "categoryImage", required = false) MultipartFile categoryImage) throws IOException {
//
//        Category category = categoryService.getCategoryById(categoryId);
//        if (category == null) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Category not found.");
//        }
//
//        category.setCategoryName(categoryName);
//        category.setDescription(description);
//        if (categoryImage != null) {
//            String fileName = StringUtils.cleanPath(categoryImage.getOriginalFilename());
//            String imagePath = saveImage(fileName, categoryImage);
//            category.setCategoryImage(imagePath);
//        }
//
//        categoryService.saveCategory(category,categoryImage);
//        return ResponseEntity.ok("Category updated successfully.");
//    }

    @DeleteMapping("/delete/{categoryId}")
    public ResponseEntity<String> deleteCategory(@PathVariable int categoryId) {
        Category category = categoryService.getCategoryById(categoryId);
        if (category != null) {
            categoryService.deleteCategory(categoryId);
            return ResponseEntity.status(HttpStatus.OK).body("Category deleted successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Category not found.");
        }
    }
    //get total categories
    @GetMapping("/getTotalCategories")
    public ResponseEntity<Long> getTotalCategories() {
        Long totalCategories = categoryService.getTotalCategories();
        return ResponseEntity.ok(totalCategories);
    }
}