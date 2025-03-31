$(document).ready(function () {
    function fetchCategories() {
        $.ajax({
            url: 'http://localhost:8081/api/v1/category/getAll',
            type: 'GET',
            dataType: 'json',
            headers: {
                'Authorization': 'Bearer ' + localStorage.getItem('token'),
                'category': 'category'
            },
            success: function (response) {
                console.log("Categories fetched:", response);
                const cardContainer = $('#category-cards');
                cardContainer.empty();

                if (!response || response.length === 0) {
                    cardContainer.append('<p>No categories available.</p>');
                    return;
                }

                response.forEach(category => {
                    const safeCategoryName = $('<div>').text(category.categoryName).html();
                    const imageFilename = category.categoryImage ? category.categoryImage.split("\\").pop() : "default.jpg";
                    const imageUrl = `http://localhost:8081/api/v1/uploads/${imageFilename}`;

                    cardContainer.append(`
                            <a class="card" href="#" data-category-id="${category.id}" data-category-name="${category.categoryName}">
                                <img src="${imageUrl}" class="card__background" alt="${safeCategoryName}">
                                <div class="card__content">
                                    <p class="card__category">${safeCategoryName}</p>
                                </div>
                            </a>
                        `);
                });
            },
            error: function (xhr) {
                console.error("Error fetching categories:", xhr.responseText);
                $('#category-cards').empty().append('<p>Failed to load categories. Please try again later.</p>');
            }
        }).fail(function (jqXHR, textStatus, errorThrown) {
            console.error("AJAX error:", textStatus, errorThrown);
        });
    }

    fetchCategories();

    // When a category card is clicked
    $('#category-cards').on('click', '.card', function () {
        const categoryId = $(this).data('category-id');
        const categoryName = $(this).data('category-name');

        // Fetch places related to the selected category
        $.ajax({
            url: `http://localhost:8081/api/v1/addPlace/getAllByCategory/${categoryName}`,
            type: 'GET',
            dataType: 'json',
            headers: {
                'Authorization': 'Bearer ' + localStorage.getItem('token')
            },
            success: function (response) {
                console.log("Places fetched for category:", categoryName);
                localStorage.setItem('categoryName',categoryName)

                // Redirect to places page or update the UI with places
                if (categoryName === "MOUNTAINS") {
                    window.location.href = "mountains.html";
                }else if (categoryName === "WATERFALLS") {
                    window.location.href = "waterfalls.html";
                }
                // Update places on the page dynamically if needed
            },
            error: function (xhr) {
                console.error("Error fetching places for category:", categoryId, xhr.responseText);
            }
        }).fail(function (jqXHR, textStatus, errorThrown) {
            console.error("AJAX error:", textStatus, errorThrown);
        });
    });
});
//     -------------------------------------------------------------------
$(document).ready(function () {
    function getAllPlaces(district) {
        if (!district) {
            $('#district-by-places-cards').html('<p>Please select a district.</p>');
            return;
        }

        $.ajax({
            url: `http://localhost:8081/api/v1/addPlace/getAllByDistrict/${district}`,
            type: 'GET',
            dataType: 'json',
            headers: {
                'Authorization': 'Bearer ' + localStorage.getItem('token')
            },
            success: function (response) {
                console.log(response)
                const container = $('#district-by-places-cards');
                container.empty();

                if (!response || response.length === 0) {
                    container.append('<p>No places available in this district.</p>');
                    return;
                }

                response.forEach((place, index) => {
                    const placeName = place.placeName || "Unknown Location";
                    const placeDescription = place.aboutPlace || "No description available.";
                    const imageFilename = place.images ? place.images.split("\\").pop() : "default.jpg";
                    const imageUrl = `http://localhost:8081/api/v1/uploads/${imageFilename}`;
                    const lat = parseFloat(place.latitude) || 7.8731;
                    const lng = parseFloat(place.longitude) || 80.7718;
                    const videoUrl = place.videoUrl ? extractYouTubeID(place.videoUrl) : null;
                    const status = place.status;
                    const mapId = `map-${index}`; // Unique ID for each map

                    const cardHtml = `
                        <div class="cardd">
                            <div class="image-containerr">
                                <img src="${imageUrl}" alt="${placeName}" />
                            </div>
                            <div class="reaction">
                                <div class="heart" onclick="checkStyle()"></div>
                            </div>
                            <div class="contentt">
                                <h3>${placeName}</h3>
                                <p>${placeDescription}</p>
                                <p><strong>Status:</strong> ${status}</p>
                                <div class="map-sectionn">
                                    <div id="${mapId}" class="map-container"></div> <!-- Unique Map ID -->
                                </div>
                                <div class="video-containerr">
                                    ${videoUrl ? `<iframe src="https://www.youtube.com/embed/${videoUrl}" frameborder="0" allowfullscreen></iframe>` : ''}
                                </div>
                            </div>
                        </div>
                    `;

                    container.append(cardHtml);

                    // Wait a bit to ensure the element is in the DOM before initializing the map
                    setTimeout(() => {
                        initMap(lat, lng, mapId);
                    }, 500);
                });
            },
            error: function () {
                $('#district-by-places-cards').append('<p>Failed to load places. Please try again later.</p>');
            }
        });
    }

    function initMap(lat, lng, mapId) {
        const mapDiv = document.getElementById(mapId);
        if (!mapDiv) {
            console.error(`Map div ${mapId} not found!`);
            return;
        }

        const position = { lat, lng };
        const map = new google.maps.Map(mapDiv, {
            zoom: 10,
            center: position,
        });

        new google.maps.Marker({ position: position, map: map });
    }

    // Extract YouTube Video ID
    function extractYouTubeID(url) {
        const regex = /(?:youtube\.com\/.*[?&]v=|youtu\.be\/)([^"&?\/\s]{11})/;
        const match = url.match(regex);
        return match ? match[1] : "";
    }

    // Trigger API call when a district is selected
    $('#districtSelect').change(function () {
        const selectedDistrict = $(this).val();
        getAllPlaces(selectedDistrict);
    });
});


//     contact
document.getElementById('contactForm').addEventListener('submit', function(event) {
    var name = document.getElementById('name').value;
    var email = document.getElementById('email').value;
    var message = document.getElementById('message').value;

    if (!name || !email || !message) {
        alert('Please fill out all fields.');
        event.preventDefault();
    } else {
        alert('Thank you for reaching out! We will get back to you soon.');
    }
});

//heart react
$(document).ready(function() {
    var style = localStorage.getItem("styleIcon");
    if (style == null) {
        $(".heart").removeClass("is-active");
        localStorage.setItem("styleIcon", "list");
    } else if (style == "grid") {
        $(".heart").addClass("is-active");
    } else if (style == "list") {
        $(".heart").removeClass("is-active");
    }
});
function checkStyle() {
    var style = localStorage.getItem("styleIcon");
    if (style == "grid") {
        $(".heart").removeClass("is-active");
        localStorage.setItem("styleIcon", "list");
    } else if (style == "list") {
        $(".heart").addClass("is-active");
        localStorage.setItem("styleIcon", "grid");
    }
};