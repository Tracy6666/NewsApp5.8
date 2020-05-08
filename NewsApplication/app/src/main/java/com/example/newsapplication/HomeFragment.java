package com.example.newsapplication;

import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment implements androidx.core.app.LoaderManager.LoaderCallbacks<List<News>> {

    @Nullable
    View rootView;
    private NewsAdapter adapter;
   // private String JsonLink = "https://newsapi.org/v2/top-headlines?country=us&category=health&apiKey=84499478883a4727bfba30a6a52cc320";
    private String JsonLink = "http://newsapi.org/v2/top-headlines?country=us&apiKey=84499478883a4727bfba30a6a52cc320";
    private String LOG_TAG = MainActivity.class.getName();
    private TextView emptyView;
    private static final int newsId = 1;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);

        ListView newsListView = (ListView) rootView.findViewById(R.id.list);

        adapter = new NewsAdapter(getContext(), new ArrayList<News>());

        newsListView.setAdapter(adapter);

        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                News news = adapter.getItem(i);
                Uri newsUri = Uri.parse(news.getUrl());
                Intent intent = new Intent(view.getContext(), newsUri.class);
               // intent.putExtra("url", newsUri);
                intent.setData(newsUri);
                startActivity(intent);
            }
        });

        emptyView = (TextView) rootView.findViewById(R.id.empty_state_textview);
        newsListView.setEmptyView(emptyView);

        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();

        if (activeNetwork != null && activeNetwork.isConnected()) {
            LoaderManager loaderManager = getLoaderManager();
            View progressBar = rootView.findViewById(R.id.progress_bar);
            progressBar.setVisibility(View.GONE);
            loaderManager.initLoader(newsId, null, this);

        } else {
            View progressBar = rootView.findViewById(R.id.progress_bar);
            progressBar.setVisibility(View.GONE);
            emptyView.setText(R.string.no_internet);
        }
        return rootView;
    }

    @NonNull
    @Override
    public Loader<List<News>> onCreateLoader(int i, @Nullable Bundle bundle) {
        // parse breaks apart the URI string that's passed into its parameter
        Uri baseUri = Uri.parse(JsonLink);
        Uri.Builder uriBuilder = baseUri.buildUpon();

      //  uriBuilder.appendQueryParameter("q", "health");
        uriBuilder.appendQueryParameter("api-key", "test");

        Log.e(LOG_TAG, uriBuilder.toString());
        return new NewsLoader(getContext(), uriBuilder.toString());

    }

    @Override
    public void onLoadFinished(@NonNull androidx.core.content.Loader<List<News>> loader, List<News> data) {
        adapter.clear();

        if (data != null && !data.isEmpty()) {
            adapter.addAll(data);
        }
    }

    @Override
    public void onLoaderReset(@NonNull androidx.core.content.Loader<List<News>> loader) {
        adapter.clear();
    }
}
