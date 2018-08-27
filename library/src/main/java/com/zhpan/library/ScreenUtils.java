package com.zhpan.library;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 *
 * ScreenUtils
 * @author  ssw
 */
public class ScreenUtils {
    private static int screenW;
    private static int screenH;
    private static float screenDensity;

    public static int getScreenW(Context context){
        if (screenW == 0){
            initScreen(context);
        }
        return screenW;
    }

    public static int getScreenH(Context context){
        if (screenH == 0){
            initScreen(context);
        }
        return screenH;
    }

    public static float getScreenDensity(Context context){
        if (screenDensity == 0){
            initScreen(context);
        }
        return screenDensity;
    }

    private static void initScreen(Context context){
        DisplayMetrics metric = context.getResources().getDisplayMetrics();
        screenW = metric.widthPixels;
        screenH = metric.heightPixels;
        screenDensity = metric.density;
    }
}
