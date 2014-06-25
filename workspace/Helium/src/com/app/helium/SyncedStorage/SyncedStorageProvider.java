package com.app.helium.SyncedStorage;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import org.joda.time.DateTime;
import org.json.JSONException;
import org.json.JSONObject;

import com.app.helium.Application;
import com.app.helium.Helper.Util;
import android.database.Cursor;

public abstract class SyncedStorageProvider extends StorageProvider implements ISyncAdapter {

//	public ArrayList<String> table_columns;
//	public ArrayList<String> table_column_options;
//	private String table_name;
//	private String databaseName;
//	private Context application_context;
//	protected DatabaseQuery Database;
//	protected boolean skip_insert_update_events = false;

	public DateTime last_modified_date_time;
	public DateTime last_sync_date_time;
	public String last_sync_error_info;
	public SyncStatus last_sync_status;
	public long remote_id;
	
	public SyncedStorageProvider() {	}
	
	protected void initializeTable(String table_name) {
		this.table_name = table_name;
		this.databaseName = Application.GetDatabaseName();
		this.application_context = Application.GetContext();
		this.table_columns = Columns();
		this.table_column_options = ColumnOptions();
		Database = new DatabaseQuery(application_context,this.databaseName,this.table_name,this.table_columns,this.table_column_options);
	}
	
	public ArrayList<String> Columns() {
		ArrayList<String> columns = new ArrayList<String>();
		columns.add("last_modified_date_time");
		columns.add("last_sync_date_time");
		columns.add("last_sync_error_info");
		columns.add("last_sync_status");
		columns.add("remote_id");
		//columns.add("id");  //NR:id not in column collection : This column defined in DBAdapter
		return columns;
	}

	public ArrayList<String> ColumnOptions() {
		ArrayList<String> column_options = new ArrayList<String>();
		column_options.add("INTEGER");
		column_options.add("INTEGER");
		column_options.add("TEXT");
		column_options.add("TEXT");
		column_options.add("INTEGER");
		//column_options.add("INTEGER PRIMARY KEY AUTOINCREMENT");   //NR:id not in column collection : This column defined in DBAdapter
		return column_options;
	}

	@Override
	public boolean Save() {
		this.last_modified_date_time = new DateTime();
		return super.Save();
	}
	
	@Override
	public boolean Insert() {
		boolean insert_succeeded = false;
		this.last_modified_date_time = new DateTime();
		insert_succeeded = super.Insert();
		//if(insert_succeeded) {
		//	SyncManager.registerSync(this);
		//}
		return insert_succeeded;
	}
	
	@Override
	public boolean Update() {
		boolean update_succeeded = false;
		this.last_modified_date_time = new DateTime();
		update_succeeded = super.Update();
		//if(update_succeeded) {
		//	SyncManager.registerSync(this);
		//}
		return update_succeeded;
	}
	
	
	public long getRemoteID() {
		return this.remote_id;
	}
	
	public long getID(long remote_id) {
		long row_id=0;
		Cursor data_cursor = Database.getDataCursor(new String[] {"id"}, "remote_id=?", new String[]{String.valueOf(remote_id)}, "", "","","asc");
		if(data_cursor != null) {
			while(data_cursor.moveToNext()) {
					int column_index = data_cursor.getColumnIndex("id") , column_data_type;
					if(column_index < 0) {
						column_data_type = Cursor.FIELD_TYPE_NULL;  //NR: if column not existing, mark it null
					} else {
						column_data_type = data_cursor.getType(column_index);  //NR: if exists, get its type
					}
					if(column_data_type != Cursor.FIELD_TYPE_NULL) {
						row_id = data_cursor.getLong(column_index);
					}
			}
		}
		return row_id;
		
	}
	
	@Override
	public void updateSyncInformation(long remote_record_id, SyncStatus sync_status, String sync_error_info ) {
		// NR: fire raw update to skip triggering IStorageProvider events as this is internal data & not user data
		String sync_date_time = String.valueOf(Util.convertDateTimeToDBFormat(new DateTime()));
		String update_query;
		if(remote_record_id > 0) {
			update_query = "UPDATE " + this.getTableName() + " SET last_sync_date_time = '" + sync_date_time + "' , last_sync_status = '" + sync_status.toString()  + "' , remote_id = '" + String.valueOf(remote_record_id) +  "' , last_sync_error_info = '" + sync_error_info + "'  WHERE id = " + String.valueOf(this.id) ;
		} else {
			update_query = "UPDATE " + this.getTableName() + " SET last_sync_date_time = '" + sync_date_time + "' , last_sync_status = '" + sync_status.toString()  + "' , last_sync_error_info = '" + sync_error_info + "'  WHERE id = " + String.valueOf(this.id) ;
		}
		Database.executeRawQuery(update_query, null);
	}
	
