package com.nacre.mobile;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

/*
 * This class is for generating report category wise
 * and total expense
 */
public class TotalReportActivity extends Activity {
	private SQLiteDatabase db;//for open and close database
	private Cursor cr;  //for retrieving values from database
	private TextView textView; //For display a message like 'Total Expense' 
	private TextView totalText;  //for Display total amount
	private TextView category;  //for display category name
	private TextView amount;  //for display category expense
	private TextView frommDateDisplay; //for displaying from date
	private TextView toDateDisplay; //for displaying to date
	private int mYear; //To get year
	private int mMonth;  //To get month
	private int mDay;  //To get date
	static final int DATE_DIALOG_ID = 1;
	static final int DATE_DIALOG_ID1 = 2;
	private int i;
	boolean before ;  //to compare fromdate and todate values
	private ListView listview;
	private List<HashMap<String, String>> er;
	private SimpleAdapter adapter;
	private String fm = new String();  //to store from date selected
	private String tm = new String();  //to store to date selected

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.report);
		listview = (ListView) findViewById(R.id.reportList);
		frommDateDisplay = (TextView) findViewById(R.id.textFrom);
		toDateDisplay = (TextView) findViewById(R.id.textTo);
		textView = (TextView) findViewById(R.id.total);
		totalText = (TextView) findViewById(R.id.totalText);
		category = (TextView) findViewById(R.id.ReportCat);
		amount = (TextView) findViewById(R.id.ReportAmt);
		er = new ArrayList<HashMap<String, String>>();
		final Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);

	}

	public void fromDay(View v) {
		showDialog(DATE_DIALOG_ID);
	}

	public void toDate(View view) {
		showDialog(DATE_DIALOG_ID1);
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		i = id;
		switch (id) {
		case DATE_DIALOG_ID:
			return new DatePickerDialog(this, mDateSetListener, mYear, mMonth,
					mDay);
		case DATE_DIALOG_ID1:
			return new DatePickerDialog(this, mDateSetListener, mYear, mMonth,
					mDay);
		}
		return null;
	}

	protected void onPrepareDialog(int id, Dialog dialog) {
		i=id;
		switch (id) {
		case DATE_DIALOG_ID:
			((DatePickerDialog) dialog).updateDate(mYear, mMonth, mDay);
			break;
		case DATE_DIALOG_ID1:
			((DatePickerDialog) dialog).updateDate(mYear, mMonth, mDay);
			break;
		}
	}

	//for getting from date selected by the user
	private void updateDisplay() {
		frommDateDisplay.setText(new StringBuilder()
				// Month is 0 based so add 1
				.append(mDay).append("-").append(mMonth + 1).append("-")
				.append(mYear).append(" "));
	}
  //for getting to date selected by the user
	private void todateDisplay() {
		toDateDisplay.setText(new StringBuilder()
				// Month is 0 based so add 1
				.append(mDay).append("-").append(mMonth + 1).append("-")
				.append(mYear).append(" "));
	}

	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;
			switch (i) {
			case DATE_DIALOG_ID:
				updateDisplay();
				break;
			default:
				todateDisplay();
				break;
			}

		}

	};

	public void generateItemReport(View view) {

		String fromDate = frommDateDisplay.getText().toString().trim();
		String toDate = toDateDisplay.getText().toString().trim();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-mm-yyyy");

		DbHelper helper = new DbHelper(this);
		db = helper.getReadableDatabase();
		if (fromDate.length() == 0) {
			Toast.makeText(this, "Select From Date", Toast.LENGTH_SHORT).show();
		} else if (toDate.length() == 0) {
			Toast.makeText(this, "Select To Date", Toast.LENGTH_SHORT).show();
		} else {
			
			try {
				java.util.Date fd = sdf.parse(fromDate);
				java.util.Date td = sdf.parse(toDate);
				
				  before = fd.before(td);
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
			if (!before) {
				Toast.makeText(this, "Fromdate should be less than todate",
						Toast.LENGTH_SHORT).show();
			} else {
				if (fm.equals(fromDate) && tm.equals(toDate)) {

				} else {
                     er.clear();
					fm = fromDate;
					tm = toDate;

					category.setVisibility(View.VISIBLE);
					amount.setVisibility(View.VISIBLE);
					textView.setVisibility(View.VISIBLE);
					totalText.setVisibility(View.VISIBLE);

					String sql = "SELECT expname,sum(cost) FROM store WHERE date BETWEEN '"
							+ fromDate
							+ "' AND '"
							+ toDate
							+ "' group by expname";
					String total = "SELECT sum(cost) FROM store WHERE date BETWEEN '"
							+ fromDate + "' AND '" + toDate + "'";

					try {
						cr = db.rawQuery(total, null);
						if (cr != null) {
							cr.moveToFirst();
							if (cr.getCount() > 0)
								textView.setText("" + cr.getDouble(0));// "dsd"+cr.getDouble(0)
							// );;
							cr.close();
						}
						cr = db.rawQuery(sql, null);

						if (cr != null) {
							cr.moveToFirst();
							if (cr.getCount() > 0)
								do {
									HashMap<String, String> expenseReport = new HashMap<String, String>();
									expenseReport.put("category", cr
											.getString(0));
									expenseReport.put("amt", cr.getDouble(1)
											+ "");
									er.add(expenseReport);

								} while (cr.moveToNext());
							adapter = new SimpleAdapter(this, er,
									R.layout.reportlistview, new String[] {
											"category", "amt" }, new int[] {
											R.id.category, R.id.amt });
							listview.setAdapter(adapter);
						}
					} catch (SQLException e) {
						Log.v("Item report", e.getMessage());
					} finally {
						if (cr != null)
							cr.close();
						db.close();
					}
				}// inner if

			}// try

		}// else
	}
}