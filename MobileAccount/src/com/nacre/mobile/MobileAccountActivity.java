package com.nacre.mobile;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
//This is the beginning activity class,which checks for user credentials 
//and passes the control to LoginActivity
public class MobileAccountActivity extends Activity {
	static SharedPreferences preferences;//for storing password
	private String password;//to read the password from SharedPreferences
	private EditText text;//To read the password, entered by the user
	private Button lbButton;//for logging button

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		preferences = getSharedPreferences("login", MODE_WORLD_READABLE);
		//disPlayScreen();
		

	}
	
	private void disPlayScreen(){
		
		password = preferences.getString("password", "no");
		text = (EditText) findViewById(R.id.hpass);
		lbButton = (Button) findViewById(R.id.loginButton);
   Log.v("PASSWORD=", password);
		text.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s) {
				
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (s.toString().equals("")) {
					lbButton.setVisibility(View.GONE);
				} else
					lbButton.setVisibility(View.VISIBLE);
			}

		});
		if (password.equals("no")) {
			Intent it = new Intent(this, SetPasswordActivity.class);
			startActivity(it);

		}
	}

	//when ever user clicks on back button,this method is clear the previousely entered password
	@Override
	protected void onResume() {
		super.onResume();
		disPlayScreen();
		text.setText("");
	}

	// called when user click login button,written to check for user Credentials
	public void login(View view) {

		if (password.equals(text.getText().toString().trim())) {

			Intent intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
		} else {
			text.setText("");
			Toast.makeText(this, "Invalid Password", Toast.LENGTH_SHORT).show();
		}
	}

}