package com.gkv.newbie.ui.textview;

import android.content.Context;
import android.util.AttributeSet;

public class StylishTextItalic extends TypefaceTextView{

    private static String FONT_NAME = "ralewaymi.ttf" ;

    public StylishTextItalic(Context context) {
        super(context, FONT_NAME);
    }

    public StylishTextItalic(Context context, AttributeSet attrs) {
        super(context, attrs, FONT_NAME);
    }

    public StylishTextItalic(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle, FONT_NAME);
    }
}