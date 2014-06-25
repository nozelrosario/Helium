package com.app.helium.SyncedStorage;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import org.joda.time.DateTime;

import com.app.helium.Application;
import com.app.helium.Helper.Util;

import android.content.Context;
import android.database.Cursor;

public abstract class StorageProvider implements IStorageProvider {


	public ArrayList<String> table_columns;
	public ArrayList<String> table_column_options;
	protected String table_name;
	protected String databaseName;
	protected Context application_context;
	protected DatabaseQuery Database;
	protected boolean skip_insert_update_events = false;
	public long id;
	
	public StorageProvider() {	}
	
	protected void initializeTable(String table_name) {
		this.table_name = table_name;
		this.databaseName = Application.GetDatabaseName();
		this.application_context = Application.GetContext();
		this.table_columns = this.Columns();
		this.table_column_options = this.ColumnOptions();
		Database = new DatabaseQuery(application_context,this.databaseName,this.table_name,this.table_columns,this.table_column_options);
	}
	
	public abstract ArrayList<String> Columns();   // TODO: could use annotations on variables for column definition, for now hard-coded is fine

	public abstract ArrayList<String> ColumnOptions();   // TODO: could use annotations on variables for options definition, for now hard-coded is fine

	private void UpdateColumnData() {
		for(String column : this.table_columns) {
			Object field_value = null;
			try {
				field_value = this.getClass().getField(column).get(this);
			} catch (IllegalAccessException e) {
				Application.LogError("Database", "StorageProvider.UpdateColumnData() :" + e.toString());
			} catch (IllegalArgumentException e) {
				Application.LogError("Database","StorageProvider.UpdateColumnData() :" + e.toString());
			} catch (NoSuchFieldException e) {
				Application.LogError("Database","StorageProvider.UpdateColumnData() :" + e.toString());
			}
			Database.appendColumnData(column,field_value);
		}
		
	}
	
	public long getID(){
		return this.id;
	}
	
	public String getTableName() {
		return this.table_name;
	}
	
	public boolean Save() {
		boolean operation_status;
	// call before save
		skip_insert_update_events = true;
		this.OnBeforeSave();     
		if(this.id <= 0) {
			operation_status = this.Insert();
		} else {
			operation_status = this.Update();
		}
	// call after save
		this.OnAfterSave();
		skip_insert_update_events = false;
		return operation_status;
	}
	
	@Override
	public boolean Insert() {
		long result;
		this.UpdateColumnData();
		// call before insert event
		if(!this.skip_insert_update_events){
			this.OnBeforeInsert();
		}		
		result = Database.addRow();
		if(result > 0) {
			this.id = result;
		// call after insert event
			if(!this.skip_insert_update_events){
				this.OnAfterInsert();
			}
			return true;	
		} else {
			return false;
		}		
	}

	@Override
	public boolean Update() {
		long result;
		this.UpdateColumnData();
		// call before update event
		if(!this.skip_insert_update_events){
			this.OnBeforeUpdate();
		}		
		result = Database.updateRow(this.id);
		if(result > 0) {
		// call after update event
			if(!this.skip_insert_update_events){
				this.OnAfterUpdate();
			}
			return true;	
		} else {
			return false;
		}	
	}

	/**
	 * Remove current record from the database.
	 * @return Returns TRUE if it was deleted, FALSE if failed.
	 */
	@Override
	public boolean Delete() {
		// NR: TODO: Clear the current object variables as well & close DB [Destroy]
		boolean result=false;;
		this.OnBeforeDelete();
		if(this.id > 0) {
			result = Database.deleteRow(this.id);			
		}
		if(result) {
			this.OnAfterDelete();
		}		
		return result;
	}
	
	/**
	 * Remove current record from the database based on condition.
	 * @return Returns TRUE if it was deleted, FALSE if failed.
	 */
	@Override
	public boolean Delete(String condition) {
		// NR: TODO: Clear the current object variables as well & close DB [Destroy]
		boolean result=false;;
		this.OnBeforeDelete();
		if(condition != "") {
			result = Database.deleteRow(condition);
		}
		if(result) {
			this.OnAfterDelete();
		}		
		return result;
	}

