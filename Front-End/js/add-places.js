// //add place ajax
// $('#btn-save-addPlaces').on('submit', function(e) {
//     e.preventDefault();
//
//     let placeName = $('#placeName').val();
//     let aboutPlace = $('#aboutPlace').val();
//     let district = $('#district').val();
//     let placeAddress = $('#s').val();
//     let placeLat = $('#placeLat').val();
//     let placeLong = $('#placeLong').val();
//
//
//     $.ajax({
//         url: 'http://localhost:8081/api/v1/addPlace/save',
//         method: 'POST',
//         data: {
//             name: placeName,
//             address: placeAddress
//         },
//         success: function(response) {
//             console.log('Place added successfully:', response);
//             $('#addPlaceForm').trigger('reset');
//         },
//         error: function(error) {
//             console.error('Error adding place:', error);
//         }
//     });
// })
let map;
let marker;

function initMap() {
    const initialPosition = { lat: 6.9271, lng: 79.8612 };
    map = new google.maps.Map(document.getElementById("map"), {
        center: initialPosition,
        zoom: 10,
    });

    map.addListener("click", (e) => {
        placeMarkerAndPanTo(e.latLng, map);
    });
}

function placeMarkerAndPanTo(latLng, map) {
    if (marker) {
        marker.setPosition(latLng);
    } else {
        marker = new google.maps.Marker({
            position: latLng,
            map: map,
        });
    }
    document.getElementById('latitude').value = latLng.lat();
    document.getElementById('longitude').value = latLng.lng();
}


document.getElementById('addPlaceForm').addEventListener('submit', function (e) {
    e.preventDefault(); // Prevent default form submission

    // Get form field values
    const placeName = document.getElementById('placeName').value;
    const aboutPlace = document.getElementById('aboutPlace').value;
    const district = document.querySelector('select').value;
    const images = document.getElementById('images').files;
    const status = document.querySelector('input[name="status"]:checked').value;

    // Simulate location capture (if using a static iframe map, you may need to add a way to capture this dynamically)
    const location = document.getElementById('location').src; // Alternatively, use coordinates

    // Prepare FormData (especially for file uploads)
    const formData = new FormData();
    formData.append('placeName', placeName);
    formData.append('aboutPlace', aboutPlace);
    formData.append('district', district);
    formData.append('status', status);
    formData.append('location', location);

    // Append multiple images
    for (let i = 0; i < images.length; i++) {
        formData.append('images', images[i]); // Backend must handle multiple files with same key 'images'
    }

    // Debug form data (Optional)
    // for (var pair of formData.entries()) {
    //     console.log(pair[0]+ ', ' + pair[1]);
    // }

    // Send data via Fetch API
    fetch('http://localhost:8081/addPlace/save', { // <-- Change to your backend API URL
        method: 'POST',
        body: formData
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not OK');
            }
            return response.json(); // Assuming backend responds with JSON
        })
        .then(data => {
            console.log('Success:', data);
            alert('Place added successfully!');
            document.getElementById('addPlaceForm').reset(); // Reset form
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Failed to add place. Please try again.');
        });
});
