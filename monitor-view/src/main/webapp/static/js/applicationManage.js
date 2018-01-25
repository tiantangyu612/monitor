$(function () {
    $("#addApplicationSubmit").click(function () {
        $("#addApplicationForm").submit();
    });


    $("#updateApplicationSubmit").click(function () {
        $("#updateApplicationForm").submit();
    });
});

function deleteApplication(productId, id) {
    var href = "/monitor/manage/applications/delete/" + productId + "/" + id;
    $("#deleteApplicationHref").prop("href", href);
}

function updateApplication(id) {
    $.get("/monitor/manage/applications/update/" + id, function (application) {
        $("#updateApplicationId").val(application.id)
        $("#updateApplicationName").val(application.name)
        $("#updateApplicationDesc").val(application.description)

        $("#updateApplicationModal").modal();
    })
}