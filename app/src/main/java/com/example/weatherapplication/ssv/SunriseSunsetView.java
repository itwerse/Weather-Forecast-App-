package com.example.weatherapplication.ssv;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.RectF;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.example.weatherapplication.Model.Time;
import com.example.weatherapplication.R;
import com.example.weatherapplication.formatter.SimpleSunriseSunsetLabelFormatter;
import com.example.weatherapplication.formatter.SunriseSunsetLabelFormatter;

import java.util.Calendar;
import java.util.Locale;


public class SunriseSunsetView extends View {

    private static final int DEFAULT_TRACK_COLOR = Color.WHITE;
    private static final int DEFAULT_TRACK_WIDTH_PX = 4;

    private static final int DEFAULT_SUN_COLOR = Color.YELLOW;
    private static final int DEFAULT_SUN_RADIUS_PX = 20;
    private static final int DEFAULT_SUN_STROKE_WIDTH_PX = 4;

    private static final int DEFAULT_SHADOW_COLOR = Color.parseColor("#32FFFFFF");

    private static final int DEFAULT_LABEL_TEXT_COLOR = Color.WHITE;
    private static final int DEFAULT_LABEL_TEXT_SIZE = 40;
    private static final int DEFAULT_LABEL_VERTICAL_OFFSET_PX = 5;
    private static final int DEFAULT_LABEL_HORIZONTAL_OFFSET_PX = 20;


    private float mRatio;

    private Paint mTrackPaint;
    private int mTrackColor = DEFAULT_TRACK_COLOR;
    private int mTrackWidth = DEFAULT_TRACK_WIDTH_PX;
    //PathEffect
    private PathEffect mTrackPathEffect = new DashPathEffect(new float[]{15, 15}, 1);
    private float mTrackRadius;

    private Paint mShadowPaint;
    private int mShadowColor = DEFAULT_SHADOW_COLOR;

    private Paint mSunPaint;
    private int mSunColor = DEFAULT_SUN_COLOR;
    private float mSunRadius = DEFAULT_SUN_RADIUS_PX;
    private Paint.Style mSunPaintStyle = Paint.Style.FILL;
    private TextPaint mLabelPaint;
    private int mLabelTextSize = DEFAULT_LABEL_TEXT_SIZE;
    private int mLabelTextColor = DEFAULT_LABEL_TEXT_COLOR;
    private int mLabelVerticalOffset = DEFAULT_LABEL_VERTICAL_OFFSET_PX;
    private int mLabelHorizontalOffset = DEFAULT_LABEL_HORIZONTAL_OFFSET_PX;

    private static final int MINIMAL_TRACK_RADIUS_PX = 300;


    private Time mSunriseTime;

    private Time mSunsetTime;

    private RectF mBoardRectF = new RectF();

    // Label Formatter - Default is a Simple label formatter.
    private SunriseSunsetLabelFormatter mLabelFormatter = new SimpleSunriseSunsetLabelFormatter ();

    public SunriseSunsetView(Context context) {
        super(context);
        init();
    }

