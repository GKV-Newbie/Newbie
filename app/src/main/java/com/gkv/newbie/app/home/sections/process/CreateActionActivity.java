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
import com.gkv.newbie.utils.gson.POJO;
import com.google.android.material.textfield.TextInputLayout;

public class CreateActionActivity extends BaseNavigationActivity {

    @BindView(R.id.titleBox)
    TextInputLayout titleBox;

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

        ButterKnife.bind(this);

        process = ProcessHolder.getInstance().getProcess();
        step = ProcessHolder.getInstance().getStep();
        action = ProcessHolder.getInstance().getAction();

        init();

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

        titleBox.getEditText().setText(action.getName());
        selection = process.stepList().indexOf(action.getStepTitle());
        stepsList.setText(action.getStepTitle(),false);
    }

    @OnClick(R.id.saveButton)
    public void save(){
        try {
            refreshAction();

            if(action.getName().length()==0){
                Toast.makeText(this,"Name cant be empty",Toast.LENGTH_LONG).show();
                return;
            }

            if(process.hasAction(action.getName())){
                if(process.getActionByTitle(action.getName()).getStepTitle().equals(action.getStepTitle()) == false){
                    Toast.makeText(this,"Action exists with that name",Toast.LENGTH_LONG).show();
                    return;
                }
            }

            if(process.hasStep(action.getStepTitle()) == false){
                Toast.makeText(this,"Invalid Step",Toast.LENGTH_LONG).show();
                return;
            }

            process.putStepActionAssociation(step,action);

            finish();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void refreshAction() {
        action = new Action(
                titleBox.getEditText().getText().toString(),
                process.stepList().get(selection)
        );
    }

}