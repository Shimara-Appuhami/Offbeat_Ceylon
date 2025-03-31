const token=localStorage.getItem('token');


let selectedLat = null;
let selectedLng = null;
let map;
let marker = null;

function initMap() {
    const initialPosition = {lat: 7.8731, lng: 80.7718};

    map = new google.maps.Map(document.getElementById("map"), {
        zoom: 7,
        center: initialPosition,
    });

    map.addListener("click", (event) => {
        selectedLat = event.latLng.lat();
        selectedLng = event.latLng.lng();

        document.getElementById("coordinates").innerText =
            "Latitude: " + selectedLat + ", Longitude: " + selectedLng;

        if (marker) {
            marker.setPosition(event.latLng);
        } else {
            marker = new google.maps.Marker({
                position: event.latLng,
                map: map,
            });
        }
    });

    const savedPlaces = JSON.parse(localStorage.getItem("savedPlaces")) || [];
    if (savedPlaces.length > 0) {
        const savedPlace = savedPlaces[0];
        const savedPosition = {lat: savedPlace.latitude, lng: savedPlace.longitude};
        map.setCenter(savedPosition);
        map.setZoom(15);
        marker = new google.maps.Marker({
            position: savedPosition,
            map: map,
        });
        selectedLat = savedPlace.latitude;
        selectedLng = savedPlace.longitude;
        document.getElementById("coordinates").innerText =
            "Latitude: " + selectedLat + ", Longitude: " + selectedLng;
    }
}

window.onload = initMap;

$("#locationSearch").on("input", function () {
    const searchInput = $(this).val();
    if (!searchInput) return;

    const geocoder = new google.maps.Geocoder();
    geocoder.geocode({address: searchInput}, function (results, status) {
        if (status === "OK" && results[0]) {
            const location = results[0].geometry.location;
            selectedLat = location.lat();
            selectedLng = location.lng();

            map.setCenter(location);
            map.setZoom(15);

            if (marker) {
                marker.setPosition(location);
            } else {
                marker = new google.maps.Marker({
                    position: location,
                    map: map,
                });
            }

            document.getElementById("coordinates").innerText =
                "Latitude: " + selectedLat + ", Longitude: " + selectedLng;
        } else {
            console.warn("Geocode was not successful for the following reason: " + status);
        }
    });
});

$("#addPlaceForm").on("submit", function (e) {
    e.preventDefault();

    if (!selectedLat || !selectedLng) {
        alert("Please select a location on the map!");
        return;
    }

    const placeName = $("#placeName").val();
    const aboutPlace = $("#aboutPlace").val();
    const district = $("#district").val();
    const status = $('input[name="status"]:checked').val();
    // const images = $("#images")[0].files;
    const category=$("#category").val();
    const email = localStorage.getItem('email'); // Assuming userId is stored in localStorage

    if (!email) {
        alert("email is not logged in or email is missing!");
        return;
    }


    const formData = new FormData();
    formData.append('placeName', placeName);
    formData.append('aboutPlace', aboutPlace);
    formData.append('district', district);
    formData.append('status', status);
    formData.append('latitude', selectedLat);
    formData.append('longitude', selectedLng);
    formData.append('category', category);
    formData.append("email", email); // Add userId to the request


    // for (let i = 0; i < images.length; i++) {
    //     formData.append('images', images[i]);
    // }
    if ($('#images')[0].files[0]) {
        formData.append('images', $('#images')[0].files[0]);
    }
    $.ajax({
        url: 'http://localhost:8081/api/v1/addPlace/save',
        type: 'POST',
        data: formData,
        contentType: false,
        processData: false,
        headers: {
            "Authorization": "Bearer " + localStorage.getItem('token')
        },
        success: function (response) {

            console.log("Response received:", response);
            console.log("token"+token);


            alert('Place added successfully!');
            $("#addPlaceForm")[0].reset();
            $("#coordinates").text("");
            // localStorage.removeItem("savedPlaces");
            // window.location.href = "index.html";
        },
        error: function (xhr, status, error) {
            console.error('Error:', error);
            alert('Failed to add place!');
        }
    });
});

