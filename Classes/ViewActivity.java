package com.nacre.mobile;

import java.util.ArrayList;
import java.util.StringTokenizer;

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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/*
 * This class is used for display day wise expense of categoryType
 * also for deleting and updating the expense type amount
 */
public class ViewActivity extends Activity {
	private ListView lv;  // listview for displaying day wise amount spent on  a particular category
	private ArrayList item;
	private ArrayAdapter adapter;
	private SQLiteDatabase db;  //for opening database
	private Cursor cr;  //for retrieving data from database
	private String selected;  //for retrieving the category selected by user
	private String delItem;  //to hold the expense of a category to delete
	private String sdate;  //for retrieving date from database
	private double cost;  //for reading day wise expense by user
	private Bundle bundle; //for restoring the state of activity
	private TextView view;  //display a message like 'categoryName' expense
	private int size ;//to get size of sparse boolean array
	private SparseBooleanArray checkedPositions; //for holding the positions selected
	private StringTokenizer st;


	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view);
		item = new ArrayList();
		lv = (ListView) findViewById(R.id.list);
		adapter = new ArrayAdapter(this,
				android.R.layout.simple_list_item_multiple_choice, item);

		lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		lv.setAdapter(adapter);
		bundle = getIntent().getExtras();
		selected = bundle.getString("selected");
		view = (TextView) findViewById(R.id.vieItem);
		view.setText(selected + "  Expenses");

		try {
			DbHelper helper = new DbHelper(this);
			db = helper.getReadableDatabase();
			cr = db.query("store", new String[] { "expname", "cost", "date" },
					"expname=" + "'" + selected + "'", null, null, null, null);

			if (cr != null) {
				cr.moveToFirst();
				if (cr.getCount() > 0)
					do {
						sdate = cr.getString(cr.getColumnIndex("date"));
						cost = cr.getDouble(cr.getColumnIndex("cost"));
						item.add(sdate + " :     Rs-" + cost);
					} while (cr.moveToNext());
			}// if
		} catch (SQLException e) {
			Log.v("expense", e.getMessage());
		} finally {
			if (cr != null)
				cr.close();
			db.close();
		}
		finishActivity(10);

	}


	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.itemmenu, menu);
		return true;

	}


	public boolean onOptionsItemSelected(MenuItem item1) {
		checkedPositions = new SparseBooleanArray();

		switch (item1.getItemId()) {
		case R.id.update1:
			checkedPositions.clear();

			checkedPositions = lv.getCheckedItemPositions();

			size = checkedPositions.size();
			if (size != 0) {
				delItem = adapter.getItem(checkedPositions.keyAt(0));
				Intent intent = new Intent(this, UpdateCostActivity.class);
				intent.putExtra("cost", selected+":"+delItem);
				startActivity(intent);
			}

			return true;
		case R.id.delete1:
			deleteItem();

			return true;
		default:
			return super.onOptionsItemSelected(item1);
		}

	}

	  //this method is written to delete a particular amount selected
	private void deleteItem() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Alert Message");
		builder.setMessage("Do You want to delete ?");
		builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {


			public void onClick(DialogInterface dialog, int which) {
				confirmDelete();
			}
		});
		builder.setNegativeButton("No", new DialogInterface.OnClickListener() {


			public void onClick(DialogInterface dialog, int which) {
				clearSelection();
			}
		});
		AlertDialog alert = builder.create();
		alert.show();

	}

	// called when the user click's on No button of alert dialog
	private void clearSelection() {
		checkedPositions = lv.getCheckedItemPositions();
		checkedPositions.clear();
		adapter.notifyDataSetChanged();
	}

	// called when the user click's on Ok button of alert dialog
	private void confirmDelete() {
		checkedPositions.clear();

		checkedPositions = lv.getCheckedItemPositions();

		size = checkedPositions.size();
		try {
			if (size != 0) {

				for (int i = 0; i < size; i++) {

					if (checkedPositions.valueAt(i)) {

						selected = adapter.getItem(checkedPositions.keyAt(i));
					}

					st = new StringTokenizer(adapter.getItem(checkedPositions
							.keyAt(i)), ":");
					delItem = st.nextToken();
					deleteItemCost(delItem.trim());
					item.remove(adapter.getItem(checkedPositions.keyAt(i)));
					adapter.notifyDataSetChanged();

					checkedPositions.clear();

				}
			}else
			{
				Toast.makeText(this, "Select items to Delete", Toast.LENGTH_SHORT).show();
			}
		} catch (Exception e) {

			Log.w("Array Index", "ArrayIndexOutOeBoundException");
		}

	}

	// called when user clicked on addItem
	public void addNewItem(View view) {
		Intent intent = new Intent(this, AddNewItemActivity.class);
		intent.putExtra("selected", selected);
		startActivity(intent);

	}

	// called when user clicked on report button
	public void itemReport(View view) {
		Intent intent = new Intent(this, ItemReportActivity.class);
		intent.putExtra("selected", selected);
		startActivity(intent);

	}

	// deleting cost of an item from the database
	public void deleteItemCost(String delCost) {
		DbHelper helper = new DbHelper(this);
		db = helper.getWritableDatabase();
		db.delete("store", "date= '" + delCost + "'", null);
		db.close();

	}

}
