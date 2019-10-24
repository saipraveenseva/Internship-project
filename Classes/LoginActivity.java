package com.nacre.mobile;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/*
 * This class is used to get the data from database
 * and displaying that information in a ListView
 */
public class LoginActivity extends Activity {
	private ListView lv;
	// for displaying expense Category in a ListView
	private ArrayList menu;
	// for adding expense Category to arrayList
	private ArrayAdapter adapter;
	private SparseBooleanArray checkedPositions;
	/* holds the positions,selected by user */
	private int position;
	private int size;
	/*
	 * for holding the size of SparseBooleanArray. i.e.how many elements
	 * selected by user
	 */
	private SQLiteDatabase db; // for opening SQLite Database
	private Cursor cr; // for retrieving data from SQLite database
	Bundle bundle;

	/* for restoring the state of Activity when the user click on back button */

	public void onCreate(Bundle savedInstanceState) {
		bundle = savedInstanceState;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		menu = new ArrayList();
		lv = (ListView) findViewById(R.id.hlist);
		adapter = new ArrayAdapter(this,
				android.R.layout.simple_list_item_multiple_choice, menu);

		lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		lv.setAdapter(adapter);

		try {
			DbHelper helper = new DbHelper(this);
			db = helper.getReadableDatabase();
			cr = db.query("expense", new String[] { "expname", "desc" }, null,
					null, null, null, null);

			if (cr != null) {
				cr.moveToFirst();
				if (cr.getCount() > 0)
					do {
						menu.add(cr.getString(cr.getColumnIndex("expname")));
					} while (cr.moveToNext());
			}// if
		} catch (SQLException e) {
			Log.v("expense", e.getMessage());
		} finally {
			cr.close();
			db.close();
		}
	}

	// method for menu creation

	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.homemenu, menu);
		return true;

	}


	public boolean onOptionsItemSelected(MenuItem item) {
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView parent, View view,
					int position, long id) {
				LoginActivity.this.position = position;
			}
		});
		checkedPositions = new SparseBooleanArray();
		String selected = null;
		switch (item.getItemId()) {
		case R.id.update:
			checkedPositions.clear();
			checkedPositions = lv.getCheckedItemPositions();
			size = checkedPositions.size();
			if (size != 0 && size == 1) {
				selected = adapter.getItem(checkedPositions.keyAt(0));
				Intent intent = new Intent(this, UpdateActivity.class);
				intent.putExtra("update", selected);
				startActivity(intent);

			} else if (size > 1) {

				Toast.makeText(this, "select only one item", Toast.LENGTH_LONG)
						.show();
				checkedPositions.clear();
				adapter.notifyDataSetChanged();
			} else {
				Toast.makeText(this, "select exactly  one item for Update",
						Toast.LENGTH_LONG).show();
				checkedPositions.clear();
				adapter.notifyDataSetChanged();
			}
			return true;
		case R.id.delete:
			checkedPositions.clear();
			checkedPositions = lv.getCheckedItemPositions();
			size = checkedPositions.size();
			if (size > 0) {

				AlertDialog.Builder dialog = new AlertDialog.Builder(this);
				dialog.setMessage("Do You want to delete ?");
				dialog.setTitle("Alert Message");
				dialog.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {


							public void onClick(DialogInterface dialog,
									int which) {
								deleteConfirm();

							}
						});

				dialog.setNegativeButton("NO",
						new DialogInterface.OnClickListener() {


							public void onClick(DialogInterface dialog,
									int which) {
								clearSelection();

							}
						});

				AlertDialog alert = dialog.create();
				alert.show();
			} else {
				Toast.makeText(this, "Select elements for delete",
						Toast.LENGTH_SHORT).show();
			}

			return true;

		case R.id.view:
			checkedPositions.clear();
			checkedPositions = lv.getCheckedItemPositions();
			size = checkedPositions.size();
			if (size != 0 && size == 1) {
				selected = adapter.getItem(checkedPositions.keyAt(0));
				Intent intent = new Intent(this, ViewActivity.class);
				intent.putExtra("selected", selected);
				startActivity(intent);
			} else if (size > 1) {

				Toast.makeText(this, "select only one item", Toast.LENGTH_LONG)
						.show();
				checkedPositions.clear();
				adapter.notifyDataSetChanged();
			} else {
				Toast.makeText(this, "select exactly  one item for Viewing",
						Toast.LENGTH_LONG).show();
				checkedPositions.clear();
				adapter.notifyDataSetChanged();
			}
			return true;
		default:
			Toast.makeText(this, "select exactly  one item for Viewing",
					Toast.LENGTH_LONG).show();
			return super.onOptionsItemSelected(item);
		}

	}


	protected void onResume() {
		super.onResume();
		onCreate(bundle);
	}

	// for clearing user selected checkedPositions list
	private void clearSelection() {
		checkedPositions = lv.getCheckedItemPositions();
		checkedPositions.clear();
		adapter.notifyDataSetChanged();
	}

	// method for confirming deletion
	private void deleteConfirm() {
		String s = null;

		try {
			while (size != 0) {
				// int j = 0;

				for (int i = 0; i < size; i++) {
					if (checkedPositions.valueAt(position)) {

						s = adapter.getItem(checkedPositions.keyAt(position));
					}
					menu.remove(s);
					delete(s);
					adapter.notifyDataSetChanged();
				}
				size--;
			}
		} catch (IndexOutOfBoundsException e) {
			Log.w("Array Index", "Array index outof bounds exception");
		}
		checkedPositions.clear();
		adapter.notifyDataSetChanged();
	}

	// method for deleting expense category and it's related data from database
	private void delete(String selected) {

		DbHelper helper = new DbHelper(getApplicationContext());
		db = helper.getWritableDatabase();
		db.delete("expense", "expname=" + "'" + selected + "'", null);
		db.delete("store", "expname=" + "'" + selected + "'", null);
		Toast.makeText(getApplicationContext(), "Deleted Successfully",
				Toast.LENGTH_LONG);
		if (db != null)
			db.close();

	}

	// called when new expense type is going to add
	public void addNewExpType(View view) {
		Intent intent = new Intent(this, ExpTypeStoreActivity.class);
		startActivity(intent);

	}

	// called when change password button is clicked
	public void changePwd(View view) {
		Intent intent = new Intent(this, ChangePasswordActivity.class);
		startActivity(intent);

	}

	// called when report button is clicked
	public void hReport(View view) {
		Intent intent = new Intent(this, TotalReportActivity.class);
		startActivity(intent);
	}

}
