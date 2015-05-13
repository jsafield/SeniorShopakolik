<?php
include 'Functions.php';

function mFailure($con) {
    unlink("Images/CampaignImages/" . $_REQUEST['image']); // if fails ???
    failure($con);
}

$con = connect();
if (!$con) die("connection_error");

if(!mysqli_autocommit($con, false))
    mFailure($con);

$storeid = getStoreID($con, $_REQUEST['email'], $_REQUEST['password']);
if(!$storeid)
    mFailure($con);

if($_REQUEST['type'] == 0) {
    
    $sql = "INSERT INTO Campaign (start_date, end_date, image, type, precondition, 
                                    details, percentage, store_id) 
            VALUES ('" . $_REQUEST['start_date'] . "', '" . $_REQUEST['end_date'] 
                    . "', '" . $_REQUEST['image'] . "', '" . $_REQUEST['type'] 
                    . "', '" . $_REQUEST['condition'] . "', '" . $_REQUEST['details'] 
                    . "', '" . $_REQUEST['percentage'] . "', '" . $storeid . "')";
    
} else if($_REQUEST['type'] == 1 || $_REQUEST['type'] == 2) {
    
    $sql = "INSERT INTO Campaign (start_date, end_date, image, type, precondition, 
                                    details, amount, store_id) 
            VALUES ('" . $_REQUEST['start_date'] . "', '" . $_REQUEST['end_date'] 
                    . "', '" . $_REQUEST['image'] . "', '" . $_REQUEST['type'] 
                    . "', '" . $_REQUEST['condition'] . "', '" . $_REQUEST['details'] 
                    . "', '" . $_REQUEST['amount'] . "', '" . $storeid . "')";
    
} else if($_REQUEST['type'] == 3) {
    
    $sql = "INSERT INTO Campaign (start_date, end_date, image, type, precondition, 
                                    details, store_id) 
            VALUES ('" . $_REQUEST['start_date'] . "', '" . $_REQUEST['end_date'] 
                    . "', '" . $_REQUEST['image'] . "', '" . $_REQUEST['type'] 
                    . "', '" . $_REQUEST['condition'] . "', '" . $_REQUEST['details'] 
                    . "', '" . $storeid . "')";
} else {
    mFailure($con);
}
$result = mysqli_query($con, $sql);
if(!$result) 
    mFailure($con);

if(mysqli_commit($con)) {
    echo "success";
    mysqli_close($con);
} else {
    mFailure($con);
}
?>