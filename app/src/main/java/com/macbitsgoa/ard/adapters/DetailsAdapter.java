package com.macbitsgoa.ard.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.macbitsgoa.ard.R;
import com.macbitsgoa.ard.models.DetailsItem;
import com.macbitsgoa.ard.viewholders.DetailsViewHolder;

import java.util.Vector;

/**
 * Created by aayush on 29/3/18.
 */

public class DetailsAdapter extends RecyclerView.Adapter<DetailsViewHolder> {
    private Vector<DetailsItem> detailsItemList=new Vector<>();

    public DetailsAdapter(Context mContext){
        detailsItemList.add(new DetailsItem("About ARD","ARD"));
        detailsItemList.add(new DetailsItem("About Mac","MAC"));
        detailsItemList.add(new DetailsItem("Sign Out","LOGOUT"));
    }

    @NonNull
    @Override
    public DetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view;

        if(viewType==0) {
            view = layoutInflater.inflate(R.layout.vh_details_main, parent, false);
        } else {
            view = layoutInflater.inflate(R.layout.vh_details_main, parent, false);
        }

        return new DetailsViewHolder(view,parent.getContext(),detailsItemList);
    }

    @Override
    public int getItemViewType(int position){
        if(position==0)
            return 0;
        else
            return 1;
    }

    @Override
    public void onBindViewHolder(@NonNull DetailsViewHolder holder, int position) {

        if(position != 0){
            holder.TV_details.setText(detailsItemList.get(position-1).getTitle());
       }else{

       }

    }

    @Override
    public int getItemCount() {
        return detailsItemList.size()+1;
    }


}
