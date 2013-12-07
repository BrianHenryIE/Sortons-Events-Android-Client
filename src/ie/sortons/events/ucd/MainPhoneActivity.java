package ie.sortons.events.ucd;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;

import com.appspot.sortonsevents.upcomingEvents.model.DiscoveredEvent;

public class MainPhoneActivity extends FragmentActivity {
	NewsfeedFragment newsfeedFragment;
	MapFragment mapFragment;
	EventslistFragment eventslistFragment;
	DbTools dbTools = new DbTools(this);
	/**
     * The number of pages can swipe to
     */
    private static final int NUM_PAGES = 3;

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
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
		
		// Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.setOffscreenPageLimit(2);
        
        //initialize fragments
        newsfeedFragment =  new NewsfeedFragment();
        List<DiscoveredEvent>  events = dbTools.getEvents();
        mapFragment = new MapFragment();
		mapFragment.setList(events);
		eventslistFragment = new EventslistFragment();
		eventslistFragment.setList(events);
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_phone, menu);
		return true;
	}
	
	/**
     * A simple pager adapter that represents 3 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
            	return eventslistFragment;
            }
            else if (position == 1) {

				return mapFragment;
            }
            else {

            	return newsfeedFragment;
            }
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }

}
