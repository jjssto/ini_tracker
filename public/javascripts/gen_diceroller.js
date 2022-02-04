import {get_token} from "./functions.js";

$( "#f_dice_roller").submit( function( event ) {
    event.preventDefault();
    let action = $( "#f_dice_roller").action;

    let debug = get_token();

    $.ajax({
        type: "post",
        url: "/diceroller",
        headers: {
            "Csrf-Token": get_token()
        },
        data: {
            d4: $("#i_d4").val(),
            d6: $("#i_d6").val(),
            d8: $("#i_d8").val(),
            d10: $("#i_d10").val(),
            d12: $("#i_d12").val(),
            d20: $("#i_d20").val(),
        },
        success: function( data ) {
            displayResult( data );
        }
    });
})

function createColumn( roll ) {
    let col = document.createElement( "td")
    col.innerHTML = roll;
    return col;
}

function displayResult( data ) {

    let json = JSON.parse( data );
    let row = document.createElement( "tr" );

    let zeit;
    let d4 = json.d4;
    if ( d4 != null ) {
        row.appendChild( createColumn( d4.roll ) );
        zeit = d4.zeit;
    } else {
        row.appendChild( createColumn( "" ) );
    }
    let d6 = json.d6;
    if ( d6 != null ) {
        zeit = d6.zeit;
        row.appendChild( createColumn( d6.roll ) );
    } else {
        row.appendChild( createColumn( "" ) );
    }
    let d8 = json.d8;
    if ( d8 != null ) {
        zeit = d8.zeit;
        row.appendChild( createColumn( d8.roll ) );
    } else {
        row.appendChild( createColumn( "" ) );
    }
    let d10 = json.d10;
    if ( d10 != null ) {
        row.appendChild( createColumn( d10.roll ) );
        zeit = d10.zeit;
    } else {
        row.appendChild( createColumn( "" ) );
    }
    let d12 = json.d12;
    if ( d12 != null ) {
        row.appendChild( createColumn( d12.roll ) );
        zeit = d12.zeit;
    } else {
        row.appendChild( createColumn( "" ) );
    }
    let d20 = json.d20;
    if ( d20 != null ) {
        zeit = d20.zeit;
        row.appendChild( createColumn( d20.roll ) );
    } else {
        row.appendChild( createColumn( "" ) );
    }

    if ( zeit != null ) {
        let col = document.createElement("td");
        let time = new Date( zeit ) ;
        let options = { hour: "2-digit", minute: "2-digit" };
        col.innerHTML =  time.toLocaleDateString('de', options );
        row.insertBefore(col, row.firstChild);
        let tbody = document.getElementById( "tbody_result" );
        tbody.appendChild( row );

        tbody.scrollTop = tbody.scrollHeight;
    }
}

