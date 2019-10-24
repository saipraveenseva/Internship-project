package com.nacre.mobile;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/*
 * This UpdateActivity class is used for updating 
 * expenseCategory information in the database
 */
public class UpdateActivity extends Activity {
	EditText udes; // for reading the category description entered by the user
	EditText utype; // for reading category type entered by the user
	SQLiteDatabase db; // for opening database
	String selected; // for retrieving expenseType which is selected by user to
						// update
	Bundle bundle; // for restoring the state of Activity
	Cursor cr; // for retrieving data from database

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.expense_type_update);
		udes = (EditText) findViewById(R.id.udes);
		utype = (EditText) findViewById(R.id.ueptype);
		bundle = getIntent().getExtras();
		selected = bundle.getString("update");
		DbHelper helper = new DbHelper(this);
		db = helper.getWritableDatabase();
		try {
			cr = db.rawQuery("select * from expense where expname=" + "'"
					+ selected + "'", null);
			if (cr != null) {
				cr.moveToLast();
				if (cr.getCount() > 0) {
					utype.setText(cr.getString(0));
					udes.setText(cr.getString(1));
				}
			}
		} catch (SQLException e) {
			Log.v("Expense Update", e.getMessage());
		} finally {
			cr.close();
		}
	}// onCreate
	// this method is called, when the user click on update button

	public void update(View view) {
		ContentValues values = new ContentValues();
		values.put("expname", utype.getText().toString());
		values.put("desc", udes.getText().toString());
		ContentValues values1 = new ContentValues();
		values1.put("expname", utype.getText().toString());
		int r = db.update("expense", values, "expname=" + "'" + selected + "'",
				null);
		db.update("store", values1, "expname=" + "'" + selected + "'", null);
		if (r >= 1) {
			Toast.makeText(this, "Update Successfull", Toast.LENGTH_SHORT)
					.show();
		}
		/*Intent intent = new Intent(this, LoginActivity.class);
		startActivity(intent);*/
		
		finish();
	}

}
