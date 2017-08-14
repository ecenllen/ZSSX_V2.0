/*******************************************************************************
 * Copyright 2011-2014 Sergey Tarasevich
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.gta.utils.resource;

/**
 * "Less-word" analog of Android {@link android.util.Log logger}
 *
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 * @since 1.0.0
 */
public final class L {

    private static final String LOG_FORMAT = "%1$s\n%2$s";
    private static final int ERROR = android.util.Log.ERROR;
    private static final int WARN = android.util.Log.WARN;
    private static final int INFO = android.util.Log.INFO;
    private static final int DEBUG = android.util.Log.DEBUG;
    private static final int VERBOSE = android.util.Log.VERBOSE;
    private static volatile boolean writeLogs = true;

    private L() {
    }

    /**
     * Enables logger (if {@link #disableLogging()} was called before)
     *
     * @deprecated Use {@link #writeLogs(boolean) writeLogs(true)} instead
     */
    @Deprecated
    public static void enableLogging() {
        writeLogs(true);
    }

    /**
     * Disables logger, no logs will be passed to LogCat, all log methods will do nothing
     *
     * @deprecated Use {@link #writeLogs(boolean) writeLogs(false)} instead
     */
    @Deprecated
    public static void disableLogging() {
        writeLogs(false);
    }

    /** Enables/disables logging of {@link } completely (even error logs). */
    public static void writeLogs(boolean writeLogs) {
        L.writeLogs = writeLogs;
    }

    public static void v(boolean ifLog, Object tagObject, String message, Object... args) {
        String tag = tagObject.getClass().getSimpleName();
        v(ifLog, tag, message, args);
    }

    public static void v(String message, Object... args) {
        v(true, "LOG", message, args);
    }

    public static void v(boolean ifLog, String tag, String message, Object... args) {
        if (ifLog) {
            log(VERBOSE, tag, null, message, args);
        }
    }

    public static void d(boolean ifLog, Object tagObject, String message, Object... args) {
        String tag = tagObject.getClass().getSimpleName();
        d(ifLog, tag, message, args);
    }

    public static void d(String message, Object... args) {
        d(true, "LOG", message, args);
    }

    public static void d(boolean ifLog, String tag, String message, Object... args) {
        if (ifLog) {
            log(DEBUG, tag, null, message, args);
        }
    }

    public static void i(boolean ifLog, Object tagObject, String message, Object... args) {
        String tag = tagObject.getClass().getSimpleName();
        i(ifLog, tag, message, args);
    }

    public static void i(boolean ifLog, String tag, String message, Object... args) {
        if (ifLog) {
            log(INFO, tag, null, message, args);
        }
    }

    public static void w(Object tagObject, String message, Object... args) {
        String tag = tagObject.getClass().getSimpleName();
        log(WARN, tag, null, message, args);
    }

    public static void w(String tag, String message, Object... args) {
        log(WARN, tag, null, message, args);
    }

    public static void e(String tag, Throwable ex) {
        e(tag, ex, null);
    }

    public static void e(String tag, String message, Object... args) {
        e(tag, null, message, args);
    }

    public static void e(Object tagObject, String message, Object... args) {
        e(tagObject, null, message, args);
    }

    public static void e(Object tagObject, Throwable ex) {
        e(tagObject, ex, null);
    }

    public static void e(Object tagObject, Throwable ex, String message, Object... args) {
        String tag = tagObject.getClass().getSimpleName();
        e(tag, ex, message, args);
    }

    public static void e(String tag, Throwable ex, String message, Object... args) {
        log(ERROR, tag, ex, message, args);
    }


    private static void log(int priority, String tag, Throwable ex, String message, Object... args) {
        if (!writeLogs) return;
        if (args.length > 0) {
            message = String.format(message, args);
        }

        String log;
        if (ex == null) {
            log = message;
        } else {
            String logMessage = message == null ? ex.getMessage() : message;
            String logBody = android.util.Log.getStackTraceString(ex);
            log = String.format(LOG_FORMAT, logMessage, logBody);
        }
        android.util.Log.println(priority, tag, log);
    }

    public static void json(String jsonFormat) {
        printLog(null, jsonFormat);
    }


    public static void json(String tag, String jsonFormat) {
        printLog(tag, jsonFormat);
    }


    private static void printLog(String tagStr, Object objectMsg) {
        String[] contents = wrapperContent(tagStr, objectMsg);
        String tag = contents[0];
        String msg = contents[1];
        String headString = contents[2];
        JsonLog.printJson(tag, msg, headString);
    }

    private static String[] wrapperContent(String tagStr, Object objectMsg) {

        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        int index = 5;
        String className = stackTrace[index].getFileName();
        String methodName = stackTrace[index].getMethodName();
        int lineNumber = stackTrace[index].getLineNumber();
        String methodNameShort = methodName.substring(0, 1).toUpperCase() + methodName.substring(1);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[ (").append(className).append(":").append(lineNumber).append(")#").append(methodNameShort).append(" ] ");

        String tag = (tagStr == null ? className : tagStr);
        String msg = (objectMsg == null) ? NULL_TIPS : objectMsg.toString();
        String headString = stringBuilder.toString();

        return new String[]{tag, msg, headString};
    }

    static String NULL_TIPS = "Log with null object";
}