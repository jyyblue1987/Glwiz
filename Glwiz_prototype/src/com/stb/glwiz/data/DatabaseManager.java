package com.stb.glwiz.data;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

public class DatabaseManager {	
	private Context			m_context;
	private DatabaseHelper	m_Helper;
	private SQLiteDatabase	m_db;

	public DatabaseManager( Context context) {
		m_context = context;
		m_db = null;		
	}
	
	public SQLiteDatabase getDB()
	{
		return m_db;
	}
	
	public boolean IsDatabaseExists( String sDBName ) {
	     File file = m_context.getDatabasePath(sDBName);       

		if( file.exists() && file.isFile() ) {
			return true;
		}
		
		return false;
	}

	public boolean CreateDatabase( String sDBName ) {
		
		if( IsDatabaseExists(sDBName) == true )
			return true;

		try {
			m_db = m_Helper.getWritableDatabase();
		} catch( SQLiteException e ) {
			return false;
		}
		
		return true;
	}

	public boolean OpenDatabase( String sDBName ) {

		m_Helper = DatabaseHelper.getInstance(m_context, sDBName);
	
		boolean bExist =  IsDatabaseExists(sDBName);
		if( bExist == false )
			return false;

		m_db = m_Helper.openDatabase();
		if( !m_db.isOpen() )
			return false;

		return true;
	}
	
	public void CloseDatabase( ) {
		m_Helper.closeDatabase();
//		m_db.close();
//		m_db = null;
//		
//		m_Helper.close();
//		m_Helper = null;
	}
	
	public boolean IsOpen( ) {
		if( m_db != null ) {
			if( m_db.isOpen() )
				return true;
		}

		return false;
	}
	
