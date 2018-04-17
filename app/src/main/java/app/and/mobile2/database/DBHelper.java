package app.and.mobile2.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    private static DBHelper instance = null;
    private Context context;

    private static final String DB_NAME = "maobile2_database";
    private static final String TABLE_USERS="users", TABLE_LIST="list";
    private static final String USERS_ID = "_id", USERS_NAME="username", USERS_PASS="password";
    public static final String LIST_ID="_id", LIST_USER="user", LIST_URL="url", LIST_APPROVED="approved";


    private static final String CREATE_USERS_TABLE = "create table " +
            TABLE_USERS + " ( " +
            USERS_ID + " integer primary key autoincrement, " +
            USERS_NAME + " text not null, " +
            USERS_PASS + " text not null);",
        CREATE_LIST_TABLE = "create table " +
                TABLE_LIST + " ( " +
                LIST_ID + " integer primary key autoincrement, " +
                LIST_USER + " integer references " + TABLE_USERS+"("+USERS_ID+"), " +
                LIST_URL + " text not null, " +
                LIST_APPROVED + " boolean);";

    private DBHelper(Context context){
        super(context,DB_NAME,null, 1);
        this.context=context;
    }

    public static DBHelper getInstance(Context context) {
        if(instance==null) instance=new DBHelper(context.getApplicationContext());
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_USERS_TABLE);
        sqLiteDatabase.execSQL(CREATE_LIST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public static String register(Context context, String login, String pass){
        SQLiteDatabase database = getInstance(context).getWritableDatabase();
        Cursor cursor = database.rawQuery("select * from " + TABLE_USERS, null);
        boolean result = true;
        String message = "Такой пользователь существует!";
        if(cursor.moveToFirst()){
            do{
                if(cursor.getString(cursor.getColumnIndexOrThrow(USERS_NAME)).equals(login)) {
                    result = false; break;
                }
            }while (cursor.moveToNext());
        }
        if(!result) return message;
        ContentValues cv = new ContentValues();
        cv.put(USERS_NAME, login);
        cv.put(USERS_PASS, pass);
        if(database.insert(TABLE_USERS, null, cv)!=-1) message = "Регистрация успешна!";
        else message = "Ошибка базы данных!";
        cursor.close();
        database.close();
        return message;
    }

    public static int login(Context context, String login, String pass){
        SQLiteDatabase database = getInstance(context).getReadableDatabase();
        Cursor cursor = database.rawQuery("select * from " + TABLE_USERS + " where " + USERS_NAME + " = '" + login + "'", null);
        int result;
        if(cursor.moveToFirst()){
            if(cursor.getString(cursor.getColumnIndexOrThrow(USERS_PASS)).equals(pass)) result = cursor.getInt(cursor.getColumnIndexOrThrow(USERS_ID));
            else result = -2;
        } else result = -1;
        cursor.close();
        database.close();
        return result;
    }

    public static boolean addToList(Context context, int user_id, String url, boolean approved){
        SQLiteDatabase database = getInstance(context).getWritableDatabase();
        ContentValues cv = new ContentValues();
        boolean result = false;
        cv.put(LIST_USER, user_id);
        cv.put(LIST_URL, url);
        cv.put(LIST_APPROVED, approved);
        if(database.insert(TABLE_LIST, null, cv)!=-1) result = true;
        database.close();
        return result;
    }

    public static boolean isInList(Context context, int user_id, String url){
        SQLiteDatabase database = getInstance(context).getReadableDatabase();
        Cursor cursor = database.rawQuery("select * from " + TABLE_LIST + " where " + LIST_USER + " = " + user_id + " and " + LIST_URL + " = '" + url + "'", null);
        boolean result = false;
        if(cursor.moveToFirst()) result = true;
        cursor.close();
        database.close();
        return result;
    }

    public static Cursor getListCursor(Context context, int user_id, boolean approved){
        String app = approved ? "1" : "0";
        SQLiteDatabase database = getInstance(context).getReadableDatabase();
        Cursor cursor = database.rawQuery("select * from " + TABLE_LIST + " where " + LIST_USER + " = " + user_id + " and " + LIST_APPROVED + " = " + app +" order by " + LIST_ID + " desc", null);
        return cursor;
    }
}
