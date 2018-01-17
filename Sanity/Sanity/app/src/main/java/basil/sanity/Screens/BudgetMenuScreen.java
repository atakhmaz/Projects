package basil.sanity.Screens;

import android.app.ListActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import basil.sanity.Controllers.BudgetManager;
import basil.sanity.Controllers.Category;
import basil.sanity.Controllers.CategoryPeriod;
import basil.sanity.Controllers.ContextSingleton;
import basil.sanity.Controllers.Budget;
import basil.sanity.Controllers.Notification;
import basil.sanity.Controllers.Settings;
import basil.sanity.Controllers.Transaction;
import basil.sanity.R;
import basil.sanity.Util.NumberTextWatcher;
import basil.sanity.Util.ProgressBarUpdate;

public class BudgetMenuScreen extends ListActivity {
    //========================================================================================================================
    //MEMBER VARIABLES
    //========================================================================================================================
    public static final String EXTRA_USER = "BudgetMenuScreen.userExtra";

    //The Budget in the main menu
    private int budgetIndex;
    private Budget budget;
    private String loggedInUser;

    //The adapter that contains all the budgets
    static BudgetAdapter budgetList;

    //Various Buttons
    Button analyticsOrDelete;
    Button cancelDelete;
    //ImageButton overallLimitSettingsButton;
    ImageButton addCategoryButton;
    LinearLayout buttonLayout;
    
    //Add category widgets
    //For adding category
    TextView thresholdProgress;
    double budgetThresholdDouble;
    //For adding transaction
    int amountMultiplier;

    //Toggle button between analytics and delete
    boolean isAnalytics;
    // A list to delete multiple category positions
    ArrayList<String> deleteList;

    public static View mainMenuView;
    public static View contextView;
    //========================================================================================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ContextSingleton contextSingleton = new ContextSingleton(getBaseContext());
        //Keyboard doesn't open when go to this screen
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_budget_menu_screen);


        //Initialize cancel delete button
        ContextThemeWrapper newContext = new ContextThemeWrapper(this, R.style.MyStyle);
        cancelDelete = new Button(newContext);
        cancelDelete.setText(getResources().getString(R.string.main_menu_cancel_label));
        //Change button appearance to match logout button
        cancelDelete.setTextColor(getResources().getColor(R.color.colorAccent));
        cancelDelete.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT,1.0f);
        params.setMargins(1, 1, 1, 2);
        cancelDelete.setLayoutParams(params);

        buttonLayout = (LinearLayout) findViewById(R.id.budget_layout_buttons);

        //Initialize analytics button
        analyticsOrDelete = new Button(this);
        //Change button appearance to match logout button
        analyticsOrDelete.setTextColor(getResources().getColor(R.color.colorTextLight));
        analyticsOrDelete.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        analyticsOrDelete.setLayoutParams(params);
        analyticsOrDelete.setText(getResources().getString(R.string.budget_menu_analytics_label));
        buttonLayout.addView(analyticsOrDelete);
        
        //Get the logged in user to create budgetmanager
        loggedInUser = getIntent().getStringExtra(EXTRA_USER);
        budgetIndex = getIntent().getIntExtra("BUDGET_ID", 0);
        Log.d("The budget index", Integer.toString(budgetIndex));
        //Get the category manager instance (Should be initialized in login/signup)
        budget = BudgetManager.getInstance().get(budgetIndex);
        Log.d("Budget username", budget.getUsername());

        ((TextView) findViewById(R.id.overall_budget_text)).setText(budget.getBudgetName());

        //Initialize deleteList
        deleteList = new ArrayList<String>();

        mainMenuView = this.getWindow().getDecorView();
        //Update overall category progress bar size
        ProgressBarUpdate.updateProgressBar(mainMenuView,
                R.id.overall_budget_progress,
                budget.getBudgetPercentageSpent(),
                budget.getBudgetSpent(),
                budget.getBudgetLimit());

