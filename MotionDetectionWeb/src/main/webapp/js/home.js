var guiComponents = {
  sliderTimeRange: '#slider-range',
  selectedTimeRange: '#selectedTimeRange',
  datePicker: '#datepicker',
  selectedDate: '#selectedDate',
  motionDetectionImage: '.motion-detection',
  selectedImage: '#selected_image',
  imageViewContainer: '.image-view-container'
};

$(document).ready(function() {

  $.ajaxSetup({ cache: false });

  var selected = -1;

  function refreshImages() {

    var selectedDate = $.datepicker.formatDate('yy-mm-dd', $(guiComponents.datePicker).datepicker('getDate'));

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

        $(guiComponents.motionDetectionImage).remove();

        $(response.imagesEncoded).each(function(index, element) {

          var img = $('<img>',
              {
                id: '#image_' + index,
                src: 'data:image/jpeg;base64,' + element,
                'data-index': index,
                class: 'motion-detection'
              });

          $(guiComponents.imageViewContainer).append(img);
        });

        if (selected === -1 && response.imagesEncoded.length > 1) {
          $(guiComponents.selectedImage).attr('src', 'data:image/jpeg;base64,' + response.imagesEncoded[0]);
        }

        $(guiComponents.motionDetectionImage).click(function(event) {
          selected = $(event.target).data('index');
          var selectedImgSrc = $(event.target).attr('src');
          $(guiComponents.selectedImage).attr('src', selectedImgSrc);
        });
      }
    });

  }

  setInterval(function(){ refreshImages(); }, 3000);

  $(guiComponents.datePicker).datepicker({
    maxDate: new Date(),
    onSelect: function(dateText) {
      $(guiComponents.selectedDate).text(this.value);
      refreshImages();
    }
  });

  $(guiComponents.selectedDate).text($(guiComponents.datePicker).val());



  $(guiComponents.sliderTimeRange).labeledslider({
    range: true,
    min: 0,
    max: 24,
    values: [0, 24],
    slide: function(event, ui) {
      if (ui.values[0] === ui.values[1]) {
        return false;
      }

      $(guiComponents.selectedTimeRange).text(ui.values[0] + ' - ' + ui.values[1]);

    },
    stop: function(event, ui) {
      refreshImages();
    }
  });

  var sliderValues = $(guiComponents.sliderTimeRange).labeledslider('values');
  $(guiComponents.selectedTimeRange).text(sliderValues[0] + ' - ' + sliderValues[1]);

  refreshImages();
});