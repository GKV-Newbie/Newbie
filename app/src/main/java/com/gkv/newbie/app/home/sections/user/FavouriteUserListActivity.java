package com.gkv.newbie.app.home.sections.user;

import butterknife.BindView;
import butterknife.ButterKnife;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.gkv.newbie.R;
import com.gkv.newbie.app.home.BaseNavigationActivity;
import com.gkv.newbie.utils.locallists.FavouriteManager;

import java.util.ArrayList;

public class FavouriteUserListActivity extends BaseNavigationActivity {

    @BindView(R.id.listView)
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_user_list);
        ButterKnife.bind(this);
        ArrayList<String> list = FavouriteManager.getInstance().getFavouritesList();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                list);

        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i1, long l) {
                Intent i = new Intent(FavouriteUserListActivity.this,ViewUserActivity.class);
                i.putExtra(ViewUserActivity.USER_EMAIL,list.get(i1));
                startActivity(i);
            }
        });

    }
}