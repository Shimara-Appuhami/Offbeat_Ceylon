package lk.ijse.offbeatceylon.service.impl;

import lk.ijse.offbeatceylon.dto.AddPlaceDTO;
import lk.ijse.offbeatceylon.entity.AddPlaces;
import lk.ijse.offbeatceylon.repo.AddPlaceRepo;
import lk.ijse.offbeatceylon.service.AddPlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class AddPlaceServiceImpl implements AddPlaceService {
    @Autowired
    private AddPlaceRepo addPlaceRepo;

    @Override
    public void addPlace(AddPlaceDTO addPlaceDTO) {
        // Convert DTO to Entity
        AddPlaces place = new AddPlaces();
        place.setPlaceName(addPlaceDTO.getPlaceName());
        place.setAboutPlace(addPlaceDTO.getAboutPlace());
        place.setDistrict(addPlaceDTO.getDistrict());
        place.setImages(addPlaceDTO.getImages());
        place.setStatus(addPlaceDTO.getStatus());
        place.setLocation(addPlaceDTO.getLocation());

        // Save to database
        addPlaceRepo.save(place);

        System.out.println("Place added successfully: " + addPlaceDTO);
    }
}
