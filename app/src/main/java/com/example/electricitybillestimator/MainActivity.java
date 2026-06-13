package com.example.electricitybillestimator;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Spinner spinnerMonth;
    RadioGroup radioGroupRebateLeft, radioGroupRebateRight;;

    EditText editUnit;

    Button btnCalculate,
            btnHistory,
            btnAbout;

    TextView textTotal,
            textFinal;

    DataCalculation db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // SQLite Database
        db = new DataCalculation(this);

        // Connect XML with Java
        spinnerMonth =
                findViewById(R.id.spinnerMonth);

        radioGroupRebateLeft =
                findViewById(R.id.radioGroupRebateLeft);

        radioGroupRebateRight =
                findViewById(R.id.radioGroupRebateRight);

        editUnit =
                findViewById(R.id.editUnit);

        btnCalculate =
                findViewById(R.id.btnCalculate);

        btnHistory =
                findViewById(R.id.btnHistory);

        btnAbout =
                findViewById(R.id.btnAbout);

        textTotal =
                findViewById(R.id.textTotal);

        textFinal =
                findViewById(R.id.textFinal);

        // Month Spinner
        String[] months = {
                "-- Choose Month --",
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
                        android.R.layout.simple_spinner_dropdown_item,
                        months
                );

        spinnerMonth.setAdapter(monthAdapter);

        // Allow only ONE rebate selection

        radioGroupRebateLeft
                .setOnCheckedChangeListener(
                        (group, checkedId) -> {

                            if (checkedId != -1) {

                                radioGroupRebateRight
                                        .clearCheck();
                            }
                        });

        radioGroupRebateRight
                .setOnCheckedChangeListener(
                        (group, checkedId) -> {

                            if (checkedId != -1) {

                                radioGroupRebateLeft
                                        .clearCheck();
                            }
                        });

        // Calculate Button
        btnCalculate.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String unitText =
                                editUnit
                                        .getText()
                                        .toString()
                                        .trim();

                        // Validation
                        if (unitText.isEmpty()) {

                            Toast.makeText(
                                    MainActivity.this,
                                    "Please enter electricity unit",
                                    Toast.LENGTH_SHORT
                            ).show();

                            return;
                        }

                        // Month validation
                        String selectedMonth =
                                spinnerMonth
                                        .getSelectedItem()
                                        .toString();

                        if (selectedMonth.equals(
                                "-- Choose Month --")) {

                            Toast.makeText(
                                    MainActivity.this,
                                    "Please choose a month",
                                    Toast.LENGTH_SHORT
                            ).show();

                            return;
                        }

                        // Rebate validation
                        int selectedRadioId =
                                radioGroupRebateLeft
                                        .getCheckedRadioButtonId();

                        if (selectedRadioId == -1) {

                            selectedRadioId =
                                    radioGroupRebateRight
                                            .getCheckedRadioButtonId();
                        }

                        if (selectedRadioId == -1) {

                            Toast.makeText(
                                    MainActivity.this,
                                    "Please choose rebate percentage",
                                    Toast.LENGTH_SHORT
                            ).show();

                            return;
                        }

                        int unit =
                                Integer.parseInt(
                                        unitText
                                );

                        // Range validation
                        if (unit < 1
                                || unit > 1000) {

                            Toast.makeText(
                                    MainActivity.this,
                                    "Unit must be between 1 - 1000",
                                    Toast.LENGTH_SHORT
                            ).show();

                            return;
                        }

                        // Calculate bill
                        double totalCharges =
                                calculateBill(unit);

                        // Get selected rebate
                        RadioButton selectedRadio =
                                findViewById(
                                        selectedRadioId
                                );

                        String rebateText =
                                selectedRadio
                                        .getText()
                                        .toString();

                        rebateText =
                                rebateText.replace(
                                        "%",
                                        ""
                                );

                        double rebate =
                                Double.parseDouble(
                                        rebateText
                                ) /100;

                        // Final cost
                        double finalCost =
                                totalCharges -
                                        (totalCharges
                                                * rebate);

                        // Display result
                        textTotal.setText(
                                String.format(
                                        "RM %.2f",
                                        totalCharges
                                )
                        );

                        textFinal.setText(
                                String.format(
                                        "RM %.2f",
                                        finalCost
                                )
                        );

                        // Save into SQLite
                        boolean inserted =
                                db.insertData(
                                        selectedMonth,
                                        unit,
                                        rebate * 100,
                                        totalCharges,
                                        finalCost
                                );

                        if (inserted) {

                            Toast.makeText(
                                    MainActivity.this,
                                    "Data Saved Successfully",
                                    Toast.LENGTH_SHORT
                            ).show();

                        } else {

                            Toast.makeText(
                                    MainActivity.this,
                                    "Failed to Save Data",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    }
                });

        // Open History Page
        btnHistory.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent =
                                new Intent(
                                        MainActivity.this,
                                        HistoryActivity.class
                                );

                        startActivity(intent);
                    }
                });

        // Open About Page
        btnAbout.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent =
                                new Intent(
                                        MainActivity.this,
                                        AboutActivity.class
                                );

                        startActivity(intent);
                    }
                });
    }

    // Electricity bill calculation
    private double calculateBill(int unit) {

        double total = 0;

        if (unit <= 200) {

            total = unit * 0.218;
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
}