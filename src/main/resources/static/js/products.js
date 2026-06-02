/**
 * Handles fetching and rendering of products 
 */

async function loadProducts(containerId, limit = null) {
    const container = document.getElementById(containerId);
    if (!container) return;

    try {
        const response = await fetchAPI('/products');
        // Structure expected: { data: [...] }
        const products = response.data || [];
        
        let displayProducts = products;
        if (limit) {
            displayProducts = products.slice(0, limit);
        }

        renderProducts(displayProducts, container);

    } catch (error) {
        console.error("Lỗi tải sản phẩm:", error);
        container.innerHTML = `<div style="text-align:center; color: red;">Xin lỗi, không thể tải danh sách sản phẩm.</div>`;
    }
}

function renderProducts(products, container) {
    container.innerHTML = ''; // Clear loading state
    
    if (products.length === 0) {
        container.innerHTML = '<div style="text-align:center; color: var(--text-muted); width: 100%; grid-column: 1 / -1; padding: 3rem;">Không tìm thấy sản phẩm nào phù hợp.</div>';
        return;
    }

    products.forEach(product => {
        // Extract main image from DB relationship
        let mainImg = "https://via.placeholder.com/400x500?text=No+Image";
        if (product.images && product.images.length > 0) {
            const foundMain = product.images.find(img => img.isMain);
            let imgUrl = foundMain ? foundMain.imageUrl : product.images[0].imageUrl;
            mainImg = window.getImageUrl(imgUrl);
        }

        const catName = product.category ? product.category.name : 'Thời trang';
        const formattedPrice = formatPrice(product.price);

        const card = document.createElement('div');
        card.className = 'product-card';
        card.innerHTML = `
            <div class="product-img-wrapper">
                <img src="${mainImg}" alt="${product.name}" class="product-img" onerror="this.src='https://via.placeholder.com/400x500?text=Error'">
                <button class="quick-view-btn" onclick="showQuickView(${product.id})">🔍 Xem nhanh</button>
            </div>
            <div class="product-category">${catName}</div>
            <h3 class="product-name" title="${product.name}">${product.name}</h3>
            <div class="product-price">${formattedPrice}</div>
            <button class="btn btn-primary" style="width: 100%; margin-top: 1rem; border-radius:4px" onclick="showQuickView(${product.id})">Mua ngay</button>
        `;
        
        container.appendChild(card);
    });
}

async function addToCart(productId, variantId, qty = 1) {
    const qtyInt = parseInt(qty);
    const token = localStorage.getItem('jwtToken');
    
    if (!variantId) {
        showToast('Vui lòng chọn Size!', 'warning');
        return;
    }

    if (!token) {
        // Guest mode: Save to localStorage
        let guestCart = JSON.parse(localStorage.getItem('guestCart') || '[]');
        const existingItem = guestCart.find(item => item.variantId === variantId);
        
        if (existingItem) {
            existingItem.quantity += qtyInt;
        } else {
            // Need product info for guest cart display
            try {
                const res = await fetchAPI(`/products/${productId}`);
                const product = res.data || res;
                const variant = product.productVariants.find(v => v.id == variantId);
                
                if (!variant) {
                    showToast('Có lỗi: Không tìm thấy kích cỡ này.', 'error');
                    return;
                }

                guestCart.push({
                    productId,
                    variantId,
                    quantity: qtyInt,
                    // Store in a structure consistent with backend DTOs
                    productVariantResponse: {
                        ...variant,
                        productResponse: product
                    }
                });
            } catch (err) {
                console.error(err);
                showToast('Lỗi khi lấy thông tin sản phẩm', 'error');
                return;
            }
        }
        
        localStorage.setItem('guestCart', JSON.stringify(guestCart));
        showToast('Đã thêm vào giỏ hàng (Khách)!', 'success');
        updateCartCount();
        return;
    }

    try {
        await fetchAPI(`/user/cart/items?productId=${productId}&variantId=${variantId}&quantity=${qtyInt}`, {
            method: 'POST'
        });
        showToast(`Đã thêm vào giỏ hàng!`, 'success');
        updateCartCount(); 
    } catch (error) {
        console.error(error);
        showToast('Lỗi khi thêm vào giỏ hàng', 'error');
    }
}

