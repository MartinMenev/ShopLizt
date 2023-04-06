let boughtProductSection = document.getElementById("boughtProductsTable")
fetch(`/api/bought-products`)
    .then((response) => response.json())
    .then((body) => {
        let productHtml = '<table>'
            productHtml += '<tbody>'

        for(let product of body) {
            productHtml += '<tr>'
            productHtml += '<td>'
            productHtml += '<a title="Return item to my Shopping list" class="bi bi-arrow-clockwise" href="/reuse-product/ ' + product.id + '"></a> '
            productHtml += ' <span>' + product.name + '</span> '
            productHtml += '</td>'
            productHtml += '<td>'
            productHtml += '<span>' + product.boughtOn + '</span> '
            productHtml += ' <a title="Delete item" href="/deleteProduct/ ' + product.id + '" class="btn btn-light bg-body-secondary"><i class="fa-solid fa-xmark bg-body-secondary"></i></a>'
            productHtml += '</td>'
            productHtml += '</tr>'
        }
            productHtml += '</tbody>'

            boughtProductSection.innerHTML += productHtml;
    })




