package ie.sortons.events.ucd;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;

import com.appspot.sortonsevents.upcomingEvents.model.DiscoveredEvent;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapFragment extends SupportMapFragment {

	GoogleMap map = getMap();

	public MapFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle bundle) {
		View view = super.onCreateView(inflater, viewGroup, bundle);

		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		view.setLayoutParams(params);

		return view;
	}

	public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        map = this.getMap();

		map.moveCamera(CameraUpdateFactory.newLatLngZoom(Data.MAPCENTRE, 14));

		map.getUiSettings().setZoomControlsEnabled(false);
		map.getUiSettings().setMyLocationButtonEnabled(false);

		showEvents();

    }

	public void showEvents() {

		if (Data.events != null && map != null && this.isAdded()) {
			map.clear();
			for (DiscoveredEvent event : Data.events) {
				if (event.getFbEvent().getLatitude() != null && event.getFbEvent().getLongitude() != null) {
					LatLng eventPin = new LatLng(event.getFbEvent().getLatitude(), event.getFbEvent().getLongitude());
					map.addMarker(new MarkerOptions().title(event.getFbEvent().getName()).snippet(event.getFbEvent().getLocation())
							.position(eventPin));
				}
			}
		}

	}

}
