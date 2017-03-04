$(document).ready(function() {

  $.ajaxSetup({ cache: false });

  var selected = -1;

  function refreshImages() {

    var selectedDate = $.datepicker.formatDate('yy-mm-dd', $('#datepicker').datepicker('getDate'));
    var data = {
      selectedDate: selectedDate,
      selectedClientId: ''
    };

    $.ajax({
      dataType: "json",
      type: 'GET',
      url: 'home/getImages',
      cache: false,
      data: data,
      success: function(response) {

        $('.motion-detection').remove();

        $(response.imagesEncoded).each(function(index, element) {

          var img = $('<img>',
              {
                id: '#image_' + index,
                src: 'data:image/jpeg;base64,' + element,
                'data-index': index,
                class: 'motion-detection'
              });

          $('.image-view-container').append(img);
        });

        if (selected === -1 && response.imagesEncoded.length > 1) {
          $('#selected_image').attr('src', 'data:image/jpeg;base64,' + response.imagesEncoded[0]);
        }

        $('.motion-detection').click(function(event) {
          selected = $(event.target).data('index');
          var selectedImgSrc = $(event.target).attr('src');
          $('#selected_image').attr('src', selectedImgSrc);
        });
      }
    });

  }

  setInterval(function(){ refreshImages(); }, 3000);

  $('#datepicker').datepicker({ maxDate: new Date()});



  $('#slider-range').labeledslider({
    range: true,
    min: 0,
    max: 24,
    values: [0, 24],
    slide: function(event, ui) {
      if (ui.values[0] === ui.values[1]) {
        return false;
      }
    }
  });

  refreshImages();
});