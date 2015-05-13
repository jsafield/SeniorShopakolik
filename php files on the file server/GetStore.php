<?php
include 'Functions.php';

$con = connect();
if (!$con) die("connection_error");

if (!isUser($con, $_REQUEST['email'], $_REQUEST['password']))
    failure($con);

$sql = "SELECT * FROM Store 
        WHERE store_id = '" . $_REQUEST['store_id'] . "'";
$result = mysqli_query($con, $sql);
if(!$result)
    failure($con);

$store = parseStoreWithCampaigns($con, $result);
mysqli_free_result($result);
if(!$store)
    failure($con);

echo json_encode($store); 
?>