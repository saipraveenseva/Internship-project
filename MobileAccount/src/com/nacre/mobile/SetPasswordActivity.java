package com.nacre.mobile;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
/*
 * THIS CLASS IS USED TO SET THE PASSWORD FOR THE APPLICATION
 * AT THE TIME OF INSTALLING THIS APPLICATION THIS ACTIVITY ASKS THE USER TO SET THE PASSWORD
 */
public class SetPasswordActivity extends Activity {

	private EditText password;  //for reading password
	private EditText confirmPassword;//for reading confirm password
	private SharedPreferences preferences;//for getting sharedpreferences reference

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setpassword);
		password = (EditText) findViewById(R.id.setPassword);
		confirmPassword = (EditText) findViewById(R.id.setConfirmPassword);
		preferences=getSharedPreferences("login", MODE_WORLD_WRITEABLE);
	}
    //this method is called when the user clicks on setPassword
	public void setPassword(View v) {
		String password = this.password.getText().toString().trim();
		String confirmPassword = this.confirmPassword.getText().toString()
				.trim();
		if (password.equals("")) {
			Toast.makeText(this, "Password must not be empty",
					Toast.LENGTH_LONG).show();
		} else if (confirmPassword.equals("")) {
			Toast.makeText(this, "Confirm Password must not be empty",
					Toast.LENGTH_LONG).show();
		} else if (!password.equals(confirmPassword)) {
			Toast.makeText(this, "Passwords are not matched!!",
					Toast.LENGTH_LONG).show();
		} else {
			SharedPreferences.Editor editor = preferences.edit();
			editor.putString("password", password);
			editor.commit();
			/*Intent intent=new Intent(this,MobileAccountActivity.class);
			startActivity(intent);*/
			finish();
		}
	}

}
