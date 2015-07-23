package com.fellowshipoftheloosescrews.camera;

import android.graphics.Bitmap;
import android.graphics.Color;

/**
 * Created by Thomas on 7/21/2015.
 *
 * Return from ThomasCamera, holds a bitmap and does
 * image calculations
 */
public class Image {

    public Bitmap bitmap;

    public Image(Bitmap bitmap)
    {
        this.bitmap = bitmap;
    }

    public int getWidth()
    {
        return bitmap.getWidth();
    }

    public int getHeight()
    {
        return bitmap.getHeight();
    }

    public int getR(int x, int y)
    {
        return Color.red(bitmap.getPixel(x, y));
    }

    public int getG(int x, int y)
    {
        return Color.green(bitmap.getPixel(x, y));
    }

    public int getB(int x, int y)
    {
        return Color.blue(bitmap.getPixel(x, y));
    }

    public int getColor(int x, int y)
    {
        return bitmap.getPixel(x, y);
    }

    public enum ColorCode
    {
        RED,
        GREEN,
        BLUE,
        FULL
    }

    /**
     * Returns a 1d array of the pixels in an area
     * @param color What color values to return
     * @param x Starting x coordinate
     * @param y Starting y coordinate
     * @param width The width of the area
     * @param height The height of the area
     * @return An array of the colors in the area, it's single-int colors if the selected value was full
     */
    public int[] getPixels(ColorCode color, int x, int y, int width, int height)
    {
        int[] pixels = {};

        bitmap.getPixels(pixels, 0, 0, x, y, width, height);

        switch (color)
        {
            case RED:
                for(int i = 0; i < pixels.length; i++)
                {
                    pixels[i] = Color.red(pixels[i]);
                }
                return pixels;
            case GREEN:
                for(int i = 0; i < pixels.length; i++)
                {
                    pixels[i] = Color.green(pixels[i]);
                }
                return pixels;
            case BLUE:
                for(int i = 0; i < pixels.length; i++)
                {
                    pixels[i] = Color.blue(pixels[i]);
                }
                return pixels;
            default:
                return pixels;
        }
    }

    public int[] getPixels(ColorCode color)
    {
        return getPixels(color, 0, 0, bitmap.getWidth(), bitmap.getHeight());
    }
}
