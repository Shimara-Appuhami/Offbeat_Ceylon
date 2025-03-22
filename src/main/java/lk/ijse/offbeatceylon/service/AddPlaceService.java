package lk.ijse.offbeatceylon.service;

import lk.ijse.offbeatceylon.entity.AddPlaces;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface AddPlaceService {
    AddPlaces savePlace(AddPlaces place);
    boolean existsByPlaceName(String placeName);

    AddPlaces updatePlace(AddPlaces place);
//    List<AddPlaces> getAllPlaces();

    AddPlaces getPlaceByName(String placeName);
    public AddPlaces getPlaceById(int placeId);

    public boolean deletePlaceById(int placeId);
//    ResponseEntity<String> updatePlace(int placeId, String placeName, String category, String aboutPlace, String district, String status, double latitude, double longitude, MultipartFile image);

    AddPlaces updatePlace(int placeId, String placeName, String category, String aboutPlace, String district, String status, double latitude, double longitude, MultipartFile image);
}
