package test.com.contactsapplication.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import test.com.contactsapplication.entity.ContactsInfoEntity;

/**
 * Created by Gaurav.Arora on 29-05-2017.
 */

public class ContactsDbHelper extends SQLiteOpenHelper {
    private static String mDatabaseName = "contacts.db";
    private static int mDatabaseVersion = 1;
    public static final String TABLE_NAME = "ContactsTable";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_UUID = "uuid";
    public static final String COLUMN_DELETED_FLAG = "deletedFlag";
    private static ContactsDbHelper sDbHelperInstance;

    private static String SQL_CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_UUID + " INTEGER PRIMARY KEY," +
                    COLUMN_NAME + " TEXT," +
                    COLUMN_DELETED_FLAG + " INTEGER DEFAULT 0)";

    private ContactsDbHelper(Context context) {
        super(context, mDatabaseName, null, mDatabaseVersion);
    }

    public static ContactsDbHelper getDbHelperInstance(Context context) {
        if (sDbHelperInstance == null) {
            sDbHelperInstance = new ContactsDbHelper(context);
        }
        return sDbHelperInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // Drop older table if existed
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        // Create tables again
        onCreate(sqLiteDatabase);
    }

    public void addContactsIntoDB(List<ContactsInfoEntity> contactsInfoList) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        for (ContactsInfoEntity contactsInfo : contactsInfoList) {
            values.put(COLUMN_NAME, contactsInfo.getName());
            values.put(COLUMN_UUID, contactsInfo.getUid());
            db.insertWithOnConflict(TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        }
        db.close();
    }

    public List<ContactsInfoEntity> getContactsFromDB() {
        List<ContactsInfoEntity> contactsList = new ArrayList<ContactsInfoEntity>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{COLUMN_UUID, COLUMN_NAME}, COLUMN_DELETED_FLAG + "=?", new String[]{String.valueOf(0)}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                ContactsInfoEntity contactsEntity = new ContactsInfoEntity();
                contactsEntity.setUid(cursor.getString(0));
                contactsEntity.setName(cursor.getString(1));
                contactsList.add(contactsEntity);
            } while (cursor.moveToNext());
        }
        return contactsList;
    }

    public void deleteContactsFromDB(ContactsInfoEntity contactsInfo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_DELETED_FLAG, 1);
        db.update(TABLE_NAME, values, COLUMN_UUID + " = ?", new String[]{contactsInfo.getUid()});
    }

    public boolean isDBExists(Context context) {
        File database = context.getDatabasePath(mDatabaseName);
        return database.exists();
    }
}
