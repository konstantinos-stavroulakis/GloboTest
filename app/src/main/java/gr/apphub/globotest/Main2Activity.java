package gr.apphub.globotest;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class Main2Activity extends AppCompatActivity {
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    private DisplayImageOptions options;

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
        String image = c.getString(c.getColumnIndex(DatabaseActivity.DBHelper.IMG));
//        Log.d("img",image);

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_stub)
                .showImageForEmptyUri(R.drawable.ic_empty)
                .showImageOnFail(R.drawable.ic_error)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .build();


        ImageView im1 = (ImageView) findViewById(R.id.image1);
        ImageLoader.getInstance().displayImage(image, im1, options, animateFirstListener);

        entry.close();

    }

}
