package ie.sortons.events.ucd;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.appspot.sortonsevents.upcomingEvents.model.DiscoveredEvent;
import com.appspot.sortonsevents.upcomingEvents.model.FbEvent;
import com.appspot.sortonsevents.upcomingEvents.model.FbPage;

public class DbTools extends SQLiteOpenHelper {

	public DbTools(Context app_context) {
		super(app_context, "modulelist.db", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String q = "CREATE TABLE events ( id INTEGER PRIMARY KEY AUTOINCREMENT," + "eventId INTEGER, name TEXT, location TEXT,"
				+ "startTime TEXT, endTime TEXT, latitude REAL," + "longitude REAL, picUrl TEXT )";
		db.execSQL(q);
		q = "CREATE TABLE sourcePages ( " + "eventId INTEGER, pageId INTEGER, name TEXT," + "pageUrl TEXT )";
		db.execSQL(q);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		String q = "DROP TABLE IF EXISTS events";
		db.execSQL(q);
		q = "DROP TABLE IF EXISTS sourcePages";
		db.execSQL(q);
		onCreate(db);
	}

	private void updateEvent(DiscoveredEvent event) {

		SQLiteDatabase db = this.getReadableDatabase();

		ContentValues values = new ContentValues();

		values.put("name", event.getFbEvent().getName());
		values.put("location", event.getFbEvent().getLocation());
		values.put("startTime", event.getFbEvent().getStartTime());
		values.put("endTime", event.getFbEvent().getEndTime());
		values.put("latitude", event.getFbEvent().getLatitude());
		values.put("longitude", event.getFbEvent().getLongitude());

		db.update("events", values, "eventId=" + event.getEid(), null);

		// TODO
		// Source pages

		// db.close();

	}

	// this function will update the database deleting anything not present in
	// this list
	public void updateEvents(List<DiscoveredEvent> upcomingEvents) {

		List<DiscoveredEvent> old = getEvents();

		List<DiscoveredEvent> toBeAdded = new ArrayList<DiscoveredEvent>();
		List<DiscoveredEvent> toBeDeleted = new ArrayList<DiscoveredEvent>();

		for (DiscoveredEvent de : upcomingEvents)
			if (old.contains(de))
				updateEvent(de);
			else
				toBeAdded.add(de);

		for (DiscoveredEvent de : old)
			if (!upcomingEvents.contains(de))
				toBeDeleted.add(de);

		deleteEvents(toBeDeleted);
		insertEvents(toBeAdded);
		
	}

	public void insertEvents(List<DiscoveredEvent> values) {
		SQLiteDatabase db = this.getWritableDatabase();
		for (DiscoveredEvent de : values) {
			ContentValues vals = new ContentValues();
			vals.put("eventId", de.getEid());
			vals.put("name", de.getFbEvent().getName());
			vals.put("location", de.getFbEvent().getLocation());
			vals.put("startTime", de.getFbEvent().getStartTime());
			vals.put("endTime", de.getFbEvent().getEndTime());
			vals.put("latitude", de.getFbEvent().getLatitude());
			vals.put("longitude", de.getFbEvent().getLongitude());

			db.insert("events", null, vals);
		}
		// db.close();
	}

	public void insertSourcePages(List<DiscoveredEvent> values) {
		SQLiteDatabase db = this.getWritableDatabase();

		for (DiscoveredEvent de : values) {
			String eventId = de.getEid();
			for (FbPage fbp : de.getSourcePages()) {

				ContentValues vals = new ContentValues();
				vals.put("eventId", eventId);
				vals.put("pageId", fbp.getPageId());
				vals.put("name", fbp.getName());
				vals.put("pageUrl", fbp.getPageUrl());

				db.insert("sourcePages", null, vals);
			}

		}

		// db.close();
	}


	private void deleteEvents(List<DiscoveredEvent> toBeDeleted) {
		SQLiteDatabase db = this.getWritableDatabase();
		for (DiscoveredEvent de : toBeDeleted) {
			String q = "DELETE FROM events WHERE eventId='" + de.getEid() + "'";
			db.execSQL(q);
			q = "DELETE FROM sourcePages WHERE eventId='" + de.getEid() + "'";
			db.execSQL(q);
		}
		// db.close();
	}


	public List<DiscoveredEvent> getEvents() {
		List<DiscoveredEvent> eventList = new ArrayList<DiscoveredEvent>();
		String q = "SELECT * FROM events";
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(q, null);

		if (cursor.moveToFirst()) {

			do {
				DiscoveredEvent e = new DiscoveredEvent();
				e.setEid(cursor.getString(1));
				FbEvent fbe = new FbEvent();
				fbe.setEid(cursor.getString(1));
				fbe.setName(cursor.getString(2));
				fbe.setLocation(cursor.getString(3));
				fbe.setStartTime(cursor.getString(4));
				fbe.setEndTime(cursor.getString(5));
				if (cursor.getString(6) != null)
					fbe.setLatitude(Double.parseDouble(cursor.getString(6)));
				if (cursor.getString(7) != null)
					fbe.setLongitude(Double.parseDouble(cursor.getString(7)));
				fbe.setPicSquare(cursor.getString(8));
				e.setFbEvent(fbe);
				// TODO
				// e.setSourcePages(getSourcePagesForEvent(e.getEid()));
				eventList.add(e);
			} while (cursor.moveToNext());
		}
		return eventList;
	}

	public DiscoveredEvent getEvent(String id) {
		DiscoveredEvent de = new DiscoveredEvent();
		SQLiteDatabase db = this.getReadableDatabase();
		String q = "SELECT * FROM events WHERE eventId='" + id + "'";
		Cursor cursor = db.rawQuery(q, null);
		if (cursor.moveToFirst()) {
			de.setEid(cursor.getString(1));
			FbEvent fbe = new FbEvent();
			fbe.setEid(cursor.getString(1));
			fbe.setName(cursor.getString(2));
			fbe.setLocation(cursor.getString(3));
			fbe.setStartTime(cursor.getString(4));
			fbe.setEndTime(cursor.getString(5));
			if (cursor.getString(6) != null)
				fbe.setLatitude(Double.parseDouble(cursor.getString(6)));
			if (cursor.getString(7) != null)
				fbe.setLongitude(Double.parseDouble(cursor.getString(7)));
			fbe.setPicSquare(cursor.getString(8));
			de.setFbEvent(fbe);
			// TODO
			// de.setSourcePages(getSourcePagesForEvent(id));
		}
		// db.close();
		return de;
	}

	public void savePicUrl(String eventId, String picUrl) {
		SQLiteDatabase db = this.getReadableDatabase();
		ContentValues values = new ContentValues();
		values.put("picUrl", picUrl);
		db.update("events", values, "eventId=" + eventId, null);
		// db.close();
	}
}
