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
@CrossOrigin("*")
public class AddPlaceController {

    @Autowired
    private AddPlaceRepo addPlacesRepo;

    @Autowired
    private AddPlaceService addPlaceService;

    // ✅ Save a new place
    @PostMapping("/save")
    public ResponseEntity<String> savePlace(
            @RequestParam("placeName") String placeName,
            @RequestParam("aboutPlace") String aboutPlace,
            @RequestParam("district") String district,
            @RequestParam("status") String status,
            @RequestParam("latitude") double latitude,
            @RequestParam("longitude") double longitude,
            @RequestParam(value = "images", required = false) List<MultipartFile> images) throws IOException {

        if (placeName.isEmpty() || aboutPlace.isEmpty() || district.isEmpty() || status.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("All fields are required.");
        }

        if (addPlaceService.existsByPlaceName(placeName)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Place with this name already exists.");
        }

        List<String> imagePaths = new ArrayList<>();
        if (images != null) {
            for (MultipartFile image : images) {
                String fileName = StringUtils.cleanPath(image.getOriginalFilename());
                String imagePath = saveImage(fileName, image); // implement saveImage accordingly
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
        if (!imagePaths.isEmpty()) {
            place.setImages(String.join(",", imagePaths));
        }

        addPlaceService.savePlace(place);

        return ResponseEntity.ok("place saved successfully.");
    }

    private String saveImage(String fileName, MultipartFile image) throws IOException {
        String directoryPath = "C:\\Users\\shima\\AppData\\Local\\Temp\\tomcat.8081.6928745218397181951\\work\\Tomcat\\localhost\\ROOT\\resources\\static\\imageFolder"; // Fixed path with proper separators

        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        File file = new File(directory, fileName);

        image.transferTo(file);

        String relativePath = "/resources/static/imageFolder/" + fileName;
        return relativePath;

    }

}