// Quick View Modal JS
window.showQuickView = async function(productId) {
    try {
        const res = await fetchAPI(`/products/${productId}`);
        const product = res.data;
        if (!product) return;

        let modal = document.getElementById('quick-view-modal');
        if (!modal) {
            modal = document.createElement('div');
            modal.id = 'quick-view-modal';
            modal.className = 'modal-overlay';
            document.body.appendChild(modal);
        }

        const images = product.images || [];
        const mainImg = images.length > 0 ? window.getImageUrl(images[0].imageUrl) : "https://via.placeholder.com/400x500";
        
        const thumbHtml = images.map((img, i) => `
            <img src="${window.getImageUrl(img.imageUrl)}" 
                 class="modal-thumb ${i===0?'active':''}" 
                 onclick="changeModalMainImg(this)">
        `).join('');

        const variants = product.productVariants || [];
        const sizeHtml = variants.map((v, i) => `
            <div class="size-item ${i===0?'active':''}" data-variant-id="${v.id}" data-stock="${v.stockQuantity || 0}" onclick="selectVariant(this)">
                ${v.size ? v.size.name : 'FreeSize'}
            </div>
        `).join('');

        modal.innerHTML = `
            <div class="modal-content">
                <span class="modal-close" onclick="closeModal()">&times;</span>
                <div class="modal-left">
                    <img id="modal-main-img" src="${mainImg}" class="modal-img-main" onerror="this.src='https://via.placeholder.com/400x500'">
                    <div class="modal-thumbnails">${thumbHtml}</div>
                </div>
                <div class="modal-right">
                    <div class="product-category" style="margin-bottom: 0.5rem; color: var(--accent); font-weight: 700;">${product.category ? product.category.name : 'LUXE FW KIDS'}</div>
                    <h2 style="font-size: 2.2rem; margin-bottom: 1rem; text-transform: none; letter-spacing: 0;">${product.name}</h2>
                    <div class="product-price" style="font-size: 1.8rem; margin-bottom: 2rem; color: var(--accent);">${formatPrice(product.price)}</div>
                    
                    <p style="color: var(--text-muted); margin-bottom: 2rem; line-height: 1.8; font-size: 1.05rem;">
                        ${product.description || 'Dòng sản phẩm thời trang cao cấp với chất liệu tự nhiên, an toàn tuyệt đối cho làn da bé, mang lại vẻ ngoài sành điệu và tự tin.'}
                    </p>

                    <div style="font-weight: 700; margin-bottom: 1rem; border-top: 1px solid var(--border); padding-top: 1.5rem;">CHỌN KÍCH CỠ:</div>
                    <div class="size-grid">${sizeHtml}</div>

                    <div id="stock-display" style="color: var(--text-muted); margin-bottom: 1rem; font-size: 0.95rem;"></div>

                    <div style="font-weight: 700; margin-bottom: 1rem;">SỐ LƯỢNG:</div>
                    <div class="qty-box">
                        <input type="number" id="quick-qty" class="qty-input" value="1" min="1" oninput="validateQty(this)">
                    </div>

                    <button class="btn btn-primary" style="width: 100%; padding: 1.5rem; font-size: 1.2rem; border-radius: var(--radius-sm);" 
                            onclick="addToCartFromModal(${product.id})">
                        THÊM VÀO GIỎ HÀNG
                    </button>
                </div>
            </div>
        `;

        modal.classList.add('active');
        document.body.style.overflow = 'hidden'; 

        const activeSize = document.querySelector('.size-item.active');
        if (activeSize) {
            selectVariant(activeSize);
        }

    } catch (err) {
        console.error(err);
        showToast("Không thể tải chi tiết sản phẩm.", "error");
    }
}

window.closeModal = function() {
    const modal = document.getElementById('quick-view-modal');
    if (modal) modal.classList.remove('active');
    document.body.style.overflow = 'auto';
}

window.changeModalMainImg = function(thumb) {
    const main = document.getElementById('modal-main-img');
    if (main) main.src = thumb.src;
    document.querySelectorAll('.modal-thumb').forEach(t => t.classList.remove('active'));
    thumb.classList.add('active');
}

