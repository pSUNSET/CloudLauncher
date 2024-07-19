<?php
if (isset($_POST['id'])) {
    
    header('Access-Control-Allow-Origin: *');
    header('Access-Control-Allow-Headers: *');
    header('Access-Control-Allow-Methods: PUT, DELETE, POST, GET');

    $host = $_POST['host'];
    $username = $_POST['username'];
    $password = $_POST['password'];
    $db = $_POST['db'];

    $id = $_POST['id'];
        
    $sql = 'SELECT * FROM configs WHERE id=' + $id;
        
    $conn = mysqli_connect($host, $username, $password, $db);
    
    $result = mysqli_query($conn, $sql) or die(mysqli_error($conn));

    [[$data]] = $result;

    echo json_encode($data);
}
        