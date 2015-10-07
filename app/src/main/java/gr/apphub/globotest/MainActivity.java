package gr.apphub.globotest;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

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
    ProgressDialog pd;
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

        startDownloadService();


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

    public void DownloadJson() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(DownloadService.TRANSACTION_DONE);
        registerReceiver(jsonReceiver, intentFilter);
        Intent i = new Intent(this, DownloadService.class);
        i.putExtra("url", url);
        startService(i);
    }

    public void startDownloadService() {

        DownloadJson();
        pd = ProgressDialog.show(this, "Fetching json", "Go intent service go!");

    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub

        try {

            unregisterReceiver(jsonReceiver);
        } catch (Exception e) {

        }
        super.onDestroy();

    }

    private void refreshContent() {
        DownloadJson();
    }


    private BroadcastReceiver jsonReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String location = intent.getExtras().getString("location");
            String url = intent.getExtras().getString("url");
            Log.d("---location---", location);
            Log.d("---url---", url);

            if (location == null || location.length() == 0) {
                Toast.makeText(context, "Failed to download json",
                        Toast.LENGTH_LONG).show();
            }

            setmAdapter();

            if (mSwipeRefreshLayout.isRefreshing()) {
                mSwipeRefreshLayout.setRefreshing(false);
                Log.d("jsonReceiver", "mSwipeRefreshLayout hidden");
            }

            if (pd.isShowing()) {
                pd.dismiss();
                Log.d("jsonReceiver", "pd dismissed");

            }
        }
    };


    public void setmAdapter() {
        DatabaseActivity entry = new DatabaseActivity(MainActivity.this);
        entry.open();
        mAdapter = new ListAdapter(MainActivity.this, entry.getData());
        mListView.setAdapter(mAdapter);
        entry.close();

    }

}