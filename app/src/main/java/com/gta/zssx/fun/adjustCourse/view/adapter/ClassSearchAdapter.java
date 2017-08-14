package com.gta.zssx.fun.adjustCourse.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gta.zssx.R;
import com.gta.zssx.fun.adjustCourse.model.bean.SearchClassBean;

import java.util.List;
import java.util.regex.Pattern;

/**
 * [Description]
 * <p/>
 * [How to use]
 * <p/>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/6/17.
 * @since 1.0.0
 */
public class ClassSearchAdapter extends RecyclerView.Adapter {

    private String mSearchString;
    private Context mContext;

    private Listener mListener;
    private List<SearchClassBean> mSearchClassBeans;

    public ClassSearchAdapter(Context context, Listener listener, List<SearchClassBean> searchClassBeans, String searchString) {
        mContext = context;
        mListener = listener;
        mSearchClassBeans = searchClassBeans;
        mSearchString = searchString;
    }

    public List<SearchClassBean> getSearchClassBeans() {
        return mSearchClassBeans;
    }

    public void removeData() {
        mSearchClassBeans.clear();
        notifyItemRangeRemoved(0, getItemCount());
    }

    public interface Listener {
        void itemClick(SearchClassBean searchClassBean);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return SearchHolder.newHolder(parent, mContext);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        SearchHolder lHolder = (SearchHolder) holder;
        final SearchClassBean lSearchClassBean = mSearchClassBeans.get(position);
        String lClassName = lSearchClassBean.getClassName();
        String lSource = "<font color=\'#00ad64\'>" + mSearchString + "</font>";
        String[] lSplit = lClassName.split(Pattern.quote(mSearchString));
        int lIndexOf = lClassName.indexOf(mSearchString);

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
            for (int i = 0; i < lClassName.length(); i++) {
                char lC = lClassName.charAt(i);
                if (i == lIndexOf) {
                    lS = lS + lSource + "<font color=\'#a6a6a6\'>" + lSplit[lSplit.length - 1] + "</font>";
                    break;
                }
                lS = lS + "<font color=\'#a6a6a6\'>" + lC + "</font>";
            }
        }
        lHolder.classNameTv.setText(Html.fromHtml(lS));
//        Html.fromHtml("<font color=\'#a6a6a6\'>" + str1 + "</font>" + "<font color=\'#8d7af2\'>" + lName + "</font>" + "<font color=\'#323232\'>" + lContent + "</font>");
        lHolder.classNameTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.itemClick(lSearchClassBean);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mSearchClassBeans.size();
    }

    private static class SearchHolder extends RecyclerView.ViewHolder {
        TextView classNameTv;

        public SearchHolder(Context context, View itemView) {
            super(itemView);
            classNameTv = (TextView) itemView.findViewById(R.id.class_name_tv);
        }

        private static SearchHolder newHolder(ViewGroup parent, Context context) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_class_search, parent, false);
            return new SearchHolder(context, view);
        }
    }
}
