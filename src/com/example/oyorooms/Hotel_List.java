package com.example.oyorooms;

import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class Hotel_List extends Activity {
	private Context context;
	private static String url = null;
	private static final String ROOM_ID = "id";
	private static final String ROOM_NAME = "name";
	private static final String ROOM_STATUS = "status";
	ArrayList<HashMap<String, String>> hotel_room_list = new ArrayList<HashMap<String, String>>();
	ListView lv;
	TextView tv;
	ArrayList<String> roomId = new ArrayList<String>();
	ArrayList<String> roomStatus = new ArrayList<String>();
	ArrayList<String> roomName = new ArrayList<String>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_item);
		TextView hotelName = (TextView)findViewById(R.id.hotel_name);
		hotelName.setText(getSharedPreferences("MYPREFS", 0).getString("name", ""));
		
	}
	@Override
	public void onResume(){
	    super.onResume();
	    // put your code here...
	
	    SharedPreferences settings = getSharedPreferences("MYPREFS", 0);
		String id = settings.getString("id", "");
		String name=settings.getString("name","");
		
		Log.e("str",name);
		
		//tv=(TextView)findViewById(R.id.tv);
		//	tv.setText(name);
		lv = (ListView)findViewById(R.id.list);
		
		url = "http://api.oyorooms.com/v1/hotels/"+ id + "/rooms/status";
		
		if (haveNetworkConnection(this)) {
/*
			getListView().setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView parent, View view,
						int position, long id) {

					Intent bookingactivity = new Intent(
							getApplicationContext(),
							com.example.oyorooms.BookingActivity.class);
					startActivity(bookingactivity);

				}
			});
	*/		new ProgressTask(Hotel_List.this).execute();
		}

		else {
//			Toast.makeText(getBaseContext(),
//					"No Internet connection available", 4000).show();
			Toast.makeText(this,"Check Internet Settings",Toast.LENGTH_LONG).show();

		}
	}
	

	private class ProgressTask extends AsyncTask<String, Void, Boolean> {
		private ProgressDialog dialog;
		//private ListActivity activity;
		private Activity activity;
		// private List<Message> messages;
		//public ProgressTask(ListActivity activity) {
		public ProgressTask(Activity activity) {
			this.activity = activity;
			context = activity;
			dialog = new ProgressDialog(context);
		}

		private Context context;

		protected void onPreExecute() {
			this.dialog.setMessage("Getting Data...");
			this.dialog.show();
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			if (dialog.isShowing()) {
				dialog.dismiss();
			}
			
			//ListAdapter adapter = new SimpleAdapter(context, hotel_room_list,
			//		R.layout.list_item, new String[] {  ROOM_NAME , ROOM_STATUS
			//				 }, new int[] {  R.id.hotel_name, R.id.status
			//				 });
			//setListAdapter(adapter);
							 
			// select single ListView item
			lv.setAdapter(new CustomList(Hotel_List.this, roomName,roomId, roomStatus));
			//lv = getListView();
		}

		protected Boolean doInBackground(final String... args) {
			JSONParser jParser = new JSONParser();
			// get JSON data from URL
			JSONArray json = jParser.getJSONFromUrl(url);
			for (int i = 0; i < json.length(); i++) {
				try {
					JSONObject c = json.getJSONObject(i);
					String id = c.getString(ROOM_ID);
					String name = c.getString(ROOM_NAME);
					String status = c.getString(ROOM_STATUS);
					//HashMap<String, String> map = new HashMap<String, String>();
					// Add child node to HashMap key & value
					//map.put(ROOM_ID, id);
					//map.put(ROOM_NAME, name);
					//map.put(ROOM_STATUS, status);
					
					roomId.add(id);
					roomName.add(name);
					roomStatus.add(status);
					//hotel_room_list.add(map);
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			return null;
		}
	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(Intent.ACTION_MAIN);
	    intent.addCategory(Intent.CATEGORY_HOME);
	    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    startActivity(intent);
	}
	
	
	 public boolean haveNetworkConnection(Context context)
		{
		    boolean haveConnectedWifi = false;
		    boolean haveConnectedMobile = false;

		    ConnectivityManager cm = (ConnectivityManager) Hotel_List.this.getSystemService(Context.CONNECTIVITY_SERVICE);
		    // or if function is out side of your Activity then you need context of your Activity
		    // and code will be as following
		    // (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

		    NetworkInfo[] netInfo = cm.getAllNetworkInfo();
		    for (NetworkInfo ni : netInfo)
		    {
		        if (ni.getTypeName().equalsIgnoreCase("WIFI"))
		        {
		            if (ni.isConnected())
		            {
		                haveConnectedWifi = true;
		                System.out.println("WIFI CONNECTION AVAILABLE");
		            } else
		            {
		                System.out.println("WIFI CONNECTION NOT AVAILABLE");
		            }
		        }
		        if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
		        {
		            if (ni.isConnected())
		            {
		                haveConnectedMobile = true;
		                System.out.println("MOBILE INTERNET CONNECTION AVAILABLE");
		            } else
		            {
		                System.out.println("MOBILE INTERNET CONNECTION NOT AVAILABLE");
		            }
		        }
		    }
		    return haveConnectedWifi || haveConnectedMobile;
		}
}
