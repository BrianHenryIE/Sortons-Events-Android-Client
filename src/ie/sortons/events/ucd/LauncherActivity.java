package ie.sortons.events.ucd;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;

public class LauncherActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_launcher);
		
		// This is for locking tablets in landscape and phones in portrait
		Intent i;
		if ( isXLargeScreen(this) )
			i = new Intent(this, MainActivity.class);
		else
			i = new Intent(this, PortraitActivity.class);
		startActivity( i );
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.launcher, menu);
		return true;
	}

	public boolean isXLargeScreen(Context context) {
		return (context.getResources().getConfiguration().screenLayout
				& Configuration.SCREENLAYOUT_SIZE_MASK)
				>= Configuration.SCREENLAYOUT_SIZE_XLARGE;
	}

}
