package anthony.example.com.syncadapterlab.sync_adapter;


import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;

import anthony.example.com.syncadapterlab.MainActivity;
import anthony.example.com.syncadapterlab.model.StockAPIService;
import anthony.example.com.syncadapterlab.model.StockQuote;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SyncAdapter extends AbstractThreadedSyncAdapter {

    // Global variables
    // Define a variable to contain a content resolver instance
    ContentResolver mContentResolver;
    public StockAPIService mService;
    private static final String TAG = "SyncAdapter";


    /**
     * Set up the sync adapter
     */
    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        /*
         * If your app uses a content resolver, get an instance of it
         * from the incoming Context
         */
        mContentResolver = context.getContentResolver();
    }


    /**
     * Set up the sync adapter. This form of the
     * constructor maintains compatibility with Android 3.0
     * and later platform versions
     */
    public SyncAdapter(
            Context context,
            boolean autoInitialize,
            boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        /*
         * If your app uses a content resolver, get an instance of it
         * from the incoming Context
         */
        mContentResolver = context.getContentResolver();

    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Log.i(TAG, "onPerformSync: =========");
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://dev.markitondemand.com/MODApis/Api/v2/Quote/")
                .build();

        mService = retrofit.create(StockAPIService.class);
        Call<StockQuote> call = mService.getCompany("NFLX");

        call.enqueue(new Callback<StockQuote>() {
            @Override
            public void onResponse(Call<StockQuote> call, Response<StockQuote> response) {
                Log.i(TAG, "onResponse: " + response.body().getLastPrice());
            }

            @Override
            public void onFailure(Call<StockQuote> call, Throwable t) {

            }
        });
    }
}