package com.pmberjaya.indotiki.interfaces.chat;

import android.view.View;

import com.pmberjaya.indotiki.models.chat.ChatMessage;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by edwin on 22/10/2016.
 */

public interface ClickListenerChat {

//    /**
//     * Quando houver click na imagem do chat
//     * @param view
//     * @param position
//     */
    void clickImageChat(ArrayList<ChatMessage> chat,String type , String chat_database_id, int position, String image_path, long timestamp,String request_type, String request_id, String link_image_small);

    /**
     * Quando houver click na imagem de mapa
     * @param view
     * @param position
     */
    void clickImageMapChat(View view, int position,String latitude,String longitude);

    void clickImageChatIntent(View view, int position, String activity, String location_image);

    void re_publish(View view, ChatMessage chatMessage);
    void re_publish_image(View view, ChatMessage chatMessage, File fileUpload, String imageName);
}
