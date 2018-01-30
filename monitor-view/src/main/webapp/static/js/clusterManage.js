$(function () {
    $("#addClusterSubmit").click(function () {
        $("#addClusterForm").submit();
    });


    $("#updateClusterSubmit").click(function () {
        $("#updateClusterForm").submit();
    });
});

function deleteCluster(productId, applicationId, id) {
    var href = "/monitor/manage/clusters/delete/" + productId + "/" + applicationId + "/" + id;
    $("#deleteClusterHref").prop("href", href);
}

function updateCluster(id) {
    $.get("/monitor/manage/clusters/update/" + id, function (cluster) {
        $("#updateClusterId").val(cluster.id)
        $("#updateClusterName").val(cluster.name)
        $("#updateClusterDesc").val(cluster.description)
        $("#alarmGroup").val(cluster.alarmGroup)

        $("#updateClusterModal").modal();
    })
}