$(document).ready(function() {
    $("#intro").fadeIn(1000);
    feedback.init();
});

function initFinished(){
    $("#intro").fadeOut(1000, () => {
        $("#launch").fadeIn(1000);
        $("#setting").fadeIn(1000);
    });
    
    // setting version select list
    getVersionList();
}

function repairStart() {
    $("#launch").hide();

    $(".loader").fadeIn(1000);
    $("#load").fadeIn(1000);
    $('#index').show();
}

function repairFinished() {
    $('#index').hide();
    $(".loader").fadeOut(1000);
    $("#load").fadeOut(1000, function() {
        $("#rDone").fadeIn(1000).fadeOut(1000, function() {
            if (!$(".settings").is(":visible")) {
                $("#launch").fadeIn(1000);
            }
        });
    });
}

function installStart() {
    $("#launch").hide();
    $("#setting").hide();

    $(".loader").fadeIn(1000);
    $("#load").fadeIn(1000);
    $('#index').show();
}

function setInstallIndex(s){
    $('#index').text(s);
}

function installFinished() {
    $('#index').hide();

    $(".loader").fadeOut(1000);
    $("#load").fadeOut(1000, function() {
        $("#done").fadeIn(1000).fadeOut(1000, function() {
            $("#running").fadeIn(1000);
            feedback.launch();
        });
    });
}

function clientClosed() {
    $("#running").fadeOut(1000, function() {
        $("#client-down").fadeIn(1000).fadeOut(1000, function() {
            $("#launch").fadeIn(1000);
            $("#setting").fadeIn(1000);
        });
    });
}

function setting() {
    $("#setting").hide();
    $("#launch").hide();

    getSettingConfigs();

    $(".settings").fadeIn(200);
    $(".setting").fadeIn(200);
}

function settingClose() {
    $(".setting").hide();
    $(".settings").hide();

    $("#setting").fadeIn(200);
    $("#launch").fadeIn(200);

    saveSettingConfigs();
}

function saveSettingConfigs(){
    
}

function getSettingConfigs(){
    
}

function getVersionList(){
    $.getJSON( "./data/mc-version.json", function( data ) {

        $.each(data, function (key, value) {

            $('#version-select').append('<option value=\"' + value + '\">' + key + '</option>');

            console.log($("#version-select"));

        })

    });
}

// check whether a object is visible
// $("div").is(":visible")