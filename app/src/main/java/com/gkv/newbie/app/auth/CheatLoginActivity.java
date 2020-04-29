package com.gkv.newbie.app.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.gkv.newbie.R;
import com.gkv.newbie.app.home.sections.user.ViewUserActivity;
import com.gkv.newbie.utils.auth.UserManager;
import com.gkv.newbie.utils.internet.ResponseHandler;
import com.gkv.newbie.utils.internet.Server;
import com.gkv.newbie.utils.locallists.FavouriteManager;

import org.json.JSONObject;

import java.util.ArrayList;

public class CheatLoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ListView listView = new ListView(this);
        setContentView(listView);
        final ArrayList<String> list = new ArrayList<>();

        for(int i=1;i<=6;i++){
            list.add("fake."+i+"@email.com");
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                list);

        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                loginEmail(list.get(i));
            }
        });
    }

    private void loginEmail(String email) {
        Server.getInstance().loginUser(this,
                email,
                "abc",
                new ResponseHandler() {
                    @Override
                    public void onCallback(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            UserManager.getInstance().setAuthToken(jsonObject.getString("accessToken"));
                            UserManager.getInstance().setEmail(email);
                            startActivity(new Intent(CheatLoginActivity.this, ViewUserActivity.class));
                            //finish();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                },
                new ResponseHandler() {
                    @Override
                    public void onCallback(String error) {

                    }
                });
    }
}