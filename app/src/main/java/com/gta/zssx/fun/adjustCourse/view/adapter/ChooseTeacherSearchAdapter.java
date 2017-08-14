package com.gta.zssx.fun.adjustCourse.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gta.zssx.R;
import com.gta.zssx.fun.adjustCourse.model.bean.ChooseTeacherSearchEntity;

import java.util.List;

/**
 * Created by liang.lu on 2016/8/17 15:44.
 */
public class ChooseTeacherSearchAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<ChooseTeacherSearchEntity> entities;
    private ClickTeacherItemListener listener;
    private String mSearchString;

    public void setmSearchString (String mSearchString) {
        this.mSearchString = mSearchString;
    }

    public void setEntities (List<ChooseTeacherSearchEntity> entities) {
        this.entities = entities;
    }

    public ChooseTeacherSearchAdapter(Context context, ClickTeacherItemListener listener) {
        this.mContext = context;
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        View view = LayoutInflater.from (mContext).inflate (R.layout.item_choose_teacher_search, parent, false);
        ChooseTeacherSearchHolder holder = new ChooseTeacherSearchHolder (view);
        return holder;
    }

    @Override
    public void onBindViewHolder (RecyclerView.ViewHolder holder, int position) {
        ChooseTeacherSearchEntity entity = entities.get (position);
        String mTeacherName = entity.getName ();
        String mTeacherCode = entity.getTeacherCode ();
        if (holder instanceof ChooseTeacherSearchHolder) {
            String mTeacherColor = SetKeyWordColor (mTeacherName);
            String mCodeColor = SetKeyWordColor (mTeacherCode);
            if (mTeacherName.contains (mSearchString) && mTeacherCode.contains (mSearchString)) {
                ((ChooseTeacherSearchHolder) holder).mTeacherName.setText (Html.fromHtml (mTeacherColor));
                ((ChooseTeacherSearchHolder) holder).mTeacherCode.setText (Html.fromHtml (mCodeColor));
            } else if (mTeacherCode.contains (mSearchString)) {
                ((ChooseTeacherSearchHolder) holder).mTeacherName.setText (mTeacherName);
                ((ChooseTeacherSearchHolder) holder).mTeacherCode.setText (Html.fromHtml (mCodeColor));
            } else if (mTeacherName.contains (mSearchString)) {
                ((ChooseTeacherSearchHolder) holder).mTeacherName.setText (Html.fromHtml (mTeacherColor));
                ((ChooseTeacherSearchHolder) holder).mTeacherCode.setText (mTeacherCode);
            } else {
                ((ChooseTeacherSearchHolder) holder).mTeacherName.setText (mTeacherName);
                ((ChooseTeacherSearchHolder) holder).mTeacherCode.setText (mTeacherCode);
            }

        }
    }

    @Override
    public int getItemCount () {
        return entities.size ();
    }

    private class ChooseTeacherSearchHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mTeacherName;
        private TextView mTeacherCode;

        public ChooseTeacherSearchHolder (View itemView) {
            super (itemView);
            mTeacherCode = (TextView) itemView.findViewById (R.id.tv_teacher_code);
            mTeacherName = (TextView) itemView.findViewById (R.id.tv_child);

            itemView.setOnClickListener (this);
        }

        @Override
        public void onClick (View v) {
            listener.clickTeacherChooseItem (getLayoutPosition ());
        }
    }

    public void removeData () {
        notifyItemRangeRemoved (0, getItemCount ());
        entities.clear ();
    }

    public interface ClickTeacherItemListener {
        void clickTeacherChooseItem(int position);
    }

    private String SetKeyWordColor (String s) {
        String lSource = "<font color=\'#00ad64\'>" + mSearchString + "</font>";
        String[] lSplit = s.split (mSearchString);
        int lIndexOf = s.indexOf (mSearchString);

        String lS = "";
        if (lIndexOf == 0) {
            if (lSplit.length == 0) {
                lS = lSource;
            } else {
                lS = lSource + "<font color=\'#a6a6a6\'>" + lSplit[1] + "</font>";
            }
        } else if (lSplit.length == 1) {
            lS = "<font color=\'#a6a6a6\'>" + lSplit[0] + "</font>" + lSource;
        } else {
            for (int i = 0; i < s.length (); i++) {
                char lC = s.charAt (i);
                if (i == lIndexOf) {
                    lS = lS + lSource + "<font color=\'#a6a6a6\'>" + lSplit[lSplit.length - 1] + "</font>";
                    break;
                }
                lS = lS + "<font color=\'#a6a6a6\'>" + lC + "</font>";
            }
        }
        return lS;
    }
}
