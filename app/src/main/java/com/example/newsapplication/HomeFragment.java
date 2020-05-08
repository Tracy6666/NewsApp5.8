package com.example.newsapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.content.DialogInterface;
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
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<News>> {

    @Nullable
    private View rootView;
    private FloatingActionButton floatingActionButton;
    private NewsAdapter adapter;
    private String JsonLink = "http://newsapi.org/v2/top-headlines?country=us&apiKey=84499478883a4727bfba30a6a52cc320";
    private String LOG_TAG = MainActivity.class.getName();
    private TextView emptyView;
    private static final int newsId = 1;
    private String[] items = { "Health", "Sport", "Business", "Technology" };

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
        floatingActionButton =
                (FloatingActionButton) rootView.findViewById(R.id.floating_action_button);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listDialog(items);
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
    private void listDialog(final String [] items) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Pick a catergory");
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub
                if("Health".equals(items[arg1])) {
                    JsonLink="http://newsapi.org/v2/top-headlines?country=us&category=health&apiKey=84499478883a4727bfba30a6a52cc320";
                }
                else if("Sport".equals(items[arg1])) {
                    JsonLink="http://newsapi.org/v2/top-headlines?country=us&category=sports&apiKey=84499478883a4727bfba30a6a52cc320";
                }else
                if("Technology".equals(items[arg1])){
                    JsonLink="http://newsapi.org/v2/top-headlines?country=us&category=technology&apiKey=84499478883a4727bfba30a6a52cc320";
                }
                else if("Business".equals(items[arg1])){
                    JsonLink="http://newsapi.org/v2/top-headlines?country=us&category=business&apiKey=84499478883a4727bfba30a6a52cc320";
                };
            reloadThis();
            }
        });
        builder.create().show();
    }
    private void reloadThis(){
        Bundle args = new Bundle();
        args.putString("JsonLink", JsonLink);
        LoaderManager lm = getLoaderManager();
        lm.restartLoader(newsId , args, this);
    }
    @NonNull
    @Override
    public Loader<List<News>> onCreateLoader(int i, @Nullable Bundle bundle) {
        // parse breaks apart the URI string that's passed into its parameter

        Uri baseUri = Uri.parse(JsonLink);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("api-key", "test");
        Log.e(LOG_TAG, uriBuilder.toString());
        return new NewsLoader(getContext(), uriBuilder.toString());

    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<News>> loader, List<News> data) {
        adapter.clear();

        if (data != null && !data.isEmpty()) {
            adapter.addAll(data);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<News>> loader) {
        adapter.clear();
    }
}
