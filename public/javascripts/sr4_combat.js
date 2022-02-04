import {get_token, createColumn } from './functions.js';

$( document ).ready( function() {
    let combatId = window.location.href.match( '/(?<=\.ch\/combat\/)[0-9]+/').toString();
    localStorage.setItem( "combat_id", combatId );
    localStorage.setItem( "last_roll", '' )
    localStorage.setItem( "last_ini_update", '' )
    getIniList( combatId );
    getDiceRolls( combatId, '' );
})

window.setInterval( function() {
    let combatId = localStorage.getItem( "combat_id" )
    getIniList( combatId );
    getDiceRolls( combatId, null )
}, 2000 );

$( "#dice_roller_f").submit( function( event ) {
    event.preventDefault();

    let exploding;
    if ( $('#dice_roller_ex').checked ) {
       exploding = 'y';
    } else {
        exploding = 'n';
    }

    $.ajax({
        type: "post",
        url: "/sr4/combat",
        headers: {
            "Csrf-Token": get_token()
        },
        data: {
            nbrOfDice: $('#dice_roller_n').val(),
            combatId: $('#dice_roller_combat_id').val(),
            charId: $('#dice_roller_s').val(),
            exploding: exploding
        },
        success: function( data ) {
            let json = JSON.parse( data );
            displayDiceRoll( json );
        }
    });
})

$("#char_update_tbody").find("button").click( function( ) {
    let row = $(this).parent().parent();
    let index = row.index();
    $.ajax({
        type: "post",
        url: "/sr4/combat/update",
        data: {
            id: $("#record_id_" + index ).val(),
            pDmg: $("#p_dmg_new_" + index).val(),
            sDmg: $("#s_dmg_new_" + index).val(),
            localIni: $("#local_ini_" + index).val()
        },
        success: function() {
            getIniList( localStorage.getItem( "combat_id") )
        }
    })
})

$("#char_update_f").submit(function( event ) {
    event.preventDefault();
})


function addRow( tbody, index ) {
    let row = document.createElement( 'tr');

    let col = document.createElement( 'td');
    col.id = "pos_" + index;
    col.innerHTML = index;
    row.appendChild( col  );

    col = document.createElement( 'td');
    col.id = "ini_" + index;
    row.appendChild( col  );

    col = document.createElement( 'td');
    let input = document.createElement( 'input' );
    input.type = "number";
    input.id = 'local_ini_' + index;
    input.min = "0";
    input.max = "50";
    col.appendChild( input );
    row.appendChild( col  );

    col = document.createElement( 'td');
    col.id = "name_" + index;
    row.appendChild( col  );

    col = document.createElement( 'td');
    col.id = "s_dmg_" + index;
    row.appendChild( col  );


    col = document.createElement( 'td');
    input = document.createElement( 'input' );
    input.type = "number";
    input.id = 's_dmg_new_' + index;
    input.min = "0";
    input.max = "50";
    col.appendChild( input );
    row.appendChild( col  );

    col = document.createElement( 'td');
    col.id = "p_dmg_" + index;
    row.appendChild( col  );

    col = document.createElement( 'td');
    input = document.createElement( 'input' );
    input.type = "number";
    input.id = 'p_dmg_new_' + index;
    input.min = "0";
    input.max = "50";
    col.appendChild( input );
    row.appendChild( col  );

    col = document.createElement( 'td');
    input = document.createElement( 'button' );
    input.type = "submit";
    input.id = 'button' + index;
    input.min = 0;
    input.max = 50;
    col.appendChild( input );

    input = document.createElement( 'input' );
    input.type = "hidden";
    input.id = 'record_id_' + index;
    col.appendChild( input );

    row.appendChild( col  );


    tbody.appendChild( row );

    return row;
}

function refreshIni( data ){

    let row = 0;
    let tBody = $("#tbody_result")
    let tRow = tBody.find("tr")

    for ( let i in data ) {
        if ( tRow == null ) {
            tRow = addRow( tBody, row )
        }
        updateRow( tRow, data[i], row );
        tRow = tRow.next();
        row++
    }
    while ( tRow != null ) {
        let nextRow = tRow.next();
        tRow.remove();
        tRow = nextRow;
    }
}

function updateRow( tRow, data, index ) {

    let element = tRow.find( "#record_id_" + index );
    element.value = data.id;
    element = tRow.find( "#name_" + index );
    element.innerHTML = data.char.name;
    element = tRow.find( "#s_dmg_" + index );
    element.innerHTML = data.sDmg;
    element = tRow.find( "#p_dmg_" + index );
    element.innerHTML = data.pDmg;

    element = tRow.find( "#ini_" + index );
    if ( parseInt( data.localIni ) > 0 ) {
        element.innerHTML = data.iniValue + "(" + data.localIni + ")";
    } else {
        element.innerHTML = data.iniValue;
    }
    //$("#record_id_" + index).value = data.id;
    //$("#name_id_" + index).innerHTML = data.char.name;
    //$("#s_dmg_" + index).innerHTML = data.sDmg;
    //$("#p_dmg_" + index).innerHTML = data.pDmg;
    //if ( parseInt( data.localIni ) > 0 ) {
    //    $("#ini_" + index).innerHTML = data.iniValue + "(" + data.localIni + ")";
    //} else {
    //    $("#ini_" + index).innerHTML = data.iniValue;
    //}
}

function getIniList( combatId ){
    $.ajax({
        type: "post",
        url: "/sr4/ini",
        data: {
            combatId: combatId
        },
        success: function( data ) {
            let json = JSON.parse( data );
            if ( json.newValues == 'j' ) {
                localStorage.setItem( "last_ini_update", json.time );
            }
            refreshIni( json.iniList );
        }
    });

}

function displayDiceRoll( data ) {

    let row = document.createElement( "tr" );
    let time = new Date( data.zeit );
    let options = { hour: "2-digit", minute: "2-digit" };
    row.appendChild( createColumn( time.toLocaleDateString( 'de', options ) ) );
    row.appendChild( createColumn( data.char ) );
    row.appendChild( createColumn( data.roll ) );

    let tbody = document.getElementById( "rolls_table_tbody" );
    if ( data.roll != null ) {
        tbody.appendChild(row);
        tbody.scrollTop = tbody.scrollHeight;
        localStorage.setItem( "last_roll", data.zeit )
    }
}

function displayDiceRolls( data ) {
    let json = JSON.parse( data );
    let time = Date.parse( localStorage.getItem( 'last_roll') );
    for ( let i in json ) {
        if ( time < Date.parse( json[i].zeit ) ) {
            displayDiceRoll( json[ i ]);
        }
    }
}

function getDiceRolls( combatId, time ) {
    if ( time == null ) {
        if ( time == null && localStorage.getItem( "last_roll") != null ) {
            time = localStorage.getItem( "last_roll" );
        } else {
            time = '';
        }
    }
    $.ajax({
      type: "get",
      url:  "/sr4/rolls",
      data: {
          id: combatId,
          timestamp: time
      },
      success: displayDiceRolls
    });
}