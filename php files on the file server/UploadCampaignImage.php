<?php
$file_path = tempnam("Images/CampaignImages", "Campaign_");
unlink($file_path);
$file_path = $file_path . ".png";
$file_name = basename($file_path );

if(move_uploaded_file($_FILES['uploaded_file']['tmp_name'], $file_path)) {
    echo $file_name;
} else{
    echo "failure";
}
?>