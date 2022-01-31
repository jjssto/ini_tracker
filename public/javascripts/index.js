import {get_token} from "./functions.js";
//function get_token() {
//    let cookies_raw = decodeURIComponent(document.cookie);
//    let ca = cookies_raw.split(";");
//    for( let i = 0 ; i< ca.length ; i++ ){
//        let c = ca[i];
//        while ( c.charAt(0) === ' ') {
//            c = c.substring(1);
//        }
//        if ( c.indexOf("token=") === 0 ) {
//            return c.substring(6,c.length);
//        }
//    }
//    return "";
//}

function load_combats() {
    const Http = new XMLHttpRequest();
    Http.onload = function () {
        let list = JSON.parse(this.responseText);
        let element = document.getElementById( "combat_list" )
        for (let x in list) {
            let link = document.createElement( "a" );
            link.href = "/combat/" + list[x].id;
            link.innerHTML = list[x].description;
            let el = document.createElement("li");
            el.appendChild( link );
            element.appendChild( el );
        }
    }
    Http.open("GET", "/getcombats");
    Http.send();
}

function new_combat() {
     let item = document.getElementById("bez");
     let ret = '{"0":"' + item.value + '"}';

    const Http = new XMLHttpRequest();
    let url = "/addcombat";
    Http.open( 'POST', url );
    Http.setRequestHeader( "Content-Type", "Application/Json");
    Http.setRequestHeader( "Csrf-Token", get_token() );
    Http.send( ret );
}

document.getElementById("new_combat_f").addEventListener(
    "submit",
 function( event ) {
    event.preventDefault();
    new_combat();
    load_combats();
});

load_combats();