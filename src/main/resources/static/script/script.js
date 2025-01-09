function togglePasswordVisibility(button) {
    const input = button.previousElementSibling;
    if (input.type === "password") {
    input.type = "text";
} else {
    input.type = "password";
}
}

