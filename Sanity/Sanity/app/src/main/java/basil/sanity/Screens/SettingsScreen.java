package basil.sanity.Screens;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Date;

import basil.sanity.Controllers.Budget;
import basil.sanity.Controllers.BudgetManager;
import basil.sanity.Controllers.Category;
import basil.sanity.Controllers.CategoryPeriod;
import basil.sanity.Controllers.Notification;
import basil.sanity.Controllers.Settings;
import basil.sanity.Database.Database;
import basil.sanity.R;
import basil.sanity.Util.NumberTextWatcher;
import basil.sanity.Util.ProgressBarUpdate;

public class SettingsScreen extends AppCompatActivity {
    //========================================================================================================================
    //MEMBER VARIABLES
    //========================================================================================================================
    // The data
    Budget budget;
    Category category;
    int budgetIndex;
    int categoryIndex;

    // The UI Elements
    TextView categoryNameText;
    DatePicker startDatePicker;
    DatePicker endDatePicker;
    EditText limitText;
    TextView thresholdProgress;
    SeekBar thresholdSeekBar;
    EditText frequencyValueText;
    RadioGroup frequencyGroup;
    Button confirmButton;

    // Data to populate UI Elements
    String categoryName;
    String categoryNameField;
    Date startDate;
    Date endDate;
    double threshold;
    double limit;
    int frequencyValue;
    String frequencyType;
    int oldFrequencyValue;
    String oldFrequencyType;
    Date oldStartDate;
    Date oldEndDate;

    // The screen view
    public static View settingsScreenView;
    //========================================================================================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_screen);

        //Keyboard doesn't open when go to this screen
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        settingsScreenView = this.getWindow().getDecorView();

        budgetIndex = getIntent().getIntExtra("BUDGET_INDEX", -1);
        categoryIndex = getIntent().getIntExtra("CATEGORY_INDEX", -1);

        budget = BudgetManager.getInstance().get(budgetIndex);
//        if(categoryIndex != -1) {
            category = budget.getCategory(categoryIndex);
//        }

        //Get the all views
        categoryNameText = (TextView) findViewById(R.id.settings_screen_budget_name);
        startDatePicker = (DatePicker) findViewById(R.id.settings_screen_budget_start_date);
        endDatePicker = (DatePicker) findViewById(R.id.settings_screen_budget_end_date);
        thresholdProgress = (TextView) findViewById(R.id.settings_screen_threshold_progress);
        thresholdSeekBar = (SeekBar) findViewById(R.id.settings_screen_threshold_seekbar);
        limitText = (EditText) findViewById(R.id.settings_screen_budget_limit);
        limitText.addTextChangedListener(new NumberTextWatcher(limitText));
        confirmButton = (Button) findViewById(R.id.settings_screen_confirm_button);
        frequencyValueText = (EditText) findViewById(R.id.frequency_number);
        frequencyGroup = (RadioGroup) findViewById(R.id.radio_group_set_freq);

        //If the category is the overall category
//        if(categoryIndex == -1){
//            categoryName = getResources().getString(R.string.overall_budget_name);
//            categoryNameField = categoryName;
//            startDate = budget.getStartDate();
//            endDate = budget.getEndDate();
//            threshold = budget.getThreshold();
//            limit = budget.getBudgetLimit();
//            frequencyValue = budget.getBudgetFrequencyValue();
//            frequencyType = budget.getBudgetFrequencyType();
//        }
//        //If the category is a regular category
//        else {
            categoryName = category.getCategoryName();
            categoryNameField = categoryName;
            startDate = category.getStartDate();
            endDate = category.getEndDate();
            threshold = category.getThreshold();
            limit = category.getCategoryLimit();
            frequencyValue = category.getFrequencyValue();
            frequencyType = category.getGetFrequencyType();
//        }

        oldFrequencyType = frequencyType;
        oldFrequencyValue = frequencyValue;
        oldStartDate = startDate;
        Date today = new Date();
        endDatePicker.setMinDate(today.getTime());
        startDatePicker.setMaxDate(oldStartDate.getTime());
        oldEndDate = endDate;

