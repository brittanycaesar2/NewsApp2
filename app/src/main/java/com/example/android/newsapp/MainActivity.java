package com.example.android.newsapp;

import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.support.v7.widget.AppCompatTextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderCallbacks<List<News>> {

    private static final String LOG_TAG = MainActivity.class.getName();

    /*
     * URl for news data from guardian website
     */
    public String REQUEST_URL =
            "http://content.guardianapis.com/search?q=debates&section=politics&show-tags=contributor&api-key=test";

    private static final int NEWS_LOADER_ID = 1;
    //list of news
    private NewsAdapter mAdapter;
    //displayed when list is empty
    private TextView mEmptyStateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //reference to ListView
        ListView newsListView = (ListView) findViewById(R.id.list);

        //new adapter with empty list of news
        mAdapter = new NewsAdapter(this, new ArrayList<News>());

        newsListView.setAdapter(mAdapter);

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        newsListView.setEmptyView(mEmptyStateTextView);
        //item click listener on ListView, with Intent to web
        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                News currentNews = mAdapter.getItem(position);
                //convert string URL to URI object
                Uri newsUri = Uri.parse(currentNews.getWebUrl());
                //new intent to view news uri
                Intent webIntent = new Intent(Intent.ACTION_VIEW, newsUri);
                //send intent to new activity
                startActivity(webIntent);
            }
        });
        //check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        //current data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        //if no connection
        if (networkInfo != null && networkInfo.isConnected()) {

            LoaderManager loaderManager = getLoaderManager();

            loaderManager.initLoader(NEWS_LOADER_ID, null, this).forceLoad();
        } else {
            View loadingIndicator = findViewById(R.id.loading_indicator);
            //update empty state text to no internet connection
            mEmptyStateTextView.setText("No internet Connection");

        }
    }


    @Override
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        //getString retrieves a String value from the preferences. the second parameter is default value for this preference.
        String pageSize = sharedPrefs.getString(getString(R.string.settings_page_size_key), getString(R.string.settings_page_size_default));

        String orderBy = sharedPrefs.getString(getString(R.string.settings_order_by_key), getString(R.string.settings_order_by_default));

        //parse break apart the URI string thats passed into its parameter
        Uri baseUri = Uri.parse(REQUEST_URL);

        //buildUpon prepares the baseUri that we just parsed so we can add query parameters to it
        Uri.Builder uriBuilder = baseUri.buildUpon();

        //Append query parameter and its value.
        uriBuilder.appendQueryParameter("format", "geojson");
        uriBuilder.appendQueryParameter("limit", "10");
        uriBuilder.appendQueryParameter("pageSize", pageSize);
        uriBuilder.appendQueryParameter("orderby", "orderBy");

        // return the completed uri
        return new NewsLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> news) {
        //Hide loading indicator
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);
        //empty state text to no news
        mEmptyStateTextView.setText(R.string.no_news);
        //Clear from previous news data
        mAdapter.clear();
        //update ListView
        if (news != null && !news.isEmpty()) {
            mAdapter.addAll(news);
        }

    }

    //Loader reset, clear existing data
    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        mAdapter.clear();
    }

    @Override
    // This method initialize the contents of the Activity's options menu.
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the Options Menu we specified in XML
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}