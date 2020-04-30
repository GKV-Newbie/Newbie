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

public class CreateStepActivity extends BaseNavigationActivity {

    @BindView(R.id.titleBox)
    TextInputLayout titleBox;

    @BindView(R.id.descriptionBox)
    TextInputLayout descriptionBox;

    Process process;
    Step step;

    @BindView(R.id.listView)
    ListView listView;
    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_step);

        ButterKnife.bind(this);

        process = ProcessHolder.getInstance().getProcess();
        step = ProcessHolder.getInstance().getStep();

        init();

    }

    private void init() {

        arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                process.getActionsOfStep(step));

        listView.setAdapter(arrayAdapter);

        titleBox.getEditText().setText(step.getTitle());
        descriptionBox.getEditText().setText(step.getDescription());

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ProcessHolder.getInstance().setAction(
                        process.getActionByName(process.getActionsOfStep(step).get(i))
                );
                startActivity(new Intent(CreateStepActivity.this,CreateActionActivity.class));
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                process.removeStepActionAssociation(step,
                        process.getActionByName(process.getActionsOfStep(step).get(i))
                        );
                refresh();
                return false;
            }
        });

        refresh();

    }

    private void refresh() {
        arrayAdapter.clear();
        arrayAdapter.addAll(process.getActionsOfStep(step));
        arrayAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            refresh();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @OnClick(R.id.addActionButton)
    public void addAction(){
        try {
            Keyboard.closeKeyboard(this);
            refreshStep();

            if(process.hasStep(step.getId()) == false){
                Snackbar.make(getRoot(),"Please save the step before adding actions.",Snackbar.LENGTH_LONG).show();
                return;
            }

            ProcessHolder.getInstance().setStep(step);
            ProcessHolder.getInstance().setAction(new Action());

            startActivity(new Intent(this,CreateActionActivity.class));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @OnClick(R.id.saveButton)
    public void save(){
        Keyboard.closeKeyboard(this);
        refreshStep();
        if(step.getTitle().length()==0){
            Snackbar.make(getRoot(),"Title cant be empty",Snackbar.LENGTH_LONG).show();
            return;
        }
        process.putStep(step);
        finish();
    }

    private void refreshStep() {
        step.setTitle(titleBox.getEditText().getText().toString());
        step.setDescription(descriptionBox.getEditText().getText().toString());
    }

}