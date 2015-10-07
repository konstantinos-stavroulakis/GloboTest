package gr.apphub.globotest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

/**
 * Created by Konstantinos on 06/10/15.
 */
public class DatabaseActivity {
    private Context ourContext;
    private DBHelper ourHelper;
    private SQLiteDatabase ourDatabase;

    public static class DBHelper extends SQLiteOpenHelper {

        static final String DATABASE_NAME = "cards.db";
        static final String DATABASE_TABLE = "cards_tbl";

        static final String ROWID = "_id";
        static final String CARDID = "cardId";
        static final String NAME = "name";
        static final String CARDSET = "cardSet";
        static final String TYPE = "type";
        static final String FACTION = "faction";
        static final String RARITY = "rarity";
        static final String COST = "cost";
        static final String ATTACK = "attack";
        static final String HEALTH = "health";
        static final String TEXT = "text";
        static final String ARTIST = "artist";
        static final String COLLECTIBLE = "collectible";
        static final String ELITE = "ELITE";
        static final String IMG = "img";
        static final String IMGGOLD = "imggold";
        static final String LOCALE = "locale";
        static final String MECHANICS = "mechanics";
        static final String HOWTOGET = "howToGet";
        static final String FLAVOR = "flavor";


        public DBHelper(Context context) {
            super(context, DATABASE_NAME, null, 1);
        }

        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + DATABASE_TABLE + " (" + ROWID
                    + " INTEGER PRIMARY KEY AUTOINCREMENT, " + CARDID
                    + " TEXT , " + NAME + " TEXT ,  " + CARDSET + " TEXT ,  " + TYPE + " TEXT ,  " + FACTION + " TEXT ,  "
                    + RARITY + " TEXT ,  " + COST + " TEXT ,  " + ATTACK + " TEXT ,  " + HEALTH + " TEXT ,  " + TEXT + " TEXT ,  "
                    + ARTIST + " TEXT ,  " + COLLECTIBLE + " TEXT ,  " + ELITE + " TEXT ,  " + IMG + " TEXT ,  " + IMGGOLD + " TEXT ,  "
                    + LOCALE + " TEXT ,  " + MECHANICS + " TEXT ,  " + HOWTOGET + " TEXT ,  " + FLAVOR + " TEXT);");

        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            android.util.Log.w("Constants",
                    "Upgrading database, which will destroy all data");
            db.execSQL("DROP TABLE IF EXISTS " + DBHelper.DATABASE_TABLE);
            onCreate(db);

        }
    }

    public DatabaseActivity(Context c) {
        ourContext = c;

    }

    public DatabaseActivity open() throws SQLException {
        ourHelper = new DBHelper(ourContext);
        ourDatabase = ourHelper.getWritableDatabase();
        return this;

    }

    public void close() {
        ourHelper.close();
    }

    public void createEntry(String cardid, String name, String cardset, String type, String faction, String rarity, String cost, String ATTACK, String health, String text, String artist, String collectible, String ELITE, String img, String imggold, String locale, String mechanics,String howToGet,String flavor) {

        try {
            ContentValues cv = new ContentValues();
            cv.put(DBHelper.CARDID, cardid);
            cv.put(DBHelper.NAME, name);
            cv.put(DBHelper.CARDSET, cardset);
            cv.put(DBHelper.TYPE, type);
            cv.put(DBHelper.FACTION, faction);
            cv.put(DBHelper.RARITY, rarity);
            cv.put(DBHelper.COST, cost);
            cv.put(DBHelper.ATTACK, ATTACK);
            cv.put(DBHelper.HEALTH, health);
            cv.put(DBHelper.TEXT, text);
            cv.put(DBHelper.ARTIST, artist);
            cv.put(DBHelper.COLLECTIBLE, collectible);
            cv.put(DBHelper.ELITE, ELITE);
            cv.put(DBHelper.IMG, img);
            cv.put(DBHelper.IMGGOLD, imggold);
            cv.put(DBHelper.LOCALE, locale);
            cv.put(DBHelper.MECHANICS, mechanics);
            cv.put(DBHelper.HOWTOGET, howToGet);
            cv.put(DBHelper.FLAVOR, flavor);


            ourDatabase.insert(DBHelper.DATABASE_TABLE, null, cv);

        } catch (Exception e) {
            Log.e("Exception in insert :", e.toString());
            e.printStackTrace();
        }
    }

      public Cursor getData() {

        String[] columns = new String[]{DBHelper.ROWID, DBHelper.CARDID, DBHelper.NAME, DBHelper.CARDSET, DBHelper.TYPE, DBHelper.FACTION, DBHelper.RARITY, DBHelper.COST
                , DBHelper.ATTACK, DBHelper.HEALTH, DBHelper.TEXT, DBHelper.ARTIST, DBHelper.COLLECTIBLE, DBHelper.ELITE, DBHelper.IMG, DBHelper.IMGGOLD, DBHelper.LOCALE, DBHelper.MECHANICS,DBHelper.HOWTOGET,DBHelper.FLAVOR};
        Cursor c = ourDatabase.query(DBHelper.DATABASE_TABLE, columns, null,
                null, null, null,null);
        return c;
    }

    public Cursor getItemByCardId(String cardid) {

        Cursor cursor = ourDatabase.rawQuery("select * from "
                + DBHelper.DATABASE_TABLE + " where " + DBHelper.CARDID + " =?"
                , new String[]{cardid});
        return cursor;
    }
    public String getCardIdFromPosition(int position){
        String[] columns = new String[]{DBHelper.ROWID, DBHelper.CARDID, DBHelper.NAME, DBHelper.CARDSET, DBHelper.TYPE, DBHelper.FACTION, DBHelper.RARITY, DBHelper.COST
                , DBHelper.ATTACK, DBHelper.HEALTH, DBHelper.TEXT, DBHelper.ARTIST, DBHelper.COLLECTIBLE, DBHelper.ELITE, DBHelper.IMG, DBHelper.IMGGOLD, DBHelper.LOCALE, DBHelper.MECHANICS,DBHelper.HOWTOGET,DBHelper.FLAVOR};
        Cursor c = ourDatabase.query(DBHelper.DATABASE_TABLE, columns, null,
                null, null, null,null);
        c.moveToPosition(position);
        return c.getString(c.getColumnIndex(DBHelper.CARDID));

    }

    //metraei tis eggrafes tis vasis

    long fetchPlacesCount() {
        String sql = "SELECT COUNT(*) FROM " + DBHelper.DATABASE_TABLE;
        SQLiteStatement statement = ourDatabase.compileStatement(sql);
        long count = statement.simpleQueryForLong();
        return count;
    }


    public int deleteAllall() {

        return ourDatabase.delete(DBHelper.DATABASE_TABLE, null, null);
    }


    public boolean Exists(String cardid,String name) {
        Cursor cursor = ourDatabase.rawQuery("select 1 from "
                + DBHelper.DATABASE_TABLE + " where " + DBHelper.NAME + "=? and "+DBHelper.CARDID+" =?"
                , new String[]{name,cardid});


        if (cursor.moveToFirst()) {
            boolean exists = (cursor.getCount() > 0);
            cursor.close();
            return exists;
        } else {
            return false;
        }

    }
}// end mainclass
