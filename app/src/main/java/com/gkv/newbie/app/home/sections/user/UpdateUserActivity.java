package com.gkv.newbie.app.home.sections.user;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ImageView;

import com.gkv.newbie.R;
import com.gkv.newbie.utils.Keyboard;
import com.gkv.newbie.utils.internet.ResponseHandler;
import com.gkv.newbie.utils.internet.Server;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class UpdateUserActivity extends AppCompatActivity {

    public static final String USER_NAME = "USER_NAME";
    public static final String USER_DISPLAY_PICTURE = "USER_DISPLAY_PICTURE";

    private static final int PICK_IMAGE = 1;

    private String imageLink;

    @BindView(R.id.profile_image)
    ImageView imageView;

    @BindView(R.id.name)
    TextInputLayout textInputLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user);
        ButterKnife.bind(this);
        loadUserDetails();
    }

    private void loadUserDetails() {
        textInputLayout.getEditText().setText(getIntent().getStringExtra(USER_NAME));
        setImageLink(getIntent().getStringExtra(USER_DISPLAY_PICTURE));
    }

    @OnClick(R.id.updateButton)
    public void update(){
        Keyboard.closeKeyboard(this);
        Server.getInstance().updateUser(this,
                textInputLayout.getEditText().getText().toString(),
                getImageLink(),
                new ResponseHandler() {
                    @Override
                    public void onCallback(String response) {
                        startActivity(new Intent(UpdateUserActivity.this,ViewUserActivity.class));
                        finish();
                    }
                },
                new ResponseHandler() {
                    @Override
                    public void onCallback(String error) {

                    }
                }
        );
    }

    @OnClick(R.id.profile_image)
    public void updateDisplayPicture() {
        Keyboard.closeKeyboard(this);
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

        startActivityForResult(chooserIntent, PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE) {
            //TODO: action
            if (data == null) {
                //Display an error
                return;
            }

            final InputStream imageStream;
            try {
                imageStream = getContentResolver().openInputStream(data.getData());
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                String encodedImage = encodeImage(selectedImage);

                uploadFile(encodedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }


        }
    }

    private String encodeImage(Bitmap bm)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);

        return encImage;
    }

    private void uploadFile(String file) {
        Server.getInstance().uploadImage(this, file,
                new ResponseHandler() {
                    @Override
                    public void onCallback(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            updateImageLink(jsonObject.getJSONObject("data").getString("link"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                },
                new ResponseHandler() {
                    @Override
                    public void onCallback(String response) {

                    }
                });
    }

    private void updateImageLink(String string) {
        setImageLink(string);
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
        Picasso.get().load(imageLink).into(imageView);
    }
}