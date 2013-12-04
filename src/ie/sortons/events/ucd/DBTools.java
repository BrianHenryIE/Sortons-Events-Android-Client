package ie.sortons.events.ucd;


import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBTools extends SQLiteOpenHelper {
	
	public DBTools(Context app_context) {
		super(app_context, "modulelist.db", null, 1);
		
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String q = "CREATE TABLE events ( id INTEGER PRIMARY KEY AUTOINCREMENT," + 
                   "eventId INTEGER, name TEXT, location TEXT," + 
				   "startTimeDate TEXT, startTime TEXT, latitude REAL," +
				   "longitude REAL, picUrl TEXT )";
		db.execSQL(q);
		q = "CREATE TABLE sourcePages ( " + 
				"eventId INTEGER, pageId INTEGER, name TEXT," + 
				"pageUrl TEXT )";
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
	public void updateEvent(HashMap<String,String> oldEvent, HashMap<String,String> newEvent) {
		String eventId = oldEvent.get("eventId");
		ContentValues values = new ContentValues();
		//grabs all the different values
		for (Map.Entry<String, String> entry : newEvent.entrySet()) {
		    String key = entry.getKey();
		    String value = entry.getValue();
		    if (value != oldEvent.get(key)) {
		    	values.put(key, value);
		    }
		}
		if (values.size() > 0) {
			String q = "SELECT * FROM events where eventId='" + eventId + "'";
			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(q, null);
			if (cursor.moveToFirst()) {
				int rowsUpdated = db.update("events", values, "eventId=" + eventId, null);
				if (rowsUpdated != 1) {
					Log.e("adding url", "Adding a url for event with id: " + eventId + "updated " + rowsUpdated + "rows");
				}
			}
			db.close();
		}
	}
	
	//this function will update the database deleting anything not present in this list
	public void updateEvents(List<HashMap<String, String>> values) {
		ArrayList<HashMap<String,String>> old = getEvents();
		ArrayList<HashMap<String,String>> toBeAdded = new ArrayList<HashMap<String,String>>();
		for (int i = 0; i < values.size(); i++) {
			boolean found = false;
			for (int j = 0; j < old.size(); j++) {
				if (values.get(i).get("eventId") == old.get(j).get("eventId")) {
					updateEvent(old.get(j), values.get(i));
					found = true;
					//do not need to keep checking this value
					old.remove(j);
				}
			}
			if (!found) {
				toBeAdded.add(values.get(i));
			}
		}
		List<String> toBeDeleted = new ArrayList<String>();
		for (int i = 0; i < old.size();i++) {
			toBeDeleted.add(old.get(i).get("eventId"));
		}
		deleteEvents(toBeDeleted);
		insertEvents(toBeAdded);
	}
	
	public void insertEvents(List<HashMap<String, String>> values) {
		SQLiteDatabase db = this.getWritableDatabase();
		for (int i = 0; i < values.size(); i++) {
			ContentValues vals = new ContentValues();
			vals.put("eventId", values.get(i).get("eventId"));
			vals.put("name", values.get(i).get("name"));
			vals.put("location", values.get(i).get("location"));
			vals.put("startTimeDate", values.get(i).get("startTimeDate"));
			vals.put("startTime", values.get(i).get("startTime"));
			vals.put("latitude", values.get(i).get("latitude"));
			vals.put("longitude", values.get(i).get("longitude"));
			if (values.get(i).containsKey("picUrl")) {
				vals.put("picUrl", values.get(i).get("picUrl"));
			}
			Log.i("insertEvents", "added event");
			db.insert("events", null, vals);
		}
		db.close();
	}
	
	public void insertSourcePages(List<HashMap<String, String>> values) {
		SQLiteDatabase db = this.getWritableDatabase();
		for (int i = 0; i < values.size(); i++) {
			ContentValues vals = new ContentValues();
			vals.put("eventId", values.get(i).get("eventId"));
			vals.put("pageId", values.get(i).get("pageId"));
			vals.put("name", values.get(i).get("name"));
			vals.put("pageUrl", values.get(i).get("pageUrl"));
			
			db.insert("sourcePages", null, vals);
		}
		db.close();
	}
	//should be called by deleteAllEventsBeforeDate
	public void deleteEvents(List<String> eventId) {
		SQLiteDatabase db = this.getWritableDatabase();
		for(int i = 0; i < eventId.size(); i++) {
			String q = "DELETE FROM events WHERE eventId='" + eventId.get(i) + "'";
			db.execSQL(q);
			q = "DELETE FROM sourcePages WHERE eventId='" + eventId.get(i) + "'";
			db.execSQL(q);
		}
		db.close();
	}
	//pass in Date and all events before date are deleted
	public void deleteAllEventsBeforeDate(Date before) {
		ArrayList<HashMap<String, String>> events = getEvents();
		ArrayList<String> toDelete = new ArrayList<String>();
		for (int i = 0; i < events.size(); i++) {
			try {
				Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.ENGLISH).parse(events.get(i).get("startTimeDate"));
				if (date.before(before)) {
					toDelete.add(events.get(i).get("eventId"));
				}
			} catch (ParseException e) {
				; //doNothing
			}
		}
		deleteEvents(toDelete);
	}
	
	public ArrayList<HashMap<String, String>> getEvents() {
		ArrayList<HashMap<String, String>> eventList = new ArrayList<HashMap<String, String>>();
		String q = "SELECT * FROM events";
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(q, null);
		
		if (cursor.moveToFirst()) {
			
			do{
				HashMap<String, String> e = new HashMap<String, String>();
				e.put("id", cursor.getString(0));
				e.put("eventId", cursor.getString(1));
				e.put("name", cursor.getString(2));
				e.put("location", cursor.getString(3));
				e.put("startTimeDate", cursor.getString(4));
				e.put("startTime", cursor.getString(5));
				e.put("latitude", cursor.getString(6));
				e.put("longitude", cursor.getString(7));
				e.put("picUrl", cursor.getString(8));
				
				
				eventList.add(e);
			} while(cursor.moveToNext());
		}
		return eventList;
	}
	
	public HashMap<String, String> getEventInfo(String id) {
		HashMap<String, String> event = new HashMap<String, String>();
		SQLiteDatabase db = this.getReadableDatabase();
		String q = "SELECT * FROM events WHERE eventId='" + id + "'";
		Cursor cursor = db.rawQuery(q, null);
		if (cursor.moveToFirst()) {
			event.put("id", cursor.getString(0));
			event.put("eventId", cursor.getString(1));
			event.put("name", cursor.getString(2));
			event.put("location", cursor.getString(3));
			event.put("startTimeDate", cursor.getString(4));
			event.put("startTime", cursor.getString(5));
			event.put("latitude", cursor.getString(6));
			event.put("longitude", cursor.getString(7));
			event.put("picURL", cursor.getString(8));
		}
		db.close();
		return event;
	}
	
	public List<HashMap<String,String>> getSourcePagesForEvent(String eventId) {
		ArrayList<HashMap<String, String>> pageList = new ArrayList<HashMap<String, String>>();
		SQLiteDatabase db = this.getReadableDatabase();
		String q = "SELECT * FROM sourcePages WHERE eventId='" + eventId + "'";
		Cursor cursor = db.rawQuery(q, null);
		if (cursor.moveToFirst()) {
			HashMap<String, String> page  = new HashMap<String,String>();
			do {
				page.put("id", cursor.getString(0));
				page.put("eventId", cursor.getString(1));
				page.put("name", cursor.getString(2));
				page.put("location", cursor.getString(3));
				page.put("startTimeDate", cursor.getString(4));
				page.put("startTime", cursor.getString(5));
				page.put("latitude", cursor.getString(6));
				page.put("longitude", cursor.getString(7));
				pageList.add(page);
			}	while (cursor.moveToNext());
		}
		db.close();
		return pageList;
	}
}
