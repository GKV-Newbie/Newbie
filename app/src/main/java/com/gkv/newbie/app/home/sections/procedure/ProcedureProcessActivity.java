package com.gkv.newbie.app.home.sections.procedure;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gkv.newbie.R;
import com.gkv.newbie.app.home.BaseNavigationActivity;
import com.gkv.newbie.app.home.sections.process.ProcessInstructionsActivity;
import com.gkv.newbie.model.Procedure;
import com.gkv.newbie.model.Process;
import com.gkv.newbie.modelmanager.ProcessHolder;
import com.gkv.newbie.utils.auth.UserManager;
import com.gkv.newbie.utils.gson.POJO;
import com.gkv.newbie.utils.internet.ResponseHandler;
import com.gkv.newbie.utils.internet.Server;

public class ProcedureProcessActivity extends BaseNavigationActivity {

    public static final String DATA = "DATA";

    Procedure procedure;

    @BindView(R.id.name)
    TextView name;

    @BindView(R.id.owner)
    TextView owner;

    @BindView(R.id.ownerControls)
    LinearLayout ownerControls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_procedure_process);
        ButterKnife.bind(this);
        loadParams();

    }

    @Override
    protected void onStart() {
        super.onStart();
        loadParams();
    }

    private void loadParams() {
        try {
            procedure = POJO.getInstance().fromJson(getIntent().getExtras().getString(DATA,"{}"),Procedure.class);
            loadDetails();
        }catch (Exception e){

        }
    }

    private void loadDetails() {
        Server.getInstance().readProcedure(this, procedure.get_id(),
                new ResponseHandler() {
                    @Override
                    public void onCallback(String response) {
                        try{
                            procedure = POJO.getInstance().fromJson(response,Procedure.class);
                            name.setText(procedure.getFormattedName());
                            owner.setText(procedure.getOwner().getEmail());
                            if(UserManager.getInstance().getEmail().equals(procedure.getOwner().getEmail()) == false)
                                ownerControls.setVisibility(View.GONE);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                },
                new ResponseHandler() {
                    @Override
                    public void onCallback(String error) {
                        System.out.println(error);
                        name.setText(procedure.getFormattedName());
                        owner.setText(procedure.getOwner().getEmail());
                        ownerControls.setVisibility(View.GONE);
                    }
                }
        );

    }

    @OnClick(R.id.openProcessButton)
    public void openProcess(){
        try {
            Process process = POJO.getInstance().fromJson(procedure.getProcess(), Process.class);
            if(process.getHeadStepTitle().length()==0){
                Toast.makeText(this,"Unable to load process at the moment. Please try again.",Toast.LENGTH_LONG).show();
                return;
            }
            ProcessHolder.getInstance().setProcess(
                    process
            );
            startActivity(new Intent(this, ProcessInstructionsActivity.class));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @OnClick(R.id.updateProcedureButton)
    public void updateProcedure(){
        Intent i = new Intent(this,CreateProcedureActivity.class);
        //Procedure procedure = procedureManager.getThinProcedure();
        if(procedure.get_id()==null)
            return;
        i.putExtra(CreateProcedureActivity.DATA,POJO.getInstance().toJson(procedure));
        i.putExtra(CreateProcedureActivity.UPDATE,true);
        startActivity(i);
    }

    @OnClick(R.id.shareProcedureButton)
    public void shareProcedure(){
        Intent i = new Intent(this,ShareProcedureActivity.class);
        //Procedure procedure = procedureManager.getThinProcedure();
        if(procedure.get_id()==null)
            return;
        i.putExtra(ShareProcedureActivity.DATA,POJO.getInstance().toJson(procedure));
        startActivity(i);
    }

}