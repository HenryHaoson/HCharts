package com.zhuhao.hcharts.exceptions;

/**
 * Created by HenryZhuhao on 2017/3/24.
 */

public class SupportException extends RuntimeException {
    public SupportException(String action, String error){
        super(String.format("error:action is %s , error is %s \n",action,error));
    }
}
