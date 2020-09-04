package com.example.baking;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.baking.model.Ingredient;
import com.example.baking.model.Recipe;

import java.util.ArrayList;
import java.util.List;

public class IngredientActivity extends AppCompatActivity {

    public Recipe recipe;
    public ArrayAdapter adapter;
    public ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Bundle data = getIntent().getExtras();
        recipe = (Recipe) data.getParcelable("Recipe");
        List<Ingredient> ingredient = recipe.getIngredients();

        listView =(ListView) findViewById(R.id.list_item);

        populateStepList(ingredient);

    }

    private void populateStepList(List<Ingredient> ingredient) {
        ArrayList<String> ArrayIngredient = new ArrayList<String>();
        for (int i=0;i<ingredient.size();i++){
            Log.i("msg", "link is " + ingredient.get(i).string());
            ArrayIngredient.add(ingredient.get(i).string());
        }

        adapter = new ArrayAdapter<>(this,R.layout.activity_listview,R.id.textView,ArrayIngredient);
        listView.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home)
        {
            super.onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
}
