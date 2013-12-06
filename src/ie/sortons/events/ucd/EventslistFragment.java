package ie.sortons.events.ucd;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class EventslistFragment extends ListFragment implements OnItemClickListener {

	private LayoutInflater inflater;
	private ArrayList<HashMap<String, String>> events; 

	private DiscoveredEventRowAdapter adapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		adapter = new DiscoveredEventRowAdapter(inflater.getContext(), events);
		setListAdapter(adapter);
		this.inflater = inflater;
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		getListView().setDivider(null);
		getListView().setOnItemClickListener(this);
		registerForContextMenu(this.getListView());
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, view, menuInfo);

	    // Retrieve the item that was clicked on
	    AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
	    eventId = events.get(info.position).get("eventId");

		MenuInflater mInflater = this.getActivity().getMenuInflater();
		mInflater.inflate(R.menu.my_context_menu, menu);

		// If the Facebook app is installed, add the menu option
		try {
			inflater.getContext().getPackageManager().getPackageInfo("com.facebook.katana", 0);
			menu.add(0, 12254772, 1, R.string.open_in_facebook);
		} catch (NameNotFoundException e) {  }

	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {

		Intent intent;

		switch (item.getItemId()) {

		case R.id.open_in_browser: 
			intent =  new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/events/" + eventId));
			startActivity(intent);
			return true;

		case 12254772: 
			intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://event/" + eventId));
			startActivity(intent);
			return true;

		case R.id.add_to_calendar: 
			HashMap<String, String> event = new DBTools(inflater.getContext()).getEventInfo(eventId);
			
			intent = new Intent(Intent.ACTION_INSERT);
			intent.setType("vnd.android.cursor.item/event");
			intent.putExtra("beginTime", event.get("startTime"));
			// intent.putExtra("allDay", true);
			intent.putExtra("title", event.get("name") );
			intent.putExtra("eventLocation", event.get("location") );
			startActivity(intent);
			return true;

		default:
			return super.onContextItemSelected(item);
		}
	}

	String eventId;

	@Override
	public void onItemClick(AdapterView adapter, View view, int position, long id) {

		Intent intent;

		if (((TextView) view.findViewById(R.id.eventid)).getText() != null) {
			eventId = ((TextView) view.findViewById(R.id.eventid)).getText().toString();
			try {
				inflater.getContext().getPackageManager().getPackageInfo("com.facebook.katana", 0);
				intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://event/" + eventId));
			} catch (Exception e) {
				intent =  new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/events/" + eventId));
			}
			startActivity(intent);
		}
		else {
			Toast.makeText(getActivity().getBaseContext(), "Item clicked: " + position, Toast.LENGTH_LONG).show();
		}

	}


	public void setList(ArrayList<HashMap<String, String>> events) {
		this.events = events;	
	}
	
	public void updateList(){
		adapter.clear();
		adapter = new DiscoveredEventRowAdapter(inflater.getContext(), events);
		setListAdapter(adapter);
	}

}