package com.pmberjaya.indotiki.services;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import com.pmberjaya.indotiki.config.Config;
import com.pmberjaya.indotiki.config.Constants;
import com.pmberjaya.indotiki.R;
import com.pmberjaya.indotiki.dao.DBController;
import com.pmberjaya.indotiki.io.ApiInterface;
import com.pmberjaya.indotiki.models.chat.ChatMessage;
import com.pmberjaya.indotiki.models.chat.Download;
import com.pmberjaya.indotiki.utilities.Utility;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;

/**
 * Created by edwin on 20/10/2016.
 */

public class DownloadService extends IntentService {

    public DownloadService() {
        super("Download Service");
    }

    private NotificationCompat.Builder notificationBuilder;
    private NotificationManager notificationManager;
    private int totalFileSize;
    private int id_notification=3510;
    private DBController dbController;
    @Override
    protected void onHandleIntent(Intent intent) {

        dbController = DBController.getInstance(this);
        String chat_database_id = intent.getStringExtra("chat_database_id");
        String imagePath = intent.getStringExtra("image_path");
        String link_image_small = intent.getStringExtra("link_image_small");
        String request_id = intent.getStringExtra("request_id");
        String request_type = intent.getStringExtra("request_type");
        String type = intent.getStringExtra("type");
        long timestamp = intent.getLongExtra("timestamp",0);
        int position= intent.getIntExtra("position",0);
        ArrayList<ChatMessage> chat = intent.getParcelableArrayListExtra("chat");
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_base_file_download_black)
                .setContentTitle("Download")
                .setContentText("Downloading File")
                .setAutoCancel(true);
        notificationManager.notify(id_notification+position, notificationBuilder.build());
        initDownload(chat_database_id, imagePath,request_id,request_type,timestamp,position,link_image_small,type,chat);

    }

    private void initDownload(String chat_database_id, String imagePath, String request_id, String request_type, long timestamp, int position,String link_image_small, String type,ArrayList<ChatMessage> chat){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.APP_IMAGE_URL)
                .build();

        ApiInterface retrofitInterface = retrofit.create(ApiInterface.class);

        Call<ResponseBody> request = retrofitInterface.downloadFile(imagePath);
        try {
            downloadFile(chat_database_id, request.execute().body(), request_id,  request_type, timestamp,position,imagePath,link_image_small, type,chat);
        } catch (IOException e) {

            e.printStackTrace();
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
            onDownloadFail(position,chat);
        }
    }
    private void downloadFile(String chat_database_id, ResponseBody body, String request_id, String request_type, long timestamp,int position, String imagePath, String link_image_small, String type, ArrayList<ChatMessage> chat) throws IOException {
        int count;
        byte data[] = new byte[1024 * 4];
        long fileSize = body.contentLength();
        InputStream bis = new BufferedInputStream(body.byteStream(), 1024 * 8);
        String image_ext = ".JPG";
        final String imageName = "IMG-"+ Utility.getInstance().formatTimeStamp(timestamp,"yyyyMMdd")+"-"+Utility.getInstance().formatTimeStamp(timestamp,"HHmmSS")+image_ext;
        String imageDir = Environment.getExternalStorageDirectory().getAbsolutePath() +"/"+getResources().getString(R.string.app_name)+"/"+request_type+"/"+request_id+"/"+type+"/";
        String filePath = imageDir+imageName;
        File folderReceived = new File(imageDir);
        if(!folderReceived.exists()){
            folderReceived.mkdirs();
        }
        File outputFile = new File(filePath);
        OutputStream output = new FileOutputStream(outputFile);
        long total = 0;
        long startTime = System.currentTimeMillis();
        int timeCount = 1;
        while ((count = bis.read(data)) != -1) {

            total += count;
            totalFileSize = (int) (fileSize / 1024);
            double current = Math.round(total / 1024);

            int progress = (int) ((total * 100) / fileSize);

            long currentTime = System.currentTimeMillis() - startTime;

            Download download = new Download();
            download.setTotalFileSize(totalFileSize);

            download.setStatusDownload("downloading");
            if (currentTime > 1000 * timeCount) {
                download.setCurrentFileSize((int) current);
                download.setProgress(progress);
                sendNotification(download, position,chat);
                timeCount++;
            }

            output.write(data, 0, count);
        }
        onDownloadComplete(chat_database_id, position, timestamp, imagePath, link_image_small, filePath,chat);
        output.flush();
        output.close();
        bis.close();
    }

    private void sendNotification(Download download, int position, ArrayList<ChatMessage>chat){

        sendIntent(download, position,chat);
        notificationBuilder.setProgress(100,download.getProgress(),false);
        notificationBuilder.setContentText(String.format("Downloaded (%d/%d) KB",download.getCurrentFileSize(),download.getTotalFileSize()));
        notificationManager.notify(id_notification+position, notificationBuilder.build());
    }


    private void sendIntent(Download download, int position,ArrayList<ChatMessage> chat){

        Intent intent = new Intent(Constants.BROADCAST_DOWNLOAD_RECEIVER);
        intent.putExtra("download",download);
        intent.putExtra("position",position);
        intent.putExtra("chat",chat);
        LocalBroadcastManager.getInstance(DownloadService.this).sendBroadcast(intent);
    }

    private void onDownloadComplete(String chat_database_id, int position, long timestamp, String imagePath, String link_image_small, String filePath, ArrayList<ChatMessage> chat){
        dbController.updateChat(chat_database_id, String.valueOf(timestamp),"success",imagePath,link_image_small, filePath);
        Download download = new Download();
        download.setStatusDownload("completed");
        download.setFilePath(filePath);
        download.setProgress(100);
        sendIntent(download,position,chat);

        notificationManager.cancel(0);
        notificationBuilder.setProgress(0,0,false);
        notificationBuilder.setContentText("File Downloaded");
        notificationManager.notify(id_notification+position, notificationBuilder.build());

    }
    private void onDownloadFail(int position, ArrayList<ChatMessage> chat){
        Download download = new Download();
        download.setStatusDownload("failed");
        download.setProgress(0);
        sendIntent(download,position,chat);

        notificationManager.cancel(0);
        notificationBuilder.setProgress(0,0,false);
        notificationBuilder.setContentText("Download Failed");
        notificationManager.notify(id_notification+position, notificationBuilder.build());

    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        notificationManager.cancel(0);
    }

}


