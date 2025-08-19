function loadGetMsg() {
    let nameVar = document.getElementById("textInputGet").value;
    const xhttp = new XMLHttpRequest();
    xhttp.onload = function() {
        document.getElementById("getResult").innerHTML = this.responseText;
    }
    xhttp.open("GET", "/app?name=" + nameVar);
    xhttp.send();
}

function loadPostMsg() {
    let nameVar = document.getElementById("textInputPost").value;
    let url = "/app/hello?name=" + nameVar;

    fetch(url, {method: 'POST'})
        .then(response => response.text())
        .then(text => document.getElementById("postResult").innerHTML = text); 
}