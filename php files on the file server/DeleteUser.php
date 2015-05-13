<?php
include 'Functions.php';

$con = connect();
if (!$con) die("connection_error");

if(!mysqli_autocommit($con, false))
    failure($con);

$userid = getUserID($con, $_REQUEST['email'], $_REQUEST['password']);
if(!$userid)
    mFailure($con);

$sql = "SELECT customer_id FROM Customer WHERE user_id = '" . $userid . "'";
$result = mysqli_query($con, $sql);

if(!$result)
    failure($con);

$count = mysqli_num_rows($result);

if($count>0)
{
    $row = mysqli_fetch_array($result);
    $customerid = $row['customer_id'];
    mysqli_free_result($result);
    
    $sql = "DELETE FROM FavStore WHERE customer_id = '" . $customerid . "'";
    $result = mysqli_query($con, $sql);
    if(!$result)
        failure($con);
    
    $sql = "DELETE FROM FavCampaign WHERE customer_id = '" . $customerid . "'";
    $result = mysqli_query($con, $sql);
    if(!$result)
        failure($con);
    
    $sql = "DELETE FROM Customer WHERE customer_id = '" . $customerid . "'";
    $result = mysqli_query($con, $sql);
    if(!$result)
        failure($con);
}
else
{
    $sql = "SELECT store_id, logo FROM Store WHERE user_id = '" . $userid . "'";
    $result = mysqli_query($con, $sql);
    if(!$result)
        failure($con);
    if(mysqli_num_rows($result) == 0)
        failure($con);
    
    $row = mysqli_fetch_array($result);
    $storeid = $row['store_id'];
    $logo = $row['logo'];
    mysqli_free_result($result);
    
    $sql = "DELETE FROM FavCampaign WHERE campaign_id IN (
                SELECT campaign_id FROM Campaign 
                WHERE store_id = '" . $storeid . "')";
    $result = mysqli_query($con, $sql);
    if(!$result)
        failure($con);
    
    $sql = "DELETE FROM FavStore WHERE store_id = '" . $storeid . "'";
    $result = mysqli_query($con, $sql);
    if(!$result)
        failure($con);
    
    // delete campaign images
    $sql = "SELECT image FROM Campaign WHERE store_id = '" . $storeid . "'";
    $result = mysqli_query($con, $sql);
    if(!$result)
        failure($con);
    while ($row = mysqli_fetch_row($result)) {
        unlink("Images/CampaignImages/" . $row['image']); // if fails ???
    }
    mysqli_free_result($result);
    
    $sql = "DELETE FROM Campaign WHERE store_id = '" . $storeid . "'";
    $result = mysqli_query($con, $sql);
    if(!$result)
        failure($con);
    
    $sql = "DELETE FROM Location WHERE store_id = '" . $storeid . "'";
    $result = mysqli_query($con, $sql);
    if(!$result)
        failure($con);
    
    $sql = "DELETE FROM CategoryStore WHERE store_id = '" . $storeid . "'";
    $result = mysqli_query($con, $sql);
    if(!$result)
        failure($con);
    
    $sql = "DELETE FROM Store WHERE store_id = '" . $storeid . "'";
    $result = mysqli_query($con, $sql);
    if(!$result)
        failure($con);
    
    $logopath = 'Images/StoreLogos/' . $logo;
    unlink($logopath); // if fails ???
}

$sql = "DELETE FROM User WHERE user_id = '" . $userid . "'";
$result = mysqli_query($con, $sql);
if (!$result)
    failure($con);
    
if (mysqli_commit($con)) {
    echo 'success';
    mysqli_close($con);
} else {
    failure($con);
}
?>