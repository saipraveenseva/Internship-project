package com.nacre.mobile;

import java.util.StringTokenizer;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
/*
 * This activity is used for updating the amount for a particular category type
 */

public class UpdateCostActivity extends Activity {
	private SQLiteDatabase db;  //for opening the database
	private StringTokenizer tokenizer;
	private String date; //for retrieving date
	private String cost; //for retrieving previous expense to display
	private EditText editText;  //for displaying expense amount
	private String selected;
	private TextView textView;


	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cost_update);
		DbHelper dbHelper = new DbHelper(this);
		editText = (EditText) findViewById(R.id.updateamt);
		textView=(TextView)findViewById(R.id.updateAmtView);
		db = dbHelper.getWritableDatabase();
		Bundle bundle = getIntent().getExtras();
		tokenizer = new StringTokenizer(bundle.getString("cost"), ":");
		while (tokenizer.hasMoreElements()) {
			selected = tokenizer.nextToken().trim();
			date = tokenizer.nextToken().trim();
			cost = tokenizer.nextToken().trim();
		}
		StringTokenizer tokenizer1 = new StringTokenizer(cost, "-");
		while (tokenizer1.hasMoreElements()) {
			cost = tokenizer1.nextToken();
		}
		textView.append("  :  " +selected);
		editText.setText(cost);

	}
    //called when the user clicks on update button
	public void updateCost(View v) {
		cost = editText.getText().toString().trim();
		ContentValues values = new ContentValues();
		values.put("cost", cost);
		int j = db.update("store", values, "date='" + date + "'", null);
		db.close();
		if (j > 0) {
			Toast.makeText(this, "Updation Successfull", Toast.LENGTH_SHORT);
		}
		Intent intent = new Intent(this, ViewActivity.class);
		intent.putExtra("selected", selected);
		startActivity(intent);
	}

}
