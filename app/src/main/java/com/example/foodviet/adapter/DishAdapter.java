package com.example.foodviet.adapter;
// hiển thị dlieu ra gdien. lấy dlieu dish gán vào gdien item
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodviet.R;
import com.example.foodviet.model.Dish;
import com.example.foodviet.ui.DishDetailActivity;

import java.util.ArrayList;
import java.util.List;

public class DishAdapter extends RecyclerView.Adapter<DishAdapter.DishVH> {

    public interface OnFavoriteClick { void onFav(Dish dish); }
// bdau skien nút click
    private final Context ctx;
    private final OnFavoriteClick favListener;
    private List<Dish> data = new ArrayList<>();

    public DishAdapter(Context ctx, OnFavoriteClick favListener) {
        this.ctx = ctx;
        this.favListener = favListener;
    }

    public void submit(List<Dish> list) {
        data = list != null ? list : new ArrayList<>();
        notifyDataSetChanged();
    }
    // cập nhất ds mới,ycau rv vẽ lại
    @NonNull @Override
    public DishVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(ctx).inflate(R.layout.item_dish, parent, false);
        return new DishVH(v);
    }
    //tạo gdien 1 dòng món ăn từ itemdishxml
    @Override
    public void onBindViewHolder(@NonNull DishVH h, int pos) {
        Dish d = data.get(pos);
        h.txtName.setText(d.getName());
        h.txtRegion.setText(d.getRegion());
        h.txtShort.setText(d.getDescription());
// lấy món ăn từ pos rồi hiển thị
        Glide.with(ctx).load(d.getImageUrl()).into(h.img);

        h.itemView.setOnClickListener(v -> {
            Intent it = new Intent(ctx, DishDetailActivity.class);
            it.putExtra("dish_id", d.getId()); // khi click item,truyền dish id để lấy ds món
            ctx.startActivity(it);
        });

        h.btnFav.setImageResource(d.isFavorite()
                ? android.R.drawable.btn_star_big_on
                : android.R.drawable.btn_star_big_off);

        h.btnFav.setOnClickListener(v -> favListener.onFav(d));
    }// khi click, cập nhất dtb, reload ds

    @Override
    public int getItemCount() { return data.size(); }

    static class DishVH extends RecyclerView.ViewHolder {
        ImageView img;
        TextView txtName, txtRegion, txtShort;
        ImageButton btnFav;
        // dishvh giữ các view của 1 item và tối ưu hiệu năng
        public DishVH(@NonNull View v) {
            super(v);
            img = v.findViewById(R.id.imgThumb);
            txtName = v.findViewById(R.id.txtName);
            txtRegion = v.findViewById(R.id.txtRegion);
            txtShort = v.findViewById(R.id.txtShortDesc);
            btnFav = v.findViewById(R.id.btnFav);
        }
    }
    // dish detaiactivity
}
