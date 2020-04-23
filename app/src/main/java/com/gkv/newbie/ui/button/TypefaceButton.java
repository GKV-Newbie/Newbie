package com.gkv.newbie.ui.button;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import androidx.appcompat.widget.AppCompatButton;

public class TypefaceButton extends AppCompatButton {
    private static final String TAG = "TypefaceButton";

    public TypefaceButton(Context context, String fontName) {
        super(context);
    }

    public TypefaceButton(Context context, AttributeSet attrs, String fontName) {
        super(context, attrs);
        setCustomFont(context, fontName);
    }

    public TypefaceButton(Context context, AttributeSet attrs, int defStyle, String fontName) {
        super(context, attrs, defStyle);
        setCustomFont(context, fontName);
    }

    public boolean setCustomFont(Context ctx, String asset) {
        Typeface tf = null;
        try {
            tf = Typeface.createFromAsset(ctx.getAssets(), asset);
        } catch (Exception e) {
            Log.e(TAG, "Could not get typeface: "+e.getMessage());
            return false;
        }

        setTypeface(tf);
        return true;
    }
}
