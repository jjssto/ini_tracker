import {get_token} from "./functions.js";

$("#f_add_role").submit(function(event){
    event.preventDefault();
    $.ajax({
        type: "post",
        url: "/addrole",
        headers: {
            "Csrf-Token": get_token()
        },
        data: {
            user: $("#i_add_role_user_id").val(),
            role: $("input[name='add_role']:checked").val()
        },
        success: function(x) {
            alert("Rolle hinzugefügt");
        }
    })
})
$("#f_remove_role").submit(function(event){
    event.preventDefault();
    $.ajax({
        type: "post",
        url: "/removerole",
        headers: {
            "Csrf-Token": get_token()
        },
        data: {
            user: $("#i_remove_role_user_id").val(),
            role: $("input[name='remove_role']:checked").val()
        },
        success: function(x) {
            alert("Rolle entfernt");
        }
    })
})