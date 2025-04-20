package lk.ijse.offbeatceylon.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "addPlace")
public class AddPlaces {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int placeId;
    @Column(unique = true)
    @NotBlank(message = "Place Name is required")
    @Pattern(regexp = "^[A-Za-z]+$", message ="place name must only letters and spaces")
    @Size(min = 1, max =200)
    private String placeName;
    @Size(min = 50, max = 150, message = "About Place must be between minimum 50 words or maximum 150 words")
    private String aboutPlace;
    private String district;
    private String images;
    private String status;
    private Double latitude;
    private Double longitude;
    private String category;
    private String videoUrl;
    @ManyToOne
    @JoinColumn(name = "email", referencedColumnName = "email")
    private User email;
    private String pending = "PENDING";

    public String getPending() {
        return pending;
    }

    public void setPending(String pending) {
        this.pending = pending;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public User getEmail() {
        return email;
    }

    public void setEmail(User email) {
        this.email = email;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getPlaceId() {
        return placeId;
    }

    public void setPlaceId(int placeId) {
        this.placeId = placeId;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getAboutPlace() {
        return aboutPlace;
    }

    public void setAboutPlace(String aboutPlace) {
        this.aboutPlace = aboutPlace;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
