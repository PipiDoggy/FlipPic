package com.chethan.flippic;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.Display;

import com.mikepenz.materialdrawer.Drawer;

import java.util.ArrayList;

import hugo.weaving.DebugLog;


/**
 * Created by chethan on 13/01/15.
 */
public class Utils {

    private static Point size = null;

    public static int getPx(Activity activity, int dps) {
        final float scale = activity.getBaseContext().getResources().getDisplayMetrics().density;
        return (int) (dps * scale + 0.5f);
    }

    public static int getPx(Context context, int dps) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dps * scale + 0.5f);
    }

    public static int getSizeX(Activity activity) {
        if (size == null) {
            Display display = activity.getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            Utils.size = size;
        }
        return size.x;
    }

    public static int getSizeY(Activity activity) {
        if (size == null) {
            Display display = activity.getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            Utils.size = size;
        }
        return size.y;
    }

    public static Typeface getLatoRegularTypeface(Context context) {
        return Typeface.createFromAsset(context.getApplicationContext().getAssets(), "fonts/Lato-Regular.ttf");
    }

    public static Typeface getLatoThinTypeface(Context context) {
        return Typeface.createFromAsset(context.getApplicationContext().getAssets(), "fonts/Lato-Thin.ttf");
    }

    public static Typeface getRegularTypeface(Context context) {
        return Typeface.createFromAsset(context.getApplicationContext().getAssets(), "fonts/segoeui.ttf");
    }

    public static Typeface getAlexTypeface(Context context) {
        return Typeface.createFromAsset(context.getApplicationContext().getAssets(), "fonts/AlexBrush-Regular.ttf");
    }

    public static Typeface getLightTypeface(Context context) {
        return Typeface.createFromAsset(context.getApplicationContext().getAssets(), "fonts/segoeuil.ttf");
    }

    public static String parsedStatusUpdate(String update){
        String modifiedUpdate ="\u2022 "+update;
        return modifiedUpdate.replace("|","\n\u2022");
    }

    public static ArrayList<String> parseDelimitedStringToList(String delimitedString){
        ArrayList<String> returnList = new ArrayList<String>();
        String[] tokens = delimitedString.split("\\|");
        for(int i=0;i<tokens.length;i++){
            returnList.add(tokens[i]);
        }
        return returnList;
    }

@DebugLog
    public static Bitmap drawableToBitmap (Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable)drawable).getBitmap();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

}