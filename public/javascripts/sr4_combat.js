import {get_token, createColumn } from './functions.js';

$( document ).ready( function() {
    let combatId = window.location.pathname.match( /[0-9]+$/ )[0];
    localStorage.setItem( "combat_id", combatId );
    localStorage.removeItem( "last_roll" )
    localStorage.removeItem( "last_ini_update" )
    getIniList( combatId );
    loadCharList_1( combatId );
    loadCharList_2( combatId );
    getDiceRolls( combatId, '' );
})

window.setInterval( function() {
    let combatId = localStorage.getItem( "combat_id" )
    getIniList( combatId );
    getDiceRolls( combatId, null )
}, 2000 );


/* **********************************
 * Roll a dice
 * **********************************/
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
        dataType: "json",
        data: {
            nbrOfDice: $('#dice_roller_n').val(),
            recordId: $('#dice_roller_s').val(),
            exploding: exploding
        },
        success: function( data ) {
            displayDiceRoll( data );
        }
    });
})

/* ****************************************
 * Roll initiative
 * **************************************/
$("#roll_ini_b").click( function() {
    let combatId = localStorage.getItem( "combat_id" )
    $.ajax({
        type: "post",
        url: "/sr4/combat/ini",
        headers: {
            "Csrf-Token": get_token()
        },
        data: {
            combatId: combatId
        },
        success: function() {
            getIniList( combatId );
        }
    })
});

/* **********************************************
 * Werte in Tabellenzeile aktualisieren
 * *********************************************/
function saveChanges( index ) {
        $.ajax({
            type: "post",
            url: "/sr4/combat/update",
            headers: {
                "Csrf-Token": get_token()
            },
            data: {
                id: $("#record_id_" + index).val(),
                pDmg: $("#p_dmg_new_" + index).val(),
                sDmg: $("#s_dmg_new_" + index).val(),
                localIni: $("#local_ini_" + index).val()
            },
            success: function () {
                getIniList(localStorage.getItem("combat_id"))
            }
        })
};
$("#char_update_f").submit(function (event) {
    event.preventDefault();
})
/* *******************************************
 * Add Char to combat
 * ******************************************/
$("#char_selection_f").submit( function( event ){
    event.preventDefault();
    let combatId = localStorage.getItem( "combat_id")
    $.ajax({
        type: "post",
        url: "/sr4/combat/addchar",
        headers: {
            "Csrf-Token": get_token()
        },
        data: {
            charId: $("#char_selection_s").val(),
            combatId: combatId
        }
    });
})


/* ***********************************
 * Remove Char from combat
 * *******************************/
$("#remove_char_f").submit( function( event ){
    event.preventDefault();
    let combatId = localStorage.getItem( "combat_id")
    $.ajax({
        type: "post",
        url: "/sr4/combat/removechar",
        headers: {
            "Csrf-Token": get_token()
        },
        data: {
            recordId: $("#remove_char_s").val(),
            combatId: combatId
        },
        success: function() {
            getIniList( combatId );
        }
    });
})


function addRow( tbody, index ) {
    let row = document.createElement( 'tr');

    let col = document.createElement( 'td');
    col.id = "pos_" + index;
    col.innerHTML = ( parseInt( index ) + 1 ).toString();
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
    input.className = 'update_button';
    input.innerHTML = "Speichern"
    input.onclick = function( ) {
        saveChanges( index );
    }
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
    let tBody = document.getElementById("char_update_tbody")
    let tRow = tBody.firstChild;
    for ( let i in data ) {
        if ( tRow == null ) {
            tRow = addRow( tBody, row );
        }
        updateRow( data[i], row );
        tRow = tRow.nextSibling;
        row++;
    }
    while ( tRow != null ) {
        let nextRow = tRow.nextSibling;
        tRow.remove();
        tRow = nextRow;
    }
}

function updateRow( data, index ) {

    let element = document.getElementById( "record_id_" + index);
    element.value = data.id;
    element = document.getElementById( "name_" + index);
    element.innerHTML = data.char.name;
    element = document.getElementById( "s_dmg_" + index);
    element.innerHTML = data.sdmg;
    element = document.getElementById( "p_dmg_" + index);
    element.innerHTML = data.pdmg;
    element = document.getElementById( "ini_" + index);
    if ( parseInt( data.localIni ) > 0 ) {
        element.innerHTML = data.iniValue + "(" + data.localIni + ")";
    } else {
        element.innerHTML = data.iniValue;
    }
}

function getIniList( combatId ){
    let time = localStorage.getItem( "last_ini_update" );
    $.ajax({
        type: "get",
        url: "/sr4/ini",
        dataType: "json",
        data: {
            combatId: combatId,
            timestamp: time
        },
        success: function( data ) {
            if ( data != null ) {
                localStorage.setItem("last_ini_update", data.lastChanged );
                refreshIni( data.charas );
            }
        }
    });

}

function displayDiceRoll( data ) {

    let row = document.createElement( "tr" );
    let time = new Date( data.zeit );
    let options = { hour: "2-digit", minute: "2-digit" };
    row.appendChild( createColumn( time.toLocaleTimeString( 'de', options ) ) );
    row.appendChild( createColumn( data.char ) );
    row.appendChild( createColumn( data.roll ) );
    row.appendChild( createColumn( data.result ) );

    let tbody = document.getElementById( "rolls_table_tbody" );
    if ( data.roll != null ) {
        tbody.appendChild(row);
        tbody.scrollTop = tbody.scrollHeight;
        localStorage.setItem( "last_roll", data.zeit )
    }
}

function displayDiceRolls( json ) {
    let time;
    if ( localStorage.getItem( "last_roll ")!= null ) {
        let time = Date.parse( localStorage.getItem( 'last_roll') );
    } else {
        time = null;
    }
    let rolls = json.rolls;
    for ( let i in rolls ) {
        if ( time == null || time < Date.parse( rolls[i].zeit ) ) {
            displayDiceRoll( rolls[ i ] );
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
      dataType: "json",
      data: {
          id: combatId,
          timestamp: time
      },
      success: function( data ) {
          displayDiceRolls( data );
      }
    });
}

function loadCharList_1( combatId ) {
    $.ajax({
        type: "get",
        url: "/sr4/ini",
        dataType: "json",
        data: {
            combatId: combatId,
            timestamp: ''
        },
        success: function (data) {
            let records = data.charas;
            if (records != null) {
                let select_1 = document.getElementById( "remove_char_s")
                let select_2 = document.getElementById( "dice_roller_s")
                for (let i in records) {
                    let option_1 = document.createElement("option");
                    let option_2 = document.createElement("option");
                    option_1.value = records[i].id;
                    option_2.value = records[i].id;
                    option_1.innerHTML = records[i].char.name;
                    option_2.innerHTML = records[i].char.name;
                    select_1.appendChild(option_1);
                    select_2.appendChild(option_2);
                }
            }
        }
    });
}
function loadCharList_2( combatId ) {
    $.ajax({
        type: "get",
        url: "/sr4/getotherchars/" + combatId,
        dataType: "json",
        data: {
        },
        success: function (chara) {
            if (chara != null) {
                let select = document.getElementById("char_selection_s");
                for( let i in chara ) {
                    let option = document.createElement( "option" );
                    option.value = chara[i].id;
                    option.value = chara[i].id;
                    option.innerHTML = chara[i].name;
                    option.innerHTML = chara[i].name;
                    select.appendChild( option );
                }
            }
        }
    });
}