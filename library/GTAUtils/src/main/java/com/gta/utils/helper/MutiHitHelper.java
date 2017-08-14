/**
 *
 * @author Woode Wang E-mail:wangwoode@qq.com
 * @version 创建时间：2015-2-13 下午4:41:05
 */
package com.gta.utils.helper;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @description 多次点击的计算监听帮助类<br>
 *              默认是最大2次点击，每次1500毫秒
 * @author
 * @since 1.0.0
 */
public class MutiHitHelper {
	public interface OnMutiHitListener {
		void onMaxHit();

		void onHitDoing(int indexOfHit);
	}

	private int max_hit = 1;
	private int mHitTag = 0;
	private long timePerHit = 1500;
	private OnMutiHitListener mOnMutiHitListener;

	public void setTimePerHit(long timePerHit) {
		this.timePerHit = timePerHit;
	}

	public void setMax_hit(int max_hit) {
		this.max_hit = max_hit;
	}

	public MutiHitHelper(OnMutiHitListener mOnMutiHitListener) {
		super();
		this.mOnMutiHitListener = mOnMutiHitListener;
	}

	public void Hit() {
		if (mHitTag < max_hit) {
			mOnMutiHitListener.onHitDoing(mHitTag);
			Timer timer = new Timer();
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					mHitTag = 0;
				}
			}, timePerHit * max_hit);
			mHitTag = mHitTag + 1;
		} else {
			mOnMutiHitListener.onMaxHit();
		}
	}
	
	public void resetHitTag() {
		mHitTag = 0;
	}
}
