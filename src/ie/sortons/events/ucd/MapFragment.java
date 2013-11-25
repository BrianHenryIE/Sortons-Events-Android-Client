package ie.sortons.events.ucd;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapFragment extends Fragment {

	Context context; 
	public void setArguments(Context context){
		this.context = context;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_map, container, false);
		Log.i("map fragment", "onCreateView");

		try {
		    MapsInitializer.initialize(context);
		} catch (GooglePlayServicesNotAvailableException e) {
		    Log.e("map", " " + e.getMessage());
		}
		
		
		Log.e("map stuff", " should be displaying a map");
	
		FragmentManager myFM = getActivity().getSupportFragmentManager();
		//final SupportMapFragment myMAPF = (SupportMapFragment) myFM.findFragmentById(R.id.map);
		
		// Get a handle to the Map Fragment
		// GoogleMap map = ((SupportMapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		GoogleMap map = ((SupportMapFragment) myFM.findFragmentById(R.id.map)).getMap();
		
		map.getUiSettings().setZoomControlsEnabled(false);
		map.getUiSettings().setMyLocationButtonEnabled(false);
		
		LatLng home = new LatLng(53.307775, -6.219453);

		map.moveCamera(CameraUpdateFactory.newLatLngZoom(home, 14));

		LatLng sydney = new LatLng(-33.867, 151.206);
		map.addMarker(new MarkerOptions().title("Sydney").snippet("The most populous city in Australia.").position(sydney));

		return view;
	}


}
