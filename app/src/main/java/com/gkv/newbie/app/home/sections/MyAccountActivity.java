package com.gkv.newbie.app.home.sections;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.gkv.newbie.R;
import com.gkv.newbie.app.auth.LoginActivity;
import com.gkv.newbie.app.auth.PostLoginActivity;
import com.gkv.newbie.app.home.BaseNavigationActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MyAccountActivity extends BaseNavigationActivity {

    private GoogleSignInClient mGoogleSignInClient;

    @BindView(R.id.email)
    TextView email;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.phoneNumber)
    TextView phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);

        ButterKnife.bind(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user==null){
            startActivity(new Intent(MyAccountActivity.this, LoginActivity.class));
            finish();
            return;
        }

        email.setText(user.getEmail());
        name.setText(user.getDisplayName());
        phoneNumber.setText(user.getPhoneNumber());

    }

    @OnClick(R.id.signoutButton)
    public void signOut() {
        // Firebase sign out
        FirebaseAuth.getInstance().signOut();

        // Google sign out
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        startActivity(new Intent(MyAccountActivity.this, LoginActivity.class));
                        finish();
                    }
                });
    }
}