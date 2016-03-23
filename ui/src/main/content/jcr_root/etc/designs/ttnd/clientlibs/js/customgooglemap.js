var latLongJson = JSON.parse("[" + $("input[name='mapConfigArray']").val() + "]");
var mapType = $("input[name='mapType']").val();
var zoom = $("input[name='zoom']").val();

var mapOptions,map;

function initialize() {
    mapOptions = {
        zoom: parseInt(zoom)||5,
        mapTypeId: mapType||"roadmap"
    }

    var mapCanvas = document.getElementById('map-canvas');
    map = new google.maps.Map(mapCanvas);
    currentLocation();

    for (var i = 0; i < latLongJson.length; i++) {
        var coordsObj = latLongJson[i];
        var latLngObj = new google.maps.LatLng(coordsObj.latitude, coordsObj.longitude);
        var marker = new google.maps.Circle({
            radius: 20000,
            strokeColor: coordsObj.color,
            fillColor: coordsObj.color,
            center: latLngObj,
            map: map
        });

    }
}

function currentLocation() {
    navigator.geolocation.getCurrentPosition(function (position) {
        var geolocate = new google.maps.LatLng(position.coords.latitude, position.coords.longitude);
        mapOptions.center = geolocate;
        map.setOptions(mapOptions);
        var infowindow = new google.maps.InfoWindow({
            map: map,
            position: geolocate,
            content: 'Current location:<br/>' +
            ' Latitude: ' + position.coords.latitude + '<br/>' +
            ' Longitude: ' + position.coords.longitude
        });
        map.setCenter(geolocate);
    });
}

jQuery(document).ready(function () {
    initialize();
});
