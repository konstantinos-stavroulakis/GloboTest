package gr.apphub.globotest;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Konstantinos on 07/10/15.
 */
public class DownloadService extends IntentService {
    public static final String TRANSACTION_DONE =
            " gr.apphub.globotest.TRANSACTION_DONE";
    private static final String TAG = DownloadService.class.getSimpleName();
    private String url;
    String CARDID, NAME, CARDSET, TYPE, FACTION, RARITY, COST, ATTACH, HEALTH, TEXT, ARTIST, COLLECTIBLE, ELIT, IMG, IMGGOLD, LOCALE, MECHANICS;
    public static final String INPUT_TEXT = "INPUT_TEXT";
    public static final String OUTPUT_TEXT = "OUTPUT_TEXT";

    public DownloadService() {
        super("DownloadService");


    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(TAG, "onHandleIntent()");

        // get inputs from intent
        url = intent.getExtras().getString("url");

        getDataFromJson();
    }

    private void notifyFinished(String location, String remoteUrl) {
        Intent i = new Intent(TRANSACTION_DONE);
        i.putExtra("location", location);
        i.putExtra("url", remoteUrl);
        DownloadService.this.sendBroadcast(i);
    }

    public void getDataFromJson() {
        ServiceHandler sh = new ServiceHandler();

        // Making a request to url and getting response
        String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);
        Log.d("Response: ", "> " + jsonStr + "\n");

        if (jsonStr != null) {
            try {
                JSONArray jsonArray = new JSONArray(jsonStr);
                // Log.d("jsonArray.length()", Integer.toString(jsonArray.length()));

                for (int i = 0; i < jsonArray.length(); i++) {
                    //TODO add if(.has()) everywhere
                    JSONObject jObj = jsonArray.getJSONObject(i);

                    CARDID = jObj.getString("cardId");
                    NAME = jObj.getString("name");
                    CARDSET = jObj.getString("cardSet");
                    TYPE = jObj.getString("type");
                    if (jObj.has("faction")) {

                        FACTION = jObj.getString("faction");
                    } else {
                        FACTION = "-";
                    }
                    RARITY = jObj.getString("rarity");
                    COST = Integer.toString(jObj.getInt("cost"));
                    if (jObj.has("attach")) {
                        ATTACH = Integer.toString(jObj.getInt("attach"));
                    } else {
                        ATTACH = "-";
                    }
                    HEALTH = Integer.toString(jObj.getInt("health"));
                    TEXT = jObj.getString("text");
                    ARTIST = jObj.getString("artist");
                    COLLECTIBLE = jObj.getString("collectible");
                    if (jObj.has("elit")) {

                        ELIT = jObj.getString("elit");
                    } else {
                        ELIT = "-";
                    }
                    IMG = jObj.getString("img");
                    if (jObj.has("imggold")) {

                        IMGGOLD = jObj.getString("imggold");
                    } else {
                        IMGGOLD = "-";
                    }
                    LOCALE = jObj.getString("locale");
                    if (jObj.has("mechanics")) {

                        MECHANICS = jObj.getString("mechanics");
                    } else {
                        MECHANICS = "-";
                    }


                    addToDatabase(CARDID, NAME, CARDSET, TYPE, FACTION, RARITY, COST, ATTACH, HEALTH, TEXT, ARTIST, COLLECTIBLE, ELIT, IMG, IMGGOLD, LOCALE, MECHANICS);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            countEntriesInDb();
            notifyFinished("location", url);

        }
    }

    public void addToDatabase(String cardid, String name, String cardset, String type, String faction, String rarity, String cost, String attach, String health, String text, String artist, String collectible, String elit, String img, String imggold, String locale, String mechanics) {
        DatabaseActivity entry = new DatabaseActivity(DownloadService.this);
        entry.open();
        if (entry.Exists(cardid, name)) {
            Log.d("card " + cardid, "exists to db");

        } else {
            entry.createEntry(cardid, name, cardset, type, faction, rarity, cost, attach, health, text, artist, collectible, elit, img, imggold, locale, mechanics);
            Log.d("card " + cardid, "added to db");

        }
        entry.close();
    }

    public void countEntriesInDb() {
        DatabaseActivity entry = new DatabaseActivity(DownloadService.this);
        entry.open();
        Log.d("TOTAL DB COUNT: ", Long.toString(entry.fetchPlacesCount()));
        entry.close();
    }
}
