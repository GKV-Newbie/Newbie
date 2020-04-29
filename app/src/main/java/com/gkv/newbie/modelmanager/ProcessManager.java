package com.gkv.newbie.modelmanager;

import com.gkv.newbie.model.Action;
import com.gkv.newbie.model.Process;
import com.gkv.newbie.model.Step;

import java.util.ArrayList;
import java.util.Stack;

public class ProcessManager {

    Step currentStep;

    Process process;

    Stack<Step> history;

    public ProcessManager() {
        history = new Stack<Step>();
    }

    public Process getProcess() {
        return process;
    }

    public void setProcess(Process process) {
        this.process = process;
    }

    public Step getCurrentStep(){
        if(currentStep == null){
            if(process == null){
                return new Step();
            }
            currentStep = process.getStepByTitle(process.getHeadStepTitle());
        }
        addToHistory(currentStep);
        return currentStep;
    }

    private void addToHistory(Step currentStep) {
        if(history.empty() == false){
            if(history.peek().getTitle().equals(currentStep.getTitle())){
                //same step on the top of stack
                return;
            }
        }
        history.push(currentStep);
    }

    private void setCurrentStep(Step currentStep) {
        this.currentStep = currentStep;
    }

    public boolean hasBack(){
        System.out.println(history);
        return history.size() > 1;
    }

    public void goBack(){
        if(history.empty())
            return;
        history.pop();
        if(history.empty())
            setCurrentStep(null);
        else
            setCurrentStep(history.peek());
    }

    public ArrayList<Action> getCurrentActions(){
        if(getCurrentStep() == null){
            return new ArrayList<Action>();
        }
        ArrayList<String> stepActionIds = process.getActionsOfStep(currentStep);
        ArrayList<Action> actions = new ArrayList<Action>();
        for (String actionId:stepActionIds) {
            actions.add(process.getActionByTitle(actionId));
        }
        return actions;
    }

    public Step dispatchAction(Action action){
        currentStep = process.getStepByTitle(action.getStepTitle());
        if(currentStep == null)
            return new Step();
        return currentStep;
    }
}
