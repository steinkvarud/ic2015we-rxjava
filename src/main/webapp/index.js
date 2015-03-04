var map = L.map('map').setView([25.505, -0.09], 2);
var ws = new WebSocket("ws://localhost:8080/events/");

ws.onopen = function() {
    ws.send("Hello Server");
};

ws.onmessage = function(evt) {
    var json = JSON.parse(evt.data);
    console.log(json);
    L.circleMarker([json.lat, json.lng], {radius: 5, color:json.color})
        .bindPopup("<b>".concat(json.name,"</b>"))
        .addTo(map);
};

ws.onclose = function() {
};

ws.onerror = function(err) {
};

L.tileLayer('https://{s}.tiles.mapbox.com/v3/{id}/{z}/{x}/{y}.png', {
    maxZoom: 18,
    attribution: 'Map data &copy; <a href="http://openstreetmap.org">OpenStreetMap</a> contributors, ' +
    '<a href="http://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, ' +
    'Imagery © <a href="http://mapbox.com">Mapbox</a>',
    id: 'examples.map-i875mjb7'
}).addTo(map);