package id.ac.ub.ptiik.papps;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import org.json.JSONObject;

import id.ac.ub.ptiik.papps.base.AppFragment;
import id.ac.ub.ptiik.papps.base.NotificationMessage;
import id.ac.ub.ptiik.papps.helpers.NotificationMessageHandler;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class GcmIntentService extends IntentService {

	private static final String TAG = "GcmIntentService";
	private int NOTIFICATION_ID = 1;
	private NotificationManager mNotificationManager;
	private NotificationMessageHandler dbHandler;
	
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
            	
            	this.dbHandler = new NotificationMessageHandler(this.getApplicationContext());
            	String messageJSONString = extras.getString("message");
            	try {
            		JSONObject messageJSONObject = new JSONObject(messageJSONString);
            		int type = messageJSONObject.getInt("type");
            		
            		String message = messageJSONObject.getString("message");
            		String sent = messageJSONObject.getString("datetime");
            		String from = messageJSONObject.getString("from");
            		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
            		String received = sdf.format(Calendar.getInstance(Locale.US).getTime());
            		NotificationMessage notificationMessage = new NotificationMessage(type, message, sent, received, from);
            		
            		this.dbHandler.add(notificationMessage);
            	} catch(Exception e) {
            		Log.e("MessageJSON Error", e.getMessage());
            	}
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
        intent.putExtra("type", NotificationMessage.MESSAGE);
        intent.putExtra("fragment", AppFragment.FRAGMENT_MESSAGES);
        
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, 0);
        
        
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
        .setSmallIcon(R.drawable.ic_papps_taskbar)
        .setContentTitle("PTIIK Apps Server")
        .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
        .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
        .setAutoCancel(true)
        .setContentText(msg);
        
        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

}
