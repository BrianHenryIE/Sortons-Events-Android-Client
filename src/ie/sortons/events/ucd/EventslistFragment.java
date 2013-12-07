package ie.sortons.events.ucd;

import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
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

import com.appspot.sortonsevents.upcomingEvents.model.DiscoveredEvent;

public class EventslistFragment extends ListFragment implements OnItemClickListener {

	private LayoutInflater inflater;

	private DiscoveredEventRowAdapter adapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		adapter = new DiscoveredEventRowAdapter(inflater.getContext(), Data.events);
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
	    eventId = Data.events.get(info.position).getEid();

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
			DiscoveredEvent event = new DbTools(inflater.getContext()).getEvent(eventId);
			
			intent = new Intent(Intent.ACTION_INSERT);
			intent.setType("vnd.android.cursor.item/event");
			intent.putExtra("beginTime", event.getFbEvent().getStartTime());
			// intent.putExtra("allDay", true);
			intent.putExtra("title", event.getFbEvent().getName());
			intent.putExtra("eventLocation", event.getFbEvent().getLocation());
			startActivity(intent);
			return true;

		default:
			return super.onContextItemSelected(item);
		}
	}

	String eventId;

	@SuppressWarnings("rawtypes")
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

	}

	public void showEvents(){
		if(adapter!=null && Data.events != null){
			adapter.clear();
			adapter = new DiscoveredEventRowAdapter(inflater.getContext(), Data.events);
			setListAdapter(adapter);
		}
	}



}