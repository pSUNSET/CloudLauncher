$(document).ready(function() {
    $("#intro").fadeIn(1000);
    feedback.init();
});

function initFinished(){
    $("#intro").fadeOut(1000, function() {
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
        $("#repair-done").fadeIn(1000).fadeOut(1000, function() {
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

function getSettingConfigs(){
    $.ajax({
        type: "POST",
        url: "./db/get.php",
        data: {
            "host": "bfrxojhf2xfiyhkhhdym-mysql.services.clever-cloud.com",
            "username": "uto6xl4qzwme1laj",
            "password": "5o7ZhOvwwUIh7FNdiANa",
            "db": "bfrxojhf2xfiyhkhhdym",
            "id": getUserConfigsId()
        },
        dataType: 'json',
        success: function (res) {
            $("#java-path-input").val(res.javaPath);
            $("#mc-path-input").val(res.mcPath);
            $("#max-ram-input").val(res.maxRam);
            $("#jvm-args-input").val(res.jvmArgs);
            $("#version-select").val(res.lastGameVersion).change();

            console.log(res);
        }
    });
}
function saveSettingConfigs(){
    $.ajax({
        type: "POST",
        url: "./db/put.php",
        data: {
            "host": "bfrxojhf2xfiyhkhhdym-mysql.services.clever-cloud.com",
            "username": "uto6xl4qzwme1laj",
            "password": "5o7ZhOvwwUIh7FNdiANa",
            "db": "bfrxojhf2xfiyhkhhdym",
            "id": getUserConfigsId(),
            "javaPath": ($("#java-path-input").val() == null? "" : $("#java-path-input").val()),
            "mcPath": ($("#mc-path-input").val() == null? "" : $("#mc-path-input").val()),
            "maxRam": ($("#max-ram-input").val() == null? "" : $("#max-ram-input").val()),
            "jvmArgs": ($("#jvm-args-input").val() == null? "" : $("#jvm-args-input").val())
            // No "lastGameVersion"
        },
        dataType: 'json',
        success: function (res) {
            console.log(res);
        }
    });
}

function getUserConfigsId(){
    return 1;
}

function getVersionList(){
    $.getJSON( "./data/mc-version.json", function( data ) {
        $.each(data, function (key, value) {
            $('#version-select').append('<option value=\"' + value + '\">' + key + '</option>');
        })
    });
}

// check whether a object is visible
// $("tag").is(":visible")