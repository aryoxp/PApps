package id.ac.ub.ptiik.papps.helpers;

import java.util.ArrayList;

import id.ac.ub.ptiik.papps.base.NotificationMessage;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class NotificationMessageHandler extends SQLiteOpenHelper {

	private String MESSAGES_TABLE = "messages";
	private String KEY_ID = "id";
	private String FIELD_TYPE = "type";
	private String FIELD_MESSAGE = "message";
	private String FIELD_SENT = "sent";
	private String FIELD_RECEIVED = "received";
	private String FIELD_FROM = "sender";
	private String FIELD_STATUS = "status";

	private static String databaseName = "MessageDB";
	private static int version = 3;
	
	public NotificationMessageHandler(Context context) {
		super(context, databaseName, null, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_MESSAGES_TABLE = "CREATE TABLE " + MESSAGES_TABLE + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + FIELD_TYPE + " INTEGER,"
                + FIELD_MESSAGE + " TEXT," + FIELD_SENT + " TEXT," 
                + FIELD_RECEIVED + " TEXT," + FIELD_FROM + " TEXT,"
                + FIELD_STATUS + " INTEGER)";
                db.execSQL(CREATE_MESSAGES_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + MESSAGES_TABLE);
		this.onCreate(db);
	}
	
	public void add(NotificationMessage notificationMessage){
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(FIELD_TYPE, notificationMessage.type);
		values.put(FIELD_MESSAGE, notificationMessage.message);
		values.put(FIELD_SENT, notificationMessage.sent);
		values.put(FIELD_RECEIVED, notificationMessage.received);
		values.put(FIELD_FROM, notificationMessage.from);
		values.put(FIELD_STATUS, notificationMessage.status);
		// Inserting Row
		db.insert(MESSAGES_TABLE, null, values);
		db.close(); // Closing database connection
	}

	public NotificationMessage get(int id){
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(MESSAGES_TABLE, new String[] { KEY_ID,
	            FIELD_TYPE, FIELD_MESSAGE, FIELD_SENT, FIELD_RECEIVED, FIELD_FROM, FIELD_STATUS }, 
	            KEY_ID + "=?", // where column
	            new String[] { String.valueOf(id) }, // where value
	            null, // group by 
	            null, // having
	            null, // order by
	            null); // limit

		if (cursor != null) cursor.moveToFirst();
		    NotificationMessage notificationMessage = new NotificationMessage(
		    	Integer.parseInt(cursor.getString(0)), // id
		    	Integer.parseInt(cursor.getString(1)), // type
		    	cursor.getString(2), // message
		    	cursor.getString(3), // date sent
		    	cursor.getString(4), // date received
		    	cursor.getString(5), // from
		    	Integer.parseInt(cursor.getString(6)) //status 
		    	);
	    cursor.close();
	    db.close();
	    return notificationMessage;
	}

	public int update(NotificationMessage notificationMessage) {

	    SQLiteDatabase db = this.getWritableDatabase();

	    ContentValues values = new ContentValues();
	    values.put(FIELD_TYPE, notificationMessage.type);
	    values.put(FIELD_MESSAGE, notificationMessage.message);
	    values.put(FIELD_SENT, notificationMessage.sent);
		values.put(FIELD_RECEIVED, notificationMessage.received);
		values.put(FIELD_FROM, notificationMessage.from);
		values.put(FIELD_STATUS, notificationMessage.status);
		
	    // updating row
	    return db.update(MESSAGES_TABLE, values, KEY_ID + " = ?",
	            new String[] { String.valueOf(notificationMessage.id) });
	}

	public int delete(NotificationMessage notification) {

	    SQLiteDatabase db = this.getWritableDatabase();
	    // deleting row
	    return db.delete(MESSAGES_TABLE, KEY_ID + " = ?", // where column
	        new String[] { String.valueOf(notification.id) }); // where values
	}
	
	public ArrayList<NotificationMessage> getAll(){

		  ArrayList<NotificationMessage> listMessages = new ArrayList<NotificationMessage>();

		  SQLiteDatabase db = this.getReadableDatabase();
		  Cursor cursor = db.rawQuery("SELECT DISTINCT " + FIELD_FROM + ", "
				  + "(SELECT " + FIELD_MESSAGE 
				  	+ " FROM " + MESSAGES_TABLE + " m " 
				  	+ " WHERE m." + FIELD_FROM + "=f." + FIELD_FROM
				  	+ " ORDER BY " + KEY_ID + " DESC LIMIT 1) AS message, "
				  + "(SELECT " + FIELD_SENT
				  	+ " FROM " + MESSAGES_TABLE + " s " 
				  	+ " WHERE s." + FIELD_FROM + "=f." + FIELD_FROM
				  	+ " ORDER BY " + KEY_ID + " DESC LIMIT 1) AS sent, "
				  + "(SELECT " + FIELD_RECEIVED
				  	+ " FROM " + MESSAGES_TABLE + " r " 
					+ " WHERE r." + FIELD_FROM + "=f." + FIELD_FROM
				  	+ " ORDER BY " + KEY_ID + " DESC LIMIT 1) AS received, "
				  + "(SELECT " + FIELD_STATUS
				  	+ " FROM " + MESSAGES_TABLE + " t " 
					+ " WHERE t." + FIELD_FROM + "=f." + FIELD_FROM
				  	+ " ORDER BY " + KEY_ID + " DESC LIMIT 1) AS status "
				  + " FROM " + MESSAGES_TABLE + " f ", null);   
		  try{
		    if (cursor != null){
		      if(cursor.moveToFirst()){
		        do {
		        	NotificationMessage notificationMessage = new NotificationMessage(
		        			cursor.getString(0), // from
		    		    	cursor.getString(1), // message
		    		    	cursor.getString(2), // date sent
		    		    	cursor.getString(3), // date sent
		    		    	Integer.parseInt(cursor.getString(4)) // status
		    		    	);
		        	listMessages.add(notificationMessage);
		        } while(cursor.moveToNext());
		      }
		    }
		  } finally {
		    cursor.close();
		    db.close();
		  }
		  return listMessages;
	}
	
	public ArrayList<NotificationMessage> getAll(String from){

		  ArrayList<NotificationMessage> listMessages = new ArrayList<NotificationMessage>();

		  SQLiteDatabase db = this.getReadableDatabase();
		  Cursor cursor = db.rawQuery("SELECT * "
				  + " FROM " + MESSAGES_TABLE + " f "
				  + " WHERE " + FIELD_FROM + " LIKE ? ", new String[] { from });   
		  try{
		    if (cursor != null){
		      if(cursor.moveToFirst()){
		        do {
		        	NotificationMessage notificationMessage = new NotificationMessage(
		    		    	Integer.parseInt(cursor.getString(0)), // id
		    		    	Integer.parseInt(cursor.getString(1)), // type
		    		    	cursor.getString(2), // message
		    		    	cursor.getString(3), // date sent
		    		    	cursor.getString(4), // date received
		    		    	cursor.getString(5), // from
		    		    	Integer.parseInt(cursor.getString(6)) //status 
		    		    	);
		        	listMessages.add(notificationMessage);
		        } while(cursor.moveToNext());
		      }
		    }
		  } finally {
		    cursor.close();
		    db.close();
		  }
		  return listMessages;
	}

}
