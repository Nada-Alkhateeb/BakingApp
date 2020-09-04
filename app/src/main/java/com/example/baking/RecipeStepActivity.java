package com.example.baking;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActionBar;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.baking.Adapter.StepsAdapter;
import com.example.baking.Fragment.StepDetailFragment;
import com.example.baking.Fragment.StepListFragment;
import com.example.baking.WidgetTow.AppWidget;
import com.example.baking.model.Recipe;
import com.example.baking.model.Step;

public class RecipeStepActivity extends AppCompatActivity implements StepListFragment.StepFragmentListener {


    private Recipe recipe;
    private Bundle bundle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step);

        Bundle data = getIntent().getExtras();
        recipe = (Recipe) data.getParcelable("Recipe");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(recipe.getName());


        Log.v("Steps Adapter", recipe.getName());

        bundle = new Bundle();
        if (!(recipe == null))
            bundle.putParcelable("Recipe", recipe);

        if (savedInstanceState != null)
            return;
        StepListFragment fragment = new StepListFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragment.setArguments(bundle);
        fragmentTransaction.add(R.id.recipe_host_frame_layout, fragment);

        if (findViewById(R.id.recipe_fragment) != null) {

            StepDetailFragment rightFragment = new StepDetailFragment();
            rightFragment.setArguments(bundle);
            fragmentTransaction.add(R.id.recipe_fragment, rightFragment);
        }
        fragmentTransaction.commit();

    }

    @Override
    public void onStepFragmentListener(Bundle bundle) {
        StepDetailFragment animalDetailFragment;

        Step recipe = (Step) bundle.getParcelable("step");

        if (findViewById(R.id.recipe_fragment) == null) {
            Intent intent = new Intent(this, StepDetailActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);

        } else {

            animalDetailFragment = new StepDetailFragment();
            animalDetailFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.recipe_fragment, animalDetailFragment).commit();
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onStepSelected(Step step) {

        FragmentManager fragmentManager = getSupportFragmentManager();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Log.v("Steps Adapter", "onStepSelected Clicked");

        StepDetailFragment animalDetailFragment;

        if (findViewById(R.id.recipe_fragment) == null) {

            Intent intent = new Intent(this, StepDetailActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);

        } else {

            animalDetailFragment = new StepDetailFragment();
            animalDetailFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.recipe_fragment, animalDetailFragment).commit();
        }


    }

}
