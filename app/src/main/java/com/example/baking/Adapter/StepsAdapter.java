package com.example.baking.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baking.R;
import com.example.baking.model.Step;

import java.util.List;

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepsViewHolder> {

    private List<Step> mStepList;
    private StepClickHandler mStepClickHandler;



    public StepsAdapter(StepClickHandler mStepClickHandler,List<Step> StepList){
        this.mStepClickHandler=mStepClickHandler;
        this.mStepList=StepList;
    }

    @NonNull
    @Override
    public StepsAdapter.StepsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.step_card_view,parent,false);
        layoutView.setTag(mStepList);
        return new StepsViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull StepsAdapter.StepsViewHolder holder, int position) {
        holder.StepTitle.setText(mStepList.get(position).getShortDescription());
    }

    @Override
    public int getItemCount() {
        return mStepList.size();
    }

    public interface StepClickHandler {
        void onStepClickHandler(int position);
    }

    public class StepsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView StepTitle;

        public StepsViewHolder(@NonNull View itemView) {
            super(itemView);
            StepTitle = itemView.findViewById(R.id.step_title);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mStepClickHandler.onStepClickHandler(getAdapterPosition());
            Log.v("Steps Adapter", "StepsAdapter Clicked");
        }
    }
}


