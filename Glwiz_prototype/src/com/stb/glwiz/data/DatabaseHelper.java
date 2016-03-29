package com.stb.glwiz.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
	static DatabaseHelper instance = null;
	private int mOpenCounter = 0;
	private SQLiteDatabase mDatabase;
	
	public static DatabaseHelper getInstance(Context context, String sDBName) {
		if (instance == null) {
			synchronized (DatabaseHelper.class) {
				if(instance == null)
					instance = new DatabaseHelper(context, sDBName);
			}		
		}
		
		return instance;
	}
	private DatabaseHelper( Context context, String sDBName ) {
		super(context, sDBName, null, 1);
	}
	
	public void onCreate( SQLiteDatabase db ) {
		
	}
	
	public void onUpgrade( SQLiteDatabase db, int oldVersion, int newVersion ) {
		
	}
	
	public synchronized SQLiteDatabase openDatabase() {
        mOpenCounter++;
        if(mOpenCounter == 1) {
            // Opening new database
            mDatabase = getWritableDatabase();
        }
        return mDatabase;
    }

    public synchronized void closeDatabase() {
        mOpenCounter--;
        if(mOpenCounter == 0) {
            // Closing database
            mDatabase.close();

        }
    }
	
	
}

