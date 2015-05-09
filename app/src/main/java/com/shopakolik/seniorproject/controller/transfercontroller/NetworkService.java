

package com.shopakolik.seniorproject.controller.transfercontroller;

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;

import com.amazonaws.mobileconnectors.s3.transfermanager.TransferManager;
import com.shopakolik.seniorproject.model.shopakolikelements.TransferModel;
import com.shopakolik.seniorproject.model.shopakolikelements.UploadModel;
import com.shopakolik.seniorproject.model.shopakolikelements.Util;

/*
 *
 *
 */
public class NetworkService extends IntentService {
    public static final String S3_KEYS_EXTRA = "keys";
    public static final String ACTION_ABORT = "abort";
    public static final String ACTION_PAUSE = "pause";
    public static final String ACTION_RESUME = "resume";
    public static final String NOTIF_ID_EXTRA = "notification_id";

    private static final String TAG = "NetworkService";
    private static final int DEFAULT_INT = -1;

    private TransferManager mTransferManager;

    public NetworkService() {
        super(TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            mTransferManager = new TransferManager(Util.getCredProvider(this));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onHandleIntent(Intent intent) {
        if (intent != null && intent.getAction() != null) {
            if (intent.getAction().equals(Intent.ACTION_GET_CONTENT) &&
                    intent.getStringArrayExtra(S3_KEYS_EXTRA) != null) {
                download(intent.getStringArrayExtra(S3_KEYS_EXTRA));
            } else if (intent.getAction().equals(Intent.ACTION_SEND) &&
                    intent.getData() != null) {
                upload(intent.getData(), intent.getExtras().getString("fileName"));
            } else if (intent.getIntExtra(NOTIF_ID_EXTRA, DEFAULT_INT)
                    != DEFAULT_INT) {
                int notifId = intent.getIntExtra(NOTIF_ID_EXTRA, DEFAULT_INT);
                if (intent.getAction().equals(ACTION_PAUSE)) {
                    pause(notifId);
                } else if (intent.getAction().equals(ACTION_ABORT)) {
                    abort(notifId);
                } else if (intent.getAction().equals(ACTION_RESUME)) {
                    resume(notifId);
                }
            }
        }
    }

    private void abort(int notifId) {
        TransferModel model = TransferModel.getTransferModel(notifId);
        model.abort();
    }

    private void download(String[] keys) {
        for (int i = 0; i < keys.length; i++) {
            DownloadModel model = new DownloadModel(this, keys[i],
                    mTransferManager);
            model.download();
        }
    }

    private void pause(int notifId) {
        TransferModel model = TransferModel.getTransferModel(notifId);
        model.pause();
    }

    private void resume(int notifId) {
        TransferModel model = TransferModel.getTransferModel(notifId);
        model.resume();
    }

    /* We use a new thread for upload because we have to copy the file */
    private void upload(Uri uri, String fileName) {
        UploadModel model = new UploadModel(this, uri, mTransferManager, fileName);
        new Thread(model.getUploadRunnable()).run();
    }
}
