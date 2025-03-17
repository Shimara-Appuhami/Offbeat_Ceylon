$(document).ready(function() {

    $("#placeForm").on("submit", function(e) {
        e.preventDefault(); // Prevent default form submission

        // Get form values
        var placeName = $("#placeName").val();
        var aboutPlace = $("#aboutPlace").val();
        var district = $("#district").val();
        var status = $("#status").val();
        var latitude = parseFloat($("#latitude").val());
        var longitude = parseFloat($("#longitude").val());

        // Get image file
        var file = $("#images")[0].files[0];

        // Check if file is selected
        if (!file) {
            alert("Please select an image file.");
            return;
        }

        // Convert image to Base64
        var reader = new FileReader();
        reader.onloadend = function() {
            var base64Image = reader.result; // Base64 string

            // Prepare data object to send
            var placeData = {
                placeName: placeName,
                aboutPlace: aboutPlace,
                district: district,
                status: status,
                latitude: latitude,
                longitude: longitude,
                images: base64Image // Store base64 image
            };

            // Send data using AJAX
            $.ajax({
                url: "http://localhost:8081/api/v1/addPlace/save", // Your API endpoint
                type: "POST",
                contentType: "application/json",
                data: JSON.stringify(placeData),
                success: function(response) {
                    alert("Place saved successfully!");
                    console.log(response);
                    $("#placeForm")[0].reset(); // Reset form after success
                },
                error: function(xhr, status, error) {
                    alert("Error saving place: " + xhr.responseText);
                    console.error(error);
                }
            });
        };

        reader.readAsDataURL(file); // Read file as base64
    });

});