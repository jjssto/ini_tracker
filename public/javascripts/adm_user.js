import {get_token} from "./functions.js";

$(".b_delete_user").click(function(){
    let userId = $(this).val();
    $.ajax({
        type: "post",
        url: "/deleteuser",
        headers: {
            "Csrf-Token": get_token()
        },
        data: {
            userId: userId
        },
        success: function() {
            location.reload();
        }
    })
})

$(".b_new_password").click(function(){
    let userId = $(this).val();
    let password = $(this).parent().siblings("td.td_password").find("input").val();

    $.ajax({
        type: "post",
        url: "/newpassword",
        headers: {
            "Csrf-Token": get_token()
        },
        data: {
            userId: userId,
            password: password
        },
        success: function() {
            /* reload */
        }
    })
})

$(".b_edit_user").click(function(){
    let userId = $(this).val();
    let password = $(this).parent().siblings("td.td_password").find("input").val();

    $.ajax({
        type: "post",
        url: "/edituser",
        headers: {
            "Csrf-Token": get_token()
        },
        data: {
            userId: userId,
            password: password
        },
        success: function() {
            /* reload */
        }
    })
})



$("#f_new").submit(function(event){
    event.preventDefault();
    let admin;
    if ( $('#i_f_new_admin').checked ) {
        admin = 'j';
    } else {
        admin = 'n';
    }

    $.ajax({
        type: "post",
        url: "/createuser",
        headers: {
            "Csrf-Token": get_token()
        },
        data: {
            userName: $("#i_f_new_name").val(),
            password: $("#i_f_new_password").val(),
            admin: admin
        },
        success: function() {
            location.reload();
        }
    })
})