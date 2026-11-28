document.addEventListener("DOMContentLoaded",()=>
{
 console.log("Doughnut Drop Admin Dashboard loaded!");

 const navLinks= document.querySelectorAll(".sidebar a");

 navLinks.forEach(link => {
     link.addEventListener("click",()=> {
         navLinks.forEach(item => item.classList.remove("active"));
         link.classList.add("active");
     });
 });
});

const addProductBtn = document.getElementById("addProductBtn");
const productModal = document.getElementById("productModal");
const cancelBtn = document.getElementById("cancelBtn");
const productForm = document.getElementById("productForm");
const modalTitle = document.getElementById("modalTitle");
const productTable = document.getElementById("adminProductTable");
const orderTable = document.getElementById("adminOrderTable");
const flavorName = document.getElementById("flavorName");
const addFlavorBtn = document.getElementById("addFlavorBtn");
const flavorTable = document.getElementById("adminFlavorTable");
const productStock = document.getElementById("productStock");
const productSearch = document.getElementById("productSearch");
const categoryFilter = document.getElementById("categoryFilter");

function showEmptyMessage() {
    if (productTable && productTable.querySelectorAll("tr").length === 0) {
        productTable.innerHTML = `
            <tr>
                <td colspan="8">No products available.</td>
            </tr>
        `;
    }
}

if (addProductBtn && productModal){
    addProductBtn.addEventListener("click",() => {
        editingRow = null;
        productForm.reset();
        imagePreview.src ="";
        imagePreview.style.display = "none";
        modalTitle.textContent= "Add Product";
        document.getElementById("productId").value = "";
        productModal.style.display = "flex";
    });
}

if (cancelBtn && productModal) {
    cancelBtn.addEventListener("click", () => {
        editingRow = null;
        productForm.reset();
        imagePreview.src ="";
        imagePreview.style.display = "none";
        productModal.style.display = "none";
    });
}

const productImage=document.getElementById("productImage");
const imagePreview=document.getElementById("imagePreview");

if(productImage && imagePreview) {
    productImage.addEventListener("change", function() {
        const file= productImage.files[0];

        if (file) {
            imagePreview.src = URL.createObjectURL(file);
            imagePreview.style.display = "block";
        } else {
            imagePreview.src= "";
            imagePreview.style.display="none";
        }
    });
}
let editingRow = null;

if (productForm) {
    productForm.addEventListener("submit", function(event){
        event.preventDefault();

        const name = document.getElementById("productName").value;
        const category = document.getElementById("productCategory").value;
        const price = document.getElementById("productPrice").value;
        const stock = productStock.value;
        const row = document.createElement("tr");
        const productId = editingRow ?editingRow.cells[0].textContent: "P" + (productTable.rows.length +1);
        const status = Number(stock) > 0? "In Stock" : "Out of Stock";

        row.innerHTML = `
    <td>${productId}</td>
    <td>
    ${imagePreview.src
            ? `<img src="${imagePreview.src}" alt="Product Image"
             style="width:50px; height:50px; object-fit:cover; border-radius:8px;">`
            : "No image"}
    </td>
    <td></td>
    <td>&#8369;${Number(price).toFixed(2)}</td>
    <td>${Number(stock)}</td>
    <td>${status}</td>
    <td>
    <img src="${imagePreview.src}" alt="Product Image"
    style="width:50px; height:50px; object-fit:cover; border-radius:8px;">
</td>
    <td>
        <button type="button" class="edit-product">Edit</button>
        <button type="button" class="delete-product">Delete</button>
    </td>
    `;

        row.cells[2].textContent = name;
        row.cells[3].textContent = category;

        if (productTable.rows.length === 1&& productTable.rows[0].cells.length === 1) {
            productTable.innerHTML ="";
        }

        if (editingRow) {
            editingRow.innerHTML = row.innerHTML;
            editingRow.cells[0].textContent = productId;
            editingRow.cells[2].textContent = name;
            editingRow.cells[3].textContent = category;
            editingRow.cells[4].textContent = "₱" + Number(price).toFixed(2);
            editingRow.cells[5].textContent = String(Number(stock));
            editingRow.cells[6].textContent = status;
        } else {
            productTable.appendChild(row);
        }

        productModal.style.display= "none";
        productForm.reset();
        editingRow = null;
        imagePreview.src = "";
        imagePreview.style.display = "none";
    });
}


