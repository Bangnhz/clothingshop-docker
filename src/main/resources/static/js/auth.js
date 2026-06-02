document.addEventListener('DOMContentLoaded', () => {
    const loginForm = document.getElementById('loginForm');
    if (loginForm) {
        loginForm.addEventListener('submit', handleLogin);
    }

    const registerForm = document.getElementById('registerForm');
    if (registerForm) {
        registerForm.addEventListener('submit', handleRegister);
    }
});

async function handleLogin(e) {
    e.preventDefault();
    const btn = document.getElementById('loginBtn');
    btn.innerText = 'Đang xử lý...';
    btn.disabled = true;

    const formData = new FormData(e.target);
    const data = Object.fromEntries(formData.entries());

    try {
        const response = await fetchAPI('/auth/login', {
            method: 'POST',
            body: JSON.stringify(data)
        });

        // The backend returns a ResponseData object
        // Example: { success: "JWT_TOKEN" or some boolean }
        // For standard Spring Boot, it might just return true/false in success or a token
        if (response.success || response.data) {
            showToast('Đăng nhập thành công!', 'success');
            
            // Backend uses HTTP Basic Authentication
            // We encode username:password into base64 and store it
            const basicAuthToken = btoa(data.username + ':' + data.password);
            localStorage.setItem('jwtToken', basicAuthToken);
            
            // Store user config
            localStorage.setItem('user', JSON.stringify({ username: data.username }));
            
            setTimeout(() => {
                if (data.username === 'admin') {
                    window.location.href = 'admin/index.html';
                } else {
                    window.location.href = 'index.html';
                }
            }, 1000);
        } else {
            showToast('Đăng nhập thất bại. Kiểm tra lại thông tin.', 'error');
            btn.disabled = false;
            btn.innerText = 'Đăng nhập';
        }

    } catch (error) {
        console.error(error);
        showToast('Lỗi kết nối tới máy chủ.', 'error');
        btn.disabled = false;
        btn.innerText = 'Đăng nhập';
    }
}

async function handleRegister(e) {
    e.preventDefault();
    const btn = document.getElementById('registerBtn');
    btn.innerText = 'Đang xử lý...';
    btn.disabled = true;

    const formData = new FormData(e.target);
    const data = Object.fromEntries(formData.entries());

    // Bypass confirm password check since it's hidden now
    delete data.confirmPassword;

    try {
        const response = await fetchAPI('/auth/register', {
            method: 'POST',
            body: JSON.stringify(data)
        });

        if (response.success || response.data) {
            showToast('Đăng ký thành công! Vui lòng đăng nhập.', 'success');
            setTimeout(() => {
                window.location.href = 'login.html';
            }, 1500);
        } else {
            showToast('Đăng ký thất bại.', 'error');
            btn.disabled = false;
            btn.innerText = 'Đăng ký';
        }

    } catch (error) {
        console.error(error);
        showToast('Tên đăng nhập hoặc email đã tồn tại.', 'error');
        btn.disabled = false;
        btn.innerText = 'Đăng ký';
    }
}
