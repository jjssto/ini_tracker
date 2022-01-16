const Http = new XMLHttpRequest();
Http.onload=function() {
    let list = JSON.parse( this.responseText );
    let select = document.getElementById("combat_select")
    for (let x in list) {
        let el = document.createElement("option");
        el.value = list[x].id;
        el.innerHTML = list[x].description;
        select.appendChild( el );
    }
}
Http.open( "GET", "/getcombats" );
Http.send();