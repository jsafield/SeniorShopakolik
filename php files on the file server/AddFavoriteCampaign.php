<?php
include 'Functions.php';

$con = connect();
if (!$con) die("connection_error");

if(!mysqli_autocommit($con, false))
    failure($con);

$customerid = getCustomerID($con, $_REQUEST['email'], $_REQUEST['password']);
if(!$customerid)
    failure($con);

$sql = "INSERT INTO FavCampaign (customer_id, campaign_id) 
        VALUES ('" . $customerid . "', '" . $_REQUEST['campaign_id'] . "')";
$result = mysqli_query($con, $sql);
if(!$result) 
    failure($con);

if(mysqli_commit($con)) {
    echo "success";
    mysqli_close($con);
} else {
    failure($con);
}
?>