import {
    get_token,
    create_cell,
    create_input_cell,
    create_hidden_input,
    get_value_from_id,
    get_inner_from_id,
    set_inner_by_id
} from './modules/functions.js'

function update_row( row_nbr  ) {
    let row = row_nbr.toString();
    let recordId = document.getElementById( "record_id_" + row ).value;
    let sDmg = document.getElementById( "sDmg_" + row ).value;
    let pDmg = document.getElementById( "pDmg_" + row ).value;
    let localIni = document.getElementById( "localIni_" + row ).value;

    const http = new XMLHttpRequest();
    let url = "/combat/update?csrfToken=" + get_token();

    let data = '{ "id": "' + recordId + '",'
    data += ' "sDmg": "' + sDmg + '",'
    data += ' "pDmg": "' + pDmg + '",'
    data += ' "localIni": "' + localIni + '"}'

    http.open( 'POST', url );

    http.setRequestHeader( "Content-Type", "application/json; charset=UTF-8");
    http.send( data );
}

function load_combat( combatId ) {
    let token = get_token();
    const Http = new XMLHttpRequest();
    Http.onload = function () {
        let data = JSON.parse(this.responseText);
        let list = data["charas"];
        let tbody = document.getElementById("char_update_tbody");
        let input = document.createElement("input");
        input.id = "combat_id";
        input.type = "hidden";
        input.value = data["id"];
        tbody.appendChild(input);
        for( let nbr in list ) {
            let record = list[nbr];
            let chara = record["char"];
            let row = document.createElement("tr");
            row.id = "row_" + nbr;

            create_cell( row, "row_nbr_" + nbr, (parseInt(nbr)+1).toString() );
            if ( parseInt( record["localIni"] ) > 0 ) {
                create_cell( row, "ini_value_" + nbr, record["iniValue"] + "(*)");
            } else {
                create_cell( row, "ini_value_" + nbr, record["iniValue"]);
            }
            create_cell( row, "name_" + nbr, chara["name"]);
            create_cell( row, "o_sDmg_" + nbr, record["sDmg"]);
            create_cell( row, "o_pDmg_" + nbr, record["pDmg"]);
            create_input_cell( row, "sDmg_" + nbr, "", "number" );
            create_input_cell( row, "pDmg_" + nbr, "", "number" );
            create_input_cell( row, "localIni_" + nbr, "", "number" );

            let element = document.createElement("td");
            input = document.createElement("input");
            input.type = "button";
            input.id = "button";
            input.name = input.id;
            input.value = "Zeile aktualisieren";
            input.onclick = function(){ update_row( parseInt(nbr) )};

            create_input_cell( row, "record_id_" + nbr, record["id"], "hidden" );
            tbody.appendChild(row);
        }
    }
    Http.open("GET", "/getcombats/" + combatId.toString());
    Http.send();
}

function update_combat( combatId ) {
    const Http = new XMLHttpRequest();
    Http.onload = function () {
        let list = JSON.parse(this.responseText);
        let el;
        let row_counter = 0;
        for( let id in list ) {
            let nbr = row_counter.toString();
            let record = list[nbr];
            let chara = record["char"]
            set_inner_by_id( "o_sDmg_" + nbr, record["sdmg"]);
            set_inner_by_id( "o_pDmg_" + nbr, record["pdmg"]);
            el = document.getElementById("iniValue_" + nbr);
            if (parseInt(record["localIni"]) > 0) {
                el.innerHTML = record["iniValue"] + "(*)";
            } else {
                el.innerHTML = record["iniValue"];
            }
            set_inner_by_id( "name_" + nbr, chara["name"]);
            set_inner_by_id( "record_id_" + nbr, record["id"]);
            row_counter++;
        }
    }
    Http.open("GET", "/ini/" + combatId.toString());
    Http.send();
}

let combat_id = parseInt( get_value_from_id( "combat_id") );
load_combat( combat_id );
window.setInterval( function() {update_combat( combat_id )}, 5000 );
