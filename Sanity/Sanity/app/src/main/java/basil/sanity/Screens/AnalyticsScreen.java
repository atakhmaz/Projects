package basil.sanity.Screens;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import basil.sanity.Controllers.Budget;
import basil.sanity.Controllers.BudgetManager;
import basil.sanity.Controllers.Category;
import basil.sanity.Controllers.CategoryPeriod;
import basil.sanity.Controllers.Settings;
import basil.sanity.Controllers.Transaction;
import basil.sanity.Database.Database;
import basil.sanity.R;

public class AnalyticsScreen extends AppCompatActivity implements OnChartValueSelectedListener {
    private PieChart mPie;
    private LineChart mLine;
    private ArrayList<Integer> colors;
    private int categoryChosen = -1;
    private ArrayList<Transaction> transactionsToDisplay;

    private int budgetIndex;
    private Database db;
    private Category category;
    private String usernameStr;
    private String budgetNameStr;
    private String categoryNameStr;
    private CategoryPeriod currentPeriod;
    private Date startDate;
    private Date endDate;
    private Double amountSpent;
    private ArrayList<CategoryPeriod> periodList;
    private ArrayList<String> periodListStr;
    private CategoryPeriod selectedPeriod;
    private ArrayList<Transaction> transactionsInPeriod;

