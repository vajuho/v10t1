package com.example.v10t1;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SearchActivity extends AppCompatActivity {

    private EditText CityNameEdit;

    private EditText YearEdit;

    private TextView StatusText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        CityNameEdit = findViewById(R.id.CityNameEdit);
        YearEdit = findViewById(R.id.YearEdit);
        StatusText = findViewById(R.id.StatusText);
    }

    public void setData(View view) {

        Context context = this;
        CarDataRetriever cr = new CarDataRetriever();
        ExecutorService service = Executors.newSingleThreadExecutor();

        // Päivitetään heti UI-threadissä StatusText
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                StatusText.setText("Haetaan");
            }
        });

        service.execute(new Runnable() {
            @Override
            public void run() {

                // Hae data taustasäikeessä
                ArrayList<CarData> data = cr.getData(context,
                        CityNameEdit.getText().toString(),
                        YearEdit.getText().toString());

                // Tarkistetaan null ja päivitetään UI-threadissä
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (data == null) {
                            StatusText.setText("Haku epäonnistui, kaupunkia ei ole olemassa tai se on kirjoitettu väärin.");
                            return;
                        }

                        // Tallennetaan data storageen
                        CarDataStorage storage = CarDataStorage.getInstance();
                        storage.clearData();
                        for (CarData c : data) {
                            storage.addCarData(c);
                        }
                        storage.setCity(CityNameEdit.getText().toString());
                        storage.setYear(Integer.parseInt(YearEdit.getText().toString()));

                        // Päivitetään UI-threadissä StatusText
                        StatusText.setText("Haku onnistui");
                    }
                });
            }
        });
    }

    public void goToListInfo(View view) {
        Intent intent = new Intent(this, ListInfoActivity.class);
        startActivity(intent);
    }

    public void goToHome(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}