package app.and.mobile2.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    private static DBHelper instance = null;
    private Context context;

    private static final String DB_NAME = "maobile2_database";
    private static final String TABLE_USERS="users", TABLE_LIST="list";
    private static final String USERS_ID = "_id", USERS_NAME="username", USERS_PASS="password";
    private static final String LIST_ID="_id", LIST_USER="user", LIST_URL="url", LIST_APPROVED="approved";


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
}
