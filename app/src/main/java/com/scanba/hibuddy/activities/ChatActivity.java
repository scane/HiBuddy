package com.scanba.hibuddy.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.scanba.hibuddy.R;
import com.scanba.hibuddy.adapters.ChatHistoryAdapter;
import com.scanba.hibuddy.models.Chat;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {

    private DatabaseReference fireBaseDatabase;
    private EditText message;
    private Profile profile;
    private Query chatQuery;
    private RecyclerView chatHistory;
    private LinearLayoutManager layoutManager;
    private ChatHistoryAdapter chatHistoryAdapter;
    private ArrayList<Chat> chats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        chats = new ArrayList<>();
        chatHistory = (RecyclerView) findViewById(R.id.chat_history);
        layoutManager = new LinearLayoutManager(this);
        chatHistory.setLayoutManager(layoutManager);
        chatHistoryAdapter = new ChatHistoryAdapter(this, chats);
        chatHistory.setAdapter(chatHistoryAdapter);


        fireBaseDatabase = FirebaseDatabase.getInstance().getReference();
        message = (EditText) findViewById(R.id.message);
        profile = Profile.getCurrentProfile();
        setupChatListener();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.chat_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                //Signout from FireBase
                FirebaseAuth.getInstance().signOut();
                //Signout from Facebook
                LoginManager.getInstance().logOut();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

    }

    private void setupChatListener() {
        chatQuery = fireBaseDatabase.child("chat").orderByChild("sentAt").startAt(System.currentTimeMillis());
        chatQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Chat chat = dataSnapshot.getValue(Chat.class);
                int position = chatHistoryAdapter.add(chat);
                chatHistory.smoothScrollToPosition(position);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void sendMessage(View v) {
        Chat.save(fireBaseDatabase, profile, message.getText().toString());
        message.setText("");
    }
}
