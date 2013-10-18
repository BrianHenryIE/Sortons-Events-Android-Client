package ie.sortons.events.ucd;


import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.ListView;
import android.widget.TextView;

import com.appspot.sortonsevents.upcomingEvents.UpcomingEvents;
import com.appspot.sortonsevents.upcomingEvents.model.DiscoveredEvent;
import com.appspot.sortonsevents.upcomingEvents.model.DiscoveredEventCollection;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.gson.GsonFactory;


public class MainActivity extends Activity {




	List<DiscoveredEvent> upcomingEvents;

	ProgressDialog dialog;
	TextView txtMessage;
	ListView eventslistview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		queryCloudEndpoint();

		eventslistview = (ListView) findViewById(R.id.events_list);

	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}


	private void queryCloudEndpoint() {

		AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {

			@Override
			public void onPreExecute() {
				txtMessage = (TextView) findViewById(R.id.result_text);
				txtMessage.setText("Connecting....");
				dialog = ProgressDialog.show(MainActivity.this, null /* title */, "Please wait...");
			}

			@Override
			protected String doInBackground(Void... params) {
				UpcomingEvents.Builder builder = new UpcomingEvents.Builder(
						AndroidHttp.newCompatibleTransport(), new GsonFactory(), null /* httpRequestInitializer */);
				try {
					DiscoveredEventCollection pojo = builder.build().upcomingEventsEndpoint().getList("197528567092983").execute();

					upcomingEvents = pojo.getItems();

					return null;
				} catch (IOException e) {
					Log.e("error:", e.toString());
					return e.toString();
				}
			}

			@Override
			public void onPostExecute(String message) {
				txtMessage.setText("");
				
				Log.i("onPostExecute", "endpoint returned "+upcomingEvents.size());

				showList();

				dialog.dismiss();
			}
		};

		task.execute();
	}

	private void showList(){	
		DiscoveredEventRowAdapter adapter = new DiscoveredEventRowAdapter(MainActivity.this, upcomingEvents);
		eventslistview.setAdapter(adapter);

	}
	
}
