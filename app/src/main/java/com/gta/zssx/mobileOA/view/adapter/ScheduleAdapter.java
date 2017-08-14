package com.gta.zssx.mobileOA.view.adapter;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baoyz.swipemenulistview.BaseSwipListAdapter;
import com.gta.utils.resource.Toast;
import com.gta.zssx.R;
import com.gta.zssx.mobileOA.model.bean.Schedule;
import com.gta.zssx.mobileOA.view.page.SchedulePlanActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 日程的适配器
 * 
 * @author bin.wang1
 * 
 */
public class ScheduleAdapter extends BaseSwipListAdapter {
	private SchedulePlanActivity mActivity;
	private List<Schedule> list;
	private Context mContext;
	private ScheduleOperationListener mListener;

    /*public ScheduleAdapter(SchedulePlanActivity activity,ScheduleOperationListener scheduleOperationListener, ArrayList<Schedule> list) {
        super();
        this.mActivity=activity;
		mListener = scheduleOperationListener;
        setData(list);
    }*/

	public ScheduleAdapter(Context context,ScheduleOperationListener scheduleOperationListener,ArrayList<Schedule> list){
		super();
		this.mContext = context;
		mListener = scheduleOperationListener;
		setData(list);
	}

	public List<Schedule> getSchedule(){
		return list;
	}


	public void setData(List<Schedule> schedules) {
		if (null != schedules) {
			this.list = schedules;
		} else {
			this.list = new ArrayList<Schedule>();
		}
	}

	@Override
	public int getCount() {
		return list == null ? 0 : list.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}
	/**
	 * 返回该日程的ID
	 */
	@Override
	public long getItemId(int position) {
		return position;
	}


	@Override
	public View getView(final int position, View convertView, final ViewGroup parent) {
		if (convertView == null) {
			convertView = View.inflate(mContext,
					R.layout.list_item_today_schedule, null);
			new ViewHolder(convertView);
		}
		ViewHolder holder = (ViewHolder) convertView.getTag();
		Schedule schedule = list.get(position);
		holder.dateTV.setText(makeDate(schedule.getStartTime(),	 schedule.getEndTime()));

		holder.finishTV.setText(schedule.getStatus()==0?"未完成":"已完成");
		if(schedule.getStatus()==0){
			holder.finishTV.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.schedule_state_selector));
		}else {
			holder.finishTV.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.schedule_state_btn2));
		}

		holder.contentTV.setText(schedule.getScheduleContent());

        //更新日程状态
		holder.finishTV.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
                if (schedule.getStatus() == 1) {
					Toast.Short(mContext,"您已完成该日程");
                    return;
                }
				mListener.updateStatusSchedule(schedule.getId(), 1);

			}
		});
		//点击要进入编辑日程
		holder.contentTV.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//remind是5或1的时候要重新获取服务器--1:单次提醒，5：不提醒
                Bundle bundle = new Bundle();
				int status = schedule.getStatus();
				bundle.putInt("Status",status );
                if (schedule.getRemind() == 5 || schedule.getRemind() ==1 ) {
                    bundle.putInt("ScheduleId", schedule.getId());
                }else {
					if(status == 1){
						//提醒是2,3,4,6且状态为已完成时，需要传Id
						bundle.putInt("ScheduleId", schedule.getId());
					}
                    bundle.putSerializable("Schedule", schedule);
                }
                mListener.editSchedule(bundle);
			}
		});
		return convertView;
	}

	private String makeDate(String startTime, String endTime){
		String result="";
		String[] arrs=startTime.split(" ");
		String[] arre=endTime.split(" ");
		String s=arrs[1];
		String e=arre[1];
		String[] strs=s.split(":");
		String[] stre=e.split(":");

		if (strs.length==2 && stre.length ==2) {
			result=s+"--"+e;
		}else if (strs.length==3 && stre.length==3) {
			result=strs[0]+":"+strs[1]+"--"+stre[0]+":"+stre[1];
		}
		return result;
	}

	class ViewHolder{
		TextView dateTV;//该天几点到几点，日程的时间
		TextView finishTV;//点击切换 未完成/已完成
		TextView contentTV;//日程的内容

		public ViewHolder(View view) {
			dateTV = (TextView) view.findViewById(R.id.schedule_lv_date_tv);
			finishTV = (TextView) view.findViewById(R.id.schedule_lv_finish_tv);
			contentTV = (TextView)view.findViewById(R.id.schedule_lv_content_tv);
			view.setTag(this);
		}
//        SwipeLayout containerSL;
//        LinearLayout editLL;
//        LinearLayout deleteLL;
	}

	public interface ScheduleOperationListener{
		//        void deleteSchedule(int id,SwipeLayout swipeLayout);
		void updateStatusSchedule(int id,int status);
        void editSchedule(Bundle bundle);
	}
}
