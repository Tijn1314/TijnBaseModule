package com.tijn.imageloader.glide;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.TransformationUtils;
import com.bumptech.glide.util.Util;

import java.nio.charset.Charset;
import java.security.MessageDigest;

/**
 * 借鉴https://github.com/wasabeef/glide-transformations/blob/master/transformations/src/main/java/jp/wasabeef/glide/transformations/CropTransformation.java
 * 用一个整形表示哪些边角需要加圆角边框
 * 例如：0b1000,表示左上角需要加圆角边框
 * 0b1110 表示左上右上右下需要加圆角边框
 * 0b0000表示不加圆形边框
 */
public class GlideBorderRoundTransformation extends BitmapTransformation {
    private static final String ID = "com.tijn.imageloader.glide.GlideBorderRoundTransformation";
    private static final byte[] ID_BYTES = ID.getBytes(CHARSET);

    private BitmapPool bitmapPool;
    private int radius; //圆角半径
    private int margin; //边距

    private float borderWidth;//边框宽度
    private int borderColor;//边框颜色
    private int cornerPos; //圆角位置
    private ImageView.ScaleType scaleType = ImageView.ScaleType.FIT_CENTER; //裁剪type 默认fitstart

    public GlideBorderRoundTransformation(Context context) {
        bitmapPool = Glide.get(context).getBitmapPool();
    }

    @Override
    protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
        switch (scaleType) {
            case CENTER_CROP:
                return transform(TransformationUtils.centerCrop(pool, toTransform, outWidth, outHeight));
            case CENTER_INSIDE:
                return transform(TransformationUtils.centerInside(pool, toTransform, outWidth, outHeight));
            case FIT_START:
                return transform(transformFitStart(pool, toTransform, outWidth, outHeight));
            default:
                return transform(TransformationUtils.fitCenter(pool, toTransform, outWidth, outHeight));
        }
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        messageDigest.update(ID_BYTES);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof GlideBorderRoundTransformation;
    }

    @Override
    public int hashCode() {
        return Util.hashCode(ID.hashCode());
    }

    public Bitmap transform(Bitmap source) {
        int width = source.getWidth();
        int height = source.getHeight();

        Bitmap bitmap = bitmapPool.get(width, height, Bitmap.Config.ARGB_8888);
        if (bitmap == null) {
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);//新建一个空白的bitmap
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(new BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));//设置要绘制的图形

        Paint borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);//设置边框样式
        borderPaint.setColor(borderColor);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(borderWidth);

        drawRoundRect(canvas, paint, width, height, borderPaint);
        return bitmap;
    }


    private void drawRoundRect(Canvas canvas, Paint paint, float width, float height, Paint borderPaint) {
        float right = width - margin;
        float bottom = height - margin;
        float halfBorder = borderWidth / 2;
        Path path = new Path();

        float[] pos = new float[8];
        int shift = cornerPos;

        int index = 3;

        while (index >= 0) {//设置四个边角的弧度半径
            pos[2 * index + 1] = ((shift & 1) > 0) ? radius : 0;
            pos[2 * index] = ((shift & 1) > 0) ? radius : 0;
            shift = shift >> 1;
            index--;
        }

        path.addRoundRect(new RectF(margin + halfBorder, margin + halfBorder, right - halfBorder, bottom - halfBorder),
                pos
                , Path.Direction.CW);

        canvas.drawPath(path, paint);//绘制要加载的图形

        canvas.drawPath(path, borderPaint);//绘制边框

    }


    private Bitmap transformFitStart(@NonNull BitmapPool pool,
                                     @NonNull Bitmap toTransform, int width, int height) {
        width = width == 0 ? toTransform.getWidth() : width;
        height = height == 0 ? toTransform.getHeight() : height;

        Bitmap.Config config =
                toTransform.getConfig() != null ? toTransform.getConfig() : Bitmap.Config.ARGB_8888;
        Bitmap bitmap = pool.get(width, height, config);

        bitmap.setHasAlpha(true);

        float scaleX = (float) width / toTransform.getWidth();
        float scaleY = (float) height / toTransform.getHeight();
        float scale = Math.max(scaleX, scaleY);

        float scaledWidth = scale * toTransform.getWidth();
        float scaledHeight = scale * toTransform.getHeight();
        float left = (width - scaledWidth) / 2;
        float top = 0;
        RectF targetRect = new RectF(left, top, left + scaledWidth, top + scaledHeight);

        bitmap.setDensity(toTransform.getDensity());

        Canvas canvas = new Canvas(bitmap);
        canvas.drawBitmap(toTransform, null, targetRect, null);

        return bitmap;
    }

    public GlideBorderRoundTransformation setRadius(int radius) {
        this.radius = radius;
        return this;
    }

    public GlideBorderRoundTransformation setMargin(int margin) {
        this.margin = margin;
        return this;
    }

    public GlideBorderRoundTransformation setBorderWidth(float borderWidth) {
        this.borderWidth = borderWidth;
        return this;
    }

    public GlideBorderRoundTransformation setBorderColor(int borderColor) {
        this.borderColor = borderColor;
        return this;
    }

    public GlideBorderRoundTransformation setCornerPos(int cornerPos) {
        this.cornerPos = cornerPos;
        return this;
    }

    public GlideBorderRoundTransformation setScaleType(ImageView.ScaleType scaleType) {
        if (scaleType != null) this.scaleType = scaleType;
        return this;
    }
}
