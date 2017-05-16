
$( document ).ready(function() {
  var map;

//DB switch
/*$('#DBswitch').change(function () {
  if ($(this).is(':checked')) {
    alert('checked');
  } else {
    alert('not checked');
  }
});*/


//Clear All inputs except selected
$('#inputDiv').contents().find(":checkbox").bind('change', function(){
  var selector = this.value;
  if($(this).is(":checked")){    
    $('input[type="checkbox"]').not(this).prop("checked", false);
    if(selector == "geo"){
      $('#location').prop("disabled", true); 
      $('#book').prop("disabled", true); 
      $('#author').prop("disabled", true); 
      $('#lat').prop("disabled", false); 
      $('#lng').prop("disabled", false); 
      $('#location').val(""); 
      $('#book').val(""); ; 
      $('#author').val(""); ; 
    }else{
      $('input[type=text]').each(function(){
        if(this.id == selector){
          $(this).prop("disabled", false); 
        }else{
          $(this).prop("disabled", true); 
          $(this).val(""); 
        }
      });  
    }
  } else {
    $('input[type=text]').each(function(){
      $(this).prop("disabled", true); 
      $(this).val(""); 
    });
  }     
});

//Click to submit
$("#searchBtn").click(function(){
  $("#resultDiv").show();
  $('input[type=checkbox]').each(function(){
    if($(this).is(":checked")){
      submit(this.value);
    }
  });
});

//Add markers 
function loadMarkers (data) {
  console.log(data);
   $.each(data, function(i, value) {
    console.log("looping:")
    console.log(value.lat + " : " + value.lng);
        map.addMarker({
          lat: value.lat,
          lng: value.lng,
          title: value.city
        });
    });
}

//Initialize map
map = new GMaps({
  el: '#resultMap',
  lat: 55.676098,
  lng: 12.568337,
  zoom: 2,
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

//Submits search to backend
function submit(category){

  //TEST CODE
  var testData = [{
    "city": "copenhagen",
    "lat": "55.676098",
    "lng": "12.568337"
  },{
    "city": "stockholm",
    "lat": "59.3289",
    "lng": "18.0649"
  },{
    "city": "paris",
    "lat": "48.8566",
    "lng": "2.3522"
  }];
  loadMarkers(testData);
  $.each(testData, function(i, value) {
          $("#resultList").append($("<li>").text(JSON.stringify(value)));
        });

  var db = "";
  var path = "limitless-oasis-66630.herokuapp.com/";
  var inputData = {};

  //Setting path to match REST-service
  if($('#DBswitch').is(":checked")){
    db = "neo4j";
  }else{
    db = "mysql";
  }   
  path += db + "/" + category;

  //Building data-object
  if(category == "geo"){
    inputData["lat"] = $("#lat").val();
    inputData["lng"] = $("#lng").val();
  } else {
    inputData[category] = $("#" + category).val(); 
  }

  //AJAX call
  $.ajax({
    url: path,
    type: "GET",
    cache: false,
    data: JSON.stringify(inputData),
    dataType: "json",
  })
    .done(function (data) {

      ///DEBUGGING///
      console.log("SUCCESS")
      console.log("Returned data:");
      console.log(data);
      ///////////////
      if (category == "book"){
        loadMarkers(data);
      } else if (category == "author"){
        $.each(data.books, function(i, value) {
          $("#resultList").append($("<li>").text(JSON.stringify(value)));
        });
        loadMarkers(data);
      } else {
        $.each(data, function(i, value) {
          $("#resultList").append($("<li>").text(JSON.stringify(value)));
        });
      }
    })
    .fail(function (error) {
      //TODO:
      //DISPLAY ERROR MSG TO USER
    });
  }
});
