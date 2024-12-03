document.addEventListener("DOMContentLoaded", async () => {
    const roleDropdown = document.getElementById("newRole");
    const responseMessage = document.getElementById("responseMessage");

    // Fetch roles from backend
    try {
        const response = await fetch("/admin/roles", {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
            },
        });

        if (response.ok) {
            const roles = await response.json();
            // Populate dropdown with roles
            roles.forEach(role => {
                const option = document.createElement("option");
                option.value = role;
                option.textContent = role;
                roleDropdown.appendChild(option);
            });
        } else {
            throw new Error("Failed to fetch roles");
        }
    } catch (err) {
        responseMessage.textContent = "Error loading roles. Please try again later.";
        responseMessage.style.color = "red";
        console.error(err);
    }

    // Handle form submission
    const form = document.getElementById("delegateRoleForm");
    form.addEventListener("submit", async (event) => {
        event.preventDefault(); // Prevent page reload

        // Get form data
        const targetUsername = document.getElementById("targetUsername").value;
        const newRole = roleDropdown.value;

        // Send the POST request to the backend
        try {
            const response = await fetch("/admin/delegate-role", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({ targetUsername, newRole }),
                credentials: "include", // Include session cookies
            });

            if (response.ok) {
                const result = await response.text();
                responseMessage.textContent = result;
                responseMessage.style.color = "green";
            } else {
                const error = await response.text();
                responseMessage.textContent = `Error: ${error}`;
                responseMessage.style.color = "red";
            }
        } catch (err) {
            responseMessage.textContent = "An unexpected error occurred.";
            responseMessage.style.color = "red";
            console.error(err);
        }
    });
});