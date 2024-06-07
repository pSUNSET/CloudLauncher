$(document).ready(function() {
    $("#intro").fadeIn(1000).fadeOut(1000, function() {
        $("#launch").fadeIn(1000);
        $("#setting").fadeIn(1000);
    });
});

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
        $("#clientDown").fadeIn(1000).fadeOut(1000, function() {
            $("#launch").fadeIn(1000);
            $("#setting").fadeIn(1000);
        });
    });
}

function setting() {
    $("#setting").hide();
    $("#launch").hide();

    getSettingConfigs();
    getVersionList();

    $(".settings").fadeIn(200);

    $("#javaPath").fadeIn(200);
    $("#mcPath").fadeIn(200);
    $("#maxRam").fadeIn(200);
    $("#jvmArgs").fadeIn(200);
    $("#repair").fadeIn(200);
    $("#version").fadeIn(200);
    $("#settingClose").fadeIn(200);

}

function settingClose() {
    $("#javaPath").hide();
    $("#mcPath").hide();
    $("#maxRam").hide();
    $("#jvmArgs").hide();
    $("#repair").hide();
    $("#version").hide();
    $("#settingClose").hide();

    $(".settings").hide();

    $("#setting").fadeIn(200);
    $("#launch").fadeIn(200);

    saveSettingConfigs();
}

function config(key, value) {
    if (value == null || value == "") {
        feedback.setConfig(key, value);

    } else {
        feedback.setConfig(key, "null");
    }
}

function config(key) {
    return feedback.getConfig(key);
}

function saveSettingConfigs(){
    config("javaPath", $("#javaPathInput").val());
    config("mcPath", $("#mcPathInput").val());
    config("maxRam", $("#maxRamInput").val());
    config("jvmArgs", $("#jvmArgsInput").val());
}

function getSettingConfigs(){
    $("#javaPathInput").val(config("javaPath"));
    $("#mcPathInput").val(config("mcPath"));
    $("#maxRamInput").val(config("maxRam"));
    $("#jvmArgsInput").val(config("jvmArgs"));
}

function getVersionList(){
    $.getJSON( "assets/mc-version.json", function( data ) {

        $.each(data, function (key, value) {

            $('#versionSelect').append('<option value=\"' + key + '\">' + value + '</option>');

            console.log($("#versionSelect"));

        })

    });
}

// check whether a object is visible
// $("div").is(":visible")