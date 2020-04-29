package com.gkv.newbie.utils.internet;

import android.app.Activity;

import com.gkv.newbie.utils.auth.UserManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Server {

    private static Server instance;

    private OkHttpClient client ;

    private Object BASE_URL = "http://newbie-rest.herokuapp.com";

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private Server(){
        this.client = new OkHttpClient();
    }

    public static Server getInstance(){
        if(instance == null)
            instance = new Server();
        return instance;
    }

    /*
    HttpUrl.Builder httpBuilder = HttpUrl.parse("http://newbie.nihalkonda.com/loadProcedures.php").newBuilder();

                httpBuilder.addQueryParameter("email",email);
                httpBuilder.addQueryParameter("password",password);
                httpBuilder.addQueryParameter("name",name);
                httpBuilder.addQueryParameter("displayPicture",displayPicture);
    */

    private String getAuthToken() {
        return UserManager.getInstance().getAuthToken();
    }

    private void requestHandler(
            Activity activity,
            Request request,
            ResponseHandler successHandler,
            ResponseHandler failureHandler
    ){
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(request.url().toString());
                try (Response response = client.newCall(request).execute()) {
                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                    String responseString = response.body().string();
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println(responseString);
                            successHandler.onCallback(responseString);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            failureHandler.onCallback(e.toString());
                        }
                    });
                }
            }
        }).start();
    }

    public void registerUser(
            Activity activity,
            String email,
            String password,
            String name,
            String displayPicture,
            ResponseHandler successHandler,
            ResponseHandler failureHandler
    ){

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("email",email);
            jsonObject.put("password", password);
            jsonObject.put("name", name);
            jsonObject.put("displayPicture", displayPicture);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(JSON,jsonObject.toString());

        Request request = new Request.Builder()
                .url(BASE_URL + "/user/register")
                .addHeader("Content-Type","application/json")
                .post(requestBody)
                .build();

        requestHandler(activity,request,successHandler,failureHandler);

    }

    public void loginUser(
            Activity activity,
            String email,
            String password,
            ResponseHandler successHandler,
            ResponseHandler failureHandler
    ){
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("email",email);
            jsonObject.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(JSON,jsonObject.toString());

        Request request = new Request.Builder()
                .url(BASE_URL + "/user/login")
                .addHeader("Content-Type","application/json")
                .post(requestBody)
                .build();

        requestHandler(activity,request,successHandler,failureHandler);

    }

    public void updateUser(
            Activity activity,
            String name,
            String displayPicture,
            ResponseHandler successHandler,
            ResponseHandler failureHandler
    ){

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("name",name);
            jsonObject.put("displayPicture", displayPicture);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(JSON,jsonObject.toString());

        Request request = new Request.Builder()
                .url(BASE_URL + "/user/update")
                .addHeader("Authorization","Bearer "+ getAuthToken())
                .addHeader("Content-Type","application/json")
                .put(requestBody)
                .build();

        requestHandler(activity,request,successHandler,failureHandler);

    }

    public void myAccount(
            Activity activity,
            ResponseHandler successHandler,
            ResponseHandler failureHandler
    ){

        Request request = new Request.Builder()
                .url(BASE_URL + "/user/my-account")
                .addHeader("Authorization","Bearer "+ getAuthToken())
                .build();

        requestHandler(activity,request,successHandler,failureHandler);

    }


    public void userAccount(
            Activity activity,
            String email,
            ResponseHandler successHandler,
            ResponseHandler failureHandler
    ){

        HttpUrl.Builder httpBuilder = HttpUrl.parse(BASE_URL + "/user/user-account").newBuilder();

        httpBuilder.addQueryParameter("email",email);

        Request request = new Request.Builder()
                .url(httpBuilder.build())
                .addHeader("Authorization","Bearer "+ getAuthToken())
                .build();

        requestHandler(activity,request,successHandler,failureHandler);

    }


    public void createProcedure(
            Activity activity,
            String name,
            String parent,
            String shareType,
            String procedureType,
            String process,
            ResponseHandler successHandler,
            ResponseHandler failureHandler
    ){

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("name",name);
            if(parent!=null)
            jsonObject.put("parent", parent);
            jsonObject.put("shareType", shareType);
            jsonObject.put("procedureType", procedureType);
            jsonObject.put("process", process);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(JSON,jsonObject.toString());

        Request request = new Request.Builder()
                .url(BASE_URL + "/procedure/create")
                .addHeader("Authorization","Bearer "+ getAuthToken())
                .addHeader("Content-Type","application/json")
                .post(requestBody)
                .build();

        requestHandler(activity,request,successHandler,failureHandler);

    }

    public void readProcedure(
            Activity activity,
            String id,
            ResponseHandler successHandler,
            ResponseHandler failureHandler
    ){

        HttpUrl.Builder httpBuilder = HttpUrl.parse(BASE_URL + "/procedure/read").newBuilder();

        httpBuilder.addQueryParameter("id",id);

        Request request = new Request.Builder()
                .url(httpBuilder.build())
                .addHeader("Authorization","Bearer "+ getAuthToken())
                .build();

        requestHandler(activity,request,successHandler,failureHandler);

    }    
    
    public void updateProcedure(
            Activity activity,
            String id,
            String name,
            String shareType,
            String procedureType,
            String process,
            ResponseHandler successHandler,
            ResponseHandler failureHandler
    ){

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("id",id);
            jsonObject.put("name",name);
            jsonObject.put("shareType", shareType);
            jsonObject.put("procedureType", procedureType);
            jsonObject.put("process", process);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(JSON,jsonObject.toString());

        Request request = new Request.Builder()
                .url(BASE_URL + "/procedure/update")
                .addHeader("Authorization","Bearer "+ getAuthToken())
                .addHeader("Content-Type","application/json")
                .put(requestBody)
                .build();

        requestHandler(activity,request,successHandler,failureHandler);

    }


    private void listProcedures(
            Activity activity,
            String suffix,
            String id,
            String email,
            ResponseHandler successHandler,
            ResponseHandler failureHandler
    ){

        HttpUrl.Builder httpBuilder = HttpUrl.parse(BASE_URL + "/procedure/list"+suffix).newBuilder();

        if(id!=null)
            httpBuilder.addQueryParameter("id",id);

        if(email!=null)
            httpBuilder.addQueryParameter("email",email);

        Request request = new Request.Builder()
                .url(httpBuilder.build())
                .addHeader("Authorization","Bearer "+ getAuthToken())
                .build();

        requestHandler(activity,request,successHandler,failureHandler);

    }

    public void listAllProcedures(
            Activity activity,
            ResponseHandler successHandler,
            ResponseHandler failureHandler
    ){
        listProcedures(activity,"",null,null,successHandler,failureHandler);
    }

    public void listMyProcedures(
            Activity activity,
            ResponseHandler successHandler,
            ResponseHandler failureHandler
    ){
        listProcedures(activity,"/my",null,null,successHandler,failureHandler);
    }

    public void listSharedProcedures(
            Activity activity,
            ResponseHandler successHandler,
            ResponseHandler failureHandler
    ){
        listProcedures(activity,"/shared",null,null,successHandler,failureHandler);
    }
    
    public void listUserProcedures(
            Activity activity,
            String email,
            ResponseHandler successHandler,
            ResponseHandler failureHandler
    ){
        listProcedures(activity,"/user",null,email,successHandler,failureHandler);
    }

    public void listChildProcedures(
            Activity activity,
            String id,
            ResponseHandler successHandler,
            ResponseHandler failureHandler
    ){
        listProcedures(activity,"/child",id,null,successHandler,failureHandler);
    }

    public void giveAccessProcedure(
            Activity activity,
            String id,
            String email,
            ResponseHandler successHandler,
            ResponseHandler failureHandler
    ){

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("id",id);
            jsonObject.put("email", email);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(JSON,jsonObject.toString());

        Request request = new Request.Builder()
                .url(BASE_URL + "/procedure/share/give")
                .addHeader("Authorization","Bearer "+ getAuthToken())
                .addHeader("Content-Type","application/json")
                .put(requestBody)
                .build();

        requestHandler(activity,request,successHandler,failureHandler);

    }

    public void revokeAccessProcedure(
            Activity activity,
            String id,
            String email,
            ResponseHandler successHandler,
            ResponseHandler failureHandler
    ){

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("id",id);
            jsonObject.put("email", email);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(JSON,jsonObject.toString());

        Request request = new Request.Builder()
                .url(BASE_URL + "/procedure/share/revoke")
                .addHeader("Authorization","Bearer "+ getAuthToken())
                .addHeader("Content-Type","application/json")
                .put(requestBody)
                .build();

        requestHandler(activity,request,successHandler,failureHandler);

    }

    public void uploadImage(
            Activity activity,
            String f,
            ResponseHandler successHandler,
            ResponseHandler failureHandler
    ){
        System.out.println(f);
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("image", f)
                .build();
        Request request = new Request.Builder()
                .url("https://api.imgur.com/3/image")
                .method("POST", body)
                .addHeader("Authorization", "Client-ID 196ccd3e87194e9")
                .build();

        //10e2a0be7668fab0ee7387ccb9b2384b195b868b
        requestHandler(activity,request,successHandler,failureHandler);
    }

}
