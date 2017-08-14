package com.gta.zssx.mobileOA.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gta.zssx.R;
import com.gta.zssx.mobileOA.model.bean.Schedule;
import com.gta.zssx.mobileOA.view.adapter.ui.SwipeLayout;
import com.gta.zssx.pub.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lan.zheng on 2016/10/20.
 */
public class SchedulePlanAdapter extends BaseSwipeAdapter {
//    private SchedulePlanActivity mActivity;
    private ArrayList<Schedule> list;
    private Context mContext;
    private ScheduleOperationListener mListener;
    private List<SwipeLayout> swipeLayoutList;

    /*public SchedulePlanAdapter(SchedulePlanActivity activity, ArrayList<Schedule> list) {
        super();
        this.mActivity=activity;
        setData(list);
    }*/

    public SchedulePlanAdapter(Context context,ScheduleOperationListener scheduleOperationListener,ArrayList<Schedule> list){
        super();
        this.mContext = context;
        mListener = scheduleOperationListener;
        swipeLayoutList = new ArrayList<>();
        setData(list);
    }

    public void setData(ArrayList<Schedule> schedules) {
        if (null != schedules) {
            this.list = schedules;
        } else {
            this.list = new ArrayList<Schedule>();
        }
    }

    public void clearOpenSwipeLayout(){
        LogUtil.Log("lenita","swipeLayoutList.size() = "+swipeLayoutList.size());
       if(swipeLayoutList.size() > 0){
           for(int i = 0;i < swipeLayoutList.size();i++){
               if(swipeLayoutList.get(i).getOpenStatus() == SwipeLayout.Status.Open){
                   swipeLayoutList.get(i).close();
                   notifyDataSetChanged();
                   swipeLayoutList.remove(i);
               }
           }
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
    public int getSwipeLayoutResourceId(int position) {
        return R.id.schedule_swipe;
    }

    @Override
    public View generateView(int position, View convertView, ViewGroup parent) {
        View view = null;

        view = ((Activity)mContext).getLayoutInflater().inflate(R.layout.item_schedule_listview, parent, false);
        assert view != null;
        final ViewHolder holder = new ViewHolder();
        holder.dateTV=(TextView) view.findViewById(R.id.schedule_lv_date_tv);
        holder.finishTV=(TextView) view.findViewById(R.id.schedule_lv_finish_tv);
        holder.contentTV=(TextView) view.findViewById(R.id.schedule_lv_content_tv);
        holder.containerSL=(SwipeLayout) view.findViewById(R.id.schedule_swipe);
//        holder.editLL=(LinearLayout) view.findViewById(R.id.schedule_lv_edit_ll);
//        holder.deleteLL=(LinearLayout) view.findViewById(R.id.schedule_lv_delete_ll);
        view.setTag(holder);
        return view;

    }

    private void bindView(final ViewHolder holder,  final int position) {
        final Schedule schedule=list.get(position);
        holder.dateTV.setText(makeDate(schedule.getStartTime(),	 schedule.getEndTime()));

        holder.finishTV.setText(schedule.getStatus()==0?"未完成":"已完成");

        holder.contentTV.setText(schedule.getScheduleContent());


        holder.finishTV.setOnClickListener(new View.OnClickListener() {
            //更新日程状态
            @Override
            public void onClick(View v) {
                if (schedule.getStatus() == 1) {
                    mListener.updateStatusSchedule(schedule.getId(), 0);
//                    mActivity.updateScheduleStatus(schedule.getId(), 0);
//					schedule.setStatus(0);
//					holder.finishTV.setText("未完成");
                }else if(schedule.getStatus() == 0){
                    mListener.updateStatusSchedule(schedule.getId(), 1);
//                    mActivity.updateScheduleStatus(schedule.getId(), 1);
//					schedule.setStatus(1);
//					holder.finishTV.setText("已完成");
                }

            }
        });
        /*holder.editLL.setOnClickListener(new View.OnClickListener() {
            //编辑日程
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mActivity,NewScheduleActivity.class);
                if (schedule.getRemind() == 5||schedule.getRemind() ==1 ) {
                    intent.putExtra("ScheduleId", schedule.getId());
                }else {
                    intent.putExtra("Schedule", schedule);
                }
                mActivity.startActivity(intent);
            }
        });*/
        /*holder.deleteLL.setOnClickListener(new View.OnClickListener() {
            //删除日程
            @Override
            public void onClick(View v) {
                mListener.deleteSchedule(schedule.getId(),holder.containerSL);
//                mActivity.deleteSchedule(schedule.getId());  //删除
            }
        });

        // 双击的回调函数
        holder.containerSL.setOnDoubleClickListener(new SwipeLayout.DoubleClickListener() {
            @Override
            public void onDoubleClick(SwipeLayout layout, boolean surface) {
                if (holder.containerSL.getOpenStatus() == SwipeLayout.Status.Open) {
                    holder.containerSL.close();
                    notifyDataSetChanged();
                }
            }
        });*/

        //滑动要进入编辑日程
        holder.contentTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO 跳转,疑惑，为什么remind是5或1的时候要重新获取服务器？？？？
                Bundle bundle = new Bundle();
                if (schedule.getRemind() == 5||schedule.getRemind() ==1 ) {
                    bundle.putInt("ScheduleId", schedule.getId());
                }else {
                    bundle.putSerializable("Schedule", schedule);
                }
                mListener.editSchedule(bundle,holder.containerSL);
            }
        });

        /*holder.contentTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.containerSL.getOpenStatus() == SwipeLayout.Status.Open) {
                    holder.containerSL.close();
                    notifyDataSetChanged();
                }

            }
        });*/

        if (holder.containerSL.getOpenStatus() == SwipeLayout.Status.Open) {
            holder.containerSL.close();
            notifyDataSetChanged();
        }

    }

    public void hideSwipe( SwipeLayout swipeLayout){
        if (swipeLayout.getOpenStatus() == SwipeLayout.Status.Open) {
            swipeLayout.close();
            notifyDataSetChanged();
        }
    }

    private String makeDate(String startTime, String endTime){
        String result="";
        String[] arrs=startTime.split("T");
        String[] arre=endTime.split("T");
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
    // 对控件的填值操作独立出来了，我们可以在这个方法里面进行item的数据赋值
    @Override
    public void fillValues(int position, View convertView) {
        ViewHolder holder=(ViewHolder) convertView.getTag();
        bindView(holder, position);
    }

    class ViewHolder{
        TextView dateTV;//该天几点到几点，日程的时间
        TextView finishTV;//点击切换 未完成/已完成
        TextView contentTV;//日程的内容
        SwipeLayout containerSL;
//        LinearLayout editLL;
//        LinearLayout deleteLL;
    }

    public interface ScheduleOperationListener{
        void deleteSchedule(int id,SwipeLayout swipeLayout);
        void updateStatusSchedule(int id,int status);
        void editSchedule(Bundle bundle,SwipeLayout swipeLayout);
    }
}