package lk.ijse.offbeatceylon.service.impl;

import lk.ijse.offbeatceylon.entity.AddPlaces;
import lk.ijse.offbeatceylon.repo.AddPlaceRepo;
import lk.ijse.offbeatceylon.service.AddPlaceService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;


@Service
@Transactional
public class AddPlaceServiceImpl implements AddPlaceService {

    @Autowired
    private AddPlaceRepo addPlaceRepo;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public AddPlaces savePlace(AddPlaces place) {
        return addPlaceRepo.save(place);
    }

    @Override
    public boolean existsByPlaceName(String placeName) {
        return addPlaceRepo.existsByPlaceName(placeName);
    }


//    @Override
//    public List<AddPlaces> getAllPlaces() {
//        return addPlaceRepo.findAll();
//    }

    @Override
    public AddPlaces getPlaceByName(String placeName) {
        return addPlaceRepo.findByPlaceName(placeName);
    }

    public AddPlaces getPlaceById(Long placeId) {
        return addPlaceRepo.findById(placeId).orElse(null);
    }

    //update
    @Override
    public AddPlaces updatePlace(AddPlaces place) {
        AddPlaces existingPlace = addPlaceRepo.findById(place.getPlaceId()).orElse(null);
        if (existingPlace != null) {
            modelMapper.map(place, existingPlace);
            return addPlaceRepo.save(existingPlace);
        }
        return null;
    }

    @Override
    public boolean deletePlace(Long placeId) {
        if (addPlaceRepo.existsById(placeId)) {
            addPlaceRepo.deleteById(placeId);
            return true;
        }
        return false;
    }

    @Override
    public AddPlaces updatePlace(int placeId, String placeName, String category, String aboutPlace, String district,
                                 String status, double latitude, double longitude, MultipartFile image) {
        AddPlaces existingPlace = addPlaceRepo.findByPlaceId(placeId);

        if (existingPlace == null) {
            return null;
        }

        existingPlace.setPlaceName(placeName);
        existingPlace.setCategory(category);
        existingPlace.setAboutPlace(aboutPlace);
        existingPlace.setDistrict(district);
        existingPlace.setStatus(status);
        existingPlace.setLatitude(latitude);
        existingPlace.setLongitude(longitude);

        if (image != null && !image.isEmpty()) {
            try {
                String fileName = StringUtils.cleanPath(image.getOriginalFilename());
                String imagePath = saveImage(fileName, image);
                existingPlace.setImages(imagePath);
            } catch (IOException e) {
                throw new RuntimeException("Error saving image: " + e.getMessage(), e);
            }
        }
        return addPlaceRepo.save(existingPlace);
    }
    private String saveImage(String fileName, MultipartFile image) throws IOException {
        String directoryPath = "C:\\Users\\shima\\AppData\\Local\\Temp\\tomcat.8081.6928745218397181951\\work\\Tomcat\\localhost\\ROOT\\resources\\static\\imageFolder";

        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        File file = new File(directory, fileName);
        image.transferTo(file);

        return "/resources/static/imageFolder/" + fileName;
    }

}


