import {get_token} from "./functions.js";

$().ready( function(){
    getCombats();
})

$( "#f_dice_roller").submit( function( event ) {
    event.preventDefault();
})

$( "#b_dice_roll").click(function(){
    let el = document.getElementById("i_no_tag");
    let noTag = 'n';

    $.ajax({
        type: "post",
        url: "/rtc/roll",
        dataType: "Json",
        headers: {
            "Csrf-Token": get_token()
        },
        data: {
            skill: $("#i_skill").val(),
            attribute: $("#i_attribute").val(),
            noTag: noTag
        },
        success: function( data ) {
            displayResult( data );
        }
    });
})

$( "#b_no_tag").click(function(){
    let el = document.getElementById("i_no_tag");
    let noTag = 'j';

    $.ajax({
        type: "post",
        url: "/rtc/roll",
        headers: {
            "Csrf-Token": get_token()
        },
        data: {
            skill: $("#i_skill").val(),
            attribute: $("#i_attribute").val(),
            noTag: noTag
        },
        success: function( data ) {
            displayResult( data );
        }
    });
})

$( "#b_only_attribut").click(function(){
    let el = document.getElementById("i_no_tag");
    let noTag = 'n';

    $.ajax({
        type: "post",
        url: "/rtc/roll",
        headers: {
            "Csrf-Token": get_token()
        },
        data: {
            skill: "0",
            attribute: $("#i_attribute").val(),
            noTag: noTag
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

function displayResult( json ) {

    let row = document.createElement( "tr" );

    let zeit;

    row.appendChild( createColumn( json.attribute ));

    let skill;
    if ( json.noTag == 'j' ) {
        skill = '(' + json.skill + ')';
    } else {
        skill = json.skill;
    }
    row.appendChild( createColumn( skill ));

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

function getCombats() {
    $.ajax({
        type: "post",
        url: "/rtc/diceroller",
        dataType: "Json",
        headers: {
            "Csrf-Token": get_token()
        },
        success: function( data ) {
           let list = document.getElementById( "ol_combats" );
           for ( let i in data ) {
              let li = document.createElement("li");
              let a = document.createElement( "a" );
              a.href = "/rtc/diceroller/" + data[i].id;
              a.innerHTML = data[i].name;
              li.appendChild( a );
              list.appendChild( li );
           }
        }

    })
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