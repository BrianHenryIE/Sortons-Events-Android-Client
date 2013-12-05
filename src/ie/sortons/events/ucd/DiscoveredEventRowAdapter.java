package ie.sortons.events.ucd;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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
		Log.i("RowAdapterDBTools", "Discovered event Row Adapter constructed with " + events.size() + " events");
	}

	static class ViewHolder {

		protected TextView name;
		protected TextView time;
		protected TextView location;
		protected ImageView picture;
		protected TextView eventId;
	}


	@Override
	public int getCount() {
		return events.size();
	}
	/* (non-Javadoc)
	 * @see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		Log.i("View", "getting the view");
		if (convertView == null) {
			LayoutInflater inflator = ((Activity) context).getLayoutInflater();
			view = inflator.inflate(R.layout.discoveredeventlistrow, null);
			final ViewHolder viewHolder = new ViewHolder();
			viewHolder.name = (TextView) view.findViewById(R.id.eventrowname);
			viewHolder.time = (TextView) view.findViewById(R.id.eventrowtime);
			viewHolder.location = (TextView) view.findViewById(R.id.eventrowlocation);
			viewHolder.picture = (ImageView) view.findViewById(R.id.eventpicture);
			viewHolder.eventId = (TextView) view.findViewById(R.id.eventid);
			view.setTag(viewHolder);

		} else {
			view = convertView;
		}
		Log.i("building list " + position + " name", events.get(position).get("name"));
		ViewHolder holder = (ViewHolder) view.getTag();
		holder.name.setText(events.get(position).get("name"));


		// holder.time.setText(events.get(position).get("startTime"));




		// Friday at 22:30
		// EEEE' at 'k:m

		// Thursday, 05 December, at 13:00
		// EEEE', 'MM' 'LLLL', at 'k:m

		// Thursday, 05 March 2014

		// TODO
		// Localise these strings!

		try {
			Date eventDate  = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH).parse(events.get(position).get("startTimeDate"));

			// Default date format
			// For events this year
			SimpleDateFormat sdf = new SimpleDateFormat("EEEE', 'MM' 'LLLL', at 'k':'mm", Locale.getDefault());

			// For events today
			if ( eventDate.getDay() == new Date().getDay() )
				if ( eventDate.getHours() == 0 && eventDate.getMinutes() == 0 )
					sdf = new SimpleDateFormat("'Today'", Locale.getDefault());
				else
					sdf = new SimpleDateFormat("'Today at 'k':'mm", Locale.getDefault());

			// For events tomorrow
			if ( eventDate.getDay()-1 == new Date().getDay() )
				if ( eventDate.getHours() == 0 && eventDate.getMinutes() == 0 )
					sdf = new SimpleDateFormat("'Tomorrow'", Locale.getDefault());
				else
					sdf = new SimpleDateFormat("'Tomorrow at 'k':'mm", Locale.getDefault());

			// For events this week
			if ( eventDate.getDay() - (new Date().getDay()) < 5 )
				if ( eventDate.getHours() == 0 && eventDate.getMinutes() == 0 )
					sdf = new SimpleDateFormat("EEEE", Locale.getDefault());
				else
					sdf = new SimpleDateFormat("EEEE' at 'k':'mm", Locale.getDefault());

			// For events next year
			if ( eventDate.getYear() != new Date().getYear() )
				if ( eventDate.getHours() == 0 && eventDate.getMinutes() == 0 )
					sdf = new SimpleDateFormat("EEEE', 'MM' 'LLLL', 'yyyy", Locale.getDefault());
				else
					sdf = new SimpleDateFormat("EEEE', 'MM' 'LLLL', 'yyyy', at 'k':'mm", Locale.getDefault());


			holder.time.setText( sdf.format( eventDate ) );


		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}


		holder.location.setText(events.get(position).get("location"));
		holder.eventId.setText(events.get(position).get("eventId"));

		File eventPic = new File(context.getCacheDir(), events.get(position).get("eventId"));	
		if ( eventPic.exists() )
			try {
				holder.picture.setImageBitmap( Bitmap.createBitmap(BitmapFactory.decodeFile( eventPic.getCanonicalPath() ), 0, 0, 50, 50) );
			} catch (IOException e) {
				e.printStackTrace();
			}

		return view;
	}
} 
