package com.gta.zssx.fun.adjustCourse.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RadioGroup;

import java.util.List;


/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/11/2.
 * @since 1.0.0
 */
public class CustomRadioGroup<T> extends RadioGroup {

    List<T> mData;

    public CustomRadioGroup(Context context) {
        super(context);
    }

    public CustomRadioGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


}
