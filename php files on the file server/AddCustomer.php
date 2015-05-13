<?php
include 'Functions.php';

$con = connect();
if (!$con) die("connection_error");

if(!mysqli_autocommit($con, false))
    failure($con);

$sql = "INSERT INTO User (email, password, confirmation) 
        VALUES ('" . $_REQUEST['email'] . "', '" . $_REQUEST['password'] 
                . "', '1')"; // TODO 1 will be 0
$result = mysqli_query($con, $sql);

if(!$result)
    failure($con);

$user_id = mysqli_insert_id($con);

$sql = "INSERT INTO Customer (user_id, name, surname, locationNotification, campaignNotification) 
        VALUES ('" . $user_id . "', '" . $_REQUEST['name'] . "', '" . $_REQUEST['surname'] . "', '" 
        . $_REQUEST['locationNotification'] . "', '" . $_REQUEST['campaignNotification'] . "')";
$result = mysqli_query($con, $sql);

if(!$result)
   failure($con);

if (mysqli_commit($con)) {
    echo 'success';
    mysqli_close($con);
} else {
    failure($con);
}
?>