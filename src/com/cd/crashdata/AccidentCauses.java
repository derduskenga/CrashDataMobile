package com.cd.crashdata;

import android.os.Bundle;
import android.app.ListActivity;
import android.content.Intent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class AccidentCauses extends ListActivity { 

    static final String[] CAUSE_AND_CODES = new String[] {

        "1 Drivers and M/Cycles","2 Pedal cyclists", "3 Pedestrians", "4 Passengers", "5 Obstruction",
            "6 Vehicle defects", "7 Road defects", "8 Weather", "9 Other causes"
    };

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setListAdapter(new ArrayAdapter < String > (this,R.layout.activity_vehicle_type_list,R.id.txtv_vehicle_type_list,CAUSE_AND_CODES));
        
        getListView().setTextFilterEnabled(true);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        // TODO Auto-generated method stub
        super.onListItemClick(l, v, position, id);
        
        
        //Toast.makeText(VehicleTypeList.this, getListView().getItemAtPosition(position).toString(), Toast.LENGTH_LONG).show();
        Intent i = new Intent(AccidentCauses.this, PoliceRemarks.class);
        i.putExtra("cause_code", getListView().getItemAtPosition(position).toString());
        startActivity(i);

    }

}
