package com.pmberjaya.indotiki.dao;

import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.pmberjaya.indotiki.interfaces.misc.OnCreateDatabaseInterface;
import com.pmberjaya.indotiki.models.chat.UserChatModel;
import com.pmberjaya.indotiki.models.bookingData.BookingCompleteMemberData.BookingCompleteData;
import com.pmberjaya.indotiki.models.bookingData.BookingInProgressMemberData.BookingInProgressMemberData;
import com.pmberjaya.indotiki.models.chat.ChatMessage;
import com.pmberjaya.indotiki.models.chat.FileModel;
import com.pmberjaya.indotiki.models.main.MainMenuItemData;
import com.pmberjaya.indotiki.models.chat.MapModel;
import com.pmberjaya.indotiki.models.bookingNew.PlaceSearchData;
import com.pmberjaya.indotiki.utilities.Utility;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DBController extends SQLiteOpenHelper{
	private static final String TAG = "Controller";
	//The Android's default system path of your application database.
	private static String DB_PATH ;

	private static String DB_NAME = "pesanojek.db";

	private SQLiteDatabase myDataBase;

	private final Context myContext;

	/******************** Database Version (Increase one if want to also upgrade your database) ************/
	public static final int DATABASE_VERSION = 20;// started at 9

	/** Create table syntax */
	private static final String FAVORITE_PLACE_CREATE =
			"CREATE TABLE IF NOT EXISTS `po_transport_favorite` (" +
					"`id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"+
					"`place` text ,"+
					"`place_details`	text ,"+
					"`place_id`	varchar(50) ,"+
					"`search_engine`	varchar(20) ,"+
					"`latitude`	TEXT NOT NULL,"+
					"`longitude`	TEXT NOT NULL"+
					");";
	private static final String HISTORY_BOOKING_CREATE =
			"CREATE TABLE IF NOT EXISTS `po_booking_history` (" +
					"`request_id` INTEGER NOT NULL ,"+
					"`from_place` TEXT ,"+
					"`from_place_details` TEXT NOT NULL,"+
					"`to_place`	TEXT ,"+
					"`to_place_details`	TEXT NOT NULL,"+
					"`request_type`	TEXT NOT NULL,"+
					"`transportation` TEXT NOT NULL,"+
					"`latitude_from` ,"+
					"`longitude_from` ,"+
					"`latitude_to`,"+
					"`longitude_to` ,"+
					"`time` TEXT,"+
					"`date` TEXT,"+
					"`time_data` TEXT,"+
					"`point` TEXT,"+
					"`status` TEXT NOT NULL" +
					");";

	private static final String HISTORY_BOOKING_DELETE=
			"DROP TABLE IF EXISTS `po_booking_history`;";

	private static final String PROGRESS_BOOKING_CREATE =
			"CREATE TABLE IF NOT EXISTS `po_booking_progress` (" +
					"`request_id` INTEGER NOT NULL ,"+
					"`from_place` TEXT ,"+
					"`from_place_details` TEXT NOT NULL,"+
					"`to_place`	TEXT ,"+
					"`to_place_details`	TEXT NOT NULL,"+
					"`request_type`	TEXT NOT NULL,"+
					"`transportation` TEXT NOT NULL,"+
					"`latitude_from` ,"+
					"`longitude_from` ,"+
					"`latitude_to`,"+
					"`longitude_to` ,"+
					"`time` TEXT,"+
					"`status` TEXT NOT NULL" +
					");";
	private static final String PROGRESS_BOOKING_DELETE=
			"DROP TABLE IF EXISTS `po_booking_progress`;";
	private static final String HISTORY_CHAT_CREATE =
			"CREATE TABLE IF NOT EXISTS `po_chat_history` (" +
					"`id`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"+
					"`username`	text NOT NULL,"+
					"`chatowner_id`	varchar(20) NOT NULL,"+
					"`userlevel` INTEGER NOT NULL,"+
					"`message`	TEXT,"+
					"`request_id`	text NOT NULL,"+
					"`request_type`	varchar(30) NOT NULL,"+
					"`status`	varchar(10) NOT NULL,"+
					"`link_image`	TEXT,"+
					"`link_image_small`	TEXT,"+
					"`location_image`	TEXT,"+
					"`latitude`	TEXT,"+
					"`longitude` TEXT,"+
					"`address` TEXT,"+
					"`time`	TEXT NOT NULL"+
					");";
	private static final String HISTORY_CHAT_DELETE=
			"DROP TABLE IF EXISTS `po_chat_history`;";
	private static final String HISTORY_CHAT_TIME_CREATE =
			"CREATE TABLE IF NOT EXISTS `po_chat_history_time` (" +
					"`id`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"+
					"`request_id`	text NOT NULL,"+
					"`request_type`	varchar(30) NOT NULL,"+
					"`time`	varchar(20) NOT NULL"+
					");";
	private static final String HISTORY_CHAT_TIME_DELETE=
			"DROP TABLE IF EXISTS `po_chat_history_time`;";

	private static final String MAIN_MENU_CREATE =
			"CREATE TABLE IF NOT EXISTS `inki_menu` (" +
					"`menu`	text,"+
					"`status` text "+
					"`parent` text "+
					");";
	private static final String MAIN_MENU_DELETE=
			"DROP TABLE IF EXISTS `inki_menu`;";

	private static final String FAVORITE_CAR_CREATE =
			"CREATE TABLE IF NOT EXISTS `rentcar_favorite_car` (" +
					"`id`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "+
					"`car_id` text  NOT NULL," +
					"`car_name` varchar(30)  NOT NULL," +
					"`car_image` text NOT NULL," +
					"`car_price` varchar(30)  NOT NULL," +
					"`car_year` varchar(10)  NOT NULL," +
					"`car_color` varchar(20)  NOT NULL," +
					"`car_bbm` varchar(20)  NOT NULL," +
					"`car_passenger` varchar(10)  NOT NULL," +
					"`car_transmission` varchar(20)  NOT NULL" +
					");";

	private static final String FAVORITE_CAR_DELETE=
			"DROP TABLE IF EXISTS `rentcar_favorite_car`;";

	/******************** Used to open database in syncronized way ************/
	/**
	 * Constructor
	 * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
	 * @param context
	 */

	public DBController(Context context) {
		super(context, DB_NAME, null, DATABASE_VERSION);
		PackageManager pM = context.getPackageManager();
		String packageName = context.getPackageName();
		try {
			PackageInfo p = pM.getPackageInfo(packageName, 0);
			packageName = p.applicationInfo.dataDir;
			DB_PATH = packageName +"/databases/";
		} catch (PackageManager.NameNotFoundException e) {
			Log.w("yourtag", "Error Package name not found ", e);
		}
		this.myContext = context;
	}
	private static DBController _instance;
	public static DBController getInstance(Context context) {
		if (_instance == null) {
			_instance = new DBController(context);
		}
		return _instance;
	}
	/**
	 * Creates a empty database on the system and rewrites it with your own database.
	 * */
	public OnCreateDatabaseInterface onCreateDatabaseInterface;
	public void copyDatabase(OnCreateDatabaseInterface onCreateDatabaseInterface) throws IOException{
		this.onCreateDatabaseInterface = onCreateDatabaseInterface;
		boolean dbExist = checkDataBase();

		if(dbExist){
			Log.d("Database", "Database Sudah Ada");
		}else{

			//By calling this method and empty database will be created into the default system path
			//of your application so we are gonna be able to overwrite that database with our database
			this.getReadableDatabase();
			try {

				InputStream myInput = myContext.getAssets().open(DB_NAME);

				// Path to the just created empty db
				String outFileName = DB_PATH + DB_NAME;

				//Open the empty db as the output stream
				OutputStream myOutput = new FileOutputStream(outFileName);

				//transfer bytes from the inputfile to the outputfile
				byte[] buffer = new byte[1024];
				int length;
				while ((length = myInput.read(buffer))>0){
					myOutput.write(buffer, 0, length);
				}
				myOutput.flush();
				myOutput.close();
				myInput.close();


			} catch (IOException e) {
				throw new Error("Error copying database");
			}
		}
		if(onCreateDatabaseInterface!=null) {
			onCreateDatabaseInterface.OnSuccessCreateDatabase();
		}
	}
	/**
	 * Check if the database already exist to avoid re-copying the file each time you open the application.
	 * @return true if it exists, false if it doesn't
	 */
	private boolean checkDataBase(){
		try{
			String myPath = DB_PATH + DB_NAME;
			File file = new File(myPath);
			if (file.exists() && !file.isDirectory()) {
				myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE+ SQLiteDatabase.NO_LOCALIZED_COLLATORS);
			}
			else{
				myDataBase= null;
			}

		}catch(SQLiteException e){
			//database does't exist yet.
		}

		if(myDataBase != null){
			myDataBase.close();
		}

		return myDataBase != null ? true : false;
	}
	/**
	 * Copies your database from your local assets-folder to the just created empty database in the
	 * system folder, from where it can be accessed and handled.
	 * This is done by transfering bytestream.
	 * */
	/********************** Open database for insert,update,delete in syncronized manner ********************/
	public void openDataBase() throws SQLException{
		//Open the database
		String myPath = DB_PATH + DB_NAME;
		File file = new File(myPath);
		if (file.exists() && !file.isDirectory())
			myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE+ SQLiteDatabase.NO_LOCALIZED_COLLATORS);

	}
	public void readDatabase() throws SQLException{
		//Open the database
		myDataBase = this.getReadableDatabase();
	}
	@Override
	public synchronized void close() {
		if(myDataBase != null)
			myDataBase.close();
		super.close();
	}

	public Cursor rawQuery(String sql, String[] args) {
		SQLiteDatabase db = this.getWritableDatabase();
		Log.d(TAG, "Query SQL " + sql + "  being excuted....");
		Cursor cursor = db.rawQuery(sql, args);
		return cursor;
	}

	/*********************** Escape string for single quotes (Insert,Update)************/
	private static String sqlEscapeString(String aString) {
		String aReturn = "";

		if (null != aString) {
			//aReturn = aString.replace("'", "''");
			aReturn = DatabaseUtils.sqlEscapeString(aString);
			// Remove the enclosing single quotes ...
			aReturn = aReturn.substring(1, aReturn.length() - 1);
		}

		return aReturn;
	}
	/*********************** UnEscape string for single quotes (show data)************/
	private static String sqlUnEscapeString(String aString) {
		String aReturn = "";
		if (null != aString) {
			aReturn = aString.replace("''", "'");
		}
		return aReturn;
	}

	/****************************************************************************-----Table Favorite Transport---*******************************************************/
	public ArrayList<HashMap<String,String>> getFavoriteTransportOrder(String strQuery) {
		ArrayList<HashMap<String,String>> favoriteList = new ArrayList<HashMap<String,String>>();
		myDataBase = this.getReadableDatabase();
		String selectQuery="";
		if(Utility.getInstance().checkIfStringIsNotNullOrEmpty(strQuery)) {
			selectQuery = "SELECT `id`,`place`,`place_details`,`place_id`,`search_engine`,`latitude`,`longitude` FROM `po_transport_favorite` WHERE `place` LIKE '% " + strQuery + " %' OR" +
					" `place` LIKE '%" + strQuery + "' OR `place` LIKE '" + strQuery + "%' OR `place` LIKE '" + strQuery + "'";
		}else{
			selectQuery = "SELECT `id`,`place`,`place_details`,`place_id`,`search_engine`,`latitude`,`longitude` FROM `po_transport_favorite`";
		}
		Cursor cursor = myDataBase.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			do {
				HashMap<String,String> map = new HashMap<String,String>();
				map.put("id_favorite", cursor.getString(0));
				map.put("place",  cursor.getString(1));
				map.put("place_details",  cursor.getString(2));
				map.put("place_id",  cursor.getString(3));
				map.put("search_engine",  cursor.getString(4));
				map.put("lat",  cursor.getString(5));
				map.put("lng",  cursor.getString(6));
				map.put("favorite", "true");
				favoriteList.add(map);
			}while (cursor.moveToNext());
		}
		cursor.close();
		myDataBase.close();
		return favoriteList;
	}
	public String getFavoriteTransport(String place_details) {
		myDataBase = this.getReadableDatabase();
		String favorite_id = null;
		String selectQuery = "SELECT `id` FROM `po_transport_favorite` WHERE place_details = '"+place_details+"'";
		Cursor cursor = myDataBase.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			do {
				favorite_id = cursor.getString(0);
			} while (cursor.moveToNext());
		}
		cursor.close();
		myDataBase.close();
		return favorite_id;
	}
	public ArrayList<HashMap<String,String>> getFavoritePlaceId(String search_engine) {
		ArrayList<HashMap<String,String>> favoritePlaceIdList = new ArrayList<HashMap<String,String>>();
		myDataBase = this.getReadableDatabase();
		String selectQuery = "SELECT `id`,`place_id` FROM `po_transport_favorite` WHERE `search_engine`='"+search_engine+"'";
		Cursor cursor = myDataBase.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			do {
				HashMap<String,String> map = new HashMap<String,String>();
				map.put("favorite_id", cursor.getString(0));
				map.put("place_id", cursor.getString(1));
				favoritePlaceIdList.add(map);
			} while (cursor.moveToNext());
		}
		cursor.close();
		myDataBase.close();
		return favoritePlaceIdList;
	}
	public long insertFavoriteTransport(String place, String place_details, String place_id, String search_engine, String latitude, String longitude) {
		myDataBase = this.getReadableDatabase();
		ContentValues values = new ContentValues();
		values.put("place", place);
		values.put("place_details", place_details);
		values.put("place_id", place_id);
		values.put("search_engine", search_engine);
		values.put("latitude", latitude);
		values.put("longitude", longitude);
		long id =  myDataBase.insert("po_transport_favorite", null, values);
		Log.d("INSERT","JALAN");
		myDataBase.close();
		return id;
	}
	public void deleteFavoriteTransport(String place_id) {
		myDataBase = this.getReadableDatabase();
		myDataBase.delete("po_transport_favorite", "`place_id`= '"+ place_id+"'", null);
		Log.d("DELETE", "JALAN");
		myDataBase.close();
	}
	public void deleteFavoriteTransportId(String favorite_id) {
		myDataBase = this.getReadableDatabase();
		myDataBase.delete("po_transport_favorite", "`id`= '"+ favorite_id+"'", null);
		Log.d("DELETE", "JALAN");
		myDataBase.close();
	}


	public HashMap<String,String> getUserLocation(String district_map) {
		if(district_map.contains("Kota")){
			district_map = district_map.replace("Kota ","");
		}else if(district_map.contains("Kabupaten")){
			district_map = district_map.replace("Kabupaten ","");
		}
		HashMap<String, String> map = new HashMap<String, String>();
		myDataBase = this.getReadableDatabase();
		String selectQuery = "SELECT `district_id`,`state_id` from id_district WHERE `district_name`='"+district_map+"'";
		Cursor cursor = myDataBase.rawQuery(selectQuery, null);
		if(cursor.getCount()==1) {
			if (cursor.moveToFirst()) {
				do {
					map.put("district_id", cursor.getString(0));
					map.put("state_id", cursor.getString(1));
				} while (cursor.moveToNext());
			}
		}
		else{
			map = null;
		}
		cursor.close();
		myDataBase.close();
		return map;
	}
