<?php
include 'Functions.php';

$con = connect();
if (!$con) die("connection_error");

$customerid = getCustomerID($con, $_REQUEST['email'], $_REQUEST['password']);
if(!$customerid)
    failure($con);

$sql = "SELECT * FROM FavCampaign WHERE campaign_id = '" . $_REQUEST['campaign_id'] . "'";

$result = mysqli_query($con, $sql);
if(!$result)
    failure($con);
    
if(mysqli_num_rows($result) == 0)
{
    failure($con);
}
echo 'success';
?>