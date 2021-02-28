package com.example.travel;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private Button plan;
    private Button add;
    private String currentCityName;
    private Adapter mainAdapter = new Adapter();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findView();

        plan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                        .setView(R.layout.taiwan_city)
                        .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                // TODO Auto-generated method stub
                                //Toast.makeText(MainActivity.this, "我了解了", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .show();
                LinearLayout contentLayout = dialog.findViewById(R.id.contentLayout);
                final ArrayList<TravelManager.TravelData> taiwantravelDataList = TravelManager.getInstance(MainActivity.this).getTaiwanTravelDataList().get(0).InfoList;


                ArrayList<String> displayCities = new ArrayList<>();
                for (final TravelManager.TravelData planeData : taiwantravelDataList) {
                    if(!displayCities.contains(planeData.Region)){
                        displayCities.add(planeData.Region);
                    }
                }

                for(final String cityName : displayCities){

                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    DisplayMetrics metrics = getResources().getDisplayMetrics();
                    layoutParams.setMargins(0, (int) (5 * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT)), 0, 0);
                    final TextView textView = new TextView(MainActivity.this);
                    textView.setLayoutParams(layoutParams);
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                    textView.setText(cityName);
                    contentLayout.addView(textView);

                    textView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            currentCityName = cityName;
                            setTitle(cityName);
                            //if(isRujing){
                            mainAdapter.refreshUI(taiwantravelDataList);
                            mainAdapter.setCurrentCityName(currentCityName);
                            //  }else{
                            //    mainAdapter.refreshUI(MainActivity.this, planeData.FIDSArrivalList);
                            // }

                            dialog.dismiss();
                        }
                    });
                }

            }

        });
        }

    @Override
    protected void onResume() {
        super.onResume();
        TravelManager.getInstance(this).syncDataFromRemote(this, new Runnable() {
            @Override
            public void run() {
                ArrayList<TravelManager.TravelData> planeDataList =
                        TravelManager.getInstance(MainActivity.this).getTaiwanTravelDataList().get(0).InfoList;
                for (TravelManager.TravelData planeData : planeDataList) {
                    String name = planeData.Region;
                    if(name.equals("臺北市")){
                        currentCityName  =name;
                        mainAdapter.refreshUI(planeDataList);
                        mainAdapter.setCurrentCityName(currentCityName);
                        setTitle(name);
                    }

                    //mainAdapter.refreshUI(planeData.);
                }

            }
        });
    }

    private void findView() {
        recyclerView = findViewById(R.id.recyclerView);
        plan = findViewById(R.id.plan);
        add = findViewById(R.id.add);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(mainAdapter);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                        .setCancelable(false)
                        .setView(R.layout.add_place)
                        .show();
                dialog.findViewById(R.id.dizhi);
                dialog.findViewById(R.id.city);
                dialog.findViewById(R.id.qu);
                dialog.findViewById(R.id.check).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        EditText e = dialog.findViewById(R.id.travelName);
                        String travelName = e.getText().toString();
                        EditText e1 = dialog.findViewById(R.id.dizhi);
                        String dizhi = e1.getText().toString();
                        EditText e2 = dialog.findViewById(R.id.city);
                        String city = e2.getText().toString();
                        EditText e3 = dialog.findViewById(R.id.qu);
                        String qu = e3.getText().toString();
                        TravelManager.TravelData data = new TravelManager.TravelData();
                        data.Name = travelName;
                        data.Add = dizhi;
                        data.Region = city;
                        data.Town = qu;
                        TravelManager.getInstance(view.getContext())
                                .addExtraTravelData(data);
                        dialog.dismiss();
                        mainAdapter.refreshUI(TravelManager.getInstance(view.getContext()).getTaiwanTravelDataList().get(0).InfoList);
                    }
                });
            }
        });
    }
}
