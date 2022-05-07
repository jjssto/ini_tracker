import {get_token} from "./functions.js";
import {displayRoll} from "./coc_index.js";

$('.b_charsheet').click(function(){
    let id = $(this).attr('id');
    let mod = $("#i_" + id ).val();
    let skill = $(this).html();
    if ( mod == null ) {
        mod = 0;
    }

    $.ajax({
        url: "/coc/roll",
        type: "post",
        headers: {
            "Csrf-Token": get_token()
        },
        data: {
            skill: skill,
            comment: id,
            adv: mod,
            combat: $("#i_combat_id").val(),
            char: $("#i_charsheet_id").val()
        },
        success: function(data) {
            displayRoll( JSON.parse( data ) )
        }
    })

})