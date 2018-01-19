package com.pmberjaya.indotiki.app.chat;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pmberjaya.indotiki.R;
import com.pmberjaya.indotiki.interfaces.chat.ClickListenerChat;
import com.pmberjaya.indotiki.models.chat.ChatMessage;
import com.pmberjaya.indotiki.models.chat.Download;
import com.pmberjaya.indotiki.models.chat.FileModel;
import com.pmberjaya.indotiki.utilities.Utility;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by edwin on 18/10/2016.
 */

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<ChatMessage> values;
    private Context context;
    private Set<String> onlineNow = new HashSet<String>();
    String request_id;
    String request_type;
    String userid;
    RecyclerView.ViewHolder mh;
    String  send_latitude;
    String  send_longitude;
    private ClickListenerChat mClickListenerChatFirebase;
    public ChatAdapter(Context context, ArrayList<ChatMessage> values, String request_id, String request_type, String userid, ClickListenerChat mClickListenerChatFirebase) {
        this.values = values;
        this.context = context;
        this.request_id=request_id;
        this.request_type=request_type;
        this.userid = userid;
        this.mClickListenerChatFirebase = mClickListenerChatFirebase;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        switch (viewType) {
            case driver:
                View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.chat_row_left, null);
                mh = new ItemRowHolderLeft(v);
                break;
            case customer:
                View v1 = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.chat_row_right, null);
                mh = new ItemRowHolderRight(v1);
        }
