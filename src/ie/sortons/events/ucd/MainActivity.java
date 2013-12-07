package ie.sortons.events.ucd;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

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
import android.view.ViewGroup;

import com.appspot.sortonsevents.clientdata.Clientdata;
import com.appspot.sortonsevents.clientdata.model.ClientPageData;
import com.appspot.sortonsevents.upcomingEvents.UpcomingEvents;
import com.appspot.sortonsevents.upcomingEvents.model.DiscoveredEvent;
import com.appspot.sortonsevents.upcomingEvents.model.DiscoveredEventCollection;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.gson.GsonFactory;

public class MainActivity extends FragmentActivity {

	DbTools dbTools = new DbTools(this);

	ViewGroup mapFrame; // (frame)
	ViewGroup eventslistFrame; // (frame)
	ViewGroup newsfeedFrame; // (frame)

	EventslistFragment eventslistFragment;
	MapFragment mapFragment;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Data.events = dbTools.getEvents();

		setContentView(R.layout.activity_main);

		if (isXLargeScreen(this))
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
				eventslistFragment.showEvents();
			}

			mapFrame = (ViewGroup) findViewById(R.id.map_frame);
			if (mapFrame != null) {
				mapFragment = new MapFragment();
				fragmentTransaction.replace(mapFrame.getId(), mapFragment, MapFragment.class.getName());
			}

			newsfeedFrame = (ViewGroup) findViewById(R.id.newsfeed_frame);
			if (newsfeedFrame != null) {
				NewsfeedFragment newsfeedFragment = new NewsfeedFragment();
				fragmentTransaction.replace(newsfeedFrame.getId(), newsfeedFragment, NewsfeedFragment.class.getName());
			}

			fragmentTransaction.commit();
		}

		getUpcomingEvents();

		getClientPageData();

		// TODO
		// unbreak phones
		if (!isXLargeScreen(this)) {
			Intent phoneIntent = new Intent(getApplication(), MainPhoneActivity.class);
			startActivity(phoneIntent);
			finish();
		}
	}

	public void onConfigurationChanged() {
		// http://androidblogger.blogspot.ie/2011/08/orientation-for-both-phones-and-tablets.html
	}

	public void refreshViews() {
		if (eventslistFragment != null)
			eventslistFragment.showEvents();

		if (mapFragment != null)
			mapFragment.showEvents();
	}

	private void getUpcomingEvents() {

		AsyncTask<String, Void, List<DiscoveredEvent>> task = new AsyncTask<String, Void, List<DiscoveredEvent>>() {

			@Override
			protected List<DiscoveredEvent> doInBackground(String... params) {
				UpcomingEvents.Builder builder = new UpcomingEvents.Builder(AndroidHttp.newCompatibleTransport(), new GsonFactory(), null /* httpRequestInitializer */);
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

				if (data != null)
					dbTools.updateEvents(data);
				
				refreshViews();

				for (DiscoveredEvent event : dbTools.getEvents())
					new GetPicture().execute(event.getEid());

			}
		};

		task.execute(Data.CLIENTID);
	}

	// TODO:
	// Disadvantages of using AsyncTasks
	// The AsyncTask does not handle configuration changes automatically, i.e.
	// if the activity is recreated, the programmer has to handle that in his
	// coding.
	// A common solution to this is to declare the AsyncTask in a retained
	// headless fragment.
	private class GetPicture extends AsyncTask<String, Void, Boolean> {

		@Override
		protected Boolean doInBackground(String... params) {
			Boolean imageUpdated = false;
			try {
				// TODO
				// Square isn't always 50x50
				URL url = new URL("https://graph.facebook.com/" + params[0] + "/picture?type=square");
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				HttpURLConnection.setFollowRedirects(true);
				connection.connect();

				// TBH I don't know if it's pulled the whole thing down from the
				// server at this point.
				String picUrl = "";
				if (dbTools.getEvent(params[0]).getFbEvent() != null && dbTools.getEvent(params[0]).getFbEvent().getPicSquare() != null)
					picUrl = dbTools.getEvent(params[0]).getFbEvent().getPicSquare();

				// TODO
				// This isn't working
				if (!connection.getURL().toString().equals(picUrl)) {
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
					} catch (IOException e) {
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
			if (imageUpdated)
				refreshViews();
		}
	};

	private void getClientPageData() {

		AsyncTask<String, Void, ClientPageData> task = new AsyncTask<String, Void, ClientPageData>() {

			@Override
			protected ClientPageData doInBackground(String... params) {
				Clientdata.Builder builder = new Clientdata.Builder(AndroidHttp.newCompatibleTransport(), new GsonFactory(), null /* httpRequestInitializer */);
				try {
					return  builder.build().clientPageDataEndpoint().getClientPageData(params[0]).execute();
				} catch (IOException e) {
					Log.e("error:", e.toString());
					return null;
				}
			}

			@Override
			public void onPostExecute(ClientPageData data) {
				Data.clientPageData = data;
				for (com.appspot.sortonsevents.clientdata.model.FbPage page : data.getIncludedPages())
					if (!page.getPageUrl().contains("pages/"))
						Data.fbPages.put(page.getPageUrl().replace("http://www.facebook.com/", ""), page.getPageId());
			}
		};

		task.execute(Data.CLIENTID);
	}

	private boolean isXLargeScreen(Context context) {
		return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
	}

}
