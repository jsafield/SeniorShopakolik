<?php
include 'Functions.php';

$con = connect();
if (!$con) die("connection_error");

if(!mysqli_autocommit($con, false))
    failure($con);
    
$userid = getUserID($con, $_REQUEST['currentemail'], $_REQUEST['currentpassword']);
if(!$userid)
    failure($con);

$sql = "UPDATE Customer SET name = '" . $_REQUEST['name'] . "', surname = '" 
    . $_REQUEST['surname'] . "' WHERE user_id = '" . $userid . "';";
$result = mysqli_query($con, $sql);

if(!$result)
    failure($con);

$sql = "UPDATE User SET email = '" . $_REQUEST['email'] . "', password = '" 
    . $_REQUEST['password'] . "' WHERE user_id = '" . $userid . "';";
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