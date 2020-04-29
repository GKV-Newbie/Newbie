package com.gkv.newbie.app.home.sections.process;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.gkv.newbie.R;
import com.gkv.newbie.app.home.BaseNavigationActivity;
import com.gkv.newbie.model.Step;
import com.gkv.newbie.modelmanager.ProcessHolder;
import com.gkv.newbie.utils.gson.POJO;
import com.gkv.newbie.model.Process;

import java.util.ArrayList;

public class CreateProcessActivity extends BaseNavigationActivity {

    Process process;

    @BindView(R.id.listView)
    ListView listView;
    ArrayAdapter<String> arrayAdapter;

    @BindView(R.id.saveButton)
    Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_process);

        ButterKnife.bind(this);
        process = ProcessHolder.getInstance().getProcess();

        init();

    }

    private void init() {

        arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                process.stepList());

        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ProcessHolder.getInstance().setStep(process.getStepByTitle(process.stepList().get(i)));
                startActivity(new Intent(CreateProcessActivity.this,CreateStepActivity.class));
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                process.removeStep(process.getStepByTitle(process.stepList().get(i)));
                refresh();
                return false;
            }
        });

        refresh();

    }

    private void refresh() {
        arrayAdapter.clear();
        arrayAdapter.addAll(process.stepList());
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

    @OnClick(R.id.addStepButton)
    public void addStep(){
        ProcessHolder.getInstance().setStep(new Step());
        startActivity(new Intent(this,CreateStepActivity.class));
    }

    @OnClick(R.id.saveButton)
    public void save(){
        try {
            System.out.println(POJO.getInstance().toJson(process));
            PopupMenu popupMenu = new PopupMenu(this,save);
            for(String step:process.stepList()){
                popupMenu.getMenu().add(step);
            }
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    process.setHeadStepTitle(menuItem.getTitle().toString());
                    finish();
                    return false;
                }
            });
            popupMenu.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}