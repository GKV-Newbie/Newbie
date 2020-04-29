package com.gkv.newbie.utils.gson;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class POJO {

    private static POJO instance;

    private Gson gson;

    private POJO(){
        gson = new Gson();
    }

    public static POJO getInstance() {
        if(instance==null)
            instance = new POJO();
        return instance;
    }

    public <T> T fromJson(String json, Class<T> classOfT) throws JsonSyntaxException {
        return gson.fromJson(json,classOfT);
    }

    public String toJson(Object o){
        return gson.toJson(o);
    }
}
