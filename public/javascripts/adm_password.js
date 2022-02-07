import {get_token} from "./functions.js";

$("#f_change_pw").submit(function(event){
    event.preventDefault();
    if ( $("#i_f_change_pw_neu_1").val() != $("#i_f_change_pw_neu_2").val() ) {
        alert( "Neue Passwörter stimmen nicht überein!");
    } else {
        $.ajax({
            type: "post",
            url: "/changepw",
            headers: {
                "Csrf-Token": get_token()
            },
            data: {
                oldPW: $("#i_f_change_pw_alt").val(),
                newPW: $("#i_f_change_pw_neu_1").val()
            },
            success: function( data ) {
                alert( data );
            },
            error: function() {
                alert( data );
            }
        })
    }
})