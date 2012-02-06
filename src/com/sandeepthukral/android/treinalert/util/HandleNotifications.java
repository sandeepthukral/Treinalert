package com.sandeepthukral.android.treinalert.util;

import com.sandeepthukral.android.treinalert.TreinAlert;

import android.R;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

public class HandleNotifications {
	
	public static final int NOTIFICATION_ID_DELAY_CLNCELLATION = 1;
	
	private boolean cancelledTrain = false;
	private boolean delayedTrain = false;
	
	public HandleNotifications(){
		super();
	}

	public boolean isCancelledTrain() {
		return cancelledTrain;
	}

	public void setCancelledTrain(boolean cancelledTrain) {
		this.cancelledTrain = cancelledTrain;
	}

	public boolean isDelayedTrain() {
		return delayedTrain;
	}

	public void setDelayedTrain(boolean delayedTrain) {
		this.delayedTrain = delayedTrain;
	}
	
	public void showNotification(TreinAlert reference, 
			NotificationManager mNotificationManager, int notificationId){
		
		SharedPreferences preferences = 
				PreferenceManager.getDefaultSharedPreferences(reference);
		
		String contentTitle="";
		String contentText="";
		
		if (this.cancelledTrain && this.delayedTrain){
			contentTitle="Trains Cancelled and Daplayed";
			contentText="Train(s) cancelled and delayed";
		} else if (this.cancelledTrain){
			contentTitle="Trains Cancelled";
			contentText="Trains(s) cancelled, no delays";
		} else if (this.delayedTrain){
			contentTitle="Trains Delayed";
			contentText="Trains(s) delayed, no cancellations";
		}
		
		int icon = R.drawable.ic_menu_recent_history;
		long when = System.currentTimeMillis();
		Notification notification = new Notification(icon, contentTitle, when);
		
		Context context = reference;
		Intent notificationIntent = new Intent(reference, TreinAlert.class);
		PendingIntent contentIntent = PendingIntent.getActivity(reference, 0, notificationIntent, 0);

		notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
		
		if (preferences.getBoolean("flash_lights", true)){
			notification.defaults |= Notification.FLAG_SHOW_LIGHTS;
		}
		if (preferences.getBoolean("sound_alarm", true)){
			notification.defaults |= Notification.DEFAULT_SOUND;
		}
		if (preferences.getBoolean("vibrate", false)){
			notification.defaults |= Notification.DEFAULT_VIBRATE;
		}
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		notification.flags |= Notification.FLAG_ONLY_ALERT_ONCE;

		mNotificationManager.notify(notificationId, notification);
	}
	
	public void clearNotification(NotificationManager mNotificationManager,
			int notificationId){
		mNotificationManager.cancel(notificationId);
	}

}
