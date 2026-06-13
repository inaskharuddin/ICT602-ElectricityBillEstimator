package com.example.electricitybillestimator;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AboutActivity extends AppCompatActivity {

    TextView textWebsite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        if (getSupportActionBar() != null) {

            getSupportActionBar()
                    .setDisplayHomeAsUpEnabled(true);
        }

        setTitle("About Developer");

        textWebsite =
                findViewById(R.id.textWebsite);

        // Open UiTM website
        textWebsite.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent =
                                new Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse(
                                                "https://www.uitm.edu.my"
                                        )
                                );

                        startActivity(intent);
                    }
                });
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}