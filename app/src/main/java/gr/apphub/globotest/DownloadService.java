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
    String CARDID, NAME, CARDSET, TYPE, FACTION, RARITY, COST, ATTACK, HEALTH, TEXT, ARTIST, COLLECTIBLE, ELITE, IMG, IMGGOLD, LOCALE, MECHANICS, HOWTOGET, FLAVOR;
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

                    if (jObj.has("cardId")) {
                        CARDID = jObj.getString("cardId");
                    } else {
                        CARDID = "";
                    }

                    if (jObj.has("name")) {
                        NAME = jObj.getString("name");
                    } else {
                        NAME = "-";
                    }

                    if (jObj.has("cardSet")) {
                        CARDSET = jObj.getString("cardSet");
                    } else {
                        CARDSET = "-";
                    }

                    if (jObj.has("type")) {
                        TYPE = jObj.getString("type");
                    } else {
                        TYPE = "-";
                    }

                    if (jObj.has("faction")) {

                        FACTION = jObj.getString("faction");
                    } else {
                        FACTION = "-";
                    }

                    if (jObj.has("rarity")) {

                        RARITY = jObj.getString("rarity");
                    } else {
                        RARITY = "-";
                    }

                    if (jObj.has("cost")) {

                        COST = jObj.getString("cost");
                    } else {
                        COST = "-";
                    }

                    if (jObj.has("attack")) {
                        ATTACK = Integer.toString(jObj.getInt("attack"));
                    } else {
                        ATTACK = "-";
                    }

                    if (jObj.has("health")) {
                        HEALTH = Integer.toString(jObj.getInt("health"));
                    } else {
                        HEALTH = "-";
                    }

                    if (jObj.has("text")) {

                        TEXT = jObj.getString("text");
                    } else {
                        TEXT = "-";
                    }

                    if (jObj.has("artist")) {

                        ARTIST = jObj.getString("artist");
                    } else {
                        ARTIST = "-";
                    }

                    if (jObj.has("collectible")) {

                        COLLECTIBLE = jObj.getString("collectible");
                    } else {
                        COLLECTIBLE = "-";
                    }

                    if (jObj.has("elite")) {

                        ELITE = jObj.getString("elite");
                    } else {
                        ELITE = "-";
                    }

                    if (jObj.has("img")) {

                        IMG = jObj.getString("img");
                    } else {
                        IMG = "-";
                    }

                    if (jObj.has("imggold")) {

                        IMGGOLD = jObj.getString("imggold");
                    } else {
                        IMGGOLD = "-";
                    }

                    if (jObj.has("locale")) {

                        LOCALE = jObj.getString("locale");
                    } else {
                        LOCALE = "-";
                    }

                    if (jObj.has("mechanics")) {

                        MECHANICS = jObj.getString("mechanics");
                    } else {
                        MECHANICS = "-";
                    }

                    if (jObj.has("howToGet")) {

                        HOWTOGET = jObj.getString("howToGet");
                    } else {
                        HOWTOGET = "-";
                    }
                    if (jObj.has("flavor")) {

                        FLAVOR = jObj.getString("flavor");
                    } else {
                        FLAVOR = "-";
                    }


                    addToDatabase(CARDID, NAME, CARDSET, TYPE, FACTION, RARITY, COST, ATTACK, HEALTH, TEXT, ARTIST, COLLECTIBLE, ELITE, IMG, IMGGOLD, LOCALE, MECHANICS, HOWTOGET, FLAVOR);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            notifyFinished("location", url);

        }
    }

    public void addToDatabase(String cardid, String name, String cardset, String type, String faction, String rarity, String cost, String attack, String health, String text, String artist, String collectible, String elit, String img, String imggold, String locale, String mechanics, String howToGet, String flavor) {
        DatabaseActivity entry = new DatabaseActivity(DownloadService.this);
        entry.open();
        if (entry.Exists(cardid, name)) {
            Log.d("card " + cardid, "exists to db");

        } else {
            entry.createEntry(cardid, name, cardset, type, faction, rarity, cost, attack, health, text, artist, collectible, elit, img, imggold, locale, mechanics, howToGet, flavor);
            Log.d("card " + cardid, "added to db");

        }
        entry.close();
    }


}
