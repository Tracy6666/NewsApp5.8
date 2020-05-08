package com.example.newsapplication;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import   java.text.SimpleDateFormat;

public class SearchFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<News>> , DatePickerDialog.OnDateSetListener {

    private View rootView;
    private NewsAdapter adapter;
    private EditText beginDate;
    private EditText endDate;
    private EditText keyword;
    private Button searchButton;
    private ListView listView;
    private TextView emptyView;
    private TextView labelBeginDate;
    private TextView labelEndDate;
    private TextView labelKeyWord;
    private ImageButton begin_date_picker;
    private ImageButton end_date_picker;
    private FloatingActionButton backButton;
    private static final int newsId = 1;
    private String baseUrl = "https://newsapi.org/v2/everything?apiKey=84499478883a4727bfba30a6a52cc320";
    private String selectedDate = "Begin";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_search, container, false);
        beginDate = (EditText)rootView.findViewById(R.id.begin);
        endDate = (EditText)rootView.findViewById(R.id.end);
        keyword = (EditText)rootView.findViewById(R.id.key_word_input);
        searchButton = (Button)rootView.findViewById(R.id.search);
        backButton = (FloatingActionButton)rootView.findViewById(R.id.back);
        listView = (ListView)rootView.findViewById(R.id.list);
        labelBeginDate = (TextView)rootView.findViewById(R.id.begin_date);
        labelEndDate = (TextView)rootView.findViewById(R.id.end_date);
        labelKeyWord = (TextView)rootView.findViewById(R.id.key_word);
        begin_date_picker = (ImageButton)rootView.findViewById(R.id.select_begin);
        end_date_picker = (ImageButton)rootView.findViewById(R.id.select_end);
        beginDate.setEnabled(false);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                labelBeginDate.setVisibility(View.INVISIBLE);
                labelEndDate.setVisibility(View.INVISIBLE);
                labelKeyWord.setVisibility(View.INVISIBLE);
                backButton.show();
                searchButton.setVisibility(View.INVISIBLE);
                beginDate.setVisibility(View.INVISIBLE);
                endDate.setVisibility(View.INVISIBLE);
                keyword.setVisibility(View.INVISIBLE);
                listView.setVisibility(View.VISIBLE);
                begin_date_picker.setVisibility(View.INVISIBLE);
                end_date_picker.setVisibility(View.INVISIBLE);
                adapter = new NewsAdapter(getContext(), new ArrayList<News>());

                listView.setAdapter(adapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        News news = adapter.getItem(i);
                        Uri newsUri = Uri.parse(news.getUrl());
                        Intent intent = new Intent(view.getContext(), newsUri.class);
                        intent.setData(newsUri);
                        startActivity(intent);
                    }
                });


                listView.setEmptyView(emptyView);

                ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
                checkNetwork(activeNetwork);


            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                backButton.hide();
                searchButton.setVisibility(View.VISIBLE);
                beginDate.setVisibility(View.VISIBLE);
                endDate.setVisibility(View.VISIBLE);
                keyword.setVisibility(View.VISIBLE);
                listView.setVisibility(View.INVISIBLE);
                labelBeginDate.setVisibility(View.VISIBLE);
                labelEndDate.setVisibility(View.VISIBLE);
                labelKeyWord.setVisibility(View.VISIBLE);
                begin_date_picker.setVisibility(View.VISIBLE);
                end_date_picker.setVisibility(View.VISIBLE);
                beginDate.setText("");
                endDate.setText((""));
                keyword.setText("");
                adapter.clear();
            }
        });

        begin_date_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedDate = "Begin";
                showDatePickerDialog();
            }
        });
        end_date_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedDate = "End";
                showDatePickerDialog();
            }
        });

        return rootView;
    }


    private void checkNetwork(NetworkInfo activeNetwork){
        if (activeNetwork != null && activeNetwork.isConnected()) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.restartLoader(newsId, null, this);
        } else {

            emptyView.setText(R.string.no_internet);
        }
    }

    @NonNull
    @Override
    public Loader<List<News>> onCreateLoader(int id, @Nullable Bundle args) {
        Uri baseUri = Uri.parse(baseUrl);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        String key =keyword.getText().toString();
        String begin = beginDate.getText().toString();
        String end = endDate.getText().toString();
        if(key.isEmpty()){
            key="health";
        }
        //uriBuilder.appendQueryParameter("api-key", "test");
        SimpleDateFormat   formatter   =   new   SimpleDateFormat   ("yyyy-MM-dd");
        String curDate =  formatter.format(new Date(System.currentTimeMillis()));

        if ((!StringUtil.isNullOrEmpty(begin) && !StringUtil.isNullOrEmpty(end))
             && begin.compareTo(end) > 0) {
            Toast.makeText(getContext(),"Input a invaild date",Toast.LENGTH_LONG);

        }

        if(StringUtil.isNullOrEmpty(begin)){
            begin = curDate;
        } else {
            begin = beginDate.getText().toString();
        }
        if(StringUtil.isNullOrEmpty(end)){
            end = curDate;
        } else {
            end = endDate.getText().toString();
        }

        uriBuilder.appendQueryParameter("from", begin);
        uriBuilder.appendQueryParameter("to", end);
        uriBuilder.appendQueryParameter("q", "+" + key);

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


    private void showDatePickerDialog(){

        DatePickerDialog datePicker = new DatePickerDialog(
                getContext(),
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH ),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        DatePicker datepicker = datePicker.getDatePicker();
        datepicker.setMaxDate(new Date().getTime());
        datePicker.show();
    }
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        if ("Begin".equals(selectedDate)) {
            month=month+1;
            String date = year+"-"+month+"-"+dayOfMonth;
            beginDate.setText(date);
        } else {
            month=month+1;
            String date = year+"-"+month+"-"+dayOfMonth;
            endDate.setText(date);
        }


    }

}