package basil.sanity.Screens;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import basil.sanity.Controllers.BudgetManager;
import basil.sanity.Controllers.Reminder;
import basil.sanity.Database.Database;
import basil.sanity.R;

public class ReminderAddScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_screen);
        //Keyboard doesn't open when go to this screen
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        final BudgetManager manager = BudgetManager.getInstance();

        RadioGroup frequencyGroup = (RadioGroup) findViewById(R.id.reminder_radio_group);
        final RadioButton radioYes = (RadioButton) findViewById(R.id.radio_yes);
        final RadioButton radioNo = (RadioButton) findViewById(R.id.radio_no);
        frequencyGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_yes:
                        radioYes.setTextColor(getResources().getColor(R.color.colorAccent));
                        radioNo.setTextColor(getResources().getColor(R.color.colorTextLight));
                        break;
                    case R.id.radio_no:
                        radioNo.setTextColor(getResources().getColor(R.color.colorAccent));
                        radioYes.setTextColor(getResources().getColor(R.color.colorTextLight));
                        break;
                }
            }
        });


        DatePicker reminderDatePicker = (DatePicker) findViewById(R.id.reminder_date_picker);
        Date today = new Date();
        reminderDatePicker.setMinDate(today.getTime());

        final Spinner budgetSpinner = (Spinner) findViewById(R.id.reminder_budget_spinner);
        List<String> budgetArray =  new ArrayList<String>();
        for(int i = 0; i < manager.getBudgetListSize(); ++i){
            budgetArray.add(manager.get(i).getBudgetName());
        }

        ArrayAdapter<String> budgetSpinnerAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, budgetArray);

        budgetSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        budgetSpinner.setAdapter(budgetSpinnerAdapter);


        final Spinner categorySpinner = (Spinner) findViewById(R.id.reminder_category_spinner);

        budgetSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("Clicked first spinner", Integer.toString(position));
                List<String> spinnerArray =  new ArrayList<String>();
                for(int i = 0; i < manager.get(position).getCategoryListSize(); ++i){
                    spinnerArray.add(manager.get(position).getCategory(i).getCategoryName());
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, spinnerArray){
                    // This part is only to set the text color
                    public View getView(int position, View convertView, ViewGroup parent) {
                        View v = super.getView(position, convertView, parent);

                        ((TextView) v).setTextColor(
                                getResources().getColorStateList(R.color.colorTextLight)
                        );

                        return v;
                    }
                };

                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                categorySpinner.setAdapter(adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        Button addReminderButton = (Button) findViewById(R.id.reminder_confirm_button);



        addReminderButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 // Initialize reminder title text input layout
                 TextInputLayout tilReminderTitle = (TextInputLayout) findViewById(R.id.til_reminder_title);
                 TextInputLayout tilReminderDescription = (TextInputLayout) findViewById(R.id.til_reminder_description);
                 tilReminderTitle.setError(null);
                 tilReminderDescription.setError(null);

                 EditText reminderTitleText = (EditText) findViewById(R.id.reminder_title);

                 String reminderTitle = reminderTitleText.getText().toString();

                 boolean hasError = false;
                 Database database = null;
                 try {
                     database = new Database();
                 } catch (Exception e) {}

                 if (reminderTitle.equals("")) {
                     tilReminderTitle.setError(getResources().getString(R.string.reminder_title_empty));
                     hasError = true;
                 } else if (database.CheckReminder(manager.getUsername(), reminderTitle)) {
                     tilReminderTitle.setError(getResources().getString(R.string.reminder_title_same));
                     hasError = true;
                 }

                 DatePicker reminderDatePicker = (DatePicker) findViewById(R.id.reminder_date_picker);

                 Calendar cal = Calendar.getInstance();

                 cal.set(Calendar.YEAR, reminderDatePicker.getYear());
                 cal.set(Calendar.MONTH, reminderDatePicker.getMonth());
                 cal.set(Calendar.DAY_OF_MONTH, reminderDatePicker.getDayOfMonth());

                 Date reminderDate = cal.getTime();
                 Date today = new Date();

                 if(reminderDate.before(today))
                 {
                     tilReminderDescription.setError("Reminder date cannot be before today.");
                     hasError = true;
                 }

                 RadioGroup frequencyGroup = (RadioGroup) findViewById(R.id.reminder_radio_group);
                 //Get which radio button checked
                 int radioButtonID = frequencyGroup.getCheckedRadioButtonId();
                 View radioButton = frequencyGroup.findViewById(radioButtonID);
                 int radioIndex = frequencyGroup.indexOfChild(radioButton);
                 boolean isRepeat;
                 if (radioIndex == 0) {
                     isRepeat = true;
                 } else {
                     isRepeat = false;
                 }

                 EditText reminderEditText = (EditText) findViewById(R.id.reminder_description);

                 boolean emptyBudgetSpinner = false;
                 boolean emptyCategorySpinner = false;

                 String budgetName = "";
                 String categoryName = "";

                 if (budgetSpinner.getSelectedItem() != null) {
                     budgetName = budgetSpinner.getSelectedItem().toString();
                 } else {
                     emptyBudgetSpinner = true;
                 }

                 if (categorySpinner.getSelectedItem() != null) {
                     categoryName = categorySpinner.getSelectedItem().toString();
                 } else {
                     emptyCategorySpinner = true;
                 }

                 String reminderDescription = reminderEditText.getText().toString();

                 if(reminderDescription.isEmpty())
                 {
                     tilReminderDescription.setError("Cannot have an empty description");
                     hasError = true;
                 }

                 Log.d("Reminder date", reminderDate.toString());
                 Log.d("Budget name", budgetName);
                 Log.d("Category name", categoryName);
                 Log.d("Reminder description", reminderDescription);

                 // Set budget or category empty
                 if (emptyBudgetSpinner || emptyCategorySpinner) {
                     tilReminderDescription.setError(getResources().getString(R.string.budget_or_category_empty));
                 }

                 if (!hasError && !emptyBudgetSpinner && !emptyCategorySpinner) {
                     Reminder reminder = new Reminder(reminderTitle, getApplicationContext(), manager.getUsername(), budgetName, categoryName, reminderDescription, reminderDate, isRepeat);
                     Database db = null;
                     try
                     {
                         db = new Database();
                     }
                     catch (Exception e){}
                     db.AddReminder(reminder);
                     ReminderMenuScreen.updateReminderList();
                     finish();
                 }
             }
         });
    }
}
