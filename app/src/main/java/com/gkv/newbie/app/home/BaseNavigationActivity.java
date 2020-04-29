package com.gkv.newbie.app.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gkv.newbie.R;
import com.gkv.newbie.app.home.sections.procedure.ProcedureGroupActivity;
import com.gkv.newbie.app.home.sections.user.FavouriteUserListActivity;
import com.gkv.newbie.app.home.sections.user.SearchUserActivity;
import com.gkv.newbie.app.home.sections.user.ViewUserActivity;
import com.google.android.material.navigation.NavigationView;

import java.util.HashMap;

public class BaseNavigationActivity extends AppCompatActivity {

    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_base_navigation);

        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        for (int i = 0 ; i < toolbar.getChildCount() ; i++ ){
            try {
                ((TextView)toolbar.getChildAt(i)).setTypeface(Typeface.createFromAsset(getAssets(), "kellyslab.ttf"));
                break;
            }catch (Exception e){

            }
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        final HashMap<Integer,Class> map = new HashMap<>();
        map.put(R.id.nav_my_account,ViewUserActivity.class);
        map.put(R.id.nav_search_user, SearchUserActivity.class);
        map.put(R.id.nav_favourite_user, FavouriteUserListActivity.class);
        map.put(R.id.nav_my_procedures, ProcedureGroupActivity.class);
        map.put(R.id.nav_shared_procedures, ProcedureGroupActivity.class);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                System.out.println(item.getTitle());

                Intent i = new Intent(BaseNavigationActivity.this,
                        map.containsKey(item.getItemId())?
                                map.get(item.getItemId()):
                                ViewUserActivity.class
                );

                if(item.getItemId()==R.id.nav_shared_procedures){
                    i.putExtra(ProcedureGroupActivity.SHOW_SHARED,true);
                }

                startActivity(i);

                finish();
                return false;
            }
        });

        mDrawerToggle = new ActionBarDrawerToggle(this, drawer,toolbar ,  R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                //getActionBar().setTitle(mTitle);
                //invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                //getActionBar().setTitle(mDrawerTitle);
                //invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };


        // Set the drawer toggle as the DrawerListener
        drawer.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public boolean onSupportNavigateUp() {
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        System.out.println(item.getTitle());
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setContentView(int layout){
        LinearLayout l = (LinearLayout) findViewById(R.id.main_include_layout);
        LayoutInflater linf = LayoutInflater.from(this);
        l.addView(linf.inflate(layout,null),new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
    }

}