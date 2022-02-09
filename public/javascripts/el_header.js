import {get_token} from "./functions.js";

$( document ).ready( function() {
    $.ajax({
        type: "get",
        url: "/getuser",
        success: function( data ){
            let p = document.getElementById( "p_user_name" );
            p.innerHTML = data;
            localStorage.setItem( "user_name", data );
            let button = document.getElementById("b_login");
            if ( data.length > 0 ) {
                button.innerHTML = "Logout";

            } else {
                button.innerHTML = "Login";

            }
        }
    })
})

$("#b_login").click(function(event){
    let userName = localStorage.getItem( "user_name" );
    if ( userName.length > 0 ) {
        $.ajax({
            type: "post",
            url: "/logout",
            headers: {
                "Csrf-Token": get_token()
            },
            success: function() {
                location.reload();
            }
        })
    } else {
        location.replace("/login");
    }
})