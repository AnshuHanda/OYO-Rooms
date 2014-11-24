package com.example.oyorooms;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;

public class CustomList extends ArrayAdapter<String>{
	private final Activity context;
	
	private final ArrayList<String> id;
	private final ArrayList<String> name;
	private final ArrayList<String> status;

	public CustomList(Activity context,	ArrayList<String> name, ArrayList<String> id, ArrayList<String> status) {
	
		super(context, R.layout.single_list_row, name);
		this.context = context;
		this.id = id;
		this.name= name;
		this.status = status;
	}

	@Override
	public View getView(final int position, View view, ViewGroup parent) {
		LayoutInflater inflater = context.getLayoutInflater();
		View rowView= inflater.inflate(R.layout.single_list_row, null, true);
		TextView txtTitle = (TextView) rowView.findViewById(R.id.txt);
		Button imageView = (Button) rowView.findViewById(R.id.img);
		txtTitle.setText(name.get(position));
		
		imageView.setText(status.get(position));
		//imageView.setImageResource(imageId[position]);
		
		imageView.setOnClickListener(new OnClickListener() {
			Button button = null;
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				button = (Button)v;
				if(button.getText().toString().equalsIgnoreCase("BLOCKED"))
				{
					Toast.makeText(context,"Room is not available for booking.",Toast.LENGTH_LONG).show();

				}else if(button.getText().toString().equalsIgnoreCase("Checkedin")){
					
					Toast.makeText(context,"Room in not avalable for booking",Toast.LENGTH_LONG).show();

				}else if(button.getText().toString().equalsIgnoreCase("checked out")){

					Toast.makeText(context,"Room is available",Toast.LENGTH_LONG).show();
					
				}else if(button.getText().toString().equalsIgnoreCase("available")){
         	
		         	AlertDialog.Builder builder = new AlertDialog.Builder(context);
		             
		             builder.setTitle("Select Option..");
		             builder.setIcon(R.drawable.tick);
		             // Setting Positive "Yes" Button
		             builder.setPositiveButton("Confirm Booking", 
		             	      new DialogInterface.OnClickListener() {
		             			
		             	         @Override
		             	         public void onClick(DialogInterface arg0, int arg1) {
		             	        	
		             	            Intent i = new Intent(context,BookingForm.class);
		             	            i.putExtra("roomName", name.get(position));
		             	            i.putExtra("roomId", id.get(position));
		             	            i.putExtra("roomStatus", status.get(position));
		             	            button.getText();
		             	            context.startActivity(i);             				
		             	         }
		             	      });
		      
		             // Setting Negative "NO" Button
		             builder.setNegativeButton("Checkin", new DialogInterface.OnClickListener() {
		                 public void onClick(DialogInterface dialog, int which) {
		                 //implement logic for sendind checkin status to the server.
		     				
		                 }
		             });
		             // Showing Alert Message
		             builder.create().show();
				}else if(button.getText().toString().equalsIgnoreCase("Confirm booking")){
					AlertDialog.Builder builder = new AlertDialog.Builder(context);
					builder.setTitle("Select Option..");
		             builder.setIcon(R.drawable.tick);
				    builder.setItems(new CharSequence[]{"NO SHOW", "CHECKEDIN", "CANCEL"},
				            new DialogInterface.OnClickListener() {
				                public void onClick(DialogInterface dialog, int which) {
				                    // The 'which' argument contains the index position
				                    // of the selected item
				                    switch (which) {
				                        case 0:
				                        	//what to do-->dont know
				                            break;
				                        case 1:
				                            //implement--> changing status to checkedin
				                            break;
				                        case 2:dialog.cancel();
				                            break;
				                    }
				                }
				            });
				    builder.create().show();
				}
			}
		});
		return rowView;
	}
}
