package com.shopakolik.seniorproject.controller.databasecontroller;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Author: Ahmet Cihat Ünaldı && Zehra Uludağ
 */

public class DatabaseManager {

    public boolean login(String email, String password) throws Exception {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("email", email));
        nameValuePairs.add(new BasicNameValuePair("password", password));
        InputStream is;
        String result;

        //http post
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://unaldi.0fees.us/shopakolik/login.php");
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();

        } catch (IOException e) {
            throw new IOException("Cannot connect to the server!");
        }

        //convert response to string
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
            is.close();

            result = sb.toString();
        } catch (IOException e) {
            throw new IOException("Error while converting result!");
        }

        if (result.equals("connection error\n")) {
            throw new Exception("Cannot connect to the database server!");
        }

        //parse json data
        try {
            JSONObject json_data = new JSONObject(result);
            return json_data.getBoolean("loggedin");
        } catch (JSONException e) {
            // The php file is not exist or its content not appropriate
            throw new JSONException("Error while parsing data!");
        }
    }

    public boolean addCustomer(String name, String surname, String email, String password) throws Exception {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("name", name));
        nameValuePairs.add(new BasicNameValuePair("surname", surname));
        nameValuePairs.add(new BasicNameValuePair("email", email));
        nameValuePairs.add(new BasicNameValuePair("password", password));
        InputStream is;
        String result;

        //http post
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://unaldi.0fees.us/shopakolik/addCustomer.php");
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();

        } catch (IOException e) {
            throw new IOException("Cannot connect to the server!");
        }

        //convert response to string
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
            is.close();

            result = sb.toString();
        } catch (IOException e) {
            throw new IOException("Error while converting result!");
        }

        if (result.equals("connection error\n")) {
            throw new Exception("Cannot connect to the database server!");
        }

        //parse json data
        try {
            JSONObject json_data = new JSONObject(result);
            return json_data.getBoolean("customeradded");

        } catch (JSONException e) {
            // The php file is not exist or its content not appropriate
            throw new JSONException("Error while parsing data!");
        }
    }

    public boolean addStore(String name, String logo, String email, String password) throws Exception {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("name", name));
        nameValuePairs.add(new BasicNameValuePair("logo", logo));
        nameValuePairs.add(new BasicNameValuePair("email", email));
        nameValuePairs.add(new BasicNameValuePair("password", password));
        InputStream is;
        String result;

        //http post
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://unaldi.0fees.us/shopakolik/addStore.php");
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();

        } catch (IOException e) {
            throw new IOException("Cannot connect to the server!");
        }

        //convert response to string
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
            is.close();

            result = sb.toString();
        } catch (IOException e) {
            throw new IOException("Error while converting result!");
        }

        if (result.equals("connection error\n")) {
            throw new Exception("Cannot connect to the database server!");
        }

        //parse json data
        try {
            JSONObject json_data = new JSONObject(result);
            return json_data.getBoolean("storeadded");

        } catch (JSONException e) {
            // The php file is not exist or its content not appropriate
            throw new JSONException("Error while parsing data!");
        }
    }

    public boolean deleteUser(String email) throws Exception {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("email", email));
        InputStream is;
        String result;

        //http post
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://unaldi.0fees.us/shopakolik/deleteUser.php");
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();

        } catch (IOException e) {
            throw new IOException("Cannot connect to the server!");
        }

        //convert response to string
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
            is.close();

            result = sb.toString();
        } catch (IOException e) {
            throw new IOException("Error while converting result!");
        }

        if (result.equals("connection error\n")) {
            throw new Exception("Cannot connect to the database server!");
        }

        //parse json data
        try {
            JSONObject json_data = new JSONObject(result);
            return json_data.getBoolean("userdeleted");

        } catch (JSONException e) {
            // The php file is not exist or its content not appropriate
            throw new JSONException("Error while parsing data!");
        }
    }

    public boolean updateCustomer(String currentEmail, String name, String surname, String email, String password) throws Exception {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("email", currentEmail));
        nameValuePairs.add(new BasicNameValuePair("name", name));
        nameValuePairs.add(new BasicNameValuePair("surname", surname));
        nameValuePairs.add(new BasicNameValuePair("newemail", email));
        nameValuePairs.add(new BasicNameValuePair("password", password));
        InputStream is;
        String result;

        //http post
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://unaldi.0fees.us/shopakolik/updateCustomer.php");
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();

        } catch (IOException e) {
            throw new IOException("Cannot connect to the server!");
        }

        //convert response to string
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
            is.close();

            result = sb.toString();
        } catch (IOException e) {
            throw new IOException("Error while converting result!");
        }

        if (result.equals("connection error\n")) {
            throw new Exception("Cannot connect to the database server!");
        }

        //parse json data
        try {
            JSONObject json_data = new JSONObject(result);
            return json_data.getBoolean("customerupdated");

        } catch (JSONException e) {
            // The php file is not exist or its content not appropriate
            throw new JSONException("Error while parsing data!");
        }
    }

    public boolean updateStore(String currentEmail, String name, String logo, String email, String password) throws Exception {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("email", currentEmail));
        nameValuePairs.add(new BasicNameValuePair("name", name));
        nameValuePairs.add(new BasicNameValuePair("logo", logo));
        nameValuePairs.add(new BasicNameValuePair("newemail", email));
        nameValuePairs.add(new BasicNameValuePair("password", password));
        InputStream is;
        String result;

        //http post
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://unaldi.0fees.us/shopakolik/updateStore.php");
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();

        } catch (IOException e) {
            throw new IOException("Cannot connect to the server!");
        }

        //convert response to string
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
            is.close();

            result = sb.toString();
        } catch (IOException e) {
            throw new IOException("Error while converting result!");
        }

        if (result.equals("connection error\n")) {
            throw new Exception("Cannot connect to the database server!");
        }

        //parse json data
        try {
            JSONObject json_data = new JSONObject(result);
            return json_data.getBoolean("storeupdated");

        } catch (JSONException e) {
            // The php file is not exist or its content not appropriate
            throw new JSONException("Error while parsing data!");
        }
    }
}
