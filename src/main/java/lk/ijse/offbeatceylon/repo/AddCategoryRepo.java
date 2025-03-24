package lk.ijse.offbeatceylon.repo;

import lk.ijse.offbeatceylon.entity.AddPlaces;
import lk.ijse.offbeatceylon.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddCategoryRepo extends JpaRepository<Category, Integer> {
    boolean existsByCategoryName(String categoryName);
    Category findByCategoryName(String categoryName);
    Category findByCategoryId(int categoryId);

}

