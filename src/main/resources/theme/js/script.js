var id = getUserConfigsId();

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
        type: "GET",
        url: "https://psunsetmc.onrender.com/cloudmc/api/configs/" + id,
        dataType: "json",
        success: function (res) {
            let [data] = res;

            $("#java-path-input").val(data.javaPath);
            $("#mc-path-input").val(data.mcPath);
            $("#max-ram-input").val(data.maxRam);
            $("#jvm-args-input").val(data.jvmArgs);
            $("#version-select").val(data.selectVersion).change();

            console.log(data);
        }
    });
}
function saveSettingConfigs(){
    let jP = $("#java-path-input").val() == null ? "" : $("#java-path-input").val();
    let mP = $("#mc-path-input").val() == null ? "" : $("#mc-path-input").val();
    let mR = $("#max-ram-input").val() == null ? "": $("#max-ram-input").val();
    let jA = $("#jvm-args-input").val() == null ? "" : $("#jvm-args-input").val();
    let sV = $("#version-select").val() == null ? "" : $("#version-select").val();
    $.ajax({
        type: "PUT",
        url: "https://psunsetmc.onrender.com/cloudmc/api/configs/" + id,
        data: JSON.stringify({
            "javaPath": jP,
            "mcPath": mP,
            "maxRam": mR,
            "jvmArgs": jA,
            "lastGameVersion": "",
            "selectVersion": sV,
        }),
        contentType: 'application/json; charset=utf-8',
        dataType: "json",
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