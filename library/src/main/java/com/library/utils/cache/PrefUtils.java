package com.library.utils.cache;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * SharePreference封装
 * 
 * @author Kevin
 * @date 2015-10-17
 */

/**
 * SharedPreferences的本质是基于XML文件存储key-value键值对数据，
 * 通常用来存储一些简单的配置信息，用Sqlite数据库来存放并不划算，
 * 因为数据库连接跟操作等耗时大大影响了程序的效率。
 * 其存储位置在/data/data/<包名>/shared_prefs目录下。
 *
 * 另外SharedPreferences只能保存简单类型的数据，例如，String、int等。一般会将复杂类型的数据转换成Base64编码，
 * 然后将转换后的数据以字符串的形式保存在 XML文件中，再用SharedPreferences保存。
 　　使用SharedPreferences保存key-value对的步骤如下：
 　　（1）使用Activity类的getSharedPreferences方法获得SharedPreferences对象，
 		其中存储key-value的文件的名称由getSharedPreferences方法的第一个参数指定，
 		第二个参数指定访问应用程序私有文件的权限。
 　　（2）使用SharedPreferences接口的edit获得SharedPreferences.Editor对象。
 　　（3）通过SharedPreferences.Editor接口的putXxx方法保存key-value对。其中Xxx表示不同的数据类型。
 		例如：字符串类型的value需要用putString方法。
 　　（4）通过SharedPreferences.Editor接口的commit方法保存key-value对。
 		commit方法相当于数据库事务中的提交（commit）操作。
 */
public class PrefUtils {

	private final static String CACHE_NAME = "student_config";

	public static boolean getBoolean(Context ctx, String key, boolean defValue) {
		SharedPreferences sp = ctx.getSharedPreferences(CACHE_NAME,
				Context.MODE_PRIVATE);
		return sp.getBoolean(key, defValue);
	}

	public static void setBoolean(Context ctx, String key, boolean value) {
		SharedPreferences sp = ctx.getSharedPreferences(CACHE_NAME,
				Context.MODE_PRIVATE);
		sp.edit().putBoolean(key, value).commit();
	}

	public static void setString(Context ctx, String key, String value) {
		SharedPreferences sp = ctx.getSharedPreferences(CACHE_NAME,
				Context.MODE_PRIVATE);
		sp.edit().putString(key, value).commit();
	}

	public static String getString(Context ctx, String key, String defValue) {
		SharedPreferences sp = ctx.getSharedPreferences(CACHE_NAME,
				Context.MODE_PRIVATE);
		return sp.getString(key, defValue);
	}

	public static void setInt(Context ctx, String key, int value) {
		SharedPreferences sp = ctx.getSharedPreferences(CACHE_NAME,
				Context.MODE_PRIVATE);
		sp.edit().putInt(key, value).commit();
	}

	public static int getInt(Context ctx, String key, int defValue) {
		SharedPreferences sp = ctx.getSharedPreferences(CACHE_NAME,
				Context.MODE_PRIVATE);
		return sp.getInt(key, defValue);
	}

	public static void remove(Context context, String key) {
		SharedPreferences sp = context.getSharedPreferences(CACHE_NAME,
				Context.MODE_PRIVATE);
		sp.edit().remove(key).commit();
	}

	/**
	 * 把之前commit后保存的所有信息全部进行清空
	 * @param context
     */
	public static void clear(Context context) {
		SharedPreferences sp = context.getSharedPreferences(CACHE_NAME,
				Context.MODE_PRIVATE);
		sp.edit().clear().commit();
	}

	/**
	 * 清除指定的key
	 */
	public static void clearAppointKey(Context ctx, String[] key) {
		for(int i = 0; i < key.length; i ++) {
			remove(ctx, key[i]);
		}
	}
}
