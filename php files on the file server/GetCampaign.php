<?php
include "Functions.php";

$con = connect();
if (!$con) die("connection_error");

$storeid = getStoreID($con, $_REQUEST['email'], $_REQUEST['password']);
if(!$storeid)
    failure($con);

$sql = "SELECT * FROM Campaign NATURAL JOIN Store 
        WHERE store_id = '" . $storeid . "' 
            AND campaign_id = '" . $_REQUEST['campaign_id'] . "'";

$result = mysqli_query($con, $sql);
if(!$result)
    failure($con);

if (mysqli_num_rows($result) == 0)
    failure($con);

$row = mysqli_fetch_assoc($result);

$campaign['campaign_id'] = $row['campaign_id'];
$campaign['start_date'] = $row['start_date'];
$campaign['end_date'] = $row['end_date'];
$campaign['image'] = $row['image'];
$campaign['type'] = $row['type'];
$campaign['precondition'] = $row['precondition'];
$campaign['details'] = $row['details'];
$campaign['percentage'] = $row['percentage'];
$campaign['amount'] = $row['amount'];
$campaign['store_id'] = $storeid;
$campaign['user_id'] = $row['user_id'];
$campaign['name'] = $row['name'];
$campaign['logo'] = $row['logo'];

mysqli_free_result($result);

echo json_encode($campaign); 
?>