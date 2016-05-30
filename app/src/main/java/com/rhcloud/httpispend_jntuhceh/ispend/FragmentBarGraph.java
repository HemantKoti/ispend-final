package com.rhcloud.httpispend_jntuhceh.ispend;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.PercentFormatter;

import java.util.ArrayList;
import java.util.HashMap;

public class FragmentBarGraph extends Fragment {

    View barGraphFragmentView;

    DatabaseHelper databaseHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        barGraphFragmentView  = inflater.inflate(R.layout.fragment_bar_graph, container, false);
        barGraphFragmentView.setBackgroundColor(Color.WHITE);

        databaseHelper = new DatabaseHelper(getContext());

        HashMap<String, String> transactionSummaryHashMap = databaseHelper.getTransactionSummaryHashMap();

        if(transactionSummaryHashMap != null && transactionSummaryHashMap.size() > 0) {

            BarChart barChart = (BarChart) barGraphFragmentView.findViewById(R.id.barChart);
            barChart.setDescription("Spends Summary");

            // creating data values
            ArrayList<BarEntry> entries = new ArrayList<>();
            int i = 0;
            for(String totalAmount : transactionSummaryHashMap.values()) {
                entries.add(new BarEntry(Float.parseFloat(totalAmount), i));
                i++;
            }

            BarDataSet dataset = new BarDataSet(entries, "");
            dataset.setColors(getManyColors());

            // creating labels
            ArrayList<String> labels = new ArrayList<String>();
            for(String category : transactionSummaryHashMap.keySet()) {
                labels.add(category);
            }

            BarData data = new BarData(labels, dataset);
            barChart.setData(data);
            barChart.animateY(5000);
        }
        else {
            BarChart barChart = (BarChart) barGraphFragmentView.findViewById(R.id.barChart);
            barChart.setDescription("Spends Summary");

            // creating data values
            ArrayList<BarEntry> entries = new ArrayList<>();
            BarDataSet dataset = new BarDataSet(entries, "");
            dataset.setColors(getManyColors());

            // creating labels
            ArrayList<String> labels = new ArrayList<String>();

            BarData data = new BarData(labels, dataset);
            barChart.setData(data);
            barChart.animateY(5000);
        }

        return barGraphFragmentView;
    }

    public void  updateContents() {
        //Toast.makeText(getContext(), "updated", Toast.LENGTH_SHORT).show();
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer, new FragmentBarGraph()).commit();
    }

    public ArrayList<Integer> getManyColors() {
        // add many colors
        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        return colors;
    }
}
