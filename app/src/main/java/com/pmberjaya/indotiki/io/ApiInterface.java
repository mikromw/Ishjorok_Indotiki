package com.pmberjaya.indotiki.io;

import com.pmberjaya.indotiki.base.BaseCallback;
import com.pmberjaya.indotiki.base.BaseGenericCallback;
import com.pmberjaya.indotiki.callbacks.account.CompletingDataCallback;
import com.pmberjaya.indotiki.callbacks.account.EditProfilCallback;
import com.pmberjaya.indotiki.callbacks.account.LoginCallback;
import com.pmberjaya.indotiki.callbacks.account.RegisterFirebaseCallback;
import com.pmberjaya.indotiki.callbacks.account.UploadPhotoTempCallback;
import com.pmberjaya.indotiki.callbacks.account.UploadProfilePhotoCallback;
import com.pmberjaya.indotiki.callbacks.account.VerificationCallback;
import com.pmberjaya.indotiki.callbacks.account.YahooProfileCallback;
import com.pmberjaya.indotiki.callbacks.bookingData.BookingCancelCallback;
import com.pmberjaya.indotiki.callbacks.bookingNew.TimeFeeCallback;
import com.pmberjaya.indotiki.callbacks.bookingNew.UploadPhotoCallback;
import com.pmberjaya.indotiki.callbacks.chat.UploadChatImageCallback;
import com.pmberjaya.indotiki.callbacks.deposit.DepositCallback;
import com.pmberjaya.indotiki.callbacks.deposit.DepositNewConfirmationCallback;
import com.pmberjaya.indotiki.callbacks.gmaps.DirectionRouteGMapsCallback;
import com.pmberjaya.indotiki.callbacks.gmaps.DistanceTimeGMapsCallback;
import com.pmberjaya.indotiki.callbacks.gmaps.GeocoderLocationGMapsCallback;
import com.pmberjaya.indotiki.callbacks.gmaps.PlaceDetailGmapsCallback;
import com.pmberjaya.indotiki.callbacks.gmaps.PlaceNearbyGmapsCallback;
import com.pmberjaya.indotiki.callbacks.main.KeyCallback;
import com.pmberjaya.indotiki.callbacks.main.VersionAndMaintenanceCallback;
import com.pmberjaya.indotiki.models.account.UserModel;
import com.pmberjaya.indotiki.models.bookingData.BookingCompleteMemberData.BookingCompleteModel;
import com.pmberjaya.indotiki.models.bookingData.BookingCompleteMemberDetailData;
import com.pmberjaya.indotiki.models.bookingData.BookingInProgressMemberData.BookingInProgressModel;
import com.pmberjaya.indotiki.models.bookingData.BookingInProgressMemberDetailData;
import com.pmberjaya.indotiki.models.bookingData.BookingReasonCancelData;
import com.pmberjaya.indotiki.models.bookingData.BookingThisTripAgainData;
import com.pmberjaya.indotiki.models.bookingData.DriverPositionMapData;
import com.pmberjaya.indotiki.models.bookingNew.DriverPositionListData.DriverPositionListModel;
import com.pmberjaya.indotiki.models.deposit.DepositConfirmationData.DepositConfirmationData;
import com.pmberjaya.indotiki.models.deposit.DepositConfirmationData.DepositConfirmationModel;
import com.pmberjaya.indotiki.models.deposit.DepositData.CheckDepositData;
import com.pmberjaya.indotiki.models.deposit.DepositData.DepositTopUpListData;
import com.pmberjaya.indotiki.models.event.EventNewModel;
import com.pmberjaya.indotiki.models.event.EventPromoModel;
import com.pmberjaya.indotiki.models.help.HelpServiceData;
import com.pmberjaya.indotiki.models.help.HelpSubCategoryData;
import com.pmberjaya.indotiki.models.main.BannerData;
import com.pmberjaya.indotiki.models.main.DistrictCentralData;
import com.pmberjaya.indotiki.models.main.MainMenuItemData;
import com.pmberjaya.indotiki.models.mart.Banner.MartBannerData;
import com.pmberjaya.indotiki.models.mart.Category.MartCategoryItemModel;
import com.pmberjaya.indotiki.models.mart.Category.MartCategoryModel;
import com.pmberjaya.indotiki.models.mart.Item.MartItemModel;
import com.pmberjaya.indotiki.models.mart.SearchMartData.MartSearchStoreData;
import com.pmberjaya.indotiki.models.mart.SearchMartData.SearchMartModel;
import com.pmberjaya.indotiki.models.others.CheckStatusBookingData;
import com.pmberjaya.indotiki.models.pln.TokenListrik.History.HistoryPlnModel;
import com.pmberjaya.indotiki.models.pln.TokenListrik.PurchasePln.PurchasePlnModel;
import com.pmberjaya.indotiki.models.pln.TokenListrik.TokenListrikModel;
import com.pmberjaya.indotiki.models.promo.PromoListModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;

