var guiComponents = {
  sliderTimeRange: '#slider-range',
  selectedTimeRange: '#selectedTimeRange',
  datePicker: '#datepicker',
  selectedDate: '#selectedDate',
  motionDetectionImage: '.motion-detection',
  selectedImage: '#selected_image',
  imageViewContainer: '.image-view-container',
  selectClientDropDown: '#selectClientDropDown'
};

$(document).ready(function() {

  $.ajaxSetup({ cache: false });

  var selected = -1;
  var pageNumber = 0;
  var lastUpdate = '';
  var numberOfPages = 1;

  function checkLastUpdate() {

    var clientId = $(guiComponents.selectClientDropDown).find(":selected").val();

    var data = {
      clientId: clientId
    };

    $.ajax({
      dataType: "json",
      type: 'GET',
      url: motionDetectionApp.conf.contextPath + '/home/getLastUpdate',
      cache: false,
      data: data,
      success: function(response) {
        if (response.lastUpdate !== lastUpdate) {
          pageNumber = 0;
          lastUpdate = response.lastUpdate;

          $(guiComponents.motionDetectionImage).remove();

          refreshImages();
        }
      }
    });

  }

  function refreshImages() {

    var date = $.datepicker.formatDate('yy-mm-dd', $(guiComponents.datePicker).datepicker('getDate'));

    var sliderValues = $(guiComponents.sliderTimeRange).labeledslider('values');
    var timeFrom = sliderValues[0];
    if (timeFrom < 10) {
      timeFrom = '0' + timeFrom;
    }
    var timeTo = sliderValues[1];
    if (timeTo < 10) {
      timeTo = '0' + timeTo;
    }

    var clientId = $(guiComponents.selectClientDropDown).find(":selected").val();
    var data = {
      date: date,
      timeFrom: timeFrom,
      timeTo: timeTo,
      clientId: clientId,
      pageNumber: pageNumber,
      lastUpdate: lastUpdate
    };

    $.ajax({
      dataType: "json",
      type: 'GET',
      url: motionDetectionApp.conf.contextPath + '/home/getImages',
      cache: false,
      data: data,
      success: function(response) {

        if (response.lastUpdate !== lastUpdate) {
          $(guiComponents.motionDetectionImage).remove();
          pageNumber = 0;
          lastUpdate = response.lastUpdate;
        }

        numberOfPages = response.numberOfPages;

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

  setInterval(function(){ checkLastUpdate(); }, 1000);

  $(guiComponents.datePicker).datepicker({
    maxDate: new Date(),
    onSelect: function(dateText) {
      var dateFormatted = $.datepicker.formatDate('yy-mm-dd', $(guiComponents.datePicker).datepicker('getDate'));
      $(guiComponents.selectedDate).text(dateFormatted);
      pageNumber = 0;
      refreshImages();
    }
  });

  $(guiComponents.selectedDate).text(
      $.datepicker.formatDate('yy-mm-dd', $(guiComponents.datePicker).datepicker('getDate')));

  $(guiComponents.sliderTimeRange).labeledslider({
    range: true,
    min: 0,
    max: 24,
    values: [0, 24],
    slide: function(event, ui) {
      if (ui.values[0] === ui.values[1]) {
        return false;
      }

      var timeFrom = ui.values[0];
      if (timeFrom < 10) {
        timeFrom = '0' + timeFrom;
      }
      var timeTo = ui.values[1];
      if (timeTo < 10) {
        timeTo = '0' + timeTo;
      }
      $(guiComponents.selectedTimeRange).text(timeFrom + ' - ' + timeTo + ' hour');

    },
    stop: function(event, ui) {
      pageNumber = 0;
      refreshImages();
    }
  });

  var sliderValues = $(guiComponents.sliderTimeRange).labeledslider('values');
  var timeFrom = sliderValues[0];
  if (timeFrom < 10) {
    timeFrom = '0' + timeFrom;
  }
  var timeTo = sliderValues[1];
  if (timeTo < 10) {
    timeTo = '0' + timeTo;
  }

  $(guiComponents.selectedTimeRange).text(timeFrom + ' - ' + timeTo + ' hour');

  $(guiComponents.selectClientDropDown).change(function() {
    pageNumber = 0;
    refreshImages();
  });

  $('.image-view-container').on('scroll', function(event) {

    if($(this).scrollTop() + $(this).innerHeight() + 1 >= $(this)[0].scrollHeight) {
      if (pageNumber < numberOfPages)
      pageNumber++;
      refreshImages();
    }
  });

  refreshImages();
});