package com.gkv.newbie.app.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gkv.newbie.R;
import com.gkv.newbie.app.home.sections.ProcedureGroupActivity;
import com.gkv.newbie.utils.auth.UserManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class PostLoginActivity extends AppCompatActivity {

    private GoogleSignInClient mGoogleSignInClient;

    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;

    @BindView(R.id.centerTitle)
    TextView textView;

    CountDownTimer mCountDownTimer;

    int i=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_login);

        //getSupportActionBar().hide();

        ButterKnife.bind(this);

        textView.setText("Hi "+FirebaseAuth.getInstance().getCurrentUser().getDisplayName());

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        goAhead();

    }

    private void goAhead() {
        final Handler handler = new Handler();

        handler.post(new Runnable() {
            @Override
            public void run() {
                mProgressBar.setProgress(i);
                if(i==100){
                    timerOut();
                }else{
                    i++;
                    handler.postDelayed(this,50);
                }
            }
        });
    }

    private void timerOut() {
        startActivity(new Intent(this, ProcedureGroupActivity.class));
        finish();
    }

    @OnClick(R.id.signoutButton)
    public void signOut() {
        // Firebase sign out
        i=1000;
        UserManager.getInstance().setAuthToken("");

        FirebaseAuth.getInstance().signOut();

        // Google sign out
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        startActivity(new Intent(PostLoginActivity.this,LoginActivity.class));
                        finish();
                    }
                });
    }

}