/*	public boolean isBookingInTheDB(String request_id){
		myDataBase = this.getWritableDatabase();
		String selectQuery = "SELECT `id` FROM `po_booking_status` WHERE `request_id`='"+request_id+"'";
		Cursor cursor = myDataBase.rawQuery(selectQuery, null);
		if(cursor.getCount()==1) {

			return true;
		}
		else{
			return false;
		}
	}*/


	public ArrayList<ChatMessage> getChatHistory(String request_id, String request_type) {
		ArrayList<ChatMessage> chatMessageArray = new ArrayList<ChatMessage>();
		myDataBase = this.getReadableDatabase();
		String selectQuery = "SELECT `id`,`chatowner_id`,`username`,`userlevel`,`message`,`time`,`status`,`link_image`,`link_image_small`," +
				"`location_image`,`latitude`,`longitude`,`address` from `po_chat_history` WHERE `request_id`='"+request_id+"' AND `request_type`='"+request_type+"'";
		Cursor cursor = myDataBase.rawQuery(selectQuery, null);
		if(cursor.getCount()>0) {
			if (cursor.moveToFirst()) {
				do {
					String id = cursor.getString(0);
					String chatowner_id = cursor.getString(1);
					String username = cursor.getString(2);
					int userlevel = cursor.getInt(3);
					String message = cursor.getString(4);
					String timeStamp = cursor.getString(5);
					Long timeTokenInMilliseconds = Long.parseLong(timeStamp);
					String status = cursor.getString(6);
					String link_image = cursor.getString(7);
					String link_image_small = cursor.getString(8);
					String location_image = cursor.getString(9);
					String latitude = cursor.getString(10);
					String longitude = cursor.getString(11);
					String address = cursor.getString(12);
					UserChatModel userChatModel = new UserChatModel(chatowner_id, username, userlevel);
					FileModel fileModel = new FileModel(link_image, link_image_small, location_image);
					MapModel mapModel = new MapModel(latitude, longitude, address);
					ChatMessage chatMessage = new ChatMessage(id, timeTokenInMilliseconds, status, message, userChatModel, fileModel, null, mapModel);
					chatMessageArray.add(chatMessage);
				} while (cursor.moveToNext());
			}
		}
		cursor.close();
		myDataBase.close();
		return chatMessageArray;
	}


	public String insertChatHistory(ChatMessage chatMessage, String request_id, String request_type) {
		myDataBase = this.getReadableDatabase();
		ContentValues values = new ContentValues();
		values.put("chatowner_id", chatMessage.getUserChatModel().getChatownerid());
		values.put("username", chatMessage.getUserChatModel().getUsername());
		values.put("userlevel", chatMessage.getUserChatModel().getUserlevel());
		values.put("message", chatMessage.getMessage());
		values.put("request_id", request_id);
		values.put("request_type", request_type);
		values.put("time", chatMessage.getTimeStamp());
		values.put("status", chatMessage.getStatus());
		values.put("link_image", chatMessage.getFileModel().getLink_image());
		values.put("link_image_small", chatMessage.getFileModel().getLink_image_small());
		values.put("location_image", chatMessage.getFileModel().getLocation_image());
		values.put("latitude",chatMessage.getMapModel().getLatitude());
		values.put("longitude",chatMessage.getMapModel().getLongitude());
		values.put("address",chatMessage.getMapModel().getAddress());
		String id =  String.valueOf(myDataBase.insert("po_chat_history", null, values));
		Log.d("INSERT CHAT","JALAN");
		myDataBase.close();
		return id;
	}

	public void updateLocationImage(String chat_id, String location_image) {
		myDataBase = this.getReadableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("location_image", location_image); //These Fields should be your String values of actual column names
		myDataBase.update("po_chat_history", cv, "`id`="+chat_id, null);
		myDataBase.close();
	}
	public void updateChatHistoryStatus(String chat_id, String status) {
		myDataBase = this.getReadableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("status", status); //These Fields should be your String values of actual column names
		myDataBase.update("po_chat_history", cv, "`id`="+chat_id, null);
		myDataBase.close();
	}
	public void updateChat(String chat_id, String timeStamp, String status, String link_image, String link_image_small, String location_image) {
		myDataBase = this.getReadableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("time", timeStamp); //These Fields should be your String values of actual column names
		cv.put("status", status); //These Fields should be your String values of actual column names
		cv.put("link_image", link_image); //These Fields should be your String values of actual column names
		cv.put("link_image_small", link_image_small); //These Fields should be your String values of actual column names
		cv.put("location_image", location_image); //These Fields should be your String values of actual column names
		myDataBase.update("po_chat_history", cv, "`id`="+chat_id, null);
		myDataBase.close();
	}
	public void deleteChatHistory(String request_id, String request_type) {
		myDataBase = this.getReadableDatabase();
		myDataBase.delete("po_chat_history", "`request_id`= '"+ request_id+"' AND `request_type`='"+request_type+"'", null);
		Log.d("DELETE", "JALAN");
		myDataBase.close();
	}

	public long getNewestTimeChat(String request_id, String request_type) {
		myDataBase = this.getReadableDatabase();
		String time = null;
		String selectQuery = "SELECT MAX(time) from `po_chat_history_time` WHERE `request_id`='"+request_id+"' AND `request_type`='"+request_type+"'";
		Cursor cursor = myDataBase.rawQuery(selectQuery, null);
		if(cursor.getCount()==1) {
			if (cursor.moveToFirst()) {
				do {
					time= cursor.getString(0);
				} while (cursor.moveToNext());
			}
		}
		cursor.close();
		if(time!=null) {
			return Long.parseLong(time);
		}else{
			return 0;
		}
	}
	public long insertChatTimeHistory(String request_id, String request_type, String time) {
		myDataBase = this.getReadableDatabase();
		ContentValues values = new ContentValues();
		values.put("request_id", request_id);
		values.put("request_type", request_type);
		values.put("time", time);
		long id =  myDataBase.insert("po_chat_history_time", null, values);
		Log.d("INSERT","JALAN");
		myDataBase.close();
		return id;
	}

	public void updateChatTimeHistory(String request_id, String request_type, String time) {
		myDataBase = this.getReadableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("time", time); //These Fields should be your String values of actual column names
		myDataBase.update("po_chat_history_time", cv, "`request_id`='"+request_id+"' AND `request_type`='"+request_type+"'", null);
		myDataBase.close();
	}
	public ArrayList<MainMenuItemData> getMainMenuItemData() {
		ArrayList<MainMenuItemData> mainMenuItemDatas = new ArrayList<>();
		myDataBase = this.getReadableDatabase();
		String selectQuery = "SELECT `menu`,`status` from `inki_menu`";
		Cursor cursor = myDataBase.rawQuery(selectQuery, null);
		if(cursor.getCount()>0) {
			if (cursor.moveToFirst()) {
				do {
					MainMenuItemData mainMenuItemData = new MainMenuItemData();
					mainMenuItemData.setDisplay_menu(cursor.getString(0));
					mainMenuItemData.setStatus(cursor.getString(1));
					mainMenuItemDatas.add(mainMenuItemData);
				} while (cursor.moveToNext());
			}
		}
		cursor.close();

		myDataBase.close();
		return mainMenuItemDatas;
	}
	public void insertMainMenu(List<MainMenuItemData> menu) {
		myDataBase = this.getReadableDatabase();
		for(int i =0;i< menu.size(); i++) {
			ContentValues values = new ContentValues();
			values.put("menu", menu.get(i).getDisplay_menu());
			values.put("status", menu.get(i).getStatus());
			myDataBase.insert("inki_menu", null, values);
		}
		myDataBase.close();
	}
	public void deleteMainMenu() {
		myDataBase = this.getReadableDatabase();
		myDataBase.delete("inki_menu", null , null);
		myDataBase.close();
	}

	public void insertHistoryBooking(BookingCompleteData bookingCompleteMemberData) {
		myDataBase = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("request_id", bookingCompleteMemberData.getId());
		values.put("from_place", bookingCompleteMemberData.getFrom_place());
		values.put("from_place_details", bookingCompleteMemberData.getFrom());
		values.put("to_place", bookingCompleteMemberData.getTo_place());
		values.put("to_place_details", bookingCompleteMemberData.getTo());
		values.put("request_type", bookingCompleteMemberData.getSource_table());
		values.put("transportation", bookingCompleteMemberData.getTransportation());
		values.put("latitude_from", bookingCompleteMemberData.getLat_from());
		values.put("longitude_from", bookingCompleteMemberData.getLng_from());
		values.put("latitude_to", bookingCompleteMemberData.getLat_to());
		values.put("longitude_to", bookingCompleteMemberData.getLng_to());
		values.put("time", bookingCompleteMemberData.getTime());
		values.put("date", bookingCompleteMemberData.getDate());
		values.put("time_data", bookingCompleteMemberData.getTimeData());
		values.put("point", bookingCompleteMemberData.getRate());
		values.put("status", bookingCompleteMemberData.getStatus());

		myDataBase.insert("po_booking_history", null, values);
		Log.d("INSERT HISTORY BOOKING","JALAN");
		myDataBase.close();
	}
	public void insertProgressBooking(BookingInProgressMemberData bookingInProgressMemberData) {

		myDataBase = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("request_id", bookingInProgressMemberData.getId());
		values.put("from_place", bookingInProgressMemberData.getFromPlace());
		values.put("from_place_details", bookingInProgressMemberData.getFrom());
		values.put("to_place", bookingInProgressMemberData.getToPlace());
		values.put("to_place_details", bookingInProgressMemberData.getTo());
		values.put("request_type", bookingInProgressMemberData.getSourceTable());
		values.put("transportation", bookingInProgressMemberData.getTransportation());
		values.put("latitude_from", bookingInProgressMemberData.getLat_from());
		values.put("longitude_from", bookingInProgressMemberData.getLng_from());
		values.put("latitude_to", bookingInProgressMemberData.getLat_to());
		values.put("longitude_to", bookingInProgressMemberData.getLng_to());
		values.put("time", bookingInProgressMemberData.getRequestTime());
		values.put("status", bookingInProgressMemberData.getStatus());

		myDataBase.insert("po_booking_progress", null, values);
		Log.d("INSERT PROGRESS BOOKING","JALAN");
		myDataBase.close();
	}
	public void deleteBookingHistory() {
		myDataBase = this.getReadableDatabase();
		myDataBase.delete("po_booking_history", null, null);
		Log.d("DELETE HISTORY BOOKING", "JALAN");
		myDataBase.close();
	}
	public void deleteBookingProgress(ArrayList<String> idArray) {
		myDataBase = this.getReadableDatabase();
		if(idArray==null) {
			myDataBase.delete("po_booking_progress", null, null);
		}else{
			for(int i =0; i<idArray.size();i++){
				myDataBase.delete("po_booking_progress", "request_id =" + idArray.get(i), null);
			}
		}
		Log.d("DELETE PROGRESS BOOKING", "JALAN");
		myDataBase.close();
	}

	public int getCountBookingInProgress() {
		myDataBase = this.getReadableDatabase();
		String selectQuery = "SELECT * from `po_booking_progress`";
		Cursor cursor = myDataBase.rawQuery(selectQuery, null);
		int banyakData = cursor.getCount();
		cursor.close();
		myDataBase.close();
		return banyakData;
	}
	public ArrayList<BookingCompleteData> getBookingHistory() {
		ArrayList<BookingCompleteData> arrayList = new ArrayList<BookingCompleteData>();
		myDataBase = this.getReadableDatabase();
		String selectQuery = "SELECT * from `po_booking_history`";
		Cursor cursor = myDataBase.rawQuery(selectQuery, null);
		if(cursor.getCount()>0) {
			if (cursor.moveToFirst()) {
				do {
					BookingCompleteData bookingCompleteMemberData = new BookingCompleteData();
					bookingCompleteMemberData.setId(cursor.getString(0));
					bookingCompleteMemberData.setFrom_place(cursor.getString(1));
					bookingCompleteMemberData.setFrom(cursor.getString(2));
					bookingCompleteMemberData.setTo_place(cursor.getString(3));
					bookingCompleteMemberData.setTo(cursor.getString(4));
					bookingCompleteMemberData.setSource_table(cursor.getString(5));
					bookingCompleteMemberData.setTransportation(cursor.getString(6));
					bookingCompleteMemberData.setLat_from(cursor.getString(7));
					bookingCompleteMemberData.setLng_from(cursor.getString(8));
					bookingCompleteMemberData.setLat_to(cursor.getString(9));
					bookingCompleteMemberData.setLng_to(cursor.getString(10));
					bookingCompleteMemberData.setTime(cursor.getString(11));
					bookingCompleteMemberData.setDate(cursor.getString(12));
					bookingCompleteMemberData.setTimeData(cursor.getString(13));
					bookingCompleteMemberData.setRate(cursor.getString(14));
					bookingCompleteMemberData.setStatus(cursor.getString(15));
					arrayList.add(bookingCompleteMemberData);
				} while (cursor.moveToNext());
			}
		}
		cursor.close();
		myDataBase.close();
		return arrayList;
	}
	public ArrayList<BookingInProgressMemberData> getBookingInProgress() {
		ArrayList<BookingInProgressMemberData> arrayList = new ArrayList<BookingInProgressMemberData>();
		myDataBase = this.getReadableDatabase();
		String selectQuery = "SELECT * from `po_booking_progress`";
		Cursor cursor = myDataBase.rawQuery(selectQuery, null);
		if(cursor.getCount()>0) {
			if (cursor.moveToFirst()) {
				do {
					BookingInProgressMemberData bookingInProgressMemberData = new BookingInProgressMemberData();
					bookingInProgressMemberData.setId(cursor.getString(0));
					bookingInProgressMemberData.setFromPlace(cursor.getString(1));
					bookingInProgressMemberData.setFrom(cursor.getString(2));
					bookingInProgressMemberData.setToPlace(cursor.getString(3));
					bookingInProgressMemberData.setTo(cursor.getString(4));
					bookingInProgressMemberData.setSourceTable(cursor.getString(5));
					bookingInProgressMemberData.setTransportation(cursor.getString(6));
					bookingInProgressMemberData.setLat_from(cursor.getString(7));
					bookingInProgressMemberData.setLng_from(cursor.getString(8));
					bookingInProgressMemberData.setLat_to(cursor.getString(9));
					bookingInProgressMemberData.setLng_to(cursor.getString(10));
					bookingInProgressMemberData.setRequestTime(cursor.getString(11));
					bookingInProgressMemberData.setStatus(cursor.getString(12));
					arrayList.add(bookingInProgressMemberData);
				} while (cursor.moveToNext());
			}
		}
		cursor.close();
		myDataBase.close();
		return arrayList;
	}
	public ArrayList<PlaceSearchData> getPlaceHistory(String originOrDestination, String request_type) {
		ArrayList<PlaceSearchData> arrayList = new ArrayList<PlaceSearchData>();
		myDataBase = this.getReadableDatabase();
		String selectQuery="";
		if(originOrDestination!=null&&originOrDestination.equals("from")) {
			selectQuery = "SELECT `from_place`,`from_place_details`,`latitude_from`,`longitude_from` from `po_booking_history` WHERE `request_type`='" +request_type+
					"' GROUP BY `from_place_details` ORDER BY `time_data` LIMIT 3";
		}else if(originOrDestination!=null&&originOrDestination.equals("to")) {
			selectQuery = "SELECT `to_place`, `to_place_details`,`latitude_to`,`longitude_to` from `po_booking_history`  WHERE `request_type`='" +request_type+
					"' GROUP BY `to_place_details` ORDER BY `time_data` LIMIT 3";
		}
		Cursor cursor = myDataBase.rawQuery(selectQuery, null);
		if(cursor.getCount()>0) {
			if (cursor.moveToFirst()) {
				do {
					PlaceSearchData placeSearchData = new PlaceSearchData();
					placeSearchData.setPlace(cursor.getString(0));
					placeSearchData.setPlace_details(cursor.getString(1));
					placeSearchData.setLat(cursor.getString(2));
					placeSearchData.setLng(cursor.getString(3));
					arrayList.add(placeSearchData);
				} while (cursor.moveToNext());
			}
		}
		cursor.close();
		myDataBase.close();
		return arrayList;
	}
	public void insertFavoriteCar(String car_id, String car_name, String car_image, Integer car_price, String car_year, String car_color, String car_bbm, String car_passenger, String car_transmission) {
		myDataBase = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("car_id", car_id);
		values.put("car_name", car_name);
		values.put("car_image", car_image);
		values.put("car_price", car_price);
		values.put("car_year", car_year);
		values.put("car_color", car_color);
		values.put("car_bbm", car_bbm);
		values.put("car_passenger", car_passenger);
		values.put("car_transmission", car_transmission);
		myDataBase.insert("rentcar_favorite_car", null, values);
		Log.d("INSERT","JALAN");
		myDataBase.close();
	}

	public ArrayList<HashMap<String, String>> getFavoriteCar() {
		ArrayList<HashMap<String, String>> listFavoriteCar = new ArrayList<HashMap<String, String>>();
		myDataBase = this.getReadableDatabase();
		String selectQuery = "SELECT `id`, `car_id`, `car_name`, `car_image`, `car_price`, `car_year`, `car_color`, `car_bbm`, `car_passenger`, `car_transmission` FROM `rentcar_favorite_car`";
		Cursor cursor = myDataBase.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			do {
				HashMap<String, String> map = new HashMap<>();
				map.put("id"   , cursor.getString(0));
				map.put("car_id", cursor.getString(1));
				map.put("car_name", cursor.getString(2));
				map.put("car_image", cursor.getString(3));
				map.put("car_price", cursor.getString(4));
				map.put("car_year", cursor.getString(5));
				map.put("car_color", cursor.getString(6));
				map.put("car_bbm", cursor.getString(7));
				map.put("car_passenger", cursor.getString(8));
				map.put("car_transmission", cursor.getString(9));
				listFavoriteCar.add(map);
			} while (cursor.moveToNext());
		}
		cursor.close();
		myDataBase.close();
		return listFavoriteCar;
	}

	public void deleteFavoriteCar(String car_id) {
		myDataBase = this.getReadableDatabase();
		myDataBase.delete("rentcar_favorite_car", "`car_id`= '"+ car_id+"'", null);
		Log.d("DELETE", "JALAN");
		myDataBase.close();
	}
