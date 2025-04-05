let selectedPlaces = [];
let markers = [];
let map, directionsService, directionsRenderer;
let userLocation = null;

function initializeMap() {
    map = new google.maps.Map(document.getElementById("map"), {
        zoom: 8,
        center: { lat: 7.8731, lng: 80.7718 },
    });
    directionsService = new google.maps.DirectionsService();
    directionsRenderer = new google.maps.DirectionsRenderer();
    directionsRenderer.setMap(map);

    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(
            (position) => {
                userLocation = {
                    lat: position.coords.latitude,
                    lng: position.coords.longitude,
                };
                map.setCenter(userLocation);
                const userMarker = new google.maps.Marker({
                    position: userLocation,
                    map: map,
                    title: "Your Location",
                    icon: "http://maps.google.com/mapfiles/ms/icons/blue-dot.png"
                });
                markers.push(userMarker);
            },
            () => {
                alert("Unable to get your location. Make sure location services are enabled.");
            }
        );
    }
}

$(document).ready(function () {
    initializeMap();

    function getAllPlaces(district) {
        if (!district) {
            $('#place-cards-container').html('<p>Please select a district.</p>');
            return;
        }

        $('.card').hide();

        $.ajax({
            url: `http://localhost:8081/api/v1/addPlace/getAllByDistrict/${district}`,
            type: 'GET',
            dataType: 'json',
            headers: { 'Authorization': 'Bearer ' + localStorage.getItem('token') },
            success: function (response) {
                const container = $('#place-cards-container');

                if (!response || response.length === 0) {
                    container.append('<p>No places available in this district.</p>');
                    return;
                }

                response.forEach((place) => {
                    const placeName = place.placeName || "Unknown Location";
                    const placeDescription = place.aboutPlace || "No description available.";
                    const imageFilename = place.images ? place.images.split("\\").pop() : "default.jpg";
                    const imageUrl = `http://localhost:8081/api/v1/uploads/${imageFilename}`;
                    const lat = parseFloat(place.latitude) || 7.8731;
                    const lng = parseFloat(place.longitude) || 80.7718;

                    const isChecked = selectedPlaces.some(p => p.lat === lat && p.lng === lng);

                    let existingCard = $(`.card[data-lat='${lat}'][data-lng='${lng}']`);

                    if (existingCard.length > 0) {
                        existingCard.show();
                    } else {
                        const cardHtml = `
                        <div class="card" data-district="${district}" data-lat="${lat}" data-lng="${lng}" style="box-shadow: black">
                            <div class="image-container">
                                <img src="${imageUrl}" alt="${placeName}" />
                            </div>
                            <div class="content">
                                <h3>${placeName}</h3>
                                <p>${placeDescription}</p>
                                <div class="checkbox-container">
                                    <label>Select this place</label>
                                    <label>
                                        <input type="checkbox" class="place-checkbox" data-lat="${lat}" data-lng="${lng}" data-name="${placeName}" ${isChecked ? "checked" : ""}>
                                        <img src="../js/../assets/icon/icons8-select-48.png">
                                    </label>
                                </div>
                                <h3 style="color: #0048c6">Click Again to UnSelect<br/></h3>
                            </div>
                        </div>
                    `;
                        container.append(cardHtml);
                    }
                });

                $('.place-checkbox').change(function () {
                    const lat = parseFloat($(this).data('lat'));
                    const lng = parseFloat($(this).data('lng'));
                    const name = $(this).data('name');

                    if (this.checked) {
                        selectedPlaces.push({ lat, lng, name });
                    } else {
                        selectedPlaces = selectedPlaces.filter(place => place.lat !== lat || place.lng !== lng);
                    }

                    updateMap();
                    $('#getDirections').toggle(selectedPlaces.length > 0);
                });
            },
            error: function () {
                $('#place-cards-container').append('<p>Failed to load places. Please try again later.</p>');
            }
        });
    }


    function clearMarkers() {
        markers.forEach(marker => marker.setMap(null));
        markers = [];
    }

    function updateMap() {
        clearMarkers();
        selectedPlaces.forEach(place => {
            const marker = new google.maps.Marker({
                position: { lat: place.lat, lng: place.lng },
                map: map,
                title: place.name
            });
            markers.push(marker);
        });
    }

    function getDirections() {
        if (!userLocation) {
            alert("Could not get your current location.");
            return;
        }

        if (selectedPlaces.length < 1) {
            alert("Please select at least one location.");
            return;
        }

        const waypoints = selectedPlaces.slice(0, -1).map(place => ({
            location: new google.maps.LatLng(place.lat, place.lng),
            stopover: true
        }));

        const request = {
            origin: userLocation,
            destination: new google.maps.LatLng(selectedPlaces[selectedPlaces.length - 1].lat, selectedPlaces[selectedPlaces.length - 1].lng),
            waypoints: waypoints,
            travelMode: google.maps.TravelMode.DRIVING,
            optimizeWaypoints: false
        };

        directionsService.route(request, (result, status) => {
            if (status === google.maps.DirectionsStatus.OK) {
                directionsRenderer.setDirections(result);
            } else {
                alert("Failed to get directions. Please try again.");
            }
        });
    }

    $('#districtSelect').change(function () {
        getAllPlaces($(this).val());
    });

    $('#getDirections').click(getDirections);
});
