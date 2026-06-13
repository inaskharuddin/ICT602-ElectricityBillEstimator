package com.example.electricitybillestimator;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {

    TextView textMonthDetail,
            textUnitDetail,
            textRebateDetail,
            textTotalDetail,
            textFinalDetail;

    Button btnUpdate, btnDelete;

    DataCalculation db;

    int selectedId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (getSupportActionBar() != null) {

            getSupportActionBar()
                    .setDisplayHomeAsUpEnabled(true);
        }

        setTitle("Bill Details");

        textMonthDetail =
                findViewById(R.id.textMonthDetail);

        textUnitDetail =
                findViewById(R.id.textUnitDetail);

        textRebateDetail =
                findViewById(R.id.textRebateDetail);

        textTotalDetail =
                findViewById(R.id.textTotalDetail);

        textFinalDetail =
                findViewById(R.id.textFinalDetail);

        btnUpdate =
                findViewById(R.id.btnUpdate);

        btnDelete =
                findViewById(R.id.btnDelete);

        db = new DataCalculation(this);

        selectedId =
                getIntent().getIntExtra(
                        "id",
                        -1
                );

        Cursor cursor =
                db.getDataById(selectedId);

        if (cursor.moveToFirst()) {

            String month =
                    cursor.getString(1);

            int unit =
                    cursor.getInt(2);

            double rebate =
                    cursor.getDouble(3);

            double total =
                    cursor.getDouble(4);

            double finalCost =
                    cursor.getDouble(5);

            textMonthDetail.setText(
                    "Month: " + month
            );

            textUnitDetail.setText(
                    "Unit Used: " + unit + " kWh"
            );

            textRebateDetail.setText(
                    "Rebate: " + rebate + "%"
            );

            textTotalDetail.setText(
                    String.format(
                            "Total Charges: RM %.2f",
                            total
                    )
            );

            textFinalDetail.setText(
                    String.format(
                            "Final Cost: RM %.2f",
                            finalCost
                    )
            );
        }

        // DELETE BUTTON
        btnDelete.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        boolean deleted =
                                db.deleteData(
                                        selectedId
                                );

                        if (deleted) {

                            Toast.makeText(
                                    DetailActivity.this,
                                    "History Deleted",
                                    Toast.LENGTH_SHORT
                            ).show();

                            Intent intent =
                                    new Intent(
                                            DetailActivity.this,
                                            HistoryActivity.class
                                    );

                            intent.setFlags(
                                    Intent.FLAG_ACTIVITY_CLEAR_TOP
                            );

                            startActivity(intent);

                            finish();
                        }
                        else {

                            Toast.makeText(
                                    DetailActivity.this,
                                    "Delete Failed",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    }
                });
        // UPDATE BUTTON
        btnUpdate.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent =
                                new Intent(
                                        DetailActivity.this,
                                        UpdateActivity.class
                                );

                        intent.putExtra(
                                "id",
                                selectedId
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