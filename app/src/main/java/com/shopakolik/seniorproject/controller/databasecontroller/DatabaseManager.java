package com.shopakolik.seniorproject.controller.databasecontroller;

import android.util.Log;

import com.shopakolik.seniorproject.model.shopakolikelements.Campaign;
import com.shopakolik.seniorproject.model.shopakolikelements.CampaignType;
import com.shopakolik.seniorproject.model.shopakolikelements.Category;
import com.shopakolik.seniorproject.model.shopakolikelements.Customer;
import com.shopakolik.seniorproject.model.shopakolikelements.Location;
import com.shopakolik.seniorproject.model.shopakolikelements.Store;
import com.shopakolik.seniorproject.model.shopakolikelements.User;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Ahmet Cihat Unaldi
 */

public class DatabaseManager {

    private static final String SERVER_URL = "http://unaldi.0fees.us/shopakolik/";
    private static boolean locationError = false;
    private static SimpleDateFormat SQLDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public static boolean isLocationsAdded() {
        return locationError;
    }

    public static String getServerUrl() {
        return SERVER_URL;
    }

    // send http post request and read http response
    private static String httpPost(String url, String urlParameters) throws Exception {

        URL url0;
        HttpURLConnection conn;

        try {
            url0 = new URL(url);
        } catch (MalformedURLException e) {
            throw new Exception("Invalid URL");
        }

        // open http connection to send post request
        try {
            conn = (HttpURLConnection) url0.openConnection();
        } catch (IOException e) {
            throw new Exception("File Server Connection Error");
        }

        conn.setRequestMethod("POST");
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setUseCaches(false);

//            conn.setRequestProperty("enctype", "application/x-www-form-urlencoded"); // default
//            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); // default
        conn.setRequestProperty("Content-Length", Integer.toString(urlParameters.getBytes().length));
        conn.setRequestProperty("Content-Language", "tr-TR");

        // http post parameters send via data output steam
        DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
        dos.writeBytes(urlParameters);
        dos.flush();
        dos.close();

        InputStream is = conn.getInputStream();
        String result;

        // read http response via input stream
        if (conn.getResponseCode() == 200) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
            reader.close();
            result = sb.toString();
        } else {
            throw new Exception("Unexpected HTTP Response");
        }
        is.close();
        conn.disconnect();
        if (result.equals("connection_error\n")) {
            throw new Exception("Database Server Connection Error");
        }
        return result;
    }

    // send http post request to upload file and returns the name of file in server
    private static String httpPostFile(String url, String sourceFileUri) throws Exception {

        URL url0;
        HttpURLConnection conn;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int maxBufferSize = 1 * 1024 * 1024;

        File sourceFile = new File(sourceFileUri);
        FileInputStream fis;
        try {
            fis = new FileInputStream(sourceFile);
        } catch (FileNotFoundException e) {
            throw new Exception("File Not Exist");
        }
        try {
            url0 = new URL(url);
        } catch (MalformedURLException e) {
            throw new Exception("Invalid URL");
        }

        // open http connection to send post request
        try {
            conn = (HttpURLConnection) url0.openConnection();
        } catch (IOException e) {
            throw new Exception("File Server Connection Error");
        }
        conn.setRequestMethod("POST");
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setUseCaches(false);

        conn.setRequestProperty("enctype", "multipart/form-data");
        conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
        conn.setRequestProperty("Connection", "Keep-Alive");
        conn.setRequestProperty("uploaded_file", sourceFileUri);

        // file send via data output steam
        DataOutputStream dos = new DataOutputStream(conn.getOutputStream());

        dos.writeBytes(twoHyphens + boundary + lineEnd);
        dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
                + sourceFileUri + "\"" + lineEnd);
        dos.writeBytes(lineEnd);

        // create a buffer of  maximum size
        int bytesAvailable = fis.available();
        int bufferSize = Math.min(bytesAvailable, maxBufferSize);
        byte[] buffer = new byte[bufferSize];

        // read file and write it into form
        int bytesRead = fis.read(buffer, 0, bufferSize);

        while (bytesRead > 0) {
            dos.write(buffer, 0, bufferSize);
            bytesAvailable = fis.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            bytesRead = fis.read(buffer, 0, bufferSize);
        }
        // send multipart form data necessary after file data
        dos.writeBytes(lineEnd);
        dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

        fis.close();
        dos.flush();
        dos.close();
        InputStream is = conn.getInputStream();
        String result;

        // read http response via input stream
        if (conn.getResponseCode() == 200) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
            reader.close();
            result = sb.toString();
        } else {
            throw new Exception("Unexpected HTTP Response");
        }
        is.close();
        conn.disconnect();
        if (result.equals("failure\n")) {
            throw new Exception("File Upload Failure");
        }
        return result;
    }

    private static Store parseSimpleStoreFromJSON(String result) throws Exception {

        JSONObject jsonObject = new JSONObject(result);

        int storeId = jsonObject.getInt("store_id");
        String name = jsonObject.getString("name");
        String logo = jsonObject.getString("logo");

        // TODO too much content
        ArrayList<Category> categories = null;
        try {
            categories = new ArrayList<>();
            JSONArray jsonArray1 = jsonObject.getJSONArray("categories");

            for (int j = 0; j < jsonArray1.length(); j++) {
                try {
                    JSONObject jsonObject1 = jsonArray1.getJSONObject(j);

                    int mCategoryId = jsonObject1.getInt("category_id");
                    String categoryName = jsonObject1.getString("category");

                    categories.add(new Category(mCategoryId, categoryName));
                } catch (Exception e) {
                    Log.e("Category parse fail", "A category of the store with store id: " + storeId
                            + ", could not be parsed.");
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            Log.e("No category", "No category found for store with store id: " + storeId);
            e.printStackTrace();
        }

        // TODO too much content
        ArrayList<Location> locations = null;
        try {
            locations = new ArrayList<>();
            JSONArray jsonArray2 = jsonObject.getJSONArray("locations");

            for (int j = 0; j < jsonArray2.length(); j++) {
                try {
                    JSONObject jsonObject2 = jsonArray2.getJSONObject(j);

                    int locationId = jsonObject2.getInt("location_id");
                    String location = jsonObject2.getString("location");
                    float latitude = Float.parseFloat(jsonObject2.getString("latitude"));
                    float longitude = Float.parseFloat(jsonObject2.getString("longitude"));
                    String address = jsonObject2.getString("address");

                    locations.add(new Location(locationId, location, latitude, longitude, address));
                } catch (Exception e) {
                    Log.e("Location parse fail", "A location of the store with store id: " + storeId
                            + ", could not be parsed.");
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            Log.e("No location", "No location found for store with store id: " + storeId);
            e.printStackTrace();
        }

        return new Store(storeId, name, logo, categories, locations);
    }

    private static Store parseStoreFromJSON(String result) throws Exception {

        Store store = parseSimpleStoreFromJSON(result);

        try {
            JSONObject jsonObject = new JSONObject(result);
            String camp = jsonObject.getString("campaigns");
            ArrayList<Campaign> campaigns = parseCampaignsFromJSON(camp);
            store.setCampaigns(campaigns);
        } catch (Exception e) {
            Log.w("No campaign", "No campaign found for store with store id: " + store.getStoreId());
            e.printStackTrace();
        }

        return store;
    }

    private static ArrayList<Store> parseStoresFromJSON(String result) throws Exception {
        ArrayList<Store> stores = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(result);

        for (int i = 0; i < jsonArray.length(); i++) {
            stores.add(parseSimpleStoreFromJSON(jsonArray.getString(i)));
        }
        return stores;
    }

    private static ArrayList<Campaign> parseCampaignsFromJSON(String result) throws Exception {

        ArrayList<Campaign> campaigns = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(result);

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                int campaignId = jsonObject.getInt("campaign_id");
                Date startDate = SQLDateFormat.parse(jsonObject.getString("start_date"));
                Date endDate = SQLDateFormat.parse(jsonObject.getString("end_date"));
                String image = jsonObject.getString("image");
                CampaignType type = CampaignType.values()[jsonObject.getInt("type")];
                String condition = jsonObject.getString("precondition");
                String details = jsonObject.getString("details");
                int storeId = jsonObject.getInt("store_id");

                int percentage = 0;
                if (type == CampaignType.DiscountPercentage)
                    percentage = jsonObject.getInt("percentage");

                float amount = 0;
                if (type == CampaignType.DiscountAmount || type == CampaignType.ShoppingVoucher)
                    amount = Float.parseFloat(jsonObject.getString("amount"));

                campaigns.add(new Campaign(campaignId, startDate, endDate, image, type, condition,
                        details, percentage, amount, storeId));
            } catch (Exception e) {
                Log.e("Campaign parse fail", "A campaign could not be parsed.");
                e.printStackTrace();
            }
        }
        return campaigns;
    }

    public static UserType login(User user) throws Exception {

        return login(user.getEmail(), user.getPassword());
    }

    public static UserType login(String email, String password) throws Exception {

        String urlParameters = "email=" + URLEncoder.encode(email, "UTF-8")
                + "&password=" + URLEncoder.encode(password, "UTF-8");
        String result = httpPost(SERVER_URL + "Login.php", urlParameters);

        if (result.equals("customer\n"))
            return UserType.Customer;
        if (result.equals("store\n"))
            return UserType.Store;
        if (result.equals("non_user\n"))
            return UserType.NonUser;

        throw new Exception("Unexpected Error In PHP File");
    }

    public static boolean addCustomer(Customer customer) throws Exception {

        String urlParameters = "name=" + URLEncoder.encode(customer.getName(), "UTF-8")
                + "&surname=" + URLEncoder.encode(customer.getSurname(), "UTF-8")
                + "&email=" + URLEncoder.encode(customer.getEmail(), "UTF-8")
                + "&password=" + URLEncoder.encode(customer.getPassword(), "UTF-8")
                + "&locationNotification="
                + URLEncoder.encode(customer.isLocationNotification() ? "1" : "0", "UTF-8")
                + "&campaignNotification="
                + URLEncoder.encode(customer.isCampaignNotification() ? "1" : "0", "UTF-8");
        String result = httpPost(SERVER_URL + "AddCustomer.php", urlParameters);

        return result.equals("success\n");
    }

    public static boolean addStore(Store store) throws Exception {

        locationError = false;
        String logo = httpPostFile(SERVER_URL + "UploadStoreLogo.php", store.getLogo());

        String urlParameters = "email=" + URLEncoder.encode(store.getEmail(), "UTF-8")
                + "&password=" + URLEncoder.encode(store.getPassword(), "UTF-8")
                + "&name=" + URLEncoder.encode(store.getName(), "UTF-8")
                + "&logo=" + URLEncoder.encode(logo, "UTF-8")
                + "&category_count=" + URLEncoder.encode("" + store.getCategories().size(), "UTF-8")
                + "&location_count=" + URLEncoder.encode("" + store.getLocations().size(), "UTF-8");

        for (int i = 0; i < store.getCategories().size(); i++) {
            urlParameters += "&category" + i + "=" +
                    URLEncoder.encode("" + store.getCategories().get(i).getCategoryId(), "UTF-8");
        }
        for (int i = 0; i < store.getLocations().size(); i++) {
            urlParameters += "&location" + i + "="
                    + URLEncoder.encode(store.getLocations().get(i).getLocation(), "UTF-8")
                    + "&latitude" + i + "="
                    + URLEncoder.encode("" + store.getLocations().get(i).getLatitude(), "UTF-8")
                    + "&longitude" + i + "="
                    + URLEncoder.encode("" + store.getLocations().get(i).getLongitude(), "UTF-8")
                    + "&address" + i + "="
                    + URLEncoder.encode(store.getLocations().get(i).getAddress(), "UTF-8");
        }

        String result = httpPost(SERVER_URL + "AddStore.php", urlParameters);

        if (result.equals("location_error\n")) {
            locationError = true;
            return true;
        }
        return result.equals("success\n");
    }

    public static boolean deleteUser(User user) throws Exception {

        return deleteUser(user.getEmail(), user.getPassword());
    }

    public static boolean deleteUser(String email, String password) throws Exception {

        String urlParameters = "email=" + URLEncoder.encode(email, "UTF-8")
                + "&password=" + URLEncoder.encode(password, "UTF-8");
        String result = httpPost(SERVER_URL + "DeleteUser.php", urlParameters);

        return result.equals("success\n");
    }

    public static boolean addCampaign(Store store, Campaign campaign) throws Exception {

        return addCampaign(store.getEmail(), store.getPassword(), campaign);
    }

    public static boolean addCampaign(String email, String password, Campaign campaign) throws Exception {

        String image = httpPostFile(SERVER_URL + "UploadCampaignImage.php", campaign.getImage());

        String urlParameters = "email=" + URLEncoder.encode(email, "UTF-8")
                + "&password=" + URLEncoder.encode(password, "UTF-8")
                + "&start_date="
                + URLEncoder.encode(SQLDateFormat.format(campaign.getStartDate()), "UTF-8")
                + "&end_date="
                + URLEncoder.encode(SQLDateFormat.format(campaign.getEndDate()), "UTF-8")
                + "&image=" + URLEncoder.encode(image, "UTF-8")
                + "&type=" + URLEncoder.encode("" + campaign.getType().ordinal(), "UTF-8")
                + "&condition=" + URLEncoder.encode(campaign.getCondition(), "UTF-8")
                + "&details=" + URLEncoder.encode(campaign.getDetails(), "UTF-8")
                + "&percentage=" + URLEncoder.encode("" + campaign.getPercentage(), "UTF-8")
                + "&amount=" + URLEncoder.encode("" + campaign.getAmount(), "UTF-8");
        String result = httpPost(SERVER_URL + "AddCampaign.php", urlParameters);

        return result.equals("success\n");
    }

    public static ArrayList<Category> getCategoryList() throws Exception {

        String result = httpPost(SERVER_URL + "GetCategoryList.php", "");

        if (result.equals("failure\n"))
            throw new Exception("Unexpected Error In PHP File");

        ArrayList<Category> categories = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(result);

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            int id = jsonObject.getInt("category_id");
            String name = jsonObject.getString("category");

            categories.add(new Category(id, name));
        }
        return categories;
    }

    public static Store getStore(String email, String password, int store_id) throws Exception {
        String urlParameters = "email=" + URLEncoder.encode(email, "UTF-8")
                + "&password=" + URLEncoder.encode(password, "UTF-8")
                + "&store_id=" + URLEncoder.encode("" + store_id, "UTF-8");
        String result = httpPost(SERVER_URL + "GetStore.php", urlParameters);

        return parseStoreFromJSON(result);
    }

    public static ArrayList<Store> getStores(User user, ArrayList<Category> categories) throws Exception {

        int[] categoryIDs = new int[categories.size()];
        for(int i=0; i<categoryIDs.length;i++)
            categoryIDs[i] = categories.get(i).getCategoryId();
        return getStores(user.getEmail(), user.getPassword(), categoryIDs);
    }

    public static ArrayList<Store> getStores(String email, String password, int[] categoryIDs)
            throws Exception {

        String urlParameters = "email=" + URLEncoder.encode(email, "UTF-8")
                + "&password=" + URLEncoder.encode(password, "UTF-8")
                + "&category_count=" + URLEncoder.encode("" + categoryIDs.length, "UTF-8");

        for(int i=0; i<categoryIDs.length;i++)
            urlParameters += "&category_id_" + i + "=" + URLEncoder.encode("" + categoryIDs[i], "UTF-8");

        String result = httpPost(SERVER_URL + "GetStores.php", urlParameters);

        if (result.equals("failure\n"))
            throw new Exception("Unexpected Error In PHP File");

        return parseStoresFromJSON(result);
    }

    public static ArrayList<Store> getStores(User user, Category category) throws Exception {

        return getStores(user.getEmail(), user.getPassword(), category.getCategoryId());
    }

    public static ArrayList<Store> getStores(String email, String password, int categoryId)
            throws Exception {

        String urlParameters = "email=" + URLEncoder.encode(email, "UTF-8")
                + "&password=" + URLEncoder.encode(password, "UTF-8")
                + "&category_count=" + URLEncoder.encode("1", "UTF-8")
                + "&category_id_0=" + URLEncoder.encode("" + categoryId, "UTF-8");
        String result = httpPost(SERVER_URL + "GetStores.php", urlParameters);

        if (result.equals("failure\n"))
            throw new Exception("Unexpected Error In PHP File");

        return parseStoresFromJSON(result);
    }

    public static ArrayList<Campaign> getCampaigns(User user, Store store) throws Exception {

        return getCampaigns(user.getEmail(), user.getPassword(), store.getStoreId());
    }

    public static ArrayList<Campaign> getCampaigns(String email, String password, int storeId)
            throws Exception {

        String urlParameters = "email=" + URLEncoder.encode(email, "UTF-8")
                + "&password=" + URLEncoder.encode(password, "UTF-8")
                + "&store_id=" + URLEncoder.encode("" + storeId, "UTF-8");
        String result = httpPost(SERVER_URL + "GetCampaigns.php", urlParameters);

        if (result.equals("failure\n"))
            throw new Exception("Unexpected Error In PHP File");

        return parseCampaignsFromJSON(result);
    }

    public static boolean addFavoriteStore(User user, ArrayList<Store> stores) throws Exception {

        int[] storeIDs = new int[stores.size()];
        for(int i=0; i<storeIDs.length;i++)
            storeIDs[i] = stores.get(i).getStoreId();
        return addFavoriteStore(user.getEmail(), user.getPassword(), storeIDs);
    }

    public static boolean addFavoriteStore(String email, String password, int[] storeIDs) throws Exception {

        String urlParameters = "email=" + URLEncoder.encode(email, "UTF-8")
                + "&password=" + URLEncoder.encode(password, "UTF-8")
                + "&store_count=" + URLEncoder.encode("" + storeIDs.length, "UTF-8");

        for(int i=0; i<storeIDs.length;i++)
            urlParameters += "&store_id_" + i + "=" + URLEncoder.encode("" + storeIDs[i], "UTF-8");

        String result = httpPost(SERVER_URL + "AddFavoriteStore.php", urlParameters);

        return result.equals("success\n");
    }

    public static boolean addFavoriteStore(User user, Store store) throws Exception {

        return addFavoriteStore(user.getEmail(), user.getPassword(), store.getStoreId());
    }

    public static boolean addFavoriteStore(String email, String password, int storeId) throws Exception {

        String urlParameters = "email=" + URLEncoder.encode(email, "UTF-8")
                + "&password=" + URLEncoder.encode(password, "UTF-8")
                + "&store_count=" + URLEncoder.encode("1", "UTF-8")
                + "&store_id_0=" + URLEncoder.encode("" + storeId, "UTF-8");
        String result = httpPost(SERVER_URL + "AddFavoriteStore.php", urlParameters);

        return result.equals("success\n");
    }

    public static boolean addFavoriteCampaign(User user, Campaign campaign) throws Exception {

        return addFavoriteCampaign(user.getEmail(), user.getPassword(), campaign.getCampaignId());
    }

    public static boolean addFavoriteCampaign(String email, String password, int campaignId)
            throws Exception {

        String urlParameters = "email=" + URLEncoder.encode(email, "UTF-8")
                + "&password=" + URLEncoder.encode(password, "UTF-8")
                + "&campaign_id=" + URLEncoder.encode("" + campaignId, "UTF-8");
        String result = httpPost(SERVER_URL + "AddFavoriteCampaign.php", urlParameters);

        return result.equals("success\n");
    }

    public static ArrayList<Store> getFavoriteStores(User user) throws Exception {

        return getFavoriteStores(user.getEmail(), user.getPassword());
    }

    public static ArrayList<Store> getFavoriteStores(String email, String password) throws Exception {

        String urlParameters = "email=" + URLEncoder.encode(email, "UTF-8")
                + "&password=" + URLEncoder.encode(password, "UTF-8");
        String result = httpPost(SERVER_URL + "GetFavoriteStores.php", urlParameters);

        if (result.equals("failure\n"))
            throw new Exception("Unexpected Error In PHP File");

        return parseStoresFromJSON(result);
    }

    public static ArrayList<Campaign> getFavoriteCampaigns(User user) throws Exception {

        return getFavoriteCampaigns(user.getEmail(), user.getPassword());
    }

    public static ArrayList<Campaign> getFavoriteCampaigns(String email, String password) throws Exception {

        String urlParameters = "email=" + URLEncoder.encode(email, "UTF-8")
                + "&password=" + URLEncoder.encode(password, "UTF-8");
        String result = httpPost(SERVER_URL + "GetFavoriteCampaigns.php", urlParameters);

        if (result.equals("failure\n"))
            throw new Exception("Unexpected Error In PHP File");

        return parseCampaignsFromJSON(result);
    }

    public static boolean removeFavoriteStore(User user, Store store) throws Exception {

        return removeFavoriteStore(user.getEmail(), user.getPassword(), store.getStoreId());
    }

    public static boolean removeFavoriteStore(String email, String password, int storeId) throws Exception {

        String urlParameters = "email=" + URLEncoder.encode(email, "UTF-8")
                + "&password=" + URLEncoder.encode(password, "UTF-8")
                + "&store_id=" + URLEncoder.encode("" + storeId, "UTF-8");
        String result = httpPost(SERVER_URL + "RemoveFavoriteStore.php", urlParameters);

        return result.equals("success\n");
    }

    public static boolean removeFavoriteCampaign(User user, Campaign campaign) throws Exception {

        return removeFavoriteCampaign(user.getEmail(), user.getPassword(), campaign.getCampaignId());
    }

    public static boolean removeFavoriteCampaign(String email, String password, int campaignId)
            throws Exception {

        String urlParameters = "email=" + URLEncoder.encode(email, "UTF-8")
                + "&password=" + URLEncoder.encode(password, "UTF-8")
                + "&campaign_id=" + URLEncoder.encode("" + campaignId, "UTF-8");
        String result = httpPost(SERVER_URL + "RemoveFavoriteCampaign.php", urlParameters);

        return result.equals("success\n");
    }

    public static boolean enableLocationNotification(String email, String password,
                                                     boolean locationNotification) throws Exception {

        String urlParameters = "email=" + URLEncoder.encode(email, "UTF-8")
                + "&password=" + URLEncoder.encode(password, "UTF-8")
                + "&locationNotification=" + URLEncoder.encode(locationNotification ? "1" : "0", "UTF-8");
        String result = httpPost(SERVER_URL + "EnableLocationNotification.php", urlParameters);

        return result.equals("success\n");
    }

    public static boolean enableCampaignNotification(String email, String password,
                                                     boolean campaignNotification) throws Exception {

        String urlParameters = "email=" + URLEncoder.encode(email, "UTF-8")
                + "&password=" + URLEncoder.encode(password, "UTF-8")
                + "&campaignNotification=" + URLEncoder.encode(campaignNotification ? "1" : "0", "UTF-8");
        String result = httpPost(SERVER_URL + "EnableLocationNotification.php", urlParameters);

        return result.equals("success\n");
    }
}
