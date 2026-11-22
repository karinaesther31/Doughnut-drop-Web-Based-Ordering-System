const DD_API = {
    formatPhp(amount) {
        const num = Number(amount || 0);
        return "₱" + num.toLocaleString("en-PH", { minimumFractionDigits: 2, maximumFractionDigits: 2 });
    },

    getCart() {
        try { return JSON.parse(localStorage.getItem("dd_cart") || "[]"); } catch { return []; }
    },

    setCart(items) {
        localStorage.setItem("dd_cart", JSON.stringify(items));
    },

    addToCart(item, qty = 1) {
        if ((item.stockQuantity ?? 0) <= 0) return;
        const cart = this.getCart();
        const existing = cart.find((c) => c.doughnutItemId === item.id);
        if (existing) {
            existing.quantity = Math.min(item.stockQuantity, existing.quantity + qty);
            existing.price = item.price;
            existing.imageUrl = item.imageUrl || "";
        } else {
            cart.push({
                doughnutItemId: item.id,
                name: item.name,
                price: item.price,
                quantity: Math.min(item.stockQuantity, qty),
                imageUrl: item.imageUrl || ""
            });
        }
        this.setCart(cart);
    },

    getSession() {
        try { return JSON.parse(localStorage.getItem("dd_session") || "null"); } catch { return null; }
    },

    setSession(session) {
        if (!session) localStorage.removeItem("dd_session");
        else localStorage.setItem("dd_session", JSON.stringify(session));
    },

    authHeaders() {
        const s = this.getSession();
        const headers = { "Content-Type": "application/json" };
        if (s && s.authHeader) headers["Authorization"] = s.authHeader;
        return headers;
    },

    stockBadgeHtml(item) {
        const qty = Number(item.stockQuantity ?? 0);
        if (qty <= 0) return `<span class="badge badge-out">Sold Out</span>`;
        if (qty <= 5) return `<span class="badge badge-warn">Low Stock – Only ${qty} left!</span>`;
        return `<span class="badge badge-ok">In Stock (${qty})</span>`;
    }
};