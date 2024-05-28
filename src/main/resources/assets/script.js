$(document).ready(function() {
    $("#intro").fadeIn(1000).delay(2000).fadeOut(1000, function() {
        $("#launch").fadeIn(1000);
        $("#repair").fadeIn(1000);
        $("#maxram").fadeIn(1000);
    });
});

function repairStart() {
    $("#launch").hide();
    $("#repair").hide();

    $(".loader").fadeIn(1000);
    $("#load").fadeIn(1000);
    $("#index").show();
}

function repairFinished() {
    $("#index").hide();
    $(".loader").fadeOut(1000);
    $("#load").fadeOut(1000, function() {
        $("#done").fadeIn(1000).fadeOut(1000, function() {
            $("#launch").fadeIn(1000);
            $("#repair").fadeIn(1000);
        });
    });
}

function installStart() {
    $("#launch").hide();
    $("#repair").hide();

    $(".loader").fadeIn(1000);
    $("#load").fadeIn(1000);
    $("#index").show();
}

function setInstallIndex(s){
    $("#index").text(s);
}

function installFinished() {
    $("#index").hide();
    $(".loader").fadeOut(1000);
    $("#load").fadeOut(1000, function() {
        $("#done").fadeIn(1000).fadeOut(1000, function() {
            $("#running").fadeIn(1000);
            feedback.launch();
        });
    });
}

function clientStart() {

}

function clientClosed() {
    $("#running").fadeOut(1000, function() {
        $("#clientdown").fadeIn(1000).delay(2000).fadeOut(1000, function() {
            $("#launch").fadeIn(1000);
        });
    });
}

// check whether a object is visible
// $("div").is(":visible")