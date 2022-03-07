package com.tijn.imageloader.base;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.widget.ImageView;
import com.tijn.imageloader.listeners.SourceReadyListener;

public class ImageBuilder {

    private int placeholder;// 默认图
    private int error;//错误占位图

    //圆角相关------------
    private float borderWidth;//边框宽度
    private int borderColor;//边框颜色
    private int radius; //圆角半径
    private int margin; //边距
    /**
     * 圆角位置 1111 顺时针角度
     */
    private int cornerPos;
    private ImageView.ScaleType scaleType; //裁剪type

    private Bitmap.Config bitmapConfig;

    private SourceReadyListener sourceReadyListener;

    public int getPlaceholder() {
        return placeholder;
    }

    public ImageBuilder setPlaceholder(int placeholder) {
        this.placeholder = placeholder;
        return this;
    }

    public int getError() {
        return error;
    }

    public ImageBuilder setError(int error) {
        this.error = error;
        return this;
    }

    public float getBorderWidth() {
        return borderWidth;
    }

    public ImageBuilder setBorderWidth(float borderWidth) {
        this.borderWidth = borderWidth;
        return this;
    }

    public int getBorderColor() {
        return borderColor;
    }

    public ImageBuilder setBorderColor(int borderColor) {
        this.borderColor = borderColor;
        return this;
    }

    public int getRadius() {
        return radius;
    }

    public ImageBuilder setRadius(int radius) {
        this.radius = radius;
        return this;
    }

    public int getMargin() {
        return margin;
    }

    public ImageBuilder setMargin(int margin) {
        this.margin = margin;
        return this;
    }

    public int getCornerPos() {
        return cornerPos;
    }

    public ImageBuilder setCornerPos(int cornerPos) {
        this.cornerPos = cornerPos;
        return this;
    }

    public ImageView.ScaleType getScaleType() {
        return scaleType;
    }

    public ImageBuilder setScaleType(ImageView.ScaleType scaleType) {
        this.scaleType = scaleType;
        return this;
    }

    public SourceReadyListener getSourceReadyListener() {
        return sourceReadyListener;
    }

    public ImageBuilder setSourceReadyListener(SourceReadyListener sourceReadyListener) {
        this.sourceReadyListener = sourceReadyListener;
        return this;
    }

    public Config getBitmapConfig() {
        return bitmapConfig;
    }

    public ImageBuilder setBitmapConfig(Config bitmapConfig) {
        this.bitmapConfig = bitmapConfig;
        return this;
    }
}