public interface ApiInterface {
    //------------------------------------------------------ACCOUNT-----------------------------------------------------------------
    //#Verificate at Completing/LoginActivity
    @FormUrlEncoded
    @PUT("API/main/verification_sms/token/{token}")
    Call<VerificationCallback> postVerificationLoginRegister(@Path("token") String token, @Field("verification_code") String code);

    //#Verificate at Edit Profile
    @FormUrlEncoded
    @PUT("API/main/verification_sms/edit_profile/{token}")
    Call<VerificationCallback> postVerificationProfile(@Path("token") String token, @FieldMap Map<String, String> params);

    //#Completing Profile
    @FormUrlEncoded
    @PUT("API/member/member/update/profile_tmp")
    Call<CompletingDataCallback> postCompletingData(@FieldMap Map<String,String> map);

    //#Upload Completing Profile Photo
    @Multipart
    @POST("API/main/upload_file/action/profile_tmp")
    Call<UploadPhotoTempCallback> doUploadPhotoTemp(@PartMap Map<String, ProgressRequestBody> file);

    //#Resend verification code login/edit
    @PUT("API/main/verification_sms/resend/{token}")
    Call<BaseCallback> postSendBackVerification(@Path("token") String token);

    //#LoginActivity by Phone Number
    @FormUrlEncoded
    @PUT("API/main/login_phone/action/member")
    Call<LoginCallback> doLoginPhone(@Field("phone") String phone);

    //#LoginActivity by Google
    @FormUrlEncoded
    @POST("API/member/gplus_member")
    Call<LoginCallback> postLoginGoogle(
            @Field("cover") String cover,
            @Field("email") String email,
            @Field("fullname") String fullname,
            @Field("gplus_id") String gplus_id
    );

    //#LoginActivity by Facebook
    @FormUrlEncoded
    @POST("API/member/facebook_member")
    Call<LoginCallback> postLoginFacebook(
            @Field("cover") String cover,
            @Field("email") String email,
            @Field("fullname") String fullname,
            @Field("facebook_id") String facebook_id
    );

    //#LoginActivity by Yahoo
    @FormUrlEncoded
    @POST("API/member/ymail_member")
    Call<LoginCallback> postLoginYahoo(
            @Field("cover") String cover,
            @Field("email") String email,
            @Field("fullname") String fullname,
            @Field("ymail_id") String ymail_id,
            @Field("phone") String phone
    );

    //#Get Yahoo acoount data
    @GET("https://social.yahooapis.com/v1/user/me/profile?format=json")
    Call<YahooProfileCallback> getYahooProfile();

    //#Resend verification code new account/completing profile
    @FormUrlEncoded
    @PUT("API/main/verification_sms/resend_new_account/{token}")
    Call<BaseCallback> postSendBackVerificationEditProfil(@Path("token") String token, @FieldMap Map<String, String> params);

    //#Get user account data
    @GET("API/main/account")
    Call<BaseGenericCallback<UserModel>> getUserData();

