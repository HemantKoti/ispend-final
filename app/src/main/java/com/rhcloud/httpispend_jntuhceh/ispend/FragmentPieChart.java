package com.rhcloud.httpispend_jntuhceh.ispend;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.PercentFormatter;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.SeriesSelection;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import java.util.ArrayList;
import java.util.HashMap;

public class FragmentPieChart extends Fragment {

    View pieChartFragmentView;

    DatabaseHelper databaseHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        pieChartFragmentView  = inflater.inflate(R.layout.fragment_pie_chart, container, false);
        pieChartFragmentView.setBackgroundColor(Color.WHITE);

        databaseHelper = new DatabaseHelper(getContext());

        HashMap<String, String> transactionSummaryHashMap = databaseHelper.getTransactionSummaryHashMap();

        if(transactionSummaryHashMap != null && transactionSummaryHashMap.size() > 0) {

            PieChart pieChart = (PieChart) pieChartFragmentView.findViewById(R.id.pieChart);
            pieChart.setUsePercentValues(true);
            pieChart.setCenterText("Spends Summary");
            pieChart.setDescription("");

            // creating data values
            ArrayList<Entry> entries = new ArrayList<>();
            int i = 0;
            for(String totalAmount : transactionSummaryHashMap.values()) {
                entries.add(new Entry(Float.parseFloat(totalAmount), i));
                i++;
            }

            PieDataSet pieDataSet = new PieDataSet(entries, "");
            pieDataSet.setValueFormatter(new PercentFormatter());
            pieDataSet.setColors(getManyColors());

            // creating labels
            ArrayList<String> labels = new ArrayList<String>();
            for(String category : transactionSummaryHashMap.keySet()) {
                labels.add(category);
            }

            PieData pieData = new PieData(labels, pieDataSet); // initialize Piedata
            pieChart.setData(pieData);
        }
        else {
            PieChart pieChart = (PieChart) pieChartFragmentView.findViewById(R.id.pieChart);
            pieChart.setUsePercentValues(true);
            pieChart.setCenterText("No Data available");
            pieChart.setDescription("");

            // creating data values
            ArrayList<Entry> entries = new ArrayList<>();
            PieDataSet pieDataSet = new PieDataSet(entries, "");
            pieDataSet.setValueFormatter(new PercentFormatter());
            pieDataSet.setColors(getManyColors());

            // creating labels
            ArrayList<String> labels = new ArrayList<String>();

            PieData pieData = new PieData(labels, pieDataSet); // initialize Piedata
            pieChart.setData(pieData);
        }

        return pieChartFragmentView;
    }

    public void  updateContents() {
        //Toast.makeText(getContext(), "updated", Toast.LENGTH_SHORT).show();
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer, new FragmentPieChart()).commit();
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
