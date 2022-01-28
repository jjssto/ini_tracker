import {
    get_token,
    create_cell,
    create_input_cell,
    create_hidden_input,
    get_value_from_id,
    //get_inner_from_id,
    set_inner_by_id,
    set_value_by_id
} from './functions.js'

function update_rows() {
    let nbr_rows = get_value_from_id("char_update_nbr_rows");
    for( let row_nbr = 0; row_nbr < nbr_rows; row_nbr++ ) {
        let row = row_nbr.toString();
        let recordId = document.getElementById("record_id_" + row).value;
        let sDmg = document.getElementById("sDmg_" + row).value;
        let pDmg = document.getElementById("pDmg_" + row).value;
        let localIni = document.getElementById("localIni_" + row).value;

        const http = new XMLHttpRequest();
        let url = "/combat/update?csrfToken=" + get_token();

        if ( pDmg !== "" || sDmg !== "" || localIni !== "" ) {
            let data = '{ "id": "' + recordId + '",'
            data += ' "sDmg": "' + sDmg + '",'
            data += ' "pDmg": "' + pDmg + '",'
            data += ' "localIni": "' + localIni + '"}'

            http.open('POST', url);

            http.setRequestHeader("Content-Type", "application/json; charset=UTF-8");
            http.send(data);
            document.getElementById("sDmg_" + row).value = "";
            document.getElementById("pDmg_" + row).value = "";
            document.getElementById("localIni_" + row).value = "";
        }
    }
    update_combat( parseInt( localStorage.getItem( "combatId")) );
}

function load_combat( combatId ) {
    localStorage.setItem( "combatId", combatId.toString() );
    const Http = new XMLHttpRequest();
    Http.onload = function () {
        let list = JSON.parse(this.responseText);
        let tbody = document.getElementById("char_update_tbody");
        let input = document.createElement("input");
        tbody.appendChild(input);
        let counter = 0;
        for (let nbr in list) {
            let row_counter = counter.toString()
            let record = list[nbr];
            let chara = record["char"];
            let row = document.createElement("tr");
            row.id = "row_" + counter;

            create_cell(row, "row_nbr_" + row_counter, (parseInt(nbr) + 1).toString());
            if (parseInt(record["localIni"]) > 0) {
                create_cell(row, "iniValue_" + row_counter, record["iniValue"] + "(*)");
            } else {
                create_cell(row, "iniValue_" + row_counter, record["iniValue"]);
            }
            create_cell(row, "name_" + row_counter, chara["name"]);
            create_cell(row, "o_sDmg_" + row_counter, record["sDmg"]);
            create_cell(row, "o_pDmg_" + row_counter, record["pDmg"]);
            create_input_cell(row, "sDmg_" + row_counter, "", "number");
            create_input_cell(row, "pDmg_" + row_counter, "", "number");
            create_input_cell(row, "localIni_" + row_counter, "", "number");

            let cell = document.createElement("td");
            input = document.createElement("input");
            input.type = "button";
            input.id = "button_" + row_counter;
            input.name = input.id;
            input.value = "Zeile aktualisieren";
            input.addEventListener("click", function () {
                update_rows()
            });
            cell.appendChild(input);
            row.appendChild(cell);

            create_hidden_input(row, "record_id_" + row_counter, record["id"]);
            tbody.appendChild(row);
            counter = counter + 1;
        }
        set_value_by_id("char_update_nbr_rows", counter);
    }
    Http.open("GET", "/ini/" + combatId.toString());
    Http.send();
}


