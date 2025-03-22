package lk.ijse.offbeatceylon.controller;

import lk.ijse.offbeatceylon.entity.AddPlaces;
import lk.ijse.offbeatceylon.repo.AddPlaceRepo;
import lk.ijse.offbeatceylon.service.AddPlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("api/v1/addPlace")
@CrossOrigin("http://localhost:63342")
public class AddPlaceController {

    @Autowired
    private AddPlaceRepo addPlacesRepo;

    @Autowired
    private AddPlaceService addPlaceService;

    @PostMapping("/save")
    public ResponseEntity<String> savePlace(
            @RequestParam("placeName") String placeName,
            @RequestParam("aboutPlace") String aboutPlace,
            @RequestParam("district") String district,
            @RequestParam("status") String status,
            @RequestParam("latitude") double latitude,
            @RequestParam("longitude") double longitude,
            @RequestParam("category")String category,
            @RequestParam(value = "images", required = false) List<MultipartFile> images) throws IOException {

        if (placeName.isEmpty() || aboutPlace.isEmpty() || district.isEmpty() || status.isEmpty()||category.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("All fields are required.");
        }

        if (addPlaceService.existsByPlaceName(placeName)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Place with this name already exists.");
        }

        List<String> imagePaths = new ArrayList<>();
        if (images != null) {
            for (MultipartFile image : images) {
                String fileName = StringUtils.cleanPath(image.getOriginalFilename());
                String imagePath = saveImage(fileName, image);
                imagePaths.add(imagePath);
            }
        }

        AddPlaces place = new AddPlaces();
        place.setPlaceName(placeName);
        place.setAboutPlace(aboutPlace);
        place.setDistrict(district);
        place.setStatus(status);
        place.setLatitude(latitude);
        place.setLongitude(longitude);
        place.setCategory(category);
        if (!imagePaths.isEmpty()) {
            place.setImages(String.join(",", imagePaths));
        }

        addPlaceService.savePlace(place);

        return ResponseEntity.ok("place saved successfully.");
    }

    private String saveImage(String fileName, MultipartFile image) throws IOException {
        String directoryPath = "C:\\Users\\shima\\AppData\\Local\\Temp\\tomcat.8081.6928745218397181951\\work\\Tomcat\\localhost\\ROOT\\resources\\static\\imageFolder";

        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        File file = new File(directory, fileName);

        image.transferTo(file);

        String relativePath = "/resources/static/imageFolder/" + fileName;
        return relativePath;

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
    @PutMapping("/update/{placeId}")
    public ResponseEntity<String> updatePlace(
            @PathVariable int placeId,
            @RequestParam String placeName,
            @RequestParam String category,
            @RequestParam String aboutPlace,
            @RequestParam String district,
            @RequestParam String status,
            @RequestParam double latitude,
            @RequestParam double longitude,
            @RequestParam(value = "image", required = false) MultipartFile image) {

        try {
            AddPlaces updatedPlace = addPlaceService.updatePlace(
                    placeId, placeName, category, aboutPlace, district, status, latitude, longitude, image);

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
    public ResponseEntity<String> deletePlace(@PathVariable Long placeId) {
        if (addPlaceService.deletePlace(placeId)) {
            return ResponseEntity.ok("Place deleted successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Place not found.");
        }
    }

}
