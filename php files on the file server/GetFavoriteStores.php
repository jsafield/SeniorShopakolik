<?php
include 'Functions.php';

$con = connect();
if (!$con) die("connection_error");

if(!mysqli_autocommit($con, false))
    failure($con);
    
$customerid = getCustomerID($con, $_REQUEST['email'], $_REQUEST['password']);
if(!$customerid)
    failure($con);

$sql = "SELECT * FROM Store 
        WHERE store_id IN (
            SELECT store_id FROM FavStore 
            WHERE customer_id = '" . $customerid . "')";
$result = mysqli_query($con, $sql);
if(!$result)
    failure($con);

$stores = parseStoresWithCampaigns($con, $result);
mysqli_free_result($result);
if(!$stores)
    failure($con);

echo json_encode($stores); 
?>