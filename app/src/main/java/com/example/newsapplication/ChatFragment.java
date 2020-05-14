package com.example.newsapplication;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.provider.Telephony;
import android.text.InputType;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

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

import static androidx.core.content.ContextCompat.getSystemService;

public class ChatFragment extends Fragment {

    View rootview;
    Button button;
    private final String currentUser = ParseUser.getCurrentUser().getString("name");
    ArrayList<ModelChat> messages = new ArrayList<ModelChat>();
    ArrayAdapter<String> arrayAdapter;

    private ChatAdapter adp;
    private EditText txt;
    private Date lastMsgDate;
    //flag to hold if activity is running or not
    private boolean isRunning;
    private static Handler handler;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.fragment_chat,container,false);

        adp = new ChatAdapter();
        ListView chatListView = (ListView) rootview.findViewById(R.id.chatListView);
        chatListView.setAdapter(adp);
        chatListView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        chatListView.setStackFromBottom(true);

        txt = (EditText) rootview.findViewById(R.id.chatEditText);
        txt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE);

        handler = new Handler();

        button = rootview.findViewById(R.id.sendChatButton);
        button.setOnClickListener(new AdapterView.OnClickListener() {

            @Override
            public void onClick(View view) {
                sendMessage();
            };

        });

        return rootview;
    }

    @Override
    public void onResume(){
        super.onResume();
        isRunning = true;
        loadConversationList();
    }

    @Override
    public void onPause(){
        super.onPause();
        isRunning = false;
    }

    private void sendMessage() {
        if (txt.length() == 0)
            return;

        String s = txt.getText().toString();
        final ModelChat c = new ModelChat(s, new Date() , currentUser);
        c.setStatus(ModelChat.STATUS_SENDING);
        messages.add(c);
        adp.notifyDataSetChanged();
        txt.setText(null);

        ParseObject message = new ParseObject("Message");
        message.put("sender", currentUser);
        message.put("content", s);

        message.saveEventually(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null)
                    c.setStatus(ModelChat.STATUS_SENT);
                else
                    c.setStatus(ModelChat.STATUS_FAILED);
                adp.notifyDataSetChanged();
            }
        });

    }


    private void loadConversationList() {
//        ListView chatListView = (ListView) rootview.findViewById(R.id.chatListView);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Message");
        //query.whereEqualTo("sender", currentUser);
        query.whereLessThanOrEqualTo("createdAt", new Date());
        query.orderByDescending("createdAt");
        query.setLimit(15);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, ParseException e) {
                if (objects != null && objects.size() >0) {
                    for(int i= objects.size()-1 ; i >= 0; i--){
                        ParseObject po = objects.get(i);
                        ModelChat c = new ModelChat(po.getString("content"), po.getCreatedAt() , po.getString("sender"));
                        messages.add(c);
                        if (lastMsgDate == null
                                || lastMsgDate.before(c.getDate()))
                            lastMsgDate = c.getDate();
                        adp.notifyDataSetChanged();
                    }
                }
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run()
                    {
                        if (isRunning)
                            loadConversationList();
                    }
                },10000);
            }
        });

    }

    private class ChatAdapter extends BaseAdapter{

        @Override
        public int getCount(){
            return  messages.size();
        }

        @Override
        public ModelChat getItem(int arg0) {
            return messages.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {
            return arg0;
        }

        @Override
        public View getView(int pos, View v, ViewGroup arg2) {
            ModelChat c = getItem(pos);
            if (c.isSent())
                v = getLayoutInflater().inflate(R.layout.row_chat_right,null);
            else
                v = getLayoutInflater().inflate(R.layout.row_chat_left, null);

            TextView lbl = (TextView) v.findViewById(R.id.isSeentTv);
            lbl.setText(DateUtils.getRelativeDateTimeString(getActivity(),c.getDate().getTime(), DateUtils.SECOND_IN_MILLIS, DateUtils.DAY_IN_MILLIS,0));

            lbl = (TextView) v.findViewById(R.id.messageTv);
            lbl.setText(c.getMessage());

            lbl = (TextView) v.findViewById(R.id.nameLbl);
            lbl.setText(c.getSender());
            return v;
        }

    }

}