if (productTable) {
    productTable.addEventListener("click", function (event) {
        if (event.target.classList.contains("edit-product")) {
            editingRow = event.target.closest("tr");
            document.getElementById("productId").value = editingRow.cells[0].textContent;
            document.getElementById("productName").value = editingRow.cells[2].textContent;
            document.getElementById("productCategory").value = editingRow.cells[3].textContent;
            document.getElementById("productPrice").value = parseFloat(editingRow.cells[4].textContent.replace(/[^\d.]/g,""));
            document.getElementById("productStock").value = editingRow.cells[5].textContent;

            const existingImage = editingRow.cells[1].querySelector("img");

            if (existingImage) {
                imagePreview.src= existingImage.src;
                imagePreview.style.display ="block";
            } else {
                imagePreview.src="";
                imagePreview.style.display="none";
            }

            modalTitle.textContent = "Edit Product";
            productModal.style.display = "flex";
            }
        if (event.target.classList.contains("delete-product")){
            const row = event.target.closest("tr") ;
            if (confirm("Are you sure you want to delete this product?")) {
                row.remove();
                showEmptyMessage();
            }
        }
        });
    }

if (productSearch && productTable) {
    productSearch.addEventListener("input", function () {
        const searchValue = this.value.toLowerCase();

        const rows = productTable.querySelectorAll("tr");

        rows.forEach(row => {
            const productName =
                (row.cells[2]?.textContent || "").toLowerCase();

            const category =
                (row.cells[3]?.textContent || "").toLowerCase();

            if (productName.includes(searchValue) ||
                category.includes(searchValue)) {
                row.style.display = "";
            } else {
                row.style.display = "none";
            }
        });
    });
}

if (productSearch && productTable) {
    productSearch.addEventListener("input", function () {
        const searchValue = this.value.toLowerCase();

        const rows = productTable.querySelectorAll("tr");

        rows.forEach(row => {
            const productName =
                (row.cells[2]?.textContent || "").toLowerCase();

            const category =
                (row.cells[3]?.textContent || "").toLowerCase();

            if (productName.includes(searchValue) ||
                category.includes(searchValue)) {
                row.style.display = "";
            } else {
                row.style.display = "none";
            }
        });
    });
}
if(categoryFilter && productTable) {
        categoryFilter.addEventListener("change", function() {
            const selectedCategory = categoryFilter.value.toLowerCase();
            const rows = productTable.querySelectorAll("tr");

            rows.forEach(row=> {
                const category = (row.cells[3]?.textContent ||"").toLowerCase();

                if (selectedCategory ===""|| category === selectedCategory){
                    row.style.display="";
                } else {
                    row.style.display = "none";
                }
            });
        });

        if (addFlavorBtn && flavorTable) {
            addFlavorBtn.addEventListener("click", function(){
                const name = flavorName.value.trim();

                if(name ==="") {
                    alert("Please enter your flavor.");
                    return;
                }
                const row = document.createElement("tr");
                const flavorId = "F" +(flavorTable.rows.length +1);

                row.innerHTML=`
                <td>${flavorId}</td>
                <td>${name}</td>
                <td>
                <button type="button" class="delete-flavor">Delete</button>
                </td>

                `;
                flavorTable.appendChild(row);

                flavorName.value="";
            });
        }

        if (flavorTable) {
            flavorTable.addEventListener("click", function(event) {
                if (event.target.classList.contains("delete-flavor")) {
                    const row = event.target.closest("tr");

                    if (confirm("Are you sure you want to remove this flavor?")) {
                        row.remove();
                    }
                }
            });
        }
}