//
        return mh;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder itemRowHolder, final int position) {
        switch (itemRowHolder.getItemViewType()) {
            case driver:
                ItemRowHolderLeft driverViewHolder = (ItemRowHolderLeft) itemRowHolder;
                configureDriverChat(driverViewHolder, position);
                break;
            case customer:
                ItemRowHolderRight memberViewHolder = (ItemRowHolderRight) itemRowHolder;
                configureCustomerChat(memberViewHolder, position);
                break;
        }
    }

    private void configureDriverChat(ItemRowHolderLeft vh1, int position) {
        final ChatMessage chatMsg = this.values.get(position);
        vh1.setTxtMessage(chatMsg.getMessage());
        vh1.setTvTimestamp(chatMsg.getTimeStamp());
        vh1.setIvChatPhoto(chatMsg.getFileModel().getLink_image_small(), chatMsg.getFileModel().getLocation_image());
        vh1.setDownload(chatMsg.getDownloadServiceData());
        vh1.setLocation(chatMsg.getMapModel().getLatitude(),chatMsg.getMapModel().getLongitude(),chatMsg.getMapModel().getAddress());
    }

    private void configureCustomerChat(ItemRowHolderRight vh2, int position) {
        final ChatMessage chatMsg = this.values.get(position);
        vh2.setTxtMessage(chatMsg.getMessage());
        vh2.setTvTimestamp(chatMsg.getTimeStamp());
        vh2.setStatus(chatMsg.getStatus());
        vh2.setIvChatPhoto(chatMsg.getFileModel().getLink_image_small(), chatMsg.getFileModel().getLocation_image());
        vh2.setLocation(chatMsg.getMapModel().getLatitude(),chatMsg.getMapModel().getLongitude(),chatMsg.getMapModel().getAddress());
        vh2.setStatus(chatMsg.getStatus());
    }

    public static final int driver = 1;
    public static final int customer = 0;

    @Override
    public int getItemViewType(int position) {
        if (values.get(position).getUserChatModel().getUserlevel()==1) {
            return driver;
        } else if (values.get(position).getUserChatModel().getUserlevel()==0) {
            return customer;
        }
        return -1;
//        return values.get(position).getUserChatModel().getUserlevel();
    }

    @Override
    public int getItemCount() {
//        return (null != values ? values.size() : 0);
        return (values.size());
    }

    public class ItemRowHolderLeft extends RecyclerView.ViewHolder implements View.OnClickListener{
        protected TextView txtMessage;
        protected TextView tvTimestamp;
        protected ImageView iv_chat_image;
        protected ChatMessage chatMsg;
        protected LinearLayout chat_image_layout;
        protected ProgressBar download_progress_bar;
        protected TextView download_progress_text;
        protected LinearLayout bt_action;
        protected ImageView img_action;
        protected TextView text_action;
        protected LinearLayout download_progress_layout;
        protected RelativeLayout chat_bubble_layout;
        protected TextView tv_location;
        protected LinearLayout shared_location_layout;


        private ItemRowHolderLeft(View view) {
            super(view);
            txtMessage = (TextView) view.findViewById(R.id.chat_message);
            tvTimestamp = (TextView) view.findViewById(R.id.chat_time);
            chat_image_layout = (LinearLayout) view.findViewById(R.id.chat_image_layout);
            iv_chat_image = (ImageView) view.findViewById(R.id.iv_chat_image);
            download_progress_bar = (ProgressBar) view.findViewById(R.id.progress);
            download_progress_layout = (LinearLayout) view.findViewById(R.id.progress_layout );
            bt_action = (LinearLayout) view.findViewById(R.id.bt_action);
            img_action = (ImageView) view.findViewById(R.id.img_action);
            text_action = (TextView) view.findViewById(R.id.text_action);
            download_progress_text = (TextView) view.findViewById(R.id.progress_text);
            chat_bubble_layout = (RelativeLayout)view.findViewById(R.id.chat_bubble_layout);
            tv_location = (TextView)view.findViewById(R.id.location);
            shared_location_layout = (LinearLayout)view.findViewById(R.id.shared_location_layout);
        }
        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            String sent = "Sent";
            String received = "Received";
            ChatMessage model = values.get(position);
//            if (model.getMapModel() != null){
//                mClickListenerChatFirebase.clickImageMapChat(view,position,model.getMapModel().getLatitude(),model.getMapModel().getLongitude());
//            }
            switch(view.getId()) {
                case R.id.iv_chat_image:
                    if (model.getFileModel().getLocation_image() == null){
                        bt_action.setVisibility(View.GONE);
                        download_progress_layout.setVisibility(View.VISIBLE);
                        if(userid.equals(model.getUserChatModel().getChatownerid())){
                            mClickListenerChatFirebase.clickImageChat(values,sent,model.getChat_database_id(), position, model.getFileModel().getLink_image(), model.getTimeStamp(),request_type,request_id,model.getFileModel().getLink_image_small());
                        }else{
                            mClickListenerChatFirebase.clickImageChat(values,received,model.getChat_database_id(), position, model.getFileModel().getLink_image(), model.getTimeStamp(),request_type,request_id,model.getFileModel().getLink_image_small());
                        }
                    }else{
                        mClickListenerChatFirebase.clickImageChatIntent(view, position, "chat", model.getFileModel().getLocation_image());
                    }
                    break;

            }
        }
        public void setTxtMessage(String message){
            if (txtMessage == null)return;
            if(message!=null){
                txtMessage.setText(message);
                txtMessage.setVisibility(View.VISIBLE);
            }else{
                txtMessage.setVisibility(View.GONE);
            }
        }

        public void setLocation (String latitude,String longitude,String address){
            send_latitude = latitude;
            send_longitude = longitude;
//            String location= address;
            if((latitude==null||longitude==null)||(latitude.equals("null")||(latitude.equals("null")))){
                shared_location_layout.setVisibility(View.GONE);
            }else{
                shared_location_layout.setVisibility(View.VISIBLE);
                tv_location.setText(address);
            }
            shared_location_layout.setOnClickListener(this);

        }

        public void setTvTimestamp(long timestamp){
            if (tvTimestamp == null)return;
            tvTimestamp.setText(Utility.getInstance().formatTimeStamp(timestamp,"hh:mm a"));
        }

        public void setDownload(Download download){
            if(download!=null){
                download_progress_bar.setProgress(download.getProgress());
                if(download.getStatusDownload()!=null && download.getStatusDownload().equals("completed")){
                    download_progress_text.setText("File Download Complete");
                    bt_action.setVisibility(View.GONE);
                    download_progress_layout.setVisibility(View.GONE);
                    chatMsg.setDownloadServiceData(null);
                } else if(download.getStatusDownload()!=null && download.getStatusDownload().equals("downloading")){
                    download_progress_text.setText(String.format("Downloaded (%d/%d) KB",download.getCurrentFileSize(),download.getTotalFileSize()));
                    download_progress_layout.setVisibility(View.VISIBLE);
                    bt_action.setVisibility(View.GONE);
                } else if(download.getStatusDownload()!=null && download.getStatusDownload().equals("failed")){
                    download_progress_layout.setVisibility(View.GONE);
                    download_progress_text.setText("Download failed");
                    bt_action.setVisibility(View.VISIBLE);
                    img_action.setImageResource(R.drawable.xml_bt_refresh);
                    text_action.setText(context.getResources().getString(R.string.retry));
                }
            }else{
                download_progress_layout.setVisibility(View.GONE);

            }
        }
        public void setIvChatPhoto(String link_image_small, String location_image){
            if (iv_chat_image == null)return;
            if(link_image_small!=null || location_image!=null) {
                if(location_image!=null){
                    File myImageFile = new File(location_image);
                    bt_action.setVisibility(View.GONE);
                    Glide.with(iv_chat_image.getContext()).load(myImageFile)
                            .apply(Utility.getInstance().setGlideOptions(300, 300))
                            .into(iv_chat_image);
                }else if(link_image_small!=null){

                    Glide.with(iv_chat_image.getContext()).load(link_image_small)
                            .apply(Utility.getInstance().setGlideOptions(100, 100))
                            .into(iv_chat_image);
                    bt_action.setVisibility(View.VISIBLE);
                }
                chat_image_layout.setVisibility(View.VISIBLE);
            }
            else{
                chat_image_layout.setVisibility(View.GONE);

            }

            iv_chat_image.setOnClickListener(this);
        }

    }

    public class ItemRowHolderRight extends RecyclerView.ViewHolder implements View.OnClickListener{
        protected TextView txtMessage;
        protected TextView tvTimestamp;
        protected ImageView iv_chat_image;
        protected LinearLayout chat_image_layout;
        protected ImageView img_status;
        protected LinearLayout chat_bubble_layout;
        protected TextView tv_location;
        protected LinearLayout shared_location_layout;

        public ItemRowHolderRight(View view) {
            super(view);
//            user = (TextView) view.findViewById(R.id.chat_user);
            txtMessage = (TextView) view.findViewById(R.id.chat_message);
            tvTimestamp = (TextView) view.findViewById(R.id.chat_time);
            chat_image_layout = (LinearLayout) view.findViewById(R.id.chat_image_layout);
            iv_chat_image = (ImageView) view.findViewById(R.id.iv_chat_image);
            img_status = (ImageView)view.findViewById(R.id.status_img);
            chat_bubble_layout = (LinearLayout)view.findViewById(R.id.chat_bubble_layout);
            tv_location = (TextView)view.findViewById(R.id.location);
            shared_location_layout = (LinearLayout)view.findViewById(R.id.shared_location_layout);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            ChatMessage model = values.get(position);
            switch(view.getId()) {
                case R.id.iv_chat_image:
                    mClickListenerChatFirebase.clickImageChatIntent(view, position, "chat", model.getFileModel().getLocation_image());
                    break;
                case R.id.chat_bubble_layout:
                    if(model.getFileModel().getLocation_image()!=null){
                        mClickListenerChatFirebase.re_publish_image(view, model, new File(model.getFileModel().getLocation_image()), model.getFileModel().getLocation_image());
                    }else {
                        mClickListenerChatFirebase.re_publish(view, model);
                    }
                    break;
            }
        }
        public void setTxtMessage(String message){
            if (txtMessage == null)return;
            if(message!=null){
                txtMessage.setText(message);
                txtMessage.setVisibility(View.VISIBLE);
            }else{
                txtMessage.setVisibility(View.GONE);
            }
        }

        public void setLocation (String latitude,String longitude,String address){
            send_latitude = latitude;
            send_longitude = longitude;
//            String location= address;
            if((latitude==null||longitude==null)||(latitude.equals("null")||(latitude.equals("null")))){
                shared_location_layout.setVisibility(View.GONE);
            }else{
                shared_location_layout.setVisibility(View.VISIBLE);
                tv_location.setText(address);
            }
            shared_location_layout.setOnClickListener(this);

        }

//        public void setIvUser(String urlPhotoUser){
//            if (ivUser == null)return;
//            Glide.with(ivUser.getContext()).load(urlPhotoUser).centerCrop().transform(new CircleTransform(ivUser.getContext())).override(40,40).into(ivUser);
//        }

        public void setTvTimestamp(long timestamp){
            if (tvTimestamp == null)return;
            tvTimestamp.setText(Utility.getInstance().formatTimeStamp(timestamp,"hh:mm a"));
        }

        public void setStatus(String status){
            int position = getAdapterPosition();
            ChatMessage model = values.get(position);
            if(status!=null&&status.equals("pending")){
                img_status.setImageResource(R.mipmap.ic_message_pending);
            }else if(status!=null&&status.equals("success")){
                img_status.setImageResource(R.mipmap.ic_message_success);
                iv_chat_image.setClickable(true);
                shared_location_layout.setClickable(true);
            }else if(status!=null&&status.equals("failed")){
                chat_bubble_layout.setOnClickListener(this);
                iv_chat_image.setOnClickListener(null);
                iv_chat_image.setClickable(false);
                shared_location_layout.setOnClickListener(null);
                shared_location_layout.setClickable(false);
                img_status.setImageResource(R.mipmap.ic_message_fail);
            }
            img_status.setVisibility(View.VISIBLE);
        }


        public void setIvChatPhoto(String link_image_small, String location_image){
            if (iv_chat_image == null)return;
            if(link_image_small!=null || location_image!=null) {
                if(location_image!=null){
                    File myImageFile = new File(location_image);

                    Glide.with(iv_chat_image.getContext()).load(myImageFile)
                            .apply(Utility.getInstance().setGlideOptions(300, 300))
                            .into(iv_chat_image);
                }else if(link_image_small!=null){

                    Glide.with(iv_chat_image.getContext()).load(link_image_small)
                            .apply(Utility.getInstance().setGlideOptions(100, 100))
                            .into(iv_chat_image);
                }
                chat_image_layout.setVisibility(View.VISIBLE);
            }
            else{
                chat_image_layout.setVisibility(View.GONE);

            }

            iv_chat_image.setOnClickListener(this);
        }
    }



    public void addMessage(ChatMessage chatMsg){
        this.values.add(chatMsg);
        notifyDataSetChanged();
    }

    //update image chat item, *params chatDatabaseId = chat_item id that need to be changed
    public void updateChatAdapter(String chatDatabaseId, String newStatus, long pubnubTimeServer, FileModel newFileModel){
        int position = 0;
        for(ChatMessage chatMsg : values) {
            if(chatMsg.getChat_database_id().equals(chatDatabaseId)){
                chatMsg.setStatus(newStatus);
                chatMsg.setTimeStamp(pubnubTimeServer);
                chatMsg.setFileModel(newFileModel);
                notifyItemChanged(position);
                break;
            }
            position++;
        }
    }
    public void setProgressDownload(Intent intent, ArrayList<ChatMessage> values){
        this.values = values;
        Download download = intent.getParcelableExtra("download");
        int position= intent.getIntExtra("position",0);
        this.values.get(position).setDownloadServiceData(download);
        if(download.getProgress()==100){
            String filePath = download.getFilePath();
            FileModel fileModel = new FileModel();
            fileModel.setLocation_image(filePath);
            values.get(position).setFileModel(fileModel);
            values.get(position).setDownloadServiceData(null);
        }
        notifyItemChanged(position);
    }
    public void setFailed(Intent intent){
        Download download = intent.getParcelableExtra("download");
        int position= intent.getParcelableExtra("position");
        this.values.get(position).setDownloadServiceData(download);
        notifyItemChanged(position);
    }

    /**
     * Method to add a list of messages and update the listview.
     * @param chatMsgs Messages to be added
     */
    public void setMessages(List<ChatMessage> chatMsgs){
        this.values.clear();
        this.values.addAll(chatMsgs);
//        notifyDataSetChanged();
    }

    /**
     * Handle users. Fill the onlineNow set with current users. Data is used to display a green dot
     *   next to users who are currently online.
     * @param user UUID of the user online.
     * @param action The presence action
     */
    public void userPresence(String user, String action){
        boolean isOnline = action.equals("join") || action.equals("state-change");
        if (!isOnline && this.onlineNow.contains(user))
            this.onlineNow.remove(user);
        else if (isOnline && !this.onlineNow.contains(user))
            this.onlineNow.add(user);
//        notifyDataSetChanged();
    }

    /**
     * Overwrite the onlineNow array with all the values attained from a call to hereNow().
     * @param onlineNow
     */
    public void setOnlineNow(Set<String> onlineNow){
        this.onlineNow = onlineNow;
//        notifyDataSetChanged();
    }
    public Set<String> getOnlineNow(){
        return onlineNow;
    }

    /**
     * Format the long System.currentTimeMillis() to a better looking timestamp. Uses a calendar
     *   object to format with the user's current time zone.
     * @param timeStamp
     * @return
     */


    /**
     * Clear all values from the values array and update the listview. Used when changing rooms.
     */
    public void clearMessages(){
        this.values.clear();
        notifyDataSetChanged();
    }

}