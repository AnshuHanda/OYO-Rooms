package com.example.oyorooms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask; 
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView; 
import android.widget.Toast; 
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends ListActivity {
	private Context context;
	private static String url = "http://api.oyorooms.com/v1/hotels/list";
	private static final String HOTEL_ID = "id";
	private static final String HOTEL_NAME = "name";
	private Map<String, String> hotelMap = new HashMap<String, String>();
	//HashMap<String, String> map = new HashMap<String, String>();
	ArrayList<HashMap<String, String>> hotel_name_list = new ArrayList<HashMap<String, String>>();
	ListView lv ;
 @Override 
 protected void onCreate(Bundle savedInstanceState) {
	 super.onCreate(savedInstanceState);
	 setContentView(R.layout.activity_main);

	 //if (ConnectionDetection.getInstance(this).isOnline(this)) {
	 if(haveNetworkConnection(this)){
		 callAsynchronousTask();

		 getListView().setOnItemClickListener(new OnItemClickListener() {
	         public void onItemClick(AdapterView parent, final View view, int position, long id) {
	         	
	         	AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
	         	String hotelname = ((TextView)view.findViewById(R.id.hotel_name)).getText().toString();
	             // Setting Dialog Title
	             builder.setTitle(hotelname);
	      
	             // Setting Dialog Message
	             builder.setMessage("Confirm with your Hotel?");
	      
	             // Setting Icon to Dialog
	             builder.setIcon(R.drawable.tick);
	      
	             // Setting Positive "Yes" Button
	             builder.setPositiveButton("Yes", 
	             	      new DialogInterface.OnClickListener() {
	             			
	             	         @Override
	             	         public void onClick(DialogInterface arg0, int arg1) {
	             	        	//String hotel_id = ((TextView)view.findViewById(R.id.hotel_id)).getText().toString();
	             	        	String hotel_name = ((TextView)view.findViewById(R.id.hotel_name)).getText().toString();
	             	        	String hotel_id = hotelMap.get(hotel_name);
	             	        	Log.e("str",hotel_id+":"+hotel_name);
	             	        	//String id = map.get(str);
	             	        	
	             	        	SharedPreferences settings = getSharedPreferences("MYPREFS", 0);
								SharedPreferences.Editor editor = settings.edit();
	                            editor.putString("id", hotel_id);
	                            editor.putString("name", hotel_name);
	                            editor.commit();
	                            editor.commit();
	             	            Intent i = new Intent(getApplicationContext(),com.example.oyorooms.Hotel_List.class);
	    
	             	            startActivity(i);             				
	             	         }
	             	      });
	      
	             // Setting Negative "NO" Button
	             builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
	                 public void onClick(DialogInterface dialog, int which) {
	                 // Write your code here to invoke NO event
	                 //	Intent i = new Intent(getApplicationContext(),com.example.oyorooms.MainActivity.class);
	     	          //  startActivity(i);
	     				
	                 }
	             });
	      
	             // Showing Alert Message
	             
	             builder.create().show();

	}
	}); 
	 
		 new ProgressTask(MainActivity.this).execute();
     }

     else
     {
     //    Toast.makeText(getBaseContext(),
     //            "No Internet connection available", 4000).show();
			Toast.makeText(this,"Check Internet Settings",Toast.LENGTH_LONG).show();

     }
 } 
	 public boolean haveNetworkConnection(Context context)
		{
		    boolean haveConnectedWifi = false;
		    boolean haveConnectedMobile = false;

		    ConnectivityManager cm = (ConnectivityManager) MainActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
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
 
 
 private class ProgressTask extends AsyncTask<String, Void, Boolean> { 
	 private ProgressDialog dialog;
	 private ListActivity activity; 
	 // private List<Message> messages; 
	 public ProgressTask(ListActivity activity) { 
		 this.activity = activity;
		 context = activity;
		 dialog = new ProgressDialog(context);
		 } 
	 private Context context;
	 protected void onPreExecute() { 
		 this.dialog.setMessage("Getting Data...");
		 this.dialog.show();
		 } 
	 @Override protected void onPostExecute(final Boolean success) {
		 if (dialog.isShowing()) { 
			 dialog.dismiss(); 
			 }
	//	 ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
     //            android.R.layout.simple_list_item_1, items.toArray(new String[items.size()]));
	//	 lv.setAdapter(adapter);
		 ListAdapter adapter = new SimpleAdapter(context, hotel_name_list,
				 R.layout.list_item, new String[] {HOTEL_NAME}, 
				 new int[] {R.id.hotel_name });
		 setListAdapter(adapter); // select single ListView item
		 lv = getListView();

		
		 } 
	 protected Boolean doInBackground(final String... args) { 
		 JSONParser jParser = new JSONParser();
		 // get JSON data from URL
		 JSONArray json = jParser.getJSONFromUrl(url);
		 for (int i = 0; i < json.length(); i++) {
			 try { 
				 JSONObject c = json.getJSONObject(i);
				 String id=c.getString(HOTEL_ID);
				 String name = c.getString(HOTEL_NAME);
				 
				 Log.e("aaaaa", "aabbccdd");
				 HashMap<String, String> map = new HashMap<String, String>();
				 // Add child node to HashMap key & value
				 map.put(HOTEL_ID, id);
				 map.put(HOTEL_NAME,name);
				 hotelMap.put(name, id);
				// map.put(HOTEL_ID, id); 
				 //map.put(HOTEL_NAME, name); 
				 
				 hotel_name_list.add(map); 
				 } 
			 catch (JSONException e)
			 { 
				 e.printStackTrace(); 
				 } 
			 } return null; 
			 } 
 }
 
 public void callAsynchronousTask() {
	    final Handler handler = new Handler();
	    Timer timer = new Timer();
	    TimerTask doAsynchronousTask = new TimerTask() {
	        @Override
	        public void run() {
	            handler.post(new Runnable() {
	                public void run() {
	                    try {
	                    	ProgressTask pt = new ProgressTask(MainActivity.this);
	                        pt.execute();
	                        //Log.e("progress done", "yeah");
	                    } catch (Exception e) {
	                        e.printStackTrace();
	                       // Log.e("exceptional progress", "Anshu");
	                    }
	                }
	            });
	        }
	    };
	    timer.schedule(doAsynchronousTask, 0, 1800000); //execute in every 10 sec
	} 
}
