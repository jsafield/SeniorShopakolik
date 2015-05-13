<?php
include 'Functions.php';

$con = connect();
if (!$con) die("connection_error");

if(!mysqli_autocommit($con, false))
    failure($con);

$customerid = getCustomerID($con, $_REQUEST['email'], $_REQUEST['password']);
if(!$customerid)
    failure($con);

$sql = "DELETE FROM FavCampaign 
        WHERE customer_id = '" . $customerid . "' AND campaign_id = '" 
            . $_REQUEST['campaign_id'] . "'";
$result = mysqli_query($con, $sql);
if (!$result)
    failure($con);
if (mysqli_affected_rows($con) == 0)
    failure($con);

if (mysqli_commit($con)) {
    echo 'success';
    mysqli_close($con);
} else {
    failure($con);
}
?>