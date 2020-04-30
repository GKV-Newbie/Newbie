package com.gkv.newbie.modelmanager;

import com.gkv.newbie.model.Action;
import com.gkv.newbie.model.Process;
import com.gkv.newbie.model.Step;

public class ProcessHolder {

    private static ProcessHolder instance;

    private Process process;

    private Step step;

    private Action action;

    boolean save = false;

    private ProcessHolder(){
        process = new Process();
        step = new Step();
        action = new Action();
    }

    public static ProcessHolder getInstance() {
        if(instance==null)
            instance = new ProcessHolder();
        return instance;
    }

    public Process getProcess() {
        return process;
    }

    public void setProcess(Process process) {
        this.process = process;
    }

    public Step getStep() {
        return step;
    }

    public void setStep(Step step) {
        this.step = step;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public boolean isSave() {
        return save;
    }

    public void setSave(boolean save) {
        this.save = save;
    }


}