	public Cursor OpenCursor( String sQuery ) {
		Cursor c = null;

		try {
			if( IsOpen() )
				c = m_db.rawQuery(sQuery, null);
		} catch ( SQLException e ) {
			c = null;
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return c;
	}
	
	public boolean CreateTable( String strTableName, String[] strFieldNameArray, String[] strFieldDataType ) {

		if( m_db == null )
			return false;
		if( m_db.isOpen() == false )
			return false;
		
		int fieldCount = strFieldNameArray.length;
		if( fieldCount < 1 )
			return false;
		if( fieldCount != strFieldDataType.length )
			return false;
		
		String strSQL = "CREATE TABLE IF NOT EXISTS " + strTableName + " (";
		for( int i = 0; i < fieldCount; i++ ) {
			strSQL = strSQL + strFieldNameArray[i] + " " + strFieldDataType[i];
			if( i < fieldCount-1 )
				strSQL = strSQL + ", ";
		}
		
		strSQL = strSQL + ")";
		try{
			m_db.execSQL(strSQL);
		} catch( SQLException  e){
			return false;
		}catch(Exception e) {
			e.printStackTrace();
		}

		return true;
	}
	
	public long addRecord(String table, JSONObject data)
	{
		if( data == null )
			return -1;
		
		ContentValues values = new ContentValues();
		
		Iterator<String> iter = data.keys();
		while (iter.hasNext()) {
		    String key = iter.next();
		    
		    try {
		    	String value = data.optString(key, "");
		    	values.put(key, value);		    	
		    } catch (Exception e) {
		        // Something went wrong!
		    }
		}
        
		try {
			m_db.beginTransaction();
			long result = m_db.insert(table, null, values);			
			m_db.setTransactionSuccessful();
			return result;
		} catch ( SQLException e ) {
			e.printStackTrace();
		}catch(Exception e) {
			e.printStackTrace();
		} finally {
			m_db.endTransaction();
		}
		
        
		return -1;
	}
	
	public long updateRecord(String table, JSONObject data, String whereClause, String []whereArgs)
	{
		if( data == null )
			return -1;
		
		ContentValues values = new ContentValues();
		
		Iterator<String> iter = data.keys();
		while (iter.hasNext()) {
		    String key = iter.next();
		    
		    try {
		    	String value = data.optString(key, "");
		    	values.put(key, value);
		    } catch (Exception e) {
		        // Something went wrong!
		    }
		}
        
		try {
			m_db.beginTransaction();
			long result = m_db.update(table, values, whereClause, whereArgs);	
			m_db.setTransactionSuccessful();
			return result;
		} catch ( SQLException e ) {
			e.printStackTrace();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			m_db.endTransaction();
		}
        
		return -1;   
	}
	
	public int deleteRecord(String table, String whereClause, String []whereArgs)
	{
		try {
			m_db.beginTransaction();
			int result = m_db.delete(table, whereClause, whereArgs);
			m_db.setTransactionSuccessful();
			return result;
		} catch ( SQLException e ) {
			e.printStackTrace();
		}catch(Exception e) {
			e.printStackTrace();
		} finally {
			m_db.endTransaction();
		}
		
        return -1;		
	}

	public Cursor searchRecord(String table, String [] columns, String whereClause, String []whereArgs, String orderBy, String limit)
	{
		try {
			return m_db.query(table, columns, whereClause, whereArgs, null, null, orderBy, limit);		
		} catch ( SQLException e ) {
			e.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
        return null;
	}
	
	public Cursor searchRecord(String table, String [] columns, String whereClause, String []whereArgs, String groupBy, String orderBy, String limit)
	{
		try {
			return m_db.query(table, columns, whereClause, whereArgs, groupBy, null, orderBy, limit);		
		} catch ( SQLException e ) {
			e.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
        return null;
	}
	
	
	public Cursor SearchRecords( String strTableName, String strQuery ) {
		String sQuery = "SELECT * FROM " + strTableName + " WHERE " + strQuery;

		return OpenCursor(sQuery);
	}

	public Cursor SearchRecords( String strFieldName, String strTableInfo, String strQuery ) {
		String sQuery;
		
		if( strQuery.equals("") == true )
			sQuery = "SELECT " + strFieldName + " FROM " + strTableInfo;
		else
			sQuery = "SELECT " + strFieldName + " FROM " + strTableInfo + " WHERE " + strQuery;

		return OpenCursor(sQuery);
	}
	
	public Cursor SearchRecordsWithRaw( String strFieldName, String strTableInfo, String strQuery ) {
		String sQuery;
		
		sQuery = "SELECT " + strFieldName + " FROM " + strTableInfo + " " + strQuery;

		return OpenCursor(sQuery);
	}
	
	public void DeleteAllRecords( String strTableInfo )
	{
		String sQuery;
		
		sQuery = "DELETE FROM " + strTableInfo;
		
		try{
			m_db.beginTransaction();
			m_db.execSQL(sQuery);
			m_db.setTransactionSuccessful();
		} catch( SQLException  e){
			return;
		} finally {
			m_db.endTransaction();
		}
	}
	
	public void DeleteRecords( String strTableInfo, String strQuery )
	{
		String sQuery;
		
		sQuery = "DELETE FROM " + strTableInfo + " WHERE " + strQuery;
		
		try{
			m_db.beginTransaction();
			m_db.execSQL(sQuery);
			m_db.setTransactionSuccessful();
		} catch( SQLException  e){
			return;
		}finally {
			m_db.endTransaction();
		}
	}
	public boolean AddRecord( String strTableName, String[] strFieldNameArray, String[] strFieldValues ) {
		
		if( m_db == null )
			return false;
		if( m_db.isOpen() == false )
			return false;
		int fieldCount = strFieldNameArray.length;
		if( fieldCount < 1 )
			return false;
		if( fieldCount != strFieldValues.length )
			return false;

		String strSQL = "INSERT INTO " + strTableName + "(";
		for( int i = 0; i < fieldCount - 1; i++ ) {
			strSQL = strSQL + strFieldNameArray[i] + ", ";
		}
		strSQL = strSQL + strFieldNameArray[fieldCount - 1] + ") VALUES (";
		
		for( int i = 0; i < fieldCount - 1; i++ ) {
			strSQL = strSQL + strFieldValues[i] + ", ";
		}
		strSQL = strSQL + strFieldValues[fieldCount - 1] + ")";

		try {
			m_db.beginTransaction();
			m_db.execSQL( strSQL );
			m_db.setTransactionSuccessful();
		} catch ( Exception e ) {
			return false;
		}finally {
			m_db.endTransaction();
		}
		
		return true;
	}

	public boolean  UpdateRecords( String strTableName, String strSetValue, String strQuery ) {
		if( IsOpen( ) == false )
			return false;
		
		String	strSQLCom;
		
		if( strQuery == "" )
			strSQLCom = String.format( "UPDATE %s SET %s", strTableName, strSetValue );
		else
			strSQLCom = String.format( "UPDATE %s SET %s WHERE %s", strTableName, strSetValue, strQuery );
		
		try {
			m_db.beginTransaction();
			m_db.execSQL( strSQLCom );
			m_db.setTransactionSuccessful();
		} catch ( Exception e ) {
			return false;
		}finally {
			m_db.endTransaction();
		}

		return true;
	}
	
	public String getDatabaseFullPath( String strDBName ) {
		 File file = m_context.getDatabasePath(strDBName);       
		String path = file.getAbsolutePath();
		String dir_path = path.substring(0, path.length() - strDBName.length() - 1);
		new File(dir_path).mkdirs();
		
		return path;
	}
	
	public int getTableRecordCount( String strTableName )
	{
		if( IsOpen() == false )
			return 0;
		
		String	strSQLCom = "SELECT COUNT(*) FROM " + strTableName;
		
		Cursor cursor = OpenCursor(strSQLCom);
		
		if( cursor == null )
			return 0;
		
		if( cursor.moveToFirst() == false )
			return 0;
		
		return cursor.getInt(0);		
	}

	public List<JSONObject> getRecordData(Cursor cursor)
	{
		List<JSONObject> list = new ArrayList<JSONObject>();

		if( cursor == null )
			return list;
				
		int columnCount = cursor.getColumnCount();
		
		while( cursor.moveToNext() )
		{	    
			JSONObject record = new JSONObject();
			for(int i = 0; i < columnCount; i++ )
			{
				try{
					String name = cursor.getColumnName(i);
					String value = cursor.getString(i);
					record.put(name, value);
				}catch(Exception e){
					
				}				
			}
			list.add(record);
		}
		
		return list;
	}
	
	public boolean isExistRecord(Cursor cursor)
	{
		if( cursor == null )
			return false;			
		
		while( cursor.moveToNext() )
		{	    
			long count = cursor.getLong(0);
			if( count < 1 )
				return false;
			return true;
		}
		return true;
	}
	
	public JSONObject getFirstRecordData(Cursor cursor)
	{

		if( cursor == null )
			return null;
				
		int columnCount = cursor.getColumnCount();
		
		while( cursor.moveToNext() )
		{	    
			JSONObject data = new JSONObject();

			for(int i = 0; i < columnCount; i++ )
			{
				try{
					String name = cursor.getColumnName(i);
					String value = cursor.getString(i);
					data.put(name, value);
				}catch(Exception e){
					
				}				
			}
			return data;
		}
		
		return null;
	}
	
	public int getMaxValue(String tableName, String fieldName)
	{
		if( IsOpen() == false )
			return 0;
		
		String	strSQLCom = "SELECT max(" + fieldName  + ") FROM " + tableName;
		
		Cursor cursor = OpenCursor(strSQLCom);
		
		if( cursor == null )
			return 0;
		
		if( cursor.moveToFirst() == false )
			return 0;
		
		return cursor.getInt(0);	
	}

}
