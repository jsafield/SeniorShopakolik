<?php
function connect() {
    return mysqli_connect("shop.c3qch1ggyyxx.eu-central-1.rds.amazonaws.com", 
		"shopakolik", "********", "shopakolik", 3306);
}

function failure($con) {
    echo 'failure';
    mysqli_close($con);
    exit();
}

function isUser($con, $email, $password) {
    $sql = "SELECT user_id FROM User WHERE email = '" . $email 
            . "' AND password = '" . $password . "' AND confirmation = '1'";
    $result = mysqli_query($con, $sql);
    if(!$result) 
        return false;
    if(mysqli_num_rows($result) == 0) 
        return false;
    
    return true;
}

function getUserID($con, $email, $password) {
    $sql = "SELECT user_id FROM User WHERE email = '" . $email 
            . "' AND password = '" . $password . "' AND confirmation = '1'";
    $result = mysqli_query($con, $sql);
    if(!$result) 
        return false;
    if(mysqli_num_rows($result) == 0) 
        return false;
    $row = mysqli_fetch_array($result);
    $userid = $row['user_id'];
    mysqli_free_result($result);
    
    return $userid;
}

function getStoreID($con, $email, $password) {
    $userid = getUserID($con, $email, $password);
    if (!$userid)
        return false;
    
    $sql = "SELECT store_id FROM Store WHERE user_id = '" . $userid . "'";
    $result = mysqli_query($con, $sql);
    if(!$result) 
        return false;
    if(mysqli_num_rows($result) == 0)
        return false;
    $row = mysqli_fetch_array($result);
    $storeid = $row['store_id'];
    mysqli_free_result($result);
    
    return $storeid;
}

function getCustomerID($con, $email, $password) {
    $userid = getUserID($con, $email, $password);
    
    $sql = "SELECT customer_id FROM Customer WHERE user_id = '" . $userid . "'";
    $result = mysqli_query($con, $sql);
    if(!$result) 
        return false;
    if(mysqli_num_rows($result) == 0)
        return false;
    $row = mysqli_fetch_array($result);
    $customerid = $row['customer_id'];
    mysqli_free_result($result);
    
    return $customerid;
}

function parseStoreRowSimple($con, $row) {

    $store['store_id'] = $row['store_id'];
    $store['name'] = $row['name'];
    $store['logo'] = $row['logo'];
        
    return $store;
}

function parseStoreRow($con, $row) {

    $store = parseStoreRowSimple($con, $row);
    
    $sql = "SELECT category_id, category FROM Category 
            WHERE category_id IN (
                SELECT category_id FROM CategoryStore 
                WHERE store_id = '" . $row['store_id'] . "')";
    $result = mysqli_query($con, $sql);
    if(!$result)
        return false;

    $j = 0;
    while ($row2 = mysqli_fetch_assoc($result)) {
        $store['categories'][$j]['category_id'] = $row2['category_id'];
        $store['categories'][$j]['category'] = $row2['category'];
        $j++;
    }
    mysqli_free_result($result);
    
    $sql = "SELECT * FROM Location 
            WHERE store_id = '" . $row['store_id'] . "'";
    $result = mysqli_query($con, $sql);
    if(!$result)
        return false;

    $j = 0;
    while ($row2 = mysqli_fetch_assoc($result)) {
        $store['locations'][$j]['location_id'] = $row2['location_id'];
        $store['locations'][$j]['location'] = $row2['location'];
        $store['locations'][$j]['latitude'] = $row2['latitude'];
        $store['locations'][$j]['longitude'] = $row2['longitude'];
        $store['locations'][$j]['address'] = $row2['address'];
        $j++;
    }
    mysqli_free_result($result);
        
    return $store;
}

function parseStoreRowWithCampaigns($con, $row) {
    $store = parseStoreRow($con, $row);
    
    $sql = "SELECT * FROM Campaign 
            WHERE store_id = '" . $row['store_id'] . "' 
            ORDER BY end_date";
    $result2 = mysqli_query($con, $sql);
    if(!$result2)
        return false;

    $store['campaigns'] =  parseCampaigns($result2);
    mysqli_free_result($result2);
        
    return $store;
}

function parseStoreRowWithPresentCampaigns($con, $row) {
    $store = parseStoreRow($con, $row);
    
    $sql = "SELECT * FROM Campaign 
            WHERE store_id = '" . $row['store_id'] . "' 
                AND end_date >= CURDATE() 
            ORDER BY end_date";
    $result2 = mysqli_query($con, $sql);
    if(!$result2)
        return false;

    $store['campaigns'] =  parseCampaigns($result2);
    mysqli_free_result($result2);
        
    return $store;
}

function parseStoreWithCampaigns($con, $result) {
    $row = mysqli_fetch_assoc($result);
    
    return parseStoreRowWithPresentCampaigns($con, $row);
}

function parseStoreWithAllCampaigns($con, $result) {
    $row = mysqli_fetch_assoc($result);
    
    return parseStoreRowWithCampaigns($con, $row);
}

function parseStoresSimple($con, $result) {
    
    $i = 0;
    while ($row = mysqli_fetch_assoc($result)) {
        $stores[$i] = parseStoreRowSimple($con, $row);
        $i++;
    }
    return $stores;
}

function parseStores($con, $result) {
    
    $i = 0;
    while ($row = mysqli_fetch_assoc($result)) {
        $stores[$i] = parseStoreRow($con, $row);
        $i++;
    }
    return $stores;
}

function parseStoresWithCampaigns($con, $result) {
    
    $i = 0;
    while ($row = mysqli_fetch_assoc($result)) {
        $stores[$i] = parseStoreRowWithPresentCampaigns($con, $row);
        $i++;
    }
    return $stores;
}

function parseCampaigns($result) {
    $i = 0;
    while ($row = mysqli_fetch_assoc($result)) {
        $campaigns[$i]['campaign_id'] = $row['campaign_id'];
        $campaigns[$i]['start_date'] = $row['start_date'];
        $campaigns[$i]['end_date'] = $row['end_date'];
        $campaigns[$i]['image'] = $row['image'];
        $campaigns[$i]['type'] = $row['type'];
        $campaigns[$i]['precondition'] = $row['precondition'];
        $campaigns[$i]['details'] = $row['details'];
        $campaigns[$i]['percentage'] = $row['percentage'];
        $campaigns[$i]['amount'] = $row['amount'];
        $campaigns[$i]['store_id'] = $row['store_id'];
        $i++;
    }
    return $campaigns;
}

function parseCampaignsDetailed($result) {
    $i = 0;
    while ($row = mysqli_fetch_assoc($result)) {
        $campaigns[$i]['campaign_id'] = $row['campaign_id'];
        $campaigns[$i]['start_date'] = $row['start_date'];
        $campaigns[$i]['end_date'] = $row['end_date'];
        $campaigns[$i]['image'] = $row['image'];
        $campaigns[$i]['type'] = $row['type'];
        $campaigns[$i]['precondition'] = $row['precondition'];
        $campaigns[$i]['details'] = $row['details'];
        $campaigns[$i]['percentage'] = $row['percentage'];
        $campaigns[$i]['amount'] = $row['amount'];
        $campaigns[$i]['store_id'] = $row['store_id'];
        $campaigns[$i]['user_id'] = $row['user_id'];
        $campaigns[$i]['name'] = $row['name'];
        $campaigns[$i]['logo'] = $row['logo'];
        $i++;
    }
    return $campaigns;
}
?>