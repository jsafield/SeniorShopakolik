<?php
include 'Functions.php';

$con = connect();
if (!$con) die("connection_error");

if (!isUser($con, $_REQUEST['email'], $_REQUEST['password']))
    failure($con);

$sql = "SELECT * FROM Campaign 
        WHERE store_id = '" . $_REQUEST['store_id'] . "' 
            AND end_date >= CURDATE() 
        ORDER BY end_date";
$result = mysqli_query($con, $sql);
if(!$result)
    failure($con);

$campaigns = parseCampaigns($result);
mysqli_free_result($result);

echo json_encode($campaigns); 
?>