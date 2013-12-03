package ie.sortons.events.ucd;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

@SuppressLint("SetJavaScriptEnabled")
public class NewsfeedFragment extends Fragment {

	Context context;

	public void setArguments(Context context){
		this.context = context;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.i("newsfeed fragment", "onCreateView");
		
		View view = inflater.inflate(R.layout.fragment_newsfeed, container, false);
		
		WebView myWebView = (WebView) view.findViewById(R.id.webview);
		
		myWebView.getSettings().setJavaScriptEnabled(true);
		
		myWebView.setBackgroundColor(Color.parseColor("#90FFFFFF"));
		

		myWebView.loadUrl("http://sortonsevents.appspot.com/recentposts/?client=367864846557326");
				
		return view;
		
	}
	

}
