package com.example.foodviet.data;
// Nơi viết sql và chuyển dlieu dtb sang object dish
// thực hiện truy vấn
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.foodviet.model.Dish;

import java.util.ArrayList;
import java.util.List;

public class DishDao {
    private final AppDatabase helper;

    public DishDao(Context ctx) {
        helper = new AppDatabase(ctx);
    }

    public List<Dish> getAll() {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + AppDatabase.TBL_DISH + " ORDER BY name", null);
        List<Dish> list = mapCursor(c);
        c.close();
        return list;
    }
// cursor đại diện kq truy vấn
    public List<Dish> searchByName(String keyword) {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.rawQuery(
                "SELECT * FROM " + AppDatabase.TBL_DISH + " WHERE name LIKE ? ORDER BY name",
                new String[]{"%"+keyword+"%"});
        List<Dish> list = mapCursor(c);
        c.close();
        return list;
    }

    public List<Dish> searchByNameAndRegion(String keyword, String regionFilter) {
        SQLiteDatabase db = helper.getReadableDatabase();
        String like = "%" + keyword + "%";
        Cursor c;
        if (regionFilter == null || regionFilter.isEmpty() || regionFilter.equals("Tất cả")) {
            c = db.rawQuery("SELECT * FROM " + AppDatabase.TBL_DISH +
                    " WHERE name LIKE ? ORDER BY name", new String[]{ like });
        } else {
            c = db.rawQuery("SELECT * FROM " + AppDatabase.TBL_DISH +
                    " WHERE name LIKE ? AND region = ? ORDER BY name", new String[]{ like, regionFilter });
        }
        List<Dish> list = mapCursor(c);
        c.close();
        return list;
    }

    public Dish findById(long id) {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + AppDatabase.TBL_DISH + " WHERE id=?",
                new String[]{String.valueOf(id)});
        Dish d = null;
        if (c.moveToFirst()) d = mapRow(c);
        c.close();
        return d;
    }

    public long insert(Dish d) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues cv = toValues(d);
        return db.insert(AppDatabase.TBL_DISH, null, cv);
    }

    public int update(Dish d) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues cv = toValues(d);
        return db.update(AppDatabase.TBL_DISH, cv, "id=?", new String[]{String.valueOf(d.getId())});
    }

    public int delete(long id) {
        SQLiteDatabase db = helper.getWritableDatabase();
        return db.delete(AppDatabase.TBL_DISH, "id=?", new String[]{String.valueOf(id)});
    }

    public int updateFavorite(long id, boolean fav) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("favorite", fav ? 1 : 0);
        return db.update(AppDatabase.TBL_DISH, cv, "id=?", new String[]{String.valueOf(id)});
    }

    public List<Dish> getFavorites() {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + AppDatabase.TBL_DISH + " WHERE favorite=1 ORDER BY name", null);
        List<Dish> list = mapCursor(c);
        c.close();
        return list;
    }

    // Helpers
    private List<Dish> mapCursor(Cursor c) {
        List<Dish> list = new ArrayList<>();
        while (c.moveToNext()) list.add(mapRow(c));
        return list;
    }

    private Dish mapRow(Cursor c) {
        Dish d = new Dish();
        d.setId(c.getLong(c.getColumnIndexOrThrow("id")));
        d.setName(c.getString(c.getColumnIndexOrThrow("name")));
        d.setRegion(c.getString(c.getColumnIndexOrThrow("region")));
        d.setImageUrl(c.getString(c.getColumnIndexOrThrow("imageUrl")));
        d.setDescription(c.getString(c.getColumnIndexOrThrow("description")));
        d.setFavorite(c.getInt(c.getColumnIndexOrThrow("favorite")) == 1);
        return d;
    }

    private ContentValues toValues(Dish d) {
        ContentValues cv = new ContentValues();
        cv.put("name", d.getName());
        cv.put("region", d.getRegion());
        cv.put("imageUrl", d.getImageUrl());
        cv.put("description", d.getDescription());
        cv.put("favorite", d.isFavorite() ? 1 : 0);
        return cv;
    }
}
//app dtb