package com.gta.zssx.patrolclass.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gta.utils.fragment.BaseFragment;
import com.gta.zssx.R;
import com.gta.zssx.patrolclass.model.entity.PatrolRegisterBeanNew;
import com.gta.zssx.patrolclass.view.adapter.AutoPatrolPointsDetailsFragmentAdapter;
import com.gta.zssx.patrolclass.view.page.DockScoreNewActivity;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.gta.zssx.patrolclass.view.page.DockScoreNewActivity.OPTION_BEANS;

/**
 * Created by liang.lu on 2017/3/1 17:11.
 */

public class PatrolPointsDetailsFragment extends BaseFragment implements View.OnClickListener {

    public static final String TEACHER_BEAN = "teacher_bean";
    public static final int REQUEST_CODE_DOCK_SCORE = 100;
    public static final String POSITION = "position";
    public static final String STATUS = "status";
    /**
     * 自动计算情况下，使用列表
     */
    private RecyclerView mRecyclerView;
    private TextView mAutoScoreTv;
    private TextView editPoints;
    private TextView deleteItem;
    private ImageView mImageView;
    private DeleteItemListener listener;
    /*当前fragment的位置*/
    private int position;
    private boolean isClose;
    private List<PatrolRegisterBeanNew.TeacherListBean.DockScoreBean.OptionsBean> optionsBeanList;
    private ListChangedListener changedListener;
    /*总集合，为了在编辑页面 使用*/
    private ArrayList<PatrolRegisterBeanNew.TeacherListBean> teacherListBeans;
    /**
     * 用来区分是否扣分详情页面保存过来的数据
     */
    private int status = 0;

    public void setClose (boolean close) {
        isClose = close;
    }

    public void setPosition (int position) {
        this.position = position;
    }

    public void setTeacherListBeans (ArrayList<PatrolRegisterBeanNew.TeacherListBean> teacherListBeans) {
        this.teacherListBeans = teacherListBeans;
    }

    public void setChangedListener (ListChangedListener changedListener) {
        this.changedListener = changedListener;
    }

    public static PatrolPointsDetailsFragment newInstance (
            ArrayList<PatrolRegisterBeanNew.TeacherListBean> teacherListBeans, int position) {
        Bundle args = new Bundle ();
        args.putSerializable (TEACHER_BEAN, teacherListBeans);
        args.putInt (POSITION, position);
        PatrolPointsDetailsFragment pageFragment = new PatrolPointsDetailsFragment ();
        pageFragment.setArguments (args);
        return pageFragment;
    }

