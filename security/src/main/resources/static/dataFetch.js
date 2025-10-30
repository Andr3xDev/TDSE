// URL
const API_BASE_URL = 'http://localhost:8080/api/inventory';

const createForm = document.getElementById('create-form');
const searchForm = document.getElementById('search-form');
const updateForm = document.getElementById('update-form');
const deleteForm = document.getElementById('delete-form');
const resultsTableBody = document.getElementById('results-table-body');
const messageArea = document.getElementById('message-area');

// Save query
let lastSearchParams = new URLSearchParams();
let currentItems = [];

// Create 
createForm.addEventListener('submit', async (event) => {
    event.preventDefault();
    
    const formData = new FormData(createForm);
    const data = {
        name: formData.get('name'),
        address: formData.get('address'),
        price: parseInt(formData.get('price'), 10),
        size: parseInt(formData.get('size'), 10),
        description: formData.get('description')
    };

    try {
        const response = await fetch(API_BASE_URL, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        });

        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.message || 'Error al crear el artículo.');
        }

        const createdItem = await response.json();
        showMessage(`Artículo "${createdItem.name}" creado con éxito (ID: ${createdItem.id}).`, 'success');
        createForm.reset();
        performSearch();
    } catch (error) {
        showMessage(error.message, 'error');
    }
});

// Get
searchForm.addEventListener('submit', async (event) => {
    event.preventDefault();
    const formData = new FormData(searchForm);
    lastSearchParams = new URLSearchParams();

    if (formData.get('name')) lastSearchParams.append('name', formData.get('name'));
    if (formData.get('address')) lastSearchParams.append('address', formData.get('address'));
    if (formData.get('maxPrice')) lastSearchParams.append('maxPrice', formData.get('maxPrice'));
    if (formData.get('minSize')) lastSearchParams.append('minSize', formData.get('minSize'));
    
    performSearch();
});

// Update
updateForm.addEventListener('submit', async (event) => {
    event.preventDefault();
    const formData = new FormData(updateForm);
    const id = formData.get('id');

    if (!id) {
        showMessage('Por favor, selecciona un artículo de la tabla para editar.', 'error');
        return;
    }

    const payload = {};
    if (formData.get('name')) payload.name = formData.get('name');
    if (formData.get('address')) payload.address = formData.get('address');
    if (formData.get('price')) payload.price = parseInt(formData.get('price'), 10);
    if (formData.get('size')) payload.size = parseInt(formData.get('size'), 10);
    if (formData.get('description')) payload.description = formData.get('description');

    try {
        const response = await fetch(`${API_BASE_URL}/${id}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });

        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.message || 'Error al actualizar el artículo.');
        }

        showMessage(`Artículo ID: ${id} actualizado con éxito.`, 'success');
        updateForm.reset();
        performSearch(); // Refrescar la tabla
    } catch (error) {
        showMessage(error.message, 'error');
    }
});

// Delete
deleteForm.addEventListener('submit', async (event) => {
    event.preventDefault();
    const formData = new FormData(deleteForm);
    const id = formData.get('id');

    if (!id) {
        showMessage('Por favor, selecciona un artículo de la tabla para eliminar.', 'error');
        return;
    }

    try {
        const response = await fetch(`${API_BASE_URL}/${id}`, {
            method: 'DELETE'
        });

        if (!response.ok) {
            throw new Error('Error al eliminar el artículo.');
        }
        
        showMessage(`Artículo ID: ${id} eliminado con éxito.`, 'success');
        deleteForm.reset();
        updateForm.reset();
        performSearch();
    } catch (error) {
        showMessage(error.message, 'error');
    }
});

resultsTableBody.addEventListener('click', (event) => {
    const target = event.target;

    if (target.classList.contains('btn-edit')) {
        const id = target.dataset.id;
        const item = currentItems.find(i => i.id == id);
        if (item) {
            document.getElementById('update-id').value = item.id;
            document.getElementById('update-name').value = item.name;
            document.getElementById('update-address').value = item.address;
            document.getElementById('update-price').value = item.price;
            document.getElementById('update-size').value = item.size;
            document.getElementById('update-description').value = item.description || '';
            updateForm.scrollIntoView({ behavior: 'smooth' });
        }
    }

    if (target.classList.contains('btn-delete')) {
        const id = target.dataset.id;
        document.getElementById('delete-id').value = id;
        deleteForm.scrollIntoView({ behavior: 'smooth' });
    }
});


async function performSearch() {
    try {
        const response = await fetch(`${API_BASE_URL}/search?${lastSearchParams.toString()}`);

        if (!response.ok) {
            throw new Error('Error al realizar la búsqueda.');
        }
        
        const page = await response.json();
        displayResults(page.content);
    } catch (error) {
        showMessage(error.message, 'error');
    }
}

function displayResults(items) {
    resultsTableBody.innerHTML = '';
    currentItems = items;

    if (items.length === 0) {
        resultsTableBody.innerHTML = '<tr><td colspan="6" style="text-align:center;">No se encontraron resultados.</td></tr>';
        return;
    }

    items.forEach(item => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${item.id}</td>
            <td>${item.name}</td>
            <td>${item.address}</td>
            <td>${item.price}</td>
            <td>${item.size}</td>
            <td class="actions-cell">
                <button class="btn-edit" data-id="${item.id}">Editar</button>
                <button class="btn-delete" data-id="${item.id}">Eliminar</button>
            </td>
        `;
        resultsTableBody.appendChild(row);
    });
}

function showMessage(message, type) {
    messageArea.textContent = message;
    messageArea.className = type;
    messageArea.style.display = 'block';

    setTimeout(() => {
        messageArea.style.display = 'none';
    }, 5000);
}

document.addEventListener('DOMContentLoaded', () => {
    performSearch();
});