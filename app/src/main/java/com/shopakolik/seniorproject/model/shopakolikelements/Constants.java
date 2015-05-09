

package com.shopakolik.seniorproject.model.shopakolikelements;


import android.util.Log;

import com.shopakolik.seniorproject.controller.databasecontroller.DatabaseManager;

public class Constants {

    private static Constants ourInstance;
    public static String AWS_ACCOUNT_ID ;
    public static String COGNITO_POOL_ID;
    public static String COGNITO_ROLE_UNAUTH;

    public static Constants getInstance() {
        return ourInstance;
    }

    public Constants() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                setCredentials();
            }
        });
        t.start();
        try {
            t.join();
            ourInstance = this;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void setCredentials(){
        try {
            Credentials cred = DatabaseManager.getCredentials();
            AWS_ACCOUNT_ID = cred.getAcid();
            COGNITO_POOL_ID = cred.getPoolid();
            COGNITO_ROLE_UNAUTH = cred.getRole();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //
    //

    //
    //
    //
    public static final String BUCKET_NAME = "shopakolik";

}
