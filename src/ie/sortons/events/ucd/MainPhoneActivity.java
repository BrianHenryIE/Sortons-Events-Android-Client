package ie.sortons.events.ucd;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

public class MainPhoneActivity extends FragmentActivity {
	
	DbTools dbTools = new DbTools(this);
	
	NewsfeedFragment newsfeedFragment;
	MapFragment mapFragment;
	EventslistFragment eventslistFragment;
	
	/**
	 * The number of pages can swipe to
	 */
	private static final int NUM_PAGES = 3;

	/**
	 * The pager widget, which handles animation and allows swiping horizontally to access previous and next wizard
	 * steps.
	 */
	private ViewPager mPager;

	/**
	 * The pager adapter, which provides the pages to the view pager widget.
	 */
	private PagerAdapter mPagerAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_phone);

		Data.events = dbTools.getEvents();

		// Instantiate a ViewPager and a PagerAdapter.
		mPager = (ViewPager) findViewById(R.id.pager);
		mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
		mPager.setAdapter(mPagerAdapter);
		mPager.setOffscreenPageLimit(2);

		// initialize fragments
		newsfeedFragment = new NewsfeedFragment();

		mapFragment = new MapFragment();
		mapFragment.showEvents();
		eventslistFragment = new EventslistFragment();
		eventslistFragment.showEvents();
	}

	@Override
	public void onBackPressed() {
		if (mPager.getCurrentItem() == 0) {
			// If the user is currently looking at the first step, allow the system to handle the
			// Back button. This calls finish() on this activity and pops the back stack.
			super.onBackPressed();
		} else {
			// Otherwise, select the previous step.
			mPager.setCurrentItem(mPager.getCurrentItem() - 1);
		}
	}

	/**
	 * A simple pager adapter that represents 3 ScreenSlidePageFragment objects, in sequence.
	 */
	private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
		public ScreenSlidePagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			if (position == 0)
				return eventslistFragment;
			else if (position == 1)
				return mapFragment;
			else
				return newsfeedFragment;

		}

		@Override
		public int getCount() {
			return NUM_PAGES;
		}
	}

}
