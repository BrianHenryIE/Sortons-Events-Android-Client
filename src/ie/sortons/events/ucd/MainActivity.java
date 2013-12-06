package ie.sortons.events.ucd;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.TextView;

import com.appspot.sortonsevents.upcomingEvents.UpcomingEvents;
import com.appspot.sortonsevents.upcomingEvents.model.DiscoveredEvent;
import com.appspot.sortonsevents.upcomingEvents.model.DiscoveredEventCollection;
import com.appspot.sortonsevents.upcomingEvents.model.FbEvent;
import com.appspot.sortonsevents.upcomingEvents.model.FbPage;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.gson.GsonFactory;


public class MainActivity extends FragmentActivity {

	private String clientId = "197528567092983";

	DBTools dbTools = new DBTools(this);
	ProgressDialog dialog;
	TextView txtMessage;


	ViewGroup mapFrame; // (frame)
	ViewGroup eventslistFrame; // (frame)
	ViewGroup newsfeedFrame; // (frame)

	EventslistFragment eventslistFragment;
	MapFragment mapFragment; 

	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if ( isXLargeScreen(this) )
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		else
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

		// Restore state
		if (savedInstanceState != null) {
			// The fragment manager will handle restoring them if we are being
			// restored from a saved state

		} else {
			// If this is the first creation of the activity, add fragments to it
			// If our layout has a container for the events list... fragment, create and add it

			eventslistFrame = (ViewGroup) findViewById(R.id.eventslist_frame);
			if (eventslistFrame != null) {
				eventslistFragment = new EventslistFragment();
				fragmentTransaction.replace(eventslistFrame.getId(), eventslistFragment, EventslistFragment.class.getName());
				eventslistFragment.setList(dbTools.getEvents());
			}

			mapFrame = (ViewGroup) findViewById(R.id.map_frame);
			if (mapFrame != null) {
				mapFragment = new MapFragment();
				fragmentTransaction.replace(mapFrame.getId(), mapFragment, MapFragment.class.getName());
				mapFragment.setList(dbTools.getEvents());
			}

			newsfeedFrame = (ViewGroup) findViewById(R.id.newsfeed_frame);
			if (newsfeedFrame != null) {
				NewsfeedFragment newsfeedFragment = new NewsfeedFragment();
				fragmentTransaction.replace(newsfeedFrame.getId(), newsfeedFragment, NewsfeedFragment.class.getName());
			}

			fragmentTransaction.commit();
		}

		queryCloudEndpoint();

		getPics();
		if (!isXLargeScreen(this)) {
			Intent phoneIntent = new Intent(getApplication(), MainPhoneActivity.class);
			startActivity(phoneIntent);
			finish();
		}
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void onConfigurationChanged(){
		// http://androidblogger.blogspot.ie/2011/08/orientation-for-both-phones-and-tablets.html
	}


