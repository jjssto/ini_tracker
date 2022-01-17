function load_chars() {
    const Http = new XMLHttpRequest();
    Http.onload = function () {
        let list = JSON.parse(this.responseText);
        let tbody = document.getElementById("char_update_tbody")
        let tr_form = document.getElementById("tr_form")
        for (let i in list) {
            let id = list[i].id;
            let row = document.createElement("tr");
            let char = list[i];
            let cell = document.createElement("td");
            cell.innerHTML = char["name"];
            row.appendChild(cell);

            cell = document.createElement("td");
            cell.innerHTML = char["ini"];
            row.appendChild(cell);

            cell = document.createElement("td");
            cell.innerHTML = char["reaction"];
            row.appendChild(cell);

            cell = document.createElement("td");
            cell.innerHTML = char["intuition"];
            row.appendChild(cell);

            cell = document.createElement("td");
            cell.innerHTML = char["sBoxes"];
            row.appendChild(cell);

            cell = document.createElement("td");
            cell.innerHTML = char["pBoxes"];
            row.appendChild(cell);

            tbody.insertBefore(row, tr_form);
        }
    }
    Http.open("GET", "/getchars");
    Http.send();
}

load_chars()