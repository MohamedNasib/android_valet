package com.icapp.icapp.Activities;

import static com.icapp.icapp.Helpers.Constants.ORDER_STATUS_2;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.icapp.icapp.Adapters.SearchAdapter;
import com.icapp.icapp.Helpers.RealM;
import com.icapp.icapp.Models.CarReport;
import com.icapp.icapp.R;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class SearchActivity extends ParentActivity  implements TextWatcher {
    ScrollView scroll_bar;
    LinearLayout empty_state;
    EditText txtSearch ;
    SearchAdapter adapter;
    ArrayList<CarReport> carReports = new ArrayList<>() ;
    RecyclerView rvCars;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarGradiant(this);
        setContentView(R.layout.activity_search);
        rvCars = findViewById(R.id.rvCars);
        scroll_bar = findViewById(R.id.scroll_bar);
        empty_state = findViewById(R.id.empty_state);
        txtSearch = findViewById(R.id.txtSearch);

        carReports = RealM.getInstance().getArrivalList().stream().filter(report -> report.getStatus() != ORDER_STATUS_2).collect(Collectors.toCollection(ArrayList::new));

        adapter = new SearchAdapter(this, carReports);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(this);
        rvCars.setLayoutManager(lm);
        rvCars.setAdapter(adapter);
        txtSearch.addTextChangedListener(this);

        if (carReports.isEmpty()) {
            scroll_bar.setVisibility(View.GONE);
            empty_state.setVisibility(View.VISIBLE);
        } else {
            scroll_bar.setVisibility(View.VISIBLE);
            empty_state.setVisibility(View.GONE);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        ArrayList<CarReport> lst = RealM.getInstance().searchPlateOrTicket(charSequence.toString());
        carReports.clear();
        carReports.addAll(lst);
        adapter.setData(carReports);
        adapter.notifyDataSetChanged();
    }
}