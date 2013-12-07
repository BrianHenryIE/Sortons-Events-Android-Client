package ie.sortons.events.ucd;

import java.util.HashMap;
import java.util.List;

import com.appspot.sortonsevents.clientdata.model.ClientPageData;
import com.appspot.sortonsevents.upcomingEvents.model.DiscoveredEvent;
import com.google.android.gms.maps.model.LatLng;

public class Data {

	public static final String CLIENTID = "197528567092983";
	public static final LatLng MAPCENTRE = new LatLng(53.307775, -6.219453);
	public static ClientPageData clientPageData;
	public static HashMap<String, String> fbPages = new HashMap<String, String>();
	public static List<DiscoveredEvent> events;

}
