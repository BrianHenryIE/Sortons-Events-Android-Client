package ie.sortons.events.ucd;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
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
	DBTools dbTools = new DBTools(this);
	ProgressDialog dialog;
	TextView txtMessage;
	ListView eventslistview;

	ViewGroup mapFrame; // (frame)

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		queryCloudEndpoint();

		eventslistview = (ListView) findViewById(R.id.events_list);


		// Restore state
		if (savedInstanceState != null) {
			// The fragment manager will handle restoring them if we are being
			// restored from a saved state
		}
		// If this is the first creation of the activity, add fragments to it
		else {

			// If our layout has a container for the image selector fragment,
			// create and add it
			mapFrame = (ViewGroup) findViewById(R.id.map_frame);
			if (mapFrame != null) {
				Log.i("oncreate", "onCreate: adding ImageSelectorFragment to MainActivity");

				// Add image selector fragment to the activity's container layout
				MapFragment mapFragment = new MapFragment();
				FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
				fragmentTransaction.replace(mapFrame.getId(), mapFragment, MapFragment.class.getName());

				// Commit the transaction
				fragmentTransaction.commit();
			}
		}

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
				txtMessage = (TextView) findViewById(R.id.result_text);
				txtMessage.setText("Connecting....");
				dialog = ProgressDialog.show(MainActivity.this, null /* title */, "Please wait...");
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
				txtMessage.setText("");

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
					entry.put("latitude", e.getLatitude().toString());
					entry.put("longtitude", e.getLongitude().toString());

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
				dbTools.deleteAllEventsBeforeDate(new Date());

				showList(data);

				dialog.dismiss();
			}
		};

		task.execute("197528567092983");
	}

	private void showList(List<DiscoveredEvent> upcomingEvents){	
		DiscoveredEventRowAdapter adapter = new DiscoveredEventRowAdapter(MainActivity.this, upcomingEvents);
		eventslistview.setAdapter(adapter);

	}

}
