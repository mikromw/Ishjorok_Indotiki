package com.pmberjaya.indotiki.io;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.pmberjaya.indotiki.utilities.Utility;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;

/**
 * Created by eric on 3/4/2016.
 */
public class ProgressRequestBody extends RequestBody {
    private  File mFile;
    private  String mtype;
    private UploadCallbacks mListener;

    private static final int DEFAULT_BUFFER_SIZE = 1024;

    public interface UploadCallbacks {
        void onProgressUpdate(int percentage, String numberProgress);
        void onError();
        void onFinish();
    }


    public ProgressRequestBody(File file, String type , final  UploadCallbacks listener) {
        mFile = file;
        mtype = type;
        mListener = listener;
        Log.d(" file1", mFile.getPath());
    }

    @Override
    public MediaType contentType() {
        // i want to upload only images
        return MediaType.parse(mtype);
    }
    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        Log.d(" file2", mFile.getPath());
        long fileLength = mFile.length();
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        FileInputStream in = new FileInputStream(mFile);
        long uploaded = 0;
        Handler handler = null;
        Runnable updateProgressDialog = null;
        try {
            int read;
            handler = new Handler(Looper.getMainLooper());
            while ((read = in.read(buffer)) != -1) {

                // update progress on UI thread
                updateProgressDialog = new ProgressUpdater(uploaded, fileLength);
                handler.post(updateProgressDialog);

                uploaded += read;
                sink.write(buffer, 0, read);
            }

        } finally {
            in.close();
        }
    }

    private class ProgressUpdater implements Runnable {
        private long mUploaded;
        private long mTotal;
        public ProgressUpdater(long uploaded, long total) {
            mUploaded = uploaded;
            mTotal = total;
            Log.d(" mUploaded", ""+mUploaded);
            Log.d(" mTotal", ""+mTotal);
        }

        @Override
        public void run() {
            Log.d(" JALAN", "sa");
            Log.d(" mUploaded2222", ""+mUploaded);
            Log.d(" mTotal22222222", ""+mTotal);
            int kb_mUploaded = Math.round(mUploaded/1024);
            int kb_mTotal = Math.round(mTotal/1024);
            if(kb_mUploaded!=kb_mTotal) {
                mListener.onProgressUpdate((int) (100 * mUploaded / mTotal), Utility.getInstance().convertPrice(kb_mUploaded) + "/" + Utility.getInstance().convertPrice(kb_mTotal) + " KB");
            }
            else {
                mListener.onProgressUpdate(100, "Processing...");
            }

        }
    }
}
