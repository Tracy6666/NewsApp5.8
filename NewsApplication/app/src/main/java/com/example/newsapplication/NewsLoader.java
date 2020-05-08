package com.example.newsapplication;

import android.content.Context;
import androidx.loader.content.AsyncTaskLoader;

import java.util.List;

public class NewsLoader extends AsyncTaskLoader<List<News>> {
    String url;

    public NewsLoader(Context context,String url){
        super(context);
        this.url = url;
    }

    protected void onStartLoading(){
        forceLoad();
    }


    @Override
    public List<News> loadInBackground() {
        if(url == null) {
            return null;
        }
        List<News> news = Utils.fetchNewsData(url);
        return news;
    }
}
