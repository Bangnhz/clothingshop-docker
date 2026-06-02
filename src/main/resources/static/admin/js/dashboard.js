let loadedData = {
    products: false,
    categories: false,
    orders: false,
    users: false,
    stats: false
};

// Handle Tab Switching
window.switchTab = function(tabId) {
    // Update active class on nav
    document.querySelectorAll('.nav-item').forEach(el => el.classList.remove('active'));
    event.currentTarget.classList.add('active');

    // Show correct section
    document.querySelectorAll('.tab-section').forEach(el => el.classList.remove('active'));
    document.getElementById(`tab-${tabId}`).classList.add('active');

    // Trigger data fetch
    if (tabId === 'dashboard') loadStats();
    if (tabId === 'products') loadProducts();
    if (tabId === 'orders') loadOrders();
    if (tabId === 'users') loadUsers();
}

function formatPrice(priceNum) {
    return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(priceNum || 0);
}

/* ==================
   STATISTICS MODULE
================== */
async function loadStats() {
    try {
        const prodRes = await fetchAdminAPI('/products');
        const orderRes = await fetchAdminAPI('/admin/orders');
        const userRes = await fetchAdminAPI('/admin/users');
        
        const products = prodRes.data || prodRes || [];
        const orders = orderRes.data || orderRes || [];
        const users = userRes.data || userRes || [];
        
        const totalRevenue = orders.reduce((sum, o) => o.status === 'COMPLETED' ? sum + o.totalPrice : sum, 0);
        
        document.getElementById('stat-products').innerText = products.length;
        document.getElementById('stat-orders').innerText = orders.length;
        document.getElementById('stat-users').innerText = users.length;
        document.getElementById('stat-revenue').innerText = formatPrice(totalRevenue);
        
        initChart(orders);
    } catch (err) {
        console.error("Stats Error:", err);
    }
}

