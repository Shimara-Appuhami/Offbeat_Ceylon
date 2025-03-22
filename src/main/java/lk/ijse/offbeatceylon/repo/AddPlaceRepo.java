package lk.ijse.offbeatceylon.repo;

import lk.ijse.offbeatceylon.entity.AddPlaces;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AddPlaceRepo extends JpaRepository<AddPlaces,Integer> {
    boolean existsByPlaceName(String placeName);
    AddPlaces findByPlaceName(String placeName);
    AddPlaces findByPlaceId(int placeId);

}
