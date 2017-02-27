$(document).ready(function () {

    $.ajaxSetup({cache: false});

    var selected = -1;

    refreshImage();

    function refreshImage() {

        $.ajax({
            dataType: "json",
            type: 'GET',
            url: 'home/getImages',
            cache: false,
            data: {},
            success: function (response) {
                $(response.imagesEncoded).each(function(index, element) {
                    $('#image_' + index).attr('src', 'data:image/jpeg;base64,' + element);
                });

                if (selected === -1 && response.imagesEncoded.length > 1) {
                    $('#selected_image').attr('src', 'data:image/jpeg;base64,' + response.imagesEncoded[0]);
                }
            }
        });
        
    }

    setInterval(function(){ refreshImage(); }, 1000);

    $('.motion-detection').click(function(event) {
        selected = $(event.target).data('index');
        var selectedImgSrc = $(event.target).attr('src');
        $('#selected_image').attr('src', selectedImgSrc);
    });

    $('#datepicker').datepicker();
});