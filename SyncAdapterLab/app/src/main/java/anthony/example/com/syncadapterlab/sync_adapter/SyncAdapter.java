package anthony.example.com.syncadapterlab.sync_adapter;


import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;

import anthony.example.com.syncadapterlab.model.StockAPIService;
import anthony.example.com.syncadapterlab.model.StockQuote;
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
            .baseUrl("http://dev.markitondemand.com/MODApis/Api/v2/Quote/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

        mService = retrofit.create(StockAPIService.class);

//        Call<StockQuote> call = mService.getCompany("NFLX");
//        call.enqueue(new Callback<StockQuote>() {
//            @Override
//            public void onResponse(Call<StockQuote> call, Response<StockQuote> response) {
//                Log.i(TAG, "onResponse: " + response.body().getLastPrice());
//            }
//
//            @Override
//            public void onFailure(Call<StockQuote> call, Throwable t) {
//
//            }
//        });

        //get Quotes for Netflix
        try {
            Response<StockQuote> response = mService.getCompany("NFLX").execute();
            Log.i(TAG, "onResponse: name is " + response.body().getName() + " last price is " +response.body().getLastPrice());

        } catch (IOException e) {
            e.printStackTrace();
        }

        //get Quotes for Apple
        try {
           Response<StockQuote> response = mService.getCompany("AAPL").execute();
            Log.i(TAG, "onResponse: name is " + response.body().getName() + " last price is " +response.body().getLastPrice());
        } catch (IOException e) {
            e.printStackTrace();
        }

        //get Quotes for Google
        try {
            Response<StockQuote> response = mService.getCompany("GOOGL").execute();
            Log.i(TAG, "onResponse: name is " + response.body().getName() + " last price is " +response.body().getLastPrice());
        } catch (IOException e) {
            e.printStackTrace();
        }

        //get Quotes for facebook
        try {
            Response<StockQuote> response = mService.getCompany("FB").execute();
            Log.i(TAG, "onResponse: name is " + response.body().getName() + " last price is " +response.body().getLastPrice());
        } catch (IOException e) {
            e.printStackTrace();
        }

        //get Quotes for Microsoft
        try {
            Response<StockQuote> response = mService.getCompany("MSFT").execute();
            Log.i(TAG, "onResponse: name is " + response.body().getName() + " last price is " +response.body().getLastPrice());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}