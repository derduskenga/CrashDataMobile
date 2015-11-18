package com.cd.crashdata;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;

public class VehicleDriverTwo extends Activity {
	
	String[] insurance;
	Spinner spin_insurance;
	EditText txt_registered_to,txt_vehicle_model,txt_insurance_policy_no,txt_other_insurance;
	RadioButton rb;
	RadioGroup rg;
	ImageButton img_next;
	VehicleDriverTwo _this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		_this=this;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vehicle_driver_two);
		
		//initialise edit texts and radioggroup
		rg= (RadioGroup)findViewById(R.id.rg_insurance);
		txt_insurance_policy_no = (EditText)findViewById(R.id.txt_policy_no);
		txt_other_insurance  = (EditText)findViewById(R.id.txt_other);
		txt_registered_to = (EditText)findViewById(R.id.txt_reg_name);
		txt_vehicle_model = (EditText)findViewById(R.id.txt_model);
		img_next = (ImageButton)findViewById(R.id.img_next);
		
		//Spinner Insurance
		insurance = getResources().getStringArray(R.array.insurance_array);
				
		 spin_insurance = (Spinner) findViewById(R.id.spin_insurance);
		
		ArrayAdapter<String> adapter_vehicle = new ArrayAdapter<String>(this,
		android.R.layout.simple_spinner_item, insurance);
		
		spin_insurance.setAdapter(adapter_vehicle);
		
		spin_insurance.setOnItemSelectedListener(new OnItemSelectedListener()
		{
		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1,
		int arg2, long arg3)
			{
				int index = arg0.getSelectedItemPosition();
			
			}
		
		@Override
		public void onNothingSelected(AdapterView<?> arg0) {}
		});
		
		Intent ir_from_db = getIntent();
		txt_vehicle_model.setText(ir_from_db.getStringExtra("DB_vehicle_model"));
		txt_registered_to.setText(ir_from_db.getStringExtra("DB_registered_to"));
		addListenerToImgButton();
	}
	
	public void addListenerToImgButton(){
		img_next.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String insurance_expired = getInsuranceExpiredOption();
				String insurance_name = spin_insurance.getSelectedItem().toString();
				String registered_to = txt_registered_to.getText().toString().trim();
				String vehicle_model = txt_vehicle_model.getText().toString().trim();
				String vehicle_insurance_policy_no = txt_insurance_policy_no.getText().toString().trim();
				String other_insurance = txt_other_insurance.getText().toString().trim();
				
				Intent ir = getIntent();
				
				if(registered_to.equals("")){
					Toast.makeText(_this, "Vehicle ownership cannot be empty", Toast.LENGTH_LONG).show();
				}else if(vehicle_model.equals("")){
					Toast.makeText(_this, "Vehicle make/model cannot be empty", Toast.LENGTH_LONG).show();
				}else{
					
					Intent i = new Intent(_this,VehicleDriverThree.class);
					//put extras to carry data
					i.putExtra("insurance_expired", insurance_expired);
					i.putExtra("insurance_name", insurance_name);
					i.putExtra("registered_to", registered_to);
					i.putExtra("vehicle_model", vehicle_model);
					i.putExtra("vehicle_insurance_policy_no", vehicle_insurance_policy_no);
					i.putExtra("other_insurance", other_insurance);
					i.putExtra("vehicle_reg_number", ir.getStringExtra("vehicle_reg_number"));
					i.putExtra("vehicle_type", ir.getStringExtra("vehicle_type"));
					i.putExtra("loading", ir.getStringExtra("loading"));
					i.putExtra("vehicle_defect", ir.getStringExtra("vehicle_defect"));
					
					_this.startActivity(i);
					
				}
				
			}
		});
	}
	
	public String getInsuranceExpiredOption(){
		
		int selectedRB = rg.getCheckedRadioButtonId();
		
		rb = (RadioButton)findViewById(selectedRB);
		
		String yes_no = rb.getText().toString();
		
		return yes_no;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_vehicle_driver_two, menu);
		return true;
	}

}
