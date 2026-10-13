package com.example.foodviet.data;
// dtb đc tạo = sqliteOpenHelper
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AppDatabase extends SQLiteOpenHelper {
    public static final String DB_NAME = "foodviet.db";
    public static final int DB_VERSION = 4; // tăng nếu cần upgrade

    public static final String TBL_DISH = "dishes";

    public AppDatabase(Context ctx) {
        super(ctx, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE " + TBL_DISH + " (" +
                        "  id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "  name TEXT NOT NULL," +
                        "  region TEXT," +
                        "  imageUrl TEXT," +
                        "  description TEXT," +
                        "  favorite INTEGER DEFAULT 0" +
                        ")"
        );

        // Seed dữ liệu mẫu (ảnh online) để có nội dung ngay khi mở
        db.execSQL("INSERT INTO dishes(name,region,imageUrl,description,favorite) VALUES" +
                "('Phở bò','Miền Bắc','https://daotaobeptruong.vn/wp-content/uploads/2020/03/cach-nau-pho-bo.jpg','Phở bò là món ăn truyền thống của Việt Nam gồm bánh phở làm từ bột gạo, nước dùng ninh từ xương bò và thịt bò thái lát, ăn kèm với các gia vị như hành, gừng, quế, tiêu, chanh và ớt. Nước dùng phở thường trong, ngọt thanh, đậm đà hương vị do ninh kỹ với các gia vị truyền thống như quế, hồi, gừng. Bánh phở mềm dai, thịt bò tươi ngon, mềm hoặc chín tái tùy loại, và thường được ăn nóng cùng các loại rau thơm như hành lá, ngò gai, giá đỗ.',0)," +
                "('Bún bò Huế','Miền Trung','https://fohlafood.vn/cdn/shop/articles/bun-bo-hue.jpg?v=1709633070','Bún bò Huế là món ăn đặc sản của Huế nổi tiếng với nước dùng đậm đà hương sả và mắm tôm, ăn kèm sợi bún dai, thịt bò, chả cua và các loại rau thơm như giá đỗ, húng quế. Nước dùng ngọt thanh từ xương ống bò, có màu sắc hấp dẫn nhờ tiết heo và dầu điều, tạo nên hương vị cay nồng, mặn mà đặc trưng. Sợi bún của món này thường to hơn bún thông thường ở các vùng khác. ',0)," +
                "('Bánh mì thịt','Miền Nam','https://mms.img.susercontent.com/vn-11134513-7r98o-lsvhlojr0538f9@resize_ss1242x600!@crop_w1242_h600_cT','Bánh mì thịt là món ăn đường phố Việt Nam phổ biến, được làm bằng cách xẻ dọc ổ bánh mì baguette giòn, rồi nhồi nhiều loại nhân đa dạng bên trong. Nhân bánh thường bao gồm pate, các loại thịt (như thịt nguội, thịt nướng, chả, xá xíu), rau thơm, dưa leo, đồ chua (cà rốt và củ cải muối), cùng với các loại sốt đặc trưng',0)," +
                "('Gỏi cuốn','Miền Nam','https://www.cet.edu.vn/wp-content/uploads/2018/11/goi-cuon-tom-thit.jpg','Gỏi cuốn, còn gọi là nem cuốn, là một món ăn truyền thống của Việt Nam làm từ bánh tráng cuốn với nhiều nguyên liệu tươi ngon như bún, tôm, thịt luộc và các loại rau sống như xà lách, húng quế, hẹ, diếp cá. Món này thường được chấm với tương hoặc mắm nêm, mang lại hương vị hài hòa, thanh mát, có thể ăn làm khai vị hoặc món chính.',0)," +
                "('Cao lầu','Miền Trung','https://cdn.tgdd.vn/2021/12/CookDish/cao-lau-la-gi-nguon-goc-cao-lau-cao-lau-va-mi-quang-khac-nhau-avt-1200x676.jpg','Cao lầu là một món ăn đặc sản của Hội An, Quảng Nam, nổi bật với sợi mì vàng dai, làm từ gạo ngâm nước tro, ăn kèm với thịt xá xíu, tôm, rau sống và bánh đa nướng giòn. Điểm đặc trưng là món ăn chỉ có rất ít nước dùng, chủ yếu để chan nhẹ lên phần nhân đã được bày sẵn, tạo nên hương vị đậm đà và kết cấu phong phú.',0)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        // đơn giản: tạo mới (nếu bạn cần giữ dữ liệu cũ hãy viết ALTER/UPDATE thay vì DROP)
        db.execSQL("DROP TABLE IF EXISTS " + TBL_DISH);
        onCreate(db);
    }
}
// dish.java
