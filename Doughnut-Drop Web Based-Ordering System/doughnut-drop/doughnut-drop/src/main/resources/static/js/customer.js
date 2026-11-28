document.addEventListener("DOMContentLoaded", function () {

    const flavorInputs = [
        document.getElementById("chocolate"),
        document.getElementById("whiteChocolate"),
        document.getElementById("ube"),
        document.getElementById("strawberry"),
        document.getElementById("matcha")
    ];

    const totalPieces = document.getElementById("totalPieces");
    const selectionMessage = document.getElementById("selectionMessage");
    const addToCartBtn = document.getElementById("addToCartBtn");

    function updateTotal() {
        const total = flavorInputs.reduce((sum, input) => {
            return sum + Number(input.value);
        }, 0);

        totalPieces.textContent = total;

        if (total === 21) {
            selectionMessage.textContent = "Your 21-piece box is complete!";
            addToCartBtn.disabled = false;
        } else if (total < 21) {
            selectionMessage.textContent =
                `Choose ${21 - total} more doughnuts.`;
            addToCartBtn.disabled = true;
        } else {
            selectionMessage.textContent =
                "Your selection exceeds 21 pieces.";
            addToCartBtn.disabled = true;
        }
    }

    flavorInputs.forEach(input => {
        input.addEventListener("input", function () {
            let value = Number(this.value);

            if (value < 0 || this.value === "") {
                value = 0;
            }

            const otherTotal = flavorInputs.reduce((sum, item) => {
                return sum + (item === this ? 0 : Number(item.value));
            }, 0);

            if (otherTotal + value > 21) {
                value = 21 - otherTotal;
            }

            this.value = value;
            updateTotal();
        });
    });

    updateTotal();

});