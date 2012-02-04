package com.sandeepthukral.tutorial;


import java.io.BufferedReader;
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
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.sandeepthukral.tutorial.util.Utilities;

public class TreinAlert extends Activity {
	private TextView textView;
	List<VertrekkendeTrein> listOfDepartures=new ArrayList<VertrekkendeTrein>();
	TrainAdapter adapter=null;
	
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
			Log.d("performance","onPostExecute started");
			listOfDepartures=Utilities.getDepartureDetails(result,10);
			
			Log.d("performance","Adapter being cleaned and reloaded");
			adapter.clear();
			for (VertrekkendeTrein vt : listOfDepartures) {
				Log.d(TAG, "Adding train ID " + vt.getRitNummer());
				adapter.add(vt);
			}
			Log.d("performance","Adapter loaded");
		}
	}

	public void station01(View view) {
		Log.d("performance","Button Clicked");
		DownloadWebPageTask task = new DownloadWebPageTask();
		Log.d("performance","DownloadTask instantiated");
		task.execute(new String[] { "http://webservices.ns.nl/ns-api-avt?station=Almm" });
		textView.setText("Station : Almere Muziekwijk");
	}
	
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