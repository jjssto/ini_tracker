import {
    get_token,
    create_cell,
    create_input_cell,
    create_hidden_input,
    get_value_from_id,
    //get_inner_from_id,
    set_inner_by_id,
    set_value_by_id
} from './modules/functions.js'

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
    update_combat( parseInt( get_value_from_id( "combat_id")) );
}

function load_combat( combatId ) {
    const Http = new XMLHttpRequest();
    Http.onload = function () {
        //let data = JSON.parse(this.responseText);
        //let list = data["charas"];
        let list = JSON.parse(this.responseText);
        let tbody = document.getElementById("char_update_tbody");
        let input = document.createElement("input");
        //input.id = "combat_id";
        //input.type = "hidden";
        //input.value = data["id"];
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

function load_chars() {
    const Http = new XMLHttpRequest();
    Http.onload = function () {
        let data = JSON.parse(this.responseText);
        let form = document.getElementById("char_selection_f");
        let button = document.getElementById("char_selection_b");
        for (let i in data) {
            let chara = data[i]
            let div = document.createElement("div");
            let input = document.createElement("input");
            input.id = "char_" + chara["id"];
            input.name = "id";
            input.type = "checkbox";
            input.value = chara["id"];
            div.appendChild(input);
            let label = document.createElement("label");
            label.for = "char_" + chara["id"];
            label.innerHTML = chara["name"];
            div.appendChild(label);
            form.insertBefore(div,button);
        }
        create_hidden_input(form, "csrfToken", get_token());
    }
    Http.open("GET", "/getchars" );
    Http.send();
}

function pre_select_chars( combat_id ) {
    const Http = new XMLHttpRequest();
    Http.onload = function () {
        let raw = JSON.parse(this.responseText);
        let data = raw["charas"];
        for (let i in data) {
            let record = data[i];
            let chara = record["char"];
            let input = document.getElementById("char_" + chara["id"] );
            input.checked = true;
        }
    }
    Http.open("GET", "/getcombats/" + combat_id );
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
    let ts = Date.now();
    Http.onload = function () {
        let list = JSON.parse(this.responseText);
        let tbody = document.getElementById("rolls_table_tbody");
        for ( let i in list ) {
           let roll = list[i];
           let row = document.createElement('tr');

           let cell = document.createElement('td');
           cell.innerHTML = ts ;
            row.appendChild( cell );

           let chara = roll["charRecord"]["char"];
           cell = document.createElement('td');
           cell.innerHTML = chara["name"] ;
           row.appendChild( cell );
           let diceRoll = "( "
           for ( let die in roll["roll"] ) {
               diceRoll += roll["roll"][die].result.toString() + ", ";
           }
           diceRoll += ')'
            cell = document.createElement('td');
            cell.innerHTML = diceRoll ;
            row.appendChild( cell );
           tbody.appendChild( row );
        }
    }

    let timestamp = localStorage.getItem( "last_roll" );

    //let url = "/rolls?csrfToken=" + get_token()
    let url = "/rolls?"
    url += "&&id=" + combatId.toString();
    if ( timestamp != null ) {
        url += "&&timestamp=" + timestamp;
    } else {
        url += "&&timestamp=0";
    }
    Http.open("GET", url );
    Http.setRequestHeader("combatId", combatId.toString() );
    Http.send();
    localStorage.setItem( "last_roll", ( Date.now() - 7 ).toString() );
}

let combat_id = parseInt( get_value_from_id( "combat_id") );
load_combat( combat_id );
load_chars();
pre_select_chars( combat_id );
get_dice_rolls( combat_id );
//window.setInterval( function() {
//    update_combat( combat_id );
//    get_dice_rolls( combat_id );
//}, 5000 );
