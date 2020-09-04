package com.example.baking.Fragment;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baking.Adapter.StepsAdapter;
import com.example.baking.IngredientActivity;
import com.example.baking.R;
import com.example.baking.RecipeStepActivity;
import com.example.baking.WidgetTow.AppWidget;
import com.example.baking.model.Recipe;
import com.example.baking.model.Step;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class StepListFragment extends Fragment implements StepsAdapter.StepClickHandler

    {

        private OnFragmentInteractionListener listener;
        private Recipe recipe;
        private Recipe ARecipe;

        private RecyclerView recyclerView;
        private Step step;

        private ImageView mFavorite;

        private RecipeStepActivity appCtx;

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_step_list,container,false);

            recipe=getArguments().getParcelable("Recipe");
            recyclerView =view.findViewById(R.id.recyclerView_steps);

            recyclerView.setAdapter(new StepsAdapter(this,recipe.getSteps()));
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

            mFavorite=view.findViewById(R.id.heart_button);

            SharedPreferences sharedPreferences =this.getActivity().getSharedPreferences("shard preferences",Context.MODE_PRIVATE);
            Gson gson=new Gson();
            if (sharedPreferences.contains("Favorite Recipe")){
                String json=sharedPreferences.getString("Favorite Recipe",null);
                Type type=new TypeToken<Recipe>() {}.getType();
                Recipe getRecipe=gson.fromJson(json,type);
                if (getRecipe != null){
                    if ((getRecipe.getName()).equals(recipe.getName())){
                        mFavorite.setImageResource(R.drawable.thumb_on);
                        mFavorite.setTag(R.drawable.thumb_on);
                    }else{
                        mFavorite.setImageResource(R.drawable.thumb_off);
                        mFavorite.setTag(R.drawable.thumb_off);
                    }
                }else {
                    mFavorite.setImageResource(R.drawable.thumb_off);
                    mFavorite.setTag(R.drawable.thumb_off);
                }


            }else {
                mFavorite.setImageResource(R.drawable.thumb_off);
                mFavorite.setTag(R.drawable.thumb_off);
            }

            mFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Integer resource = (Integer)mFavorite.getTag();

                    if (resource == R.drawable.thumb_off){
                        saveData(recipe);
                        mFavorite.setImageResource(R.drawable.thumb_on);
                        mFavorite.setTag(R.drawable.thumb_on);
                    }else {
                        saveData(ARecipe);
                        mFavorite.setImageResource(R.drawable.thumb_off);
                        mFavorite.setTag(R.drawable.thumb_off);
                    }

                    Intent intent = new Intent(getContext(), AppWidget.class);
                    intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
                    int[] ids = AppWidgetManager.getInstance(getActivity().getApplication())
                            .getAppWidgetIds(new ComponentName(getActivity().getApplication(), AppWidget.class));
                    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
                    getActivity().sendBroadcast(intent);

                }
            });

        return view;
    }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView textView=view.findViewById(R.id.step_title);
        Button button=view.findViewById(R.id.Button);

        button.setOnClickListener(onClickListener);
        recipe=getArguments().getParcelable("Recipe");


    }

    AdapterView.OnClickListener onClickListener=new AdapterView.OnClickListener(){

        @Override
        public void onClick(View view) {
            Intent intent=new Intent(getActivity(), IngredientActivity.class);
            intent.putExtra("Recipe", recipe);

            getActivity().startActivity(intent);
        }
    };

        @Override
        public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(activity instanceof  OnFragmentInteractionListener){
            listener = (OnFragmentInteractionListener) activity;
        }
    }

        @Override
        public void onDetach() {
        super.onDetach();
        listener = null;
    }

        @Override
        public void onStepClickHandler(int position) {
            Log.v("Steps Adapter", "onStepClickHandler Clicked");
            StepFragmentListener ingredientStepsOnClickListener = (StepFragmentListener) getActivity();
            Bundle bundle = new Bundle();
            step=recipe.getSteps().get(position);
            bundle.putParcelable("step",step);
            bundle.putParcelable("Recipe",recipe);
            Log.v("Steps Adapter", step.getShortDescription());
            bundle.putParcelableArrayList("ArrayList", (ArrayList) recipe.getSteps());
            ingredientStepsOnClickListener.onStepFragmentListener(bundle);
        }

        private void saveData(Recipe recipe){
            SharedPreferences sharedPreferences =this.getActivity().getSharedPreferences("shard preferences",Context.MODE_PRIVATE);
            SharedPreferences.Editor editor=sharedPreferences.edit();
            Gson gson =new Gson();
            String json = gson.toJson(recipe);
            editor.putString("Favorite Recipe",json);
            editor.apply();
        }
        public interface OnFragmentInteractionListener {
            public void onStepSelected(Step step);
        }


        public interface StepFragmentListener {
            void onStepFragmentListener(Bundle bundle);
            void onStepSelected(Step step);
        }
    }


