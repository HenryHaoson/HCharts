package cn.henry.zhuhao.animatordemo.utils;

import android.graphics.Color;

/**
 * Created by zhuhao on 28/7/2017.
 */

public class ColorUtils {
    public static int getColorWithAlpha(int color, float ratio) {
        int newColor = 0;
        int alpha = Math.round(Color.alpha(color) * ratio);
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);
        newColor = Color.argb(alpha, r, g, b);
        return newColor;
    }
}
