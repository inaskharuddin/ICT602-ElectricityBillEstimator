package com.example.electricitybillestimator;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class UpdateActivity
        extends AppCompatActivity {

    Spinner spinnerMonthUpdate,
            spinnerRebateUpdate;

    EditText editUnitUpdate;

    Button btnSaveUpdate,
            btnCalculateUpdate;

    TextView textTotalUpdate,
            textFinalUpdate;

    DataCalculation db;

    int selectedId;

    double totalCharge = 0;
    double finalCost = 0;

    @Override
    protected void onCreate(
            Bundle savedInstanceState
    ) {
        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_update
        );

        // Back arrow
        if (getSupportActionBar() != null) {

            getSupportActionBar()
                    .setDisplayHomeAsUpEnabled(
                            true
                    );
        }

        setTitle("Update Bill");

        // Connect XML
        spinnerMonthUpdate =
                findViewById(
                        R.id.spinnerMonthUpdate
                );

        spinnerRebateUpdate =
                findViewById(
                        R.id.spinnerRebateUpdate
                );

        editUnitUpdate =
                findViewById(
                        R.id.editUnitUpdate
                );

        btnCalculateUpdate =
                findViewById(
                        R.id.btnCalculateUpdate
                );

        btnSaveUpdate =
                findViewById(
                        R.id.btnSaveUpdate
                );

        textTotalUpdate =
                findViewById(
                        R.id.textTotalUpdate
                );

        textFinalUpdate =
                findViewById(
                        R.id.textFinalUpdate
                );

        db = new DataCalculation(this);

        // Month Spinner
        String[] months = {
                "January",
                "February",
                "March",
                "April",
                "May",
                "June",
                "July",
                "August",
                "September",
                "October",
                "November",
                "December"
        };

        ArrayAdapter<String> monthAdapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout
                                .simple_spinner_dropdown_item,
                        months
                );

        spinnerMonthUpdate
                .setAdapter(
                        monthAdapter
                );

        // Rebate Spinner
        String[] rebates = {
                "0%",
                "1%",
                "2%",
                "3%",
                "4%",
                "5%"
        };

        ArrayAdapter<String> rebateAdapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout
                                .simple_spinner_dropdown_item,
                        rebates
                );

        spinnerRebateUpdate
                .setAdapter(
                        rebateAdapter
                );

        // Get selected ID
        selectedId =
                getIntent()
                        .getIntExtra(
                                "id",
                                -1
                        );

        Cursor cursor =
                db.getDataById(
                        selectedId
                );

        // Load old data
        if (cursor.moveToFirst()) {

            String month =
                    cursor.getString(1);

            int unit =
                    cursor.getInt(2);

            double rebate =
                    cursor.getDouble(3);

            totalCharge =
                    cursor.getDouble(4);

            finalCost =
                    cursor.getDouble(5);

            // Set old unit
            editUnitUpdate.setText(
                    String.valueOf(unit)
            );

            // Show current value
            textTotalUpdate.setText(
                    String.format(
                            "RM %.2f",
                            totalCharge
                    )
            );

            textFinalUpdate.setText(
                    String.format(
                            "RM %.2f",
                            finalCost
                    )
            );

            // Set month spinner
            for (int i = 0;
                 i < months.length;
                 i++) {

                if (months[i]
                        .equals(month)) {

                    spinnerMonthUpdate
                            .setSelection(i);

                    break;
                }
            }

            // Set rebate spinner
            spinnerRebateUpdate
                    .setSelection(
                            (int) rebate
                    );
        }

        // CALCULATE BUTTON
        btnCalculateUpdate
                .setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(
                                    View v
                            ) {

                                calculateAndDisplay();
                            }
                        });

        // SAVE UPDATE BUTTON
        btnSaveUpdate
                .setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(
                                    View v
                            ) {

                                String month =
                                        spinnerMonthUpdate
                                                .getSelectedItem()
                                                .toString();

                                String unitText =
                                        editUnitUpdate
                                                .getText()
                                                .toString();

                                if (unitText.isEmpty()) {

                                    Toast.makeText(
                                            UpdateActivity.this,
                                            "Enter unit",
                                            Toast.LENGTH_SHORT
                                    ).show();

                                    return;
                                }

                                int unit =
                                        Integer.parseInt(
                                                unitText
                                        );

                                // Get rebate
                                String rebateText =
                                        spinnerRebateUpdate
                                                .getSelectedItem()
                                                .toString();

                                rebateText =
                                        rebateText.replace(
                                                "%",
                                                ""
                                        );

                                double rebate =
                                        Double.parseDouble(
                                                rebateText
                                        );

                                // IMPORTANT:
                                // Always recalculate
                                totalCharge =
                                        calculateBill(
                                                unit
                                        );

                                finalCost =
                                        totalCharge -
                                                (totalCharge
                                                        * rebate
                                                        / 100);

                                boolean updated =
                                        db.updateData(
                                                selectedId,
                                                month,
                                                unit,
                                                rebate,
                                                totalCharge,
                                                finalCost
                                        );

                                if (updated) {

                                    Toast.makeText(
                                            UpdateActivity.this,
                                            "Updated Successfully",
                                            Toast.LENGTH_SHORT
                                    ).show();

                                    Intent intent =
                                            new Intent(
                                                    UpdateActivity.this,
                                                    HistoryActivity.class
                                            );

                                    intent.setFlags(
                                            Intent.FLAG_ACTIVITY_CLEAR_TOP
                                                    | Intent.FLAG_ACTIVITY_NEW_TASK
                                    );

                                    startActivity(
                                            intent
                                    );

                                    finish();
                                }
                            }
                        });
    }

    // Calculate and display
    private void calculateAndDisplay() {

        String unitText =
                editUnitUpdate
                        .getText()
                        .toString();

        if (unitText.isEmpty()) {

            Toast.makeText(
                    this,
                    "Enter unit",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        int unit =
                Integer.parseInt(
                        unitText
                );

        String rebateText =
                spinnerRebateUpdate
                        .getSelectedItem()
                        .toString();

        rebateText =
                rebateText.replace(
                        "%",
                        ""
                );

        double rebate =
                Double.parseDouble(
                        rebateText
                );

        totalCharge =
                calculateBill(unit);

        finalCost =
                totalCharge -
                        (totalCharge
                                * rebate
                                / 100);

        textTotalUpdate.setText(
                String.format(
                        "RM %.2f",
                        totalCharge
                )
        );

        textFinalUpdate.setText(
                String.format(
                        "RM %.2f",
                        finalCost
                )
        );
    }

    // Bill calculation
    private double calculateBill(
            int unit
    ) {

        double total = 0;

        if (unit <= 200) {

            total =
                    unit * 0.218;
        }

        else if (unit <= 300) {

            total =
                    (200 * 0.218)
                            + ((unit - 200)
                            * 0.334);
        }

        else if (unit <= 600) {

            total =
                    (200 * 0.218)
                            + (100 * 0.334)
                            + ((unit - 300)
                            * 0.516);
        }

        else {

            total =
                    (200 * 0.218)
                            + (100 * 0.334)
                            + (300 * 0.516)
                            + ((unit - 600)
                            * 0.546);
        }

        return total;
    }

    @Override
    public boolean onSupportNavigateUp() {

        finish();
        return true;
    }
}