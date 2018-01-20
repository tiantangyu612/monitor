$(function () {
    $("#addProductSubmit").click(function () {
        $("#addProductForm").submit();
    });


    $("#updateProductSubmit").click(function () {
        $("#updateProductForm").submit();
    });
});

function deleteProduct(id) {
    var href = "/monitor/manage/products/delete/" + id;
    $("#deleteProductHref").prop("href", href);
}

function updateProduct(id) {
    $.get("/monitor/manage/products/update/" + id, function (product) {
        $("#updateProductId").val(product.id)
        $("#updateProductName").val(product.name)
        $("#updateProductOwner").val(product.owner)
        $("#updateProductDesc").val(product.description)

        $("#updateProductModal").modal();
    })
}