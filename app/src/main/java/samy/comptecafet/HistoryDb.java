package samy.comptecafet;

/**
 * Created by samy on 17/04/17.
 */
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcelable;
import android.util.Log;

public class HistoryDb {

    class Row extends Object {

        private long _Id;
        private String date;
        private String type;
        private String prix;
        private String liste;

        public long get_Id() {

            return _Id;
        }

        public String getDate() {

            return date;
        }

        public String getType() {
            return type;
        }

        public String getPrix() {
            return prix;
        }

        public String getListe() {
            return liste;
        }

    }

    private static final String DATABASE_NAME = "HISTORYDB";
    private static final String DATABASE_TABLE = "OPERATIONS";
    private static final String DATABASE_CREATE =
            "create table " + DATABASE_TABLE + "(_id integer primary key autoincrement, "
                    + "date text not null, "
                    + "type text not null, "
                    + "prix text not null, "
                    + "liste text"
                    +");";

    private SQLiteDatabase db;

    public HistoryDb(Context context) {
        db = context.openOrCreateDatabase(DATABASE_NAME, Context.MODE_PRIVATE, null);
        try {
            db.execSQL(DATABASE_CREATE);
        } catch (SQLException e) {
            Log.d("SQL", "Base de données existante");
        }
    }

    public void close() {
        db.close();
    }

    public long createRow(String date, String type, String prix, String liste) {
        ContentValues initialValues = new ContentValues();
        initialValues.put("date", date);
        initialValues.put("type", type);
        initialValues.put("prix", prix);
        initialValues.put("liste", liste);
        return db.insert(DATABASE_TABLE, null, initialValues);
    }

    public void deleteRow(long rowId) {
        db.delete(DATABASE_TABLE, "_id=" + rowId, null);
    }

    public List<Row> fetchAllRows() {
        ArrayList<Row> ret = new ArrayList<Row>();
        try {
            Cursor c = db.rawQuery("select * from " + DATABASE_TABLE, null);
            int numRows = c.getCount();
            c.moveToFirst();
            for (int i = 0; i < numRows; ++i) {
                Row row = new Row();
                row._Id = c.getLong(0);
                row.date = c.getString(1);
                row.type = c.getString(2);
                row.prix = c.getString(3);
                row.liste = c.getString(4);
                ret.add(row);
                c.moveToNext();
            }
        } catch (SQLException e) {
            Log.e("Exception on query", e.toString());
        }
        return ret;
    }

    public Row fetchRow(long rowId) {
        Row row = new Row();
        Cursor c = db.rawQuery("select * from " + DATABASE_TABLE, null);
        if (c.getCount() > 0) {
            c.moveToFirst();
            row._Id = c.getLong(0);
            row.date = c.getString(1);
            row.type = c.getString(2);
            row.prix = c.getString(3);
            row.liste = c.getString(4);
            return row;
        } else {
            row._Id = -1;
            row.date = row.type = row.prix = row.liste = null;
        }
        c.close();
        return row;
    }

    public void updateRow(long rowId, String date, String type, String prix, String liste) {
        ContentValues args = new ContentValues();
        args.put("date", date);
        args.put("type", type);
        args.put("prix", prix);
        args.put("liste", liste);
        db.update(DATABASE_TABLE, args, "_id=" + rowId, null);
    }

    public Cursor GetAllRows() {
        try {
            return db.rawQuery("select * from " + DATABASE_TABLE, null);
        } catch (SQLException e) {
            Log.e("Exception on query", e.toString());
            return null;
        }
    }
}
