package com.israt.tourmate.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.israt.tourmate.PojoClass.Moment;
import com.israt.tourmate.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MomentAdapter extends RecyclerView.Adapter<MomentAdapter.ViewHolder> {

    private Context context;
    private List<Moment> momentList;

    public MomentAdapter(Context context, List<Moment> momentList) {
        this.context = context;
        this.momentList = momentList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.moment_model_design,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Moment moment = momentList.get(position);
        Picasso.get().load(moment.getMomentImg()).into(holder.momentIvDesign);
    }

    @Override
    public int getItemCount() {
        return momentList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView momentIvDesign;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            momentIvDesign = itemView.findViewById(R.id.momentIvDesign);

        }
    }

}
