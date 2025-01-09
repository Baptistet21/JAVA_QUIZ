function togglePasswordVisibility() {
    var passwordFields = document.querySelectorAll('#password, #confirm_password');
    passwordFields.forEach(function(field) {
        if (field.type === 'password') {
            field.type = 'text';
        } else {
            field.type = 'password';
        }
    });
}