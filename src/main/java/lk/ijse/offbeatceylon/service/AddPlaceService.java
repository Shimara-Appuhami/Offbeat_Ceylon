package lk.ijse.offbeatceylon.service;

import lk.ijse.offbeatceylon.dto.AddPlaceDTO;
import lk.ijse.offbeatceylon.entity.AddPlaces;

public interface AddPlaceService {
    AddPlaces savePlace(AddPlaces place);
    boolean existsByPlaceName(String placeName);}
