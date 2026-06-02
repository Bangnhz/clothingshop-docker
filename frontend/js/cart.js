document.addEventListener('DOMContentLoaded', () => {
    loadCart();
});

async function loadCart() {
    const container = document.getElementById('cart-content');
    const token = localStorage.getItem('jwtToken');
    
    if (!token) {
        // Guest mode
        const guestCart = JSON.parse(localStorage.getItem('guestCart') || '[]');
        if (guestCart.length === 0) {
            container.innerHTML = `
                <div style="text-align: center; padding: 5rem 0;">
                    <h2 style="font-size: 2.5rem; margin-bottom: 1rem;">Giỏ hàng trống</h2>
                    <p style="color: var(--text-muted); margin-bottom: 2rem; font-size: 1.2rem;">Hãy ghé thăm Cửa hàng để thêm các sản phẩm tuyệt vời nhé!</p>
                    <a href="products.html" class="btn btn-primary" style="padding: 1.2rem 3rem;">Khám phá Sản phẩm</a>
                </div>
            `;
            return;
        }
        // Wrapping guest data into a DTO-like object
        renderCart({ items: guestCart }, container);
        return;
    }

    try {
        const response = await fetchAPI('/user/cart');
        const cart = response.data;
        
        if (!cart || !cart.items || cart.items.length === 0) {
            container.innerHTML = `
                <div style="text-align: center; padding: 5rem 0;">
                    <h2 style="font-size: 2.5rem; margin-bottom: 1rem;">Giỏ hàng trống</h2>
                    <p style="color: var(--text-muted); margin-bottom: 2rem; font-size: 1.2rem;">Hãy ghé thăm Cửa hàng để thêm các sản phẩm tuyệt vời nhé!</p>
                    <a href="products.html" class="btn btn-primary" style="padding: 1.2rem 3rem;">Khám phá Sản phẩm</a>
                </div>
            `;
            return;
        }

        renderCart(cart, container);
    } catch (e) {
        console.error(e);
        container.innerHTML = `
            <div style="text-align: center; padding: 5rem 0; color: red;">
                Không thể tải thông tin giỏ hàng. <br> Vui lòng thử đăng nhập lại.
            </div>
        `;
    }
}

function renderCart(cart, container) {
    let itemsHtml = '';
    
    // Iterate over CartItemDTO
    cart.items.forEach(item => {
        // Extract from DTO hierarchy
        const prodVariant = item.productVariantResponse || {};
        const prodInfo = prodVariant.productResponse || {};
        
        const productName = prodInfo.name || ('Sản phẩm ' + (item.productId || ''));
        const price = prodInfo.price || 0;
        
        // Determination of ID for deletion/checking
        const itemId = item.id || item.variantId || (prodVariant ? prodVariant.id : null);
        
        let mainImg = "https://via.placeholder.com/150";
        if (prodInfo.images && prodInfo.images.length > 0) {
            let imgObj = prodInfo.images[0]; 
            mainImg = window.getImageUrl(imgObj.imageUrl);
        }
        
        const itemTotal = price * item.quantity;
        
        // Extract size name
        const sizeName = (prodVariant.size && prodVariant.size.name) || 'FreeSize';

        itemsHtml += `
            <tr>
                <td style="width: 50px;">
                    <input type="checkbox" class="cart-item-check" data-id="${itemId}" data-price="${itemTotal}" checked onchange="updateTotal()">
                </td>
                <td>
                    <div class="cart-item-info">
                        <img src="${mainImg}" alt="${productName}" class="cart-item-img">
                        <div>
                            <div class="cart-item-title">${productName}</div>
                            <div style="color: var(--accent); font-weight: 600; font-size: 0.9rem; margin-top: 0.2rem;">Kích cỡ: ${sizeName}</div>
                            <div style="color: var(--text-muted); font-size: 0.9rem;">Số lượng: ${item.quantity}</div>
                        </div>
                    </div>
                </td>
                <td style="font-weight: 600;">${formatPrice(price)}</td>
                <td style="font-weight: 600; color: var(--accent);">${formatPrice(itemTotal)}</td>
                <td>
                    <button class="btn btn-outline" style="padding: 0.4rem 0.8rem; color: #e74c3c; border-color: #e74c3c;" onclick="removeCartItem('${itemId}')">Xóa</button>
                </td>
            </tr>
        `;
    });

    container.innerHTML = `
        <table class="cart-table">
            <thead>
                <tr>
                    <th><input type="checkbox" id="check-all" checked onchange="toggleAllCheckboxes()"></th>
                    <th>Sản phẩm</th>
                    <th>Đơn giá</th>
                    <th>Tổng</th>
                    <th>Thao tác</th>
                </tr>
            </thead>
            <tbody>
                ${itemsHtml}
            </tbody>
        </table>
        
        <div style="display: flex; justify-content: space-between; align-items: flex-start; margin-top: 2rem;">
            <div>
                <button class="btn btn-outline" style="color: #e74c3c; border-color: #e74c3c;" onclick="deleteSelectedItems()">Xóa các mục đã chọn</button>
            </div>
            <div class="cart-summary">
                <h3 style="margin-bottom: 0.5rem;">CỘNG GIỎ HÀNG:</h3>
                <div class="total-price" id="cart-total-price">0 đ</div>
                <button class="btn btn-primary" style="width: 100%; padding: 1rem; font-size: 1.1rem; border-radius: var(--radius-sm); margin-top: 1rem;" onclick="proceedToCheckout()">Tiến hành đặt hàng</button>
            </div>
        </div>
    `;
    
    updateTotal();
}

