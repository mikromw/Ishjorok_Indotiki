package com.pmberjaya.indotiki.utilities;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.pmberjaya.indotiki.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by edwin on 09/07/2016.
 */
public class PicassoLoader{

    public static void loadImage(Context context, String url, ImageView image){
        if(url!=null && !url.equals("")) {
            Picasso.with(context).load(url).error(R.mipmap.img_no_image).noFade().into(image);
        }else{
            loadImageFail(context, image);
        }
    }

    public static void loadImageEvent(Context context, String url, int width, int height, ImageView image){
        if(url!=null && !url.equals("")) {
            Picasso.with(context).load(url).error(R.mipmap.img_no_image).resize(width,height).noFade().into(image);
        }else{
            loadImageFail(context, image);
        }
    }

    public static void loadImageFail(Context context, ImageView image){
        Picasso.with(context).load(R.mipmap.img_no_image).error(R.mipmap.img_no_image).into(image);
    }

    public static void loadImageFile(Context context, File file, ImageView image){

        Glide.with(context).load(file)
                .apply(Utility.getInstance().setGlideOptions(300, 300))
                .into(image);
    }

    public static void loadProfile(Context context,String url, ImageView image, int imageDrawableFailed){
        if(Utility.getInstance().checkIfStringIsNotNullOrEmpty(url)) {
            Picasso.with(context).load(url).error(imageDrawableFailed).into(image);
        }else{
            loadProfileFail(context, image, imageDrawableFailed);
        }
    }

    public static void loadProfileFail(Context context, ImageView image, int imageDrawableFailed){
        Picasso.with(context).load(imageDrawableFailed).error(imageDrawableFailed).into(image);
    }
    public static void loadDefaultEvent(Context context, ImageView image){
        Picasso.with(context).load(R.mipmap.img_no_image).error(R.mipmap.img_no_image).into(image);
    }
    public static String saveImageToFile(Context context, String sourceFile, long timestamp, String request_id, String request_type) throws IOException {
        String image_ext = ".JPG";
        final String imageName = "IMG-"+Utility.getInstance().formatTimeStamp(timestamp,"yyyyMMdd")+"-"+Utility.getInstance().formatTimeStamp(timestamp,"HHmmSS")+image_ext;
        String imageDir = Environment.getExternalStorageDirectory().getAbsolutePath() +"/"+context.getResources().getString(R.string.app_name)+"/"+request_type+"/"+request_id+"/"+"Sent/";

        File savePhoto = new File(imageDir);
        if(!savePhoto.exists()){
            savePhoto.mkdirs();
        }
        File source = new File (sourceFile);
        String filePath = imageDir+imageName;
        File output = new File(filePath);
        try
        {
            FileUtils.copyFile(source, output);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return filePath;
    }
    static boolean isSuccessLoad;

    public static boolean loadImageFromDir(Context context, String image_path, ImageView ivImage){
        File myImageFile = new File(image_path);

        Picasso.with(context).load(myImageFile).into(ivImage, new Callback() {
            @Override
            public void onSuccess() {
                isSuccessLoad = true;
            }
            @Override
            public void onError() {
                isSuccessLoad = false;
            }

        });
        return isSuccessLoad;
    }
    public static void deleteImageFromDir(Context context, String request_type, String request_id) {
        File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() +"/"+context.getResources().getString(R.string.app_name)+"/"+request_type+"/"+request_id+"/");
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                new File(dir, children[i]).delete();
            }
        }
    }
//    public static Target downloadPicassoImageTarget(Context context, long timestamp, String request_id, String request_type) {
//        Log.d("picassoImageTarget", " picassoImageTarget");
//        final String imageName = "IMG-"+Utility.getInstance().formatTimeStamp(timestamp,"yyyyMMdd")+"-"+Utility.getInstance().formatTimeStamp(timestamp,"HHmmSS");
//        String imageDir = context.getResources().getString(R.string.app_name)+"/"+request_type+"/"+request_id+"/"+"Received";
//        ContextWrapper cw = new ContextWrapper(context);
//        final File directory = cw.getDir(imageDir, Context.MODE_PRIVATE); // path to /data/data/yourapp/app_imageDir
//        return new Target() {
//            @Override
//            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        final File myImageFile = new File(directory, imageName); // Create image file
//                        FileOutputStream fos = null;
//                        try {
//                            fos = new FileOutputStream(myImageFile);
//                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        } finally {
//                            try {
//                                fos.close();
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                        Log.i("image", "image saved to >>>" + myImageFile.getAbsolutePath());
//
//                    }
//                }).start();
//            }
//
//            @Override
//            public void onBitmapFailed(Drawable errorDrawable) {
//
//            }
//
//            @Override
//            public void onPrepareLoad(Drawable placeHolderDrawable) {
//                if (placeHolderDrawable != null) {}
//            }
//        };
//    }
}

