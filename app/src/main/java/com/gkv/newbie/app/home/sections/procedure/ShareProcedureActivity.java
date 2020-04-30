package com.gkv.newbie.app.home.sections.procedure;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.gkv.newbie.R;
import com.gkv.newbie.app.home.BaseNavigationActivity;
import com.gkv.newbie.app.home.sections.user.ViewUserActivity;
import com.gkv.newbie.model.Procedure;
import com.gkv.newbie.model.User;
import com.gkv.newbie.utils.Keyboard;
import com.gkv.newbie.utils.gson.POJO;
import com.gkv.newbie.utils.internet.ResponseHandler;
import com.gkv.newbie.utils.internet.Server;
import com.gkv.newbie.utils.locallists.FavouriteManager;
import com.gkv.newbie.utils.locallists.HistoryManager;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

public class ShareProcedureActivity extends BaseNavigationActivity {

    @BindView(R.id.searchBox)
    TextInputLayout textInputLayout;

    @BindView(R.id.searchBoxTv)
    AppCompatAutoCompleteTextView autoTextView;

    @BindView(R.id.listView)
    ListView listView;

    public static final String DATA = "DATA" ;

    private Procedure procedure;
    private ArrayList<String> emailList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_procedure);
        ButterKnife.bind(this);
        emailList = new ArrayList<String>();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.select_dialog_item, HistoryManager.getInstance().getHistoryList());
        autoTextView.setThreshold(1);
        autoTextView.setAdapter(adapter);

        procedure = null;
        try {
            procedure = POJO.getInstance().fromJson(getIntent().getExtras().getString(DATA,"{}"),Procedure.class);
            loadShareList();
        }catch (Exception e){
            e.printStackTrace();
            onBackPressed();
        }



    }

    private void loadShareList() {
        if(procedure.get_id()==null){
            onBackPressed();
            return;
        }
        Server.getInstance().readProcedure(this, procedure.get_id(),
                new ResponseHandler() {
                    @Override
                    public void onCallback(String response) {

                        try{
                            procedure.setSharedAccess(
                                    POJO.getInstance().fromJson(response,Procedure.class).getSharedAccess()
                            );
                            loadShareEmailList();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                },
                new ResponseHandler() {
                    @Override
                    public void onCallback(String response) {

                    }
                }
        );
    }

    private void loadShareEmailList() {
        emailList.clear();
        for(User u:procedure.getSharedAccess()){
            emailList.add(u.getEmail());
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                emailList);

        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                revokeAccess(emailList.get(i));
            }
        });
    }

    private void revokeAccess(String s) {
        Server.getInstance().revokeAccessProcedure(
                this,
                procedure.get_id(),
                s.trim().toLowerCase(),
                new ResponseHandler() {
                    @Override
                    public void onCallback(String response) {
                        Snackbar.make(getRoot(),"Access is revoked.",Snackbar.LENGTH_LONG).show();
                    }
                },
                new ResponseHandler() {
                    @Override
                    public void onCallback(String error) {
                        Snackbar.make(getRoot(),error,Snackbar.LENGTH_LONG).show();
                    }
                }
        );
    }

    @OnClick(R.id.shareButton)
    public void shareProcedure(){
        Keyboard.closeKeyboard(this);
        Server.getInstance().giveAccessProcedure(
                this,
                procedure.get_id(),
                textInputLayout.getEditText().getText().toString().trim().toLowerCase(),
                new ResponseHandler() {
                    @Override
                    public void onCallback(String response) {
                        Snackbar.make(getRoot(),"Procedure is shared.",Snackbar.LENGTH_LONG).show();
                    }
                },
                new ResponseHandler() {
                    @Override
                    public void onCallback(String error) {
                        Snackbar.make(getRoot(),error,Snackbar.LENGTH_LONG).show();
                    }
                }
        );
    }
}