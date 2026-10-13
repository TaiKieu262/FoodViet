package com.example.foodviet.ui;
// chi tiêt món, nhận dish_id rồi hiển thị
// người dùng co thể yêu thích, sửa, xóa
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import com.bumptech.glide.Glide;
import com.example.foodviet.R;
import com.example.foodviet.data.DishRepository;
import com.example.foodviet.model.Dish;

public class DishDetailActivity extends AppCompatActivity {

    private DishRepository repo;
    private Dish dish;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), true);
        setContentView(R.layout.activity_dish_detail);
        //khởi tạo activity, gắn layout

        androidx.appcompat.app.ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setTitle("Chi tiết món");
        }
        //hiển thị nút back, đặt tiêu đề mhinh

        repo = new DishRepository(this);

        long id = getIntent().getLongExtra("dish_id", -1);
        if (id == -1) { finish(); return; }
        //nhận dishid từ adapter, ko có đóng mh
        dish = repo.get(id);
        if (dish == null) { finish(); return; }
        //truy vấn dtb từ repo, ko có -> thoát
        ImageView img = findViewById(R.id.imgCover);
        TextView tvName = findViewById(R.id.tvName);
        TextView tvRegion = findViewById(R.id.tvRegion);
        TextView tvIntro = findViewById(R.id.tvIntro);
        TextView tvIngredients = findViewById(R.id.tvIngredients);
        TextView tvSteps = findViewById(R.id.tvSteps);
        TextView labelIng = findViewById(R.id.labelIngredients);
        TextView labelSteps = findViewById(R.id.labelSteps);
        Button btnToggleFav = findViewById(R.id.btnToggleFav);
        Button btnEdit = findViewById(R.id.btnEdit);
        Button btnDelete = findViewById(R.id.btnDelete);
        // ánh xạ view
        Glide.with(this).load(dish.getImageUrl()).into(img);//load ảnh = glide
        tvName.setText(dish.getName());//hiển thị tên
        tvRegion.setText(dish.getRegion());

        String desc = dish.getDescription() == null ? "" : dish.getDescription();
        String intro = desc, ingredients = "", steps = "";
        String upper = desc.toUpperCase();
        int idxIng = upper.indexOf("NGUYÊN LIỆU");
        int idxSteps = upper.indexOf("CÁCH LÀM");
        // tách nd mô tả thành 3phan: ko có phần nào ẩn label tương ứng
        // xly logic dlieu trước khi hiển thị UI
        if (idxIng >= 0) {
            intro = desc.substring(0, idxIng).trim();
            if (idxSteps > idxIng) {
                ingredients = desc.substring(idxIng, idxSteps).replaceFirst("(?i)NGUYÊN LIỆU[:：]?", "").trim();
                steps = desc.substring(idxSteps).replaceFirst("(?i)CÁCH LÀM[:：]?", "").trim();
            } else {
                ingredients = desc.substring(idxIng).replaceFirst("(?i)NGUYÊN LIỆU[:：]?", "").trim();
            }
        } else if (idxSteps >= 0) {
            intro = desc.substring(0, idxSteps).trim();
            steps = desc.substring(idxSteps).replaceFirst("(?i)CÁCH LÀM[:：]?", "").trim();
        }
        tvIntro.setText(intro.isEmpty() ? "—" : intro);
        if (ingredients.isEmpty()) { labelIng.setVisibility(android.view.View.GONE); tvIngredients.setVisibility(android.view.View.GONE); }
        else { tvIngredients.setText(ingredients); }
        if (steps.isEmpty()) { labelSteps.setVisibility(android.view.View.GONE); tvSteps.setVisibility(android.view.View.GONE); }
        else { tvSteps.setText(steps); }

        updateFavBtn(btnToggleFav);
        btnToggleFav.setOnClickListener(v -> {
            boolean toFav = !dish.isFavorite(); //bật tắt trạng thái yêu thích
            repo.toggleFavorite(dish.getId(), toFav); //cập nhất dtb qua repo, đồng bộ lại ui
            dish.setFavorite(toFav);
            updateFavBtn(btnToggleFav);
            Toast.makeText(this, toFav ? getString(R.string.added_to_fav) : getString(R.string.removed_from_fav), Toast.LENGTH_SHORT).show();
        });

        // Sửa: mở mh thêm món, truyền edit id vào chế độ sua
        btnEdit.setOnClickListener(v -> {
            Intent i = new Intent(this, AddDishActivity.class);
            i.putExtra("edit_id", dish.getId());
            startActivity(i);
        });

        // Xóa: confirm rồi xóa
        btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Xóa món")
                    .setMessage("Bạn chắc chắn muốn xóa \"" + dish.getName() + "\"?")
                    .setPositiveButton("Xóa", (dlg, w) -> {
                        repo.delete(dish.getId());
                        Toast.makeText(this, "Đã xóa", Toast.LENGTH_SHORT).show();
                        finish(); // quay lại
                    })
                    .setNegativeButton("Hủy", null)
                    .show();
        });
    }

    private void updateFavBtn(Button b) {
        b.setText(dish.isFavorite() ? "Bỏ Yêu thích" : "Thêm Yêu thích");
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }// xử lý nút back

    @Override
    protected void onResume() {
        super.onResume();
        // nếu vừa sửa xong quay lại, tải lại để cập nhật UI
        long id = dish.getId();
        dish = repo.get(id);
        if (dish == null) { finish(); return; }
        ((TextView)findViewById(R.id.tvName)).setText(dish.getName());
        ((TextView)findViewById(R.id.tvRegion)).setText(dish.getRegion());
        Glide.with(this).load(dish.getImageUrl()).into((ImageView)findViewById(R.id.imgCover));
    }
}

// add dish activity