    //#Edit user profile
    @FormUrlEncoded
    @PUT("API/member/account")
    Call<EditProfilCallback> postEditProfil(@FieldMap Map<String, String> params);

    //#Upload user profile photo
    @Multipart
    @POST("API/main/upload_file/action/profile")
    Call<UploadProfilePhotoCallback> doUploadProfilePhoto(@PartMap Map<String, ProgressRequestBody> file);

    //#Delete user profile photo
    @PUT("API/member/member/update/delete_avatar")
    Call<BaseCallback>doDeleteProfilePhoto();

    //#Create new firebase id
    @FormUrlEncoded
    @POST("API/main/fcm")
    Call<RegisterFirebaseCallback> postRegisterFCM(@FieldMap Map<String, String> params);

    //#Update new firebase id
    @FormUrlEncoded
    @PUT("API/main/fcm/action/update")
    Call<BaseCallback> deniedAccess(@FieldMap Map<String, String> params);

    //-------------------------------------------------------------------------------------------------------------------------------
    //-------------------------------------------------NEW BOOKING ORDER-------------------------------------------------------------
    //#Order Transport
    @FormUrlEncoded
    @PUT("API/member/order/action/transport")
    Call<BaseGenericCallback<String>> postRequestTransport(@FieldMap Map<String, String> params);

    //#Order Courier
    @FormUrlEncoded
    @PUT("API/member/order/action/courier")
    Call<BaseGenericCallback<String>> postRequestCourier(@FieldMap Map<String, String> params);

    //#Order Food
    @FormUrlEncoded
    @PUT("API/member/order/action/food/id/{id}")
    Call<BaseGenericCallback<String>> postRequestFood(@Path("id") String restaurant_id, @FieldMap Map<String, String> params);

    //#Upload Courier Photo
    @Multipart
    @POST("API/main/upload_file/action/order_courier")
    Call<UploadPhotoCallback> doUploadPhotoCourier(@PartMap Map<String, ProgressRequestBody> file);

    //#Cancel Booking
    @FormUrlEncoded
    @PUT("API/member/member_action/action/cancel")
    Call<BookingCancelCallback> postRequestResponseCancel(@FieldMap Map<String, String> params);

    //#Get booking price/fee
    @GET("API/main/fee/action/calculate")
    Call<TimeFeeCallback> getTimeFee(@QueryMap Map<String, String> params);

    //#Repeat booking notification 1 time
    @FormUrlEncoded
    @PUT("API/main/order/action/repeat")
    Call<BaseCallback> postRequestResponseRepeat(@FieldMap Map<String, String> params);
    //-----------------------------------------------------------------------------------------------------------------------------
    //-------------------------------------------------BOOKING DATA----------------------------------------------------------------
    //#Get booking In-Progress list
    @GET("API/member/member_list/request/waiting")
    Call<BaseGenericCallback<BookingInProgressModel>> getBookingInProgressMember(@Query("filter[]") String[] filterArray);

    //#Get booking Complete list
    @GET("API/member/member_list/request/complete")
    Call<BaseGenericCallback<BookingCompleteModel>> getBookingCompleteMember(@Query("filter[]") String[] filterArray);

    //#Send e-Receipt via email

    @GET("API/main/receipt/action/order_receipt/id/{request_id}")
    Call<BaseCallback> postEmailReceipt(@Path("request_id") String request_id, @Query("request_type") String request_type);

    //#Book this trip again
    @GET("API/member/order/action/book_again")
    Call<BaseGenericCallback<BookingThisTripAgainData>> postBookingAgainMember(@Query("request_id") String requestid, @Query("type") String request_type);

    //#Get driver position (Multiple)
    @GET("API/rider/account/get/all_location")
    Call<BaseGenericCallback<DriverPositionListModel>> getPositionDriverList(@QueryMap Map<String, String> params);
    //-------------------------------------------------------------------------------------------------------------------------------
    //-------------------------------------------------BOOKING IN-PROGRESS DETAIL----------------------------------------------------
    //#Get booking In-Progress Detail
    @GET("API/member/order_view/action/process")
    Call<BaseGenericCallback<BookingInProgressMemberDetailData>> getBookingInProgressDetail(@Query("request_id") String request_id, @Query("type") String request_type);

