package com.gkv.newbie.app.home.sections.procedure;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gkv.newbie.R;
import com.gkv.newbie.app.home.BaseNavigationActivity;
import com.gkv.newbie.app.home.sections.procedure.adapter.ProcedureAdapter;
import com.gkv.newbie.model.Procedure;
import com.gkv.newbie.model.Process;
import com.gkv.newbie.modelmanager.ProcedureManager;
import com.gkv.newbie.modelmanager.ProcessHolder;
import com.gkv.newbie.utils.gson.POJO;
import com.gkv.newbie.utils.internet.ResponseHandler;
import com.gkv.newbie.utils.internet.Server;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProcedureGroupActivity extends BaseNavigationActivity {

    @BindView(R.id.procedureTitle)
    TextView procedureTitle;

    @BindView(R.id.listView)
    ListView listView;

    @BindView(R.id.ownerControls)
    LinearLayout ownerControls;

    public static final String USER_EMAIL = "USER_EMAIL";
    public static final String SHOW_SHARED = "SHOW_SHARED";

    ResponseHandler successHandler,failureHandler;

    ProcedureManager procedureManager;
    private ProcedureAdapter adapter;
    private String userEmail;
    private boolean showShared;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_procedure_group);
        ButterKnife.bind(this);

        init();

    }

    private void init(){
        successHandler = new ResponseHandler() {
            @Override
            public void onCallback(String response) {
            ArrayList<Procedure> list = new ArrayList<>();
            try {
                JSONArray jsonArray = new JSONArray(response);
                System.out.println(jsonArray.toString(3));
                for(int i=0;i<jsonArray.length();i++){
                    list.add(POJO.getInstance().fromJson(jsonArray.getJSONObject(i).toString(),Procedure.class));
                }
                System.out.println(
                        new JSONArray(
                                POJO.getInstance().toJson(list)
                        ).toString(3)
                );
            } catch (JSONException e) {
                e.printStackTrace();
            }
            procedureManager = new ProcedureManager(list);
            listInit();
            }
        };
        failureHandler = new ResponseHandler() {
            @Override
            public void onCallback(String error) {
            Toast.makeText(ProcedureGroupActivity.this,error,Toast.LENGTH_LONG).show();
            }
        };

        loadList();
    }

    private void loadList() {
        userEmail = "";

        try {
            userEmail = getIntent().getExtras().getString(USER_EMAIL,"");
        }catch (Exception e){

        }

        if(userEmail.equals("") == false){
            listUserProcedures(userEmail);
            return;
        }

        showShared = false;

        try {
            showShared = getIntent().getExtras().getBoolean(SHOW_SHARED,false);
        }catch (Exception e){

        }

        listProcedures(showShared);
    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            loadList();
        }catch (Exception e){
            
        }
    }

    private void listProcedures(boolean showShared) {
        if(showShared){
            ownerControls.setVisibility(View.GONE);
            Server.getInstance().listSharedProcedures(this,successHandler,failureHandler);
        }else{
            Server.getInstance().listMyProcedures(this,successHandler,failureHandler);
        }
    }

    private void listUserProcedures(String userEmail) {
        ownerControls.setVisibility(View.GONE);
        Server.getInstance().listUserProcedures(this,userEmail,successHandler,failureHandler);
    }

    private void listInit() {
        procedureTitle.setText(procedureManager.getCurrentProcedure().getFormattedName());

        ArrayList<Procedure> list = procedureManager.getChildren();

        adapter = new ProcedureAdapter(
                this,
                android.R.layout.simple_list_item_1,
                list);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                try {

                    Procedure procedure = adapter.getItem(i);
                    if(procedure.getProcedureType().equals("group")) {
                        procedureManager.setCurrentProcedure(procedure);
                        updateList();
                    }else{
                        Intent intent = new Intent(ProcedureGroupActivity.this,ProcedureProcessActivity.class);

                        Procedure proc = new Procedure();
                        proc.set_id(procedure.get_id());
                        proc.setName(procedure.getName());
                        proc.setOwner(procedure.getOwner());
                        proc.setProcess(procedure.getProcess());

                        intent.putExtra(ProcedureProcessActivity.DATA,
                                POJO.getInstance().toJson(proc)
                                );
                        startActivity(intent);
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Procedure procedure = adapter.getItem(i);
                return false;
            }
        });
    }

    public void updateList(){
        adapter.clear();
        adapter.addAll(procedureManager.getChildren());
        adapter.notifyDataSetChanged();
        procedureTitle.setText(procedureManager.getCurrentProcedure().getFormattedName());
    }

    @Override
    public void onBackPressed() {
        if(procedureManager.canGoBack()){
            procedureManager.goBack();
            updateList();
        }
    }

    @OnClick(R.id.createProcedureButton)
    public void createProcedure(){
        Intent i = new Intent(this,CreateProcedureActivity.class);
        Procedure procedure = procedureManager.getThinProcedure();
        i.putExtra(CreateProcedureActivity.DATA,POJO.getInstance().toJson(procedure));
        i.putExtra(CreateProcedureActivity.UPDATE,false);
        startActivity(i);
    }

    @OnClick(R.id.updateProcedureButton)
    public void updateProcedure(){
        Intent i = new Intent(this,CreateProcedureActivity.class);
        Procedure procedure = procedureManager.getThinProcedure();
        if(procedure.get_id()==null)
            return;
        i.putExtra(CreateProcedureActivity.DATA,POJO.getInstance().toJson(procedure));
        i.putExtra(CreateProcedureActivity.UPDATE,true);
        startActivity(i);
    }

    @OnClick(R.id.shareProcedureButton)
    public void shareProcedure(){
        Intent i = new Intent(this,ShareProcedureActivity.class);
        Procedure procedure = procedureManager.getThinProcedure();
        if(procedure.get_id()==null)
            return;
        i.putExtra(ShareProcedureActivity.DATA,POJO.getInstance().toJson(procedure));
        startActivity(i);
    }
}
