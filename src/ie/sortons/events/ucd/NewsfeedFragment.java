package ie.sortons.events.ucd;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.appspot.sortonsevents.clientdata.Clientdata;
import com.appspot.sortonsevents.clientdata.model.ClientPageData;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.gson.GsonFactory;

@SuppressLint("SetJavaScriptEnabled")
public class NewsfeedFragment extends Fragment {

	Context context;

	HashMap<String, String> fbPages = new HashMap<String, String>();
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		context = inflater.getContext();
		
		WebView webView = new WebView(inflater.getContext()); 
		webView.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)); 
		
		View view = webView;
		
		webView.setWebViewClient(new WebViewClient() {

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				Log.i("shouldOverrideUrlLoading", url);
				Intent intent;
				if(url.contains("facebook.com")){
					try {
						Log.i("shouldOverrideUrlLoading", "try Facebook");
						NewsfeedFragment.this.context.getPackageManager().getPackageInfo("com.facebook.katana", 0);

						// http://www.kanatorn.info/2012/01/03/open-facebook-official-app-with-intent-android/

						intent =  new Intent(Intent.ACTION_VIEW, Uri.parse(url));

						// regular link/YouTube
						// http://m.facebook.com/l.php?u=http%3A%2F%2Fwww.isiccard.ie%2F&h=aAQEJBph0&s=1
						if(url.contains("http://m.facebook.com/l.php?u=")){
							url = url.replace("http://m.facebook.com/l.php?u=", "");
							url = url.replace("&s=1","");
							url = URLDecoder.decode(url);
							intent =  new Intent(Intent.ACTION_VIEW, Uri.parse(url));

							// view comments:
							// http://m.facebook.com/ajax/ufi.php?count=1&prev=1&p=0&ft_ent_identifier=333821546758032&gfid=AQDYCMK6oOLd-Cxs
							// likes:
							// http://m.facebook.com/browse/likes/?id=333821546758032&locale2=en_US
							// story:
							// http://m.facebook.com/story.php?story_fbid=480447118736277&id=129777967136529&locale2=en_US
						} else if( url.contains("facebook.com/ajax/ufi.php?" ) || url.contains("facebook.com/browse/likes" ) || url.contains("story.php" ) ) {
							Log.i("facebook comment", "get id!");

							// TODO
							// Find a fb intent that opens directly to a facebook post

							// intent = new Intent( Intent.ACTION_VIEW, Uri.parse("fb://post/333821546758032") );
							intent =  new Intent(Intent.ACTION_VIEW, Uri.parse(url));

							// event:
							// http://m.facebook.com/events/619835538058641?locale2=en_US						
						} else if( url.contains("facebook.com/events" ) ) {
							Pattern p = Pattern.compile("\\d+");
							Matcher m = p.matcher(url);
							m.find();
							intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://event/" + m.group()));

							// photo
							// http://m.facebook.com/photo.php?fbid=639128962810167&id=112316168824785&set=a.131881163534952.22428.112316168824785&source=48&locale2=en_US
						} else if ( url.contains( "/photo.php?" ) ) {
							Pattern p = Pattern.compile("\\d+");
							Matcher m = p.matcher(url);
							m.find();
							intent = new Intent( Intent.ACTION_VIEW, Uri.parse( "fb://photo/639128962810167" ) );

							// page:
							// http://m.facebook.com/UCDInternational?locale2=en_US
							// profile
							// http://m.facebook.com/ConorJoeRyan?locale2=en_US
						} else if ( url.matches( ".*://.*facebook.com/.*/?.*" ) ){
							Log.i("facebook page/profile", "id?");

							// Two types of URL
							// www.facebook.com/pages/UCD-English-Graduate-Society/104683989604121
							// www.facebook.com/UCDGamesoc

							if ( url.matches( "facebook.com/pages" ) ) {
								Pattern p = Pattern.compile("(\\d+)(?!.*\\d)");
								Matcher m = p.matcher(url);
								if (m.find())
									intent = new Intent( Intent.ACTION_VIEW, Uri.parse("fb://page/" + m.group(1) ) );
							} else {
								Pattern p = Pattern.compile("com/(.*?)\\?.*");
								Matcher m = p.matcher(url);
								if (m.find())
								{
									if ( fbPages.containsKey( m.group(1) ) )
										intent = new Intent( Intent.ACTION_VIEW, Uri.parse("fb://page/" + fbPages.get(m.group(1)) ) );
								} else {
									// TODO 
									// facebook.com/PageName doesn't work => graph call for its id
									intent =  new Intent(Intent.ACTION_VIEW, Uri.parse(url));
								}
							}
						}

					} catch (Exception e) {
						Log.i("shouldOverrideUrlLoading", "fb fail");
						intent =  new Intent(Intent.ACTION_VIEW, Uri.parse(url));
					}
				} else {
					intent =  new Intent(Intent.ACTION_VIEW, Uri.parse(url));
				}
				startActivity(intent);
				return true;
			}

			@Override
			public WebResourceResponse shouldInterceptRequest(final WebView view, String url) {
				// https://labs.mwrinfosecurity.com/blog/2012/04/23/adventures-with-android-webviews/
				if(url.contains("facebook.com") && !url.contains("plugins") && !url.contains("facebook.com/connect") ){
					shouldOverrideUrlLoading(view, url);
					return null;
				} else {
					return super.shouldInterceptRequest(view, url);
				}
			}

		});

		webView.getSettings().setJavaScriptEnabled(true);

		webView.setBackgroundColor(Color.parseColor("#90FFFFFF"));

		webView.loadUrl("http://sortonsevents.appspot.com/recentposts/?client="+clientId);

		queryCloudEndpoint();

		return view;

	}

	private String clientId = "197528567092983";

	private void queryCloudEndpoint() {

		AsyncTask<String, Void, ClientPageData> task = new AsyncTask<String, Void, ClientPageData>() {

			@Override
			public void onPreExecute() {

			}

			@Override
			protected ClientPageData doInBackground(String... params) {
				Clientdata.Builder builder = new Clientdata.Builder(
						AndroidHttp.newCompatibleTransport(), new GsonFactory(), null /* httpRequestInitializer */);
				try {
					ClientPageData cpd = builder.build().clientPageDataEndpoint().getClientPageData(params[0]).execute();
					return cpd;

				} catch (IOException e) {
					Log.e("error:", e.toString());
					return null;
				}
			}

			@Override
			public void onPostExecute(ClientPageData data) {
				for(com.appspot.sortonsevents.clientdata.model.FbPage page : data.getIncludedPages())
					if(!page.getPageUrl().contains("pages/"))
						NewsfeedFragment.this.fbPages.put(page.getPageUrl().replace("http://www.facebook.com/", ""), page.getPageId());
			}
		};

		task.execute(clientId);
	}


}
