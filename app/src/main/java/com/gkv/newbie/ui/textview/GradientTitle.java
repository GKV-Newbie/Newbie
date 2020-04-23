package com.gkv.newbie.ui.textview;

import android.content.Context;
import android.util.AttributeSet;

public class GradientTitle extends TypefaceTextView{

    private static final String FONT_NAME = "kellyslab.ttf" ;

    public GradientTitle(Context context) {
        super(context, FONT_NAME);
    }

    public GradientTitle(Context context, AttributeSet attrs) {
        super(context, attrs, FONT_NAME);
    }

    public GradientTitle(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle, FONT_NAME);
    }
}
