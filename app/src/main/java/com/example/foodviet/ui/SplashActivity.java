package com.example.foodviet.ui;
// xac dinh splash là launcher, gọi splash đầu tiên.
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat; // <-- import

import com.example.foodviet.R;

public class SplashActivity extends AppCompatActivity { //chạy
    @Override protected void onCreate(@Nullable Bundle savedInstanceState) { //oncreate đc gọi
        super.onCreate(savedInstanceState); //gọi xly của lớp cha, cbi tài nguyên hệ thống
        // Ngăn gdien tràn lên status bar
        WindowCompat.setDecorFitsSystemWindows(getWindow(), true);   // <-- thêm
        setContentView(R.layout.activity_splash); // hiển thị gdien spl

        new android.os.Handler().postDelayed(() -> { // tạo đỗ trễ
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        }, 800);
    }
}
// main