$(document).ready(function() {
    $("#closedescription").hide();
    $(".loader").hide();

    $("#intro").fadeIn(1000).delay(2000).fadeOut(1000, function() {
        $("#launch").fadeIn(1000);
    });
});

function installStart() {
    $("#launch").hide();
    $(".loader").fadeIn(1000);
    $("#load").fadeIn(1000);
}

function installFinished() {
    $(".loader").fadeOut(1000);
    $("#load").fadeOut(1000, function() {
        $("#done").fadeIn(1000).delay(2000).fadeOut(1000, function() {
            $("#running").fadeIn(1000);
            feedback.launch();
        });
    });
}

function clientclose() {
    $("#running").fadeOut(1000, function() {
        $("#clientdown").fadeIn(1000).delay(2000).fadeOut(1000, function() {
            $("#launch").fadeIn(1000);
        });
    });
}

// check whether a object is visible
// $("div").is(":visible")