    //#Send Chat Notification
    @FormUrlEncoded
    @POST("API/main/chat/action/notification")
    Call<BaseCallback> sendChatNotification(@FieldMap Map<String, String> params);

    //#Upload Chat Image
    @Multipart
    @POST("API/main/upload_file/action/chat_image/channel/{channel}")
    Call<UploadChatImageCallback> doUploadChatImage(@PartMap Map<String, ProgressRequestBody> file, @Path("channel") String channel);

    //#Download Chat Image
    @GET("{imagePath}")
    @Streaming
    Call<ResponseBody> downloadFile(@Path("imagePath") String imagePath);

    //#Check booking status
    @GET("API/member/order/action/status")
    Call<BaseGenericCallback<CheckStatusBookingData>> getCheckStatusBooking(@Query("request_id") String request_id, @Query("request_type") String request_type);

    //#Get driver position (Single)
    @GET("API/member/member/get/driver_location")
    Call<BaseGenericCallback<DriverPositionMapData>> getDriverPositionMap(@Query("user_id") String driver_id);

    @GET("API/member/member/get/reason_cancel")
    Call<BaseGenericCallback<List<BookingReasonCancelData>>> getBookingReasonCancel();

    //-------------------------------------------------------------------------------------------------------------------------------
    //-------------------------------------------------BOOKING COMPLETE DETAIL-------------------------------------------------------
    //#Get booking Complete Detail
    @GET("API/member/order_view/action/complete")
    Call<BaseGenericCallback<BookingCompleteMemberDetailData>> getBookingCompleteMemberDetail(@Query("request_id") String request_id, @Query("type") String request_type);

    //#Post star rating and comment driver
    @FormUrlEncoded
    @PUT("API/member/member_action/action/rate")
    Call<BaseCallback> postUpdateBookingRatingDriver(@FieldMap Map<String, String> params);
    //-------------------------------------------------------------------------------------------------------------------------------
    //-------------------------------------------------SPLASH SCREEN-----------------------------------------------------------------
    //#Get Update and Maintenance info
    @GET("API/main/common/action/version_maintenance")
    Call<VersionAndMaintenanceCallback> getVersionAndMaintenance(@QueryMap Map<String, String> params);

    //#Generate API key
    @PUT("API/auth")
    Call<KeyCallback> put();

    //#Get Pop up event/promo info
    @GET("API/main/banner")
    Call<BaseGenericCallback<List<BannerData>>> getBanner(@QueryMap Map<String, String> params);

    //#Set up Location Indotiki Service
    @GET("API/member/Check_in/service/all")
    Call<BaseGenericCallback<DistrictCentralData>> getDistrictCentral(@QueryMap Map<String, String> params);
    //-------------------------------------------------------------------------------------------------------------------------------
    //-------------------------------------------------MAINMENU ---------------------------------------------------------------------
    //#Get Indotiki Menu Item
    @GET("API/main/menu_app/district/{district_id}")
    Call<BaseGenericCallback<ArrayList<MainMenuItemData>>> getMainMenuItem(@Path("district_id") String district_id);

    //#Update user current city
    @FormUrlEncoded
    @PUT("API/member/member/update/current_city")
    Call<BaseCallback> postCurrentCity(@Field("district_id") String district_id);
    //-------------------------------------------------------------------------------------------------------------------------------
    //------------------------------------------------DEPOSIT-------------------------------------------------------------------------
    //#Post a Inki-Pay confirmation
    @FormUrlEncoded
    @POST("API/main/deposit/action/confirmation")
    Call<DepositNewConfirmationCallback> postDepositConfirmationData(@FieldMap Map<String, String> params);

