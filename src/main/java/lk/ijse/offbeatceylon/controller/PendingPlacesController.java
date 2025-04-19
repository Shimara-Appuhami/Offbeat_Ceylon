package lk.ijse.offbeatceylon.controller;

import jakarta.validation.Valid;
import lk.ijse.offbeatceylon.dto.ResponseDTO;
import lk.ijse.offbeatceylon.entity.AddPlaces;
import lk.ijse.offbeatceylon.entity.User;
import lk.ijse.offbeatceylon.service.AddPlaceService;
import lk.ijse.offbeatceylon.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
@RestController
@RequestMapping("api/v1/pendingPlaces")
@CrossOrigin("http://localhost:63342")
public class PendingPlacesController {
    private final ResponseDTO responseDTO;
    private final AddPlaceService addPlaceService;
    private final UserService userService;

    public PendingPlacesController(ResponseDTO responseDTO, AddPlaceService addPlaceService, UserService userService) {
        this.responseDTO = responseDTO;
        this.addPlaceService = addPlaceService;
        this.userService = userService;
    }

    @PostMapping("/save")
    public ResponseEntity<ResponseDTO> savePlace(@Valid
                                                 @RequestParam("email") String email,
                                                 @RequestParam("placeName") String placeName,
                                                 @RequestParam("aboutPlace") String aboutPlace,
                                                 @RequestParam("district") String district,
                                                 @RequestParam("status") String status,
                                                 @RequestParam("latitude") double latitude,
                                                 @RequestParam("longitude") double longitude,
                                                 @RequestParam("category")String category,
                                                 @RequestParam("videoUrl")String videoUrl,
                                                 @RequestParam(value = "images", required = false) MultipartFile placeImages ) throws IOException {

        User user=userService.getUserByEmail(email);
        if (user == null) {
            responseDTO.setMessage("User not found with this email.");
            responseDTO.setData(HttpStatus.NOT_FOUND);
            return new ResponseEntity<>(responseDTO, HttpStatus.NOT_FOUND);
        }
        AddPlaces place = new AddPlaces();
        place.setEmail(user);
        place.setPlaceName(placeName);
        place.setAboutPlace(aboutPlace);
        place.setDistrict(district);
        place.setStatus(status);
        place.setLatitude(latitude);
        place.setLongitude(longitude);
        place.setCategory(category);
        place.setVideoUrl(videoUrl);
        boolean isAdded=addPlaceService.savePlace(place,placeImages);
        if (isAdded) {
            responseDTO.setMessage("Place listed for place successfully!");
            responseDTO.setData(HttpStatus.CREATED);
            return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
        } else {
            responseDTO.setMessage("Failed to list place for save.");
            responseDTO.setData(HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
        }
    }

    //get All
    @GetMapping("/getAll")
    public ResponseEntity<List<AddPlaces>> getAllPlaces() {
        List<AddPlaces> places = addPlaceService.getAllPlaces();
        if (!places.isEmpty()) {
            return ResponseEntity.ok(places);
        } else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }
    }
    //get All by category
    @GetMapping("/getAllByCategory/{category}")
    public ResponseEntity<List<AddPlaces>> getPlacesByCategory(@PathVariable String category) {
        List<AddPlaces> places = addPlaceService.getPlacesByCategory(category);
        if (!places.isEmpty()) {
            return ResponseEntity.ok(places);
        } else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }
    }
    //get All by name
    @GetMapping("/getAllByName/{placeName}")
    public ResponseEntity<AddPlaces> getPlaceByName(@PathVariable String placeName) {
        AddPlaces place = addPlaceService.getPlaceByName(placeName);
        if (place != null) {
            return ResponseEntity.ok(place);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
    //get All by district
    @GetMapping("/getAllByDistrict/{district}")
    public ResponseEntity<List<AddPlaces>> getPlacesByDistrict(@PathVariable String district) {
        List<AddPlaces> places = addPlaceService.getPlacesByDistrict(district);
        if (!places.isEmpty()) {
            return ResponseEntity.ok(places);
        } else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }
    }
    @PutMapping("/update/{placeId}")
    public ResponseEntity<String> updatePlace(@Valid
//            @RequestParam("email") String email,
                                              @PathVariable int placeId,
                                              @RequestParam String placeName,
                                              @RequestParam String category,
                                              @RequestParam String aboutPlace,
                                              @RequestParam String district,
                                              @RequestParam String status,
                                              @RequestParam double latitude,
                                              @RequestParam double longitude,
                                              @RequestParam String videoUrl,
                                              @RequestParam(value = "image", required = false) MultipartFile image) {

        try {
            AddPlaces updatedPlace = addPlaceService.updatePlace(
                    placeId, placeName, category, aboutPlace, district, status, latitude, longitude, image,videoUrl);

            if (updatedPlace != null) {
                return ResponseEntity.ok("Place updated successfully.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Place not found.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update place: " + e.getMessage());
        }
    }


    //delete
    @DeleteMapping("/delete/{placeId}")
    public ResponseEntity<String> deletePlace(@PathVariable("placeId") int placeId) {
        boolean isDeleted = addPlaceService.deletePlaceById(placeId);

        if (isDeleted) {
            return ResponseEntity.status(HttpStatus.OK).body("Place deleted successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Place not found.");
        }
    }
}
