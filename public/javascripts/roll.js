import {get_token} from "./functions.js";

let button = document.getElementById( 'dice_roller_b' );

button.addEventListener(
    'click',
    function( ) {
        let url = '/combat';
        let token =  $('input[name="csrfToken"]').attr('value')
        $.ajax({
            type: "post",
            url: "/combat",
            referer: "/combat" + localStorage.getItem( "combatId" ),
            headers: {
                "Csrf-Token": get_token()
            },
            data: {
                nbrOfDice: $('#dice_roller_n').val(),
                combatId: $('#dice_roller_combat_id').val(),
                charId: $('#dice_roller_s').val()
            }
        });
    }
);

$( "#dice_roller_f").submit( function( event ) {
    event.preventDefault();
});


button = document.getElementById( 'char_selection_b' );
button.addEventListener(
    'click',
    function() {
    let ret = ""
    let select = document.getElementById("char_selection_s");
    ret = '"' + select.value + '"';
    let combatId = localStorage.getItem( 'combatId');
    let payload = '{"chars": [' + ret + '], "combatId": "' + combatId.toString() + '"}';
    let url = "/combat/addchars?csrfToken=" + get_token();

    const Http = new XMLHttpRequest();
    Http.open( "POST", url );
    Http.setRequestHeader(  "Content-Type", "application/json" );
    Http.send( payload );
});

button = document.getElementById( 'remove_char_b' );
button.addEventListener(
    'click',
    function() {
    let ret = ""
    let select = document.getElementById("remove_char_s");
    ret = '"' + select.value + '"';
    let combatId = localStorage.getItem( 'combatId');
    let payload = '{"chars": [' + ret + '], "combatId": "' + combatId.toString() + '"}';
    let url = "/combat/removechars?csrfToken=" + get_token();

    const Http = new XMLHttpRequest();
    Http.open( "POST", url );
    Http.setRequestHeader(  "Content-Type", "application/json" );
    Http.send( payload );
});
