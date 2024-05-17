$(document).ready(function(){

    $("#intro").fadeIn(1000).delay(2000).fadeOut(1000, function(){
        
        $("#load").fadeIn(1000);

    });

});

function finished(){

    $("#load").fadeOut(function(){
        $(".loader").fadeOut();
        $("#done").fadeIn(1000).delay(2000).fadeOut(1000, function(){
            $("#close").fadeIn(1000)
        });
    });

}