package com.gkv.newbie.ui.button;

import android.content.Context;
import android.util.AttributeSet;

public class StylishButton extends TypefaceButton{

    private static final String FONT_NAME = "ralewaym.ttf" ;

    public StylishButton(Context context) {
        super(context, FONT_NAME);
    }

    public StylishButton(Context context, AttributeSet attrs) {
        super(context, attrs, FONT_NAME);
    }

    public StylishButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle, FONT_NAME);
    }
}