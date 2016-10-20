/*
 * The MIT License (MIT)
 * Copyright (c) 2016 baoyongzhang <baoyz94@gmail.com>
 */
package com.baoyz.bigbang;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.RectEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.animation.ValueAnimatorCompat;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by baoyongzhang on 2016/10/20.
 */
public class BigBangActionBar extends ViewGroup {

    ImageView mSearch;
    ImageView mShare;
    ImageView mCopy;
    Drawable mBorder;
    private int mActionGap;
    private int mContentPadding;

    public BigBangActionBar(Context context) {
        this(context, null);
    }

    public BigBangActionBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BigBangActionBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initSubViews();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BigBangActionBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initSubViews();
    }

    private void initSubViews() {
        Context context = getContext();

        mBorder = ContextCompat.getDrawable(context, R.drawable.bigbang_action_bar_bg);
        mBorder.setCallback(this);

        mSearch = new ImageView(context);
        mSearch.setImageResource(R.mipmap.bigbang_action_search);
        mShare = new ImageView(context);
        mShare.setImageResource(R.mipmap.bigbang_action_share);
        mCopy = new ImageView(context);
        mCopy.setImageResource(R.mipmap.bigbang_action_copy);

        addView(mSearch, createLayoutParams());
        addView(mShare, createLayoutParams());
        addView(mCopy, createLayoutParams());

        setWillNotDraw(false);

        mActionGap = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, getResources().getDisplayMetrics());
        mContentPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
    }

    private LayoutParams createLayoutParams() {
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        return params;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int childCount = getChildCount();
        int measureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            child.measure(measureSpec, measureSpec);
        }

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        setMeasuredDimension(width, height + mContentPadding + mSearch.getMeasuredHeight());
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        layoutSubView(mSearch, mActionGap, 0);
        layoutSubView(mShare, width - mActionGap * 2 - mShare.getMeasuredWidth() - mCopy.getMeasuredWidth(), 0);
        layoutSubView(mCopy, width - mActionGap - mCopy.getMeasuredWidth(), 0);

        Rect oldBounds = mBorder.getBounds();
        Rect newBounds = new Rect(0, mSearch.getMeasuredHeight() / 2, width, height);

        if (!oldBounds.equals(newBounds)) {
//            PropertyValuesHolder top = PropertyValuesHolder.ofInt("top", oldBounds.top, newBounds.top);
//            PropertyValuesHolder bottom = PropertyValuesHolder.ofInt("bottom", oldBounds.bottom, newBounds.bottom);
//            PropertyValuesHolder left = PropertyValuesHolder.ofInt("left", oldBounds.left, newBounds.left);
//            PropertyValuesHolder right = PropertyValuesHolder.ofInt("right", oldBounds.right, newBounds.right);
//            ValueAnimator valueAnimator = ValueAnimator.ofPropertyValuesHolder(top, bottom, left, right);
//            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                @Override
//                public void onAnimationUpdate(ValueAnimator animation) {
//                    int top = (int) animation.getAnimatedValue("top");
//                    int bottom = (int) animation.getAnimatedValue("bottom");
//                    int left = (int) animation.getAnimatedValue("left");
//                    int right = (int) animation.getAnimatedValue("right");
//                    mBorder.setBounds(left, top, right, bottom);
//                }
//            });
//            valueAnimator.setDuration(200).start();


            ObjectAnimator.ofObject(mBorder, "bounds", new RectEvaluator(), oldBounds, newBounds).setDuration(200).start();
        }
    }

    private void layoutSubView(View view, int l, int t) {
        view.layout(l, t, view.getMeasuredWidth() + l, view.getMeasuredHeight() + t);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mBorder.draw(canvas);
    }

    public int getContentPadding() {
        return mContentPadding;
    }

    @Override
    protected boolean verifyDrawable(Drawable who) {
        return super.verifyDrawable(who) || who == mBorder;
    }
}