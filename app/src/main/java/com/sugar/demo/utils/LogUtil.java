package com.sugar.demo.utils;

import android.util.Log;

public class LogUtil {
	private static final boolean DEBUG = true;
	private static final boolean ERROR = true;
	private static final String TAG = "tsf";

	public static void e(String paramString) {
		if (ERROR)
			Log.e(TAG, paramString);
	}

	public static void e(String paramString, Throwable paramThrowable) {
		if (ERROR)
			Log.e(TAG, paramString, paramThrowable);
	}

	public static void e(String paramString1, String paramString2) {
		if (ERROR)
			Log.e(paramString1, paramString2);
	}

	public static void e(String paramString1, String paramString2,
                         Throwable paramThrowable) {
		if (ERROR)
			Log.e(paramString1, paramString2, paramThrowable);
	}

	public static void i(String paramString) {
		if (DEBUG)
			Log.i(TAG, paramString);
	}

	public static void i(String paramString1, String paramString2) {
		if (DEBUG)
			Log.i(paramString1, paramString2);
	}

	public static void i(String paramString1, String paramString2,
                         Throwable paramThrowable) {
		if (DEBUG)
			Log.i(paramString1, paramString2, paramThrowable);
	}

	public static void v(String paramString) {
		if (DEBUG)
			Log.v(TAG, paramString);
	}

	public static void v(String paramString1, String paramString2) {
		if (DEBUG)
			Log.v(paramString1, paramString2);
	}

	public static void d(String paramString) {
		if (DEBUG)
			Log.d(TAG, paramString);
	}

	public static void d(String paramString1, String paramString2) {
		if (DEBUG)
			Log.d(paramString1, paramString2);
	}

	public static void d(String paramString1, String paramString2,
                         Throwable paramThrowable) {
		if (DEBUG)
			Log.d(paramString1, paramString2, paramThrowable);
	}
}