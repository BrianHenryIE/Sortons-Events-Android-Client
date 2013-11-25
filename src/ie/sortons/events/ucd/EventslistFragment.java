package ie.sortons.events.ucd;

import java.util.ArrayList;
import java.util.HashMap;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class EventslistFragment extends ListFragment implements OnItemClickListener{

	private ArrayList<HashMap<String, String>> events; 
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		DiscoveredEventRowAdapter adapter = new DiscoveredEventRowAdapter(inflater.getContext(), events);
		setListAdapter(adapter);
		
		
		
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

		Toast.makeText(getActivity().getBaseContext(), "Item clicked: " + position, Toast.LENGTH_LONG).show();

	}
	
	
	public void setList(ArrayList<HashMap<String, String>> events) {
		this.events = events;		
	}



}