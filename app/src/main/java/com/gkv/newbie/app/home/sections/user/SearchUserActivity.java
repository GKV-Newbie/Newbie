package com.gkv.newbie.app.home.sections.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.gkv.newbie.R;
import com.gkv.newbie.app.home.BaseNavigationActivity;
import com.gkv.newbie.utils.internet.Server;
import com.gkv.newbie.utils.locallists.HistoryManager;
import com.google.android.material.textfield.TextInputLayout;

public class SearchUserActivity extends BaseNavigationActivity {

    @BindView(R.id.searchBox)
    TextInputLayout textInputLayout;

    @BindView(R.id.searchBoxTv)
    AppCompatAutoCompleteTextView autoTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);
        ButterKnife.bind(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.select_dialog_item, HistoryManager.getInstance().getHistoryList());
        autoTextView.setThreshold(1);
        autoTextView.setAdapter(adapter);
    }

    @OnClick(R.id.searchButton)
    public void searchUser(){
        String email = textInputLayout.getEditText().getText().toString().trim().toLowerCase();
        if(email.length()==0){
            Toast.makeText(this,"Email cant be empty",Toast.LENGTH_LONG).show();
            return;
        }
        Intent i = new Intent(this,ViewUserActivity.class);
        i.putExtra(ViewUserActivity.USER_EMAIL,email);
        startActivity(i);
    }
}