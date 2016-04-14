package anthony.example.com.syncadapterlab;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import anthony.example.com.syncadapterlab.model.StockAPIService;
import anthony.example.com.syncadapterlab.model.StockQuote;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    // Constants
    private static final String TAG = "MainActivity";
    // Content provider authority
    public static final String AUTHORITY = "anthony.example.com.syncadapterlab.sync_adapter.StubProvider";
    // Account type
    public static final String ACCOUNT_TYPE = "example.com";
    // Account
    public static final String ACCOUNT = "default_account";

    Account mAccount;

    Button syncAutoMinuteButton;
    Button manualSyncButton;
    Button syncAutoFiveMinuteButton;
    TextView companyName;
    TextView companyLastPrice;
    ProgressBar progressBar;

    // Global variables
    // A content resolver for accessing the provider
    // ContentResolver mResolver;

    public StockAPIService mService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        manualSyncButton = (Button) findViewById(R.id.btn_manually_sync);
        syncAutoMinuteButton = (Button) findViewById(R.id.btn_auto_sync_every_min);
        syncAutoFiveMinuteButton = (Button) findViewById(R.id.btn_auto_sync_five_min);
        companyName = (TextView) findViewById(R.id.company_name_text_view);
        companyLastPrice = (TextView) findViewById(R.id.last_stock_price_text_view);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        setSyncEveryMinuteButton();
        setSyncEveryFiveMinuteButton();
        setManualSyncButton();


        mAccount = createSyncAccount(this);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://dev.markitondemand.com/MODApis/Api/v2/Quote/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mService = retrofit.create(StockAPIService.class);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    Bundle settingsBundle = new Bundle();
                    settingsBundle.putBoolean(
                            ContentResolver.SYNC_EXTRAS_MANUAL, true);
                    settingsBundle.putBoolean(
                            ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
                    /*
                     * Request the sync for the default account, authority, and
                     * manual sync settings
                     */
                    ContentResolver.requestSync(mAccount, AUTHORITY, settingsBundle);


                }
            });
        }
//        ContentResolver.setSyncAutomatically(mAccount, AUTHORITY, true);
//        ContentResolver.addPeriodicSync(mAccount, AUTHORITY, Bundle.EMPTY, 5);

    }

    private void setSyncEveryFiveMinuteButton() {
        syncAutoFiveMinuteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle settingsBundle = new Bundle();
                settingsBundle.putBoolean(
                        ContentResolver.SYNC_EXTRAS_MANUAL, true);
                settingsBundle.putBoolean(
                        ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
                    /*
                     * Request the sync for the default account, authority, and
                     * manual sync settings
                     */
                //ContentResolver.requestSync(mAccount, AUTHORITY, settingsBundle);
                ContentResolver.addPeriodicSync(mAccount, AUTHORITY, Bundle.EMPTY, 300);

            }
        });
    }

    private void setSyncEveryMinuteButton() {
        syncAutoMinuteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle settingsBundle = new Bundle();
                settingsBundle.putBoolean(
                        ContentResolver.SYNC_EXTRAS_MANUAL, true);
                settingsBundle.putBoolean(
                        ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
                    /*
                     * Request the sync for the default account, authority, and
                     * manual sync settings
                     */
                ContentResolver.requestSync(mAccount, AUTHORITY, settingsBundle);
                ContentResolver.addPeriodicSync(mAccount, AUTHORITY, Bundle.EMPTY, 60);

            }
        });

    }

    private void setManualSyncButton() {
        manualSyncButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                Call<StockQuote> call = mService.getCompany("NFLX");

                call.enqueue(new Callback<StockQuote>() {
                    @Override
                    public void onResponse(Call<StockQuote> call, Response<StockQuote> response) {
                        progressBar.setVisibility(View.INVISIBLE);
                        Log.i(TAG, "onResponse: " + response.body().getLastPrice());
                        companyName.setText(response.body().getName());
                        companyLastPrice.setText( String.valueOf(response.body().getLastPrice()));
                    }

                    @Override
                    public void onFailure(Call<StockQuote> call, Throwable t) {
                    }
                });
            }
        });
    }


    public static Account createSyncAccount(Context context) {
        // Create the account type and default account
        Account newAccount = new Account(
                ACCOUNT, ACCOUNT_TYPE);
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(
                        ACCOUNT_SERVICE);
        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
        if (accountManager.addAccountExplicitly(newAccount, null, null)) {
          /*
           * If you don't set android:syncable="true" in
           * in your <provider> element in the manifest,
           * then call context.setIsSyncable(account, AUTHORITY, 1)
           * here.
           */
        } else {
            /*
             * The account exists or some other error occurred. Log this, report it,
             * or handle it internally.
             */
        }
        return newAccount;
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
