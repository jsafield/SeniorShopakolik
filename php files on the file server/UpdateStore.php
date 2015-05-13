<?php
include 'Functions.php';

function mFailure($con) {
    unlink("Images/StoreLogos/" . $_REQUEST['logo']); // if fails ???
    failure($con);
}

$con = connect();
if (!$con) die("connection_error");

if(!mysqli_autocommit($con, false))
    mFailure($con);

$userid = getUserID($con, $_REQUEST['currentemail'], $_REQUEST['currentpassword']);
if(!$userid)
    mFailure($con);

$storeid = getStoreID($con, $_REQUEST['currentemail'], $_REQUEST['currentpassword']);
if(!$storeid)
    mFailure($con);

$catIDs = "'" . $_REQUEST['category_id_0'] . "'";
for($i = 1; $i < $_REQUEST['category_count']; $i++){
    $catIDs .= ", '" . $_REQUEST['category_id_' . $i] . "'";
}

$sql = "DELETE FROM CategoryStore 
        WHERE store_id = '" . $storeid . "' 
            AND category_id NOT IN (" . $catIDs . ")";
$result = mysqli_query($con, $sql);
if(!$result)
    mFailure($con);

for($i = 0; $i < $_REQUEST['category_count']; $i++){
    $sql = "INSERT INTO CategoryStore (store_id, category_id) 
            VALUES ('" . $storeid . "', '" .$_REQUEST['category_id_' . $i] . "')";
    mysqli_query($con, $sql);
    // if unexpected mFailure occurs, nothing will be done 
}

$sql = "UPDATE Store SET name = '" . $_REQUEST['name'] . "'" . 
        (($_REQUEST['isLogoChanged'] == 1) ? ", 
                logo = '" . $_REQUEST['logo'] . "'" : "") . " 
        WHERE user_id = '" . $userid . "';";
    
$result = mysqli_query($con, $sql);

if(!$result)
    mFailure($con);

$sql = "UPDATE User SET email = '" . $_REQUEST['email'] . "', password = '" 
    . $_REQUEST['password'] . "' WHERE user_id = '" . $userid . "';";
$result = mysqli_query($con, $sql);

if(!$result)
    mFailure($con);

if (mysqli_commit($con)) {
    echo "success";
    unlink("Images/StoreLogos/" . $_REQUEST['prevlogo']); // if fails ???
    mysqli_close($con);
} else {
    mFailure($con);
}
?>