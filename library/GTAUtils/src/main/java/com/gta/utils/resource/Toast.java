/*******************************************************************************
 * Copyright 2011-2014 Sergey Tarasevich
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.gta.utils.resource;

import android.content.Context;

/**
 * "Less-word" analog of Android {@link android.util.Log logger}
 *
 * @author Woode Wang 
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 * @since 1.0.0
 */
public final class Toast {

	private Toast() {
	}
	public static void Long(Context context, int resId) {
		toast(context, android.widget.Toast.LENGTH_LONG, resId);
	}
	public static void Long(Context context, int resId, Object... args) {
		toast(context, android.widget.Toast.LENGTH_LONG, resId, args);
	}
	
	public static void Long(Context context, String message) {
		toast(context, android.widget.Toast.LENGTH_LONG, message);
	}
	public static void Long(Context context, String message, Object... args) {
		toast(context, android.widget.Toast.LENGTH_LONG, message, args);
	}
	
	public static void Short(Context context, int resId) {
		toast(context, android.widget.Toast.LENGTH_SHORT, resId);
	}
	public static void Short(Context context, int resId, Object... args) {
		toast(context, android.widget.Toast.LENGTH_SHORT, resId, args);
	}
	
	public static void Short(Context context, String message) {
		toast(context, android.widget.Toast.LENGTH_SHORT, message);
	}
	public static void Short(Context context, String message, Object... args) {
		toast(context, android.widget.Toast.LENGTH_SHORT, message,args);
	}

	private static void toast(Context context, int duration, int resId, Object... args) {
		String message = context.getString(resId);
		if (args.length > 0) {
			message = String.format(message, args);
		}
		android.widget.Toast.makeText(context, message, duration).show();
	}
	private static void toast(Context context, int duration, String message, Object... args) {
		if (args.length > 0) {
			message = String.format(message, args);
		}
		android.widget.Toast.makeText(context, message, duration).show();
	}
}