package lk.ijse.offbeatceylon.service.impl;

import lk.ijse.offbeatceylon.dto.AddPlaceDTO;
import lk.ijse.offbeatceylon.entity.AddPlaces;
import lk.ijse.offbeatceylon.repo.AddPlaceRepo;
import lk.ijse.offbeatceylon.service.AddPlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class AddPlaceServiceImpl implements AddPlaceService {

    @Autowired
    private AddPlaceRepo addPlaceRepo;

    @Override
    public AddPlaces savePlace(AddPlaces place) {
        return addPlaceRepo.save(place);
    }

    @Override
    public boolean existsByPlaceName(String placeName) {
        return addPlaceRepo.existsByPlaceName(placeName);
    }
}
