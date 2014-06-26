package com.app.helium.SyncedStorage;

import java.util.ArrayList;


import android.content.Context;
import android.database.Cursor;


//NR: TODO : This class should extend the 	android.database.sqlite.SQLiteQueryBuilder or can utilize it functionality
         //  android.database.sqlite.SQLiteQueryBuilder provides better support for Joins.

public class DatabaseQuery {
	// Variables area
	private ArrayList<String> arrayKeys = null;
	private ArrayList<Object> arrayValues = null;
	//private ArrayList<String> databaseKeys = null;
	//private ArrayList<String> databaseKeyOptions = null;
	private DBAdapter database;
	
	/**
	 * Initialize the ArrayList
	 * @param context Pass context from calling class.
	 */
	public DatabaseQuery(Context context,String databaseName,String tableName,ArrayList<String> databaseKeys,ArrayList<String> databaseKeyOptions ) {
		
		// Call the database adapter to create the database
		database = new DBAdapter(context,databaseName, tableName, databaseKeys, databaseKeyOptions);
        database.open();
		arrayKeys = new ArrayList<String>();
		arrayValues = new ArrayList<Object>();

	}
	
	/**
	 * Append data to an ArrayList to then submit to the database
	 * @param key Key of the value being appended to the Array.
	 * @param value Value to be appended to Array.
	 */
	public void appendColumnData(String key, Object value){
		arrayKeys.add(key);
		arrayValues.add(value);
	}
	
	
	/**
	 * This method adds the row created by appending data to the database.
	 * The parameters constitute one row of data.
	 * return: Row id of inserted row or -1 if Fail
	 */
	public long addRow(){
		return database.insertEntry(arrayKeys, arrayValues);
	}
	
	/**
	 * This method updates the row in database based in rowId criteria.
	 * The parameters constitute one row of data.
	 * return: Number of Rows affected.
	 */
	public long updateRow(long rowId){
		return database.updateEntry(rowId, arrayKeys, arrayValues);
	}
	
	
	
	/**
	 * Append Row data to an ArrayList to then submit to the database
	 * @param columnNames ArrayList of Column Names.
	 * @param columnValues ArrayList of respective Column values to b inserted into Database .
	 */
	
	public void appendRowData(ArrayList<String> columnNames, ArrayList<String> columnValues)
	{
		for (int i=0;i<columnNames.size();i++)
		{
			appendColumnData(columnNames.get(i),columnValues.get(i));
		}
		
	}
	
	
	/**
	 * Get data list [single column] from the table.
	 * @param column : Column  to include in the result.
	 * @param selection Return rows with the following string only. Null returns all rows.
	 * @param selectionArgs Arguments of the selection.
	 * @param groupBy Group results by.
	 * @param having A filter declare which row groups to include in the cursor.
	 * @param sortBy Column to sort elements by.
	 * @param sortOption ASC for ascending, DESC for descending.
	 * @return Returns an ArrayList<String> with the results of the sortBy Column field.
	 */
	public ArrayList<String> getDataList(String column, String selection, String[] 
	  selectionArgs, String groupBy, String having, String sortOption){
		
		ArrayList<String> list = new ArrayList<String>(); 
		String[] keys = new String[1];
		keys[0]= column;
		Cursor results = database.getAllEntries(keys, selection, 
				selectionArgs, groupBy, having, column, sortOption);
		while(results.moveToNext())
			list.add(results.getString(results.getColumnIndex(column)));
		return list;

	}
	
	/**
	 * Get data from the table.
	 * @param keys List of columns to include in the result.
	 * @param selection Return rows with the following string only. Null returns all rows.
	 * @param selectionArgs Arguments of the selection.
	 * @param groupBy Group results by.
	 * @param having A filter declare which row groups to include in the cursor.
	 * @param sortBy Column to sort elements by.
	 * @param sortOption ASC for ascending, DESC for descending.
	 * @return Returns an Cursor to the results of the selected field.
	 */
	public Cursor getDataCursor(String[] keys, String selection, String[] 
	  selectionArgs, String groupBy, String having, String sortBy, String sortOption){ 
		Cursor results = database.getAllEntries(keys, selection, 
				selectionArgs, groupBy, having, sortBy, sortOption);
		return results;

	}
	
	/**
	 * Get data from the table.
	 * @param keys List of columns to include in the result.
	 * @param selection Return rows with the following string only. Null returns all rows.
	 * @param selectionArgs Arguments of the selection.
	 * @param groupBy Group results by.
	 * @param having A filter declare which row groups to include in the cursor.
	 * @param sortBy Column to sort elements by.
	 * @param sortOption ASC for ascending, DESC for descending.
	 * @return Returns an Cursor to the results of the selected field.
	 */
	public Cursor getDataCursor(String[] keys, String selection, String[] 
	  selectionArgs, String groupBy, String having, String sortBy, String sortOption, String limit){ 
		Cursor results = database.getAllEntries(keys, selection, 
				selectionArgs, groupBy, having, sortBy, sortOption,limit);
		return results;

	}
	
	/**
	 * Remove a row from the database.
	 * @param rowIndex Number of the row to remove.
	 * @return Returns TRUE if it was deleted, FALSE if failed.
	 */
	public boolean deleteRow(long rowId)
	{
		return database.removeEntry(rowId);
	}
	
	/**
	 * Delete Row based on Condition [Eg. "age>25"]
	 * @param condition
	 * @return Returns TRUE if it was deleted, FALSE if failed.
	 */
	public boolean deleteRow(String condition)
	{
		return database.removeEntry(condition);
	}
	
	/**
	 * Execute a Raw SQL Lite query on the database
	 * @param sqlQuery : String SQL lite query
	 * @param selectionArgs : Selection args
	 * @return Cursor to the Resultant data
	 */
	public Cursor executeRawQuery(String sqlQuery,String[] selectionArgs)
	{
		return database.executeQuery(sqlQuery, selectionArgs);
	}
	
	public void executeRawQuery(String sqlQuery)
	{
		database.executeRawQuery(sqlQuery);
	}
	
	/**
	 * Destroy the reporter.
	 * @throws Throwable
	 */
	public void destroy() throws Throwable{
		database.close();
	}
	
	/**
	 *  Truncate table Data
	 */
	public void truncate()
	{
		database.clearTable();
	}
	
	/**
	 * Clear all the row data in memory from previous database operations
	 * Recommended before calling a new database operation unless explicitly required. 
	 */
	public void flush()
	{
		arrayKeys.clear();
		arrayValues.clear();
	}
}
