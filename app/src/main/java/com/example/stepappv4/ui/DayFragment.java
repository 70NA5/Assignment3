package com.example.stepappv4.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.ColorLong;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.TreeMap;


import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Column;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;
import com.example.stepappv4.StepAppOpenHelper;
import com.example.stepappv4.databinding.FragmentDayBinding;
import com.example.stepappv4.R;

import androidx.fragment.app.Fragment;

public class DayFragment extends Fragment {


    public int todaySteps = 0;
    TextView numStepsTextView;
    AnyChartView anyChartView2;

    Date cDate = new Date();
    String current_time = new SimpleDateFormat("yyyy-MM-dd").format(cDate);

    public Map<String, Integer> stepsByDay = null;

    private FragmentDayBinding binding;
    Calendar calendar = Calendar.getInstance(); //calender is used to retrieve the last seven days

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentDayBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Create column chart
        anyChartView2 = root.findViewById(R.id.dayBarChart);
        anyChartView2.setProgressBar(root.findViewById(R.id.loadingBarDay));

        Cartesian cartesian = createColumnChart();
        anyChartView2.setBackgroundColor("#00000000");
        anyChartView2.setChart(cartesian);


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    /**
     * This function creates the bar chart with the steps of the last 7 days using the stepByDay function from the StepAppOpenHelper
     * @return
     */
    public Cartesian createColumnChart() {

        // TODO 1 (YOUR TURN): Get the map with days and the number of steps for each day
        // from the database and assign it to variable stepsByDay
        stepsByDay = StepAppOpenHelper.loadStepsByDay(getContext());

        // TODO 4: Create and get the cartesian coordinate system for the column chart
        Cartesian cartesian = AnyChart.column();

        // TODO 5: Create data entries for x and y axis of the graph
        List<DataEntry> data = new ArrayList<>();

        // Here the last 7 days are loaded based on the current day
        List<String> last7Days = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();

        for (int i = 0; i < 7; i++) {
            last7Days.add(dateFormat.format(calendar.getTime()));
            calendar.add(Calendar.DAY_OF_YEAR, -1);
        }

        // This iterates over the last 7 days to display the data
        for (int i = last7Days.size() - 1; i >= 0; i--) {
            String date = last7Days.get(i);
            Integer numSteps = stepsByDay.get(date);
            numSteps = (numSteps != null) ? numSteps : 0;
            data.add(new ValueDataEntry(date, numSteps));
        }


        // TODO 6: Add the data to the column chart and get the columns
        Column column = cartesian.column(data);

        // TODO 7 (YOUR TURN): Change the color of the column chart and its border
        column.fill("#1EB980");
        column.stroke("#1EB980");

        // TODO 8: Modifying properties of the tooltip
        column.tooltip()
                .titleFormat("On day: {%X}")
                .format("{%Value} Steps")
                .anchor(Anchor.RIGHT_BOTTOM);

        // TODO 9 (YOUR TURN): Modify column chart tooltip properties
        column.tooltip()
                .position(Position.RIGHT_TOP)
                .offsetX(0d)
                .offsetY(5);

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
        cartesian.interactivity().hoverMode(HoverMode.BY_X);
        cartesian.yScale().minimum(0);

        // TODO 10 (YOUR TURN): Modify the UI of the cartesian
        cartesian.yAxis(0).title("Number of steps");
        cartesian.xAxis(0).title("Day");
        cartesian.background().fill("#00000000");
        cartesian.animation(true);

        return cartesian;
    }
}
