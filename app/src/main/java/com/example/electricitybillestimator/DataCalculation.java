package com.example.electricitybillestimator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataCalculation extends SQLiteOpenHelper {

    private static final String DATABASE_NAME =
            "electricity.db";

    private static final int DATABASE_VERSION = 1;

    public DataCalculation(Context context) {
        super(context,
                DATABASE_NAME,
                null,
                DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql = "CREATE TABLE bill(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "month TEXT," +
                "unitUsed INTEGER," +
                "rebate REAL," +
                "totalCharge REAL," +
                "finalCost REAL)";

        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db,
                          int oldVersion,
                          int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS bill");
        onCreate(db);
    }

    // Insert data
    public boolean insertData(
            String month,
            int unitUsed,
            double rebate,
            double totalCharge,
            double finalCost
    ) {

        SQLiteDatabase db =
                this.getWritableDatabase();

        ContentValues values =
                new ContentValues();

        values.put("month", month);
        values.put("unitUsed", unitUsed);
        values.put("rebate", rebate);
        values.put("totalCharge", totalCharge);
        values.put("finalCost", finalCost);

        long result =
                db.insert("bill",
                        null,
                        values);

        return result != -1;
    }

    // Get all data
    public Cursor getAllData() {

        SQLiteDatabase db =
                this.getReadableDatabase();

        return db.rawQuery(
                "SELECT * FROM bill",
                null
        );
    }

    // Get data by id (for history)
    public Cursor getDataById(int id) {

        SQLiteDatabase db =
                this.getReadableDatabase();

        return db.rawQuery(
                "SELECT * FROM bill WHERE id = ?",
                new String[]{
                        String.valueOf(id)
                }
        );
    }

    // Delete data
    public boolean deleteData(int id) {

        SQLiteDatabase db =
                this.getWritableDatabase();

        long result =
                db.delete(
                        "bill",
                        "id=?",
                        new String[]{
                                String.valueOf(id)
                        }
                );

        return result > 0;
    }

    // Update data
    public boolean updateData(
            int id,
            String month,
            int unitUsed,
            double rebate,
            double totalCharge,
            double finalCost
    ) {

        SQLiteDatabase db =
                this.getWritableDatabase();

        ContentValues values =
                new ContentValues();

        values.put("month", month);
        values.put("unitUsed", unitUsed);
        values.put("rebate", rebate);
        values.put("totalCharge", totalCharge);
        values.put("finalCost", finalCost);

        int result =
                db.update(
                        "bill",
                        values,
                        "id=?",
                        new String[]{
                                String.valueOf(id)
                        }
                );

        return result > 0;
    }

}