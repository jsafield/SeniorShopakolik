<?php
include 'Functions.php';

$con = connect();
if (!$con) die("connection_error");

if(!mysqli_autocommit($con, false))
    failure($con);

$customerid = getCustomerID($con, $_REQUEST['email'], $_REQUEST['password']);
if(!$customerid)
    failure($con);

$sql = "SELECT * FROM Campaign NATURAL JOIN Store 
        WHERE end_date >= CURDATE() AND campaign_id IN (
            SELECT campaign_id FROM FavCampaign 
            WHERE customer_id = '" . $customerid . "')
        ORDER BY end_date";

$result = mysqli_query($con, $sql);
if(!$result)
    failure($con);
$campaigns = parseCampaignsDetailed($result);
mysqli_free_result($result);

echo json_encode($campaigns); 
?>