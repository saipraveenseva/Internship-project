package com.nacre.mobile;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
/*
 * This class is used for opening the database and returns db object
 */
public class DbHelper extends SQLiteOpenHelper {
	private final static String DATA_BASE = "expenseStorage"; //Name of the database
	private final static int VERSION = 1;
	private final static String create_home = "create table expense(expname text primary key,desc text not null)";
	// private final static String
	// login="create table user(password text not null)";
	private final static String store = "create table store(expname text not null,cost real not null,date datetime not null)";

	public DbHelper(Context context) {
		super(context, DATA_BASE, null, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		db.execSQL(create_home);
		db.execSQL(store);
		// db.execSQL(login);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.v(create_home, "In expense table");
		db.execSQL("drop table" + create_home + "if exists");
		db.execSQL("drop table" + store + "if exists");
		onCreate(db);
	}
}
