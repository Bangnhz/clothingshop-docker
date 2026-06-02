(() => {
    function createChatWidget() {
        if (document.getElementById("chat-widget-btn")) {
            return;
        }

        const btn = document.createElement("button");
        btn.id = "chat-widget-btn";
        btn.className = "chat-widget-btn";
        btn.type = "button";
        btn.title = "Chat cùng tư vấn viên";
        btn.textContent = "Chat";

        const box = document.createElement("section");
        box.id = "chat-widget-box";
        box.className = "chat-widget-box";
        box.innerHTML = `
            <div class="chat-header">
                <span>Tư vấn LUXE FW</span>
                <button type="button" class="chat-close-btn" aria-label="Đóng chat">&times;</button>
            </div>
            <div class="chat-body" id="chat-widget-body">
                <div class="chat-msg">Xin chào! Chúng tôi có thể giúp gì cho bạn?</div>
            </div>
            <form class="chat-footer" id="chat-widget-form">
                <input type="text" id="chat-widget-input" placeholder="Nhập câu hỏi..." autocomplete="off" />
                <button type="submit" aria-label="Gửi tin nhắn">➤</button>
            </form>
        `;

        document.body.appendChild(btn);
        document.body.appendChild(box);

        const closeBtn = box.querySelector(".chat-close-btn");
        const form = document.getElementById("chat-widget-form");
        const input = document.getElementById("chat-widget-input");
        const body = document.getElementById("chat-widget-body");

        const toggleChat = () => {
            box.classList.toggle("open");
            if (box.classList.contains("open")) {
                input.focus();
            }
        };

        const appendMessage = (content, isUser = false) => {
            const msgEl = document.createElement("div");
            msgEl.className = `chat-msg${isUser ? " user" : ""}`;
            msgEl.textContent = content;
            body.appendChild(msgEl);
            body.scrollTop = body.scrollHeight;
        };

        const sendChatRequest = async (message) => {
            if (typeof fetchAPI === "function") {
                return fetchAPI("/chat", {
                    method: "POST",
                    body: JSON.stringify({ message })
                });
            }
            throw new Error("chat_api_unavailable");
        };

        btn.addEventListener("click", toggleChat);
        closeBtn.addEventListener("click", toggleChat);

        form.addEventListener("submit", async (event) => {
            event.preventDefault();
            const message = input.value.trim();
            if (!message) return;

            appendMessage(message, true);
            input.value = "";

            try {
                const response = await sendChatRequest(message);
                const reply = response?.reply || response?.data?.reply || "Cảm ơn bạn! Chúng tôi đã ghi nhận.";
                appendMessage(reply);
            } catch (_error) {
                appendMessage("Xin lỗi, hệ thống đang bận. Vui lòng thử lại sau!");
            }
        });
    }

    document.addEventListener("DOMContentLoaded", createChatWidget);
})();
