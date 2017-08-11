package com.zhuhao.hcharts.exceptions;

/**
 * Author : zhuhao
 * Date : 11/8/2017
 *
 * @Last Modified Time :11/8/2017
 * Description : 当要绘制的东西在画布的范围之外
 */

public class ContentOutOfCanvasException extends SupportException {
    public ContentOutOfCanvasException(String error) {
        super(ContentOutOfCanvasException.class.getName(),String.format("%s is out of the Canvas", error));
    }
}
