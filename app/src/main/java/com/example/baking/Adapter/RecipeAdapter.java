package com.example.baking.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baking.R;
import com.example.baking.RecipeStepActivity;
import com.example.baking.model.Recipe;

import java.util.ArrayList;
import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private List<Recipe> mRecipeList;
    private Context mContext;
    private Bundle mBundle;
    private Boolean mScreenType;

    public RecipeAdapter(Context mContext, List<Recipe> RecipeList,Boolean screenType){
        this.mContext=mContext;
        this.mRecipeList=RecipeList;
        this.mScreenType=screenType;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView;
        if (mScreenType == true){
        layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_tablet_card_view,parent,false);
        }else {
        layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_card_view,parent,false);}
        return new RecipeViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        holder.recipeTitle.setText(mRecipeList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return mRecipeList.size();
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder {

        public TextView recipeTitle;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);

            recipeTitle=(TextView) itemView.findViewById(R.id.recipe_title);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Recipe recipe=mRecipeList.get(getAdapterPosition());
                    Intent intent=new Intent(mContext, RecipeStepActivity.class);
                    mBundle = new Bundle();
                    intent.putExtra("Recipe", recipe);
                    mBundle.putParcelableArrayList("ArrayList", (ArrayList) recipe.getSteps());
                    mBundle.putParcelable("Recipe", recipe);
                    intent.putExtras(mBundle);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                }
            });
        }
    }

}
