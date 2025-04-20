//total categories
function fetchTotalCategories() {
    fetch('http://localhost:8081/api/v1/category/getTotalCategories')
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            const totalCategories = data.totalCategories || data;
            document.getElementById('total-categories').textContent = totalCategories;
        })
        .catch(error => {
            console.error('Error fetching data:', error);
            document.getElementById('total-categories').textContent = 'Error fetching data';
        });
}


//total places
function fetchTotalPlaces() {
    fetch('http://localhost:8081/api/v1/addPlace/getPlacesCount')
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            const totalPlaces = data.totalPlaces || data;
            document.getElementById('total-places').textContent = totalPlaces;
        })
        .catch(error => {
            console.error('Error fetching data:', error);
            document.getElementById('total-places').textContent = 'Error fetching data';
        });
}

document.addEventListener("DOMContentLoaded", function () {
    const map = L.map('full-map').setView([7.8731, 80.7718], 7);

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '© OpenStreetMap contributors'
    }).addTo(map);

    fetch('http://localhost:8081/api/v1/addPlace/getAll')
        .then(response => response.json())
        .then(data => {
            data.forEach(place => {
                const {latitude, longitude, placeName} = place;

                if (latitude && longitude) {
                    L.marker([latitude, longitude])
                        .addTo(map)
                        .bindPopup(`<strong>${placeName}</strong>`)
                        .openPopup();
                }
            });
        })
        .catch(error => {
            console.error("Error loading places:", error);
        });
});
const searchInput = document.getElementById('search-input');
searchInput.addEventListener('input', () => {
    const query = searchInput.value.toLowerCase();
    // Filter your place list or cards here based on 'query'
});

document.addEventListener('DOMContentLoaded', function() {
    fetchTotalPlaces();
    fetchTotalCategories();

});
// date
const today = new Date();
const options = { year: 'numeric', month: 'long', day: 'numeric' };
document.getElementById('inline-date').textContent = today.toLocaleDateString(undefined, options);

