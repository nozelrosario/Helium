package com.app.helium.SyncedStorage;

import java.util.ArrayList;

public interface IStorageProvider {
	
	public ArrayList<String> columns();
	public ArrayList<String> columnOptions();
	public boolean insert();
	public boolean update();
	public boolean delete();
	public boolean delete(String condition);
	public void loadData(long id);
	// public void LoadData(Cursor cursor);  //NR: TODO: fill data via cursor, This  will save lots of LOC
	public long getID();
	public String toString();
	public String getTableName();
	public void onAfterSave();
	public void onBeforeSave();
	public void onAfterInsert();
	public void onBeforeInsert();
	public void onBeforeUpdate();
	public void onAfterUpdate();
	public void onBeforeDelete();
	public void onAfterDelete();

}
