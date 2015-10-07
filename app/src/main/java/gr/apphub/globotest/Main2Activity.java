package gr.apphub.globotest;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class Main2Activity extends AppCompatActivity {
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    private DisplayImageOptions options;
    String CARDID, NAME, CARDSET, TYPE, FACTION, RARITY, COST, ATTACK, HEALTH, TEXT, ARTIST, COLLECTIBLE, ELITE, IMG, IMGGOLD, LOCALE, MECHANICS, HOWTOGET,FLAVOR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Bundle extras = getIntent().getExtras();
        String cardid = extras.get("cardid").toString();

        DatabaseActivity entry = new DatabaseActivity(Main2Activity.this);
        entry.open();
        Cursor c = entry.getItemByCardId(cardid);
        c.moveToFirst();
        CARDID = c.getString(c.getColumnIndex(DatabaseActivity.DBHelper.CARDID));
        NAME = c.getString(c.getColumnIndex(DatabaseActivity.DBHelper.NAME));
        CARDSET = c.getString(c.getColumnIndex(DatabaseActivity.DBHelper.CARDSET));
        TYPE = c.getString(c.getColumnIndex(DatabaseActivity.DBHelper.TYPE));
        FACTION = c.getString(c.getColumnIndex(DatabaseActivity.DBHelper.FACTION));
        RARITY = c.getString(c.getColumnIndex(DatabaseActivity.DBHelper.RARITY));
        COST = c.getString(c.getColumnIndex(DatabaseActivity.DBHelper.COST));
        ATTACK = c.getString(c.getColumnIndex(DatabaseActivity.DBHelper.ATTACK));
        HEALTH = c.getString(c.getColumnIndex(DatabaseActivity.DBHelper.HEALTH));
        TEXT = c.getString(c.getColumnIndex(DatabaseActivity.DBHelper.TEXT));
        ARTIST = c.getString(c.getColumnIndex(DatabaseActivity.DBHelper.ARTIST));
        COLLECTIBLE = c.getString(c.getColumnIndex(DatabaseActivity.DBHelper.COLLECTIBLE));
        ELITE = c.getString(c.getColumnIndex(DatabaseActivity.DBHelper.ELITE));
        IMG = c.getString(c.getColumnIndex(DatabaseActivity.DBHelper.IMG));
        IMGGOLD = c.getString(c.getColumnIndex(DatabaseActivity.DBHelper.IMGGOLD));
        LOCALE = c.getString(c.getColumnIndex(DatabaseActivity.DBHelper.LOCALE));
        MECHANICS = c.getString(c.getColumnIndex(DatabaseActivity.DBHelper.MECHANICS));
        HOWTOGET = c.getString(c.getColumnIndex(DatabaseActivity.DBHelper.HOWTOGET));
        FLAVOR = c.getString(c.getColumnIndex(DatabaseActivity.DBHelper.FLAVOR));

//        Log.d("img",image);

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_stub)
                .showImageForEmptyUri(R.drawable.ic_empty)
                .showImageOnFail(R.drawable.ic_error)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .build();

        TextView cardName = (TextView) findViewById(R.id.cardName);
        cardName.setText(NAME);

        TextView cardText = (TextView) findViewById(R.id.cardText);
        cardText.setText(Html.fromHtml(TEXT));


        TextView cardRarity = (TextView) findViewById(R.id.cardRarity);
        cardRarity.setText(Html.fromHtml(RARITY));


        TextView cardType = (TextView) findViewById(R.id.cardType);
        cardType.setText(Html.fromHtml(TYPE));

        TextView cardSet = (TextView) findViewById(R.id.cardSet);
        cardSet.setText(Html.fromHtml(CARDSET));

        TextView cardCollectible = (TextView) findViewById(R.id.cardCollectible);
        if (COLLECTIBLE.equals("true")) {
            cardCollectible.setText("Yes");
        } else {
            cardCollectible.setText("No");
        }

        LinearLayout howToGetLayout = (LinearLayout) findViewById(R.id.howToGetLayout);
        TextView cardHowToGet = (TextView) findViewById(R.id.cardhowToGet);

        if (!HOWTOGET.equals("-")) {
            cardHowToGet.setText(Html.fromHtml(HOWTOGET));
        } else {
            howToGetLayout.setVisibility(View.GONE);
        }

        TextView cardFlavor = (TextView) findViewById(R.id.cardFlavor);
        cardFlavor.setText(Html.fromHtml(FLAVOR));

        Log.d("ELITE", ELITE);
        TextView cardElit = (TextView) findViewById(R.id.cardElit);
        if (ELITE.equals("true")) {
            cardElit.setText("Yes");
        } else {
            cardElit.setText("No");
        }

        TextView cardArtist = (TextView) findViewById(R.id.cardArtist);
        cardArtist.setText(Html.fromHtml(ARTIST));

        TextView cardCost = (TextView) findViewById(R.id.cardCost);
        cardCost.setText(COST);

        TextView cardATTACK = (TextView) findViewById(R.id.cardAttack);
        cardATTACK.setText(ATTACK);

        TextView cardHealth= (TextView) findViewById(R.id.cardHealth);
        cardHealth.setText(HEALTH);


        TextView cardId= (TextView) findViewById(R.id.cardId);
        cardId.setText(CARDID);


        ImageView im1 = (ImageView) findViewById(R.id.cardImg);
        ImageLoader.getInstance().displayImage(IMG, im1, options, animateFirstListener);

        entry.close();

    }

}
