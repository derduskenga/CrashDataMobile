package com.cd.crashdata;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class Emergency extends Activity {
	
	ArrayList<Contacts> contactsList;
	ProgressDialog pDialog;
	ListView lv;
	
	private String URL_CONTACTS = "http://10.0.2.2/crashdata/get_contacts.php";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_emergency);
		
		contactsList = new ArrayList<Contacts>();
				
		lv = (ListView) findViewById(R.id.emergency_list);
		new GetContacts().execute();
        
	}
	
	private void populateListView() {
		
		List<String> lables = new ArrayList<String>();
		//List<String> lables_num = new ArrayList<String>();
		
		for (int i = 0; i < contactsList.size(); i++) {
			lables.add(contactsList.get(i).getRespondent());
			//lables_num.add(contactsList.get(i).getNumber());
			
		/*	HashMap<String, String> map = new HashMap<String, String>();
						
			map.put("name", contactsList.get(i).getRespondent());
			
			// Place name
			map.put("number", contactsList.get(i).getNumber());
			
			ListAdapter adapter = new SimpleAdapter(
	                Emergency.this,  (List<? extends Map<String, ?>>) contactsList, R.layout.contacts_listview,
	                new String[] { "name", "number" },
	                new int[] { R.id.txt_contact_name, R.id.txt_contact_number });
	                				        
	        lv.setAdapter(adapter);
	        */
		}	
		
		// Binding resources Array to ListAdapter
        lv.setAdapter(new ArrayAdapter<String>(this, R.layout.contacts_listview, R.id.txt_contact_name, lables));        
        
 
        // listening to single list item on click
        lv.setOnItemClickListener(new OnItemClickListener() {
          @Override
		public void onItemClick(AdapterView<?> parent, View view,
              int position, long id) {
               
             
          }
        });
	}
	
	/**
	 * Async task to get all contacts
	 * */
	private class GetContacts extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(Emergency.this);
			pDialog.setMessage("Fetching Contacts..");
			pDialog.setCancelable(false);
			pDialog.show();

		}

		@Override
		protected Void doInBackground(Void... arg0) {
			ServiceHandler jsonParser = new ServiceHandler();
			String json = jsonParser.makeServiceCall(URL_CONTACTS, ServiceHandler.GET);

			Log.e("Response: ", "> " + json);

			if (json != null) {
				try {
					JSONObject jsonObj = new JSONObject(json);
					if (jsonObj != null) {
						JSONArray cont = jsonObj
								.getJSONArray("contacts");						

						for (int i = 0; i < cont.length(); i++) {
							JSONObject catObj = (JSONObject) cont.get(i);
							Contacts cat = new Contacts (catObj.getInt("emergency_id"),
									catObj.getString("respondent"),catObj.getString("phone_number"));
							contactsList.add(cat);
						}
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}

			} else {
				Log.e("JSON Data", "Didn't receive any data from server!");
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if (pDialog.isShowing())
				pDialog.dismiss();
			populateListView();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_emergency, menu);
		return true;
	}

}
