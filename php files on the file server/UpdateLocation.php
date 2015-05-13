<?php
include 'Functions.php';

$con = connect();
if (!$con) die("connection_error");

if(!mysqli_autocommit($con, false))
    failure($con);

$storeid = getStoreID($con, $_REQUEST['email'], $_REQUEST['password']);
if(!$storeid)
    failure($con);

$sql = "UPDATE Location SET location = '" . $_REQUEST['location'] . "', 
            latitude = '" . $_REQUEST['latitude'] . "', 
            longitude = '" . $_REQUEST['longitude'] . "', 
            address = '" . $_REQUEST['address'] . "' 
        WHERE store_id = '" . $storeid . "' 
            AND location_id = '" . $_REQUEST['location_id'] . "'";
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