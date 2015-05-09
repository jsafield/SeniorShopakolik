package com.shopakolik.seniorproject.controller.transfercontroller;
/*
 * DOWNLOAD MODEL
 *
 */


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.amazonaws.services.s3.model.ProgressEvent;
import com.amazonaws.services.s3.model.ProgressListener;
import com.amazonaws.mobileconnectors.s3.transfermanager.Download;
import com.amazonaws.mobileconnectors.s3.transfermanager.PersistableDownload;
import com.amazonaws.mobileconnectors.s3.transfermanager.Transfer;
import com.amazonaws.mobileconnectors.s3.transfermanager.TransferManager;
import com.amazonaws.mobileconnectors.s3.transfermanager.exception.PauseException;
import com.shopakolik.seniorproject.model.shopakolikelements.Constants;
import com.shopakolik.seniorproject.model.shopakolikelements.TransferModel;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

/*
 *
 * DOWNLOAD MODEL for AWS
 */
public class DownloadModel extends TransferModel {
    private static final String TAG = "DownloadModel";

    private Download mDownload;
    private PersistableDownload mPersistableDownload;
    private ProgressListener mListener;
    private String mKey;
    private Status mStatus;
    private Uri mUri;
    private Context mContext;

    public DownloadModel(Context context, String key, TransferManager manager) {
        super(context, Uri.parse(key), manager);
        mContext = context;
        mKey = key;
        mStatus = Status.IN_PROGRESS;
        mListener = new ProgressListener() {
            @Override
            public void progressChanged(ProgressEvent event) {
                if (event.getEventCode() == ProgressEvent.COMPLETED_EVENT_CODE) {

                    Intent mediaScanIntent = new Intent(
                            Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    mediaScanIntent.setData(mUri);
                    getContext().sendBroadcast(mediaScanIntent);

                    mStatus = Status.COMPLETED;

                }
            }
        };
    }

    @Override
    public Status getStatus() {
        return mStatus;
    }

    @Override
    public Transfer getTransfer() {
        return mDownload;
    }

    @Override
    public Uri getUri() {
        return mUri;
    }

    @Override
    public void abort() {
        if (mDownload != null) {
            mStatus = Status.CANCELED;
            try {
                mDownload.abort();
            } catch (IOException e) {
                Log.e(TAG, "", e);
            }
        }
    }

    public void download() {
        mStatus = Status.IN_PROGRESS;
        File file = new File(
                Environment.getExternalStoragePublicDirectory(
                        "Shop"),
                getFileName());
        File f = new File(mContext.getExternalCacheDir(), "mytextfile.txt" );

        mUri = Uri.fromFile(file);

        mDownload = getTransferManager().download(
                Constants.BUCKET_NAME.toLowerCase(Locale.US), mKey, file);
        if (mListener != null) {
            mDownload.addProgressListener(mListener);
        }
    }

    @Override
    public void pause() {
        if (mStatus == Status.IN_PROGRESS) {
            mStatus = Status.PAUSED;
            try {
                mPersistableDownload = mDownload.pause();
            } catch (PauseException e) {
                Log.d(TAG, "", e);
            }
        }
    }

    @Override
    public void resume() {
        if (mStatus == Status.PAUSED) {
            mStatus = Status.IN_PROGRESS;
            if (mPersistableDownload != null) {
                mDownload = getTransferManager().resumeDownload(
                        mPersistableDownload);
                mDownload.addProgressListener(mListener);
                mPersistableDownload = null;
            } else {
                download();
            }
        }
    }
}
