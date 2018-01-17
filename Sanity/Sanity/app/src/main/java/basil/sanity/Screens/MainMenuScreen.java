package basil.sanity.Screens;

import android.content.Intent;
import android.content.res.Resources;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;

import basil.sanity.Controllers.BudgetManager;
import basil.sanity.Controllers.MyApplication;
import basil.sanity.R;
import basil.sanity.Util.ProgressBarUpdate;

import static basil.sanity.Screens.BudgetMenuScreen.EXTRA_USER;

public class MainMenuScreen extends AppCompatActivity {
    //Views
    TextView welcomeText;
    Button cancelDelete;
    Button logoutOrDelete;
    public static BudgetManagerAdapter myAdapter;
    ListView budgetListView;
    LinearLayout buttonLayout;
    SearchView search;

    ImageButton addBudgetButton;

    ImageButton helpButton;
    ImageButton reminderButton;

    public static Resources appResources;

    //Data
    String loggedInUser;
    BudgetManager budgetManager;
    ArrayList<String> deleteList;

    //Switch between create and delete
    private boolean isLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu_screen);

        appResources = getResources();
        //Keyboard doesn't open when go to this screen
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        //Get logged in user from login screen
        loggedInUser = getIntent().getStringExtra(BudgetMenuScreen.EXTRA_USER);
        //Get the budgetManager
        budgetManager = BudgetManager.getInstance();
        //budgetManager = new BudgetManager(loggedInUser);
        //Initialize deleteList
        deleteList = new ArrayList<String>();
        isLogout = true;
        //Find views
        welcomeText = (TextView) findViewById(R.id.welcome_text);
        addBudgetButton = (ImageButton) findViewById(R.id.add_budget_button);
        helpButton = (ImageButton) findViewById(R.id.help_button);
        reminderButton = (ImageButton) findViewById(R.id.reminder_button);
        budgetListView = (ListView) findViewById(R.id.main_list);
        buttonLayout = (LinearLayout) findViewById(R.id.main_layout_buttons);
        search = (SearchView) findViewById(R.id.search);

        //Initialize cancel delete button
        cancelDelete = new Button(this);
        cancelDelete.setText(getResources().getString(R.string.main_menu_cancel_label));
        //Change button appearance to match logout button
        cancelDelete.setTextColor(getResources().getColor(R.color.colorAccent));
        cancelDelete.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT,1.0f);
        params.setMargins(1, 1, 1, 2);
        cancelDelete.setLayoutParams(params);

        //Initialize logout button
        logoutOrDelete = new Button(this);
        //Change button appearance to match logout button
        logoutOrDelete.setTextColor(getResources().getColor(R.color.colorTextLight));
        logoutOrDelete.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        logoutOrDelete.setLayoutParams(params);
        logoutOrDelete.setText(getResources().getString(R.string.main_menu_logout_label));
        buttonLayout.addView(logoutOrDelete);

        //Display welcome
        String message = String.format(getResources().getString(R.string.main_menu_welcome_user), loggedInUser);
        welcomeText.setText(message);
        //Fill the listview
        myAdapter = new BudgetManagerAdapter();
        budgetListView.setAdapter(myAdapter);

        addBudgetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Testing without db
               createPopup(1);
            }
        });

        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainMenuScreen.this, HelpScreen.class);
                startActivity(i);
            }
        });

        reminderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainMenuScreen.this, ReminderMenuScreen.class);
                startActivity(i);
            }
        });

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextChange(String newText) {
                //Log.e("onQueryTextChange", "called");
                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent i = new Intent(MainMenuScreen.this, SearchScreen.class);
                i.putExtra("USERNAME", budgetManager.getUsername());
                i.putExtra("SEARCH_STRING", search.getQuery().toString());
                startActivity(i);
                return false;
            }

        });


        //Listener for logout / cancel
        logoutOrDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLogout){
                    //Save status in myApplication
                    //Update global login value
                    ((MyApplication)getApplication()).setLoginStatus(false);
                    Intent i = new Intent(MainMenuScreen.this, LoginScreen.class);
                    finish();
                    startActivity(i);
                } else {
                    createPopup(2);
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
                isLogout = true;
                logoutOrDelete.setText(getResources().getString(R.string.main_menu_logout_label));
                logoutOrDelete.setTextColor(getResources().getColor(R.color.colorTextLight));

                //Update listview
                myAdapter.notifyDataSetChanged();

                //Remove cancel button
                buttonLayout.removeView(cancelDelete);
            }
        });

    }


    public static void updateBudgetList(){
        if(myAdapter != null) {
            myAdapter.notifyDataSetChanged();
        }
    }



    void createPopup(final int windowType){
        // get a reference to the already created main layout
        LinearLayout mainLayout = (LinearLayout) findViewById(R.id.activity_main_menu);

        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        final View popupView = inflater.inflate(R.layout.popup_window, null);

        // Adding a budget
        if(windowType == 1){
            ((TextView) popupView.findViewById(R.id.popup_window_id)).setText("Add A New Budget");
            LinearLayout inputField = (LinearLayout) popupView.findViewById(R.id.popup_window_contents);
            getLayoutInflater().inflate(R.layout.add_budget_input, inputField);
        }
        // Deleting budgets
        else if (windowType == 2){
            int deleteNum = deleteList.size();
            String budgetStr = "budgets";
            if (deleteNum == 1) {
                budgetStr = "budget";
            }
            String message = String.format(getResources().getString(R.string.main_menu_confirm_delete), deleteNum, budgetStr);
            ((TextView) popupView.findViewById(R.id.popup_window_id)).setText(message);
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
                //Empty the list
                deleteList.clear();
                //Change button from delete back to logout
                isLogout = true;
                logoutOrDelete.setText(getResources().getString(R.string.main_menu_logout_label));
                logoutOrDelete.setTextColor(getResources().getColor(R.color.colorTextLight));

                //Update listview
                myAdapter.notifyDataSetChanged();

                //Remove cancel button
                buttonLayout.removeView(cancelDelete);
                popupWindow.dismiss();
            }
        });

        //If the user clicks the button to the left of the cancel button, the correct action is done
        completeActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Adding a budget
                if (windowType == 1) {
                    // Initialize text input layout for add budget input text field
                    TextInputLayout tilBudgetName = (TextInputLayout) popupView.findViewById(R.id.til_add_budget_name_2);
                    tilBudgetName.setError(null);
                    boolean hasError = false;

                    EditText budgetNameField = (EditText) popupView.findViewById(R.id.add_budget_name_2);
                    String budgetName = budgetNameField.getText().toString();

                    // Check for empty budget name string
                    if (budgetName.equals("")) {
                        tilBudgetName.setError(getResources().getString(R.string.budget_empty));
                        hasError = true;
                    }
                    // Check for budget name unique
                    if (!budgetManager.isUnique(budgetName)) {
                        tilBudgetName.setError(getResources().getString(R.string.budget_not_unique));
                        hasError = true;
                    }
                    // If no errors, add new budget and dismiss window
                    if (!hasError) {
                        budgetManager.addBudget(budgetName);
                        popupWindow.dismiss();
                    }
                }
                // Deleting budgets
                else if (windowType == 2) {
                    // Delete the selected budgets
                    for (int i = 0; i < deleteList.size(); ++i) {
                        budgetManager.removeBudget(deleteList.get(i));
                    }
                    // Reset list and flag
                    deleteList.clear();
                    isLogout = true;
                    // Reset button to logout
                    logoutOrDelete.setText(getResources().getString(R.string.main_menu_logout_label));
                    logoutOrDelete.setTextColor(getResources().getColor(R.color.colorTextLight));
                    //Remove cancel button
                    buttonLayout.removeView(cancelDelete);
                    popupWindow.dismiss();
                }
                myAdapter.notifyDataSetChanged();
            }
        });
    }





    //========================================================================================================================
    //Custom adapter that loads in the main menu items
    //========================================================================================================================
    private class BudgetManagerAdapter extends BaseAdapter {
        @Override
        public int getCount() { return budgetManager.getBudgetListSize(); }

        @Override
        public String getItem(int position) { return budgetManager.getBudgetName(position); }

        @Override
        public long getItemId(int position) { return position; }

        //This constructs the budget object in the menu
        @Override
        public View getView(final int position, View convertView, ViewGroup container) {
            //Not sure what this does
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.budgetmanager_list_item, container, false);
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

            //Update the budget name
            ((TextView) convertView.findViewById(android.R.id.text1)).setText(getItem(position));

            //Update progress bar size
            ProgressBarUpdate.updateProgressBar(convertView, R.id.budget_progress,
                    budgetManager.getBudgetPercentageSpent(position),
                    budgetManager.get(position).getBudgetSpent(),
                    budgetManager.get(position).getBudgetLimit());

            //These are the buttons, we need to add popups when you press them to do different things

            ImageButton settingsButton = (ImageButton) convertView.findViewById(R.id.settings_button);
            //ImageButton deleteBudgetButton = (ImageButton) convertView.findViewById(R.id.delete_button);

            final RelativeLayout budgetRow = (RelativeLayout) convertView.findViewById(R.id.budget_row);

            //This is the same view but it needs to be final to be sent into the button event listener
            final View convertViewFinal = convertView;

            budgetRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(MainMenuScreen.this, BudgetMenuScreen.class);
                    i.putExtra("BUDGET_ID", position);
                    startActivity(i);
                }
            });
            //Long click on budget deletes it
            budgetRow.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    //Search the deletelist
                    int i =0;
                    boolean isFound = false;
                    while (!isFound && i<deleteList.size()){
                        //Already in deletelist, so unselect it
                        if (deleteList.get(i) == budgetManager.getBudgetName(position)) {
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
                        deleteList.add(budgetManager.getBudgetName(position));
                    }
                    if (deleteList.size() > 0) {
                        //Add cancel button if not there already
                        if (isLogout) {
                            isLogout = false;
                            //Create cancel button
                            buttonLayout.addView(cancelDelete);
                        }

                        //Change logout to delete
                        logoutOrDelete.setText(getResources().getString(R.string.main_menu_delete_label));
                        logoutOrDelete.setTextColor(getResources().getColor(R.color.colorAccent));

                    } else {
                        // List empty, change delete to logout
                        isLogout = true;
                        logoutOrDelete.setText(getResources().getString(R.string.main_menu_logout_label));
                        logoutOrDelete.setTextColor(getResources().getColor(R.color.colorTextLight));
                        buttonLayout.removeView(cancelDelete);

                    }
                    //True makes sure long click doesn't also count as short click
                    return true;
                }
            });

            return convertView;
        }
    }
    //========================================================================================================================
}
