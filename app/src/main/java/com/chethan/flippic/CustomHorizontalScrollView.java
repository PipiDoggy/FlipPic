package com.chethan.flippic;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import hugo.weaving.DebugLog;
import timber.log.Timber;

/**
 * Created by chethan on 23/02/15.
 */
public class CustomHorizontalScrollView extends HorizontalScrollView implements SensorEventListener {

    private static final float ALPHA = 0.2f;
    float smoothedValue;
    int mImageWidth = -1;
    int mImageHeight = -1;
    SensorManager mSensorManager;
    Sensor mSensor;
    int mMaxScroll;

    public CustomHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);

        Timber.plant(new Timber.DebugTree());
        mSensorManager = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_GAME);

    }

    public void updateImageSize(int size){
        mImageWidth = size;
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
//        mImageWidth = ((ImageView)CustomHorizontalScrollView.this.getChildAt(0)).getDrawable().getIntrinsicWidth();
        mMaxScroll = (10000- getContext().getResources().getDisplayMetrics().widthPixels) / 2;
        float value = event.values[1];
        value = value*100;
        smoothedValue = smooth(value, smoothedValue);
        value = smoothedValue;

        int scrollX = CustomHorizontalScrollView.this.getScrollX();
        if (scrollX + value >= mMaxScroll) value = mMaxScroll - scrollX;
        if (scrollX + value <= -mMaxScroll) value = -mMaxScroll - scrollX;
        CustomHorizontalScrollView.this.scrollBy((int) value, 0);
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private static float smooth(float input, float output) {
        return (int) (output + ALPHA * (input - output));
    }
}
