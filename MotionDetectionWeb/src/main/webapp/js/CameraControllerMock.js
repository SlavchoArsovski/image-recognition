$(document).ready(function () {

  var config = {};

  var imageAsDataUrl = undefined;

  $('#f').on('change', function(ev) {
    var f = ev.target.files[0];
    console.log('f = ', f);
    var fr = new FileReader();

    fr.onload = function(ev2) {
      console.dir(ev2);
      imageAsDataUrl = ev2.target.result;
    };

    fr.readAsDataURL(f);
  });

  var getCommonAjaxRequest = function(urlPath, httpMethod, data, successFunction, errorFunction){
    return {
      url: 'http://localhost:8080/home/' + urlPath,
      headers: {'Access-Control-Allow-Origin': '*'},
      type: httpMethod,
      crossDomain: true,
      success: successFunction,
      error: errorFunction,
      contents: data
    };
  };

  function UploadImage() {

  }

  function continuousUpload() {

  }

  function getConfig() {
    $.ajax(getCommonAjaxRequest(
      'getConfig',
      'GET'
    )).then(function (data) {
      console.log('then... data: ', data);
      config = data;
    });
  }

  $("#getConfig").click(function () {
    getConfig();
  });

  $("#sendRed").click(function () {

    // var formData = new FormData();
    //formData.append("image", imageAsDataUrl);
    var xhr = new XMLHttpRequest();
    xhr.open("POST", "http://localhost:8080/home/uploadBase64");
    xhr.send(imageAsDataUrl);
    //$.post("http://localhost:8080/home/upload", imageAsDataUrl);
    /*$.ajax(getCommonAjaxRequest(
      'uploadBase64',
      'POST'
    )).then(function (data) {
      console.log("uploaded");
    })*/
  })
});