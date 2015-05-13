<?php
include 'Functions.php';

function mFailure($con) {
    unlink("Images/StoreLogos/" . $_REQUEST['logo']); // if fails ???
    failure($con);
}
function addLocation($con, $location, $latitude, $longitude, $address, $storeid) {
    $sql = "INSERT INTO Location (location, latitude, longitude, address, store_id) 
            VALUES ('" . $location . "', '" . $latitude . "', '" . $longitude 
            . "', '" . $address . "', '" . $storeid . "')";
    $result = mysqli_query($con, $sql);
    return $result;
}
function addCategory($con, $storeid, $categoryid) {
    $sql = "INSERT INTO CategoryStore (store_id, category_id) 
            VALUES ('" . $storeid . "', '" . $categoryid  . "')";
    $result = mysqli_query($con, $sql);
    return $result;
}

$con = connect();
if (!$con) die("connection_error");

if(!mysqli_autocommit($con, false))
    mFailure($con);

$sql = "INSERT INTO User (email, password, confirmation) 
        VALUES ('" . $_REQUEST['email'] . "', '" . $_REQUEST['password'] 
                . "', '1')"; // TODO 1 will be 0
$result = mysqli_query($con, $sql);

if(!$result)
    mFailure($con);

$user_id = mysqli_insert_id($con);

$sql = "INSERT INTO Store (user_id, name, logo) 
        VALUES ('" . $user_id . "', '" . $_REQUEST['name'] . "', '" 
        . $_REQUEST['logo'] . "')";
$result = mysqli_query($con, $sql);

if(!$result)
    mFailure($con);

$storeid = mysqli_insert_id($con);

$category_count = $_REQUEST['category_count'];
for ($i = 0; $i < $category_count; $i++) {
    $result = addCategory($con, $storeid, $_REQUEST['category' . $i]);
    if(!$result)
        mFailure($con);
}
$locationErr = false;
$location_count = $_REQUEST['location_count'];
for ($i = 0; $i < $location_count; $i++) {
    $result = addLocation($con, $_REQUEST['location' . $i], $_REQUEST['latitude' 
        . $i], $_REQUEST['longitude' . $i], $_REQUEST['address' . $i], $storeid);
    if(!$result) {
        $locationErr = true;
    }
}
if (mysqli_commit($con)) {
    if($locationErr)
        echo "location_error";
    else
        echo "success";
    mysqli_close($con);
} else {
    mFailure($con);
}
?>