$(document).ready(function () {
    loadUsers();

    $('#registerForm').submit(function (event) {
        event.preventDefault();
        const user = {
            email: $('#email').val(),
            password: $('#password').val(),
            name: $('#name').val(),
            role: $('#role').val()
        };

        $.ajax({
            url: 'http://localhost:8081/api/v1/user/register',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(user),
            success: function (response) {
                Swal.fire(response.message);
                loadUsers();
                $('#registerForm')[0].reset();
            },
            error: function (error) {
                Swal.fire('Registration failed: ' + error.responseJSON.message);
            }
        });
    });

    function loadUsers() {
        $.ajax({
            url: 'http://localhost:8081/api/v1/user/getAll',
            type: 'GET',
            success: function (users) {
                $('#userTable tbody').empty();
                users.forEach(user => {
                    $('#userTable tbody').append(`
                            <tr>
                                <td>${user.name}</td>
                                <td>${user.email}</td>
                                <td>${user.role}</td>
                                <td>
                                    <button class="edit-btn btn btn-info" data-name="${user.name}" data-email="${user.email}" data-role="${user.role}">Edit</button>
                                    <button class="delete-btn btn btn-danger" data-name="${user.name}">Delete</button>
                                </td>
                            </tr>
                        `);
                });
            }
        });
    }

    $(document).on('click', '.edit-btn', function () {
        $('#name').val($(this).data('name'));
        $('#email').val($(this).data('email'));
        $('#role').val($(this).data('role'));
        $('#updateBtn').data('email', $(this).data('email'));
    });

    $('#updateBtn').click(function () {
        const userEmail = $(this).data('email');
        if (!userEmail) {
            Swal.fire('Please select a user to update');
            return;
        }
        const password = $('#password').val();

        const user = {
            email: userEmail,
            password: password,
            name: $('#name').val(),
            role: $('#role').val()
        };

        $.ajax({
            url: `http://localhost:8081/api/v1/user/update`,
            type: 'PUT',
            contentType: 'application/json',
            data: JSON.stringify(user),
            success: function (response) {
                Swal.fire(response.message);
                loadUsers();
                $('#registerForm')[0].reset();
                // $('#updateBtn').removeData('email');
                console.log(response)

            },
            error: function (error) {
                Swal.fire('Update failed: ' + (error.responseJSON ? error.responseJSON.message : 'User not found'));
            }
        });
    });

    $(document).on('click', '.delete-btn', function () {
        const uid=this.id;
        if (confirm('Are you sure you want to delete this user?')) {
            $.ajax({
                url: `http://localhost:8081/api/v1/user/delete/${uid}`,
                type: 'DELETE',
                success: function (response) {
                    Swal.fire(response.message);
                    loadUsers();
                },
                error: function (error) {
                    Swal.fire('Delete failed: ' + (error.responseJSON ? error.responseJSON.message : 'User not found'));
                }
            });
        }
    });
});