package ie.sortons.events.ucd;

import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.appspot.sortonsevents.upcomingEvents.model.DiscoveredEvent;

public class DiscoveredEventRowAdapter extends ArrayAdapter<DiscoveredEvent> {

	private final Context context;
	private final List<DiscoveredEvent> upcomingEvents;

	public DiscoveredEventRowAdapter(Context context, List<DiscoveredEvent> upcomingEvents) {
		super(context, R.layout.discoveredeventlistrow, upcomingEvents);
		this.context = context;
		this.upcomingEvents = upcomingEvents;
	}

	static class ViewHolder {

		protected TextView name;
		protected TextView time;
		protected TextView location;
	}


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
				
			view.setTag(viewHolder);
			
		} else {
			view = convertView;
		}
		ViewHolder holder = (ViewHolder) view.getTag();
		holder.name.setText(upcomingEvents.get(position).getFbEvent().getName());
		holder.time.setText(upcomingEvents.get(position).getFbEvent().getStartTime());
		holder.location.setText(upcomingEvents.get(position).getFbEvent().getLocation());
		
		return view;
	}
} 
