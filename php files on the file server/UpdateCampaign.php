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
    
    $sql = "UPDATE Campaign SET start_date = '" . $_REQUEST['start_date'] . "', 
                end_date = '" . $_REQUEST['end_date'] . "', 
                " .
                (($_REQUEST['isImageChanged'] == 1) ? ("image = '" . $_REQUEST['image'] . "', 
                ") : "") .
                "type = '" . $_REQUEST['type'] . "', 
                precondition = '" . $_REQUEST['precondition'] . "', 
                details = '" . $_REQUEST['details'] . "', 
                percentage = '" . $_REQUEST['percentage'] . "', 
                amount = NULL 
            WHERE store_id = '" . $storeid . "' 
                AND campaign_id = '" . $_REQUEST['campaign_id'] . "'";
    
} else if($_REQUEST['type'] == 1 || $_REQUEST['type'] == 2) {
    
    $sql = "UPDATE Campaign SET start_date = '" . $_REQUEST['start_date'] . "', 
                end_date = '" . $_REQUEST['end_date'] . "', 
                " .
                (($_REQUEST['isImageChanged'] == 1) ? ("image = '" . $_REQUEST['image'] . "', 
                ") : "") .
                "type = '" . $_REQUEST['type'] . "', 
                precondition = '" . $_REQUEST['precondition'] . "', 
                details = '" . $_REQUEST['details'] . "', 
                percentage = NULL, 
                amount = '" . $_REQUEST['amount'] . "' 
            WHERE store_id = '" . $storeid . "' 
                AND campaign_id = '" . $_REQUEST['campaign_id'] . "'";
    
} else if($_REQUEST['type'] == 3) {
    
    $sql = "UPDATE Campaign SET start_date = '" . $_REQUEST['start_date'] . "', 
                end_date = '" . $_REQUEST['end_date'] . "', 
                " .
                (($_REQUEST['isImageChanged'] == 1) ? ("image = '" . $_REQUEST['image'] . "', 
                ") : "") .
                "type = '" . $_REQUEST['type'] . "', 
                precondition = '" . $_REQUEST['precondition'] . "', 
                details = '" . $_REQUEST['details'] . "', 
                percentage = NULL, 
                amount = NULL 
            WHERE store_id = '" . $storeid . "' 
                AND campaign_id = '" . $_REQUEST['campaign_id'] . "'";
} else {
    mFailure($con);
}

$result = mysqli_query($con, $sql);
if(!$result)
    mFailure($con);

if (mysqli_commit($con)) {
    echo 'success';
    unlink("Images/CampaignImages/" . $_REQUEST['previmage']); // if fails ???
    mysqli_close($con);
} else {
    mFailure($con);
}
?>