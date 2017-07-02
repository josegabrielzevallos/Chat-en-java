package com.example.josegabriel.chatultimo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter {

    static final String KEY_ROWID = "_id";
    static final String KEY_NAME = "name";
    static final String KEY_EMAIL = "email";
    static final String KEY_IDUSU = "id_usu";
    static final String KEY_MENSAJE = "mensaje";
    static final String TAG = "DBAdapter";
    static final String DATABASE_NAME = "MyDB";
    static final String DATABASE_TABLE = "contacts";
    static final String DATABASE_MENSAJES = "mensajes";
    static final int DATABASE_VERSION = 1;
    static final String DATABASE_CREATE =
            "create table contacts (_id integer primary key autoincrement, "
                    + "name text not null, email text not null);";
    static final String DATABASE_CREATE_2 =
            "create table mensajes (_id integer primary key autoincrement, "
                    + "id_usu integer not null, mensaje text not null);";
    final Context context;
    DatabaseHelper DBHelper;
    SQLiteDatabase db;

    public DBAdapter(Context ctx) {

        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context)
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            try {
                db.execSQL(DATABASE_CREATE);
                db.execSQL(DATABASE_CREATE_2);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS contacts");
            onCreate(db);
        }
    }
    //---opens the database--
    public DBAdapter open() throws SQLException {

        db = DBHelper.getWritableDatabase();
        return this;
    }
    //---closes the database--
    public void close() {

        DBHelper.close();
    }

    //---insert a contact into the database--
    public long insertContact(String name, String email) {

        ContentValues initialValues = new ContentValues();

        initialValues.put(KEY_NAME, name);

        initialValues.put(KEY_EMAIL, email);

        return db.insert(DATABASE_TABLE, null, initialValues);
    }

    public long insertMensaje(int idusu, String men) {

        ContentValues initialValues = new ContentValues();

        initialValues.put(KEY_IDUSU, idusu);

        initialValues.put(KEY_MENSAJE, men);

        return db.insert(DATABASE_MENSAJES, null, initialValues);
    }

    //---deletes a particular contact--
    public boolean deleteContact(long rowId) {

        return db.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }

    public boolean deleteMensaje(long rowId) {

        return db.delete(DATABASE_MENSAJES, KEY_ROWID + "=" + rowId, null) > 0;
    }

    //---retrieves all the contacts--
    public Cursor getAllContacts() {

        return db.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_NAME,

                KEY_EMAIL}, null, null, null, null, null);
    }

    //---retrieves a particular contact--
    public Cursor getContact(long rowId) throws SQLException {

        Cursor mCursor =
                db.query(true, DATABASE_TABLE, new String[] {KEY_ROWID,

                                KEY_NAME, KEY_EMAIL},
                        KEY_ROWID + "=" + rowId, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }

        return mCursor;
    }

    public Cursor getMensajes(long IdUsu) throws SQLException {

        Cursor mCursor =
                db.query(true, DATABASE_MENSAJES, new String[] {KEY_ROWID,

                                KEY_IDUSU, KEY_MENSAJE},
                        KEY_IDUSU + "=" + IdUsu, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }

        return mCursor;
    }

    //---updates a contact--
    public boolean updateContact(long rowId, String name, String email) {

        ContentValues args = new ContentValues();

        args.put(KEY_NAME, name);

        args.put(KEY_EMAIL, email);

        return db.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) >
                0;
    }
}