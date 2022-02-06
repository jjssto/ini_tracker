import {get_token} from "./functions.js";

$("#new_combat_f").submit( function( event ){
    event.preventDefault();

    $.ajax({
        type: "post",
        url: "/sr4/addcombat",
        dataType: "json",
        headers: {
            "Csrf-Token": get_token()
        },
        data: {
            bez: $("#bez").val()
        },
        success: function( data ) {
            let list = document.getElementById("combat_list");
            let el = document.createElement( "li" );
            let a = document.createElement( "a" );
            a.href = "/sr4/combat/" + data.id;
            a.innerHTML = data.bez;
            el.appendChild(a);
            list.appendChild(el);
        }

    })
})