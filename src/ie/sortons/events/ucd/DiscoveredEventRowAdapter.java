package ie.sortons.events.ucd;

import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.util.Log;

import com.appspot.sortonsevents.upcomingEvents.model.DiscoveredEvent;

public class DiscoveredEventRowAdapter extends ArrayAdapter<DiscoveredEvent> {
	DBTools dbTools;
	private final Context context;
	List<HashMap<String,String>> events;
	public DiscoveredEventRowAdapter(Context context, List<HashMap<String,String>> events) {
		super(context, R.layout.discoveredeventlistrow);
		this.context = context;
		dbTools = new DBTools(context);
		this.events = events;
		Log.i("RowAdapterDBTools", "Discovered event Row Adapter constructed");
	}

	static class ViewHolder {

		protected TextView name;
		protected TextView time;
		protected TextView location;
		protected ImageView picture;
	}


	/* (non-Javadoc)
	 * @see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		if (convertView == null) {
			LayoutInflater inflator = ((Activity) context).getLayoutInflater();
			view = inflator.inflate(R.layout.discoveredeventlistrow, null);
			final ViewHolder viewHolder = new ViewHolder();
			viewHolder.name = (TextView) view.findViewById(R.id.eventrowname);
			viewHolder.time = (TextView) view.findViewById(R.id.eventrowtime);
			viewHolder.location = (TextView) view.findViewById(R.id.eventrowlocation);
			viewHolder.picture = (ImageView) view.findViewById(R.id.eventpicture);
			view.setTag(viewHolder);
			
		} else {
			view = convertView;
		}
		ViewHolder holder = (ViewHolder) view.getTag();
		holder.name.setText(events.get(position).get("name"));
		holder.time.setText(events.get(position).get("startTime"));
		holder.location.setText(events.get(position).get("location"));
		
//		URL imageUrl = null;
//		try {
//			imageUrl = new URL("//graph.facebook.com/" + upcomingEvents.get(position).getFbEvent().getEid() + "/picture?type=square");
//		} catch (MalformedURLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		holder.picture.setImageURL(imageUrl);
//		
		return view;
	}
} 
