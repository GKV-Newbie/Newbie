package com.gkv.newbie.ui.textview;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import androidx.appcompat.widget.AppCompatTextView;

public class TypefaceTextView extends AppCompatTextView {
    private static final String TAG = "TypefaceTextView";

    public TypefaceTextView(Context context, String fontName) {
        super(context);
    }

    public TypefaceTextView(Context context, AttributeSet attrs, String fontName) {
        super(context, attrs);
        setCustomFont(context, fontName);
    }

    public TypefaceTextView(Context context, AttributeSet attrs, int defStyle, String fontName) {
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