function initChart(orders) {
    const ctx = document.getElementById('revenueChart').getContext('2d');
    if (window.myChart) window.myChart.destroy();
    
    // Simple mock logic for last 7 days revenue
    const labels = [];
    const data = [];
    for (let i = 6; i >= 0; i--) {
        const d = new Date();
        d.setDate(d.getDate() - i);
        labels.push(d.toLocaleDateString('vi-VN', { day: 'numeric', month: 'short' }));
        data.push(Math.floor(Math.random() * 1000000) + 500000); // Random mock data
    }

    window.myChart = new Chart(ctx, {
        type: 'line',
        data: {
            labels: labels,
            datasets: [{
                label: 'Doanh thu (VND)',
                data: data,
                borderColor: '#3699ff',
                backgroundColor: 'rgba(54, 153, 255, 0.1)',
                fill: true,
                tension: 0.4
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: { legend: { display: false } },
            scales: { y: { beginAtZero: true } }
        }
    });
}

/* ==================
   PRODUCTS MODULE
================== */
async function loadProducts() {
    const tbody = document.getElementById('tb-products');
    try {
        const res = await fetchAdminAPI('/products');
        const products = res.data || res || [];
        
        let html = '';
        products.forEach(p => {
            const mainImg = p.images && p.images.length > 0 ? p.images[0].imageUrl : 'placeholder.png';
            const displayImg = mainImg.startsWith('http') ? mainImg : `../images/${mainImg}`;
            
            // Generate stock display string for multiple variants
            let stockHtml = 'Trống';
            if (p.productVariants && p.productVariants.length > 0) {
                stockHtml = p.productVariants.map(v => 
                    `<div style="font-size:0.85rem; line-height:1.2;"><b>${v.size ? v.size.name : '?'}:</b> ${v.stockQuantity}</div>`
                ).join('');
            }

            html += `
            <tr>
                <td>#${p.id}</td>
                <td><img src="${displayImg}" style="width:50px; height:50px; object-fit:cover; border-radius:4px;"></td>
                <td><span class="item-name">${p.name}</span></td>
                <td style="font-weight: 600; color: var(--success);">${formatPrice(p.price)}</td>
                <td><div class="stock-list">${stockHtml}</div></td>
                <td>
                    <button class="act-btn" onclick="openProductModal(${p.id})">Sửa</button>
                    <button class="act-btn delete" onclick="deleteProduct(${p.id})">Xóa</button>
                </td>
            </tr>`;
        });
        tbody.innerHTML = html || '<tr><td colspan="6" style="text-align:center;">Trống</td></tr>';
    } catch (err) {
        tbody.innerHTML = `<tr><td colspan="6" style="color:red; text-align:center;">Lỗi: ${err.message}</td></tr>`;
    }
}

window.openProductModal = async function(id = null) {
    const modal = document.getElementById('modal-product');
    const form = document.getElementById('form-product');
    const title = document.getElementById('modal-product-title');
    const idInput = document.getElementById('prod-id');
    
    form.reset();
    title.innerText = id ? 'Sửa Sản Phẩm' : 'Thêm Sản Phẩm Mới';
    idInput.readOnly = id ? true : false;
    
    // Reset all size checkboxes and stocks
    document.querySelectorAll('.size-checkbox').forEach(cb => cb.checked = false);
    document.querySelectorAll('.size-stock-input').forEach(si => si.value = 0);
    
    if (id) {
        try {
            const res = await fetchAdminAPI(`/products/${id}`);
            const p = res.data || res;
            idInput.value = p.id;
            document.getElementById('prod-name').value = p.name;
            document.getElementById('prod-price').value = p.price;
            document.getElementById('prod-desc').value = p.description || '';
            document.getElementById('prod-image').value = (p.images && p.images.length > 0) ? p.images[0].imageUrl : '';
            
            if (p.productVariants && p.productVariants.length > 0) {
                p.productVariants.forEach(v => {
                    if (v.size) {
                        const cb = document.querySelector(`.size-checkbox[value="${v.size.name}"]`);
                        if (cb) {
                            cb.checked = true;
                            const row = cb.closest('.size-row');
                            const stockInp = row.querySelector('.size-stock-input');
                            if (stockInp) stockInp.value = v.stockQuantity;
                        }
                    }
                });
            }
        } catch (err) { alert("Lỗi tải chi tiết sản phẩm"); }
    } else {
        idInput.value = '';
    }
    
    modal.classList.add('active');
}

window.closeProductModal = function() {
    document.getElementById('modal-product').classList.remove('active');
}

document.getElementById('form-product').onsubmit = async (e) => {
    e.preventDefault();
    const id = document.getElementById('prod-id').value;
    const imageUrl = document.getElementById('prod-image').value;
    
    // Collect all selected variants
    const productVariants = [];
    document.querySelectorAll('.size-row').forEach(row => {
        const checkbox = row.querySelector('.size-checkbox');
        const stockInput = row.querySelector('.size-stock-input');
        if (checkbox && checkbox.checked) {
            productVariants.push({
                stockQuantity: parseInt(stockInput.value) || 0,
                size: { name: checkbox.value }
            });
        }
    });

    if (productVariants.length === 0) {
        alert("Vui lòng chọn ít nhất một kích cỡ!");
        return;
    }

    const isEdit = document.getElementById('modal-product-title').innerText.includes('Sửa');

    const data = {
        id: parseInt(id),
        name: document.getElementById('prod-name').value,
        price: parseFloat(document.getElementById('prod-price').value),
        description: document.getElementById('prod-desc').value,
        images: imageUrl ? [{ imageUrl: imageUrl, isMain: true }] : [],
        productVariants: productVariants
    };
    
    try {
        const method = isEdit ? 'PUT' : 'POST';
        const url = isEdit ? `/admin/products/${id}` : '/admin/products';
        await fetchAdminAPI(url, method, data);
        alert("Lưu sản phẩm thành công!");
        closeProductModal();
        loadProducts();
    } catch (err) { alert("Lỗi khi lưu sản phẩm: " + err.message); }
}

async function deleteProduct(id) {
    if (!confirm("Bạn có chắc chắn muốn xóa sản phẩm này không?")) return;
    try {
        const res = await fetchAdminAPI(`/admin/products/${id}`, 'DELETE');
        alert("Xóa thành công!");
        loadProducts();
    } catch (err) { 
        alert("Lỗi: " + (err.message || "Không thể thực hiện yêu cầu xóa.")); 
    }
}

/* ==================
   ORDERS MODULE
================== */
async function loadOrders() {
    const tbody = document.getElementById('tb-orders');
    try {
        const res = await fetchAdminAPI('/admin/orders');
        const orders = res.data || res || [];
        let html = '';
        orders.forEach(o => {
            html += `
            <tr>
                <td>#${o.id}</td>
                <td>${o.email || (o.user ? o.user.email : 'N/A')}</td>
                <td style="font-weight:600;">${formatPrice(o.totalPrice)}</td>
                <td><span class="badge badge-warning">${o.status}</span></td>
                <td>
                    <select onchange="updateOrderStatus(${o.id}, this.value)" style="padding: 2px; border-radius:4px;">
                        <option value="">Đổi trạng thái</option>
                        <option value="CONFIRMED">Xác nhận</option>
                        <option value="SHIPPING">Giao hàng</option>
                        <option value="COMPLETED">Hoàn thành</option>
                        <option value="CANCELLED">Hủy</option>
                    </select>
                </td>
            </tr>`;
        });
        tbody.innerHTML = html || '<tr><td colspan="5" style="text-align:center;">Trống</td></tr>';
    } catch (err) { tbody.innerHTML = `<tr><td colspan="5" style="color:red; text-align:center;">Lỗi tải ĐH</td></tr>`; }
}

window.updateOrderStatus = async function(id, newStatus) {
    if (!newStatus) return;
    try {
        await fetchAdminAPI(`/admin/orders/${id}/status?status=${newStatus}`, 'PATCH');
        loadOrders();
    } catch (err) { alert("Lỗi cập nhật trạng thái"); }
}

/* ==================
   USERS MODULE
================== */
async function loadUsers() {
    const tbody = document.getElementById('tb-users');
    try {
        const res = await fetchAdminAPI('/admin/users');
        const users = res.data || res || [];
        let html = '';
        users.forEach(u => {
            const isBanned = u.status === 'LOCKED';
            html += `
            <tr>
                <td>#${u.id}</td>
                <td>${u.username}</td>
                <td>${u.email}</td>
                <td>${u.phone || 'N/A'}</td>
                <td>${u.role}</td>
                <td><span class="badge ${isBanned?'badge-danger':'badge-success'}">${u.status}</span></td>
                <td>
                    ${isBanned 
                        ? `<button class="act-btn" onclick="unbanUser(${u.id})">Mở khóa</button>`
                        : `<button class="act-btn delete" onclick="banUser(${u.id})">Khóa</button>`
                    }
                </td>
            </tr>`;
        });
        tbody.innerHTML = html || '<tr><td colspan="7" style="text-align:center;">Trống</td></tr>';
    } catch (err) { tbody.innerHTML = `<tr><td colspan="7" style="color:red; text-align:center;">Lỗi tải User</td></tr>`; }
}
async function sendMail() {
    if (!confirm("Bạn có chắc muốn gửi email cho TẤT CẢ người dùng?")) return;

    try {
        await sendMailAPI();
        alert("✅ Gửi email thành công!");
    } catch (err) {
        console.error(err);
        alert("❌ Gửi email thất bại!");
    }
}
window.banUser = async function(id) {
    if(!confirm("Khóa tài khoản này?")) return;
    try {
        await fetchAdminAPI(`/admin/users/${id}/ban`, 'PATCH');
        loadUsers();
    } catch (err) { alert("Lỗi khóa"); }
}

window.unbanUser = async function(id) {
    try {
        await fetchAdminAPI(`/admin/users/${id}/unban`, 'PATCH');
        loadUsers();
    } catch (err) { alert("Lỗi mở khóa"); }
}

// Initial Load
document.addEventListener('DOMContentLoaded', () => {
    loadStats();
});
