<?php
include 'Functions.php';

$con = connect();
if (!$con) die("connection_error");

if(!mysqli_autocommit($con, false))
    failure($con);

$storeid = getStoreID($con, $_REQUEST['email'], $_REQUEST['password']);
if(!$storeid)
    failure($con);

$sql = "INSERT INTO Location (location, latitude, longitude, address, store_id) 
        VALUES ('" . $_REQUEST['location'] . "', '" . $_REQUEST['latitude'] 
            . "', '" . $_REQUEST['longitude'] . "', '" . $_REQUEST['address'] 
            . "', '" . $storeid . "')";
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