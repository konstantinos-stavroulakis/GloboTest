package gr.apphub.globotest;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONArray;

public class MainActivity extends AppCompatActivity {
    private SwipeRefreshLayout mSwipeRefreshLayout;
    ListView mListView;
    ListAdapter mAdapter;
    private ProgressDialog dialogfeed;

    String url = "https://omgvamp-hearthstone-v1.p.mashape.com/cards/qualities/Legendary?collectible=1&locale=enUS";

    private static final String CARDID = "cardId";
    private static final String NAME = "name";
    private static final String CARDSET = "cardSet";
    private static final String TYPE = "type";
    private static final String FACTION = "faction";
    private static final String RARITY = "rarity";
    private static final String COST = "cost";
    private static final String ATTACH = "attach";
    private static final String HEALTH = "health";
    private static final String TEXT = "text";
    private static final String ARTIST = "artist";
    private static final String COLLECTIBLE = "collectible";
    private static final String ELIT = "elit";
    private static final String IMG = "img";
    private static final String IMGGOLD = "imggold";
    private static final String LOCALE = "locale";
    private static final String MECHANICS = "mechanics";
    JSONArray json = null;

//TODO comment ola ta Log.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create global configuration and initialize ImageLoader with this config
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().init(config);

        mListView = (ListView) findViewById(R.id.lvItems);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshContent();
            }
        });

//        new AsyncLoadXMLFeed().execute();
        Intent ImageDownloadService= new Intent(MainActivity.this, ImageDownloadService.class);
// add necessary  data to intent
// start service
        startService(ImageDownloadService);
        DatabaseActivity entry = new DatabaseActivity(MainActivity.this);
        entry.open();
        mAdapter = new ListAdapter(MainActivity.this, entry.getData());
        mListView.setAdapter(mAdapter);
        entry.close();

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                DatabaseActivity entry = new DatabaseActivity(MainActivity.this);
                entry.open();
                String cid = entry.getCardIdFromPosition(position);
                entry.close();

                Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                intent.putExtra("cardid", cid);
                startActivity(intent);


            }
        });

    }//end onCreate

    private void refreshContent() {
        Intent ImageDownloadService= new Intent(MainActivity.this, ImageDownloadService.class);

        startService(ImageDownloadService);

        mSwipeRefreshLayout.setRefreshing(false);

    }}