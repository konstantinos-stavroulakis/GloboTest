package gr.apphub.globotest;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONArray;

public class MainActivity extends AppCompatActivity {
    private SwipeRefreshLayout mSwipeRefreshLayout;
    ConnectivityManager conMgr;

    ListView mListView;
    GridView mGridView;

    ListAdapter mAdapter;
    GridAdapter mGridAdapter;

    private ProgressDialog dialogfeed;

    private static final String TAG = MainActivity.class.getName();

    String url;

    private Menu optionsMenu;

    private static final String CARDID = "cardId";
    private static final String NAME = "name";
    private static final String CARDSET = "cardSet";
    private static final String TYPE = "type";
    private static final String FACTION = "faction";
    private static final String RARITY = "rarity";
    private static final String COST = "cost";
    private static final String ATTACK = "ATTACK";
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
    boolean adapterChoice;


    //TODO comment ola ta Log.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate();");

        conMgr = (ConnectivityManager) this
                .getSystemService(Context.CONNECTIVITY_SERVICE);


        displaySharedPreferences();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            url = extras.get("url").toString();
        }

        // Create global configuration and initialize ImageLoader with this config
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().init(config);

        mListView = (ListView) findViewById(R.id.lvItems);
        mGridView = (GridView) findViewById(R.id.gridView1);


        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshContent();
            }
        });

        setListAdapter();

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


                //AppCompat 21 Material Theme Style

//                ActivityOptionsCompat options =
//                        ActivityOptionsCompat.makeSceneTransitionAnimation(
//                                MainActivity.this, view, "cardid");
//                Intent intent = new Intent(MainActivity.this, Main2Activity.class);
//                intent.putExtra("cardid", cid);
//                ActivityCompat.startActivity(MainActivity.this, intent, options.toBundle());


            }
        });
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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

    }//end onCrate


    public void DownloadJson() {

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(DownloadService.TRANSACTION_DONE);
        registerReceiver(jsonReceiver, intentFilter);
        Intent i = new Intent(this, DownloadService.class);
        i.putExtra("url", url);
        startService(i);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            unregisterReceiver(jsonReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void refreshContent() {
        if (conMgr.getActiveNetworkInfo() != null) {
            DownloadJson();
        } else {
            Toast.makeText(this, getString(R.string.nointernet), Toast.LENGTH_SHORT).show();
            mSwipeRefreshLayout.setRefreshing(false);

        }
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
            if (!adapterChoice) {
                setListAdapter();
            } else {
                setGridAdapter();
            }
            mSwipeRefreshLayout.setRefreshing(false);
            Log.d("jsonReceiver", "mSwipeRefreshLayout hidden");
            setRefreshActionButtonstate(false);


        }
    };


    public void setListAdapter() {
        DatabaseActivity entry = new DatabaseActivity(MainActivity.this);
        entry.open();
        mAdapter = new ListAdapter(MainActivity.this, entry.getData());
        mListView.setAdapter(mAdapter);
        entry.close();

    }

    public void setGridAdapter() {
        DatabaseActivity entry = new DatabaseActivity(MainActivity.this);
        entry.open();

        mGridAdapter = new GridAdapter(MainActivity.this, entry.getData());
        mGridView.setAdapter(mGridAdapter);

        entry.close();

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume();");
        displaySharedPreferences();
        if (adapterChoice) {

            if (mListView.getVisibility() != View.GONE) {
                mSwipeRefreshLayout.setVisibility(View.GONE);
                mListView.setVisibility(View.GONE);
                mGridView.setVisibility(View.VISIBLE);
                setGridAdapter();
            }
        } else {
            if (mListView.getVisibility() != View.VISIBLE) {
                mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                mListView.setVisibility(View.VISIBLE);
                mGridView.setVisibility(View.GONE);
                setListAdapter();
            }
        }
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        this.optionsMenu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_load:
                if (conMgr.getActiveNetworkInfo() != null) {
                    setRefreshActionButtonstate(true);
                    DownloadJson();
                } else {
                    Toast.makeText(this, getString(R.string.nointernet), Toast.LENGTH_SHORT).show();
                    mSwipeRefreshLayout.setRefreshing(false);

                }
                return true;
            case R.id.settings:

                Intent intent = new Intent(MainActivity.this,
                        PrefsActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void setRefreshActionButtonstate(final boolean refreshing) {
        if (optionsMenu != null) {
            final MenuItem refreshItem = optionsMenu
                    .findItem(R.id.menu_load);
            if (refreshItem != null) {
                if (refreshing) {
                    refreshItem.setActionView(R.layout.progressbar);
                } else {
                    refreshItem.setActionView(null);
                }
            }
        }
    }


    private void displaySharedPreferences() {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(MainActivity.this);
        adapterChoice = prefs.getBoolean("checkBox", false);
    }
}