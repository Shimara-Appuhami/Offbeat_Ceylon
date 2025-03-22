// $(document).ready(function() {
//
//     $("#placeForm").on("submit", function(e) {
//         e.preventDefault();
//
//         var placeName = $("#placeName").val();
//         var aboutPlace = $("#aboutPlace").val();
//         var district = $("#district").val();
//         var status = $("#status").val();
//         var latitude = parseFloat($("#latitude").val());
//         var longitude = parseFloat($("#longitude").val());
//
//         var file = $("#images")[0].files[0];
//
//         if (!file) {
//             alert("Please select an image file.");
//             return;
//         }
//
//         var reader = new FileReader();
//         reader.onloadend = function() {
//             var base64Image = reader.result;
//
//             var placeData = {
//                 placeName: placeName,
//                 aboutPlace: aboutPlace,
//                 district: district,
//                 status: status,
//                 latitude: latitude,
//                 longitude: longitude,
//                 images: base64Image
//             };
//
//             $.ajax({
//                 url: "http://localhost:8081/api/v1/addPlace/save",
//                 type: "POST",
//                 contentType: "application/json",
//                 data: JSON.stringify(placeData),
//                 success: function(response) {
//                     alert("Place saved successfully!");
//                     console.log(response);
//                     $("#placeForm")[0].reset();
//                 },
//                 error: function(xhr, status, error) {
//                     alert("Error saving place: " + xhr.responseText);
//                     console.error(error);
//                 }
//             });
//         };
//
//         reader.readAsDataURL(file);
//     });
//
// });
//
// //update