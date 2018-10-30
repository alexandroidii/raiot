package ca.raiot.cst2335.raiot;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DeviceDatabaseHelper extends SQLiteOpenHelper {
    protected static final String ACTIVITY_NAME = "1234 DeviceDatabaseHelper";

    private static final String DATABASE_NAME = "Devices.db";
    private static final int VERSION_NUM = 1;
    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "name";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_ICON = "icon";
    public static final String KEY_TYPE = "type";
    public static final String TABLE_NAME = "Devices";

    private static final String SQL_CREATE_ENTRIES =
            "create table " + TABLE_NAME + " (" + KEY_ID + " integer primary key autoincrement, "
                    + KEY_NAME + " text "
                    + KEY_DESCRIPTION + " text "
                    + KEY_ICON + " text "
                    + KEY_TYPE + " text)";

    private static final String SQL_DROP_ENTRIES = "drop table if exists " + TABLE_NAME;

    public DeviceDatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, VERSION_NUM);
    }

    @Override public void onCreate(SQLiteDatabase db) {
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
}