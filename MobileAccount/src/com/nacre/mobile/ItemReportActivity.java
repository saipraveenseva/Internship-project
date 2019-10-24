package com.nacre.mobile;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

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
import android.widget.TextView;
import android.widget.Toast;

/*
 * This class is for generating report on individual item
 */
public class ItemReportActivity extends Activity {

	private SQLiteDatabase db; //for open database
	private Cursor cr;  //for retrieving data from data base
	private Bundle bundle;
	private String selected;  //for retrieving selected ietem from the list view
	private TextView textView; //
	private TextView total; //for display total amount spent on a particular category
	private TextView frommDateDisplay;//for getting from date
	private TextView toDateDisplay;  //for getting todate
	private int mYear; //for getting year
	private int mMonth;//for getting month
	private int mDay;//for getting day
	static final int DATE_DIALOG_ID = 1;
	static final int DATE_DIALOG_ID1 = 2;
	private int i;
	private int compare;//to compare todate and from date
	private String fm; //for holding from date
	private String tm; //for holding todate

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.report);
		fm = new String();
		tm = new String();
		frommDateDisplay = (TextView) findViewById(R.id.textFrom);
		toDateDisplay = (TextView) findViewById(R.id.textTo);
		bundle = getIntent().getExtras();
		textView = (TextView) findViewById(R.id.totalText);
		total=(TextView)findViewById(R.id.total);
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
		i = id;
		switch (id) {
		case DATE_DIALOG_ID:
			((DatePickerDialog) dialog).updateDate(mYear, mMonth, mDay);
			break;
		case DATE_DIALOG_ID1:
			((DatePickerDialog) dialog).updateDate(mYear, mMonth, mDay);
			break;
		}
	}

	private void updateDisplay() {
		frommDateDisplay.setText(new StringBuilder()
				// Month is 0 based so add 1
				.append(mDay).append("-").append(mMonth + 1).append("-")
				.append(mYear).append(" "));
	}

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

	
	//method for generating report on a particular expense type
	public void generateItemReport(View view) {
		String fromDate = frommDateDisplay.getText().toString().trim();
		String toDate = toDateDisplay.getText().toString().trim();
		DbHelper helper = new DbHelper(this);
		selected = bundle.getString("selected");
		db = helper.getReadableDatabase();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-mm-yyyy");
		String sql = "SELECT sum(cost) FROM store where expname= '" + selected
				+ "'  AND date BETWEEN '" + fromDate + "' AND '" + toDate + "'";
		try {
			cr = db.rawQuery(sql, null);
			if (fromDate.length() == 0) {
				Toast.makeText(this, "Select From Date", Toast.LENGTH_SHORT)
						.show();
			} else if (toDate.length() == 0) {
				Toast.makeText(this, "Select To Date", Toast.LENGTH_SHORT)
						.show();
			} else {

				try {
					java.util.Date fd = sdf.parse(fromDate);
					java.util.Date td = sdf.parse(toDate);
					  compare = fd.compareTo(td);
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
				if (compare> 0) {
					Toast.makeText(this, "Fromdate should be less than todate",
							Toast.LENGTH_SHORT).show();
				} else {
				

				if (fm.equals(fromDate) && tm.equals(toDate)) {

				} else {
					
					fm = fromDate;
					tm = toDate;
                     textView.setVisibility(View.VISIBLE);
                     total.setVisibility(View.VISIBLE);
					if (cr != null) {
						cr.moveToFirst();
						if (cr.getCount() > 0)
							textView.append(" on " + selected + "  is : "
									+ cr.getDouble(0));
					}
				}}
			}
		} catch (SQLException e) {
			Log.v("Item report", e.getMessage());
		} finally {
			cr.close();
			db.close();
		}

	}
}
