package caso.victor_arroyo.dropboxepubs;

import java.sql.Date;
import java.util.ArrayList;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBManager extends SQLiteOpenHelper{
	public DBManager(Context context) {
		super(context, "DropboxEpubs", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE IF NOT EXISTS epubs (nombre TEXT NOT NULL, nombreArchivo TEXT NOT NULL, fecha TIMESTAMP NOT NULL DEFAULT current_timestamp, dbPath TEXT NOT NULL, cachePath TEXT NOT NULL);");
		Log.d("DBManager", "onCreate: epubs db table created");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		db.execSQL("DROP TABLE IF EXISTS epubs");
		db.execSQL("CREATE TABLE IF NOT EXISTS epubs (nombre TEXT NOT NULL, nombreArchivo TEXT NOT NULL, fecha TIMESTAMP NOT NULL DEFAULT current_timestamp, dbPath TEXT NOT NULL, cachePath TEXT NOT NULL);");
		Log.d("DBManager", "onUpgrade: Epubs db table created");
	}
	
	/**
	 * This method tries to insert a contact into "epubs" table in data base
	 * 
	 * @param libro
	 *            Epub whose fields values will be inserted in data base
	 * @return "true" if epub is successfully inserted, "false" if the
	 *         epub is not inserted
	 * @see Epub
	 */
	public boolean addEpub(Epub libro) {
		boolean ret = false;
		SQLiteDatabase db = this.getWritableDatabase();
		try {
			// Create a values container with epub information specified in
			// parameter
			ContentValues cValues = new ContentValues();
			cValues.put("nombre", libro.getNombre());
			cValues.put("nombreArchivo", libro.getNombreArchivo());
			cValues.put("fecha", libro.getFecha().toString());
			cValues.put("dbPath", libro.getDbPath());
			cValues.put("cachePath", libro.getCachePath());

			// Calling to insert method with values container
			long res = db.insertOrThrow("epubs", null, cValues);
			// It returns row number where epub is inserted
			db.close();
			if (res == -1) {
				// Epub cannot be inserted
				Log.d("DBManager", "addEpub: Epub libro: "
						+ libro.getNombre() + " could not be inserted");
				ret = false;
			} else {
				// Contact is successfully inserted in "res" row
				Log.d("DBManager", "addEpub: Epub libro: "
						+ libro.getNombre() + " was successfully inserted");
				ret = true;
			}
		} catch (SQLiteConstraintException e) {
			Log.e("Already Stored", "true");
			ret = true;
		} catch (SQLException e) {
			Log.d("DBManager",
					"addEpub: Epub libro: " + libro.getNombre()
							+ " could not be inserted");
			Log.d("DBManager", "addEpub: Exception: " + e.getMessage());
			ret = false;
		} finally {
			db.close();
			ret = false;
		}
		return ret;
	}

	/**
	 * This method execute a select sentence in data base to get all epubs in
	 * "epubs" table
	 * 
	 * @return epubs list with extracted values from select sentence
	 * @see Epub
	 */
	public ArrayList<Epub> getEpubsList() {
		SQLiteDatabase db = this.getWritableDatabase();
		// Creating a new product list
		ArrayList<Epub> acl = new ArrayList<Epub>();
		// Execute query specified in first parameter
		Cursor cursor = db.rawQuery(
				"SELECT nombre, nombreArchivo," +
				         " (strftime('%s', fecha) * 1000) AS fecha," +
				         " dbPath, cachePath" +
				         " FROM epubs ORDER BY nombre COLLATE NOCASE ASC",
				null);
		// Getting first result
		cursor.moveToFirst();
		if ((cursor != null) && (cursor.getCount() > 0)) {
			do {
				// Parsing values of a result
				String sNombre = cursor.getString(0);
				String sNombreArchivo = cursor.getString(1);
				long millis = cursor.getLong(cursor.getColumnIndexOrThrow("fecha"));
				Date fecha = new Date(millis);
				String sDbPath = cursor.getString(3);
				String sCachePath = cursor.getString(4);
				// Creating a new epub using obtained values
				Epub cnt = new Epub(sNombre, fecha, sNombreArchivo,
						sDbPath, sCachePath);
				
				 Log.d("DBManager", "getEpubsList: Epub libro: " +
				 sNombre + " extracted from db");
				 
				// Adding new epub to list
				acl.add(cnt);
			}
			// Getting next result
			while (cursor.moveToNext());
		}
		if (db != null) {
			db.close();
		}
		return acl;
	}
	
	/**
	 * This method execute a select sentence in data base to get all epubs in
	 * "epubs" table ordered by ascendent date
	 * 
	 * @return epubs list with extracted values from select sentence
	 * @see Epub
	 */
	public ArrayList<Epub> getEpubsListFechaAsc() {
		SQLiteDatabase db = this.getWritableDatabase();
		// Creating a new product list
		ArrayList<Epub> acl = new ArrayList<Epub>();
		// Execute query specified in first parameter
		Cursor cursor = db.rawQuery(
				"SELECT nombre, nombreArchivo," +
				         " (strftime('%s', fecha) * 1000) AS fecha," +
				         " dbPath, cachePath" +
				         " FROM epubs ORDER BY fecha ASC",
				null);
		// Getting first result
		cursor.moveToFirst();
		if ((cursor != null) && (cursor.getCount() > 0)) {
			do {
				// Parsing values of a result
				String sNombre = cursor.getString(0);
				String sNombreArchivo = cursor.getString(1);
				long millis = cursor.getLong(cursor.getColumnIndexOrThrow("fecha"));
				Date fecha = new Date(millis);
				//String sFecha = cursor.getString(2);
				String sDbPath = cursor.getString(3);
				String sCachePath = cursor.getString(4);
				// Creating a new epub using obtained values
				Epub cnt = new Epub(sNombre, fecha, sNombreArchivo,
						sDbPath, sCachePath);
				
				 Log.d("DBManager", "getEpubsListFechaAsc: Epub libro: " +
				 sNombre + " extracted from db");
				 
				// Adding new epub to list
				acl.add(cnt);
			}
			// Getting next result
			while (cursor.moveToNext());
		}
		if (db != null) {
			db.close();
		}
		return acl;
	}
	
	/**
	 * This method execute a select sentence in data base to get all epubs in
	 * "epubs" table ordered by descendent date
	 * 
	 * @return epubs list with extracted values from select sentence
	 * @see Epub
	 */
	public ArrayList<Epub> getEpubsListFechaDesc() {
		SQLiteDatabase db = this.getWritableDatabase();
		// Creating a new product list
		ArrayList<Epub> acl = new ArrayList<Epub>();
		// Execute query specified in first parameter
		Cursor cursor = db.rawQuery(
				"SELECT nombre, nombreArchivo," +
				         " (strftime('%s', fecha) * 1000) AS fecha," +
				         " dbPath, cachePath" +
				         " FROM epubs ORDER BY fecha DESC",
				null);
		// Getting first result
		cursor.moveToFirst();
		if ((cursor != null) && (cursor.getCount() > 0)) {
			do {
				// Parsing values of a result
				String sNombre = cursor.getString(0);
				String sNombreArchivo = cursor.getString(1);				
				long millis = cursor.getLong(cursor.getColumnIndexOrThrow("fecha"));
				Date fecha = new Date(millis);
				//String sFecha = cursor.getString(2);
				String sDbPath = cursor.getString(3);
				String sCachePath = cursor.getString(4);
				// Creating a new epub using obtained values
				Epub cnt = new Epub(sNombre, fecha, sNombreArchivo,
						sDbPath, sCachePath);
				
				 Log.d("DBManager", "getEpubsListFechaDesc: Epub libro: " +
				 sNombre + " extracted from db");
				 
				// Adding new epub to list
				acl.add(cnt);
			}
			// Getting next result
			while (cursor.moveToNext());
		}
		if (db != null) {
			db.close();
		}
		return acl;
	}
	
	/**
	 * This method execute a delete sentence in data base 
	 * 
	 * @return true if list was deleted successfully 
	 * false if an error happened
	 */
	public boolean deleteEpubsTable() {
		SQLiteDatabase db = this.getWritableDatabase();
		boolean ret = false;
		// Calling to delete method
		int res = db.delete("epubs", "nombre <> 1", null);
		// It returns the number of rows affected
		db.close();
		if (res >= 1) {
			// The epub was successfully deleted
			Log.d("DBManager", "deleteEpubsTable: " + res + " rows affected");
			ret = true;
		} else if (res == 0) {
			// The epub cannot be deleted
			Log.d("DBManager", "deleteEpubsTable: " + res + " rows affected");
			ret = false;
		}
		return ret;
	}

}
