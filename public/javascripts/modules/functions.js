export { get_token, create_cell, create_input_cell, create_hidden_input, get_value_from_id, get_inner_from_id, set_value_by_id, set_inner_by_id }

function get_token() {
    let cookies_raw = decodeURIComponent(document.cookie);
    let ca = cookies_raw.split(";");
    for( let i = 0 ; i< ca.length ; i++ ){
        let c = ca[i];
        while ( c.charAt(0) === ' ') {
            c = c.substring(1);
        }
        if ( c.indexOf("token=") === 0 ) {
            return c.substring(6,c.length);
        }
    }
    return "";
}

function create_cell( parent, id, value ) {
    let cell = document.createElement( "td");
    cell.id = id;
    cell.innerHTML = value;
    parent.appendChild(cell);
}

function create_input_cell( parent, id, value, type ) {
    let cell = document.createElement( "td");
    let input = document.createElement("input")
    input.id = id;
    input.type = type;
    input.value = value;
    cell.appendChild(input);
    parent.appendChild(cell);
}

function create_hidden_input( parent, id, value ) {
    let input = document.createElement("input");
    input.id = id;
    input.value = value;
    input.type = "hidden";
    parent.appendChild(input);
}

function get_value_from_id( id ) {
    let el = document.getElementById( id );
    return el.value;
}
function get_inner_from_id( id ) {
    let el = document.getElementById( id );
    return el.innerHTML;
}

function set_inner_by_id( id, text ) {
    let el = document.getElementById("o_sDmg_" + nbr);
    el.innerHTML = text;
}
function set_value_by_id( id, text ) {
    let el = document.getElementById("o_sDmg_" + nbr);
    el.value = text;
}