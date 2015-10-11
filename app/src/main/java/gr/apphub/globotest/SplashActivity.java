package gr.apphub.globotest;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Window;
import android.widget.Toast;

/**
 * Created by Konstantinos on 07/10/15.
 */
public class SplashActivity extends Activity {
    protected boolean _active = true;
    protected int _splashTime = 3000; // time to display the splash screen in ms
    String url = "https://omgvamp-hearthstone-v1.p.mashape.com/cards/qualities/Legendary?collectible=1&locale=enUS";
    ConnectivityManager conMgr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.splash);

// use conMgr in order to check id internet connection is active
        conMgr = (ConnectivityManager) this
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (conMgr.getActiveNetworkInfo() != null) {
// if internet connection is active
            startDownloadService();

        } else {
//if internet connection is disabled


            Toast.makeText(this, getString(R.string.nointernet), Toast.LENGTH_LONG).show();

            DatabaseActivity entry = new DatabaseActivity(SplashActivity.this);
            entry.open();
            if (entry.getData().getCount() > 0) {
                //exei eggrafes stin vasi

                Thread splashTread = new Thread() {
                    @Override
                    public void run() {
                        try {
                            int waited = 0;
                            while (_active && (waited < _splashTime)) {
                                sleep(100);
                                if (_active) {
                                    waited += 100;
                                }
                            }
                        } catch (InterruptedException e) {
                            // do nothing
                        } finally {
                            //finish splash screen and move to MainActivity.class
                            finish();
                            goToMainActivity();

                        }
                    }
                };
                splashTread.start();

            } else {
                //i vasi einai adeia (prwti xrisi tis efarmogis dld)

                noDataDialog();
            }
        }


    }

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

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            _active = false;
        }
        return true;
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

            goToMainActivity();
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();

        try {
            unregisterReceiver(jsonReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void goToMainActivity() {
        finish();
        Intent i2 = new Intent(SplashActivity.this, MainActivity.class);
        //pass the url via intent
        i2.putExtra("url", url);
        startActivity(i2);
    }

    public void noDataDialog() {
        //pop up an alert dialog to notify user that the app needs internet connection for the first time opening
        AlertDialog alertDialog = new AlertDialog.Builder(this).create(); //Read Update
        alertDialog.setTitle("First time use");
        alertDialog.setMessage("Please enable internet connection and retry!");



        alertDialog.setButton(Dialog.BUTTON_NEGATIVE, "EXIT", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                finish();

            }
        });

        alertDialog.setButton(Dialog.BUTTON_POSITIVE, "SETTINGS", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                finish();
                startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));


            }
        });


        alertDialog.show();
        //the app finishes and the user have to enable internet connection and retry

    }

}
