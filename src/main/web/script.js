
$( document ).ready(function() {
    var map;

//DB switch
$('#DBswitch').change(function () {
    if ($(this).is(':checked')) {
        alert('checked');
    } else {
        alert('not checked');
    }
});


//Clear All inputs except selected
$('#inputDiv').contents().find(":checkbox").bind('change', function(){
    var selector = this.value;      
    $('input[type="checkbox"]').not(this).prop("checked", false);
    if(selector == "geo"){
        $('#location').prop("disabled", true); 
        $('#book').prop("disabled", true); 
        $('#author').prop("disabled", true); 
        $('#lat').prop("disabled", false); 
        $('#lng').prop("disabled", false); 
    }else{
        $('input[type=text]').each(function(){
            if(this.id == selector){
                $(this).prop("disabled", false); 
            }else{
                $(this).prop("disabled", true); 
                $(this).val(""); 
            }
        })  
    }     
});


$("#searchBtn").click(function(){
    $("#resultDiv").show();
    $('input[type=checkbox]').each(function(){
        if($(this).is(":checked")){
            submit(this.value);
        }
    });
});

//Add markers 
function loadResults (data) {
  var items, markers_data = [];
  if (data.venues.length > 0) {
    items = data.venues;

    for (var i = 0; i < items.length; i++) {
      var item = items[i];

      if (item.location.lat != undefined && item.location.lng != undefined) {
        markers_data.push({
          lat : item.location.lat,
          lng : item.location.lng,
          title : item.name,
      });
    }
}
}

map.addMarkers(markers_data);
}

//Initialize map
map = new GMaps({
    el: '#resultMap',
    lat: 55.676098,
    lng: 12.568337,
    zoomControl : true,
    zoomControlOpt: {
        style : 'SMALL',
        position: 'TOP_LEFT'
    },
    panControl : false,
    streetViewControl : false,
    mapTypeControl: false,
    overviewMapControl: false
});

/*// Define user location
GMaps.geolocate({
  success: function(position) {
    map.setCenter(position.coords.latitude, position.coords.longitude);


        // Creating marker of user location
        map.addMarker({
          lat: position.coords.latitude,
          lng: position.coords.longitude,
          title: 'Lima',
          click: function(e) {
            alert('You clicked in this marker');
        },
        infoWindow: {
          content: '<p>You are here!</p>'
      }
  });
    },
    error: function(error) {
        alert('Geolocation failed: '+error.message);
    },
    not_supported: function() {
        alert("Your browser does not support geolocation");
    }
});*/

//AJAX CALL
function submit(url){

   //TODO:
   //FORMAT INPUTDATA TO JSON FORMAT
   var inputData = $('#inputDiv :input').serialize(); 

   console.log(inputData);

   $.ajax({
    url: "api/something/" + url,
    type: "POST",
    cache: false,
    data: inputData,
    dataType: "json",
})
   .done(function (data) {
    if (url == "book"){
        //TODO:
        //APPLY RESULT TO MAP
    } else if (url == "author"){
        data.each(function (index) {
            $("#resultList").add('<li>' + $(this) + '</li>');
        });
        //TODO:
        //APPLY RESULT TO MAP
    } else {
        data.each(function (index) {
            $("#resultList").add('<li>' + $(this) + '</li>');
        });
    }
})
   .fail(function (error) {


   });
}

});


$(document).ready(function(){


}); 