package com.scanba.hibuddy.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.facebook.internal.CallbackManagerImpl;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.scanba.hibuddy.R;
import com.scanba.hibuddy.fragments.FacebookLoginFragment;
import com.scanba.hibuddy.models.Chat;

public class MainActivity extends AppCompatActivity {

    public FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener fireBaseAuthListener;

    private LinearLayout loginButtons;
    private LinearLayout loader;

    private FacebookLoginFragment facebookLoginFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginButtons = (LinearLayout) findViewById(R.id.login_buttons);
        loader = (LinearLayout) findViewById(R.id.loader);

        firebaseAuth = FirebaseAuth.getInstance();

        setupFireBaseListener();
    }

    @Override
    public void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(fireBaseAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (fireBaseAuthListener != null) {
            firebaseAuth.removeAuthStateListener(fireBaseAuthListener);
        }
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        String fragmentSimpleName = fragment.getClass().getSimpleName();
        if (fragmentSimpleName.equals("FacebookLoginFragment")) {
            facebookLoginFragment = (FacebookLoginFragment) fragment;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(CallbackManagerImpl.RequestCodeOffset.Login.toRequestCode() == requestCode)
            facebookLoginFragment.onActivityResult(requestCode, resultCode, data);

    }


    private void setupFireBaseListener() {
        fireBaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Chat.save(FirebaseDatabase.getInstance().getReference(), user, "Joined chat.");
                    Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                    startActivity(intent);
                }
            }
        };
    }

    public void showLoader() {
        loginButtons.setVisibility(LinearLayout.INVISIBLE);
        loader.setVisibility(LinearLayout.VISIBLE);
    }

    public void hideLoader() {
        loader.setVisibility(LinearLayout.INVISIBLE);
        loginButtons.setVisibility(LinearLayout.VISIBLE);
    }
}
