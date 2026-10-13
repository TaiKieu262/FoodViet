package com.example.foodviet.ui;
// thêm mới/ chỉnh sửa món.
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import com.example.foodviet.R;
import com.example.foodviet.data.DishRepository;
import com.example.foodviet.model.Dish;

public class AddDishActivity extends AppCompatActivity {

    private DishRepository repo;
    private long editId = -1; // -1 = thêm mới; >=0 = sửa

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), true);
        setContentView(R.layout.activity_add_dish);

        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }

        repo = new DishRepository(this);

        EditText edtName = findViewById(R.id.edtName);
        Spinner spnRegion = findViewById(R.id.spnRegion);
        EditText edtImageUrl = findViewById(R.id.edtImageUrl);
        EditText edtDesc = findViewById(R.id.edtDesc);
        Button btnSave = findViewById(R.id.btnSave);

        ArrayAdapter<CharSequence> spAdapter = ArrayAdapter.createFromResource(
                this, R.array.regions_array, android.R.layout.simple_spinner_item);
        spAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnRegion.setAdapter(spAdapter);

        // Kiểm tra có phải edit mode không
        editId = getIntent().getLongExtra("edit_id", -1);
        if (editId >= 0) {
            if (ab != null) ab.setTitle("Sửa món");

            Dish d = repo.get(editId);
            if (d != null) {
                edtName.setText(d.getName());
                edtImageUrl.setText(d.getImageUrl());
                edtDesc.setText(d.getDescription());
// lấy dlieu từ dtb và đổ lên form sửa
                // chọn đúng miền, đảm bảo dlieu hthi chính xác
                String[] arr = getResources().getStringArray(R.array.regions_array);
                for (int i = 0; i < arr.length; i++) {
                    if (arr[i].equalsIgnoreCase(d.getRegion())) {
                        spnRegion.setSelection(i);
                        break;
                        //duyet qua tung vungmien trong spinner. so sanh vs region mon
                        // khi trùng set selection chon dung mien
                    }
                }
            }
        } else {
            if (ab != null) ab.setTitle("Thêm món cộng đồng");
        }

        btnSave.setOnClickListener(v -> {
            String name = edtName.getText().toString().trim();
            String region = (String) spnRegion.getSelectedItem();
            if ("Tất cả".equals(region)) region = "";
            String imageUrl = edtImageUrl.getText().toString().trim();
            String desc = edtDesc.getText().toString().trim();
// lấy dlieu - ktra -thêm mới/update
            if (TextUtils.isEmpty(name)) {
                Toast.makeText(this, "Vui lòng nhập tên món", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(region)) {
                Toast.makeText(this, "Vui lòng chọn miền", Toast.LENGTH_SHORT).show();
                return;
            }

            if (editId >= 0) {
                Dish d = repo.get(editId); // gọi update trong repo
                if (d == null) { Toast.makeText(this, "Món không tồn tại", Toast.LENGTH_SHORT).show(); return; }
                d.setName(name);
                d.setRegion(region);
                d.setImageUrl(imageUrl);
                d.setDescription(desc);
                repo.update(d);
                Toast.makeText(this, "Đã cập nhật món!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                //them moi. tạo object dish mới
                Dish d = new Dish(name, region, imageUrl, desc, false);//
                long id = repo.insert(d);// ghi vào dtb = insert
                if (id > 0) {
                    Toast.makeText(this, "Đã thêm món!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "Thêm thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
// favourite activity
