<?php
include 'Functions.php';

$con = connect();
if (!$con) die("connection_error");

if (!isUser($con, $_REQUEST['email'], $_REQUEST['password']))
    failure($con);

$sql = "SELECT * FROM Store WHERE name LIKE '%" . $_REQUEST['search_key'] . "%'";
$result = mysqli_query($con, $sql);
if(!$result)
    failure($con);

$stores = parseStoresSimple($con, $result);
mysqli_free_result($result);
if(!$stores)
    failure($con);

echo json_encode($stores);
?>