window.addEventListener("beforeunload", function () {
    if (selectedLat && selectedLng) {
        const currentPlace = {latitude: selectedLat, longitude: selectedLng};
        localStorage.setItem("savedPlaces", JSON.stringify([currentPlace]));
    }
});

let placeId = null;

//search
$("#placeName").on("keypress", function (event) {
    if (event.which === 13) {
        event.preventDefault();
        const placeName = $(this).val().trim();

        if (placeName) {
            $.ajax({
                url: `http://localhost:8081/api/v1/addPlace/getAllByName/${placeName}`,
                type: "GET",
                headers: {
                    "Authorization": "Bearer " + localStorage.getItem('token')
                },
                success: function (response) {
                    console.log("Response received:", response);

                    if (response && response.placeId) {
                        placeId = response.placeId;

                        $("#category").val(response.category);
                        $("#aboutPlace").val(response.aboutPlace);
                        $("#district").val(response.district);
                        $("input[name='status'][value='" + response.status + "']").prop("checked", true);

                        if (response.latitude && response.longitude) {
                            selectedLat = response.latitude;
                            selectedLng = response.longitude;

                            map.setCenter({ lat: selectedLat, lng: selectedLng });
                            map.setZoom(15);

                            if (marker) {
                                marker.setPosition({ lat: selectedLat, lng: selectedLng });
                            } else {
                                marker = new google.maps.Marker({
                                    position: { lat: selectedLat, lng: selectedLng },
                                    map: map,
                                });
                            }

                            $("#coordinates").text(`Latitude: ${selectedLat}, Longitude: ${selectedLng}`);
                        }

                        if (response.imageUrl) {
                            $("#images").attr("src", response.imageUrl).show();
                        } else {
                            $("#images").hide();
                        }
                    } else {
                        alert("Place not found or placeId is missing!");
                    }
                },
                error: function (xhr, status, error) {
                    console.error("Error fetching place:", error);
                    alert("Error fetching place details!");
                }
            });
        }
    }
});

$("#btn-update-place").on("click", function () {
    if (placeId === null) {
        alert("Please search for a place first!");
        console.warn("Update attempt failed: placeId is null.");
        return;
    }

    const formData = new FormData();
    formData.append("placeName", $("#placeName").val().trim());
    formData.append("category", $("#category").val());
    formData.append("aboutPlace", $("#aboutPlace").val().trim());
    formData.append("district", $("#district").val());
    formData.append("status", $("input[name='status']:checked").val());
    formData.append("latitude", selectedLat);
    formData.append("longitude", selectedLng);

    const imageInput = $("#imageUpload")[0];
    if (imageInput && imageInput.files.length > 0) {
        formData.append("image", imageInput.files[0]);
    }

    $.ajax({
        url: `http://localhost:8081/api/v1/addPlace/update/${placeId}`,
        type: "PUT",
        headers: {
            "Authorization": "Bearer " + localStorage.getItem('token')
        },
        data: formData,
        processData: false,
        contentType: false,
        success: function (response) {
            alert("Place updated successfully!");
            console.log("Update Response:", response);
        },
        error: function (xhr, status, error) {
            console.error("Error updating place:", error);
            alert("Error updating place details!");
        }
    });
});

//delete
$("#btn-delete-place").on("click", function () {
    if (!placeId || isNaN(placeId)) {
        alert("Please search for a valid place before deleting!");
        console.warn("Delete attempt failed: Invalid placeId.");
        return;
    }

    if (confirm("Are you sure you want to delete this place?")) {
        $.ajax({
            url: `http://localhost:8081/api/v1/addPlace/delete/${placeId}`,
            type: "DELETE",
            headers: {
                "Authorization": "Bearer " + localStorage.getItem('token')
            },
            success: function (response) {
                alert("Place deleted successfully!");
                console.log("Delete Response:", response);

                placeId = null;
                $("#placeName").val("");
                $("#category").val("");
                $("#aboutPlace").val("");
                $("#district").val("");
                $("input[name='status']").prop("checked", false);
                $("#coordinates").text("");
                $("#images").hide();
                if (marker) {
                    marker.setMap(null);
                }
            },
            error: function (xhr, status, error) {
                console.error("Error deleting place:", er/ror);
                alert("Error deleting place details!");
            }
        });
    }
});