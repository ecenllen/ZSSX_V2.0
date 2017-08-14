package com.gta.zssx.pub.widget;

import android.content.Context;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;


/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/7/22.
 * @since 1.0.0
 */
public class DisableEmojiEditText extends EditText {

    private int mMaxLength = Integer.MAX_VALUE;
    private int mMaxLine = Integer.MAX_VALUE;

    public DisableEmojiEditText(Context context) {
        super(context);
        init();
    }

    public DisableEmojiEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DisableEmojiEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setFilters(new InputFilter[]{new EmojiExcludeFilter(), new InputFilter.LengthFilter(mMaxLength), new MaxLinesInputFilter(mMaxLine)});
    }

    /**
     * set max length of contents
     */
    public void setMaxLength(int maxLength) {
        this.mMaxLength = maxLength;
        init();
    }

    /**
     * set max length of contents
     */
    public void setMaxLine(int maxLine) {
        this.mMaxLine = maxLine;
        init();
    }

    private class EmojiExcludeFilter implements InputFilter {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            for (int i = start; i < end; i++) {
                int type = Character.getType(source.charAt(i));
                if (type == Character.SURROGATE || type == Character.OTHER_SYMBOL) {
                    return "";
                }
            }
            return null;
        }
    }

    private class MaxLinesInputFilter implements InputFilter {

        private final int mMax;

        public MaxLinesInputFilter(int max) {
            mMax = max;
        }

        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            int newLinesToBeAdded = countOccurrences(source.toString(), '\n');
            int newLinesBefore = countOccurrences(dest.toString(), '\n');
            if (newLinesBefore >= mMax - 1 && newLinesToBeAdded > 0) {
                // filter
                return "";
            }

            // do nothing
            return null;
        }

        /**
         * @return the maximum lines enforced by this input filter
         */
        public int getMax() {
            return mMax;
        }

        /**
         * Counts the number occurrences of the given char.
         *
         * @param string         the string
         * @param charAppearance the char
         * @return number of occurrences of the char
         */
        public int countOccurrences(String string, char charAppearance) {
            int count = 0;
            for (int i = 0; i < string.length(); i++) {
                if (string.charAt(i) == charAppearance) {
                    count++;
                }
            }
            return count;
        }
    }

    //防止在scrollview中和滑动冲突
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_MOVE:
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_UP:
                getParent().requestDisallowInterceptTouchEvent(false);
                break;
        }

        return super.onTouchEvent(event);
    }
}