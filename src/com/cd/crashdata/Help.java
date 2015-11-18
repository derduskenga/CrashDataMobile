package com.cd.crashdata;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class Help extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help);
		
		// storing string resources into Array
        String[] help = {"help","feedback","about"};
        
        ListView lv = (ListView) findViewById(R.id.listview);
         
        // Binding resources Array to ListAdapter
        lv.setAdapter(new ArrayAdapter<String>(this, R.layout.help_listview, R.id.txt_help, help));        
        
 
        // listening to single list item on click
        lv.setOnItemClickListener(new OnItemClickListener() {
          @Override
		public void onItemClick(AdapterView<?> parent, View view,
              int position, long id) {
               
              // selected item
              String product = ((TextView) view).getText().toString();
               
         /*     // Launching new Activity on selecting single List Item
              Intent i = new Intent(getApplicationContext(), SingleListItem.class);
              // sending data to new activity
              i.putExtra("product", product);
              startActivity(i);
              */
          }
        });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_help, menu);
		return true;
	}

}