window.selectVariant = function(el) {
    document.querySelectorAll('.size-item').forEach(i => i.classList.remove('active'));
    el.classList.add('active');
    
    const stock = parseInt(el.getAttribute('data-stock') || 0);
    const stockDisplay = document.getElementById('stock-display');
    const qtyInput = document.getElementById('quick-qty');
    
    if (stockDisplay) {
        stockDisplay.innerText = stock > 0 ? `Còn lại: ${stock} sản phẩm` : 'Hết hàng';
    }
    
    if (qtyInput) {
        qtyInput.max = stock;
        if (parseInt(qtyInput.value) > stock) {
            qtyInput.value = stock;
        }
    }
}

window.validateQty = function(input) {
    const stock = parseInt(input.max || 0);
    let val = parseInt(input.value || 0);
    if (val > stock) {
        input.value = stock;
    } else if (val < 1) {
        input.value = 1;
    }
}

window.addToCartFromModal = function(productId) {
    const activeSize = document.querySelector('.size-item.active');
    const variantId = activeSize ? activeSize.getAttribute('data-variant-id') : null;
    const stock = activeSize ? parseInt(activeSize.getAttribute('data-stock') || 0) : 0;
    const qty = document.getElementById('quick-qty').value;
    
    if (parseInt(qty) > stock) {
        showToast(`Số lượng yêu cầu vượt quá số lượng có sẵn (${stock})`, 'warning');
        return;
    }
    
    addToCart(productId, variantId, qty);
    closeModal();
}

// Search & Filter
window.handleSearch = async function() {
    const input = document.getElementById('search-input');
    const name = input ? input.value.trim() : "";
    const grid = document.getElementById('products-grid') || document.getElementById('new-products');
    
    if (!grid) return;
    grid.innerHTML = '<div style="text-align: center; width: 100%; grid-column: 1 / -1; padding: 3rem;">Đang tìm kiếm...</div>';

    try {
        const res = await fetchAPI(`/products/search?name=${encodeURIComponent(name)}`);
        renderProducts(res.data || [], grid);
    } catch (err) {
        showToast("Lỗi tìm kiếm sản phẩm", "error");
    }
}

window.applyFilters = async function() {
    const minInput = document.getElementById('min-price');
    const maxInput = document.getElementById('max-price');
    const priceRadio = document.querySelector('input[name="price"]:checked');
    const grid = document.getElementById('products-grid');
    if (!grid) return;

    grid.innerHTML = '<div style="text-align: center; width: 100%; grid-column: 1 / -1; padding: 3rem;">Đang lọc sản phẩm...</div>';
    
    let minPrice = 0;
    let maxPrice = 999999999;

    let useRadio = true;

    if (minInput && minInput.value || maxInput && maxInput.value) {
        useRadio = false;
        minPrice = parseInt(minInput.value) || 0;
        maxPrice = parseInt(maxInput.value) || 999999999;
        
        // Uncheck radios
        document.querySelectorAll('input[name="price"]').forEach(r => r.checked = false);
    } else if (priceRadio) {
        if (priceRadio.value === 'under50') maxPrice = 50000;
        else if (priceRadio.value === '50to100') { minPrice = 50000; maxPrice = 100000; }
        else if (priceRadio.value === '100to200') { minPrice = 100000; maxPrice = 200000; }
        else if (priceRadio.value === 'over200') minPrice = 200000;
    }

    try {
        let res;
        if (useRadio && (!priceRadio || priceRadio.value === 'all')) {
            res = await fetchAPI('/products');
        } else {
            const resData = await fetchAPI(`/products/filter?minPrice=${minPrice}&maxPrice=${maxPrice}`);
            res = resData; 
        }
        renderProducts(res.data || res || [], grid);
        showToast("Đã áp dụng bộ lọc.", "success");
    } catch (err) {
        console.error(err);
        showToast("Lỗi khi lọc sản phẩm.", "error");
        grid.innerHTML = '<div style="text-align: center; width: 100%; grid-column: 1 / -1; padding: 3rem; color: red;">Lỗi khi lọc sản phẩm.</div>';
    }
}

window.resetFilters = function() {
    const priceRadioAll = document.querySelector('input[name="price"][value="all"]');
    if (priceRadioAll) priceRadioAll.checked = true;
    
    const minInput = document.getElementById('min-price');
    const maxInput = document.getElementById('max-price');
    if (minInput) minInput.value = '';
    if (maxInput) maxInput.value = '';
    
    const searchInput = document.getElementById('search-input');
    if (searchInput) searchInput.value = '';
    
    applyFilters();
}
