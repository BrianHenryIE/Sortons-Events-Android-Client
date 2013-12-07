package ie.sortons.events.ucd;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.appspot.sortonsevents.upcomingEvents.model.DiscoveredEvent;

public class DiscoveredEventRowAdapter extends ArrayAdapter<DiscoveredEvent> {

	private final Context context;
	private List<DiscoveredEvent> events;

	public DiscoveredEventRowAdapter(Context context, List<DiscoveredEvent> events) {
		super(context, R.layout.discoveredeventlistrow);
		this.context = context;
		this.events = events;
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
		// TODO
		// Does this need to be overwriten?
		return events.size();
	}

	/*
	 * (non-Javadoc)
	 * 
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
			viewHolder.eventId = (TextView) view.findViewById(R.id.eventid);
			view.setTag(viewHolder);

		} else {
			view = convertView;
		}

		ViewHolder holder = (ViewHolder) view.getTag();

		holder.name.setText(events.get(position).getFbEvent().getName());
		holder.location.setText(events.get(position).getFbEvent().getLocation());
		holder.eventId.setText(events.get(position).getEid());

		try {
			// TODO
			// Use the DateTime object and not the string
			Date eventDate;
			if (events.get(position).getFbEvent().getStartTime().length() == 10)
				eventDate = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(events.get(position).getFbEvent().getStartTime());
			else
				eventDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssz", Locale.ENGLISH).parse(events.get(position).getFbEvent().getStartTime());

			Calendar now = new GregorianCalendar();
			now.setTime(new Date());

			Calendar timeOfEvent = new GregorianCalendar();
			timeOfEvent.setTime(eventDate);

			Calendar eventTomorrow = new GregorianCalendar();
			eventTomorrow.setTime(eventDate);
			eventTomorrow.add(Calendar.DATE, -1);

			Calendar eventNextFiveDays = new GregorianCalendar();
			eventNextFiveDays.setTime(eventDate);
			eventNextFiveDays.add(Calendar.DATE, -5);

			String sdfTemplate = null;
			// For events today
			if (timeOfEvent.get(Calendar.DAY_OF_YEAR) == Calendar.getInstance().get(Calendar.DAY_OF_YEAR)) {
				if (eventDate.getHours() == 0 && eventDate.getMinutes() == 0)
					sdfTemplate = "'" + context.getString(R.string.today) + "'";
				else
					sdfTemplate = "'" + context.getString(R.string.today_at) + "'k':'mm";

				// For events tomorrow
			} else if (eventTomorrow.get(Calendar.DAY_OF_YEAR) == Calendar.getInstance().get(Calendar.DAY_OF_YEAR)) {
				if (eventDate.getHours() == 0 && eventDate.getMinutes() == 0)
					sdfTemplate = "'" + context.getString(R.string.tomorrow) + "'";
				else
					sdfTemplate = "'" + context.getString(R.string.tomorrow_at) + "'k':'mm";

				// For events this week
			} else if (eventNextFiveDays.get(Calendar.DAY_OF_YEAR) < Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
					&& eventNextFiveDays.get(Calendar.YEAR) == Calendar.getInstance().get(Calendar.YEAR)) {
				if (eventDate.getHours() == 0 && eventDate.getMinutes() == 0)
					sdfTemplate = "EEEE";
				else
					sdfTemplate = "EEEE' at 'k':'mm";

				// For events next year
			} else if (now.get(Calendar.YEAR) < timeOfEvent.get(Calendar.YEAR)) {
				if (eventDate.getHours() == 0 && eventDate.getMinutes() == 0)
					sdfTemplate = "EEEE', 'd' 'LLLL', 'yyyy";
				else
					sdfTemplate = "EEEE', 'd' 'LLLL', 'yyyy', at 'k':'mm";

				// For everything in between
			} else {
				if (eventDate.getHours() == 0 && eventDate.getMinutes() == 0)
					sdfTemplate = "EEEE', 'd' 'LLLL";
				else
					sdfTemplate = "EEEE', 'd' 'LLLL', at 'k':'mm";
			}

			SimpleDateFormat sdf = new SimpleDateFormat(sdfTemplate, Locale.getDefault());
			holder.time.setText(sdf.format(eventDate));

		} catch (ParseException e1) {
			e1.printStackTrace();
		}

		File eventPic = new File(context.getCacheDir(), events.get(position).getEid());
		if (eventPic.exists())
			try {
				holder.picture.setImageBitmap(Bitmap.createBitmap(BitmapFactory.decodeFile(eventPic.getCanonicalPath()), 0, 0, 50, 50));
			} catch (IOException e) {
				e.printStackTrace();
			}

		return view;
	}
}
