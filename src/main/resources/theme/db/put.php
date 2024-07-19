<?php
if (isset($_POST["id"])) {

    header('Access-Control-Allow-Origin: *');
    header('Access-Control-Allow-Headers: *');
    header('Access-Control-Allow-Methods: PUT, DELETE, POST, GET');

    $host = $_POST['host'];
    $username = $_POST['username'];
    $password = $_POST['password'];
    $db = $_POST['db'];

    $id = $_POST['id'];
    $javaPath = $_POST['javaPath'];
    $mcPath = $_POST['mcPath'];
    $maxRam = $_POST['maxRam'];
    $jvmArgs = $_POST['jvmArgs'];

    $sql = 'UPDATE configs SET javaPath=' + $javaPath + ', mcPath=' + $mcPath + ', maxRam=' + $maxRam + ', jvmArgs=' + $jvmArgs + ' WHERE id=' + $id;
    
    $conn = mysqli_connect($host, $username, $password, $db);

    $result = mysqli_query($conn, $sql) or die(mysqli_error($conn));

    echo $result;
}