    public void setDeleteListener (DeleteItemListener listener) {
        this.listener = listener;
    }

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        if (getArguments () != null && teacherListBeans == null) {
            teacherListBeans = (ArrayList<PatrolRegisterBeanNew.TeacherListBean>) getArguments ()
                    .getSerializable (TEACHER_BEAN);
        }
        if (!isClose) {
            position = getArguments ().getInt (POSITION);
        }
    }

    @Nullable
    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        View view = inflater.inflate (R.layout.fragment_patrol_points_details, container, false);
        initView (view);
        setInteractListener ();
        return view;
    }

    private void initView (View view) {
        mAutoScoreTv = (TextView) view.findViewById (R.id.text_view_display_state);
        editPoints = (TextView) view.findViewById (R.id.text_view_patrol_register_edit);
        deleteItem = (TextView) view.findViewById (R.id.text_view_patrol_register_delete);
        mImageView = (ImageView) view.findViewById (R.id.image_view_score);

        mRecyclerView = (RecyclerView) view.findViewById (R.id.recyclerView);

        mRecyclerView.setLayoutManager (new LinearLayoutManager (getContext ()));
        mRecyclerView.setHasFixedSize (true);
    }

    private void setInteractListener () {
        deleteItem.setOnClickListener (this);
        editPoints.setOnClickListener (this);
    }

    @Override
    public void onViewCreated (View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated (view, savedInstanceState);
        setAutoScoreTvText ();
        createScoreTitle (teacherListBeans.get (position));
    }

    @Override
    public void onClick (View v) {
        switch (v.getId ()) {
            case R.id.text_view_patrol_register_edit:
                //点击编辑，跳至扣分详情编辑页面
                Intent intent = new Intent (getContext (), DockScoreNewActivity.class);
                Bundle bundle = new Bundle ();
                bundle.putSerializable (OPTION_BEANS, teacherListBeans);
                bundle.putInt (POSITION, position);
                intent.putExtras (bundle);

                startActivityForResult (intent, REQUEST_CODE_DOCK_SCORE);
                break;
            case R.id.text_view_patrol_register_delete:
                //点击删除，删除一个登记教师
                if (listener != null) {
                    listener.onDelete (teacherListBeans);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult (requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_DOCK_SCORE && resultCode == RESULT_OK) {
            changedListener.setData ((ArrayList<PatrolRegisterBeanNew.TeacherListBean>) data
                    .getExtras ().getSerializable (DockScoreNewActivity.OPTION_BEANS));
            status = data.getIntExtra (STATUS, -1);
            setAutoScoreTvText ();
            createScoreTitle (teacherListBeans.get (position));
        }
    }

    /**
     * 为fragment填充数据，该页面下某位教师的得分详情列表
     *
     * @param teacherListBean
     */
    public void createScoreTitle (PatrolRegisterBeanNew.TeacherListBean teacherListBean) {
        optionsBeanList = new ArrayList<> ();
        for (int i = 0, size = teacherListBean.getDockScore ().size (); i < size; i++) {
            PatrolRegisterBeanNew.TeacherListBean.DockScoreBean.OptionsBean bean =
                    new PatrolRegisterBeanNew.TeacherListBean.DockScoreBean.OptionsBean ();
            bean.setIsTitle (true);
            bean.setAllScore (teacherListBean.getDockScore ().get (i).getAllScore ());
            bean.setTitle (teacherListBean.getDockScore ().get (i).getName ());
            bean.setItemType (1);
            bean.setGetScore (teacherListBean.getDockScore ().get (i).getGetAllScore ());//自动计算所得总分
            bean.setInputScore (teacherListBean.getDockScore ().get (i).getUnAutoScore ());
            optionsBeanList.add (bean);
            //如果是自动计算，那么就将数据集合转换成options
            if (DockScoreNewActivity.AUTO_SCORE.equals (teacherListBean.getAutoScore ())) {
                List<PatrolRegisterBeanNew.TeacherListBean.DockScoreBean.OptionsBean> options =
                        teacherListBean.getDockScore ().get (i).getOptions ();
                for (int j = 0, ss = options.size (); j < ss; j++) {
                    if (status == DockScoreNewActivity.STATUS) {
                        if (options.get (j).isIsCheck ()) {
                            optionsBeanList.add (options.get (j));
                        }
                    } else {
                        if (options.get (j).getGetScore () != null && !"".equals (options.get (j).getGetScore ())) {
                            optionsBeanList.add (options.get (j));
                        }
                    }
                }
            }

        }

        AutoPatrolPointsDetailsFragmentAdapter adapter =
                new AutoPatrolPointsDetailsFragmentAdapter (optionsBeanList, status, teacherListBean.getAutoScore ());
        mRecyclerView.setAdapter (adapter);

    }

    private void setAutoScoreTvText () {
        String mIsAutoScore = teacherListBeans.get (position).getAutoScore ();
        mAutoScoreTv.setText (mIsAutoScore.equals (DockScoreNewActivity.AUTO_SCORE) ? getContext ()
                .getResources ().getString (R.string.patrol_class_auto_score)
                : getContext ().getResources ().getString (R.string.patrol_class_no_auto_score));
    }

    /**
     * 删除fragment监听
     */
    public interface DeleteItemListener {
        void onDelete (List<PatrolRegisterBeanNew.TeacherListBean> teacherListBeen);
    }

    /**
     * 在fragment点击编辑---编辑得分页面----保存-----拿到更改后的数据需要同步更新到登记页面，提交的时候要用到
     * 并且也要同步更新到所有的fragment中，因为在不同的fragment中编辑保存数据后要保证登记页面拿到的这个
     * teacherListBeans集合数据是最新的
     */
    public interface ListChangedListener {
        void setData (ArrayList<PatrolRegisterBeanNew.TeacherListBean> teacherListBeans);
    }
}
