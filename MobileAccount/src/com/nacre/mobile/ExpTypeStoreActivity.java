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
 * This class adds expense Category to 
 */
public class ExpTypeStoreActivity extends Activity {
	
	private int count; //to know an expense type exists in the data base or not
	SQLiteDatabase db;// for open the data base
	Cursor cursor; //for reading data from database
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.expense_type);
	}
   //for storing expense type in the data base
	public void store(View view){
		EditText type=(EditText)findViewById(R.id.eptype);//for reading the type
		EditText des=(EditText)findViewById(R.id.edes);//for reading the description
		String expname=type.getText().toString().trim();
		expname.toUpperCase();
		DbHelper helper=new DbHelper(this);
		try{
		db=helper.getWritableDatabase();
		cursor=db.rawQuery("select * from expense where upper(expname)='"+expname+"'", null);
		if(cursor!=null){
		  count=cursor.getCount();
		  /*cursor.moveToFirst();
		  exists=cursor.getString(0);*/
		}
		if(type.getText().toString().trim().equals(""))
		{
			Toast.makeText(this, "Type Must not be empty", Toast.LENGTH_LONG).show();
		}else if(count>0){
			Toast.makeText(this, expname+" Type already Exists!!", Toast.LENGTH_LONG).show();
			type.setText("");
			des.setText("");
		}else{
			ContentValues values=new ContentValues();
			values.put("expname",expname );
			values.put("desc", des.getText().toString().trim());
			long r=  db.insert("expense", null, values);
			if(r!=0){
				Toast.makeText(this,"Store Success", Toast.LENGTH_LONG).show();
				type.setText("");
				des.setText("");
			}
			
		}
		}catch (SQLException e) {
			Log.w("ExpTypeStoreActivity", "Sql Exception");
		}finally{
			if(cursor!=null)
				cursor.close();
			db.close();
		}
	}//store
  //method for switching the control to Login activity
	public void home(View view) {
		/*Intent intent = new Intent(this, LoginActivity.class);
		startActivity(intent);*/
		finish();
	}

}
