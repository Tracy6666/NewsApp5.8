package com.example.newsapplication;

import android.app.Activity;
import android.content.Context;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;



public class NewsAdapter extends ArrayAdapter<News> {
    public NewsAdapter(Context context, ArrayList<News> news) {
        super(context, 0, news);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView,@NonNull ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        News currentNews = getItem(position);

        TextView titleView = (TextView) listItemView.findViewById(R.id.title);
        titleView.setText(currentNews.getTitle());

        TextView dateView = (TextView) listItemView.findViewById(R.id.date);
        dateView.setText(currentNews.getDate());

        TextView sectionView = (TextView)listItemView.findViewById(R.id.section);
        sectionView.setText(currentNews.getSection());

        TextView author = (TextView) listItemView.findViewById(R.id.author);
        author.setText("Author: "+currentNews.getAuthor());

        ImageView imageView = (ImageView)listItemView.findViewById(R.id.image);
        String url=currentNews.getImgUrl();
        Picasso.with(getContext()).load(url).resize(400,400).into(imageView);
        return listItemView;
    }
}
