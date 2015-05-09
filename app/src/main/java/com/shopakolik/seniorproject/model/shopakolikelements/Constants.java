/*
 * Copyright 2010-2014 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

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
