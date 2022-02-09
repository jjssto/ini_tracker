import {get_token} from "./functions.js";


$( "#f_dice_roller").submit( function( event ) {
    event.preventDefault();


    $.ajax({
        type: "post",
        url: "/rtc/diceroller",
        headers: {
            "Csrf-Token": get_token()
        },
        data: {
            skill: $("#i_skill").val(),
            attribute: $("#i_attribute").val(),
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

    row.appendChild( createColumn( json.attribute ));
    row.appendChild( createColumn( json.skill ));

    let d6 = json.d6;
    if ( d6 != null ) {
        row.appendChild( createColumn( d6.roll ) );
        zeit = d6.zeit;
    } else {
        row.appendChild( createColumn( "" ) );
    }
    let d8 = json.d8;
    if ( d8 != null ) {
        row.appendChild( createColumn( d8.roll ) );
        zeit = d8.zeit;
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
    row.appendChild( createColumn( json.success ));

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


$("#i_skill").change(function(event){
    skillSliderValue();
})

$("#i_attribute").change(function(event){
    attributeSliderValue();
})

function skillSliderValue() {
    let value =  $("#i_skill").val();
    if ( parseInt( value ) == -1 ) {
        value = "0";
    } else if ( parseInt( value ) == 0 ) {
        value = "Ø";
    }
    $("#p_skill").text( value );
}

function attributeSliderValue() {
    $("#p_attribute").text( $("#i_attribute").val() );
}

skillSliderValue();
attributeSliderValue();