	@Override
	public void updateSyncInformation(SyncStatus last_sync_status) {
		this.updateSyncInformation(0,last_sync_status,"none");
	}
	
	@Override
	public JSONObject getJSON() throws ReferenceObjectNotSyncedException {
		JSONObject json_object = new JSONObject();
		String[] columns = this.getAllColumns();
		String column;
		for(int col_index = 0 ; col_index < columns.length ; col_index++) {
			Object field_value = null;
			column = columns[col_index];
			long refrence_remote_id;
			String batch_id;
			try {
				Field field = this.getClass().getField(column);
				field_value = field.get(this);
				String values_pipe_list = "";
				if(field_value == null) {
					json_object.put(column, null);
				} else if((field.getType().isArray() && (!(field.getClass().getComponentType().isPrimitive()))) || field.getType().isAssignableFrom(ArrayList.class)) {
					if(field.getType().isArray()) {
						for(int i = 0 ; i < Array.getLength(field_value) ; i++){
							String string_field_value = ((Object[])field_value)[i].toString();
							if(string_field_value != "") {
								values_pipe_list = values_pipe_list + string_field_value + "|";
							}							
						}
					} else if(field.getType().isAssignableFrom(ArrayList.class)) {
						for(Object sub_value : ArrayList.class.cast(field_value)) {
							String string_field_value = sub_value.toString();
							if(string_field_value != "") {
								values_pipe_list = values_pipe_list + string_field_value + "|";
							}
						}
					}
					json_object.put(column, values_pipe_list);
					
				} else if(field.getType().isPrimitive()) {
					json_object.put(column, field_value);
				} else if(field.getType() == String.class) {
					json_object.put(column, field_value);
				} else if(field.getType() == DateTime.class) {
					json_object.put(column, Util.convertDateTimeToDBFormat((DateTime)field_value));
				} else if(SyncedStorageProvider.class.isAssignableFrom(field.getType().getClass())) {
					refrence_remote_id = ((SyncedStorageProvider)field_value).getRemoteID();
					if(refrence_remote_id < 0) {      // refrence not Synced
						//NR: Register Sync for parent object 
						// Or
						// Get already register Sync's Batch_id
						batch_id = SyncManager.registerSync((SyncedStorageProvider)field_value);
						//NR: Register Sync for current type
						SyncManager.registerSync((SyncedStorageProvider)this,batch_id);
						//NR: Throw Exception to Notify Failed Sync
						throw new ReferenceObjectNotSyncedException(((SyncedStorageProvider)field_value).getClass().getName(), ((SyncedStorageProvider)field_value).getID(), batch_id); 
						
					} else {
						json_object.put(column, refrence_remote_id);
					}
				} else if(field.getType().isEnum()) {
					//NR: will handle Enum
					json_object.put(column, field_value.toString());
				} else {
					//NR: will handle non-primitive extended types.
					json_object.put(column, Util.tryParse(field.getType(),field_value.toString()));
				}
				
			} catch (IllegalAccessException e) {
				Application.LogError("Database", "StorageProvider.getJSON() :" + e.toString());
			} catch (IllegalArgumentException e) {
				Application.LogError("Database","StorageProvider.getJSON() :" + e.toString());
			} catch (NoSuchFieldException e) {
				Application.LogError("Database","StorageProvider.getJSON() :" + e.toString());
			} catch (JSONException e) {
				Application.LogError("Database","StorageProvider.getJSON() :" + e.toString());
			}
			
		}
		return json_object;
	}
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void LoadData(JSONObject json_data){
		String[] columns = this.getAllColumns(); //this.table_columns.toArray(new String[this.table_columns.size()]);
		if(json_data != null) {
			String column;
				for(int col_index = 0 ; col_index < columns.length ; col_index++) { //for(String column : this.table_columns) {
					column = columns[col_index];
					Object field_value = null;					
					//NR: Parse the field Value o Field Data Type
					
					try {
						Field field = this.getClass().getField(column);
						
						if(!(json_data.isNull(column))) {
							if(field.getType().isPrimitive() && !(field.getType().isArray())) {
								if(field.getType() == int.class) {
									field_value = json_data.getInt(column);
								} else if(field.getType() == long.class) {
									field_value = json_data.getLong(column);
								} else if(field.getType() == float.class) {
									field_value = json_data.getDouble(column);  // get as double as no provision in JSON
								} else if(field.getType() == double.class) {
									field_value = json_data.getDouble(column);
								} else if(field.getType() == short.class) {
									field_value = json_data.getInt(column);   // get as int as no provision in JSON
								} else if(field.getType() == char.class) {
									field_value = json_data.getString(column).charAt(0);
								} else if(field.getType() == byte.class) {
									field_value = (Byte.valueOf(json_data.getString(column))).byteValue();
								} else if(field.getType() == boolean.class) {
									field_value = json_data.getBoolean(column);   //NR convert boolean to string "true/false"
								}
							} else if (field.getType().isArray()) {   //NR: json string should contain pipe-seperated string values in case of Array
								String values_pipe_list = String.valueOf((json_data.getString(column)));
								String[] values = values_pipe_list.split("|");
								
								field_value = Array.newInstance(field.getType().getComponentType(), values.length);
								
								// If array of IStorageProvider
								if(IStorageProvider.class.isAssignableFrom(field.getType().getComponentType())) {
									for(int i=0 ; i< values.length ; i++) {
										if((values[i] != null) && !(values[i].equals(""))) {
											SyncedStorageProvider store_object = (SyncedStorageProvider) field.getType().newInstance();
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
											Array.set(field_value, i,null);
										}
									}
								} 
							} else if (field.getType().isAssignableFrom(ArrayList.class)) {   //NR: json string should contain pipe-seperated string values in case of Array List
								String values_pipe_list = String.valueOf((json_data.getString(column)));
								String[] values = values_pipe_list.split("|");
								
								field_value = field.getType().getComponentType().newInstance();  //NR: Create new instance of ArrayList
								Method add = ArrayList.class.getDeclaredMethod("add",Object.class); //NR: Get refrence to Add Method
								//NR If array of IStorageProvider
								if(IStorageProvider.class.isAssignableFrom(field.getType().getComponentType())) {
									for(int i=0 ; i< values.length ; i++) {
										if((values[i] != null) && !(values[i].equals(""))) {
											SyncedStorageProvider store_object = (SyncedStorageProvider) field.getType().newInstance();
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
								field_value = json_data.getString(column);
								
							} else if(field.getType() == DateTime.class) {
								field_value = Util.convertDBFormatToDateTime(json_data.getLong(column));
								
							} else if(field.getType().isEnum()) {
								//NR cast the value to Enum
								field_value = Enum.valueOf((Class<Enum>) field.getType(), json_data.getString(column));  //NR: un-safe type casting, but we are already sure about Enum => String in DB 
							} else if(SyncedStorageProvider.class.isAssignableFrom(field.getType().getClass())) {   // If Foreign Key
								//NR cast the value to IStorageProvider Type and do LoadData based on ForeignKey value from DB.
								SyncedStorageProvider store_object = (SyncedStorageProvider) field.getType().newInstance();
								store_object.LoadData(json_data.getLong(column));
								field_value = store_object; 
							} else {
								//NR cast the value to proper type
								//NR: This is Risky and Prone to fail.
								Application.LogWarning("Database","DataType not Supported.Trying Generic Parse un-supported Data type");
								field_value = Util.tryParse(field.getType(),json_data.getString(column));
							}
	
						}
					
						//NR: Set Parsed the field Value
						field.set(this,field_value);	
						
					} catch (IllegalAccessException e) {
						Application.LogError("Database", "StorageProvider.LoadData(JSON) on column[" + column + "] FAILED :" + e.toString());
					} catch (IllegalArgumentException e) {
						Application.LogError("Database","StorageProvider.LoadData(JSON) on column[" + column + "] FAILED :" + e.toString());
					} catch (NoSuchFieldException e) {
						Application.LogError("Database","StorageProvider.LoadData(JSON) on column[" + column + "] FAILED :" + e.toString());
					} catch (ClassCastException e) {
						Application.LogError("Database","StorageProvider.LoadData(JSON) on column[" + column + "] FAILED :" + e.toString());
					} catch (InstantiationException e) {
						Application.LogError("Database","StorageProvider.LoadData(JSON) on column[" + column + "] FAILED :" + e.toString());
					} catch (NoSuchMethodException e) {
						Application.LogError("Database","StorageProvider.LoadData(JSON) on column[" + column + "] FAILED :" + e.toString());
					} catch (InvocationTargetException e) {
						Application.LogError("Database","StorageProvider.LoadData(JSON) on column[" + column + "] FAILED :" + e.toString());
					} catch (JSONException e) {
						Application.LogError("Database","StorageProvider.LoadData(JSON) on column[" + column + "] FAILED :" + e.toString());
					}
				}
				
				//NR: set id for remote_id [significant to  decide if to insert or update] 
				if(!(json_data.isNull("remote_id"))) {
					try {
						this.id = this.getID(json_data.getLong("remote_id"));
					} catch (JSONException e) {
						Application.LogError("Database","StorageProvider.LoadData(JSON) on column[remote_id] FAILED :" + e.toString());
					}
				} else {
					this.id = 0;
				}				
		}
	}
	
	@Override
	public void triggerSync() {
		if(this.getID() > 0) { 
			SyncManager.registerSync(this);
		}
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
