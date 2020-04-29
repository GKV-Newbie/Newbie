package com.gkv.newbie.app.home.sections.user;

import androidx.annotation.NonNull;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gkv.newbie.R;
import com.gkv.newbie.app.auth.LoginActivity;
import com.gkv.newbie.app.home.BaseNavigationActivity;
import com.gkv.newbie.model.User;
import com.gkv.newbie.utils.auth.UserManager;
import com.gkv.newbie.utils.locallists.FavouriteManager;
import com.gkv.newbie.utils.gson.POJO;
import com.gkv.newbie.utils.internet.ResponseHandler;
import com.gkv.newbie.utils.internet.Server;
import com.gkv.newbie.utils.locallists.HistoryManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

public class ViewUserActivity extends BaseNavigationActivity {

    public static final String USER_EMAIL = "USER_EMAIL";

    private GoogleSignInClient mGoogleSignInClient;

    @BindView(R.id.email)
    TextView email;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.profile_image)
    ImageView imageView;

    @BindView(R.id.signoutButton)
    Button signoutButton;
    @BindView(R.id.updateButton)
    Button updateButton;
    @BindView(R.id.favouriteButton)
    Button favouriteButton;
    @BindView(R.id.proceduresButton)
    Button proceduresButton;

    User user;
    boolean self;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_user);

        ButterKnife.bind(this);



        String userEmail = "";

        try {
            userEmail = getIntent().getExtras().getString(USER_EMAIL,"");
        }catch (Exception e){

        }

        if(userEmail.equals("")||userEmail.equals(UserManager.getInstance().getEmail())){
            loadSelf();
        }else{
            loadUser(userEmail);
        }


    }

    private void loadUser(String userEmail) {
        Server.getInstance().userAccount(this,
                userEmail,
                new ResponseHandler() {
                    @Override
                    public void onCallback(String response) {
                        try {
                            loadUser(POJO.getInstance().fromJson(response,User.class),false);
                        }catch (Exception e){

                        }
                    }
                },
                new ResponseHandler() {
                    @Override
                    public void onCallback(String error) {
                        Log.e(ViewUserActivity.this.getLocalClassName(),error);
                        Toast.makeText(ViewUserActivity.this,"Having some trouble finding the requested user. Please try again",Toast.LENGTH_LONG).show();
                        onBackPressed();
                    }
                });
    }

    private void loadSelf() {
        /*FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user==null){
            startActivity(new Intent(ViewUserActivity.this, LoginActivity.class));
            finish();
            return;
        }*/
        Server.getInstance().myAccount(this,
                new ResponseHandler() {
                    @Override
                    public void onCallback(String response) {
                        try {
                            loadUser(POJO.getInstance().fromJson(response,User.class),true);
                        }catch (Exception e){

                        }
                    }
                },
                new ResponseHandler() {
                    @Override
                    public void onCallback(String error) {
                        Log.e(ViewUserActivity.this.getLocalClassName(),error);
                    }
                });
    }

    private void loadUser(User user,boolean self){
        HistoryManager.getInstance().addToHistoryList(user.getEmail());
        try {
            this.user = user;
            this.self = self;
            email.setText(user.getEmail());
            name.setText(user.getName());
            Picasso.get().load(user.getDisplayPicture()).into(imageView);
        }catch (Exception e){

        }
        if(self){
            signoutButton.setVisibility(View.VISIBLE);
            updateButton.setVisibility(View.VISIBLE);
            favouriteButton.setVisibility(View.GONE);
            proceduresButton.setVisibility(View.GONE);
        }else{
            signoutButton.setVisibility(View.GONE);
            updateButton.setVisibility(View.GONE);
            favouriteButton.setVisibility(View.VISIBLE);
            favouriteButton.setText((FavouriteManager.getInstance().existsInFavouritesList(user.getEmail())?"Un-":"")+"Favourite User");
            proceduresButton.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.signoutButton)
    public void signOut() {
        // Firebase sign out

        try {

            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build();

            mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

            FirebaseAuth.getInstance().signOut();

            // Google sign out
            mGoogleSignInClient.signOut().addOnCompleteListener(this,
                    new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            startActivity(new Intent(ViewUserActivity.this, LoginActivity.class));
                            finish();
                        }
                    });

        } catch (Exception e){
            startActivity(new Intent(ViewUserActivity.this, LoginActivity.class));
            finish();
        }

    }

    @OnClick(R.id.updateButton)
    public void update(){
        Intent i = new Intent(this,UpdateUserActivity.class);
        i.putExtra(UpdateUserActivity.USER_NAME,this.user.getName());
        i.putExtra(UpdateUserActivity.USER_DISPLAY_PICTURE,this.user.getDisplayPicture());
        startActivity(i);
    }

    @OnClick(R.id.favouriteButton)
    public void favourite(){
        try {
            FavouriteManager.getInstance().toggleFromFavouritesList(this.user.getEmail());
            System.out.println(FavouriteManager.getInstance().getFavouritesList());
            loadUser(this.user,this.self);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}