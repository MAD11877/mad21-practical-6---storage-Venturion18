package main_activity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Random;

public class DBHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Prac6Database";

    public DBHandler(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE User (name TEXT, description TEXT, id INTEGER PRIMARY KEY AUTOINCREMENT, followed INTEGER)";
        db.execSQL(CREATE_USERS_TABLE);

        for(int i=0; i<20; i++)
        {
            ContentValues values = new ContentValues();
            values.put("name", "Name" + new Random().nextInt());
            values.put("description","Description " + new Random().nextInt());
            values.put("followed", new Random().nextInt()%2 == 0);
            db.insert("User", null, values);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS User");
        onCreate(db);
    }

    public ArrayList<User> getUsers()
    {
        ArrayList<User> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM User";
        Cursor cursor = db.rawQuery(query, null);

        while(cursor.moveToNext())
        {
            User u = new User();
            u.name = cursor.getString(0);
            u.description = cursor.getString(1);
            u.id = cursor.getInt(2);
            u.followed = cursor.getInt(3) != 0;

            list.add(u);
        }
        cursor.close();
        db.close();
        return list;
    }

    public void updateUser(User u)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("followed", u.followed);
        int count = db.update("User", values, "id = ?", new String[]{ "" + u.id });

        db.close();
    }
}
