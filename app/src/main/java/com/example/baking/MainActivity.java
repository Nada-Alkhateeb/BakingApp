package com.example.baking;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.baking.Adapter.RecipeAdapter;
import com.example.baking.model.Recipe;
import com.example.baking.util.APIService;
import com.example.baking.util.Constant;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecipeAdapter recipeAdapter;
    private static final String TAG = MainActivity.class.getSimpleName();
    private List<Recipe> recipes;
    private Bundle bundle;
    private Boolean isTablet;
    private Button retry;


    private LinearLayout NoInternetIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NoInternetIcon=(LinearLayout) findViewById(R.id.No_internet);
        retry=(Button) findViewById(R.id.try_again_button);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);

        if(isNetworkAvailable()){
            NoInternetIcon.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            loadRecipes();
        }else{
            recyclerView.setVisibility(View.GONE);
            NoInternetIcon.setVisibility(View.VISIBLE);
        }

        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isNetworkAvailable()){
                    NoInternetIcon.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    loadRecipes();
                }else{
                    recyclerView.setVisibility(View.GONE);
                    NoInternetIcon.setVisibility(View.VISIBLE);
                }
            }
        });


    }

    private void loadRecipes() {

        SharedPreferences  mPrefs = getPreferences(MODE_PRIVATE);
        isTablet=isTablet(this);

        if(isTablet == true){
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView myList = (RecyclerView) findViewById(R.id.recyclerView);
        myList.setLayoutManager(layoutManager);
        }

        APIService apiService= Constant.getClient().create(APIService.class);
        Call<List<Recipe>> call=apiService.getAllRecipes();
        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                 recipes=response.body();
                RecipesSharedPrefe(recipes);
                recyclerView.setAdapter(new RecipeAdapter(getApplicationContext(),recipes,isTablet));
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });


    }

    public void RecipesSharedPrefe(List<Recipe> recipes){
        SharedPreferences.Editor editor = getSharedPreferences("PreferencesName", MODE_PRIVATE).edit();
        editor.putInt("RecipesSize", recipes.size());
        editor.apply();
    }

    public List<Recipe> getRecipes(){
        return recipes;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public boolean isTablet(Context context) {
        boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4);
        boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
        return (xlarge || large);
    }


}
