package lk.ijse.offbeatceylon.service;

import lk.ijse.offbeatceylon.entity.AddPlaces;
import lk.ijse.offbeatceylon.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AddPlaceService {
    boolean savePlace(AddPlaces place,MultipartFile image);
    boolean existsByPlaceName(String placeName);

    AddPlaces updatePlace(AddPlaces place);
//    List<AddPlaces> getAllPlaces();

    AddPlaces getPlaceByName(String placeName);
    public AddPlaces getPlaceById(int placeId);

    public boolean deletePlaceById(int placeId);
//    ResponseEntity<String> updatePlace(int placeId, String placeName, String category, String aboutPlace, String district, String status, double latitude, double longitude, MultipartFile image);

    AddPlaces updatePlace( int placeId, String placeName, String category, String aboutPlace, String district, String status, double latitude, double longitude, MultipartFile image,String videoUrl);

    List<AddPlaces> getAllPlaces();

    List<AddPlaces> getPlacesByCategory(String category);

    List<AddPlaces> getPlacesByDistrict(String district);

    User getUserByEmail(String email);

    int getPlacesCount();

}
