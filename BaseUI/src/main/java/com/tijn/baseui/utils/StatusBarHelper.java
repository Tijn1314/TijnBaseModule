package com.tijn.baseui.utils;


import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.ColorInt;
import androidx.annotation.IntDef;
import androidx.core.view.ViewCompat;


import com.tijn.baseui.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;
import java.lang.reflect.Method;


public class StatusBarHelper {

    private final static int STATUSBAR_TYPE_DEFAULT = 0;
    private final static int STATUSBAR_TYPE_MIUI = 1;
    private final static int STATUSBAR_TYPE_FLYME = 2;
    private final static int STATUSBAR_TYPE_ANDROID6 = 3; // Android 6.0
    private final static int STATUS_BAR_DEFAULT_HEIGHT_DP = 25; // 大部分状态栏都是25dp
    // 在某些机子上存在不同的density值，所以增加两个虚拟值
    public static float sVirtualDensity = -1;
    public static float sVirtualDensityDpi = -1;
    private static int sStatusbarHeight = -1;
    private static @StatusBarType
    int mStatuBarType = STATUSBAR_TYPE_DEFAULT;
    private static Integer sTransparentValue;

    public static void setStatusBarColor(Activity activity, int color) {
        // 5.0 以上
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().setStatusBarColor(color);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
            View statusView = decorView.findViewById(R.id.status_bar_holder_view);
            if (statusView == null) {
                activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                final int statusBarHeight = StatusBarHelper.getStatusbarHeight(activity);
                statusView = new View(activity);
                statusView.setId(R.id.status_bar_holder_view);
                ViewGroup.LayoutParams params = new ViewGroup
                        .LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, statusBarHeight);
                statusView.setLayoutParams(params);
                decorView.addView(statusView);
            }
            statusView.setBackgroundColor(color);
        }
    }

    public static void setStatusBarTranslucent(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            View decorView = activity.getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    public static void translucent(Activity activity) {
        translucent(activity.getWindow());
    }

    public static void translucent(Window window) {
        translucent(window, 0x40000000);
    }

    private static boolean supportTranslucent() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
                // Essential Phone 在 Android 8 之前沉浸式做得不全，系统不从状态栏顶部开始布局却会下发 WindowInsets
                && !(DeviceHelper.isEssentialPhone() && Build.VERSION.SDK_INT < 26);
    }

    /**
     * 沉浸式状态栏。
     * 支持 4.4 以上版本的 MIUI 和 Flyme，以及 5.0 以上版本的其他 Android。
     *
     * @param activity 需要被设置沉浸式状态栏的 Activity。
     */
    public static void translucent(Activity activity, @ColorInt int colorOn5x) {
        Window window = activity.getWindow();
        translucent(window, colorOn5x);
    }

    @TargetApi(19)
    public static void translucent(Window window, @ColorInt int colorOn5x) {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            // 版本小于4.4，绝对不考虑沉浸式
            return;
        }
        // 小米和魅族4.4 以上版本支持沉浸式
        if (DeviceHelper.isMeizu() || DeviceHelper.isMIUI()) {
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            return;
        }

        if (NotchHelper.isNotchOfficialSupport()) {
            handleDisplayCutoutMode(window);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && supportTransclentStatusBar6()) {
                // android 6以后可以改状态栏字体颜色，因此可以自行设置为透明
                // ZUK Z1是个另类，自家应用可以实现字体颜色变色，但没开放接口
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.TRANSPARENT);
            } else {
                // android 5不能修改状态栏字体颜色，因此直接用FLAG_TRANSLUCENT_STATUS，nexus表现为半透明
                // 魅族和小米的表现如何？
                // update: 部分手机运用FLAG_TRANSLUCENT_STATUS时背景不是半透明而是没有背景了。。。。。
                // 采取setStatusBarColor的方式，部分机型不支持，那就纯黑了，保证状态栏图标可见
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(colorOn5x);
            }
        }
    }

    @TargetApi(28)
    private static void handleDisplayCutoutMode(final Window window) {
        View decorView = window.getDecorView();
        if (decorView != null) {
            if (ViewCompat.isAttachedToWindow(decorView)) {
                realHandleDisplayCutoutMode(window, decorView);
            } else {
                decorView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
                    @Override
                    public void onViewAttachedToWindow(View v) {
                        v.removeOnAttachStateChangeListener(this);
                        realHandleDisplayCutoutMode(window, v);
                    }

                    @Override
                    public void onViewDetachedFromWindow(View v) {

                    }
                });
            }
        }
    }

    @TargetApi(28)
    private static void realHandleDisplayCutoutMode(Window window, View decorView) {
        if (decorView.getRootWindowInsets() != null &&
                decorView.getRootWindowInsets().getDisplayCutout() != null) {
            WindowManager.LayoutParams params = window.getAttributes();
            params.layoutInDisplayCutoutMode = WindowManager.LayoutParams
                    .LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
            window.setAttributes(params);
        }
    }

    /**
     * 设置状态栏黑色字体图标，
     * 支持 4.4 以上版本 MIUI 和 Flyme，以及 6.0 以上版本的其他 Android
     *
     * @param activity 需要被处理的 Activity
     */
    public static boolean setStatusBarLightMode(Activity activity) {
        if (activity == null) return false;
        // 无语系列：ZTK C2016只能时间和电池图标变色。。。。
        if (DeviceHelper.isZTKC2016()) {
            return false;
        }

        if (mStatuBarType != STATUSBAR_TYPE_DEFAULT) {
            return setStatusBarLightMode(activity, mStatuBarType);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (isMIUICustomStatusBarLightModeImpl() && MIUISetStatusBarLightMode(activity.getWindow(), true)) {
                mStatuBarType = STATUSBAR_TYPE_MIUI;
                return true;
            } else if (FlymeSetStatusBarLightMode(activity.getWindow(), true)) {
                mStatuBarType = STATUSBAR_TYPE_FLYME;
                return true;
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Android6SetStatusBarLightMode(activity.getWindow(), true);
                mStatuBarType = STATUSBAR_TYPE_ANDROID6;
                return true;
            }
        }
        return false;
    }

    /**
     * 已知系统类型时，设置状态栏黑色字体图标。
     * 支持 4.4 以上版本 MIUI 和 Flyme，以及 6.0 以上版本的其他 Android
     *
     * @param activity 需要被处理的 Activity
     * @param type     StatusBar 类型，对应不同的系统
     */
    private static boolean setStatusBarLightMode(Activity activity, @StatusBarType int type) {
        if (type == STATUSBAR_TYPE_MIUI) {
            return MIUISetStatusBarLightMode(activity.getWindow(), true);
        } else if (type == STATUSBAR_TYPE_FLYME) {
            return FlymeSetStatusBarLightMode(activity.getWindow(), true);
        } else if (type == STATUSBAR_TYPE_ANDROID6) {
            return Android6SetStatusBarLightMode(activity.getWindow(), true);
        }
        return false;
    }


    /**
     * 设置状态栏白色字体图标
     * 支持 4.4 以上版本 MIUI 和 Flyme，以及 6.0 以上版本的其他 Android
     */
    public static boolean setStatusBarDarkMode(Activity activity) {
        if (activity == null) return false;
        if (mStatuBarType == STATUSBAR_TYPE_DEFAULT) {
            // 默认状态，不需要处理
            return true;
        }

        if (mStatuBarType == STATUSBAR_TYPE_MIUI) {
            return MIUISetStatusBarLightMode(activity.getWindow(), false);
        } else if (mStatuBarType == STATUSBAR_TYPE_FLYME) {
            return FlymeSetStatusBarLightMode(activity.getWindow(), false);
        } else if (mStatuBarType == STATUSBAR_TYPE_ANDROID6) {
            return Android6SetStatusBarLightMode(activity.getWindow(), false);
        }
        return true;
    }

    @TargetApi(23)
    private static int changeStatusBarModeRetainFlag(Window window, int out) {
        out = retainSystemUiFlag(window, out, View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        out = retainSystemUiFlag(window, out, View.SYSTEM_UI_FLAG_FULLSCREEN);
        out = retainSystemUiFlag(window, out, View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        out = retainSystemUiFlag(window, out, View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        out = retainSystemUiFlag(window, out, View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        out = retainSystemUiFlag(window, out, View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        return out;
    }

    public static int retainSystemUiFlag(Window window, int out, int type) {
        int now = window.getDecorView().getSystemUiVisibility();
        if ((now & type) == type) {
            out |= type;
        }
        return out;
    }


    /**
     * 设置状态栏字体图标为深色，Android 6
     *
     * @param window 需要设置的窗口
     * @param light  是否把状态栏字体及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    @TargetApi(23)
    private static boolean Android6SetStatusBarLightMode(Window window, boolean light) {
        View decorView = window.getDecorView();
        int systemUi = light ? View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR : View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        systemUi = changeStatusBarModeRetainFlag(window, systemUi);
        decorView.setSystemUiVisibility(systemUi);
        if (DeviceHelper.isMIUIV9()) {
            // MIUI 9 低于 6.0 版本依旧只能回退到以前的方案
            MIUISetStatusBarLightMode(window, light);
        }
        return true;
    }

    /**
     * 设置状态栏字体图标为深色，需要 MIUIV6 以上
     *
     * @param window 需要设置的窗口
     * @param light  是否把状态栏字体及图标颜色设置为深色
     * @return boolean 成功执行返回 true
     */
    @SuppressWarnings("unchecked")
    public static boolean MIUISetStatusBarLightMode(Window window, boolean light) {
        boolean result = false;
        if (window != null) {
            Class clazz = window.getClass();
            try {
                int darkModeFlag;
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                if (light) {
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag);//状态栏透明且黑色字体
                } else {
                    extraFlagField.invoke(window, 0, darkModeFlag);//清除黑色字体
                }
                result = true;
            } catch (Exception ignored) {

            }
        }
        return result;
    }

    /**
     * 更改状态栏图标、文字颜色的方案是否是MIUI自家的， MIUI9 && Android 6 之后用回Android原生实现
     * 见小米开发文档说明：https://dev.mi.com/console/doc/detail?pId=1159
     */
    private static boolean isMIUICustomStatusBarLightModeImpl() {
        if (DeviceHelper.isMIUIV9() && Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        return DeviceHelper.isMIUIV5() || DeviceHelper.isMIUIV6() ||
                DeviceHelper.isMIUIV7() || DeviceHelper.isMIUIV8();
    }

    /**
     * 设置状态栏图标为深色和魅族特定的文字风格
     * 可以用来判断是否为 Flyme 用户
     *
     * @param window 需要设置的窗口
     * @param light  是否把状态栏字体及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    public static boolean FlymeSetStatusBarLightMode(Window window, boolean light) {
        boolean result = false;
        if (window != null) {
            // flyme 在 6.2.0.0A 支持了 Android 官方的实现方案，旧的方案失效
            Android6SetStatusBarLightMode(window, light);

            try {
                WindowManager.LayoutParams lp = window.getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class
                        .getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (light) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                window.setAttributes(lp);
                result = true;
            } catch (Exception ignored) {

            }
        }
        return result;
    }

    /**
     * 获取是否全屏
     *
     * @return 是否全屏
     */
    public static boolean isFullScreen(Activity activity) {
        boolean ret = false;
        try {
            WindowManager.LayoutParams attrs = activity.getWindow().getAttributes();
            ret = (attrs.flags & WindowManager.LayoutParams.FLAG_FULLSCREEN) != 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    /**
     * API19之前透明状态栏：获取设置透明状态栏的system ui visibility的值，这是部分有提供接口的rom使用的
     * http://stackoverflow.com/questions/21865621/transparent-status-bar-before-4-4-kitkat
     */
    public static Integer getStatusBarAPITransparentValue(Context context) {
        if (sTransparentValue != null) {
            return sTransparentValue;
        }
        String[] systemSharedLibraryNames = context.getPackageManager()
                .getSystemSharedLibraryNames();
        String fieldName = null;
        for (String lib : systemSharedLibraryNames) {
            if ("touchwiz".equals(lib)) {
                fieldName = "SYSTEM_UI_FLAG_TRANSPARENT_BACKGROUND";
            } else if (lib.startsWith("com.sonyericsson.navigationbar")) {
                fieldName = "SYSTEM_UI_FLAG_TRANSPARENT";
            }
        }

        if (fieldName != null) {
            try {
                Field field = View.class.getField(fieldName);
                if (field != null) {
                    Class<?> type = field.getType();
                    if (type == int.class) {
                        sTransparentValue = field.getInt(null);
                    }
                }
            } catch (Exception ignored) {
            }
        }
        return sTransparentValue;
    }

    /**
     * 检测 Android 6.0 是否可以启用 window.setStatusBarColor(Color.TRANSPARENT)。
     */
    public static boolean supportTransclentStatusBar6() {
        return !(DeviceHelper.isZUKZ1() || DeviceHelper.isZTKC2016());
    }

    /**
     * 获取状态栏的高度。
     */
    public static int getStatusbarHeight(Context context) {
        if (sStatusbarHeight == -1) {
            initStatusBarHeight(context);
        }
        return sStatusbarHeight;
    }

    private static void initStatusBarHeight(Context context) {
        Class<?> clazz;
        Object obj = null;
        Field field = null;
        try {
            clazz = Class.forName("com.android.internal.R$dimen");
            obj = clazz.newInstance();
            if (DeviceHelper.isMeizu()) {
                try {
                    field = clazz.getField("status_bar_height_large");
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }
            if (field == null) {
                field = clazz.getField("status_bar_height");
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
        if (field != null && obj != null) {
            try {
                int id = Integer.parseInt(field.get(obj).toString());
                sStatusbarHeight = context.getResources().getDimensionPixelSize(id);
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
        if (DeviceHelper.isTablet(context)
                && sStatusbarHeight > ScreenUtil.dp2px(context, STATUS_BAR_DEFAULT_HEIGHT_DP)
                && !DeviceHelper.brandEquals("readboy")) {
            //状态栏高度大于25dp的平板，状态栏通常在下方
            // 读书郎平板比较特殊，跳过这个逻辑

            //平板横屏宽度>高度
            if (ScreenUtil.getScreenWidth(context) > ScreenUtil.getScreenHeight(context)) {
                sStatusbarHeight = 0;
            }
        } else {
            if (sStatusbarHeight <= 0) {
                if (sVirtualDensity == -1) {
                    sStatusbarHeight = ScreenUtil.dp2px(context, STATUS_BAR_DEFAULT_HEIGHT_DP);
                } else {
                    sStatusbarHeight = (int) (STATUS_BAR_DEFAULT_HEIGHT_DP * sVirtualDensity + 0.5f);
                }
            }
        }
    }

    public static void setVirtualDensity(float density) {
        sVirtualDensity = density;
    }

    public static void setVirtualDensityDpi(float densityDpi) {
        sVirtualDensityDpi = densityDpi;
    }

    @IntDef({STATUSBAR_TYPE_DEFAULT, STATUSBAR_TYPE_MIUI, STATUSBAR_TYPE_FLYME, STATUSBAR_TYPE_ANDROID6})
    @Retention(RetentionPolicy.SOURCE)
    private @interface StatusBarType {
    }

}
