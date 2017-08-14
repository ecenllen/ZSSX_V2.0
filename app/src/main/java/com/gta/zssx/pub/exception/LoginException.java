package com.gta.zssx.pub.exception;

import android.widget.Toast;

import com.gta.zssx.ZSSXApplication;

/**
 * Created by liang.lu on 2016/11/18 16:39.
 */

public class LoginException extends RuntimeException {

    public LoginException(String message) {
        Toast.makeText(ZSSXApplication.instance, message, Toast.LENGTH_SHORT).show();
    }

}
