var guiComponents = {
  sliderTimeRange: '#slider-range',
  selectedTimeRange: '#selectedTimeRange',
  datePicker: '#datepicker',
  selectedDate: '#selectedDate',
  motionDetectionImage: '.motion-detection',
  imageViewContainer: '.image-view-container',
  selectClientDropDown: '#selectClientDropDown'
};

// initialize state
var state = {
  pageNumber: 0,
  numberOfPages: 1,
  lastUpdate: '',
  filterChanged: false
};

$(document).ready(function() {

  $.ajaxSetup({ cache: false });

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
        if (response.lastUpdate !== state.lastUpdate) {
          state.pageNumber = 0;
          state.lastUpdate = response.lastUpdate;

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
      pageNumber: state.pageNumber,
      lastUpdate: state.lastUpdate
    };

    $.ajax({
      dataType: "json",
      type: 'GET',
      url: motionDetectionApp.conf.contextPath + '/home/getImages',
      cache: false,
      data: data,
      success: function(response) {

        if (response.lastUpdate !== state.lastUpdate || state.filterChanged === true) {
          $(guiComponents.motionDetectionImage).remove();
          state.pageNumber = 0;
          state.lastUpdate = response.lastUpdate;
          state.filterChanged = false;
        }

        state.numberOfPages = response.numberOfPages;

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

        $(guiComponents.motionDetectionImage).click(function(event) {

          var productOverlay = $('.product-image-overlay');
          var productOverlayImage = $('.product-image-overlay img');

          var productImageSource = $(this).attr('src');

          productOverlayImage.attr('src', productImageSource);
          productOverlay.fadeIn(100);
          $('body').css('overflow', 'hidden');

          productOverlay.click(function() {
            productOverlay.fadeOut(100);
            $('body').css('overflow', 'auto');
          });

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
      filterChange();
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
      filterChange();
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
    filterChange();
  });

  $('.image-view-container').on('scroll', function(event) {

    if($(this).scrollTop() + $(this).innerHeight() + 1 >= $(this)[0].scrollHeight) {
      if (state.pageNumber < state.numberOfPages) {
        state.pageNumber++;
        refreshImages();
      }
    }
  });

  function filterChange() {

    state.pageNumber = 0;
    state.filterChanged = true;

    refreshImages();
  }

  refreshImages();
});