//        // Initialize overall category settings button
//        overallLimitSettingsButton = (ImageButton) findViewById(R.id.overall_settings_button);
//        // Listener for the overall category settings button
//        overallLimitSettingsButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //Create a new intent to go to SettingsScreen when login button is clicked
//                Intent i = new Intent(BudgetMenuScreen.this, SettingsScreen.class);
//                i.putExtra("BUDGET_INDEX", budgetIndex);
//                i.putExtra("CATEGORY_INDEX", -1);
//                startActivity(i);
//            }
//        });

        // Initialize add category button
        addCategoryButton = (ImageButton) findViewById(R.id.add_budget_button);
        // Listener for the add category button
        addCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open a popup to add a new category
                createPopup(0, 3, null);
            }
        });

        // Initialize analytics button
        // Defaults to analytics, changes to delete when user longclicks category
        isAnalytics = true;
        // Listener for the analytics button
        analyticsOrDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Function as view analytics
                if (isAnalytics) {
                    //Create a new intent to go to AnalyticsScreen when login button is clicked
                    Intent i = new Intent(BudgetMenuScreen.this, AnalyticsScreen.class);
                    i.putExtra("BUDGET_ID", budgetIndex);
                    startActivity(i);
                } else {
                    //Other function is delete category
                    createPopup(0, 2,v);
                }
            }
        });
        //Listener for canceling delete selected
        cancelDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Empty the list
                deleteList.clear();
                //Change button from delete back to logout
                isAnalytics = true;
                analyticsOrDelete.setText(getResources().getString(R.string.budget_menu_analytics_label));
                analyticsOrDelete.setTextColor(getResources().getColor(R.color.colorTextLight));

                //Update listview
                budgetList.notifyDataSetChanged();
                SearchScreen.updateCategoryList();
                SearchScreen.updateBudgetList();
                SearchScreen.updateTransactionList();

                //Remove cancel button
                buttonLayout.removeView(cancelDelete);
            }
        });

        //Dynamically adding budgets to main menu, this should always be at the end
        Log.d("Adding the adapter", "a");
        budgetList = new BudgetAdapter();
        setListAdapter(budgetList);
    }


    //========================================================================================================================
    //HELPER FUNCTIONS
    //========================================================================================================================
    //Create a popup window for adding a transaction to a specific category
    void createPopup(final int position, final int windowType, final View view){
        // get a reference to the already created main layout
        LinearLayout mainLayout = (LinearLayout) findViewById(R.id.activity_budget_menu);

        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        final View popupView = inflater.inflate(R.layout.popup_window, null);

        //Add a new transaction
        if(windowType == 1) {
            ((TextView) popupView.findViewById(R.id.popup_window_id)).setText(
                    "Add A Transaction To " + budget.getCategoryName(position));
            //Add a text field to get transaction amount
            LinearLayout inputField = (LinearLayout) popupView.findViewById(R.id.popup_window_contents);
            getLayoutInflater().inflate(R.layout.transaction_input, inputField);
            //Check if regular transaction or refund
            RadioGroup transactionType= (RadioGroup) popupView.findViewById(R.id.radio_group_type);

            EditText transactionAmount = (EditText) popupView.findViewById(R.id.transaction_amount);
            transactionAmount.addTextChangedListener(new NumberTextWatcher(transactionAmount));

            //Initialize
            amountMultiplier = 1;
            transactionType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                    switch (checkedId) {
                        case R.id.radio_transaction:
                            amountMultiplier = 1;
                            ((RadioButton) popupView.findViewById(R.id.radio_transaction)).setTextColor(getResources().getColor(R.color.colorAccent));
                            ((RadioButton) popupView.findViewById(R.id.radio_refund)).setTextColor(getResources().getColor(R.color.colorTextLight));
                            break;
                        case R.id.radio_refund:
                            amountMultiplier = -1;
                            ((RadioButton) popupView.findViewById(R.id.radio_refund)).setTextColor(getResources().getColor(R.color.colorAccent));
                            ((RadioButton) popupView.findViewById(R.id.radio_transaction)).setTextColor(getResources().getColor(R.color.colorTextLight));
                            break;
                    }
                }
            });
            DatePicker transactionDatePicker = (DatePicker) popupView.findViewById(R.id.add_transaction_date);
            Date today = new Date();
            transactionDatePicker.setMaxDate(today.getTime());
        }
        //Remove a category
        else if (windowType == 2){
            int deleteNum = deleteList.size();
            String budgetStr = "categories";
            if (deleteNum == 1) {
                budgetStr = "category";
            }
            String message = String.format(getResources().getString(R.string.main_menu_confirm_delete), deleteNum, budgetStr);
            ((TextView) popupView.findViewById(R.id.popup_window_id)).setText(message);
        }
        //Add a new category
        else if (windowType == 3){
            ((TextView) popupView.findViewById(R.id.popup_window_id)).setText(R.string.add_category_label);
            LinearLayout inputField = (LinearLayout) popupView.findViewById(R.id.popup_window_contents);
            getLayoutInflater().inflate(R.layout.add_category_input, inputField);

            EditText budgetLimit = (EditText) popupView.findViewById(R.id.add_budget_limit);
            budgetLimit.addTextChangedListener(new NumberTextWatcher(budgetLimit));

            DatePicker endDate = (DatePicker) popupView.findViewById(R.id.add_budget_end_date);
            Date today = new Date();
            endDate.setMinDate(today.getTime());
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);
            endDate.updateDate(year, month+1, day);

            //thresholdProgress declared in main class
            thresholdProgress = (TextView) popupView.findViewById(R.id.threshold_progress);
            budgetThresholdDouble = 0.75;
            SeekBar thresholdBar = (SeekBar) popupView.findViewById(R.id.threshold_seekbar);
            thresholdBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    //Add 1 to progress to prevent 0
                    budgetThresholdDouble = (progress+1) / 100.0;
                    thresholdProgress.setText(Integer.toString(progress+1)+"%");
                }
                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }
                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
            RadioGroup frequencyGroup = (RadioGroup)popupView.findViewById(R.id.radio_group_add_freq);
            final RadioButton radioDay = (RadioButton) popupView.findViewById(R.id.radio_add_day);
            final RadioButton radioWeek = (RadioButton) popupView.findViewById(R.id.radio_add_week);
            //Todo make the correct radio button checked
            frequencyGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    switch (checkedId) {
                        case R.id.radio_add_day:
                            radioDay.setTextColor(getResources().getColor(R.color.colorAccent));
                            radioWeek.setTextColor(getResources().getColor(R.color.colorTextLight));
                            break;
                        case R.id.radio_add_week:
                            radioWeek.setTextColor(getResources().getColor(R.color.colorAccent));
                            radioDay.setTextColor(getResources().getColor(R.color.colorTextLight));
                            break;
                    }
                }
            });
        }


        //Get the popup window buttons
        ImageButton closePopupButton = (ImageButton) popupView.findViewById(R.id.close_popup_button);
        Button cancelActionButton = (Button) popupView.findViewById(R.id.cancel_action_button);
        Button completeActionButton = (Button) popupView.findViewById(R.id.complete_action_button);

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // show the popup window
        popupWindow.showAtLocation(mainLayout, Gravity.CENTER, 0, 0);

        //If the user clicks the close popup button at the top right of the popup, the popup closes
        closePopupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        //If the user clicks the cancel button at the bottom right of the popup, the popup closes
        cancelActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Cleanup if delete canceled
                if (windowType == 2) {
                    //Empty the list
                    deleteList.clear();
                    analyticsOrDelete.setText(getResources().getString(R.string.budget_menu_analytics_label));
                    analyticsOrDelete.setTextColor(getResources().getColor(R.color.colorTextLight));
                    //logoutButton.setText(getResources().getString(R.string.main_menu_logout_label));
                    //logoutButton.setTextColor(getResources().getColor(R.color.colorTextLight));
                    isAnalytics = true;
                    buttonLayout.removeView(cancelDelete);
                    budgetList.notifyDataSetChanged();
                    SearchScreen.updateCategoryList();
                    SearchScreen.updateBudgetList();
                    SearchScreen.updateTransactionList();
                }
                popupWindow.dismiss();
            }
        });

        //If the user clicks the button to the left of the cancel button, the correct action is done
        completeActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Add a transaction to the specific category
                if(windowType == 1) {

                    // Window type 1 (new transaction)
                    TextInputLayout tilTransactionItemName = (TextInputLayout)
                            popupView.findViewById(R.id.til_transaction_item_name);
                    TextInputLayout tilTransactionAmount = (TextInputLayout)
                            popupView.findViewById(R.id.til_transaction_amount);
                    TextInputLayout tilTransactionMemo = (TextInputLayout)
                            popupView.findViewById(R.id.til_transaction_memo);

                    tilTransactionItemName.setError(null);
                    tilTransactionAmount.setError(null);
                    tilTransactionMemo.setError(null);

                    //Get all the data from the user input fields
                    EditText itemName = (EditText) popupView.findViewById(R.id.transaction_item_name);
                    EditText transactionAmount = (EditText) popupView.findViewById(R.id.transaction_amount);
                    EditText memo = (EditText) popupView.findViewById(R.id.transaction_memo);
                    //Get the date
                    DatePicker transactionDatePicker = (DatePicker) popupView.findViewById(R.id.add_transaction_date);
                    Calendar cal = Calendar.getInstance();
                    cal.set(Calendar.YEAR, transactionDatePicker.getYear());
                    cal.set(Calendar.MONTH, transactionDatePicker.getMonth());
                    cal.set(Calendar.DAY_OF_MONTH, transactionDatePicker.getDayOfMonth());
                    Date transactionDate = cal.getTime();

                    final CheckBox recurring = (CheckBox) popupView.findViewById(R.id.recurring_transaction);

                    //Convert to correct type
                    String itemNameString = itemName.getText().toString();
                    double transactionAmountDouble = -1.0;
                    try {
                        transactionAmountDouble = Double.parseDouble(transactionAmount.getText().toString().substring(1).replace(",",""));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    String memoString = memo.getText().toString();

                    boolean hasError = false;

                    if (itemNameString.isEmpty()) {
                        // Item name empty
                        tilTransactionItemName.setError(getResources().getString(R.string.empty_item_name));
                        hasError = true;
                    }
                    if (transactionAmountDouble < 0.0) {
                        // Transaction amount empty
                        tilTransactionAmount.setError(getResources().getString(R.string.empty_amount));
                        hasError = true;
                    }
                    if(!hasError){
                        //If refund, amountMultiplier will be -1
                        transactionAmountDouble *= amountMultiplier;

                        // If check box is selected, add recurring transaction
                        if (recurring.isChecked()) {
                            Notification nc = new Notification();
                            nc.setRepeatingTransaction(BudgetManager.getInstance().getUsername(), budget.getBudgetName(),
                                    budget.getCategoryName(position), itemNameString, memoString, transactionAmountDouble);
                        }

                        //Add the transaction to the specific category
                        boolean isFirstTimeOver = budget.addTransaction(new Transaction(itemNameString, transactionDate, transactionAmountDouble, memoString, budget.getBudgetName(), budget.getCategoryName(position)), position);
                        if (isFirstTimeOver) {
                            String message = budget.getCategoryName(position) + getResources().getString(R.string.warning_over_budget);
                            Toast.makeText(getApplicationContext(),
                                     message, Toast.LENGTH_SHORT).show();
                        }

                        //update the specific category progress
                        ProgressBarUpdate.updateProgressBar(view, R.id.budget_progress,
                                budget.getPercentageSpent(position),
                                budget.getCategory(position).getAmountSpent(),
                                budget.getCategoryLimit(position));

                        //Dismiss the popup
                        popupWindow.dismiss();
                    }
                }
                //Remove the specific category
                else if (windowType == 2) {
                    //Delete all
                    for (int i =0; i< deleteList.size(); ++i) {
                        budget.removeCategory(deleteList.get(i));
                    }
                    //Empty the list
                    deleteList.clear();
                    analyticsOrDelete.setText(getResources().getString(R.string.budget_menu_analytics_label));
                    analyticsOrDelete.setTextColor(getResources().getColor(R.color.colorTextLight));
                    //logoutButton.setText(getResources().getString(R.string.main_menu_logout_label));
                    //logoutButton.setTextColor(getResources().getColor(R.color.colorTextLight));
                    isAnalytics = true;
                    buttonLayout.removeView(cancelDelete);
                    popupWindow.dismiss();
                }
                //Add a new category
                else if (windowType == 3) {

                    // Window type 3 (new category)
                    TextInputLayout tilAddBudgetName = (TextInputLayout)
                            popupView.findViewById(R.id.til_add_budget_name);
                    TextInputLayout tilAddBudgetFrequencyNumber = (TextInputLayout)
                            popupView.findViewById(R.id.til_add_budget_frequency_number);
                    TextInputLayout tilAddBudgetLimit = (TextInputLayout)
                            popupView.findViewById(R.id.til_add_budget_limit);
                    tilAddBudgetName.setError(null);
                    tilAddBudgetFrequencyNumber.setError(null);
                    tilAddBudgetLimit.setError(null);

                    //Get all the data from the user input fields
                    EditText categoryName = (EditText) popupView.findViewById(R.id.add_budget_name);
                    DatePicker endDate = (DatePicker) popupView.findViewById(R.id.add_budget_end_date);
                    EditText frequencyValueText = (EditText) popupView.findViewById(R.id.add_budget_frequency_number);
                    EditText budgetLimit = (EditText) popupView.findViewById(R.id.add_budget_limit);


                    RadioGroup frequencyGroup = (RadioGroup) popupView.findViewById(R.id.radio_group_add_freq);
                    //Get which radio button checked
                    int radioButtonID = frequencyGroup.getCheckedRadioButtonId();
                    View radioButton = frequencyGroup.findViewById(radioButtonID);
                    int radioIndex = frequencyGroup.indexOfChild(radioButton);
                    String frequencyTypeString;
                    if (radioIndex == 0) {
                        frequencyTypeString = "Day";
                    } else {
                        frequencyTypeString = "Week";
                    }

                    //Convert to correct type
                    String categoryNameString = categoryName.getText().toString();

                    //double budgetThresholdDouble = -1.0;
                    double budgetLimitDouble = -1.0;
                    int frequencyValueInt = 0;
                    try {
                        //budgetThresholdDouble = Integer.parseInt(budgetThreshold.getText().toString()) / 100.0;
                        budgetLimitDouble = Double.parseDouble(budgetLimit.getText().toString().substring(1).replace(",",""));
                        frequencyValueInt = Integer.parseInt(frequencyValueText.getText().toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    Calendar cal = Calendar.getInstance();
                    cal.set(Calendar.YEAR, endDate.getYear());
                    cal.set(Calendar.MONTH, endDate.getMonth());
                    cal.set(Calendar.DAY_OF_MONTH, endDate.getDayOfMonth());
                    Date endDateDate = cal.getTime();
                    Date today = new Date();

                    boolean hasError = false;

                    if (categoryNameString.isEmpty()) {
                        // Category name empty
                        tilAddBudgetName.setError(getResources().getString(R.string.budget_name_empty));
                        hasError = true;
                    }
                    if (endDateDate.before(today)) {
                        // Date mismatch
                        tilAddBudgetLimit.setError(getResources().getString(R.string.date_mismatch_2));
                        hasError = true;
                    }
                    if (frequencyValueText.getText().toString().isEmpty()) {
                        // No frequency
                        tilAddBudgetFrequencyNumber.setError(getResources().getString(R.string.empty_frequency));
                        hasError = true;
                    }
                    if(frequencyValueInt <= 0){
                        // Negative frequency
                        tilAddBudgetFrequencyNumber.setError(getResources().getString(R.string.non_pos_frequency));
                        hasError = true;
                    }
                    if (budgetLimitDouble <= 0) {
                        // Negative category limit
                        tilAddBudgetLimit.setError(getResources().getString(R.string.limit_too_low));
                        hasError = true;
                    }
                    if (!hasError) {
                        //Construct a new category

                        // Date endDateDate = new Date(endDate.getYear(), endDate.getMonth(), endDate.getDayOfMonth());
                        CategoryPeriod categoryPeriod = new CategoryPeriod(endDateDate);
                        Settings budgetSettings = new Settings(budgetThresholdDouble, categoryPeriod, frequencyTypeString, frequencyValueInt);
                        Category newCategory = new Category(categoryNameString, budgetLimitDouble, budgetSettings, budget.getBudgetName());

                        boolean budgetExists = budget.checkIfCategoryExists(categoryNameString);
                        if(!budgetExists){
                            //Add the category to the category manager
                            budget.addCategory(newCategory);
                            //Dismiss the popup
                            popupWindow.dismiss();
                        }
                        else{
                            // Category name exists
                            tilAddBudgetName.setError(getResources().getString(R.string.budget_exists));
                        }
                    }
                }


                //update the overall category progress
                ProgressBarUpdate.updateProgressBar(mainMenuView,
                        R.id.overall_budget_progress,
                        budget.getBudgetPercentageSpent(),
                        budget.getBudgetSpent(),
                        budget.getBudgetLimit());
                //Notify the adapter that the list has changed
                budgetList.notifyDataSetChanged();
                MainMenuScreen.updateBudgetList();
                SearchScreen.updateCategoryList();
                SearchScreen.updateBudgetList();
                SearchScreen.updateTransactionList();
            }
        });
    }
    //========================================================================================================================

    public static void updateBudgetList(){
        if(budgetList != null) {
            budgetList.notifyDataSetChanged();
        }
    }















    //========================================================================================================================
    //Custom adapter that loads in the budget items
    //========================================================================================================================
    private class BudgetAdapter extends BaseAdapter {
        @Override
        public int getCount() { Log.d("The Budget List Size: ", Integer.toString(budget.getCategoryListSize())); return budget.getCategoryListSize(); }

        @Override
        public String getItem(int position) { return budget.getCategoryName(position); }

        @Override
        public long getItemId(int position) { return position; }

        //This constructs the category object in the menu
        @Override
        public View getView(final int position, View convertView, ViewGroup container) {
            //Not sure what this does
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.budget_list_item, container, false);
            }

            //Set the colors to alternate
            if(position % 2 == 0)
            {
                convertView.setBackgroundColor(getResources().getColor(R.color.listViewColor1));
            }
            else
            {
                convertView.setBackgroundColor(getResources().getColor(R.color.listViewColor2));
            }

            //Update the category name
            ((TextView) convertView.findViewById(android.R.id.text1)).setText(getItem(position));

            //Update progress bar size
            ProgressBarUpdate.updateProgressBar(convertView, R.id.budget_progress,
                    budget.getPercentageSpent(position),
                    budget.getCategory(position).getAmountSpent(),
                    budget.getCategoryLimit(position));

            //These are the buttons, we need to add popups when you press them to do different things
            ImageButton addTransactionButton = (ImageButton) convertView.findViewById(R.id.transaction_button);
            ImageButton settingsButton = (ImageButton) convertView.findViewById(R.id.settings_button);
            //ImageButton deleteBudgetButton = (ImageButton) convertView.findViewById(R.id.delete_button);

            final RelativeLayout budgetRow = (RelativeLayout) convertView.findViewById(R.id.budget_row);

            //This is the same view but it needs to be final to be sent into the button event listener
            final View convertViewFinal = convertView;

            addTransactionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Create a popup
                    createPopup(position, 1, convertViewFinal);
                }
            });

            budgetRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(BudgetMenuScreen.this, TransactionsScreen.class);
                    i.putExtra("BUDGET_INDEX", budgetIndex);
                    i.putExtra("CATEGORY_INDEX", position);
                    startActivity(i);
                }
            });
            //Long click on category deletes it
            budgetRow.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    //Search the deletelist
                    int i =0;
                    boolean isFound = false;
                    while (!isFound && i<deleteList.size()){
                        //Already in deletelist, so unselect it
                        if (deleteList.get(i) == budget.getCategoryName(position)) {
                            isFound = true;
                            deleteList.remove(i);
                            if (position %2 == 0) {
                                budgetRow.setBackgroundColor(getResources().getColor(R.color.listViewColor1));
                            } else {
                                budgetRow.setBackgroundColor(getResources().getColor(R.color.listViewColor2));
                            }
                        }
                        ++i;
                    }
                    // New position clicked
                    if (!isFound) {
                        budgetRow.setBackgroundResource(R.drawable.border_red);
                        deleteList.add(budget.getCategoryName(position));
                    }
                    if (deleteList.size() > 0) {
                        if (isAnalytics) {
                            //Change analytics to delete
                            isAnalytics = false;
                            //Create cancel button
                            buttonLayout.addView(cancelDelete);
                        }

                        analyticsOrDelete.setText(getResources().getString(R.string.main_menu_delete_label));
                        analyticsOrDelete.setTextColor(getResources().getColor(R.color.colorAccent));
                    } else {
                        // List empty, change delete to analytics
                        isAnalytics = true;
                        analyticsOrDelete.setText(getResources().getString(R.string.budget_menu_analytics_label));
                        analyticsOrDelete.setTextColor(getResources().getColor(R.color.colorTextLight));
                        //Remove cancel button
                        buttonLayout.removeView(cancelDelete);
                    }
                    //True makes sure doesn't counnt as short click
                    return true;
                }
            });

            settingsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Create a new intent to go to Category Settings when settings button is clicked
                    contextView = convertViewFinal;
                    Intent i = new Intent(BudgetMenuScreen.this, SettingsScreen.class);
                    i.putExtra("BUDGET_INDEX", budgetIndex);
                    i.putExtra("CATEGORY_INDEX", position);
                    startActivity(i);
                }
            });
            

            return convertView;
        }
    }
    //========================================================================================================================
}

