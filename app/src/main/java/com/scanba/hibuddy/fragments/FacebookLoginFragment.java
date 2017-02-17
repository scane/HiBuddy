package com.scanba.hibuddy.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.scanba.hibuddy.R;
import com.scanba.hibuddy.activities.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class FacebookLoginFragment extends Fragment {


    private LoginButton facebookLoginButton;
    private CallbackManager facebookCallbackManager;
    private AccessTokenTracker facebookTracker;
    private MainActivity activity;

    public FacebookLoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_facebook_login, container, false);

        activity = (MainActivity) getActivity();

        facebookCallbackManager = CallbackManager.Factory.create();
        facebookLoginButton = (LoginButton) view.findViewById(R.id.login_with_facebook_button);
        facebookLoginButton.setReadPermissions("email", "public_profile");

        setupFacebookTracker();
        setupFacebookListener();
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        facebookCallbackManager.onActivityResult(requestCode, resultCode, data);
    }


    private void setupFacebookTracker() {
        facebookTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken accessToken, AccessToken accessToken2) {
                if (accessToken2 == null) {
                    FirebaseAuth.getInstance().signOut();
                }
            }
        };
    }

    private void setupFacebookListener() {
        facebookLoginButton.registerCallback(facebookCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                activity.showLoader();
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d("test", "test");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("test", "test");
            }
        });
    }

    private void handleFacebookAccessToken(AccessToken token) {

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        activity.firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (!task.isSuccessful()) {
                            activity.hideLoader();
                        }
                    }
                });
    }

}
