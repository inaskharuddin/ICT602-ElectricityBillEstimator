package com.example.electricitybillestimator;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {

    ListView listViewHistory;

    DataCalculation db;

    ArrayList<String> listData;
    ArrayList<Integer> idList;

    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        if (getSupportActionBar() != null) {

            getSupportActionBar()
                    .setDisplayHomeAsUpEnabled(true);
        }

        setTitle("History");

        listViewHistory =
                findViewById(R.id.listViewHistory);

        db = new DataCalculation(this);

        listData = new ArrayList<>();
        idList = new ArrayList<>();

        loadData();

        // Click item
        listViewHistory.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(
                            AdapterView<?> parent,
                            View view,
                            int position,
                            long id) {

                        Intent intent =
                                new Intent(
                                        HistoryActivity.this,
                                        DetailActivity.class
                                );

                        intent.putExtra(
                                "id",
                                idList.get(position)
                        );

                        startActivity(intent);
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();

        listData.clear();
        idList.clear();

        loadData();
    }

    private void loadData() {

        Cursor cursor =
                db.getAllData();

        if (cursor.getCount() == 0) {

            Toast.makeText(
                    this,
                    "No History Found",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        while (cursor.moveToNext()) {

            int id =
                    cursor.getInt(0);

            String month =
                    cursor.getString(1);

            idList.add(id);

            listData.add(month);
        }

        adapter =
                new ArrayAdapter<String>(
                        this,
                        R.layout.row_bill,
                        R.id.textMonth,
                        listData
                ) {

                    @Override
                    public View getView(
                            int position,
                            View convertView,
                            ViewGroup parent
                    ) {

                        View view =
                                super.getView(
                                        position,
                                        convertView,
                                        parent
                                );

                        TextView textMonth =
                                view.findViewById(
                                        R.id.textMonth
                                );

                        TextView textCost =
                                view.findViewById(
                                        R.id.textCost
                                );

                        textMonth.setText(
                                listData.get(position)
                        );

                        Cursor costCursor =
                                db.getAllData();

                        costCursor.moveToPosition(position);

                        double cost =
                                costCursor.getDouble(5);

                        textCost.setText(
                                "Final Cost: RM " +
                                        String.format(
                                                "%.2f",
                                                cost
                                        )
                        );

                        return view;
                    }
                };

        listViewHistory.setAdapter(adapter);
    }
    @Override
    public boolean onSupportNavigateUp() {

        finish();
        return true;
    }
}