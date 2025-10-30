const API_URL = 'http://localhost:8084';

// Devuelve el access token guardado (si existe)
function getAccessToken() {
    return localStorage.getItem('accessToken');
}

// Devuelve el cognitoId del usuario actual
function getCognitoId() {
    return localStorage.getItem('cognitoId');
}

// Construye headers con Authorization si hay token
function authHeaders(extraHeaders = {}) {
    const token = getAccessToken();
    const headers = Object.assign({ 'Content-Type': 'application/json' }, extraHeaders);
    if (token) headers['Authorization'] = `Bearer ${token}`;
    return headers;
}

// Small helper to decode JWT payload (no verification, only for UI display)
function decodeJwtPayload(token) {
    if (!token) return null;
    try {
        const parts = token.split('.');
        if (parts.length < 2) return null;
        const payload = parts[1].replace(/-/g, '+').replace(/_/g, '/');
        const json = decodeURIComponent(atob(payload).split('').map(function (c) {
            return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
        }).join(''));
        return JSON.parse(json);
    } catch (e) {
        return null;
    }
}


// Crear post
document.getElementById('postForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    const content = document.getElementById('content').value;
    const cognitoId = getCognitoId();

    if (!cognitoId) {
        alert('⚠️ No estás autenticado. Por favor, inicia sesión.');
        window.location.href = 'login.html';
        return;
    }

    try {
        const response = await fetch(`${API_URL}/posts?cognitoId=${cognitoId}`, {
            method: 'POST',
            headers: authHeaders(),
            body: JSON.stringify({ content })
        });
        if (response.ok) {
            alert('✅ Post creado con éxito');
            document.getElementById('content').value = '';
            loadStream();
        } else {
            const error = await response.text();
            throw new Error(error || 'Error al crear post');
        }
    } catch (error) {
        console.error('Error al crear post:', error);
        alert('❌ ' + error.message);
    }
});

// Cargar stream
async function loadStream() {
    try {
        const response = await fetch(`${API_URL}/stream`, { headers: authHeaders() });
        if (!response.ok) {
            throw new Error('Error al cargar stream');
        }
        const stream = await response.json();
        const list = document.getElementById('streamList');
        list.innerHTML = '';
        stream.posts.sort((a, b) => new Date(b.timestamp) - new Date(a.timestamp));
        stream.posts.forEach(post => {
            const li = document.createElement('li');
            li.innerHTML = `<strong>${post.user.username}:</strong> ${post.content} <em>(${new Date(post.timestamp).toLocaleString()})</em>`;
            list.appendChild(li);
        });
    } catch (error) {
        console.error(error);
    }
}

// Carga inicial del stream
loadStream();