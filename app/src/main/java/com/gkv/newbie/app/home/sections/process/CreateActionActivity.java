package com.gkv.newbie.app.home.sections.process;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.Toast;

import com.gkv.newbie.R;
import com.gkv.newbie.app.home.BaseNavigationActivity;
import com.gkv.newbie.model.Action;
import com.gkv.newbie.model.Process;
import com.gkv.newbie.model.Step;
import com.gkv.newbie.modelmanager.ProcessHolder;
import com.gkv.newbie.utils.Keyboard;
import com.gkv.newbie.utils.gson.POJO;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

public class CreateActionActivity extends BaseNavigationActivity {

    @BindView(R.id.nameBox)
    TextInputLayout nameBox;

    @BindView(R.id.stepsList)
    AutoCompleteTextView stepsList;

    Process process;

    Step step;

    Action action;

    ArrayAdapter<String> arrayAdapter;

    int selection=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_action);

        try {

            ButterKnife.bind(this);

            process = ProcessHolder.getInstance().getProcess();
            step = ProcessHolder.getInstance().getStep();
            action = ProcessHolder.getInstance().getAction();

            init();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void init() {

        arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                process.stepList());

        stepsList.setAdapter(arrayAdapter);

        stepsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selection = i;
            }
        });

        nameBox.getEditText().setText(action.getName());
        try {
            selection = process.stepList().indexOf(process.getStepById(action.getStepId()).getTitle());
            stepsList.setText(process.getStepById(action.getStepId()).getTitle(),false);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @OnClick(R.id.saveButton)
    public void save(){
        Keyboard.closeKeyboard(this);
        try {
            refreshAction();

            if(action.getName().length()==0){
                Snackbar.make(getRoot(),"Name cant be empty",Snackbar.LENGTH_LONG).show();
                return;
            }

            Action pre = process.getActionByName(action.getName());

            if(pre != null){
                if(pre.getStepId().equals(action.getStepId()) == false){
                    Snackbar.make(getRoot(),"Action exists with that name",Snackbar.LENGTH_LONG).show();
                    return;
                }
            }

            if(process.hasStep(action.getStepId()) == false){
                Snackbar.make(getRoot(),"Invalid Step",Snackbar.LENGTH_LONG).show();
                return;
            }

//            if(process.hasAction(action.getId()) == false){
//                process.putAction(action);
//            }

            process.putStepActionAssociation(step,action);

            finish();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void refreshAction() {
        action.setName(nameBox.getEditText().getText().toString());
        action.setStepId(process.getStepByTitle(process.stepList().get(selection)).getId());
    }

}