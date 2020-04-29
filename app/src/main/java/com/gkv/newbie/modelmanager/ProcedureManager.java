package com.gkv.newbie.modelmanager;

import com.gkv.newbie.model.Procedure;

import java.util.ArrayList;
import java.util.HashMap;

public class ProcedureManager {

    Procedure rootProcedure;

    Procedure currentProcedure;

    public ProcedureManager(ArrayList<Procedure> list){

        ArrayList<Procedure> rootChildren = new ArrayList<>(list);

        HashMap<String,Procedure> map = new HashMap<>();

        for(Procedure procedure:list){
            map.put(procedure.get_id(),procedure);
        }

        for(Procedure procedure:list){
            String parentId = procedure.getParent();
            if(map.containsKey(parentId)) {
                Procedure parentProcedure = map.get(parentId);
                parentProcedure.addChild(procedure);
                procedure.setParentProcedure(parentProcedure);
                rootChildren.remove(procedure);
            }
        }

        currentProcedure = new Procedure();
        rootProcedure = new Procedure();

        currentProcedure.setName("Procedures");
        rootProcedure.setName("Procedures");

        currentProcedure.setProcedureType("group");
        rootProcedure.setProcedureType("group");

        for(Procedure proc:rootChildren){
            currentProcedure.addChild(proc);
            rootProcedure.addChild(proc);

            proc.setParentProcedure(rootProcedure);
        }


    }

    public ArrayList<Procedure> getChildren(){
        try {
            ArrayList<Procedure> list = currentProcedure.getChildren();
            list.size();
            return list;
        }catch (Exception e){
            return new ArrayList<>();
        }
    }

    public Procedure getCurrentProcedure() {
        return currentProcedure;
    }

    public Procedure getThinProcedure(){
        Procedure procedure = new Procedure();

        procedure.set_id(currentProcedure.get_id());
        procedure.setName(currentProcedure.getName());
        procedure.setParent(currentProcedure.getParent());
        procedure.setProcedureType(currentProcedure.getProcedureType());
        procedure.setProcess(currentProcedure.getProcess());
        procedure.setShareType(currentProcedure.getShareType());

        return procedure;
    }

    public void setCurrentProcedure(Procedure currentProcedure) {
        try{
            this.currentProcedure = currentProcedure;
            System.out.println("*****setCurrentProcedure > "+currentProcedure);
            System.out.println(getChildren());
        }catch (Exception e){
            System.out.println(e.toString());
        }
    }

    public void goBack(){
        if(canGoBack())
            setCurrentProcedure(currentProcedure.getParentProcedure());
    }

    public boolean canGoBack(){
        return currentProcedure.getParentProcedure()!=null;
    }

}
