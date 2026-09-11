package com.wur7.store.util.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogUtil {
    private static Logger logger = LoggerFactory.getLogger(LogUtil.class);

    public static void info(String msg){
        logger.info(packageString()+msg);
    }

    public static void info(String format,Object ... arguments) {
        logger.info(packageString()+format, arguments);
    }

    public static void info(Object msg,Throwable e){
        logger.info(packageString()+msg, e);
    }

    public static void debug(String msg){
        logger.debug(packageString()+msg);
    }

    public static void debug(String format,Object ... arguments){
        logger.debug(packageString()+format, arguments);
    }

    public static void debug(Object msg,Throwable e){
        logger.debug(packageString() + msg, e);
    }

    public static void warn(String msg){
        logger.warn(packageString()+msg);
    }

    public static void warn(String format,Object ... arguments){
        logger.warn(packageString()+format, arguments);
    }

    public static void warn(Object msg , Throwable e){
        logger.warn(packageString()+msg, e);
    }

    public static void error(String msg){
        logger.error(packageString()+msg);
    }

    public static void error(String format,Object ...  arguments) {
        logger.error(packageString()+format, arguments);
    }

    public static void error(Object msg, Throwable e) {
        logger.error(packageString()+msg, e);
    }

    private static String packageString() {
        String className = Thread.currentThread().getStackTrace()[3]
                .getClassName();
        String methodName = Thread.currentThread().getStackTrace()[3]
                .getMethodName();
        int lineNumber = Thread.currentThread().getStackTrace()[3]
                .getLineNumber();
        return className + "." + methodName + ":  " + lineNumber + "   日志信息: ";
    }
}
