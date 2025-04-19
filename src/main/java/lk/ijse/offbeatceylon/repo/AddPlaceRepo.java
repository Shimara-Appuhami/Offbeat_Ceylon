package lk.ijse.offbeatceylon.repo;

import lk.ijse.offbeatceylon.entity.AddPlaces;
import lk.ijse.offbeatceylon.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AddPlaceRepo extends JpaRepository<AddPlaces,Integer> {
    boolean existsByPlaceName(String placeName);
    AddPlaces findByPlaceName(String placeName);
    AddPlaces findByPlaceId(int placeId);

    List<AddPlaces> findAllByCategory(String category);

    List<AddPlaces> findAllByDistrict(String district);



//    User findByEmail(String email);
}