    //#Get Inki-Pay log
    @GET("API/main/deposit")
    Call<DepositCallback> getDepositLog(@QueryMap Map<String, String> params);

    //#Get Inki-Pay confirmation list
    @GET("API/main/deposit/action/confirmation")
    Call<BaseGenericCallback<DepositConfirmationModel>> getDepositDriverConfirmation(@QueryMap Map<String, String> params);

    //#Upload Inki-Pay evidence image
    @Multipart
    @POST("API/main/upload_file/action/transfer_receipt/id/{id}")
    Call<UploadPhotoCallback> doUploadPaymentEvidencePhoto(@Path("id") String id, @PartMap Map<String, ProgressRequestBody> file);

    //#Get DepositLogFragment Top-up/Package List
    @GET("API/main/deposit")
    Call<BaseGenericCallback<List<DepositTopUpListData>>> getDepositTopUpList(@Query("action") String action);

    //#Update Inki-Pay confirmation status
    @POST("API/main/deposit/action/paid/id/{id}")
    Call<BaseCallback>sendStatusConfirmDeposit(@Path("id")String id);

    //#Get Inki-Pay confirmation detail
    @GET("API/main/deposit/action/detail_confirmation")
    Call<BaseGenericCallback<DepositConfirmationData>> getDepositConfirmationDetail(@Query("id") String transaction_id);

    //#Get user Inki-Pay balance
    @GET("API/main/payment/action/check_deposit")
    Call<BaseGenericCallback<CheckDepositData>> getCheckDeposit();
    //-------------------------------------------------------------------------------------------------------------------------------

    //------------------------------------------------PROMO--------------------------------------------------------------------------
    //#Get "general promo and my promo list"
    @GET("API/main/account/action/my_promo")
    Call<BaseGenericCallback<PromoListModel>> getPromoCodeList(@QueryMap Map<String, String> params);

    //#Get main banner and event list
    @GET("API/main/event/action/home")
    Call<BaseGenericCallback<EventPromoModel>> getEventPromo(@QueryMap Map<String, String> params);

    //#Get list event new
    @GET("API/main/news/action/terbaru")
    Call<BaseGenericCallback<EventNewModel>> getEventNew(@QueryMap Map<String, String> params);

    //#Get list event other
    @GET("API/main/news/action/lainnya")
    Call<BaseGenericCallback<EventNewModel>> getEventOther(@QueryMap Map<String, String> params);

    //#post voucher code
    @FormUrlEncoded
    @PUT("API/main/voucher/action/use")
    Call<BaseGenericCallback>postVoucherCode(@Field("imei")String imei, @Field("code_promo")String code_promo);

    //#post rider coupon code
    @FormUrlEncoded
    @POST("API/main/referral/action/rider_referal")
    Call<BaseCallback> postBonusReferal(@Field("imei")String imei, @Field("code") String code);
    //-------------------------------------------------------------------------------------------------------------------------------
    //------------------------------------------------MISC---------------------------------------------------------------------------
    //#Send feedback
    @FormUrlEncoded
    @POST("API/main/feedback")
    Call<BaseCallback> postFeedback(@FieldMap Map<String, String> params);

    //#Get link invite friends
    @GET("API/account/action/link_invitation")
    Call<BaseGenericCallback<String>> sendReferral();

    //#Get help category and FAQ data
    @GET("API/help/request/help_service")
    Call<BaseGenericCallback<HelpServiceData>>getHelpService();

    //#Search help data
    @GET("API/help/request/help_search")
    Call<BaseGenericCallback<List<HelpSubCategoryData>>>getSearchHelpData(@Query("subject")String subject);

    //#Get help subcategory data
    @GET("API/help/request/help_list")
    Call<BaseGenericCallback<List<HelpSubCategoryData>>>getHelpServiceSelectedData(@Query("with_service")String id);

    //-------------------------------------------------------------------------------------------------------------------------------
    //------------------------------------------------MART--------------------------------------------------------------------------

