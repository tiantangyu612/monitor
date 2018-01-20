$(function () {
    $("#addUserSubmit").click(function () {
        $("#addUserForm").submit();
    });


    $("#updateUserSubmit").click(function () {
        $("#updateUserForm").submit();
    });

    $("#resetUserPasswordHref").click(function () {
        $("#resetUserPasswordConfirm").modal('hide')
        var href = $(this).prop("href");

        $.get(href, function (flag) {
            if (flag) {
                $("#resetResult").text("重置成功");
            } else {
                $("#resetResult").text("重置失败");
            }
        })
        $("#resetUserPasswordResult").modal();

        return false;
    });
});

function deleteUser(id) {
    var href = "/monitor/manage/users/delete/" + id;
    $("#deleteUserHref").prop("href", href);
}

function updateUser(id) {
    $.get("/monitor/manage/users/update/" + id, function (user) {
        $("#updateUserId").val(user.id)
        $("#updateUserName").val(user.username)
        $("#updateName").val(user.name)
        $("#updateEmail").val(user.email)
        $("#updatePhone").val(user.phone)

        $("#updateUserModal").modal();
    })
}

function resetUserPassword(id) {
    var href = "/monitor/manage/users/resetPassword/" + id;
    $("#resetUserPasswordHref").prop("href", href);
}