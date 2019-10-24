package com.nacre.mobile;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
/*
 * This Activity allows the user to change password.
 * this class provides fully validation
 */
public class ChangePasswordActivity extends Activity {
	EditText oldp; //to read old password
	EditText newp;  //to read new password
	EditText rep;  //to read confirm password
	static SharedPreferences preferences;
	String password;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.changepassword);
		preferences= getSharedPreferences("login", MODE_WORLD_WRITEABLE);
		password = preferences.getString("password", "no");
	}
      //called when submit button is clicked
	public void submit(View view) {
		oldp = (EditText) findViewById(R.id.oldpwd);
		newp = (EditText) findViewById(R.id.newpwd);
		rep = (EditText) findViewById(R.id.redpwd);
		 
		if (oldp.getText().toString().equals("")) {
			Toast.makeText(this, "Old password must not be empty",
					Toast.LENGTH_LONG).show();
		} else if (newp.getText().toString().equals("")) {
			Toast.makeText(this, "New password must not be empty",
					Toast.LENGTH_LONG).show();
		} else if (rep.getText().toString().equals("")) {
			Toast.makeText(this, "Re enter password must not be empty",
					Toast.LENGTH_LONG).show();
		} else {
			if (password.equals(oldp.getText().toString().trim())) {
				if (!newp.getText().toString().trim().equals(
						rep.getText().toString().trim())) {
					Toast.makeText(this, "Passwords are not matched",
							Toast.LENGTH_LONG).show();
				} else {
					SharedPreferences.Editor editor = preferences.edit();
					editor.putString("password", newp.getText().toString()
							.trim());
					editor.commit();
					Toast.makeText(this, "Password changed sucessfully",
							Toast.LENGTH_LONG).show();
					/*Intent intent = new Intent(this, LoginActivity.class);
					startActivity(intent);*/
					finish();

				}// else
			} else {
				Toast.makeText(this, "Old Password Entered is Wrong!!",
						Toast.LENGTH_LONG).show();
			}
		}

	}//submit
	//when user click on clear button , this method is called
	public void refresh(View view){
		if(oldp!=null)
		oldp.setText("");
		if(newp!=null)
		newp.setText("");
		if(rep!=null)
		rep.setText("");
	}

}
