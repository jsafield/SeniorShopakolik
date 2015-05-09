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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.content.Context;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.amazonaws.services.s3.model.DeleteObjectsRequest.KeyVersion;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.shopakolik.seniorproject.controller.databasecontroller.DatabaseManager;

/* 
 *
 */
public class Util {
    private static AmazonS3Client sS3Client;
    private static CognitoCachingCredentialsProvider sCredProvider;
    private static Credentials creds;

    public static CognitoCachingCredentialsProvider getCredProvider(Context context) throws InterruptedException {
        if (sCredProvider == null) {

            if(Constants.AWS_ACCOUNT_ID == null || Constants.COGNITO_POOL_ID == null ||
                    Constants.COGNITO_ROLE_UNAUTH == null) {

                Thread cT = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            creds = DatabaseManager.getCredentials();
                            Constants.AWS_ACCOUNT_ID = creds.getAcid();
                            Constants.COGNITO_POOL_ID = creds.getPoolid();
                            Constants.COGNITO_ROLE_UNAUTH = creds.getRole();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                cT.start();
                cT.join();

            }
            sCredProvider = new CognitoCachingCredentialsProvider(
                    context,
                    Constants.AWS_ACCOUNT_ID,
                    Constants.COGNITO_POOL_ID,
                    Constants.COGNITO_ROLE_UNAUTH,
                    null,
                    Regions.US_EAST_1);
        }
        return sCredProvider;
    }

    public static String getPrefix(Context context) throws InterruptedException {
        return getCredProvider(context).getIdentityId() + "/";

    }

    public static AmazonS3Client getS3Client(Context context) throws InterruptedException {
        if (sS3Client == null) {
            sS3Client = new AmazonS3Client(getCredProvider(context));
        }
        return sS3Client;
    }

    public static String getFileName(String path) {
        return path.substring(path.lastIndexOf("/") + 1);
    }

    public static boolean doesBucketExist() {
        return sS3Client.doesBucketExist(Constants.BUCKET_NAME.toLowerCase(Locale.US));
    }

    public static void createBucket() {
        sS3Client.createBucket(Constants.BUCKET_NAME.toLowerCase(Locale.US));
    }

    public static void deleteBucket() {
        String name = Constants.BUCKET_NAME.toLowerCase(Locale.US);
        List<S3ObjectSummary> objData = sS3Client.listObjects(name).getObjectSummaries();
        if (objData.size() > 0) {
            DeleteObjectsRequest emptyBucket = new DeleteObjectsRequest(name);
            List<KeyVersion> keyList = new ArrayList<KeyVersion>();
            for (S3ObjectSummary summary : objData) {
                keyList.add(new KeyVersion(summary.getKey()));
            }
            emptyBucket.withKeys(keyList);
            sS3Client.deleteObjects(emptyBucket);
        }
        sS3Client.deleteBucket(name);
    }
}
