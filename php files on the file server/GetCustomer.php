<?php
include 'Functions.php';

$con = connect();
if (!$con) die("connection_error");

$userid = getUserID($con, $_REQUEST['email'], $_REQUEST['password']);
if(!$userid)
    failure($con);

$sql = "SELECT * FROM Customer WHERE user_id = '" . $userid . "'";
$result = mysqli_query($con, $sql);
if(!$result) 
    failure($con);
if(mysqli_num_rows($result) == 0)
    failure($con);
$row = mysqli_fetch_assoc($result);
$customer['user_id'] = $row['user_id'];
$customer['customer_id'] = $row['customer_id'];
$customer['name'] = $row['name'];
$customer['surname'] = $row['surname'];
$customer['locationNotification'] = ($row['locationNotification'] == 1) ? true : false;
$customer['campaignNotification'] = ($row['campaignNotification'] == 1) ? true : false;

mysqli_free_result($result);
if(!$customer)
    failure($con);

echo json_encode($customer); 
?>