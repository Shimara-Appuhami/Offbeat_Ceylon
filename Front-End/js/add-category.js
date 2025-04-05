$(document).ready(function () {

    function fetchCategories() {
        $.ajax({
            url: 'http://localhost:8081/api/v1/category/getAll',
            type: 'GET',
            success: function (response) {
                const tableBody = $('#tbl-category');
                tableBody.empty();

                if (response.length === 0) {
                    tableBody.append('<tr><td colspan="5">No categories found.</td></tr>');
                    return;
                }


                response.forEach(category => {
                    const imageFilename = category.categoryImage ? category.categoryImage.split("\\").pop() :"default.jpg";

                    tableBody.append(`
        <tr>
            <td>${category.categoryId}</td>
            <td>${category.categoryName}</td>
            <td><img src="http://localhost:8081/api/v1/uploads/${imageFilename}" ></td>
            <td>${category.description}</td>
            <td>
                <button class="btn-delete" data-id="${category.categoryId}">Delete</button>
            </td>
        </tr>
    `);
                });

            },
            error: function (xhr) {
                alert("Failed to load categories. Error: " + xhr.responseText);
            }
        });
    }

    fetchCategories();

    $('#btn-add-category').on('click', function () {
        $('#category-form').fadeIn();
    });

    $('#btn-cancel').on('click', function () {
        $('#category-form').fadeOut();
    });

    $('#btn-save-category').on('click', function () {
        const categoryName = $('#category-name').val().trim();
        // const categoryImage = $('#category-image')[0].files;
        const categoryDescription = $('#category-description').val().trim();


        if (!categoryName || !categoryDescription) {
            alert('Please fill in all fields.');
            return;
        }

        const formData = new FormData();
        formData.append('categoryName', categoryName);
        // formData.append('categoryImage', categoryImage);
        formData.append('description', categoryDescription);
        if ($('#category-image')[0].files[0]) {
            formData.append('categoryImage', $('#category-image')[0].files[0]);
        }

        $.ajax({
            url: 'http://localhost:8081/api/v1/category/save',
            type: 'POST',
            headers: {
                'Authorization': 'Bearer ' + localStorage.getItem('token')
            },
            data: formData,
            processData: false,
            contentType: false,
            success: function () {
                alert('Category added successfully!');
                $('#category-form').val('');
                $('#category-name, #category-description').val('');
                $('#category-image').val('');
                fetchCategories();
            },
            error: function (xhr) {
                alert('Error adding category: ' + xhr.responseText);
            }
        });
    });

    $(document).on('click', '.btn-delete', function () {
        const categoryId = $(this).data('id');

        if (confirm('Are you sure you want to delete this category?')) {
            $.ajax({
                url: `http://localhost:8081/api/v1/category/delete/${categoryId}`,
                type: 'DELETE',
                headers: {
                    'Authorization': 'Bearer ' + localStorage.getItem('token')
                },
                success: function () {
                    alert('Category deleted successfully!');
                    fetchCategories();
                },
                error: function (xhr) {
                    alert('Error deleting category: ' + xhr.responseText);
                }
            });
        }
    });

});