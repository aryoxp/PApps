package id.ac.ub.ptiik.papps.helpers;

import java.util.ArrayList;

import id.ac.ub.ptiik.papps.base.Message;
import id.ac.ub.ptiik.papps.base.MessageIndex;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MessageDBHelper extends SQLiteOpenHelper {

	private String MESSAGES_TABLE = "messages";
	private String KEY_ID = "id";
	private String FIELD_TYPE = "type";
	private String FIELD_MESSAGE = "message";
	private String FIELD_SENT = "sent";
	private String FIELD_RECEIVED = "received";
	private String FIELD_SENDER = "sender";
	private String FIELD_RECEIVER = "receiver";
	private String FIELD_STATUS = "status";

	private static String databaseName = "MessageDB";
	private static int version = 4;
	
	public MessageDBHelper(Context context) {
		super(context, databaseName, null, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_MESSAGES_TABLE = "CREATE TABLE " + MESSAGES_TABLE + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + FIELD_TYPE + " INTEGER,"
                + FIELD_MESSAGE + " TEXT," + FIELD_SENT + " TEXT," 
                + FIELD_RECEIVED + " TEXT," + FIELD_SENDER + " TEXT,"
                + FIELD_RECEIVER + " TEXT," + FIELD_STATUS + " INTEGER)";
                db.execSQL(CREATE_MESSAGES_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + MESSAGES_TABLE);
		this.onCreate(db);
	}
	
	public void add(Message message){
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(FIELD_TYPE, message.type);
		values.put(FIELD_MESSAGE, message.message);
		values.put(FIELD_SENT, message.sent);
		values.put(FIELD_RECEIVED, message.received);
		values.put(FIELD_SENDER, message.sender);
		values.put(FIELD_RECEIVER, message.receiver);
		values.put(FIELD_STATUS, message.readStatus);
		// Inserting Row
		db.insert(MESSAGES_TABLE, null, values);
		db.close(); // Closing database connection
	}

	public Message get(int id){
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(MESSAGES_TABLE, new String[] { KEY_ID,
	            FIELD_TYPE, FIELD_MESSAGE, FIELD_SENT, FIELD_RECEIVED, FIELD_SENDER, 
	            FIELD_RECEIVER, FIELD_STATUS }, 
	            KEY_ID + "=?", // where column
	            new String[] { String.valueOf(id) }, // where value
	            null, // group by 
	            null, // having
	            null, // order by
	            null); // limit

		if (cursor != null) cursor.moveToFirst();
		Message message = new Message(
		    	Integer.parseInt(cursor.getString(0)), // id
		    	Integer.parseInt(cursor.getString(1)), // type
		    	cursor.getString(2), // message
		    	cursor.getString(3), // date sent
		    	cursor.getString(4), // date received
		    	cursor.getString(5), // sender
		    	cursor.getString(6), // receiver
		    	Integer.parseInt(cursor.getString(7)) //status 
		    	);
	    cursor.close();
	    db.close();
	    return message;
	}

	public int update(Message message) {

	    SQLiteDatabase db = this.getWritableDatabase();

	    ContentValues values = new ContentValues();
	    values.put(FIELD_TYPE, message.type);
	    values.put(FIELD_MESSAGE, message.message);
	    values.put(FIELD_SENT, message.sent);
		values.put(FIELD_RECEIVED, message.received);
		values.put(FIELD_SENDER, message.sender);
		values.put(FIELD_RECEIVER, message.receiver);
		values.put(FIELD_STATUS, message.readStatus);
		
	    // updating row
	    return db.update(MESSAGES_TABLE, values, KEY_ID + " = ?",
	            new String[] { String.valueOf(message.id) });
	}

	public int delete(Message selectedMessage) {

	    SQLiteDatabase db = this.getWritableDatabase();
	    // deleting row
	    return db.delete(MESSAGES_TABLE, KEY_ID + " = ?", // where column
	        new String[] { String.valueOf(selectedMessage.id) }); // where values
	}
	
	public ArrayList<MessageIndex> getAll(){

		  ArrayList<MessageIndex> messagesList = new ArrayList<MessageIndex>();

		  SQLiteDatabase db = this.getReadableDatabase();
		  Cursor cursor = db.rawQuery("SELECT DISTINCT " + FIELD_SENDER + ", " + FIELD_RECEIVER + ", "
				  + "(SELECT " + FIELD_MESSAGE 
				  	+ " FROM " + MESSAGES_TABLE + " m " 
				  	+ " WHERE m." + FIELD_SENDER + "=f." + FIELD_SENDER
				  	+ " ORDER BY " + KEY_ID + " DESC LIMIT 1) AS message, "
				  + "(SELECT " + FIELD_SENT
				  	+ " FROM " + MESSAGES_TABLE + " s " 
				  	+ " WHERE s." + FIELD_SENDER + "=f." + FIELD_SENDER
				  	+ " ORDER BY " + KEY_ID + " DESC LIMIT 1) AS sent, "
				  + "(SELECT " + FIELD_RECEIVED
				  	+ " FROM " + MESSAGES_TABLE + " r " 
					+ " WHERE r." + FIELD_SENDER + "=f." + FIELD_SENDER
				  	+ " ORDER BY " + KEY_ID + " DESC LIMIT 1) AS received, "
				  + "(SELECT " + FIELD_STATUS
				  	+ " FROM " + MESSAGES_TABLE + " t " 
					+ " WHERE t." + FIELD_SENDER + "=f." + FIELD_SENDER
				  	+ " ORDER BY " + KEY_ID + " DESC LIMIT 1) AS status "
				  + " FROM " + MESSAGES_TABLE + " f ", null);   
		  try{
		    if (cursor != null){
		    	
		      if(cursor.moveToFirst()){
		        do {
		        	MessageIndex message = new MessageIndex(
		        			cursor.getString(0), // sender
		        			cursor.getString(1), // receiver
		    		    	cursor.getString(2), // message
		    		    	cursor.getString(3), // date sent
		    		    	cursor.getString(4), // date received
		    		    	Integer.parseInt(cursor.getString(5)) // status
		    		    	);
		        	messagesList.add(message);
		        	
		        } while(cursor.moveToNext());
		      }
		    }
		  } finally {
		    cursor.close();
		    db.close();
		  }
		  return messagesList;
	}
	
	public ArrayList<Message> getAll(String sender, String receiver){

		  ArrayList<Message> messagesList = new ArrayList<Message>();

		  SQLiteDatabase db = this.getReadableDatabase();
		  Cursor cursor = db.rawQuery("SELECT * "
				  + " FROM " + MESSAGES_TABLE + " f "
				  + " WHERE " + FIELD_SENDER + " LIKE ? "
				  + " OR " + FIELD_RECEIVER + " LIKE ? ", new String[] { sender, receiver });   
		  try{
		    if (cursor != null){
		      if(cursor.moveToFirst()){
		    	  int count = cursor.getCount();
		    	  int i = 0;
		        do {
		        	if(count-i > 10 && Integer.parseInt(cursor.getString(7)) == Message.STATUS_READ) {
		        		i++;
		        		continue;
		        	}
		        	Message message = new Message(
		    		    	Integer.parseInt(cursor.getString(0)), // id
		    		    	Integer.parseInt(cursor.getString(1)), // type
		    		    	cursor.getString(2), // message
		    		    	cursor.getString(3), // date sent
		    		    	cursor.getString(4), // date received
		    		    	cursor.getString(5), // sender
		    		    	cursor.getString(6), // receiver
		    		    	Integer.parseInt(cursor.getString(7)) //status 
		    		    	);
		        	messagesList.add(message);
		        	i++;
		        } while(cursor.moveToNext());
		      }
		    }
		  } finally {
		    cursor.close();
		    db.close();
		  }
		  return messagesList;
	}
	
	public int getUnreadCount() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT COUNT(*) "
				  + " FROM " + MESSAGES_TABLE + " f "
				  + " WHERE " + FIELD_STATUS + "=" + Message.STATUS_NEW
				  + " AND " + FIELD_TYPE + "=" + Message.TYPE_RECEIVED, null);  
	    if (cursor != null) {
	      cursor.moveToFirst();
	      int count = cursor.getInt(0);
	      return count;
	    }
		return 0;
	}
	
	public int clear(String sender, String receiver) {
		SQLiteDatabase db = this.getWritableDatabase();
	    // deleting row
	    return db.delete(MESSAGES_TABLE, 
	    		FIELD_SENDER + " = ? AND " + FIELD_RECEIVER + " = ?", // where column
	        new String[] { sender, receiver }); // where values
	}

}
