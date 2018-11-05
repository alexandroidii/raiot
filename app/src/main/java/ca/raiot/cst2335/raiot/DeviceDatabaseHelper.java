package ca.raiot.cst2335.raiot;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.Arrays;
import java.util.HashMap;

import static java.sql.Types.REF;

public class DeviceDatabaseHelper extends SQLiteOpenHelper {
    protected static final String ACTIVITY_NAME = "1234 DeviceDatabaseHelper";

    private static final String DATABASE_NAME = "Devices.db";
    private static final int VERSION_NUM = 1;
    public static final String KEY_REF = "ref";
    public static final String KEY_NAME = "name";
    public static final String KEY_LOCATION = "location";
    public static final String KEY_STATUS = "status";

    public static final String TABLE_NAME = "Devices";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" + KEY_REF + " integer primary key, "
                    + KEY_NAME + " text ,"
                    + KEY_LOCATION + " text, "
                    + KEY_STATUS + " text)";

    private static final String SQL_DROP_ENTRIES = "drop table if exists " + TABLE_NAME;

    public DeviceDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION_NUM);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
        Log.i(ACTIVITY_NAME, "Calling onCreate()");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DROP_ENTRIES);
        onCreate(db);
        Log.i(ACTIVITY_NAME, "Calling onUpgrade(), oldVersion = " + oldVersion
                + " newVersion = " + newVersion);
    }

    public boolean addDevice(HashMap<String, String> device, Context context) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_REF, device.get("ref"));
        contentValues.put(KEY_NAME, device.get("name"));
        contentValues.put(KEY_LOCATION, device.get("location"));
        contentValues.put(KEY_STATUS, device.get("status"));

        Log.i(ACTIVITY_NAME, "addDevice(): Adding device " + device.get("name") + " to " + TABLE_NAME);

        long result = db.insert(TABLE_NAME, null, contentValues);

        //if date as inserted incorrectly it will return -1
        return result == -1;

    }

    public boolean updateDevice(HashMap<String, String> device, Context context) {

        // create method to update device's Name and Location but not the ref or Status.
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_REF, device.get("ref"));
        contentValues.put(KEY_NAME, device.get("name"));
        contentValues.put(KEY_LOCATION, device.get("location"));
        contentValues.put(KEY_STATUS, device.get("status"));

        long result = db.update(TABLE_NAME, contentValues, KEY_REF + "=" + device.get("ref"), null);


        Log.i(ACTIVITY_NAME, "updateDevice(): updating device with ref " + device.get("ref") + " from " + TABLE_NAME);


        return result == -1;
    }

    /* Returns all the devices from database
     * @return
     */
    public Cursor getAllSavedDevices() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor data = db.rawQuery(query, null);
        Log.i(ACTIVITY_NAME, "getAllSavedDevices(): Fetching all device from " + TABLE_NAME);
        return data;
    }

    /* Returns selected device from database
     * @return
     */
    public Cursor getSelectedDevice(String REF) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + KEY_REF + " = " + REF;
        Cursor data = db.rawQuery(query, null);
        Log.i(ACTIVITY_NAME, "getSelectedDevices(): Fetching specific device " + REF + " from " + TABLE_NAME);
        return data;
    }

    /* Delete the selected device from database */

    public void deleteDevice(String REF, Context context) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME + " WHERE " + KEY_REF + " = " + REF;
        Log.i(ACTIVITY_NAME, "deleteDevice(): Deleting device " + REF + " from " + TABLE_NAME);
        db.execSQL(query);
    }
}
