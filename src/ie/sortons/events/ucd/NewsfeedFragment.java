package ie.sortons.events.ucd;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class NewsfeedFragment extends Fragment {

	Context context; 
	public void setArguments(Context context){
		this.context = context;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_newsfeed, container, false);
		Log.i("newsfeed fragment", "onCreateView");
		
		WebView myWebView = (WebView) view.findViewById(R.id.webview);
		WebSettings webSettings = myWebView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		myWebView.loadUrl("http://sortonsevents.appspot.com/recentposts/?client=367864846557326");
		
		
		return view;

	}
	

}