    public SunriseSunsetView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SunriseSunsetView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SunriseSunsetView, defStyleAttr, 0);
        if (a != null) {
            mTrackColor = a.getColor( R.styleable.SunriseSunsetView_ssv_track_color, DEFAULT_TRACK_COLOR);
            mTrackWidth = a.getDimensionPixelSize( R.styleable.SunriseSunsetView_ssv_track_width, DEFAULT_TRACK_WIDTH_PX);

            mShadowColor = a.getColor( R.styleable.SunriseSunsetView_ssv_shadow_color, DEFAULT_SHADOW_COLOR);

            mSunColor = a.getColor( R.styleable.SunriseSunsetView_ssv_sun_color, DEFAULT_SUN_COLOR);
            mSunRadius = a.getDimensionPixelSize( R.styleable.SunriseSunsetView_ssv_sun_radius, DEFAULT_SUN_RADIUS_PX);

            mLabelTextColor = a.getColor( R.styleable.SunriseSunsetView_ssv_label_text_color, DEFAULT_LABEL_TEXT_COLOR);
            mLabelTextSize = a.getDimensionPixelSize( R.styleable.SunriseSunsetView_ssv_label_text_size, DEFAULT_LABEL_TEXT_SIZE);
            mLabelVerticalOffset = a.getDimensionPixelOffset( R.styleable.SunriseSunsetView_ssv_label_vertical_offset, DEFAULT_LABEL_VERTICAL_OFFSET_PX);
            mLabelHorizontalOffset = a.getDimensionPixelOffset( R.styleable.SunriseSunsetView_ssv_label_horizontal_offset, DEFAULT_LABEL_HORIZONTAL_OFFSET_PX);
            a.recycle();
        }
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int paddingRight = getPaddingRight();
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();

        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        if (widthSpecMode == MeasureSpec.AT_MOST) {
            widthSpecSize = paddingLeft + paddingRight + MINIMAL_TRACK_RADIUS_PX * 2 + (int) mSunRadius * 2;
        }

        mTrackRadius = 1.0f * (widthSpecSize - paddingLeft - paddingRight - 2 * mSunRadius) / 2;
        int expectedHeight = (int) (mTrackRadius + mSunRadius + paddingBottom + paddingTop);
        mBoardRectF.set(paddingLeft + mSunRadius, paddingTop + mSunRadius, widthSpecSize - paddingRight - mSunRadius, expectedHeight - paddingBottom);
        setMeasuredDimension(widthSpecSize, expectedHeight);
    }

    private void init() {

        mTrackPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTrackPaint.setStyle(Paint.Style.STROKE);
        prepareTrackPaint();

        mShadowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mShadowPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        prepareShadowPaint();

        mSunPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSunPaint.setStrokeWidth(DEFAULT_SUN_STROKE_WIDTH_PX);
        prepareSunPaint();

        mLabelPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        prepareLabelPaint();
    }


    private void prepareTrackPaint() {
        mTrackPaint.setColor(mTrackColor);
        mTrackPaint.setStrokeWidth(mTrackWidth);
        mTrackPaint.setPathEffect(mTrackPathEffect);
    }

    private void prepareShadowPaint() {
        mShadowPaint.setColor(mShadowColor);
    }

    private void prepareSunPaint() {
        mSunPaint.setColor(mSunColor);
        mSunPaint.setStrokeWidth(DEFAULT_SUN_STROKE_WIDTH_PX);
        mSunPaint.setStyle(mSunPaintStyle);
    }


    private void prepareLabelPaint() {
        mLabelPaint.setColor(mLabelTextColor);
        mLabelPaint.setTextSize(mLabelTextSize);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawSunTrack(canvas);
        drawShadow(canvas);
        drawSun(canvas);
        drawSunriseSunsetLabel(canvas);
    }

    private void drawSunTrack(Canvas canvas) {
        prepareTrackPaint ( );
        canvas.save ( );
        RectF rectF = new RectF ( mBoardRectF.left , mBoardRectF.top , mBoardRectF.right , mBoardRectF.bottom + mBoardRectF.height ( ) );
        canvas.drawArc ( rectF , 180 , 180 , false , mTrackPaint );
        canvas.restore ( );
    }
    private void drawShadow(Canvas canvas) {
        prepareShadowPaint();

        canvas.save();
        Path path = new Path();
        float endY = mBoardRectF.bottom;
        RectF rectF = new RectF(mBoardRectF.left, mBoardRectF.top, mBoardRectF.right, mBoardRectF.bottom + mBoardRectF.height());
        float curPointX = mBoardRectF.left + mTrackRadius - mTrackRadius * (float) Math.cos(Math.PI * mRatio);

        path.moveTo(0, endY);
        path.arcTo(rectF, 180, 180 * mRatio);
        path.lineTo(curPointX, endY);
        path.close();
        canvas.drawPath(path, mShadowPaint);
        canvas.restore();
   //
    }

    private void drawSun(Canvas canvas) {
        prepareSunPaint();
        canvas.save();

        float curPointX = mBoardRectF.left + mTrackRadius - mTrackRadius * (float) Math.cos(Math.PI * mRatio);
        float curPointY = mBoardRectF.bottom - mTrackRadius * (float) Math.sin(Math.PI * mRatio);
        canvas.drawCircle(curPointX, curPointY, mSunRadius, mSunPaint);

        canvas.restore();
    }


    private void drawSunriseSunsetLabel(Canvas canvas) {
        if (mSunriseTime == null || mSunsetTime == null) {
            return;
        }
        prepareLabelPaint();

        canvas.save();

        String sunriseStr = mLabelFormatter.formatSunriseLabel(mSunriseTime);

        mLabelPaint.setTextAlign(Paint.Align.LEFT);
        Paint.FontMetricsInt metricsInt = mLabelPaint.getFontMetricsInt();
        float baseLineX = mBoardRectF.left + mSunRadius + mLabelHorizontalOffset;
        float baseLineY = mBoardRectF.bottom - metricsInt.bottom - mLabelVerticalOffset;
        canvas.drawText(sunriseStr, baseLineX, baseLineY, mLabelPaint);

        mLabelPaint.setTextAlign(Paint.Align.RIGHT);
        String sunsetStr = mLabelFormatter.formatSunsetLabel(mSunsetTime);
        baseLineX = mBoardRectF.right - mSunRadius - mLabelHorizontalOffset;
        canvas.drawText(sunsetStr, baseLineX, baseLineY, mLabelPaint);
        canvas.restore();
    }

    public void setRatio(float ratio) {
        mRatio = ratio;
        invalidate();
    }

    public void setSunriseTime(Time sunriseTime) {
        mSunriseTime = sunriseTime;
    }

    public Time getSunriseTime() {
        return mSunriseTime;
    }

    public void setSunsetTime(Time sunsetTime) {
        mSunsetTime = sunsetTime;
    }

    public Time getSunsetTime() {
        return mSunsetTime;
    }

    public float getSunRadius() {
        return mSunRadius;
    }

    public SunriseSunsetLabelFormatter getLabelFormatter() {
        return mLabelFormatter;
    }

    public void setLabelFormatter(SunriseSunsetLabelFormatter labelFormatter) {
        mLabelFormatter = labelFormatter;
    }

    public void setTrackColor(int trackColor) {
        mTrackColor = trackColor;
    }

    public void setTrackWidth(int trackWidthInPx) {
        mTrackWidth = trackWidthInPx;
    }

    public void setTrackPathEffect(PathEffect trackPathEffect) {
        mTrackPathEffect = trackPathEffect;
    }

    public void setSunColor(int sunColor) {
        mSunColor = sunColor;
    }

    public void setSunRadius(float sunRadius) {
        mSunRadius = sunRadius;
    }

    public void setSunPaintStyle(Paint.Style sunPaintStyle) {
        mSunPaintStyle = sunPaintStyle;
    }

    public void setShadowColor(int shadowColor) {
        mShadowColor = shadowColor;
    }

    public void setLabelTextSize(int labelTextSize) {
        mLabelTextSize = labelTextSize;
    }

    public void setLabelTextColor(int labelTextColor) {
        mLabelTextColor = labelTextColor;
    }

    public void setLabelVerticalOffset(int labelVerticalOffset) {
        mLabelVerticalOffset = labelVerticalOffset;
    }

    public void setLabelHorizontalOffset(int labelHorizontalOffset) {
        mLabelHorizontalOffset = labelHorizontalOffset;
    }

    public void startAnimate() {
        if (mSunriseTime == null || mSunsetTime == null) {
            throw new RuntimeException("You need to set both sunrise and sunset time before start animation");
        }
        int sunrise = mSunriseTime.transformToMinutes();
        int sunset = mSunsetTime.transformToMinutes();
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMinute = calendar.get(Calendar.MINUTE);
        int currentTime = currentHour * Time.MINUTES_PER_HOUR + currentMinute;
        float ratio = 1.0f * (currentTime - sunrise) / (sunset - sunrise);
        ratio = ratio <= 0 ? 0 : (ratio > 1.0f ? 1 : ratio);
        ObjectAnimator animator = ObjectAnimator.ofFloat(this, "ratio", 0f, ratio);
        animator.setDuration(1500L);
        animator.setInterpolator(new LinearInterpolator());
        animator.start();
    }

}
