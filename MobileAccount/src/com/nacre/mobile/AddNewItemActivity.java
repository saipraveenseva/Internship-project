package com.nacre.mobile;

import java.util.Calendar;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/*
 * This class is used for Adding a new ExpenseAmount of a particular expense category
 */
public class AddNewItemActivity extends Activity {
	private EditText cost; //for enter amount
	private TextView costView; // To get the cost of 
	private Bundle bundle;  //to get the information send by another activity 
	private String etype;  //to get expense type send by another activity
	private SQLiteDatabase db;  //for opening the database
	private Double amt;  //for holding the amount entered by the user

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.additem);
		DbHelper helper = new DbHelper(this);
		db = helper.getWritableDatabase();
		cost = (EditText) findViewById(R.id.amt);
		costView = (TextView) findViewById(R.id.addAmtView);
		bundle = getIntent().getExtras();
		etype = bundle.getString("selected");
		costView.append(" : " + etype);
	}
   //this method is called when the user clicks on save button
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		//if(requestCode==10)
	}
	
	public void saveItem(View view) {

		int success = 0;
		ContentValues values = new ContentValues();

		values.put("expname", etype);
		String expense = cost.getText().toString();
		if (expense.equals("")) {
			Toast.makeText(this, "Must Enter Amount", Toast.LENGTH_LONG).show();
		} else {
			try {
				amt = Double.parseDouble(expense.trim());
			} catch (NumberFormatException e) {
				success = 1;
			}
			if (amt < 0) {
				Toast.makeText(this, "Expenditure Cannot be Negative",
						Toast.LENGTH_LONG).show();
			}

		}
		if (success == 0) {
			int i = -1;
			values.put("cost", amt);
			final Calendar c = Calendar.getInstance();
			int yy = c.get(Calendar.YEAR);
			int mm = c.get(Calendar.MONTH) + 1;
			int dd = c.get(Calendar.DAY_OF_MONTH);
			values.put("date", dd + "-" + mm + "-" + yy);
			try {
				i = (int) db.insert("store", null, values);
			} catch (Exception e) {
				Log.v("AddNewItemActivity", "database Open Error");
			} finally {
				db.close();
			}
			if (i > 0) {
				Toast.makeText(this, "Saved", Toast.LENGTH_LONG).show();
			}
			Intent intent = new Intent();
			intent.putExtra("SELECTED", etype);
			setResult(10,intent);
			finish();
		} else {
			Toast.makeText(this, "Enter Digits only!!", Toast.LENGTH_LONG)
					.show();
		}

	}

}
