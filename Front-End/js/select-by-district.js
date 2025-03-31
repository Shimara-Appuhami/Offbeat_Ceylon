let selectedPlaces = [];
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

    // Get user's live location
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(
            (position) => {
                userLocation = {
                    lat: position.coords.latitude,
                    lng: position.coords.longitude,
                };
                map.setCenter(userLocation);
                new google.maps.Marker({
                    position: userLocation,
                    map: map,
                    title: "Your Location",
                    icon: "http://maps.google.com/mapfiles/ms/icons/blue-dot.png"
                });
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

        $.ajax({
            url: `http://localhost:8081/api/v1/addPlace/getAllByDistrict/${district}`,
            type: 'GET',
            dataType: 'json',
            headers: { 'Authorization': 'Bearer ' + localStorage.getItem('token') },
            success: function (response) {
                const container = $('#place-cards-container');
                container.empty();
                selectedPlaces = [];

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

                    const cardHtml = `
                            <div class="card">
                                <div class="image-container">
                                    <img src="${imageUrl}" alt="${placeName}" />
                                </div>
                                <div class="content">
                                    <h3>${placeName}</h3>
                                    <p>${placeDescription}</p>
                                    <div class="checkbox-container">
                                        <input type="checkbox" class="place-checkbox" data-lat="${lat}" data-lng="${lng}" data-name="${placeName}">
                                        <label>Select this place</label>
                                    </div>
                                </div>
                            </div>
                        `;
                    container.append(cardHtml);
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

    function updateMap() {
        selectedPlaces.forEach(place => {
            new google.maps.Marker({
                position: { lat: place.lat, lng: place.lng },
                map: map,
                title: place.name
            });
        });
    }

    function getDirections() {
        if (!userLocation) return alert("Could not get your current location.");

        if (selectedPlaces.length === 0) {
            alert("Please select at least one location.");
            return;
        }

        const waypoints = selectedPlaces.map(place => ({
            location: new google.maps.LatLng(place.lat, place.lng),
            stopover: true
        }));

        const request = {
            origin: userLocation,
            destination: waypoints[waypoints.length - 1].location,
            waypoints: waypoints.slice(0, -1),  // Remove the last waypoint from waypoints (it will be the destination)
            travelMode: google.maps.TravelMode.DRIVING,
            optimizeWaypoints: true // Let Google optimize the order
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