package com.example.v10t1;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ListInfoActivity extends AppCompatActivity {

    private TextView CityText;
    private TextView YearText;
    private TextView CarInfoText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_list_info);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        CityText = findViewById(R.id.CityText);
        YearText = findViewById(R.id.YearText);
        CarInfoText = findViewById(R.id.CarInfoText);
        showData();
    }

    public void showData() {
        CarDataStorage storage = CarDataStorage.getInstance();

        String s = "";
        CityText.setText(storage.getCity());
        YearText.setText(String.valueOf(storage.getYear()));
        for(CarData i : storage.getCarData()){
            s += i.getType() + ": " + i.getAmount() + "\n";
        }
        CarInfoText.setText(s);
    }

    public void goToHome(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}