package com.example.newsapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseUser;

public class InfoFragment extends Fragment {

    View rootview;
    TextView tvName, tvEmail;
    Button button;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.fragment_info,container,false);

        ParseUser currentUser = ParseUser.getCurrentUser();
        tvName = rootview.findViewById(R.id.tvName);
        tvEmail = rootview.findViewById(R.id.tvEmail);

        if(currentUser!=null){
            tvName.setText(currentUser.getString("name"));
            tvEmail.setText(currentUser.getEmail());
        }

        //set log out button, when you press the button, go back to the log in activity.

        button = rootview.findViewById(R.id.logout_button);
        button.setOnClickListener(new AdapterView.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();

                if (activeNetwork != null && activeNetwork.isConnected()){
                    ProgressDialog progress = new ProgressDialog(getContext());
                    progress.setMessage("Loading ...");
                    progress.show();
                    ParseUser.logOut();

                    Intent intent = new Intent(rootview.getContext(), LoginActivity.class);
                    startActivity(intent);
                    //finish();
                    progress.dismiss();
                } else {
                    Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                }

            }
        });


        return rootview;
    }
    /*public void logout(View view) {
        ProgressDialog progress = new ProgressDialog(getContext());
        progress.setMessage("Loading ...");
        progress.show();
        ParseUser.logOut();
        Intent intent = new Intent(rootview.getContext(), LoginActivity.class);
        startActivity(intent);
        finish();
        progress.dismiss();
    }*/
}
