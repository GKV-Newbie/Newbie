package com.gkv.newbie.utils.locallists;

import android.os.Environment;

import java.io.File;
import java.util.ArrayList;

public class HistoryManager {

    private static HistoryManager instance;

    private File rootDirectory;

    private HistoryManager(){
        rootDirectory = new File(Environment.getExternalStorageDirectory(),"history");
        rootDirectory.mkdirs();
        if(rootDirectory.exists() == false){
            System.out.println("FAILED!!!!!");
        }
    }

    public static HistoryManager getInstance() {
        if(instance==null)
            instance = new HistoryManager();
        return instance;
    }

    public ArrayList<String> getHistoryList(){
        ArrayList<String> list = new ArrayList<>();
        if(rootDirectory.list()!=null)
        for(String str:rootDirectory.list()){
            //list.add(new String(Base64.decode(str,Base64.DEFAULT)));
            list.add(str);
        }
        return list;
    }

    public void addToHistoryList(String email){
        System.out.println("addToHistoryList "+email);
        //email = Base64.encodeToString(email.getBytes(),Base64.DEFAULT);
        new File(rootDirectory,email).mkdirs();
    }
/*
    public void removeFromHistoryList(String email){
        System.out.println("removeFromHistoryList "+email);
        //email = Base64.encodeToString(email.getBytes(),Base64.DEFAULT);
        new File(rootDirectory,email).delete();
    }

    public boolean existsInHistoryList(String email){
        //email = Base64.encodeToString(email.getBytes(),Base64.DEFAULT);
        return new File(rootDirectory,email).exists();
    }

    public void toggleFromHistoryList(String email){
        if(existsInHistoryList(email)){
            removeFromHistoryList(email);
        }else{
            addToHistoryList(email);
        }
    }

*/

}
