$(document).ready(function () {

    $.ajaxSetup({cache: false});

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
            }
        });
        
    }

    setInterval(function(){ refreshImage(); }, 1000);
});