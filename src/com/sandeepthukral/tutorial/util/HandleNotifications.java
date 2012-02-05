package com.sandeepthukral.tutorial.util;

import com.sandeepthukral.tutorial.TreinAlert;

import android.R;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
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
		/*
		if (this.cancelledTrain){
			Toast.makeText(reference, "At least one train has been cancelled", Toast.LENGTH_SHORT).show();
		}
		if (this.delayedTrain){
			Toast.makeText(reference, "At least one train is running late", Toast.LENGTH_SHORT).show();
		}
		*/
		
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
		
		notification.defaults |= Notification.DEFAULT_SOUND;
		notification.defaults |= Notification.DEFAULT_VIBRATE;
		notification.defaults |= Notification.FLAG_SHOW_LIGHTS;
		notification.flags |= Notification.FLAG_AUTO_CANCEL;

		mNotificationManager.notify(notificationId, notification);
	}
	
	public void clearNotification(NotificationManager mNotificationManager,
			int notificationId){
		mNotificationManager.cancel(notificationId);
	}

}