window.proceedToCheckout = function() {
    window.location.href = 'checkout.html';
}

window.toggleAllCheckboxes = function() {
    const isChecked = document.getElementById('check-all').checked;
    const checkboxes = document.querySelectorAll('.cart-item-check');
    checkboxes.forEach(cb => {
        cb.checked = isChecked;
    });
    updateTotal();
}

window.updateTotal = function() {
    let sum = 0;
    const checkboxes = document.querySelectorAll('.cart-item-check');
    checkboxes.forEach(cb => {
        if (cb.checked) {
            sum += parseFloat(cb.getAttribute('data-price')) || 0;
        }
    });
    const totalEl = document.getElementById('cart-total-price');
    if (totalEl) {
        totalEl.innerText = formatPrice(sum);
    }
}

async function removeCartItem(itemId) {
    if(!confirm("Bạn có chắc muốn xóa sản phẩm này khỏi giỏ hàng?")) return;
    
    const token = localStorage.getItem('jwtToken');
    if (!token) {
        // Guest mode
        let guestCart = JSON.parse(localStorage.getItem('guestCart') || '[]');
        guestCart = guestCart.filter(item => item.variantId != itemId);
        localStorage.setItem('guestCart', JSON.stringify(guestCart));
        showToast('Đã xóa sản phẩm.', 'success');
        loadCart();
        updateCartCount();
        return;
    }

    try {
        await fetchAPI(`/user/cart/items/${itemId}`, { method: 'DELETE' });
        showToast('Đã xóa sản phẩm.', 'success');
        await loadCart();
        if (typeof updateCartCount === 'function') updateCartCount();
    } catch (error) {
        showToast('Lỗi khi xóa sản phẩm.', 'error');
    }
}

window.deleteSelectedItems = async function() {
    const checkboxes = document.querySelectorAll('.cart-item-check:checked');
    if (checkboxes.length === 0) {
        showToast('Vui lòng chọn sản phẩm để xóa.', 'warning');
        return;
    }

    if (!confirm(`Xóa ${checkboxes.length} sản phẩm đã chọn khỏi giỏ hàng?`)) return;

    const token = localStorage.getItem('jwtToken');
    if (!token) {
        // Guest mode
        let guestCart = JSON.parse(localStorage.getItem('guestCart') || '[]');
        const idsToRemove = Array.from(checkboxes).map(cb => cb.getAttribute('data-id'));
        guestCart = guestCart.filter(item => !idsToRemove.some(id => id == item.variantId));
        localStorage.setItem('guestCart', JSON.stringify(guestCart));
        showToast('Đã xóa các sản phẩm chọn.', 'success');
        loadCart();
        updateCartCount();
        return;
    }

    try {
        for (const cb of checkboxes) {
            const itemId = cb.getAttribute('data-id');
            await fetchAPI(`/user/cart/items/${itemId}`, { method: 'DELETE' });
        }
        showToast('Đã xóa các sản phẩm chọn.', 'success');
        await loadCart();
        if (typeof updateCartCount === 'function') updateCartCount();
    } catch (err) {
        console.error(err);
        showToast('Lỗi khi xóa nhiều sản phẩm.', 'error');
    }
}
