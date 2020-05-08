package com.example.newsapplication;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChatFragment extends Fragment {

    View rootview;
    Button button;
    private final String currentUser = ParseUser.getCurrentUser().getString("name");

    ArrayList<String> messages = new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.fragment_chat,container,false);

        ListView chatListView = (ListView) rootview.findViewById(R.id.chatListView);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Message");
        //query.whereEqualTo("sender", currentUser);
        query.whereLessThanOrEqualTo("createdAt", new Date());
        query.setLimit(15);
        query.orderByAscending("createdAt");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    for(int i= 0 ; i < objects.size(); i++){
                        String message = objects.get(i).getString("content");
                        String sender = objects.get(i).getString("sender");
                        messages.add(sender + ": " + message.toString());
                    }

                } else {
                    // Something is wrong
                }
            }
        });

        arrayAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, messages);
        chatListView.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();


        button = rootview.findViewById(R.id.sendChatButton);
        button.setOnClickListener(new AdapterView.OnClickListener(){
            @Override
            public void onClick(View view) {
                EditText chatEditText = (EditText) rootview.findViewById(R.id.chatEditText);
                final String messageContent = chatEditText.getText().toString();

                ParseObject message = new ParseObject("Message");
                message.put("sender", currentUser);
                message.put("content", messageContent);

                chatEditText.setText("");

                message.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {

                        if (e == null) {
                            messages.remove(0);
                            messages.add(currentUser + ": " + messageContent);

                            arrayAdapter.notifyDataSetChanged();

                        }

                    }
                });

            }
        });


        return rootview;
    }
}
