import {get_token} from "./functions.js";
export {displayRoll};

$( "#f_roll" ).submit( function(event){
   event.preventDefault();

   $.ajax({
       url: "/coc/roll",
       type: "post",
       headers: {
           "Csrf-Token": get_token()
       },
       data: {
           skill: $("#i_skill").val(),
           adv: $("input[name='adv']:checked").val()
       },
       success: function(data) {
           displayRoll( JSON.parse( data ) )
       }
   })
})

$( "#f_new_combat" ).submit( function(event){
    event.preventDefault();
    $.ajax({
        url: "/coc/newcombat",
        type: "post",
        headers: {
            "Csrf-Token": get_token()
        },
        data: {
            name: $("#i_new_combat_name").val(),
        },
        success: function(data) {
            location.reload();
        }
    })
})

function displayRoll( json ) {
    let tbody = document.getElementById( "tbody_result" );

    let row = document.createElement( "tr" );

    let col = document.createElement( "td" );
    let time = new Date( json.t ) ;
    let options = { hour: "2-digit", minute: "2-digit" };
    col.innerHTML =  time.toLocaleDateString('de', options );
    row.appendChild( col );

    col = document.createElement( "td" );
    col.innerHTML = json.r2;
    row.appendChild( col );

    col = document.createElement( "td" );
    col.innerHTML = json.r1;
    row.appendChild( col );



    col = document.createElement( "td" );
    let result;
    switch (json.s) {
        case "0":
            result = "Failure";
            break;
        case "1":
            result = "Success";
            break;
        case "2":
            result = "Hard Success";
            break;
        case "3":
            result = "Extreme Success";
            break;
        default:
            result = "";
            break;
    }
    col.innerHTML = result;
    row.appendChild( col );

    tbody.appendChild( row );

    tbody.scrollTop = tbody.scrollHeight;
}

