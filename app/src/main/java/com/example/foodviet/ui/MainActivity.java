package com.example.foodviet.ui;
// trung tâm app
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat; // <-- import
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodviet.R;
import com.example.foodviet.adapter.DishAdapter;
import com.example.foodviet.data.DishRepository;
import com.example.foodviet.model.Dish;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DishRepository repo;
    private DishAdapter adapter;
    private EditText edtSearch;
    private Spinner spnRegion;
// Kieu duc tai
    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        // Ngăn gdien tràn lên status bar
        WindowCompat.setDecorFitsSystemWindows(getWindow(), true);   // <-- thêm
        setContentView(R.layout.activity_main);

        repo = new DishRepository(this); // khowri tạo để cbi lm việc vs dlieu

        edtSearch  = findViewById(R.id.edtSearch);
        spnRegion  = findViewById(R.id.spnRegion);
        RecyclerView rv = findViewById(R.id.rvDishes);
        Button btnOpenFav = findViewById(R.id.btnOpenFav);
        Button btnAddDish = findViewById(R.id.btnAddDish);
        // ánh xạ các view trong thư viện để cbi thao tác
        ArrayAdapter<CharSequence> spAdapter = ArrayAdapter.createFromResource(
                this, R.array.regions_array, android.R.layout.simple_spinner_item);
        // lấy dlieu từ region array gắn vào spinner
        spAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnRegion.setAdapter(spAdapter);//hien thi

        adapter = new DishAdapter(this, dish -> {  // hiển thi ds món
            repo.toggleFavorite(dish.getId(), !dish.isFavorite()); // khi bấm yeu thích ,activity gọi repo để cập nhật dtb
            reload();
        });

        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);
        // hiển thị ds đọc
        //recycle view sẵn sàng hien thi dlieu

        btnOpenFav.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, FavoritesActivity.class)));
        //gán sự kiện click cho nút
        btnAddDish.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, AddDishActivity.class)));


        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(Editable s) {
                reload(); }
        });

        spnRegion.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) { reload(); }
            @Override public void onNothingSelected(android.widget.AdapterView<?> parent) {}
        });

        reload();
    }

    @Override
    protected void onResume() {
        super.onResume();
        reload();
    }
    // từ add dish, favourite quay về. load lại dlieu

    private void reload() {
        String keyword = edtSearch.getText().toString().trim();
        String region  = (String) spnRegion.getSelectedItem();
        List<Dish> list = repo.search(keyword, region);
        if (keyword.isEmpty() && (region == null || region.equals("Tất cả"))) {
            list = repo.loadAll();
        }
        adapter.submit(list);
    }
}
// dish repo