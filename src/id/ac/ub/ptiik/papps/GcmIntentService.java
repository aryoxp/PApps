package id.ac.ub.ptiik.papps;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import id.ac.ub.ptiik.papps.base.AppFragment;
import id.ac.ub.ptiik.papps.base.Message;
import id.ac.ub.ptiik.papps.base.MessageReceived;
import id.ac.ub.ptiik.papps.helpers.MessageDBHelper;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

public class GcmIntentService extends IntentService {

	private static final String TAG = "GcmIntentService";
	private int NOTIFICATION_ID = 1;
	private NotificationManager mNotificationManager;
	private MessageDBHelper dbHandler;
	
	private MessageReceived message;
	
	public GcmIntentService(String name) {
		super(name);
	}
	
	public GcmIntentService() {
		super(null);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		
		Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);
        
        //Log.d("GcmIntentService", messageType);

        if (!extras.isEmpty()) {  // has effect of unparcelling Bundle
            /*
             * Filter messages based on message type. Since it is likely that GCM
             * will be extended in the future with new message types, just ignore
             * any message types you're not interested in, or that you don't
             * recognize.
             */
            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                sendNotification("Send error: " + extras.toString());
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
                sendNotification("Deleted messages on server: " + extras.toString());
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
            	
            	this.dbHandler = new MessageDBHelper(this.getApplicationContext());
            	
            	String receiver = extras.getString("receiver");
            	String sender = extras.getString("sender");
            	String sent = extras.getString("datetime");
            	String message = extras.getString("message");
            	int type = Message.TYPE_RECEIVED;
            	
            	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        		String received = sdf.format(Calendar.getInstance(Locale.US).getTime());
        		MessageReceived newMessage = 
        				new MessageReceived(type, message, sent, received, sender, receiver);
        		
        		Intent localIntent = new Intent("newNotification");
        		localIntent.putExtra("receiver", receiver);
        		localIntent.putExtra("sender", sender);
        		localIntent.putExtra("sent", sent);
        		localIntent.putExtra("received", received);
        		localIntent.putExtra("message", message);
        		localIntent.putExtra("type", type);
        		
        		LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(localIntent);
        		
        		this.message = newMessage;
        		this.dbHandler.add(newMessage);
        		
                // Post notification of received message.
                sendNotification("Received: " + extras.getString("message"));
                Log.i(TAG, "Received: " + extras.toString());
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        C2DMReceiver.completeWakefulIntent(intent);
	}
	
	// Put the message into a notification and post it.
    // This is just one simple example of what you might choose to do with
    // a GCM message.
    private void sendNotification(String msg) {
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);
        
        Intent intent = new Intent(this, MainActivity.class);
        String fragmentToLaunch = AppFragment.FRAGMENT_TAG_MESSAGES.toString();
        intent.putExtra("fragment", fragmentToLaunch);
        int flags = PendingIntent.FLAG_UPDATE_CURRENT;
        
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, flags);
        
        
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
        .setSmallIcon(R.drawable.ic_papps_taskbar)
        .setContentTitle("PTIIK Apps Server")
        .setStyle(new NotificationCompat.BigTextStyle().bigText(this.message.message))
        .setAutoCancel(true)
        .setContentText(this.message.message);
        
        if(this.notificationStatus)
        	mBuilder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
        
        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }
       
    private Boolean notificationStatus = true;
    
    public void turnNotificationOn(Boolean status) {
    	this.notificationStatus = status;
    }
    

}
