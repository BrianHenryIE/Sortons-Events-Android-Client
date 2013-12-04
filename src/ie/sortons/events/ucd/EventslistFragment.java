package ie.sortons.events.ucd;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class EventslistFragment extends ListFragment implements OnItemClickListener {

	private LayoutInflater inflater;
	private ArrayList<HashMap<String, String>> events; 
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		DiscoveredEventRowAdapter adapter = new DiscoveredEventRowAdapter(inflater.getContext(), events);
		setListAdapter(adapter);
		this.inflater = inflater;
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		this.getListView().setDivider(null);
		getListView().setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView adapter, View view, int position, long id) {

		Intent intent;
		String eventId;
		if (((TextView) view.findViewById(R.id.eventid)).getText() != null) {
			eventId = ((TextView) view.findViewById(R.id.eventid)).getText().toString();
			try {
			    inflater.getContext().getPackageManager().getPackageInfo("com.facebook.katana", 0);
			    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://event/" + eventId));
			   } catch (Exception e) {
			    intent =  new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/" + eventId));
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



}