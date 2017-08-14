package com.gta.zssx.fun.coursedaily.registerrecord.view;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.gta.zssx.R;
import com.gta.zssx.fun.coursedaily.registerrecord.model.DTO.ClassInfoDto;
import com.gta.zssx.fun.coursedaily.registerrecord.model.DTO.DetailRegisteredRecordDto;
import com.gta.zssx.fun.coursedaily.registerrecord.presenter.DetailRegisteredRecordPresenter;
import com.gta.zssx.fun.coursedaily.registerrecord.view.adapter.CheckExpandableListAdapter;
import com.gta.zssx.fun.coursedaily.registerrecord.view.base.RegisterRecordBaseFragment;
import com.gta.zssx.fun.coursedaily.registerrecord.view.base.RegisterRecordFragmentBuilder;
import com.gta.zssx.pub.util.LogUtil;

/**
 * Created by lan.zheng on 2016/6/17.
 */
@Deprecated
public class DetailRegisteredRecordFragment extends RegisterRecordBaseFragment<DetailRegisteredRecordView, DetailRegisteredRecordPresenter>
        implements DetailRegisteredRecordView {
    public static final String PAGE_TAG = DetailRegisteredRecordFragment.class.getSimpleName();
    public static String CLASS_INFO_DTO;
    private ClassInfoDto mClassInfoDto;
    private String ClassName;

    private TextView mCourseNameTextView;
    private TextView mDeptNameTextView;
    private TextView mTeacherNameTextView;
    private TextView mStudentCountTextView;
    private TextView mAttendCountTextView;
    private TextView mCourseTeacherTextView;
    private TextView mRemarkTextView;

    private MyExpandListView expandableListView;
//    private ExpandableListView expandableListView;
//    private ExpandableListAdapter mExpandableListAdapter;
    private CheckExpandableListAdapter mExpandableListAdapter;
    String[] lateString;
    String[] leaveString;
    String[] truantString;
    String[] holidayString;

    @Override
    public void showResultList(DetailRegisteredRecordDto detailRegisteredRecordDto) {
        formatCount(detailRegisteredRecordDto);
        updateView(detailRegisteredRecordDto);
        updateExpandableList();
    }

    @Override
    public void updateExpandableList() {
        updateList();
    }


    /**
     * 返回的数据进行分出迟到人数，请假人数，旷课人数和公假人数列表处理
     * @param detailRegisteredRecordDto
     */
    private void formatCount(DetailRegisteredRecordDto detailRegisteredRecordDto){
        if(detailRegisteredRecordDto.getLateList() != null){
            int count = detailRegisteredRecordDto.getLateList().size();
            lateString = new String[count];
            for(int i=0;i<count;i++){
                lateString[i] = detailRegisteredRecordDto.getLateList().get(i).getStudentName()+" ("
                        +detailRegisteredRecordDto.getLateList().get(i).getStudentNo()+")";
            }
        }else
            LogUtil.Log("lenita","lateString length = "+lateString.length);
        //=================================
        if(detailRegisteredRecordDto.getLeaveList() != null){
            int count = detailRegisteredRecordDto.getLeaveList().size();
            leaveString = new String[count];
            for(int i=0;i<count;i++){
                leaveString[i] = detailRegisteredRecordDto.getLeaveList().get(i).getStudentName()+" ("
                        +detailRegisteredRecordDto.getLeaveList().get(i).getStudentNo()+")";
            }
        }else
            LogUtil.Log("lenita","lateString length = "+leaveString.length);
        //=================================
        if(detailRegisteredRecordDto.getTruantList() != null){
            int count = detailRegisteredRecordDto.getTruantList().size();
            truantString = new String[count];
            for(int i=0;i<count;i++){
                truantString[i] = detailRegisteredRecordDto.getTruantList().get(i).getStudentName()+" ("
                        +detailRegisteredRecordDto.getTruantList().get(i).getStudentNo()+")";
            }
        }else
            LogUtil.Log("lenita","lateString length = "+truantString.length);
        //===================================
        if(detailRegisteredRecordDto.getHolidayList() != null){
            int count = detailRegisteredRecordDto.getHolidayList().size();
            holidayString = new String[count];
            for(int i=0;i<count;i++){
                holidayString[i] = detailRegisteredRecordDto.getHolidayList().get(i).getStudentName()+"   ("
                        +detailRegisteredRecordDto.getHolidayList().get(i).getStudentNo()+")";
            }
        }else
            LogUtil.Log("lenita","lateString length = "+holidayString.length);
    }


    private void updateView(DetailRegisteredRecordDto detailRegisteredRecordDto){
        //根据获取到的信息来更新View
        if(detailRegisteredRecordDto.getCourseName()!=null && !detailRegisteredRecordDto.getCourseName().equals("")){
            mCourseNameTextView.setText(detailRegisteredRecordDto.getCourseName().get(0).getCourseName());
        }
        if(detailRegisteredRecordDto.getDeptName()!=null && !detailRegisteredRecordDto.getDeptName().equals("")){
            mDeptNameTextView.setText(detailRegisteredRecordDto.getDeptName());
        }
        if(detailRegisteredRecordDto.getTeacherName()!=null && !detailRegisteredRecordDto.getTeacherName().equals("")){
            mTeacherNameTextView.setText(detailRegisteredRecordDto.getTeacherName());
        }
        if(detailRegisteredRecordDto.getCourseTeacher()!=null && !detailRegisteredRecordDto.getCourseTeacher().equals("")){
            mCourseTeacherTextView.setText(detailRegisteredRecordDto.getCourseTeacher().get(0).getTeacherName());
        }
        if(String.valueOf(detailRegisteredRecordDto.getStudentCount())!= null){
            mStudentCountTextView.setText(""+detailRegisteredRecordDto.getStudentCount());
        }
        if(String.valueOf(detailRegisteredRecordDto.getAttendCount())!= null){
            mAttendCountTextView.setText(""+detailRegisteredRecordDto.getAttendCount());
        }
        if(detailRegisteredRecordDto.getMemo()!=null && !detailRegisteredRecordDto.getMemo().equals("")){
            mRemarkTextView.setText(detailRegisteredRecordDto.getMemo());
        }
    }

    private void updateList(){
        LogUtil.Log("lenita","updateList()");
        mExpandableListAdapter = null;
        mExpandableListAdapter = new CheckExpandableListAdapter(mActivity,lateString,leaveString,truantString,holidayString);
        expandableListView.setAdapter(mExpandableListAdapter);
        //默认为全部展开
        int groupCount = expandableListView.getCount();
        for (int i=0; i<groupCount; i++) {
            expandableListView.expandGroup(i);
        }
        //进行高度计算,为了备注的显示
        ViewGroup.LayoutParams lLayoutParams = expandableListView.getLayoutParams();
        lLayoutParams.height = getHeight();
        expandableListView.setLayoutParams(lLayoutParams);
        expandableListView.requestLayout();

        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
//                LogUtil.Log("lenita"," onGroupClick");
                mExpandableListAdapter.updateChecked(groupPosition);
//                CheckExpandableListAdapter.group_checked[groupPosition] = CheckExpandableListAdapter.group_checked[groupPosition]+1;
                // 刷新界面
                mExpandableListAdapter.notifyDataSetChanged();
                return false;
            }
        });
    }

    /**
     * 进行高度计算,为了备注的显示
     * @return
     */
    private int getHeight(){
        int totalHeight = 0;
        int deliverCount= 0;
        for (int i = 0; i < mExpandableListAdapter.getGroupCount(); i++) { // listAdapter.getCount()返回数据项的数目
            View listgroupItem = mExpandableListAdapter .getGroupView(i, true, null, expandableListView );
            listgroupItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listgroupItem .getMeasuredHeight(); // 统计所有子项的总高度
            System. out.println("height : group" +i +"次" +totalHeight );
            for (int j = 0; j < mExpandableListAdapter.getChildrenCount( i); j++) {
                View listchildItem = mExpandableListAdapter .getChildView(i, j, false , null, expandableListView);
                listchildItem.measure(0, 0); // 计算子项View 的宽高
                totalHeight += listchildItem.getMeasuredHeight(); // 统计所有子项的总高度
                deliverCount++;
            }
        }
        totalHeight = totalHeight+(expandableListView.getDividerHeight() * (mExpandableListAdapter.getGroupCount() + deliverCount- 1));
        return totalHeight;
    }

    public static class Builder extends RegisterRecordFragmentBuilder<DetailRegisteredRecordFragment> {
        ClassInfoDto mClassInfoDto;

        public Builder(Context context, ClassInfoDto classInfoDto) {
            super(context);
            CLASS_INFO_DTO = "CLASS_INFO_DTO";
            mClassInfoDto = classInfoDto;
        }

        @Override
        public DetailRegisteredRecordFragment create() {
            Bundle lBundle = new Bundle();
            lBundle.putSerializable(CLASS_INFO_DTO, mClassInfoDto);
            DetailRegisteredRecordFragment lDetailRegisteredRecordFragment = new DetailRegisteredRecordFragment();
            lDetailRegisteredRecordFragment.setArguments(lBundle);
            return lDetailRegisteredRecordFragment;
        }

        @Override
        public String getPageTag() {
            return PAGE_TAG;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail_registered_record, container, false);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        getData();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.attachView(this);
        uiAction();
    }

    private void getData(){
        //初始化数组
        lateString = new String[0];
        leaveString = new String[0];
        truantString = new String[0];
        holidayString = new String[0];
        //获取数据
        mClassInfoDto = (ClassInfoDto)getArguments().getSerializable(CLASS_INFO_DTO);
        ClassName = mClassInfoDto.getClassName();
    }

    private void uiAction(){

        initView();
        getRecordDetail();
        //测试数据
//        formatCount(testData());
//        updateView(testData());
    }

    private void getRecordDetail(){
        presenter.getRegisteredRecordDetailData(mClassInfoDto);
    }


    private void initView(){
        mCourseNameTextView = (TextView)mActivity.findViewById(R.id.tv_course_name_show);
        mDeptNameTextView = (TextView)mActivity.findViewById(R.id.tv_dpat_show);
        mTeacherNameTextView = (TextView)mActivity.findViewById(R.id.tv_teacher_name_show);
        mStudentCountTextView= (TextView)mActivity.findViewById(R.id.tv_class_person_num_show);
        mAttendCountTextView = (TextView)mActivity.findViewById(R.id.tv_class_actual_come_show);
        mCourseTeacherTextView = (TextView)mActivity.findViewById(R.id.tv_text_class_teacher);
        mRemarkTextView = (TextView)mActivity.findViewById(R.id.tv_class_remark_show);

        expandableListView = (MyExpandListView) mActivity.findViewById(R.id.expanded_menu);
        // 设置默认图标为不显示状态
        expandableListView.setGroupIndicator(null);

        mToolBarManager
                .showBack(true)
                .showLeftImage(false)
                .setTitle(ClassName)
                .showRightButton(false)
                .setRightText("");
    }

    @NonNull
    @Override
    public DetailRegisteredRecordPresenter createPresenter() {
        return new DetailRegisteredRecordPresenter();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        presenter.detachView(false);
    }

    /*private DetailRegisteredRecordDto testData(){
        DetailRegisteredRecordDto lDto = new DetailRegisteredRecordDto();
        lDto.setClassName("统计学");
        lDto.setDeptName("财经部");
        lDto.setTeacherName("哈哈哈");
        lDto.setStudentCount(30);
        lDto.setAttendCount(28);

        DetailRegisteredRecordDto.LateListBean lLateBean = new DetailRegisteredRecordDto.LateListBean();
        DetailRegisteredRecordDto.LateListBean lLateBean2 = new DetailRegisteredRecordDto.LateListBean();
        DetailRegisteredRecordDto.LateListBean lLateBean3 = new DetailRegisteredRecordDto.LateListBean();
        DetailRegisteredRecordDto.LateListBean lLateBean4 = new DetailRegisteredRecordDto.LateListBean();
        lLateBean.setStudentName("测试");
        lLateBean.setStudentNo("3100717131");
        lLateBean2.setStudentName("测试");
        lLateBean2.setStudentNo("3100717131");
        lLateBean3.setStudentName("测试");
        lLateBean3.setStudentNo("3100717131");
        lLateBean4.setStudentName("测试");
        lLateBean4.setStudentNo("3100717131");
        List<DetailRegisteredRecordDto.LateListBean> lLateListBeen = new ArrayList<>();
        lLateListBeen.add(lLateBean);
        lLateListBeen.add(lLateBean2);
        lLateListBeen.add(lLateBean3);
        lLateListBeen.add(lLateBean4);

        DetailRegisteredRecordDto.LeaveListBean lLeaveBean = new DetailRegisteredRecordDto.LeaveListBean();
        DetailRegisteredRecordDto.LeaveListBean lLeaveBean2 = new DetailRegisteredRecordDto.LeaveListBean();
        DetailRegisteredRecordDto.LeaveListBean lLeaveBean3 = new DetailRegisteredRecordDto.LeaveListBean();
        DetailRegisteredRecordDto.LeaveListBean lLeaveBean4 = new DetailRegisteredRecordDto.LeaveListBean();
        lLeaveBean.setStudentName("测试");
        lLeaveBean.setStudentNo("3100222222");
        lLeaveBean2.setStudentName("测试");
        lLeaveBean2.setStudentNo("3100222222");
        lLeaveBean3.setStudentName("测试");
        lLeaveBean3.setStudentNo("3100222222");
        lLeaveBean4.setStudentName("测试");
        lLeaveBean4.setStudentNo("3100222222");
        List<DetailRegisteredRecordDto.LeaveListBean> lLeaveListBeen = new ArrayList<>();
        lLeaveListBeen.add(lLeaveBean);
        lLeaveListBeen.add(lLeaveBean2);
        lLeaveListBeen.add(lLeaveBean3);
        lLeaveListBeen.add(lLeaveBean4);

        lDto.setLateList(lLateListBeen);
        lDto.setLeaveList(lLeaveListBeen);

        return lDto;
    }*/

    /*class ExpandableListAdapter extends BaseExpandableListAdapter {
        private String[] group_title_array = new String[] { "迟到人数", "请假人数","旷课人数","公假人数" };
        private List<String> groupArray;  //title
        private List<String> groupCount;  //是否有箭头
        private List<List<String>> childArray;  //child
        Context mContext;
        // 一级标签上的状态图片数据源
        int[] group_state_array = new int[] { R.drawable.group_down,
                R.drawable.group_up };

        public ExpandableListAdapter(Context context,String[] late,String[] leave,String[] truant,String[] holiday){
            mContext = context;

            groupCount = new ArrayList<>();
            groupCount.add(""+late.length);
            groupCount.add(""+leave.length);
            groupCount.add(""+truant.length);
            groupCount.add(""+holiday.length);

            groupArray = new ArrayList<>();
            for(int index=0;index<group_title_array.length;index++){
                groupArray.add(group_title_array[index]);
            }

            childArray = new ArrayList<List<String>>();
            List<String> childItem1 =new ArrayList<>();
            for(int i=0;i<late.length;i++){
                childItem1.add(late[i]);}
            List<String> childItem2 =new ArrayList<>();
            for(int i=0;i<leave.length;i++){
                childItem2.add(leave[i]);}
            List<String> childItem3 =new ArrayList<>();
            for(int i=0;i<truant.length;i++){
                childItem3.add(truant[i]);}
            List<String> childItem4 =new ArrayList<>();
            for(int i=0;i<holiday.length;i++){
                childItem1.add(holiday[i]);}
            childArray.add(childItem1);
            childArray.add(childItem2);
            childArray.add(childItem3);
            childArray.add(childItem4);
            LogUtil.Log("lenita","childArray.size = "+childArray.size()+" "+groupArray.size());
        }

        @Override
        public int getGroupCount() {
            return groupArray.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return childArray.get(groupPosition).size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return getGroup(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return childArray.get(groupPosition).get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        *//**
         * 一级标签设置
         * @param groupPosition
         * @param isExpanded
         * @param convertView
         * @param parent
         * @return
         *//*
        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            // 为视图对象指定布局
            convertView = LinearLayout.inflate(mContext,
                    R.layout.list_item_expand_group, null);

            RelativeLayout myLayout = (RelativeLayout) convertView
                    .findViewById(R.id.group_layout);

            *//**
             * 声明视图上要显示的控件
             *//*
            // 新建一个TextView对象，用来显示一级标签上的标题信息
            TextView group_title = (TextView) convertView
                    .findViewById(R.id.group_title);
            // 新建一个TextView对象，用来显示一级标签上的大体描述的信息
            ImageView group_state = (ImageView) convertView
                    .findViewById(R.id.group_state);
            TextView group_count = (TextView)convertView.findViewById(R.id.group_num) ;
            *//**
             * 设置相应控件的内容
             *//*
            // 设置标题上的文本信息
            group_title.setText(groupArray.get(groupPosition));

            // 设置整体描述上的文本信息
            if (group_checked[groupPosition] % 2 == 1) {
                // 设置默认的图片是选中状态
                group_state.setBackgroundResource(group_state_array[1]);
                myLayout.setBackgroundResource(R.drawable.text_item_bg);
            } else {
                for (int test : group_checked) {
                    if (test == 0 || test % 2 == 0) {

                        // 设置默认的图片是未选中状态
                        group_state.setBackgroundResource(group_state_array[0]);
                        myLayout.setBackgroundResource(R.drawable.text_item_top_bg);
                    }
                }
            }

            //是否显示箭头和改变count
            if(groupCount.get(groupPosition).equals("0")){
                group_state.setVisibility(View.INVISIBLE);
                myLayout.setBackgroundResource(R.drawable.text_item_bg);
            }else {
                group_count.setText(groupCount.get(groupPosition));
            }

            // 返回一个布局对象
            return convertView;
        }

        *//**
         * 对一级标签下的二级标签进行设置
         *//*
        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            // 为视图对象指定布局
            convertView =  LinearLayout.inflate(mContext, R.layout.list_item_expand_group_child, null);
            *//**
             * 声明视图上要显示的控件
             *//*
            // 新建一个TextView对象，用来显示具体内容
            TextView child_text = (TextView) convertView
                    .findViewById(R.id.child_text);
            *//**
             * 设置相应控件的内容
             *//*
            // 设置要显示的文本信息
            child_text.setText(childArray.get(groupPosition).get(childPosition));
            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }*/

   /* public class MyExpandableListView extends ExpandableListView {

        public MyExpandableListView(Context context, AttributeSet attrs) {
            super(context, attrs);
            // TODO Auto-generated constructor stub
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            // TODO Auto-generated method stub
            int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,

                    MeasureSpec.AT_MOST);

            super.onMeasure(widthMeasureSpec, expandSpec);
        }
    }*/
}
