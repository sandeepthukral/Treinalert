package com.sandeepthukral.tutorial.util;

import com.sandeepthukral.tutorial.TreinAlert;

import android.content.Context;
import android.widget.Toast;

public class HandleNotifications {
	
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
	
	public void showNotification(TreinAlert reference){
		if (this.cancelledTrain){
			Toast.makeText(reference, "At least one train has been cancelled", Toast.LENGTH_SHORT).show();
		}
		if (this.delayedTrain){
			Toast.makeText(reference, "At least one train is running late", Toast.LENGTH_SHORT).show();
		}
	}

}
