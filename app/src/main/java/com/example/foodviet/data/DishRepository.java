package com.example.foodviet.data;
// main ko lm vc với dtb mà thông qua dish repo
import android.content.Context;

import com.example.foodviet.model.Dish;

import java.util.List;

public class DishRepository {
    private final DishDao dao;

    public DishRepository(Context ctx) {
        dao = new DishDao(ctx);
    } // tạo dish dao để thao tác dtb

    public List<Dish> loadAll() { return dao.getAll(); }
    public List<Dish> search(String keyword) { return dao.searchByName(keyword); }
    public List<Dish> search(String keyword, String region) { return dao.searchByNameAndRegion(keyword, region); }
    public Dish get(long id) { return dao.findById(id); }

    public long insert(Dish d) { return dao.insert(d); }
    public int update(Dish d) { return dao.update(d); }
    public int delete(long id) { return dao.delete(id); }

    public void toggleFavorite(long id, boolean toFav) { dao.updateFavorite(id, toFav); }
    public List<Dish> favorites() { return dao.getFavorites(); }
}
// dish dao