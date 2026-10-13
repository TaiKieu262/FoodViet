package com.example.foodviet.model;
// dish model đại diện cho 1 món ăn
// chứa dlieu, truền dlieu giữa các tầng
public class Dish {
    private long id;
    private String name;
    private String region;
    private String imageUrl;
    private String description;
    private boolean favorite;
    //khai báo các thuộc tnh
    public Dish() { }

    public Dish(String name, String region, String imageUrl, String description, boolean favorite) {
        this.name = name;
        this.region = region;
        this.imageUrl = imageUrl;
        this.description = description;
        this.favorite = favorite;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getName() { return name; }
    public String getRegion() { return region; }
    public String getImageUrl() { return imageUrl; }
    public String getDescription() { return description; }
    public boolean isFavorite() { return favorite; }

    public void setName(String name) { this.name = name; }
    public void setRegion(String region) { this.region = region; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public void setDescription(String description) { this.description = description; }
    public void setFavorite(boolean favorite) { this.favorite = favorite; }
}
// dish adapter