//
//	public boolean CheckFavoriteCar(String car_id) {
//		myDataBase = this.getReadableDatabase();
//		FavoriteCarDatas favoriteCarDatas = new FavoriteCarDatas();
//		ArrayList<HashMap<String, String>> listFavoriteCar = new ArrayList<HashMap<String, String>>();
//		Cursor cursor = null;
//		String checkQuery = "SELECT `id`, `car_id`, `car_name`, `car_image`, `car_price`, `car_year`, `car_color`, `car_bbm`, `car_passenger`, `car_transmission` FROM `rentcar_favorite_car` WHERE `car_id`='"+car_id+"'";
//		cursor= myDataBase.rawQuery(checkQuery,null);
//		if (cursor.moveToFirst()) {
//			do {
//				HashMap<String, String> map = new HashMap<>();
//				map.put("id"   , cursor.getString(0));
//				map.put("car_id", cursor.getString(1));
//				map.put("car_name", cursor.getString(2));
//				map.put("car_image", cursor.getString(3));
//				map.put("car_price", cursor.getString(4));
//				map.put("car_year", cursor.getString(5));
//				map.put("car_color", cursor.getString(6));
//				map.put("car_bbm", cursor.getString(7));
//				map.put("car_passenger", cursor.getString(8));
//				map.put("car_transmission", cursor.getString(9));
//				listFavoriteCar.add(map);
//			} while (cursor.moveToNext());
//		}
//		boolean exists =  (cursor.getCount()>0)?true : false;
//		cursor.close();
//		myDataBase.close();
//		return exists;
//	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(MAIN_MENU_CREATE);
		db.execSQL(HISTORY_CHAT_CREATE);
		db.execSQL(HISTORY_CHAT_TIME_CREATE);
		db.execSQL(HISTORY_BOOKING_CREATE);
		db.execSQL(FAVORITE_PLACE_CREATE);
		db.execSQL(FAVORITE_CAR_CREATE);
		db.execSQL(PROGRESS_BOOKING_CREATE);


	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		Log.d("adsf","sadf");
		if (oldVersion < DATABASE_VERSION) {
			db.execSQL(MAIN_MENU_DELETE);
			db.execSQL(HISTORY_CHAT_DELETE);
			db.execSQL(HISTORY_CHAT_TIME_DELETE);
			db.execSQL(HISTORY_BOOKING_DELETE);
			db.execSQL(FAVORITE_CAR_DELETE);
			db.execSQL(PROGRESS_BOOKING_DELETE);

			db.execSQL(MAIN_MENU_CREATE);
			db.execSQL(HISTORY_CHAT_CREATE);
			db.execSQL(HISTORY_CHAT_TIME_CREATE);
			db.execSQL(HISTORY_BOOKING_CREATE);
			db.execSQL(FAVORITE_PLACE_CREATE);
			db.execSQL(FAVORITE_CAR_CREATE);
			db.execSQL(PROGRESS_BOOKING_CREATE);
		}
	}
}


 
