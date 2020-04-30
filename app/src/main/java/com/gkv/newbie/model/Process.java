package com.gkv.newbie.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

public class Process implements Serializable {

    String headStepTitle;

    LinkedHashMap<String, Step> stepMap;

    LinkedHashMap<String, Action> actionMap;

    LinkedHashMap<String, HashSet<String>> stepActionMap;

    public Process() {
        stepMap = new LinkedHashMap<String, Step>();
        actionMap = new LinkedHashMap<String, Action>();
        stepActionMap = new LinkedHashMap<String, HashSet<String>>();
    }

    public String getHeadStepTitle() {
        System.out.println("getHeadStepTitle "+ headStepTitle);
        return headStepTitle;
    }

    public void setHeadStepTitle(String headStepTitle) {
        System.out.println("setHeadStepTitle "+ headStepTitle);
        this.headStepTitle = headStepTitle;
    }

    public ArrayList<String> stepList(){
        ArrayList<String> list = new ArrayList<>();
        list.addAll(stepMap.keySet());
        return list;
    }

    public Step getStepByTitle(String title){
        System.out.println("getStepByTitle "+ title +" > "+stepMap.get(title));
        return stepMap.get(title);
    }

    public Step putStep(Step step){
        System.out.println("putStep "+step);
        if(stepActionMap.containsKey(step.getTitle()) == false){
            stepActionMap.put(step.getTitle(),new HashSet<String>());
        }
        return stepMap.put(step.getTitle(),step);
    }

    public Step updateStep(String title,Step step){
        if(title.equals("")==false) {
            for (String actionName : actionMap.keySet()) {
                if (actionMap.get(actionName).getStepTitle().equals(title))
                    actionMap.get(actionName).setStepTitle(step.getTitle());
            }
            stepMap.remove(title);
        }
        return putStep(step);
    }

    public Action getActionByTitle(String title){
        System.out.println("getActionByTitle "+title+" > "+actionMap.get(title));
        return actionMap.get(title);
    }

    public Action putAction(Action action){
        System.out.println("putAction "+action);
        return actionMap.put(action.getName(),action);
    }

    public Action updateAction(String name,Action action){
        if(name.equals("")==false){
            for(String stepTitle:stepActionMap.keySet()){
                if(stepActionMap.get(stepTitle).contains(name)){
                    stepActionMap.get(stepTitle).remove(name);
                    stepActionMap.get(stepTitle).add(action.getName());
                }
            }
        }
        return putAction(action);
    }

    public boolean putStepActionAssociation(Step step, Action action){
        System.out.println("putStepActionAssociation "+step+" "+action);
        if(stepMap.containsKey(step.getTitle()) == false){
            putStep(step);
        }
        if(actionMap.containsKey(action.getName()) == false){
            putAction(action);
        }
        if(stepActionMap.containsKey(step.getTitle()) == false){
            stepActionMap.put(step.getTitle(),new HashSet<String>());
        }
        return stepActionMap.get(step.getTitle()).add(action.getName());
    }

    public void removeStepActionAssociation(Step step, Action action){
        stepActionMap.get(step.getTitle()).remove(action.getName());
        removeUnusedActions();
    }

    public ArrayList<String> getActionsOfStep(Step step){
        System.out.println("getActionsOfStep "+step+" "+stepActionMap.get(step.getTitle()));
        ArrayList<String> list = new ArrayList<>();
        try {
            list.addAll(stepActionMap.get(step.getTitle()));
        }catch (Exception e){

        }
        return list;
    }

    public boolean hasAction(String name){
        return actionMap.containsKey(name);
    }

    public boolean hasStep(String title){
        return stepMap.containsKey(title);
    }

    public void removeStep(Step step) {
        stepMap.remove(step.getTitle());
        stepActionMap.remove(step.getTitle());
        removeUnusedActions();
    }

    private void removeUnusedActions() {
        Collection<HashSet<String>> usedActionsFromMap = stepActionMap.values();
        HashSet<String> usedActions = new HashSet<>();
        for(HashSet<String> i:usedActionsFromMap){
            for(String j:i){
                usedActions.add(j);
            }
        }
        for(String key:actionMap.keySet()){
            if(usedActions.contains(key)==false)
                actionMap.remove(key);
        }
    }

}
