let container = document.getElementById('container')

toggle = () => {
    container.classList.toggle('sign-in')
    container.classList.toggle('sign-up')
}

setTimeout(() => {
    container.classList.add('sign-in')
}, 200)
//google
window.openPopupSignup = function () {
    var id = "popup-modal-id-3";

    window.dataLayer = window.dataLayer || [];

    function close() {
        document.getElementById(id).style.display = "none";
        setCookieVariable("DontShowEmailPopup", 180); //   3 min
    }

    document.getElementById("popup-signup-overlay-id").onclick = close;
    document.getElementById("popup-signup-form-close-id").onclick = close;
    document.getElementById(id).style.display = "flex";

    var currentUrl = window.location.pathname;
    var query = {
        errorUrl: currentUrl,
        loginTo: currentUrl,
        redirectTo: currentUrl
    };

    function onClickBtn(e) {
        setCookieVariable("DontShowEmailPopup", 180); //   3 min
    }
    //==================================================================================================
    document.getElementById("signup-facebook").href = createLink(
        "/challenge/facebook",
        query
    );
    document.getElementById("signup-google").href = createLink(
        "/challenge/google",
        query
    );
    //=========================
    document.getElementById("signup-facebook").onclick = onClickBtn;
    document.getElementById("signup-google").onclick = onClickBtn;
    document.getElementById("signup-signin").onclick = onClickBtn;
    //================================================================================

    document.getElementById("email-form").onsubmit = function submit(e) {
        var t = document.getElementById("email-form-test").value;

        e.preventDefault();
        var xmlhttp = new XMLHttpRequest();
        xmlhttp.open(
            "POST",
            "https://www.tipranks.com/api/general/SubscribeNewsletter"
        );
        xmlhttp.setRequestHeader("Content-Type", "application/json");
        xmlhttp.send(
            JSON.stringify({
                email: t
            })
        );

        xmlhttp.onreadystatechange = function () {
            if (this.readyState == 4 && this.status == 200) {
                dataLayer.push({
                    "event-category": "popup email",
                    "event-action": "popup email - social submit email",
                    "event-label": window.PageContentGroup,
                    event: "event-trigger"
                });

                setCookieVariable("TR_EmailSubscriber", 2592000); // 2592000= 1m, 7776000=3 months in seconds
            }
        };
        close();
    };
};

//======================DONT COPY THIS====================================
function createLink(q) {
    return "http://tipraks.com" + q;
}
function setCookieVariable() {
    return "fsdf";
}
// window.openPopupSignup();


////////////////////////

$(document).ready(function() {
    $('#btn-signup').click(function(event) {
        event.preventDefault();

        var username = $('#username').val();
        var email = $('#email').val();
        var password = $('#password').val();
        var confirmPassword = $('#confirmPassword').val();
        var role= $('#role').val();

        if (password !== confirmPassword) {
            alert('Passwords do not match!');
            return;
        }

        var formData = {
            name: username,
            email: email,
            password: password,
            role:role
        };

        $.ajax({
            url: 'http://localhost:8081/api/v1/user/register',
            type: 'POST',
            dataType: 'json',
            contentType: 'application/json',
            data: JSON.stringify(formData),
            success: function(response) {
                console.log(response);
                    alert('User registered successfully!');
                    $('#username').val('');
                    $('#email').val('');
                    $('#password').val('');
                    $('#confirmPassword').val('');
                    $('#role').val('');
                    window.location.href="../js/../index.html"


            },
            error: function(xhr, status, error) {
                console.log("Error:", error);
                alert("Error: " + xhr.responseText);

                $('#username').val('');
                $('#email').val('');
                $('#password').val('');
                $('#confirmPassword').val('');
                $('#role').val('');

            }
        });
    });
});

//login
$(document).ready(function () {
    $('#btn-signin').click(function (event) {
        event.preventDefault();

        var email = $('#email2').val();
        var password = $('#password2').val();

        if (!email || !password) {
            alert("Please enter both email and password.");
            return;
        }

        var userData = {
            email: email,
            password: password
        };

        $.ajax({
            url: 'http://localhost:8081/api/v1/auth/authenticate',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(userData),
            success: function (response) {
                console.log("Server Response:", response);
                localStorage.setItem('token', response.data.token);
                if (response.code === 201) {
                    if (response.role='USER'){
                        alert('Welcome User!');
                        window.location.href="../js/../index.html"
                    }else if (response.role='ADMIN'){
                        alert('Welcome Admin!');
                        window.location.href="../js/../index.html"
                    }else {
                        alert('Invalid Credentials!');
                    }
                }


            },
            error: function (xhr, status, error) {
                console.error("AJAX Error:", status, error);
                console.error("Response:", xhr.responseText);
                alert("Login failed. Please check your credentials and try again.");
            }
        });
    });
});
