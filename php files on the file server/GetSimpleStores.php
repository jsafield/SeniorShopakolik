<?php
include 'Functions.php';

$con = connect();
if (!$con) die("connection_error");

if (!isUser($con, $_REQUEST['email'], $_REQUEST['password']))
    failure($con);

$catIDs = "'" . $_REQUEST['category_id_0'] . "'";
for($i = 1; $i < $_REQUEST['category_count']; $i++){
    $catIDs .= ", '" . $_REQUEST['category_id_' . $i] . "'";
}

$sql = "SELECT * FROM Store 
        WHERE store_id IN (
            SELECT store_id FROM CategoryStore 
            WHERE category_id IN (" . $catIDs . "))";
$result = mysqli_query($con, $sql);
if(!$result)
    failure($con);

$stores = parseStoresSimple($con, $result);
mysqli_free_result($result);
if(!$stores)
    failure($con);

echo json_encode($stores);
?>