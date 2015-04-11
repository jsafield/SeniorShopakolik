package com.shopakolik.seniorproject.controller.databasecontroller;

import android.util.Log;

import com.shopakolik.seniorproject.model.shopakolikelements.Campaign;
import com.shopakolik.seniorproject.model.shopakolikelements.Category;
import com.shopakolik.seniorproject.model.shopakolikelements.Customer;
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

/**
 * Ahmet Cihat Unaldi
 */

public class DatabaseManager {

    private static final String SERVER_URL = "http://unaldi.0fees.us/shopakolik/";
    private static boolean locationError = false;
    private static SimpleDateFormat SQLDateFormat = new SimpleDateFormat("yyyy-mm-dd");

    public static boolean isLocationsAdded() {
        return locationError;
    }

    // send http post request and read http response
    public String httpPost(String url, String urlParameters) throws Exception {

        URL url0;
        HttpURLConnection conn;
        DataOutputStream dos;
        InputStream is;
        String result;

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
        dos = new DataOutputStream(conn.getOutputStream());
        dos.writeBytes(urlParameters);
        dos.flush();
        dos.close();

        is = conn.getInputStream();

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
    public String httpPostFile(String url, String sourceFileUri) throws Exception {

        URL url0;
        HttpURLConnection conn;
        File sourceFile;
        FileInputStream fis;
        DataOutputStream dos;
        InputStream is;
        String result;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;

        sourceFile = new File(sourceFileUri);
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
        dos = new DataOutputStream(conn.getOutputStream());

        dos.writeBytes(twoHyphens + boundary + lineEnd);
        dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
                + sourceFileUri + "\"" + lineEnd);
        dos.writeBytes(lineEnd);

        // create a buffer of  maximum size
        bytesAvailable = fis.available();
        bufferSize = Math.min(bytesAvailable, maxBufferSize);
        buffer = new byte[bufferSize];

        // read file and write it into form
        bytesRead = fis.read(buffer, 0, bufferSize);

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
        is = conn.getInputStream();

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
        if (result.equals("failure")) {
            throw new Exception("File Upload Failure");
        }
        return result;
    }

    public UserType login(User user) throws Exception {

        return login(user.getEmail(), user.getPassword());
    }

    public UserType login(String email, String password) throws Exception {

        String urlParameters = "email=" + URLEncoder.encode(email, "UTF-8")
                + "&password=" + URLEncoder.encode(password, "UTF-8");
        String result = httpPost(SERVER_URL + "Login.php", urlParameters);

        if (result.equals("customer"))
            return UserType.Customer;
        if (result.equals("store"))
            return UserType.Store;
        if (result.equals("non_user"))
            return UserType.NonUser;

        throw new Exception("Unexpected Error In PHP File");
    }

    public boolean addCustomer(Customer customer) throws Exception {

        String urlParameters = "name=" + URLEncoder.encode(customer.getName(), "UTF-8")
                + "&surname=" + URLEncoder.encode(customer.getSurname(), "UTF-8")
                + "&email=" + URLEncoder.encode(customer.getEmail(), "UTF-8")
                + "&password=" + URLEncoder.encode(customer.getPassword(), "UTF-8")
                + "&locationNotification="
                + URLEncoder.encode(customer.isLocationNotification() ? "1" : "0", "UTF-8")
                + "&campaignNotification="
                + URLEncoder.encode(customer.isCampaignNotification() ? "1" : "0", "UTF-8");
        String result = httpPost(SERVER_URL + "AddCustomer.php", urlParameters);

        return result.equals("success");
    }

    public boolean addStore(Store store) throws Exception {

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

        if (result.equals("location_error")) {
            locationError = true;
            return true;
        }
        return result.equals("success");
    }

    public boolean deleteUser(User user) throws Exception {

        String urlParameters = "email=" + URLEncoder.encode(user.getEmail(), "UTF-8")
                + "&password=" + URLEncoder.encode(user.getPassword(), "UTF-8");
        String result = httpPost(SERVER_URL + "DeleteUser.php", urlParameters);

        return result.equals("success");
    }

    public boolean addCampaign(String email, String password, Campaign campaign) throws Exception {

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

        return result.equals("success");
    }

    public ArrayList<Category> getCategoryList(String email, String password) throws Exception {

        String urlParameters = "email=" + URLEncoder.encode(email, "UTF-8")
                + "&password=" + URLEncoder.encode(password, "UTF-8");
        String result = httpPost(SERVER_URL + "GetCategoryList.php", urlParameters);

        if (result.equals("failure"))
            throw new Exception("Unexpected Error In PHP File");

        ArrayList<Category> categories = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(result);
        JSONObject jsonObject;
        int id;
        String name;
        for (int i = 0; i<jsonArray.length(); i++) {
            jsonObject = jsonArray.getJSONObject(i);
            id = jsonObject.getInt("category_id");
            name = jsonObject.getString("category");
            categories.add(new Category(id, name));
        }
        return categories;
    }
}