	private void queryCloudEndpoint() {

		AsyncTask<String, Void, List<DiscoveredEvent>> task = new AsyncTask<String, Void, List<DiscoveredEvent>>() {

			@Override
			public void onPreExecute() {

				// txtMessage.setText("Connecting....");
				//dialog = ProgressDialog.show(MainActivity.this, null /* title */, "Please wait...");
			}

			@Override
			protected List<DiscoveredEvent> doInBackground(String... params) {
				UpcomingEvents.Builder builder = new UpcomingEvents.Builder(
						AndroidHttp.newCompatibleTransport(), new GsonFactory(), null /* httpRequestInitializer */);
				try {
					DiscoveredEventCollection pojo = builder.build().upcomingEventsEndpoint().getList(params[0]).execute();

					return pojo.getItems();

				} catch (IOException e) {
					Log.e("error:", e.toString());
					return null;
				}
			}

			@Override
			public void onPostExecute(List<DiscoveredEvent> data) {
				// txtMessage.setText("");

				if (data != null) {
					Log.i("onPostExecute", "endpoint returned "+data.size());
					ArrayList<HashMap<String,String>> eventsToBeAdded = new ArrayList<HashMap<String,String>>();
					ArrayList<HashMap<String,String>> pagesToBeAdded = new ArrayList<HashMap<String, String>>();
					for (int i = 0; i < data.size(); i++) {
						HashMap<String,String> entry = new HashMap<String,String>();
						FbEvent e = data.get(i).getFbEvent();
						String eventId = e.getEid();
						entry.put("eventId", eventId);
						entry.put("name",  e.getName());
						entry.put("location", e.getLocation());
						entry.put("startTimeDate", e.getStartTimeDate().toString());
						entry.put("startTime", e.getStartTime());
						if (e.getLatitude() != null) {
							entry.put("latitude", e.getLatitude().toString());
						}
						if (e.getLongitude() != null) {
							entry.put("longitude", e.getLongitude().toString());
						}
						for(int j = 0; j < data.get(i).getSourcePages().size(); j++) {
							FbPage p = data.get(i).getSourcePages().get(j);
							HashMap<String, String> page = new HashMap<String, String>(); 
							page.put("eventId", eventId);
							page.put("pageId", p.getPageId());
							page.put("name", p.getName());
							page.put("pageUrl", p.getPageUrl());
							pagesToBeAdded.add(page);
						}

						eventsToBeAdded.add(entry);
					}
					dbTools.insertSourcePages(pagesToBeAdded);
					dbTools.updateEvents(eventsToBeAdded);
				}
				else {
					Log.i("onPostExecute", "no data was returned");
				}

				if ( eventslistFragment != null )					
					eventslistFragment.setList( dbTools.getEvents() );

				if ( mapFragment != null )					
					mapFragment.setList( dbTools.getEvents() );


				//dialog.dismiss();
			}
		};

		task.execute(clientId);
	}


	// https://graph.facebook.com/shaverm/picture?type=square
	private void getPics() {
		for(HashMap<String, String> event : dbTools.getEvents())
			new GetPicture().execute(event.get("eventId"));		
	}


	// TODO:
	// Disadvantages of using AsyncTasks
	// The AsyncTask does not handle configuration changes automatically, i.e. if the activity is recreated, the programmer has to handle that in his coding.
	// A common solution to this is to declare the AsyncTask in a retained headless fragment.
	private class GetPicture extends AsyncTask<String, Void, Boolean> {

		@Override
		public void onPreExecute() {				

		}

		@Override
		protected Boolean doInBackground(String... params) {
			Boolean imageUpdated = false;

			try {
				// TOOD
				// Square isn't always 50x50
				URL url = new URL ( "https://graph.facebook.com/" + params[0] + "/picture?type=square" );
				HttpURLConnection connection =  (HttpURLConnection) url.openConnection();
				HttpURLConnection.setFollowRedirects(true);
				connection.connect();

				// TBH I don't know if it's pulled the whole thing down from the server at this point.
				if( connection.getURL().equals( dbTools.getEventInfo(params[0]).get("picUrl") ) ) {
					imageUpdated = true;
					InputStream in = connection.getInputStream();
					Bitmap newImage = BitmapFactory.decodeStream(in);
					File saveImage;
					try {
						saveImage = new File(MainActivity.this.getCacheDir(), params[0]);
						FileOutputStream fOut = new FileOutputStream(saveImage);
						newImage.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
						fOut.flush();
						fOut.close();

						dbTools.savePicUrl(params[0], connection.getURL().toString());
					}
					catch (IOException e) {
						// Error while creating file
					}
				}

			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			return imageUpdated;
		}

		@Override
		public void onPostExecute(Boolean imageUpdated) {

			if(imageUpdated) {
				// TODO
				// find the item in the list and show the new picture
			}

		}
	};


	private boolean isXLargeScreen(Context context) {
		return (context.getResources().getConfiguration().screenLayout
				& Configuration.SCREENLAYOUT_SIZE_MASK)
				>= Configuration.SCREENLAYOUT_SIZE_XLARGE;
	}

}
