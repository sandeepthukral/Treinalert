package com.sandeepthukral.tutorial;


import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sandeepthukral.tutorial.util.HandleNotifications;
import com.sandeepthukral.tutorial.util.Utilities;

public class TreinAlert extends Activity {
	private TextView textView;
	List<VertrekkendeTrein> listOfDepartures=new ArrayList<VertrekkendeTrein>();
	TrainAdapter adapter=null;
	
	private static final int SHOW_ABOUT_DIALOG = 101;
	private static final int SHOW_SETTINGS_COMING_DIALOG = 102;
	
/** Called when the activity is first created. */

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		textView = (TextView) findViewById(R.id.TextView01);
		ListView list = (ListView) findViewById(R.id.trains);
		
		adapter=new TrainAdapter();
		list.setAdapter(adapter);
	}
	
	protected Dialog onCreateDialog(int id) {
        
		Dialog dialog;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		String message="";
		
		switch(id) {
        case SHOW_ABOUT_DIALOG:
        	
        	message="(C) 2012 Sandeep Thukral\nhttp://www.thukral.nl";
    		
    		builder.setMessage(message);
    		builder.setNegativeButton(R.string.STRING_OK, new DialogInterface.OnClickListener() {
					
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.dismiss();	
				}
			});
    		
    		dialog = builder.create();
            break;
        case SHOW_SETTINGS_COMING_DIALOG:
        	
        	message="Settings dialog will be coming soon";
    		
    		builder.setMessage(message);
    		builder.setNegativeButton(R.string.STRING_OK, new DialogInterface.OnClickListener() {
					
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.dismiss();	
				}
			});
    		
    		dialog = builder.create();
            break;
        default:
            dialog = null;
        }
        return dialog;
    }

	/**
	 * 
	 * @author sandeep
	 * This class is an implementation of AsyncTask. This will fetch the response
	 * from the API and help parse the same.
	 */
	private class DownloadWebPageTask extends AsyncTask<String, Void, String> {
		private static final String TAG = "ProgressTest";

		@Override
		protected String doInBackground(String... urls) {
			Log.d("performance","doInBackground started");
			String response = "";
			
			for (String url : urls) {
				Log.d("performance","getting clicent instance");
				DefaultHttpClient client = new DefaultHttpClient();
				try {
					Log.d("performance","updating client");
					client.getCredentialsProvider().setCredentials(
		                    new AuthScope("webservices.ns.nl", 80),
		                    new UsernamePasswordCredentials("sandeepthukral@gmail.com", 
		                    		"sVwzxU7HmC9oAXDlIHyuIl_M82Xi48ABxFGZq6dLcawTsBIT1s8G3w"));
					Log.d("performance","getting httpGet");
					HttpGet httpGet = new HttpGet(url);
				
					try {
						Log.d("performance","client.execute");
						HttpResponse execute = client.execute(httpGet);
						Log.d("performance","getting inputstream");
						InputStream content = execute.getEntity().getContent();
						
						Log.d("performance","making string out of inputstream");
						BufferedReader buffer = new BufferedReader(
							new InputStreamReader(content));
						String s = "";
						while ((s = buffer.readLine()) != null) {
							response += s;
						}
						Log.d("performance","string created");
						
					} catch (Exception e) {
						e.printStackTrace();
					}
				} finally{}
			}
			Log.d("performance","doInBackground stopped");
			return response;
		}

		@Override
		protected void onPostExecute(String result) {
			HandleNotifications notifications = new  HandleNotifications();
			boolean delayedTrain=false;
			boolean cancelledTrain=false;
			Log.d("performance","onPostExecute started");
			
			InputStream is = new ByteArrayInputStream(result.getBytes());
			listOfDepartures=Utilities.getListOfTrains(is,10);
			
			Log.d("performance","Adapter being cleaned and reloaded");
			adapter.clear();
			for (VertrekkendeTrein vt : listOfDepartures) {
				Log.d(TAG, "Adding train ID " + vt.getRitNummer());
				if (vt.isSetDelay()){
					if (vt.getDelay().equalsIgnoreCase(VertrekkendeTrein.CANCELLED_TRAIN_STRING)){
						cancelledTrain=true;
					} else {
						delayedTrain=true;
					}
				}
				adapter.add(vt);
			}
			Log.d("performance","Adapter loaded");
			
			//TODO check for events that need Notifications
			notifications.setCancelledTrain(cancelledTrain);
			notifications.setDelayedTrain(delayedTrain);
			
			String ns = Context.NOTIFICATION_SERVICE;
			NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);
			
			if (cancelledTrain || delayedTrain){
				notifications.showNotification(TreinAlert.this, 
						mNotificationManager, HandleNotifications.NOTIFICATION_ID_DELAY_CLNCELLATION);
		
			} else {
				notifications.clearNotification(mNotificationManager, 
						HandleNotifications.NOTIFICATION_ID_DELAY_CLNCELLATION);
			}
		}
	}

	/**
	 * I think this one is the 'default' code executed when the button is clicked
	 * The link between the function name and the button is theid of the button
	 * @param view
	 */
	public void station01(View view) {
		Log.d("performance","Button Clicked");
		DownloadWebPageTask task = new DownloadWebPageTask();
		Log.d("performance","DownloadTask instantiated");
		task.execute(new String[] { "http://webservices.ns.nl/ns-api-avt?station=Almm" });
		textView.setText("Station : Almere Muziekwijk");
	}
	
	/**
	 * I think this one is the 'default' code executed when the button is clicked
	 * The link between the function name and the button is theid of the button
	 * @param view
	 */
	public void station02(View view) {
		DownloadWebPageTask task = new DownloadWebPageTask();
		task.execute(new String[] { "http://webservices.ns.nl/ns-api-avt?station=dmnz" });
		textView.setText("Station : Diemen Zuid");
	}
	
    /**
     * This is the adapter class that will be used to display the rows
     * First define the constructor that will use the user-defined layout
     * Override the getView method that will return the view to display
     */
	public class TrainAdapter extends ArrayAdapter<VertrekkendeTrein>{

    	public TrainAdapter() {
    		super(TreinAlert.this, R.layout.row,	listOfDepartures);
    		Log.d("TrainAdapter", "Adapter Class Instantiated");
    	}
    	
    	/*
    	 * (non-Javadoc)
    	 * @see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
    	 */
    	public View getView(int position, View convertView,
    							ViewGroup parent) {
    		Log.d("TrainAdapter", "getView called...");
    		View row=convertView;
    		TrainHolder holder=null;
    		
    		if (row==null) {
    			LayoutInflater inflater=getLayoutInflater();
    			
    			row=inflater.inflate(R.layout.row, parent, false);
    			holder=new TrainHolder(row);
    			row.setTag(holder);
    		} else {
    			holder=(TrainHolder)row.getTag();
    		}
    		
    		if (position< listOfDepartures.size()){
    			holder.populateForm(listOfDepartures.get(position));
    		}
    		
    		return(row);
    	}
    }
    
	/**
	 * This method displays the menu for the app
	 */
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	new MenuInflater(this).inflate(R.menu.option, menu);
    	return(super.onCreateOptionsMenu(menu));
    }
    
	/**
	 * This method decides what action to take when a menu item is selected
	 */
	
    public boolean onOptionsItemSelected(MenuItem item) {
    	if (item.getItemId() == R.id.about){
  
    		showDialog(SHOW_ABOUT_DIALOG);
    		return(true);
    	} else if (item.getItemId()==R.id.settings) {
    		//showDialog(SHOW_SETTINGS_COMING_DIALOG);
    		//Toast.makeText(TreinAlert.this, "The settings dialog will come soon", Toast.LENGTH_SHORT).show();
    		Intent intent = new Intent(this,SettingsMain.class);
    		startActivity(intent);
    		return(true);
    	}
    	
    	return(super.onOptionsItemSelected(item));
    }
	
	/**
	 * @author sandeep
	 *	This class actually assigns the text or images to the individual (Text or Image) views
	 */
    static class TrainHolder{
    	private TextView time;
    	private TextView delay;
    	private TextView destination;
    	private TextView type;
    	private TextView platform;
    	
		public TrainHolder(View row) {
			this.time = (TextView)row.findViewById(R.id.time);
			this.delay = (TextView)row.findViewById(R.id.delay);
			this.destination = (TextView)row.findViewById(R.id.destination);
			this.type = (TextView)row.findViewById(R.id.type);
			this.platform = (TextView)row.findViewById(R.id.platform);
		}
		
		void populateForm (VertrekkendeTrein t){
			Log.d("TrainHolder", "Filling a row for the row");
			time.setText(t.getVertrekTijdShort());
			delay.setText(t.getDelay());
			destination.setText(t.getEindBestemming());
			type.setText(t.getTreinSoort());
			platform.setText(t.getVertrekSpoor());
		}
    }
}