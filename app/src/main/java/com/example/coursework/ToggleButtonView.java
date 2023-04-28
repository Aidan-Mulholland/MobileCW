package com.example.coursework;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


public class ToggleButtonView extends View {

    private Paint slider, circle, outline;
    private boolean isToggled;
    private boolean firstDraw = true;
    private int width, height, radius, offset;

    public ToggleButtonView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.ToggleButton,
                0,0);
        try {
            isToggled = a.getBoolean(R.styleable.ToggleButton_isToggled, false);
        } finally {
            a.recycle();
        }

        init(attrs);
    }

    private void init(AttributeSet attrs) {
        slider = new Paint(Paint.ANTI_ALIAS_FLAG);
        slider.setARGB(0, 0,0,0);
        slider.setStyle(Paint.Style.FILL);
        slider.setStrokeWidth(10);

        circle = new Paint(Paint.ANTI_ALIAS_FLAG);
        circle.setColor(Color.parseColor("#dddddd"));
        circle.setStyle(Paint.Style.STROKE);
        circle.setStrokeWidth(10);

        outline = new Paint(Paint.ANTI_ALIAS_FLAG);
        outline.setColor(Color.parseColor("#dddddd"));
        outline.setStyle(Paint.Style.STROKE);
        outline.setStrokeWidth(10);
    }

    @Override
    public void onDraw(Canvas canvas) {
        width = getWidth();
        height = getHeight();
        int cornerRadius = height / 2;
        radius = Math.min((height - 60) / 2, (width - 60) / 2);

        if (firstDraw) {
            if (isToggled) {
                offset = (width - 2 * radius - 60);
                slider.setARGB(255, 27,118,109);
            }
            firstDraw = false;
        }

        canvas.drawRoundRect(10, 10, width - 10, height - 10, cornerRadius, cornerRadius, slider);

        canvas.drawCircle(radius + 30 + offset, height/2, radius, circle);

        canvas.drawRoundRect(10, 10, width - 10, height - 10, cornerRadius, cornerRadius, outline);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(widthMeasureSpec, Math.min(widthMeasureSpec/2, heightMeasureSpec));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            isToggled = !isToggled;
            ValueAnimator animator = ValueAnimator.ofInt(isToggled ? 0 : (width - 2 * radius - 60), isToggled ? (width - 2 * radius - 60) : 0);
            animator.setDuration(200);
            animator.addUpdateListener(animation -> {
                offset = (int) animation.getAnimatedValue();
                invalidate();
            });
            ValueAnimator colourAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), isToggled ? Color.argb(0, 0, 0, 0) : Color.argb(255, 27,118,109), isToggled ? Color.argb(255, 27,118,109) : Color.argb(0, 0, 0, 0));
            colourAnimator.setDuration(200);
            colourAnimator.addUpdateListener(animation -> {
                slider.setColor((int) animation.getAnimatedValue());
                invalidate();
            });
            animator.start();
            colourAnimator.start();
            return true;
        }
        return false;
    }

    public void setToggle(boolean value) {
        isToggled = value;
    }
}