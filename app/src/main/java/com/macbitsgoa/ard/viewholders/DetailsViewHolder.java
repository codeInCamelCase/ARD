package com.macbitsgoa.ard.viewholders;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.macbitsgoa.ard.R;
import com.macbitsgoa.ard.activities.AuthActivity;
import com.macbitsgoa.ard.models.DetailsItem;

import java.util.Vector;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by aayush on 29/3/18.
 */

public class DetailsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    @BindView(R.id.textView_details_title)
    public TextView TV_details;

    private Context mContext;
    private Vector<DetailsItem> list;
    public DetailsViewHolder(View itemView, Context mContext, Vector<DetailsItem> list) {
        super(itemView);
        this.list=list;
        this.mContext=mContext;
        ButterKnife.bind(this,itemView);
        itemView.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch(list.get(getAdapterPosition()).getTAG()){
            case "ARD":
              /* Intent intent=new Intent(mContext,);
               mContext.startActivity(intent);
               break;*/
            case "MAC":
            /*    Intent intent=new Intent(mContext,);
                mContext.startActivity(intent);
                break;*/
            case "LOGOUT":
                FirebaseAuth.getInstance().signOut();
                Intent intent=new Intent(mContext, AuthActivity.class);
                mContext.startActivity(intent);
                ((Activity)mContext).finish();
                break;
        }
    }
}
