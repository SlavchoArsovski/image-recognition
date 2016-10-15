$(document).ready(function () {

    $.ajaxSetup({cache: false});

    refreshImage();

    function refreshImage() {

        $.ajax({
            dataType: "json",
            type: 'GET',
            url: 'home/getImage',
            cache: false,
            data: {},
            success: function (response) {
                $('#imageRecognition').attr('src', 'data:image/jpeg;base64,' + response.data);
            }
        });
        
    }

    setInterval(function(){ refreshImage(); }, 1000);
});