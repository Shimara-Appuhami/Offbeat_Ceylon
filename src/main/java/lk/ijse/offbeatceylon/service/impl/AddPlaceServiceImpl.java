package lk.ijse.offbeatceylon.service.impl;

import lk.ijse.offbeatceylon.entity.AddPlaces;
import lk.ijse.offbeatceylon.entity.User;
import lk.ijse.offbeatceylon.repo.AddPlaceRepo;
import lk.ijse.offbeatceylon.repo.UserRepository;
import lk.ijse.offbeatceylon.service.AddPlaceService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class AddPlaceServiceImpl implements AddPlaceService {

    @Autowired
    private AddPlaceRepo addPlaceRepo;

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private UserRepository userRepository;

//    @Override
//    public boolean savePlace(AddPlaces place MultipartFile image) {
//
//            if (image != null && !image.isEmpty()) {
//                String imagePath = saveImage(image);
//                if (imagePath != null) {
//                    category.setCategoryImage(imagePath);
//                } else {
//                    return false;
//                }
//            }
//
//            categoryRepo.save(category);
//            return true;
//
//    }



    @Override
    public boolean savePlace(AddPlaces place, MultipartFile image) {
        try {

        if (image != null && !image.isEmpty()) {
            String imagePath = saveImage(image);
            if (imagePath != null) {
                place.setImages(imagePath);
            } else {
                return false;
            }
        }

        addPlaceRepo.save(place);
        return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }


    }
    public String saveImage(MultipartFile image) throws IOException {
        try {
            String fileName = UUID.randomUUID().toString();

            String fileExtension = getFileExtension(image);
            if (fileExtension == null) {
                return null;
            }

            String projectRootPath = System.getProperty("user.dir");

            Path path = Paths.get(projectRootPath, "uploads", "images", fileName + fileExtension);

            Files.createDirectories(path.getParent());

            image.transferTo(path.toFile());

            return path.toString();
        } catch (IOException e) {
            System.err.println("Error saving image: " + e.getMessage());
        }
        return null;
    }

    private String getFileExtension(MultipartFile image) {
        String contentType = image.getContentType();
        if (contentType != null) {
            switch (contentType) {
                case "image/jpeg":
                    return ".jpg";
                case "image/png":
                    return ".png";
                case "image/gif":
                    return ".gif";
                default:
                    return null;
            }
        }
        return null;
    }

    @Override
    public boolean existsByPlaceName(String placeName) {
        return addPlaceRepo.existsByPlaceName(placeName);
    }

    @Override
    public AddPlaces getPlaceByName(String placeName) {
        return addPlaceRepo.findByPlaceName(placeName);
    }

    @Override
    public AddPlaces getPlaceById(int placeId) {
        return addPlaceRepo.findById(placeId).orElse(null);
    }

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
    public boolean deletePlaceById(int placeId) {
        if (addPlaceRepo.existsById(placeId)) {
            addPlaceRepo.deleteById(placeId);
            return true;
        }
        return false;
    }

    @Override
    public AddPlaces updatePlace(String email, int placeId, String placeName, String category, String aboutPlace, String district,
                                 String status, double latitude, double longitude, MultipartFile image,String videoUrl) {
        AddPlaces existingPlace = addPlaceRepo.findByPlaceId(placeId);
        if (existingPlace == null) {
            return null;
        }

        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("User not found with email: " + email); // Handle the case where user is not found
        }
        existingPlace.setEmail(user);
        existingPlace.setPlaceName(placeName);
        existingPlace.setCategory(category);
        existingPlace.setAboutPlace(aboutPlace);
        existingPlace.setDistrict(district);
        existingPlace.setStatus(status);
        existingPlace.setLatitude(latitude);
        existingPlace.setLongitude(longitude);
        existingPlace.setVideoUrl(videoUrl);

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

    @Override
    public List<AddPlaces> getAllPlaces() {
        return addPlaceRepo.findAll();
    }

    @Override
    public List<AddPlaces> getPlacesByCategory(String category) {
        return addPlaceRepo.findAllByCategory(category);
    }

    @Override
    public List<AddPlaces> getPlacesByDistrict(String district) {
        return addPlaceRepo.findAllByDistrict(district);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public int getPlacesCount() {
        return (int) addPlaceRepo.count();
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