	//NR:  TODO: to improve this method when implementing annotations for table columns and options
	protected String[] getAllColumns() {
		ArrayList<String> all_columns = new ArrayList<String>();
		all_columns.add("id");  //NR: Add id separately as its default column and not part of column collection.
		//all_columns.add("timeStamp");     //NR: If required we can add the time-stamp column too
		all_columns.addAll(this.table_columns);  //NR: Add all columns defined
		return all_columns.toArray(new String[this.table_columns.size() + 1]);  // +1 is for extra column 'id'
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void LoadData(long id){
		String[] columns = this.getAllColumns();
		Cursor data_cursor = Database.getDataCursor(columns, "id=?", new String[]{String.valueOf(id)}, "", "","","asc");
		if(data_cursor != null) {
			String column;
			while(data_cursor.moveToNext()) {
				for(int col_index = 0 ; col_index < columns.length ; col_index++) { //for(String column : this.table_columns) {
					column = columns[col_index];
					Object field_value = null;					
					//NR: Parse the field Value o Field Data Type
					
					try {
						int column_index = data_cursor.getColumnIndex(column) , column_data_type;
						Field field = this.getClass().getField(column);
						//NR: check if column exists in DB
						if(column_index < 0) {
							column_data_type = Cursor.FIELD_TYPE_NULL;  //NR: if column not existing, mark it null
						} else {
							column_data_type = data_cursor.getType(column_index);  //NR: if exists, get its type
						}
						
						if(column_data_type != Cursor.FIELD_TYPE_NULL) {
							if(field.getType().isPrimitive() && !(field.getType().isArray())) {
								if(field.getType() == int.class) {
									field_value = data_cursor.getInt(column_index);
								} else if(field.getType() == long.class) {
									field_value = data_cursor.getLong(column_index);
								} else if(field.getType() == float.class) {
									field_value = data_cursor.getFloat(column_index);
								} else if(field.getType() == double.class) {
									field_value = data_cursor.getDouble(column_index);
								} else if(field.getType() == short.class) {
									field_value = data_cursor.getShort(column_index);
								} else if(field.getType() == char.class) {
									field_value = data_cursor.getString(column_index).charAt(0);
								} else if(field.getType() == byte.class) {
									field_value = data_cursor.getBlob(column_index);
								} else if(field.getType() == boolean.class) {
									field_value = data_cursor.getString(column_index).equals("true");   //NR convert boolean to string "true/false"
								}
							} else if (field.getType().isArray()) {   //NR: DB will contain pipe-seperated string values in case of Array
								String values_pipe_list = String.valueOf((data_cursor.getString(column_index)));
								String[] values = values_pipe_list.split("|");
								
								field_value = Array.newInstance(field.getType().getComponentType(), values.length);
								
								// If array of IStorageProvider
								if(IStorageProvider.class.isAssignableFrom(field.getType().getComponentType())) {
									for(int i=0 ; i< values.length ; i++) {
										if((values[i] != null) && !(values[i].equals(""))) {
											IStorageProvider store_object = (IStorageProvider) field.getType().newInstance();
											store_object.LoadData(Long.valueOf(values[i]).longValue());
											Array.set(field_value, i,store_object);
										} else {
											Array.set(field_value, i,null);
										}
									}									
								} else {
									for(int i=0 ; i< values.length ; i++) {
										if((values[i] != null) && !(values[i].equals(""))) {
											Array.set(field_value, i, Util.tryParse(field.getType().getComponentType(),values[i]));
										} else {
											Array.set(field_value, i, null);
										}
									}
								} 
							} else if (field.getType().isAssignableFrom(ArrayList.class)) {   //NR: DB will contain pipe-seperated string values in case of Array List
								String values_pipe_list = String.valueOf((data_cursor.getString(column_index)));
								String[] values = values_pipe_list.split("|");
								
								field_value = field.getType().getComponentType().newInstance();  //NR: Create new instance of ArrayList
								Method add = ArrayList.class.getDeclaredMethod("add",Object.class); //NR: Get refrence to Add Method
								//NR If array of IStorageProvider
								if(IStorageProvider.class.isAssignableFrom(field.getType().getComponentType())) {
									for(int i=0 ; i< values.length ; i++) {
										if((values[i] != null) && !(values[i].equals(""))) {
											IStorageProvider store_object = (IStorageProvider) field.getType().newInstance();
											store_object.LoadData(Long.valueOf(values[i]).longValue());
											add.invoke(field_value, store_object);
										} else {
											add.invoke(field_value, (Object)null);
										}
									}									
								} else {
									for(int i=0 ; i< values.length ; i++) {
										if((values[i] != null) && !(values[i].equals(""))) {
											add.invoke(field_value, Util.tryParse(field.getType().getComponentType(),values[i]));
										} else {
											add.invoke(field_value, (Object)null);
										}
									}
								} 
							} else if(field.getType() == String.class) {
								field_value = data_cursor.getString(column_index);
								
							} else if(field.getType() == DateTime.class) {
								field_value = Util.convertDBFormatToDateTime(data_cursor.getLong(column_index));
								
							} else if(field.getType().isEnum()) {
								//NR cast the value to Enum
								field_value = Enum.valueOf((Class<Enum>) field.getType(), data_cursor.getString(column_index));  //NR: un-safe type casting, but we are already sure about Enum => String in DB 
							} else if(IStorageProvider.class.isAssignableFrom(field.getType().getClass())) {   // If Foreign Key
								//NR cast the value to IStorageProvider Type and do LoadData based on ForeignKey value from DB.
								IStorageProvider store_object = (IStorageProvider) field.getType().newInstance();
								store_object.LoadData(data_cursor.getLong(column_index));
								field_value = store_object; 
							} else {
								//NR cast the value to proper type
								//NR: This is Risky and Prone to fail.
								Application.LogWarning("Database","DataType not Supported.Trying Generic Parse un-supported Data type");
								field_value = Util.tryParse(field.getType(),data_cursor.getString(column_index));
							}
	
						}
					
						//NR: Set Parsed the field Value
						field.set(this,field_value);	
						
					} catch (IllegalAccessException e) {
						Application.LogError("Database", "StorageProvider.LoadData() on column[" + column + "] FAILED :" + e.toString());
					} catch (IllegalArgumentException e) {
						Application.LogError("Database","StorageProvider.LoadData() on column[" + column + "] FAILED :" + e.toString());
					} catch (NoSuchFieldException e) {
						Application.LogError("Database","StorageProvider.LoadData() on column[" + column + "] FAILED :" + e.toString());
					} catch (ClassCastException e) {
						Application.LogError("Database","StorageProvider.LoadData() on column[" + column + "] FAILED :" + e.toString());
					} catch (InstantiationException e) {
						Application.LogError("Database","StorageProvider.LoadData() on column[" + column + "] FAILED :" + e.toString());
					} catch (NoSuchMethodException e) {
						Application.LogError("Database","StorageProvider.LoadData() on column[" + column + "] FAILED :" + e.toString());
					} catch (InvocationTargetException e) {
						Application.LogError("Database","StorageProvider.LoadData() on column[" + column + "] FAILED :" + e.toString());
					}
				}
			}
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ArrayList<IStorageProvider> select(String where_clause, String[] query_parameters, String group_by_column, String having_clause, String sort_on_column, String sort_order, int page_size, int page_number) {
		
		ArrayList<IStorageProvider> results =  new ArrayList<IStorageProvider>();
		Cursor results_cursor;
		String limit_clause ;
		int offset;
		String[] columns = this.getAllColumns();
		
		//NR: Handle Paging
		if(page_size <= 0)  {
			page_size = 0;   // return all records
			limit_clause = null;
		} else {
			if(page_number <= 0 ) page_number = 1; // default first page
			offset = (page_number-1) * page_size;
			limit_clause = String.valueOf(offset) + "," + String.valueOf(page_size);
		}
 		
		//NR: Handle OrderBy , sort_order will be auto defaulted to "asc" in DBAdapter
		if(sort_on_column.equals("")){
			sort_on_column = "id";
		}
		
		//NR:  Fire Query
		results_cursor = Database.getDataCursor(columns, where_clause, query_parameters, group_by_column, having_clause, sort_on_column, sort_order, limit_clause);
		
		if(results_cursor != null) {
			String column;
			while(results_cursor.moveToNext()) {
				try {
					//NR: Create new instance of the Entity
					IStorageProvider result_object = this.getClass().newInstance();
					
					for(int col_index = 0 ; col_index < columns.length ; col_index++) { 
						column = columns[col_index];
						Object field_value = null;					
						//NR: Parse the field Value o Field Data Type
						
						try {
							int column_index = results_cursor.getColumnIndex(column) , column_data_type;
							Field field = result_object.getClass().getField(column);
							//NR: check if column exists in DB
							if(column_index < 0) {
								column_data_type = Cursor.FIELD_TYPE_NULL;  //NR: if column not existing, mark it null
							} else {
								column_data_type = results_cursor.getType(column_index);  //NR: if exists, get its type
							}
							
							if(column_data_type != Cursor.FIELD_TYPE_NULL) {
								if(field.getType().isPrimitive() && !(field.getType().isArray())) {
									if(field.getType() == int.class) {
										field_value = results_cursor.getInt(column_index);
									} else if(field.getType() == long.class) {
										field_value = results_cursor.getLong(column_index);
									} else if(field.getType() == float.class) {
										field_value = results_cursor.getFloat(column_index);
									} else if(field.getType() == double.class) {
										field_value = results_cursor.getDouble(column_index);
									} else if(field.getType() == short.class) {
										field_value = results_cursor.getShort(column_index);
									} else if(field.getType() == char.class) {
										field_value = results_cursor.getString(column_index).charAt(0);
									} else if(field.getType() == byte.class) {
										field_value = results_cursor.getBlob(column_index);
									} else if(field.getType() == boolean.class) {
										field_value = results_cursor.getString(column_index).equals("true");   //NR convert boolean to string "true/false"
									}
								} else if (field.getType().isArray()) {   //NR: DB will contain pipe-seperated string values in case of Array
									String values_pipe_list = String.valueOf((results_cursor.getString(column_index)));
									String[] values = values_pipe_list.split("|");
									
									field_value = Array.newInstance(field.getType().getComponentType(), values.length);
									
									// If array of IStorageProvider
									if(IStorageProvider.class.isAssignableFrom(field.getType().getComponentType())) {
										for(int i=0 ; i< values.length ; i++) {
											if((values[i] != null) && !(values[i].equals(""))) {
												IStorageProvider store_object = (IStorageProvider) field.getType().newInstance();
												store_object.LoadData(Long.valueOf(values[i]).longValue());
												Array.set(field_value, i,store_object);
											} else {
												Array.set(field_value, i,null);
											}
										}									
									} else {
										for(int i=0 ; i< values.length ; i++) {
											if((values[i] != null) && !(values[i].equals(""))) {
												Array.set(field_value, i, Util.tryParse(field.getType().getComponentType(),values[i]));
											} else {
												Array.set(field_value, i, null);
											}
										}
									} 
								} else if (field.getType().isAssignableFrom(ArrayList.class)) {   //NR: DB will contain pipe-seperated string values in case of Array List
									String values_pipe_list = String.valueOf((results_cursor.getString(column_index)));
									String[] values = values_pipe_list.split("|");
									
									field_value = field.getType().getComponentType().newInstance();  //NR: Create new instance of ArrayList
									Method add = ArrayList.class.getDeclaredMethod("add",Object.class); //NR: Get refrence to Add Method
									//NR If array of IStorageProvider
									if(IStorageProvider.class.isAssignableFrom(field.getType().getComponentType())) {
										for(int i=0 ; i< values.length ; i++) {
											if((values[i] != null) && !(values[i].equals(""))) {
												IStorageProvider store_object = (IStorageProvider) field.getType().newInstance();
												store_object.LoadData(Long.valueOf(values[i]).longValue());
												add.invoke(field_value, store_object);
											} else {
												add.invoke(field_value, (Object)null);
											}
										}									
									} else {
										for(int i=0 ; i< values.length ; i++) {
											if((values[i] != null) && !(values[i].equals(""))) {
												add.invoke(field_value, Util.tryParse(field.getType().getComponentType(),values[i]));
											} else {
												add.invoke(field_value, (Object)null);
											}
										}
									} 
								} else if(field.getType() == String.class) {
									field_value = results_cursor.getString(column_index);
									
								} else if(field.getType() == DateTime.class) {
									field_value = Util.convertDBFormatToDateTime(results_cursor.getLong(column_index));
									
								}else if(field.getType().isEnum()) {
									//NR cast the value to Enum
									field_value = Enum.valueOf((Class<Enum>) field.getType(), results_cursor.getString(column_index));  //NR: un-safe type casting, but we are already sure about Enum => String in DB 
								} else if(IStorageProvider.class.isAssignableFrom(field.getType().getClass())) {   // If Foreign Key
									//NR cast the value to IStorageProvider Type and do LoadData based on ForeignKey value from DB.
									IStorageProvider store_object = (IStorageProvider) field.getType().newInstance();
									store_object.LoadData(results_cursor.getLong(column_index));
									field_value = store_object; 
								} else {
									//NR cast the value to proper type
									//NR: This is Risky and Prone to fail.
									Application.LogWarning("Database","DataType not Supported.Trying Generic Parse un-supported Data type");
									field_value = Util.tryParse(field.getType(),results_cursor.getString(column_index));
								}
		
							}
						
							//NR: Set Parsed the field Value to new object
							field.set(result_object,field_value);	
							
						} catch (IllegalAccessException e) {
							Application.LogError("Database", "StorageProvider.select() on column[" + column + "] FAILED :" + e.toString());
						} catch (IllegalArgumentException e) {
							Application.LogError("Database","StorageProvider.select() on column[" + column + "] FAILED :" + e.toString());
						} catch (NoSuchFieldException e) {
							Application.LogError("Database","StorageProvider.select() on column[" + column + "] FAILED :" + e.toString());
						} catch (ClassCastException e) {
							Application.LogError("Database","StorageProvider.select() on column[" + column + "] FAILED :" + e.toString());
						} catch (InstantiationException e) {
							Application.LogError("Database","StorageProvider.select() on column[" + column + "] FAILED :" + e.toString());
						} catch (NoSuchMethodException e) {
							Application.LogError("Database","StorageProvider.select() on column[" + column + "] FAILED :" + e.toString());
						} catch (InvocationTargetException e) {
							Application.LogError("Database","StorageProvider.select() on column[" + column + "] FAILED :" + e.toString());
						}
					}
				
					//NR: Add Entity object to results collection.	
					results.add(result_object);
					
				} catch (InstantiationException ex) {
					Application.LogError("Database", "StorageProvider.select()  FAILED :" + ex.toString());
				} catch (IllegalAccessException ex) {
					Application.LogError("Database", "StorageProvider.select()  FAILED :" + ex.toString());
				}

			}
		}
		
		return results;
	}
	
	public String toString(){
		return String.valueOf(this.getID());
	}
	
	public abstract void OnAfterSave();

	public abstract void OnBeforeSave();

	public abstract void OnAfterInsert();

	public abstract void OnBeforeInsert();

	public abstract void OnBeforeUpdate();

	public abstract void OnAfterUpdate();

	public abstract void OnBeforeDelete();

	public abstract void OnAfterDelete();
}
