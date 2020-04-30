package com.gkv.newbie.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

public class Process implements Serializable {

    String headStepId;

    LinkedHashMap<String, Step> stepMap;

    LinkedHashMap<String, Action> actionMap;

    LinkedHashMap<String, HashSet<String>> stepActionMap;

    public Process() {
        stepMap = new LinkedHashMap<String, Step>();
        actionMap = new LinkedHashMap<String, Action>();
        stepActionMap = new LinkedHashMap<String, HashSet<String>>();
    }

    public String getHeadStepId() {
        System.out.println("getheadStepId "+ headStepId);
        return headStepId;
    }

    public void setHeadStepId(String headStepId) {
        System.out.println("setheadStepId "+ headStepId);
        this.headStepId = headStepId;
    }

    public ArrayList<String> stepList(){
        ArrayList<String> list = new ArrayList<>();
        for(Step step:stepMap.values()){
            list.add(step.getTitle());
        }
        return list;
    }

    public Step getStepById(String id){
        System.out.println("getStepById "+ id +" > "+stepMap.get(id));
        return stepMap.get(id);
    }

    public Action getActionById(String id){
        System.out.println("getActionById "+ id +" > "+stepMap.get(id));
        return actionMap.get(id);
    }

    public Step putStep(Step step){
        System.out.println("putStep "+step);
        if(stepActionMap.containsKey(step.getId()) == false){
            stepActionMap.put(step.getId(),new HashSet<String>());
        }
        return stepMap.put(step.getId(),step);
    }

//    public Step updateStep(String title,Step step){
//        if(title.equals("")==false) {
//            for (String actionName : actionMap.keySet()) {
//                if (actionMap.get(actionName).getStepTitle().equals(title))
//                    actionMap.get(actionName).setStepTitle(step.getTitle());
//            }
//            stepMap.remove(title);
//        }
//        return putStep(step);
//    }

    public Action getActionByName(String name){
        System.out.println("getActionByName "+name+" > ");
        for(Action action:actionMap.values()){
            if(action.getName().equals(name)){
                return action;
            }
        }
        return null;
    }

    public Step getStepByTitle(String title){
        for(Step step:stepMap.values()){
            if(step.getTitle().equals(title))
                return step;
        }
        return null;
    }

    public Action putAction(Action action){
        System.out.println("putAction "+action);
        return actionMap.put(action.getId(),action);
    }

//    public Action updateAction(String name,Action action){
//        if(name.equals("")==false){
//            for(String stepTitle:stepActionMap.keySet()){
//                if(stepActionMap.get(stepTitle).contains(name)){
//                    stepActionMap.get(stepTitle).remove(name);
//                    stepActionMap.get(stepTitle).add(action.getName());
//                }
//            }
//        }
//        return putAction(action);
//    }

    public boolean putStepActionAssociation(Step step, Action action){
        System.out.println("putStepActionAssociation "+step+" "+action);
        if(stepMap.containsKey(step.getId()) == false){
            putStep(step);
        }
        if(actionMap.containsKey(action.getId()) == false){
            putAction(action);
        }
        if(stepActionMap.containsKey(step.getId()) == false){
            stepActionMap.put(step.getId(),new HashSet<String>());
        }
        return stepActionMap.get(step.getId()).add(action.getId());
    }

    public void removeStepActionAssociation(Step step, Action action){
        stepActionMap.get(step.getId()).remove(action.getId());
        removeUnusedActions();
    }

    public ArrayList<String> getActionsOfStep(Step step){
        System.out.println("getActionsOfStep "+step);
        ArrayList<String> list = new ArrayList<>();
        try {
            for(String actionId:stepActionMap.get(step.getId())){
                list.add(actionMap.get(actionId).getName());
            }
        }catch (Exception e){

        }
        return list;
    }

    public boolean hasAction(String id){
        return actionMap.containsKey(id);
    }

    public boolean hasStep(String id){
        return stepMap.containsKey(id);
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