        thresholdSeekBar.setProgress((int)(threshold*100)-1);
        thresholdProgress.setText(Integer.toString((int)(threshold*100))+"%");
        //Set the values on the screen
        categoryNameText.setText(categoryNameField);
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        startDatePicker.updateDate(year, month, day);
        cal.setTime(endDate);
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
        day = cal.get(Calendar.DAY_OF_MONTH);
        endDatePicker.updateDate(year, month, day);
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        limitText.setText(formatter.format(limit));
        frequencyValueText.setText(Integer.toString(frequencyValue));


        //thresholdProgress declared in main class

        thresholdSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //Add 1 to progress to prevent 0
                threshold = (progress+1) / 100.0;
                thresholdProgress.setText(Integer.toString(progress+1)+"%");
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        //Change color of day / week text
        final RadioButton radioDay = (RadioButton) findViewById(R.id.radio_set_day);
        final RadioButton radioWeek = (RadioButton) findViewById(R.id.radio_set_week);
        if (frequencyType.equals("Day")) {
            //Show in settings as "day"
            radioDay.setTextColor(getResources().getColor(R.color.colorAccent));
            radioWeek.setTextColor(getResources().getColor(R.color.colorTextLight));
            radioDay.setChecked(true);
        } else {
            //Show as week
            radioWeek.setTextColor(getResources().getColor(R.color.colorAccent));
            radioDay.setTextColor(getResources().getColor(R.color.colorTextLight));
            radioWeek.setChecked(true);
        }
        frequencyGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_set_day:
                        radioDay.setTextColor(getResources().getColor(R.color.colorAccent));
                        radioWeek.setTextColor(getResources().getColor(R.color.colorTextLight));
                        break;
                    case R.id.radio_set_week:
                        radioWeek.setTextColor(getResources().getColor(R.color.colorAccent));
                        radioDay.setTextColor(getResources().getColor(R.color.colorTextLight));
                        break;
                }
            }
        });

        //The confirm buttons is clicked
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get the new values
                Calendar cal = Calendar.getInstance();

                cal.set(Calendar.YEAR, startDatePicker.getYear());
                cal.set(Calendar.MONTH, startDatePicker.getMonth());
                cal.set(Calendar.DAY_OF_MONTH, startDatePicker.getDayOfMonth());
                cal.set(Calendar.MILLISECOND, 0);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.HOUR_OF_DAY, 0);
                Date newStartDate = cal.getTime();

                cal.set(Calendar.YEAR, endDatePicker.getYear());
                cal.set(Calendar.MONTH, endDatePicker.getMonth());
                cal.set(Calendar.DAY_OF_MONTH, endDatePicker.getDayOfMonth());
                Date newEndDate = cal.getTime();

                // Initiate and nullify TextInputLayouts
                TextInputLayout tilFrequencyNumber = (TextInputLayout)
                        findViewById(R.id.til_frequency_number);
                TextInputLayout tilSettingsScreenBudgetLimit = (TextInputLayout)
                        findViewById(R.id.til_settings_screen_budget_limit);
                tilFrequencyNumber.setError(null);
                tilSettingsScreenBudgetLimit.setError(null);

                int radioButtonID = frequencyGroup.getCheckedRadioButtonId();
                View radioButton = frequencyGroup.findViewById(radioButtonID);
                int radioIndex = frequencyGroup.indexOfChild(radioButton);
                String frequencyTypeString;
                if (radioIndex == 0) {
                    frequencyTypeString = "Day";
                } else {
                    frequencyTypeString = "Week";
                }

                Date today = new Date();
                cal.setTime(today);
                cal.set(Calendar.MILLISECOND, 0);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.HOUR_OF_DAY, 0);
                today = cal.getTime();

                //double newThreshold = Double.parseDouble(thresholdText.getText().toString());
                double newThreshold = threshold;
                double newLimit = 0.0;
                int frequencyValueInt = 0;
                try {
                    newLimit = Double.parseDouble(limitText.getText().toString().substring(1).replace(",",""));
                    frequencyValueInt = Integer.parseInt(frequencyValueText.getText().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Log.d("New Limit", Double.toString(newLimit));
                boolean hasError = false;
                if(newEndDate.before(today)) {
                    // Cant have the end date be before today or else you might lose some transactions
                    tilSettingsScreenBudgetLimit.setError("Cannot have the end date be before today.");
                    hasError = true;
                }
                if(newStartDate.after(oldStartDate)) {
                    // Cant have the new start date be after the old one or else you might lose some transactions
                    tilSettingsScreenBudgetLimit.setError("Cannot have the start date be after the old start date.");
                    hasError = true;
                }
                if (newEndDate.before(newStartDate)) {
                    // Date mismatch
                    tilSettingsScreenBudgetLimit.setError(getResources().getString(R.string.date_mismatch));
                    hasError = true;
                }
                if (newLimit <= 0.0) {
                    // Category limit too low
                    tilSettingsScreenBudgetLimit.setError(getResources().getString(R.string.limit_too_low));
                    hasError = true;
                }
                if (frequencyValueInt <= 0) {
                    // Non pos frequency
                    tilFrequencyNumber.setError(getResources().getString(R.string.non_pos_frequency));
                    hasError = true;
                }
                if(frequencyValueText.getText().toString().isEmpty()){
                    // Empty frequency
                    tilFrequencyNumber.setError(getResources().getString(R.string.empty_frequency));
                    hasError = true;
                }
                if(!hasError){
                    Settings newSettings = new Settings(newThreshold, new CategoryPeriod(newStartDate, newEndDate), frequencyTypeString, frequencyValueInt);

                    String username = budget.getUsername();

                    Database db = null;
                    try {
                        db = new Database();
                    } catch (Exception e) {

                    }

//                    if (categoryIndex == -1) {
//                        db.updateOverallSettings(username, newSettings);
//                        db.setOverallBudgetLimit(username, newLimit);
//                        budget.editBudgetLimit(newLimit);
//                        budget.editBudgetSetting(newSettings);
//                        ProgressBarUpdate.updateProgressBar(BudgetMenuScreen.mainMenuView,
//                                R.id.overall_budget_progress,
//                                budget.getBudgetPercentageSpent(),
//                                budget.getBudgetSpent(),
//                                budget.getBudgetLimit());
//                        //This is the overall category
//                        Notification nc = new Notification();
//                        if(oldFrequencyValue != frequencyValueInt || !oldFrequencyType.equals(frequencyTypeString)){
//                            nc.updateOverallFrequency(username,"Overall Category",frequencyValueInt,frequencyTypeString,getBaseContext());
//                        }
//                        if(!oldStartDate.equals(newStartDate) || !oldEndDate.equals(newEndDate)){
//                            nc.updateOverallBudgetDate(username, "Overall Category",newStartDate,newEndDate,getBaseContext());
//                        }
//                    } else {
                        //String categoryName = budget.getCategoryName(budgetID); // Is this needed?

                        double newOverallBudgetChange = budget.getBudgetLimit() - (category.getCategoryLimit() - newLimit);
                        db.setOverallBudgetLimit(username, newOverallBudgetChange);
                        budget.editBudgetLimit(newOverallBudgetChange);

                        db.updateSetting(newSettings, categoryName, username);
                        db.setBudgetLimit(username, categoryName, newLimit);
                        category.setCategoryLimit(newLimit);
                        category.setSettings(newSettings);
                        ProgressBarUpdate.updateProgressBar(BudgetMenuScreen.contextView,
                                R.id.budget_progress,
                                category.getPercentageSpent(),
                                category.getAmountSpent(),
                                category.getCategoryLimit());
                       ProgressBarUpdate.updateProgressBar(BudgetMenuScreen.mainMenuView,
                                R.id.overall_budget_progress,
                                budget.getBudgetPercentageSpent(),
                                budget.getBudgetSpent(),
                                budget.getBudgetLimit());
                        //This is a regular category
                        Notification nc = new Notification();

                        if(oldFrequencyValue != frequencyValueInt || !oldFrequencyType.equals(frequencyTypeString)){
                            nc.updateCategoryFrequency(username,categoryName,budget.getBudgetName(),frequencyValueInt,frequencyTypeString,getBaseContext());

                        }
                        if(!oldStartDate.equals(newStartDate) || !oldEndDate.equals(newEndDate)){
                            db.UpdateCategoryPeriod(username,categoryName,budget.getBudgetName(),oldStartDate,oldEndDate,newStartDate,newEndDate);
                            nc.updateCategoryPeriod(username, categoryName,budget.getBudgetName(),newStartDate,newEndDate,getBaseContext());

//                        }
                    }

                    MainMenuScreen.updateBudgetList();
                    SearchScreen.updateCategoryList();
                    SearchScreen.updateBudgetList();
                    SearchScreen.updateTransactionList();
                    //Close settings and go back to main menu
                    finish();
                }
            }
        });
    }
}
