package com.gkv.newbie.utils.locallists;

import android.os.Environment;

import java.io.File;
import java.util.ArrayList;

public class FavouriteManager {

    private static FavouriteManager instance;

    private File rootDirectory;

    private FavouriteManager(){
        rootDirectory = new File(Environment.getExternalStorageDirectory(),"favourites");
        rootDirectory.mkdirs();
        if(rootDirectory.exists() == false){
            System.out.println("FAILED!!!!!");
        }
    }

    public static FavouriteManager getInstance() {
        if(instance==null)
            instance = new FavouriteManager();
        System.out.println(instance.getFavouritesList());
        return instance;
    }

    public ArrayList<String> getFavouritesList(){
        ArrayList<String> list = new ArrayList<>();
        if(rootDirectory.list()!=null)
        for(String str:rootDirectory.list()){
            //list.add(new String(Base64.decode(str,Base64.DEFAULT)));
            list.add(str);
        }
        return list;
    }

    public void addToFavouritesList(String email){
        System.out.println("addToFavouritesList "+email);
        //email = Base64.encodeToString(email.getBytes(),Base64.DEFAULT);
        new File(rootDirectory,email).mkdirs();
    }

    public void removeFromFavouritesList(String email){
        System.out.println("removeFromFavouritesList "+email);
        //email = Base64.encodeToString(email.getBytes(),Base64.DEFAULT);
        new File(rootDirectory,email).delete();
    }

    public boolean existsInFavouritesList(String email){
        //email = Base64.encodeToString(email.getBytes(),Base64.DEFAULT);
        return new File(rootDirectory,email).exists();
    }

    public void toggleFromFavouritesList(String email){
        if(existsInFavouritesList(email)){
            removeFromFavouritesList(email);
        }else{
            addToFavouritesList(email);
        }
    }

}
