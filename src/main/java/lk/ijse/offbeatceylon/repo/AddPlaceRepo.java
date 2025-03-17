package lk.ijse.offbeatceylon.repo;

import lk.ijse.offbeatceylon.entity.AddPlaces;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddPlaceRepo extends JpaRepository<AddPlaces,Long> {
    boolean existsByPlaceName(String placeName);
}
