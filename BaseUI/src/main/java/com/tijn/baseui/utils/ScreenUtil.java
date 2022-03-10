package com.tijn.baseui.utils;


import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

public class ScreenUtil {


    public static int dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public static int px2dp(Context context, float px) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue
     * @return
     */
    public static int px2sp(Context context,float pxValue) {
        final float fontScale =context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     * @return
     */
    public static int sp2px(Context context,float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static Rect getViewRect(View view, Rect rect) {
        if (rect == null) {
            rect = new Rect();
        }
        ((Activity) view.getContext()).getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        int decorViewTop = rect.top;
        view.getGlobalVisibleRect(rect);
        rect.top = rect.top - decorViewTop;
        rect.bottom = rect.bottom - decorViewTop;
        return rect;
    }

    /**
     * 或者View实际的位置（主要是减去了标题栏高度）
     *
     * @param view
     * @return
     */
    public static Rect getViewRect(View view) {
        return getViewRect(view, null);
    }

    /**
     * 获取标题栏高度
     */
    @Deprecated
    public static int getBarHeight(Context context) {
        return StatusBarHelper.getStatusbarHeight(context);
    }
//
    public static DisplayMetrics getMetrics(Context context) {
        DisplayMetrics metrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(metrics);
        return metrics;
    }

    /**
     * 获取屏幕宽度
     */
    public static int getScreenWidth(Context context) {
        return getMetrics(context).widthPixels;
    }

    /**
     * 获取屏幕高度
     */
    public static int getScreenHeight(Context context) {
        return getMetrics(context).heightPixels;
    }

    /**
     * 获取屏幕密度
     */
    public static float getScreenDensity(Context context) {
        return getMetrics(context).density;
    }


}
