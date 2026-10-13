package com.example.foodviet.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodviet.R;
import com.example.foodviet.adapter.DishAdapter;
import com.example.foodviet.data.DishRepository;
import com.example.foodviet.model.Dish;

import java.util.List;

public class FavoritesActivity extends AppCompatActivity {

    private DishRepository repo;
    private DishAdapter adapter;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_favorites);

        androidx.appcompat.app.ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setTitle(getString(R.string.favorites));
        }

        repo = new DishRepository(this);
        RecyclerView rv = findViewById(R.id.rvFavs);
        rv.setLayoutManager(new LinearLayoutManager(this));

        adapter = new DishAdapter(this, dish -> {
            repo.toggleFavorite(dish.getId(), !dish.isFavorite());
            loadFavs();
        });
        rv.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadFavs();
    }

    private void loadFavs() {
        List<Dish> list = repo.favorites();
        adapter.submit(list);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
