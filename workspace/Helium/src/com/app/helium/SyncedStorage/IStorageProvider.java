package com.app.helium.SyncedStorage;

import java.util.ArrayList;

public interface IStorageProvider {
	
	public ArrayList<String> Columns();
	public ArrayList<String> ColumnOptions();
	public boolean Insert();
	public boolean Update();
	public boolean Delete();
	public boolean Delete(String condition);
	public void LoadData(long id);
	// public void LoadData(Cursor cursor);  //NR: TODO: fill data via cursor, This  will save lots of LOC
	public long getID();
	public String toString();
	public String getTableName();
	public void OnAfterSave();
	public void OnBeforeSave();
	public void OnAfterInsert();
	public void OnBeforeInsert();
	public void OnBeforeUpdate();
	public void OnAfterUpdate();
	public void OnBeforeDelete();
	public void OnAfterDelete();

}
