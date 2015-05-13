<?php
include 'Functions.php';

function finish($con, $value) {
    echo $value;
    mysqli_close($con);
    exit();
}

$con = connect();
if (!$con) die("connection_error");

$sql = "SELECT user_id FROM User WHERE email = '" . $_REQUEST['email'] 
    . "' AND password = '" . $_REQUEST['password'] . "' AND confirmation = '1'";
$result = mysqli_query($con, $sql);

if(!$result)
    finish($con, "error");
if(mysqli_num_rows($result) == 0)
    finish($con, "non_user");
$row = mysqli_fetch_array($result);
$userid = $row['user_id'];
mysqli_free_result($result);

$sql = "SELECT * FROM Customer WHERE user_id = '" . $userid . "'";
$result = mysqli_query($con, $sql);

if(!$result)
    finish($con, "error");
if(mysqli_num_rows($result) > 0)
    finish($con, "customer");

$sql = "SELECT * FROM Store WHERE user_id = '" . $userid . "'";
$result = mysqli_query($con, $sql);

if(!$result)
    finish($con, "error");
if(mysqli_num_rows($result) > 0)
    finish($con, "store");

finish($con, "error");
?>