    //#Get mart category
    @GET("API/mart/category/action/list")
    Call<BaseGenericCallback<MartCategoryModel>>getMartCategoryList(@QueryMap Map<String, String> params);

    //#Get mart category item
    @GET("API/mart/item/action/category/mart_id")
    Call<BaseGenericCallback<MartCategoryItemModel>>getMartCategoryItem(@Query("mart_id")String martid);

    //#Get mart item list
    @GET("API/mart/item/action/list")
    Call<BaseGenericCallback<List<MartItemModel>>>getMartItemList(@QueryMap Map<String, String> params);

    //#Get Search Mart
    @GET("API/mart/search/action/mart_n_item")
    Call<BaseGenericCallback<SearchMartModel>> getSearchMart(@QueryMap Map<String, String> params);

    //#Get mart banner
    @GET("API/main/banner")
    Call<BaseGenericCallback<List<MartBannerData>>> getMartBanner(@QueryMap Map<String,String>params);

    //#Get mart all list
    @GET("API/mart/category/action/store_list/mart_category_id/from_lat/from_lng/district")
    Call<BaseGenericCallback<List<MartSearchStoreData>>>getMartSeeAllList(@QueryMap Map<String, String> params);

    //#Post Request Mart
    @FormUrlEncoded
    @PUT("API/member/order/action/mart")
    Call<BaseGenericCallback<String>> postRequestMart(@FieldMap Map<String, String> params);

    //-------------------------------------------------------------------------------------------------------------------------------
    //-------------------------------------------------GOOGLE MAPS API---------------------------------------------------------------
    @GET("https://maps.googleapis.com/maps/api/distancematrix/json?")
    Call<DistanceTimeGMapsCallback> getDistanceTimePlace(@QueryMap Map<String, String> params);

    @GET("https://maps.googleapis.com/maps/api/directions/json")
    Call<DirectionRouteGMapsCallback> getDirectionRoute(@QueryMap Map<String, String> params);

    @GET("https://maps.googleapis.com/maps/api/geocode/json")
    Call<GeocoderLocationGMapsCallback> getGeocodeLocation(@QueryMap Map<String, String> params);

    @GET("https://maps.googleapis.com/maps/api/place/nearbysearch/json")
    Call<PlaceNearbyGmapsCallback> getPlaceNearby(@QueryMap Map<String, String> params);

    @GET("https://maps.googleapis.com/maps/api/place/textsearch/json")
    Call<PlaceDetailGmapsCallback> getPlaceDetail(@QueryMap Map<String, String> params);

    //-------------------------------------------------------------------------------------------------------------------------------
    //------------------------------------------------PLN----------------------------------------------------------------------------

    // #Post List Token Pln
    @FormUrlEncoded
    @POST("API/main/pln/action/prepaid")
    Call<BaseGenericCallback<TokenListrikModel>> postPlnTokenList(@FieldMap Map<String, String> params);

    // #Post Purchase Token Pln
    @FormUrlEncoded
    @POST("API/main/payment/action/pln_prepaid")
    Call<BaseGenericCallback<PurchasePlnModel>> postPurchaseTokenPln(@FieldMap Map<String, String> params);

    // #Get History List Pln
    @GET("API/main/pln/action/history")
    Call<BaseGenericCallback<HistoryPlnModel>> getHistoryPln();

    //-------------------------------------------------------------------------------------------------------------------------------
    //------------------------------------------------NOT USED-----------------------------------------------------------------------
//    @FormUrlEncoded
//    @PUT("API/member/update/location")
//    Call<BaseCallback> postLocationMember(@FieldMap Map<String, String> params);

//    @GET("API/main/banner")
//    Call<BaseGenericCallback<HotBannerModel>> getHotImageEvent();

//    @FormUrlEncoded
//    @POST("API/main/promo/reduction")
//    Call<CheckPromoCallback> checkPromoCode(@FieldMap Map<String, String> params);
//
//    @GET("API/main/banner")
//    Call<BannerCallback> getBanner(@QueryMap Map<String, String> params);
}