    private Button periodButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_analytics_screen);

        budgetIndex = getIntent().getIntExtra("BUDGET_ID", 0);

        //Get database info
        db = new Database(getBaseContext());
        usernameStr = BudgetManager.getInstance().getUsername();
        category = BudgetManager.getInstance().get(budgetIndex).getCategory(0);
        categoryNameStr = category.getCategoryName();
        budgetNameStr = BudgetManager.getInstance().get(budgetIndex).getBudgetName();

        //Get period info
        periodList = db.GetCategoryPeriod
                (usernameStr, categoryNameStr, budgetNameStr);
        //Show the newest periodList first
        Collections.sort(periodList);
        currentPeriod = periodList.get(0);
        startDate = currentPeriod.getStartDate();
        endDate = currentPeriod.getEndDate();
        periodListStr = new ArrayList<>();
        //Put period start and end dates in an arraylist
        for (int i=0; i < periodList.size(); ++i) {
            periodListStr.add(periodList.get(i).toString());
        }
        //Populate select period popup with period dates
        final ArrayAdapter<String> periodAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, periodListStr);

        periodButton = (Button) findViewById(R.id.analytics_period_button);
        periodButton.setTextColor(getResources().getColor(R.color.colorAccent));
        //Hide button to begin with
        periodButton.setVisibility(View.GONE);
        final String prompt = getResources().getString(R.string.select_period_label);
        periodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (categoryChosen != -1) {
                    category = BudgetManager.getInstance().get(budgetIndex).getCategory(categoryChosen);
                    categoryNameStr = category.getCategoryName();

                    //Get period info
                    periodList = db.GetCategoryPeriod(usernameStr, categoryNameStr, budgetNameStr);
                    //Show the newest periodList first
                    Collections.sort(periodList);
                    currentPeriod = periodList.get(0);
                    startDate = currentPeriod.getStartDate();
                    endDate = currentPeriod.getEndDate();
                    periodListStr = new ArrayList<>();
                    //Put period start and end dates in an arraylist
                    for (int i = 0; i < periodList.size(); ++i) {
                        periodListStr.add(periodList.get(i).toString());
                    }
                    periodAdapter.notifyDataSetChanged();
                    new AlertDialog.Builder(v.getContext())
                            .setTitle(prompt)
                            .setAdapter(periodAdapter, new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int position) {
                                    //Update current period with selected value
                                    currentPeriod = periodList.get(position);
                                    startDate = currentPeriod.getStartDate();
                                    endDate = currentPeriod.getEndDate();
                                    //Update pie chart
                                    mLine.invalidate();
                                    setLineData(categoryChosen);
                                    dialog.dismiss();
                                }
                            }).create().show();
                }
                else {
                    // TODO: Show some message about needing to select a category first
                    periodButton.setTextColor(getResources().getColor(R.color.colorAccent));
                }
            }
        });

        colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);


        mPie = (PieChart) findViewById(R.id.chart);

        mPie.setUsePercentValues(true);
        mPie.getDescription().setEnabled(false);
        mPie.setExtraOffsets(5, 10, 5, 5);

        mPie.setDragDecelerationFrictionCoef(0.95f);

        mPie.setCenterTextColor(getResources().getColor(R.color.colorTextLight));
        mPie.setCenterTextSize(20);
        mPie.setCenterText("$anity\n$pending");

        mPie.setDrawHoleEnabled(true);
        mPie.setHoleColor(getResources().getColor(R.color.colorPrimary));

        mPie.setTransparentCircleColor(Color.WHITE);
        mPie.setTransparentCircleAlpha(110);

        mPie.setHoleRadius(58f);
        mPie.setTransparentCircleRadius(61f);

        mPie.setDrawCenterText(true);

        mPie.setRotationAngle(0);
        // enable rotation of the chart by touch
        mPie.setRotationEnabled(true);
        mPie.setHighlightPerTapEnabled(true);

        mPie.setEntryLabelColor(Color.BLACK);
        Legend pieLegend = mPie.getLegend();
        pieLegend.setTextColor(getResources().getColor(R.color.colorTextLight));

        Log.i("EVENT", "PIE CHART CREATED");
        mPie.setOnChartValueSelectedListener(this);
        setPieData();

        //Line Chart
        mLine = (LineChart) findViewById(R.id.chart1);
        mLine.setDrawGridBackground(false);
        Legend lineLegend = mLine.getLegend();
        lineLegend.setTextColor(getResources().getColor(R.color.colorTextLight));

        // no description text
        mLine.getDescription().setEnabled(false);

        // enable touch gestures
        mLine.setTouchEnabled(true);

        // enable scaling and dragging
        mLine.setDragEnabled(true);
        mLine.setScaleEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        mLine.setPinchZoom(true);

        // x-axis limit line
        LimitLine llXAxis = new LimitLine(10f, "Index 10");
        llXAxis.enableDashedLine(10f, 10f, 0f);
        llXAxis.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        llXAxis.setTextSize(10f);

        XAxis xAxis = mLine.getXAxis();
        xAxis.enableGridDashedLine(10f, 10f, 0f);
        xAxis.setTextColor(getResources().getColor(R.color.colorTextLight));
        xAxis.setValueFormatter(new MyXAxisValueFormatter());

        YAxis leftAxis = mLine.getAxisLeft();
        leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setDrawZeroLine(true);
        leftAxis.setTextColor(getResources().getColor(R.color.colorTextLight));

        // limit lines are drawn behind data (and not on top)
        leftAxis.setDrawLimitLinesBehindData(true);

        mLine.getAxisRight().setEnabled(false);

        // add data
        setLineData(-1);

        mLine.animateX(2500);

    }

    private void setPieData() {

        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        Budget budget = BudgetManager.getInstance().get(budgetIndex);
        ArrayList<Category> categories = budget.getCategories();
        double limit = budget.getBudgetLimit();

        int nonEmptyCategories = 0;
        double totalSpent = 0.0;

        for (Category category : categories) {
            amountSpent = getSpentInPeriod(startDate, endDate, category.getCategoryName());
            totalSpent += amountSpent;
            if (amountSpent > 0.0) ++nonEmptyCategories;
        }

        if (totalSpent < limit) {
            for (Category category : categories) {
                amountSpent = getSpentInPeriod(startDate, endDate, category.getCategoryName());
                if(amountSpent > 0.0) {
                    entries.add(new PieEntry((float) (amountSpent / limit), category.getCategoryName(), category));
                }
            }
            entries.add(new PieEntry((float) ((limit - totalSpent) / limit), "Remaining"));
        } else {
            for (Category category : categories) {
                amountSpent = getSpentInPeriod(startDate, endDate, category.getCategoryName());
                if(amountSpent > 0.0) {
                    entries.add(new PieEntry((float) (amountSpent / totalSpent), category.getCategoryName(), category));
                }
            }
        }

        PieDataSet dataSet = new PieDataSet(entries, "Overall Spending");

        dataSet.setDrawIcons(false);

        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);

        Log.d("totalSpent", Double.toString(totalSpent));
        Log.d("limit", Double.toString(limit));

        if (totalSpent < limit) {
            colors.add(nonEmptyCategories, Color.GRAY);
        }

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.BLACK);
        mPie.setData(data);

        // undo all highlights
        mPie.highlightValues(null);

        mPie.invalidate();
    }

    private void setLineData(int index) {
        int count = 45;
        float range = 100;

        Budget budget = BudgetManager.getInstance().get(budgetIndex);
        ArrayList<Category> categories = budget.getCategories();
        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();

        if (index == -1) {
            for (int i = 0; i < categories.size(); i++) {
                ArrayList<Entry> values = new ArrayList<Entry>();
                Category category = categories.get(i);
                Settings settings = category.getSettings();
                Database db = new Database(getBaseContext());

                //Get period info
                ArrayList<CategoryPeriod> periods = db.GetCategoryPeriod
                        (usernameStr, category.getCategoryName(), budgetNameStr);
                CategoryPeriod current = periods.get(0);
                Date startingDate = currentPeriod.getStartDate();
                Date endingDate = currentPeriod.getEndDate();
                ArrayList<Transaction> transactions = db.GetTransactionListForDate(startingDate, endingDate,
                        usernameStr, budgetNameStr, category.getCategoryName());
                Collections.sort(transactions);

                if (transactions.size() > 0) {
                    Date date = transactions.get(0).getTransactionDate();
                    float val = 0;
                    for (int j = transactions.size() - 1; j >= 0; j--) {
                        val += transactions.get(j).getPrice();
                        values.add(new Entry((float) transactions.get(j).getTransactionDate().getTime(), val));
                    }

                    LineDataSet set = new LineDataSet(values, category.getCategoryName());

                    set.setDrawIcons(false);
                    set.setColor(colors.get(i));
                    set.setCircleColor(colors.get(i));
                    set.setLineWidth(1f);
                    set.setCircleRadius(3f);
                    set.setDrawCircleHole(false);
                    set.setValueTextSize(9f);
                    set.setDrawFilled(true);
                    set.setFormLineWidth(1f);
                    set.setFormSize(15.f);
                    set.setFillAlpha(0);
                    set.setValueTextColor(getResources().getColor(R.color.colorTextLight));
                    dataSets.add(set); // add the datasets
                }
            }
        } else {
            ArrayList<Entry> values = new ArrayList<Entry>();
            Category category = categories.get(index);
            Settings settings = category.getSettings();
            Database db = new Database(getBaseContext());
            ArrayList<Transaction> transactions = db.GetTransactionListForDate(startDate, endDate,
                    usernameStr, budgetNameStr, categoryNameStr);
            Collections.sort(transactions);

            if (transactions.size() > 0) {
                Date date = transactions.get(0).getTransactionDate();
                float val = 0;
                for (int j = transactions.size() - 1; j >= 0; j--) {
                    val += transactions.get(j).getPrice();
                    values.add(new Entry((float) transactions.get(j).getTransactionDate().getTime(),
                            (float) val));
                }

                LineDataSet set = new LineDataSet(values, category.getCategoryName());

                set.setDrawIcons(false);
                set.setColor(colors.get(index));
                set.setCircleColor(colors.get(index));
                set.setLineWidth(1f);
                set.setCircleRadius(3f);
                set.setDrawCircleHole(false);
                set.setValueTextSize(9f);
                set.setDrawFilled(true);
                set.setFormLineWidth(1f);
                set.setFormSize(15.f);
                set.setFillAlpha(0);

                dataSets.add(set);
            }
        }
        // create a data object with the datasets
        LineData data = new LineData(dataSets);

        // set data
        mLine.setData(data);
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        Log.d("Value selected", currentPeriod.toString());

        Object o = e.getData();
        if (o != null) {
            Category category = (Category) o;
            Budget budget = BudgetManager.getInstance().get(budgetIndex);
            mLine.invalidate();

            categoryChosen = budget.findCategory(category);

            category = BudgetManager.getInstance().get(budgetIndex).getCategory(categoryChosen);
            //Get period info
            categoryNameStr = category.getCategoryName();
            periodList = db.GetCategoryPeriod(usernameStr, categoryNameStr, budgetNameStr);
            Collections.sort(periodList);
            currentPeriod = periodList.get(0);
            startDate = currentPeriod.getStartDate();
            endDate = currentPeriod.getEndDate();

            periodButton.setTextColor(getResources().getColor(R.color.colorTextLight));
            periodButton.setVisibility(View.VISIBLE);
            setLineData(categoryChosen);
        }
        Log.d("New period", currentPeriod.toString());
    }

    @Override
    public void onNothingSelected() {
        Log.d("Nothing selected", currentPeriod.toString());
        mLine.invalidate();
        categoryChosen = -1;
        //periodButton.setTextColor(getResources().getColor(R.color.colorAccent));
        periodButton.setVisibility(View.GONE);
        setLineData(categoryChosen);
    }
    //Calculate how much was spent in a particular category between dates
    private double getSpentInPeriod(Date start, Date end, String categoryName) {
        ArrayList<Transaction> transactionsInPeriod = db.GetTransactionListForDate
                        (start, end, usernameStr, budgetNameStr, categoryName);
        double result = 0;
        for (int i = 0; i < transactionsInPeriod.size(); ++i) {
            result += transactionsInPeriod.get(i).getPrice();
        }
        return result;
    }

    public class MyXAxisValueFormatter implements IAxisValueFormatter {

        public MyXAxisValueFormatter() {
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            // "value" represents the position of the label on the axis (x or y)
            Date date = new Date((long) value);
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd");
            return dateFormat.format(date);
        }
    }
}
