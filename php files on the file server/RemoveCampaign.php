<?php
include 'Functions.php';

$con = connect();
if (!$con) die("connection_error");

if(!mysqli_autocommit($con, false))
    failure($con);

$storeid = getStoreID($con, $_REQUEST['email'], $_REQUEST['password']);
if(!$storeid)
    failure($con);
    
$sql = "DELETE FROM FavCampaign WHERE campaign_id IN (
            SELECT campaign_id FROM Campaign 
            WHERE store_id = '" . $storeid . "')";
$result = mysqli_query($con, $sql);
if(!$result)
    failure($con);

$sql = "DELETE FROM Campaign WHERE store_id = '" . $storeid 
        . "' AND campaign_id = '" . $_REQUEST['campaign_id'] . "'";
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