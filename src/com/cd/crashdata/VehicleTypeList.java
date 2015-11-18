package com.cd.crashdata;

import android.os.Bundle;
import android.app.ListActivity;
import android.content.Intent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class VehicleTypeList extends ListActivity { 

    static final String[] VEHICLE_TYPE_LIST = new String[] {

        "Matatus","Cars ans utilities", "Lorries", "Trailers", "Petroleum tankers",
            "Other tankers", "Tractors", "Urban buses", "Country buses",
            "School/College buses", "Other Institutional buses", "Tourist vans", "Taxis",
            "Motor cycles", "Motor tricycles/Tuktuks", "Invalid carriages", "Hand carts","Animals drawn carts", "Pedal cycles", 
            "Animals", "Not known"
    };

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setListAdapter(new ArrayAdapter < String > (this,R.layout.activity_vehicle_type_list,R.id.txtv_vehicle_type_list,VEHICLE_TYPE_LIST));
        
        getListView().setTextFilterEnabled(true);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        // TODO Auto-generated method stub
        super.onListItemClick(l, v, position, id);
        
        
        //Toast.makeText(VehicleTypeList.this, getListView().getItemAtPosition(position).toString(), Toast.LENGTH_LONG).show();
        Intent i = new Intent(VehicleTypeList.this, VehicleDriverOne.class);
        i.putExtra("vehicle_type", getListView().getItemAtPosition(position).toString());
        startActivity(i);

    }

}