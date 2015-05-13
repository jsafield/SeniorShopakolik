<?php
include 'Functions.php';

$con = connect();
if (!$con) die("connection_error");

$storeid = getStoreID($con, $_REQUEST['email'], $_REQUEST['password']);
if(!$storeid)
    mFailure($con);

$sql = "SELECT * FROM Store WHERE store_id = '" . $storeid . "'";
$result = mysqli_query($con, $sql);
if(!$result)
    failure($con);

$store = parseStoreWithAllCampaigns($con, $result);
mysqli_free_result($result);
if(!$store)
    failure($con);

echo json_encode($store); 
?>