function load_chars_1( combatId ) {
    const Http = new XMLHttpRequest();
    Http.onload = function () {
        let data = JSON.parse(this.responseText);
        let form = document.getElementById("char_selection_f");
        let button = document.getElementById("char_selection_b");
        let select = document.createElement( "select");
        select.id = "char_selection_s";
        for (let i in data) {
            let chara = data[i]
            let input = document.createElement("option");
            input.value = chara["id"];
            input.innerHTML = chara["name"];
            select.appendChild(input);
        }
        form.insertBefore( select, button )
        //create_hidden_input(form, "csrfToken", get_token());
    }
    Http.open("GET", "/getotherchars/" + combatId );
    Http.send();
}
function load_chars_2( combatId ) {
    const Http = new XMLHttpRequest();
    Http.onload = function () {
        let data = JSON.parse(this.responseText);
        let form2 = document.getElementById("remove_char_f");
        let button2 = document.getElementById("remove_char_b");
        let select = document.createElement( "select");
        let select2 = document.getElementById( "dice_roller_s");
        select.id = "remove_char_s";
        for (let i in data) {
            let chara = data[i];
            let input = document.createElement("option");
            let input2 = document.createElement("option");
            input.value = chara["id"];
            input2.value = chara["id"];
            input2.name = "charId";
            input.innerHTML = chara["name"];
            input2.innerHTML = chara["name"];
            select.appendChild( input );
            select2.appendChild( input2 );
        }
        form2.insertBefore( select, button2 );
        //create_hidden_input(form, "csrfToken", get_token());
    }
    Http.open("GET", "/getcombatchars/" + combatId );
    Http.send();
}



function update_combat( combatId ) {
    const Http = new XMLHttpRequest();
    Http.onload = function () {
        let list = JSON.parse(this.responseText);
        let nbr_rows = get_value_from_id( "char_update_nbr_rows");
        for ( let counter = 0; counter < nbr_rows; counter++ ) {
            let row = counter.toString();
            let record = list[counter];
            let chara = record["char"]
            set_inner_by_id( "o_sDmg_" + row, record["sdmg"]);
            set_inner_by_id( "o_pDmg_" + row, record["pdmg"]);
            let el = document.getElementById("iniValue_" + row);
            if (parseInt(record["localIni"]) > 0) {
                el.innerHTML = record["iniValue"] + "(*)";
            } else {
                el.innerHTML = record["iniValue"];
            }
            document.getElementById( "name_" + row).innerHTML = chara["name"];
            document.getElementById( "record_id_" + row).value = record["id"];
        }
    }
    Http.open("GET", "/ini/" + combatId.toString());
    Http.send();
}

function get_dice_rolls( combatId ) {

    const Http = new XMLHttpRequest();
    Http.onload = function () {
        let resp = JSON.parse(this.responseText);
        let list = resp["rolls"];
        let lastRoll = localStorage.getItem( "last_roll" )
        if ( lastRoll == "" ) {
            localStorage.setItem( "last_roll", resp["time"] );
        }
        let tbody = document.getElementById("rolls_table_tbody");
        for ( let i in list ) {
           let roll = list[i];
            if ( Date.parse(lastRoll ) >= Date.parse( roll["zeit"] )  ) {
                continue;
            }
           let row = document.createElement('tr');

           let cell = document.createElement('td');
           let time = new Date( roll.zeit );
           let options = { hour: "2-digit", minute: "2-digit", second: "2-digit" };
           cell.innerHTML = time.toLocaleDateString( 'de', options );
            row.appendChild( cell );

           cell = document.createElement('td');
           cell.innerHTML = roll["char"] ;
           row.appendChild( cell );
           let diceRoll = "( "
           for ( let die in roll["roll"] ) {
               diceRoll += roll["roll"][die].toString() + ", ";
           }
           diceRoll += ')'
           cell = document.createElement('td');
           cell.innerHTML = diceRoll ;
           row.appendChild( cell );

            cell = document.createElement('td');
            cell.innerHTML = roll["result"] ;
            row.appendChild( cell );
           tbody.appendChild( row );
           localStorage.setItem( "last_roll", roll["zeit"] );
        }
    }

    let timestamp;
    timestamp = localStorage.getItem("last_roll").toString();

    let url = "/rolls?"
    url += "&&id=" + combatId.toString();
    url += "&&timestamp=" + timestamp;
    Http.open("GET", url );
    Http.send();
}


localStorage.setItem( "last_roll", "");
let combat_id = parseInt( localStorage.getItem( "combatId") );
load_combat( combat_id );
load_chars_1( combat_id );
load_chars_2( combat_id );
get_dice_rolls( combat_id );


window.setInterval( function() {
    update_combat( combat_id );
}, 5000 );
window.setInterval( function() {
get_dice_rolls( combat_id );
    }, 1000)
