let container = document.getElementById('container');

const toggle = () => {
    container.classList.toggle('sign-in');
    container.classList.toggle('sign-up');
};

setTimeout(() => {
    container.classList.add('sign-in');
}, 200);

// Google Signup Popup
window.openPopupSignup = function () {
    const id = "popup-modal-id-3";

    window.dataLayer = window.dataLayer || [];

    function close() {
        document.getElementById(id).style.display = "none";
        setCookieVariable("DontShowEmailPopup", 180); // 3 min
    }

    document.getElementById("popup-signup-overlay-id").onclick = close;
    document.getElementById("popup-signup-form-close-id").onclick = close;
    document.getElementById(id).style.display = "flex";

    const currentUrl = window.location.pathname;
    const query = {
        errorUrl: currentUrl,
        loginTo: currentUrl,
        redirectTo: currentUrl
    };

    function onClickBtn() {
        setCookieVariable("DontShowEmailPopup", 180);
    }

    document.getElementById("signup-facebook").href = createLink("/challenge/facebook", query);
    document.getElementById("signup-google").href = createLink("/challenge/google", query);

    document.getElementById("signup-facebook").onclick = onClickBtn;
    document.getElementById("signup-google").onclick = onClickBtn;
    document.getElementById("signup-signin").onclick = onClickBtn;

    document.getElementById("email-form").onsubmit = function (e) {
        e.preventDefault();
        const email = document.getElementById("email-form-test").value;

        const xmlhttp = new XMLHttpRequest();
        xmlhttp.open("POST", "https://www.tipranks.com/api/general/SubscribeNewsletter");
        xmlhttp.setRequestHeader("Content-Type", "application/json");
        xmlhttp.send(JSON.stringify({ email }));

        xmlhttp.onreadystatechange = function () {
            if (this.readyState === 4 && this.status === 200) {
                dataLayer.push({
                    "event-category": "popup email",
                    "event-action": "popup email - social submit email",
                    "event-label": window.PageContentGroup,
                    event: "event-trigger"
                });

                setCookieVariable("TR_EmailSubscriber", 2592000); // 1 month in seconds
            }
        };
        close();
    };
};

function createLink(path, queryParams) {
    const queryString = new URLSearchParams(queryParams).toString();
    return `http://tipraks.com${path}?${queryString}`;
}

function setCookieVariable(name, seconds) {
    const d = new Date();
    d.setTime(d.getTime() + (seconds * 1000));
    document.cookie = `${name}=true;expires=${d.toUTCString()};path=/`;
}

// $(document).ready
$(function () {
    // Sign Up
    $('#btn-signup').click(function (event) {
        event.preventDefault();

        const username = $('#username').val();
        const email = $('#email').val();
        const password = $('#password').val();
        const confirmPassword = $('#confirmPassword').val();
        const role = $('#role').val();

        if (password !== confirmPassword) {
            Swal.fire('Passwords do not match!');
            return;
        }

        const formData = {
            name: username,
            email: email,
            password: password,
            role: role
        };

        $.ajax({
            url: 'http://localhost:8081/api/v1/user/register',
            type: 'POST',
            dataType: 'json',
            contentType: 'application/json',
            data: JSON.stringify(formData),
            success: function (response) {
                console.log(response);
                Swal.fire('User registered successfully!');
                $('#username, #email, #password, #confirmPassword, #role').val('');
                window.location.href = "../signup.html";
            },
            error: function (xhr, status, error) {
                console.error("Error:", error);
                Swal.fire("Registration failed. Please try again.");
                $('#username, #email, #password, #confirmPassword, #role').val('');
            }
        });
    });

    // Sign In
    $('#btn-signin').click(function (event) {
        event.preventDefault();

        const email = $('#email2').val().trim();
        const password = $('#password2').val().trim();

        if (!email || !password) {
            Swal.fire("Please enter both email and password.");
            return;
        }

        const userData = {
            email,
            password
        };

        $.ajax({
            url: 'http://localhost:8081/api/v1/auth/authenticate',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(userData),
            success: function (response) {
                console.log("Login Response:", response);

                if (response.code === 201 && response.data?.token) {
                    const { token, email: userEmail, role: userRole } = response.data;

                    localStorage.setItem('token', token);
                    localStorage.setItem('email', userEmail);
                    localStorage.setItem('role', userRole);

                    Swal.fire(`Welcome ${userRole === 'ADMIN' ? 'Admin' : 'User'}!`);

                    if (userRole === 'ADMIN') {
                        window.location.href = "../dashboard.html";
                    }else {
                        window.location.href="../js/../index.html"
                    }
                } else {
                    Swal.fire("Invalid credentials or server error.");
                }
            },
            error: function (xhr, status, error) {
                console.error("Login Error:", error);
                Swal.fire("Login failed. Please check your credentials and try again.");
            }
        });
    });
});
