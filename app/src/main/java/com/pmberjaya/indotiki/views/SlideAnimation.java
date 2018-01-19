package com.pmberjaya.indotiki.views;

import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;

/**
 * Created by edwin on 20/11/2017.
 */

public class SlideAnimation  extends Animation  {

    private static final float SPEED = 0.5f;

    private float mStart;
    private float mEnd;

    public SlideAnimation(float fromX, float toX) {
        mStart = 100;
        mEnd = 600;

        setInterpolator(new LinearInterpolator());

        float duration = Math.abs(mEnd - mStart) / SPEED;
        setDuration((long) duration);
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);

//        float offset = (mEnd - mStart) * interpolatedTime + mStart;
//        mOffset = (int) offset;
//        postInvalidate();
    }

}