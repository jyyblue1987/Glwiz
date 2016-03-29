package com.stb.glwiz.data;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.stb.glwiz.Const;

import android.content.Context;
import android.database.Cursor;
import common.library.utils.CheckUtils;
import common.library.utils.FileUtils;

public class DBManager {
	private static final String DATABASE_NAME = "streamcrop.db";
	private static final String FAVORITE_TABLE = "favorite";
	
	private static Context		m_context = null;
	
	public static void loadDB(Context context)
	{
		m_context = context;
		
		DatabaseManager db = new DatabaseManager(context);
		String path = db.getDatabaseFullPath(DATABASE_NAME);
		FileUtils.copyAssetFileToSDCard(context, "data/streamcrop.db", path);

	}
	
	public static DatabaseManager openDatabaseManager()
	{
		DatabaseManager db = new DatabaseManager(m_context);
		if( db.OpenDatabase(DATABASE_NAME) == false )
			return null;
		
		return db;
	}
	
	public static void closeDatabaseManager(DatabaseManager db)
	{
		if(db == null)
			return;
		
		if( db.IsDatabaseExists(DATABASE_NAME) == false )
			return;
		
		if( db.IsOpen() == false )
			return;
		
		db.CloseDatabase();
	}
	
	public static long addRecord(Context context, String table, JSONObject data)
	{
		if( context == null || data == null )
			return -1;
		
		DatabaseManager db = new DatabaseManager(context);
		if( db.OpenDatabase(DATABASE_NAME) == false )
			return -1;
		
		long result = db.addRecord(table, data);
        
		db.CloseDatabase();
		
		return result;
	}
	
	public static long updateRecord(Context context, String table, JSONObject data, String whereClause, String []whereArgs)
	{
		if( context == null || data == null )
			return -1;
		
		DatabaseManager db = new DatabaseManager(context);
		if( db.OpenDatabase(DATABASE_NAME) == false )
			return -1;
		
		long result = db.updateRecord(table, data, whereClause, whereArgs);
        
		db.CloseDatabase();
		
		return result;
	}

	public static long addFavorite(Context context, JSONObject data)
	{
		if( context == null || data == null )
			return -1;
		
		long ret = -1;
		if( isExistFavorite(context, data) == true )
		{
			String channelID = data.optString(Const.CHANNEL_ID, "");
			
			String whereClause = "channel_id = ?";
			String [] whereArgs = { channelID };
			
			ret = updateRecord(context, FAVORITE_TABLE, data, whereClause, whereArgs);
		}
		else
		{
			ret = addRecord(context, FAVORITE_TABLE, data);	
		}
		
		return ret;
	}
	
	public static boolean isExistFavorite(Context context, JSONObject data)
	{
		if( context == null || data == null )
			return false;
		
		DatabaseManager db = new DatabaseManager(context);
		if( db.OpenDatabase(DATABASE_NAME) == false )
			return false;
				
		String channelID = data.optString(Const.CHANNEL_ID, "");
		if( CheckUtils.isEmpty(channelID) )
		{
			db.CloseDatabase();
			return false;
		}
		
		String whereClause = "channel_id = ?";
		String [] whereArgs = { channelID };
		String [] column = {"count(*)"};
		
		Cursor cursor = db.searchRecord(FAVORITE_TABLE, column, whereClause, whereArgs, null, null, "1");
		
		boolean exist = db.isExistRecord(cursor);
		
		cursor.close();
		db.CloseDatabase();

		return exist;
	}
	
	public static List<JSONObject> getFavoriteList(Context context)
	{
		List<JSONObject> list = new ArrayList<JSONObject>();
		if( context == null)
			return list;
		
		DatabaseManager db = new DatabaseManager(context);
		if( db.OpenDatabase(DATABASE_NAME) == false )
			return list;
				
		Cursor cursor = db.searchRecord(FAVORITE_TABLE, null, null, null, null, null, null);
		
		list = db.getRecordData(cursor);
		
		cursor.close();
		db.CloseDatabase();

		return list;
	}
	
	
}
