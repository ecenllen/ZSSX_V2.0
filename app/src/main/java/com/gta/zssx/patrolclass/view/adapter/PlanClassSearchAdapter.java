package com.gta.zssx.patrolclass.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gta.zssx.R;
import com.gta.zssx.patrolclass.model.entity.PlanClassSearchEntity;

import java.util.List;

/**
 * Created by liang.lu on 2016/8/23 14:14.
 */
public class PlanClassSearchAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List<PlanClassSearchEntity> entities;
    private PlanClassSearchListener listener;
    private String mSearchString;

    public void setmSearchString (String mSearchString) {
        this.mSearchString = mSearchString;
    }

    public PlanClassSearchAdapter(Context context){
        this.mContext = context;
    }

    public void setEntities (List<PlanClassSearchEntity> entities) {
        this.entities = entities;
    }

    public void setListener(PlanClassSearchListener listener){
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        View view = LayoutInflater.from (mContext).inflate (R.layout.item_class_search,parent,false);
        PlanClassSearchViewHolder holder = new PlanClassSearchViewHolder (view);
        return holder;
    }

    @Override
    public void onBindViewHolder (RecyclerView.ViewHolder holder, int position) {
        PlanClassSearchEntity entity = entities.get (position);
        String lClassName = entity.getClassName ();
        if (holder instanceof PlanClassSearchViewHolder){
            String lSource = "<font color=\'#00ad64\'>" + mSearchString + "</font>";
            String[] lSplit = lClassName.split(mSearchString);
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
            ((PlanClassSearchViewHolder) holder).classNameTv.setText (Html.fromHtml(lS));
        }
    }

    @Override
    public int getItemCount () {
        return entities.size ();
    }

    public class PlanClassSearchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView classNameTv;

        public PlanClassSearchViewHolder (View itemView) {
            super (itemView);
            classNameTv = (TextView) itemView.findViewById (R.id.class_name_tv);
            classNameTv.setOnClickListener (this);
        }

        @Override
        public void onClick (View view) {
            listener.itemClick (view,getLayoutPosition ());
        }
    }

    public void removeData() {
        notifyItemRangeRemoved(0, getItemCount());
        entities.clear();
    }

    public interface PlanClassSearchListener {
        void itemClick (View view, int position);
    }
}
