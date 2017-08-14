/**
 *
 * @author Woode Wang E-mail:wangwoode@qq.com
 * @version 创建时间：2015-2-5 上午10:55:09
 */
package com.gta.utils.resource;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.text.TextUtils;

import com.example.gtalutils.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @description 电话相关操作集合的帮助类
 * @author Woode Wang 
 * @since 1.0.0
 */
public class PhoneHelper {

    /**
     * 拨打电话
     */
    public static void dialPhone(final Context context, final String num) {
    	if (TextUtils.isEmpty(num.trim())) {
			Toast.Short(context, R.string.no_phone_data);
			return;
		}
    	final String[] items = num.split(",");
    	switch (items.length) {
    	case 0:
    		Toast.Short(context, R.string.no_phone_data);
    		break;
		case 1:
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setTitle(R.string.util_toast_dialornot);
			builder.setMessage(num);
			builder.setPositiveButton(R.string.util_text_yes, new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {

					Uri uri = Uri.parse("tel:" + num);
					Intent intent = new Intent(Intent.ACTION_CALL, uri);
//					context.startActivity(intent);

				}
			});
			builder.setNegativeButton(R.string.util_text_no, null);
			builder.create().show();
			break;

		default:
			int checkedItem = 0;
			OnClickListener listener = new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					final String lnum = items[which];
					L.v(true, this, "%s is chosen", lnum);
					AlertDialog.Builder builder = new AlertDialog.Builder(context);
					builder.setTitle(R.string.util_toast_dialornot);
					builder.setMessage(lnum);
					builder.setPositiveButton(R.string.util_text_yes, new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							
							Uri uri = Uri.parse("tel:" + lnum);
							Intent intent = new Intent(Intent.ACTION_CALL, uri);
//							context.startActivity(intent);
							
						}
					});
					builder.setNegativeButton(R.string.util_text_no, null);
					builder.create().show();
				}
			};
			new AlertDialog.Builder(context)
			.setTitle(R.string.util_toast_select_phone)
//    	.setIcon(R.drawable.icon1)
			.setSingleChoiceItems(items, checkedItem, listener)
			.setNegativeButton(R.string.util_text_cancel, null)
//			.setView(view);
			.show();
			break;
		}
    }
	/**
	 * 检查号码合法性结果
	 * @param phone
	 * @param code
	 * @param countryRules
	 * @return
	 */
    public static boolean isPhoneNumLegitimate(Context context ,String phone, String code, HashMap<String,String> countryRules) {
		if (code.startsWith("+")) {
			code = code.substring(1);
		}

		if(TextUtils.isEmpty(phone)) {
//			Toast.makeText(getActivity(), R.string.abs__action_bar_home_description, Toast.LENGTH_SHORT).show();
			L.w(null, "phone 为空");
			Toast.Short(context, R.string.util_toast_com_toast_enter_telephone);
			return false;
		}

		String rule = countryRules.get(code);
		Pattern p = Pattern.compile(rule);
		Matcher m = p.matcher(phone);
		return (!m.matches())?false:true;
	}
	public static String getContactNameByPhone(Context context,String phone){
    	ContentResolver cr = context.getContentResolver();
    	// get the id
    	Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,
    			ContactsContract.CommonDataKinds.Phone.NUMBER +" = ?",//TODO 在联系人名字的查询中如何添加areacode的并列查询条件
    			new String[]{phone}, null);
    	String id = null;
    	if (pCur.getCount() > 0) {
    		while (pCur.moveToNext()) {
    			id = pCur.getString(
    					pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
    		}
    	}
    	pCur.close();
    	
    	if (id!= null) {
    		Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
    				null, ContactsContract.Contacts._ID +" = ?", new String[]{id}, null);
    		String name = null;
    		if (cur.getCount() > 0) {
    			while (cur.moveToNext()) {
    				name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
    			}
    		}
    		cur.close();
    		return name;
		} else {
			return null;
		}
    }
	
	public static void CheckMobileNumber(Context context) {
		getMobileNumberList(context);
//		String mobileNumberList = PhoneHelper.getMobileNumberList();//手机号码集合 每个号码以英文,隔开
//		HttpReqSet_1User.CheckMobileNumber.start(context, iflog, mobileNumberList, onReceiveHttpResultSuccListener);
	}
    
	/**
	 * 查询手机本地联系人数据库以获取对应号码的本地姓名
	 * @param context
	 * @return
	 */
    private static ArrayList<String> getMobileNumberList(Context context) {
    	ContentResolver cr = context.getContentResolver();
    	Cursor pCursor = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.TYPE +
    			"=" + ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE, null, null);
    	ArrayList<String> numberList = new ArrayList<String>();
    	if (pCursor.getCount() > 0) {
			while (pCursor.moveToNext()) {
				String number = (String) pCursor.getString(pCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
				numberList.add(number);
			}
		}
    	//查询所有联系人
    	//获得每个联系人号码，插入list中
    	return numberList;
    }
	/**
	 * 获取所有数据
	 * 
	 * @return
	 */
	public static Map<String, ArrayList<String>> getAllCallRecords(Context context) {
		Map<String, ArrayList<String>> temp = new HashMap<String, ArrayList<String>>();
		Cursor c = context.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null,
				ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC");
		if (c.moveToFirst()) {
			do {
				// 获得联系人的ID号
				String contactId = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
				// 获得联系人姓名
				String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
				// 查看该联系人有多少个电话号码。如果没有这返回值为0
				int phoneCount = c.getInt(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
				ArrayList<String> phoneList = new ArrayList<String>();
				if (phoneCount > 0) {
					// 获得联系人的电话号码
					Cursor phones = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
							ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
					if (phones.moveToFirst()) {
						do {
							String number = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
							phoneList.add(number);
						} while (phones.moveToNext());
					}
					phones.close();
				}
				temp.put(name, phoneList);
			} while (c.moveToNext());
		}
		c.close();
		return temp;
	}
	

}
