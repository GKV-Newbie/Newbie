package com.gkv.newbie.app.home.sections.procedure;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.gkv.newbie.R;
import com.gkv.newbie.app.home.BaseNavigationActivity;
import com.gkv.newbie.app.home.sections.process.CreateProcessActivity;
import com.gkv.newbie.model.Procedure;
import com.gkv.newbie.model.Process;
import com.gkv.newbie.modelmanager.ProcessHolder;
import com.gkv.newbie.utils.Keyboard;
import com.gkv.newbie.utils.auth.UserManager;
import com.gkv.newbie.utils.gson.POJO;
import com.gkv.newbie.utils.internet.ResponseHandler;
import com.gkv.newbie.utils.internet.Server;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONObject;

public class CreateProcedureActivity extends BaseNavigationActivity {

    public static final String UPDATE = "UPDATE" ;
    public static final String DATA = "DATA" ;

    private Procedure procedure;
    private boolean update;

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.nameBox)
    TextInputLayout nameBox;

    @BindView(R.id.sharingType)
    ChipGroup sharingType;

    @BindView(R.id.procedureType)
    ChipGroup procedureType;

    @BindView(R.id.submitButton)
    Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_procedure);

        ButterKnife.bind(this);

        loadParams();

    }

    private void loadParams() {

        update = false;
        try {
            update = getIntent().getExtras().getBoolean(UPDATE,false);
        }catch (Exception e){

        }

        procedure = null;
        try {
            procedure = POJO.getInstance().fromJson(getIntent().getExtras().getString(DATA,"{}"),Procedure.class);
        }catch (Exception e){

        }

        loadPage();

    }

    private void loadPage() {
        if(update){
            title.setText("Update Procedure");
            submitButton.setText("Update Procedure");
            loadProcedureData();
        }else{
            title.setText("Create Procedure");
            submitButton.setText("Create Procedure");
        }
    }

    private void loadProcedureData() {

        Server.getInstance().readProcedure(this, procedure.get_id(),
                new ResponseHandler() {
                    @Override
                    public void onCallback(String response) {

                        try{
                            procedure = POJO.getInstance().fromJson(response,Procedure.class);
                            nameBox.getEditText().setText(procedure.getName());
                            sharingType.check(procedure.getShareType().equals("public")?R.id.public_chip:R.id.private_chip);
                            procedureType.check(procedure.getProcedureType().equals("group")?R.id.group_chip:R.id.process_chip);
                            ProcessHolder.getInstance().setProcess(
                                    POJO.getInstance().fromJson(procedure.getProcess(),Process.class)
                            );
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

    @OnClick(R.id.processButton)
    public void editProcess(){

        Keyboard.closeKeyboard(this);

        if(procedureType.getCheckedChipId() == R.id.group_chip)
            return;

        try {

            Process process = POJO.getInstance().fromJson(procedure.getProcess(),Process.class);
            System.out.println(process.getHeadStepTitle().toLowerCase());
            ProcessHolder.getInstance().setProcess(process);

        } catch (Exception e){
            ProcessHolder.getInstance().setProcess(new Process());
        }

        startActivity(new Intent(this, CreateProcessActivity.class));

    }



    @OnClick(R.id.submitButton)
    public void submitButton(){
        Keyboard.closeKeyboard(this);
        fetchInputDetails();
        if(update){
            submitUpdate();
        }else{
            submitCreate();
        }
    }

    private void fetchInputDetails() {
        procedure.setName(nameBox.getEditText().getText().toString());
        procedure.setShareType(sharingType.getCheckedChipId() == R.id.public_chip ? "public" : "private");
        procedure.setProcedureType(procedureType.getCheckedChipId() == R.id.group_chip ? "group" : "process");
        if(procedureType.getCheckedChipId() == R.id.process_chip)
            procedure.setProcess(POJO.getInstance().toJson(
                    ProcessHolder.getInstance().getProcess()
            ));
    }

    private void submitCreate() {
        procedure.setParent(procedure.get_id());
        Server.getInstance().createProcedure(
                this,
                procedure.getName(),
                procedure.getParent(),
                procedure.getShareType(),
                procedure.getProcedureType(),
                procedure.getProcess(),
                new ResponseHandler() {
                    @Override
                    public void onCallback(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            jsonObject.remove("owner");
                            Procedure temp = POJO.getInstance().fromJson(jsonObject.toString(),Procedure.class);
                            toast(temp.getName()+" is created.");
                        }catch (Exception e){
                            toast(e.toString());
                        }
                    }
                },
                new ResponseHandler() {
                    @Override
                    public void onCallback(String error) {
                        toast(error);
                    }
                }
        );
    }

    private void submitUpdate() {
        Server.getInstance().updateProcedure(
                this,
                procedure.get_id(),
                procedure.getName(),
                procedure.getShareType(),
                procedure.getProcedureType(),
                procedure.getProcess(),
                new ResponseHandler() {
                    @Override
                    public void onCallback(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            jsonObject.remove("owner");
                            Procedure temp = POJO.getInstance().fromJson(jsonObject.toString(),Procedure.class);
                            toast(temp.getName()+" is updated.");
                        }catch (Exception e){
                            toast(e.toString());
                        }
                    }
                },
                new ResponseHandler() {
                    @Override
                    public void onCallback(String error) {
                        toast(error);
                    }
                }
        );
    }

    public void toast(String msg){
        Snackbar.make(getRoot(),msg,Snackbar.LENGTH_LONG).show();
    }
}