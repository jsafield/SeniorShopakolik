<?php
include 'Functions.php';

$con = connect();
if (!$con) die("connection_error");

if(!mysqli_autocommit($con, false))
    failure($con);

$customerid = getCustomerID($con, $_REQUEST['email'], $_REQUEST['password']);
if(!$customerid)
    failure($con);

$values = "('" . $_REQUEST['store_id_0'] . "', '" . $customerid . "')";
for($i = 1; $i < $_REQUEST['store_count']; $i++){
    $values .= ", ('" . $_REQUEST['store_id_' . $i] . "', '" . $customerid . "')";
}

$sql = "INSERT INTO FavStore (store_id, customer_id) VALUES " . $values;
$result = mysqli_query($con, $sql);
if(!$result) 
    failure($con);

if(mysqli_commit($con)) {
    echo "success";
    mysqli_close($con);
} else {
    failure($con);
}
?>