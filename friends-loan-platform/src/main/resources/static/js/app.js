const API_BASE = "http://localhost:8080/api";

// Token সংরক্ষণ
function saveToken(token) {
    localStorage.setItem("token", token);
}

function getToken() {
    return localStorage.getItem("token");
}

function logout() {
    localStorage.removeItem("token");
    window.location.href = "login.html";
}

// ✅ Updated apiRequest - Rating Submit + Token Handling Fixed
// ✅ FINAL FIXED apiRequest - Rating Submit + All POST requests এর জন্য
async function apiRequest(url, method = "GET", data = null) {
    const token = localStorage.getItem("token");

    const headers = {
        "Content-Type": "application/json"
    };

    if (token) {
        headers["Authorization"] = `Bearer ${token}`;
    }

    // URL ঠিক করা
    const fullUrl = url.startsWith("/api") ? url : "/api" + (url.startsWith("/") ? "" : "/") + url;

    console.log("API Request:", method, fullUrl, "Token Present:", !!token);

    try {
        const response = await fetch(fullUrl, {
            method: method,
            headers: headers,
            body: data ? JSON.stringify(data) : null
        });

        console.log("Response Status for", fullUrl, ":", response.status);

        if (response.status === 401) {
            alert("Session expired. Please login again.");
            localStorage.removeItem("token");
            window.location.href = "login.html";
            return null;
        }

        if (response.status === 403) {
            console.log("❌ Forbidden (403)");
            throw new Error("You don't have permission to access this resource");
        }

        if (!response.ok) {
            const errorText = await response.text().catch(() => "");
            throw new Error(errorText || "Request failed");
        }

        if (response.status === 204) return null;

        return await response.json();
    } catch (error) {
        console.error("Fetch Error for", fullUrl, ":", error);
        throw error;
    }
}

// Register
document.getElementById("registerForm")?.addEventListener("submit", async (e) => {
    e.preventDefault();

    const request = {
        name: document.getElementById("name").value,
        email: document.getElementById("email").value,
        phone: document.getElementById("phone").value,
        password: document.getElementById("password").value
    };

    try {
        const data = await apiRequest("/auth/register", "POST", request);
        alert("Registration Successful! Please Login.");
        window.location.href = "login.html";
    } catch (error) {
        alert("Error: " + error.message);
    }
});