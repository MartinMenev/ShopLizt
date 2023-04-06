let boughtProductSection = document.getElementById("boughtProductsTable")
fetch(`/api/bought-products`)
    .then((response) => response.json())
    .then((body) => {
        let productHtml = '<table class="table table-responsive-sm  rounded-3 bg-body-secondary" >'
            productHtml += '<tbody>'
            productHtml += '<tr>'
        for(let product of body) {
            productHtml = '<td>'
            // productHtml = '<a title="Return item to my Shopping list" class="bi bi-arrow-clockwise" href="#" th:href="@{/reuse-product/{id}(id=${product.id})}"></a>'
            productHtml += '<span>' + product.name + '</span>'
            productHtml += '</td>'
            productHtml += '<td>'
            productHtml += product.boughtOn
            // productHtml = '<a title="Delete item" th:href="@{/deleteProduct/{id}(id=${product.id})}" class="btn btn-light bg-body-secondary"><i class="fa-solid fa-xmark bg-body-secondary"></i></a>'
            productHtml += '</td>'
            productHtml += '</tr>'
            productHtml += '</tbody>'

            boughtProductSection.innerHTML += productHtml;
        }



    })




