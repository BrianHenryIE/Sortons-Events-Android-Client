package ie.sortons.events.ucd;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.ListView;
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
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

		// Restore state
		if (savedInstanceState != null) {
			// The fragment manager will handle restoring them if we are being
			// restored from a saved state
			Log.i("mainactivity", "savedInstanceState!=null");
		} else {

			// If this is the first creation of the activity, add fragments to it

			Log.i("mainactivity", "savedInstanceState==null");

			// If our layout has a container for the image selector fragment,
			// create and add it
			eventslistFrame = (ViewGroup) findViewById(R.id.eventslist_frame);
			if (eventslistFrame != null) {
				Log.i("oncreate", "onCreate: adding EventslistFragment to MainActivity");

				// Add map fragment to the activity's container layout
				eventslistFragment = new EventslistFragment();
				

				fragmentTransaction.replace(eventslistFrame.getId(), eventslistFragment, EventslistFragment.class.getName());

				eventslistFragment.setList(dbTools.getEvents());
			}
			
			
			// If our layout has a container for the image selector fragment,
			// create and add it
			mapFrame = (ViewGroup) findViewById(R.id.map_frame);
			if (mapFrame != null) {
				Log.i("oncreate", "onCreate: adding MapFragment to MainActivity");

				// Add map fragment to the activity's container layout
				MapFragment mapFragment = new MapFragment();
				mapFragment.setArguments(this);

				fragmentTransaction.replace(mapFrame.getId(), mapFragment, MapFragment.class.getName());

			}

			
			
			newsfeedFrame = (ViewGroup) findViewById(R.id.newsfeed_frame);
			if (mapFrame != null) {
				Log.i("oncreate", "onCreate: adding NewsfeedFragment to MainActivity");

				// Add map fragment to the activity's container layout
				NewsfeedFragment newsfeedFragment = new NewsfeedFragment();
				newsfeedFragment.setArguments(this);

				fragmentTransaction.replace(newsfeedFrame.getId(), newsfeedFragment, NewsfeedFragment.class.getName());

				fragmentTransaction.replace(newsfeedFrame.getId(), newsfeedFragment, NewsfeedFragment.class.getName());

			}
			// TODO maybe only when there is something to commit!
			// Commit the transaction
			fragmentTransaction.commit();

		}
		
				
		queryCloudEndpoint();
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
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

					Log.i("Asd", pojo.toPrettyString());

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
							entry.put("longtitude", e.getLongitude().toString());
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
					dbTools.insertEvents(eventsToBeAdded);
					dbTools.insertSourcePages(pagesToBeAdded);
					dbTools.deleteAllEventsBeforeDate(new Date()); //TODO: delete everything instead before updating or change primary key
				}
				else {
					Log.i("onPostExecute", "no data was returned");
				}
				
				if ( eventslistFragment != null ) {
					
					eventslistFragment.setList(dbTools.getEvents());
					
				}
					

				//dialog.dismiss();
			}
		};

		task.execute(clientId);
	}


}
