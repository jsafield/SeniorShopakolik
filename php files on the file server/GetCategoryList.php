<?php
include 'Functions.php';

$con = connect();
if (!$con) die("connection_error");

$sql = "SELECT category_id, category FROM Category";
$result = mysqli_query($con, $sql);
if(!$result)
    failure($con);
$i = 0;
while ($row = mysqli_fetch_assoc($result)) {
    $cats[$i]['category_id'] = $row['category_id'];
    $cats[$i]['category'] = $row['category'];
    $i++;
}
mysqli_free_result($result);
echo json_encode($cats);
?>