package basil.sanity.Screens;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;

import basil.sanity.Controllers.BudgetManager;
import basil.sanity.Controllers.Reminder;
import basil.sanity.Database.Database;
import basil.sanity.R;

//Displays all reminders and allows deleting them
public class ReminderMenuScreen extends AppCompatActivity {
    
    Button createOrDelete;
    Button cancelDelete;
    LinearLayout buttonLayout;
    ListView reminderListView;
    static Database db;
    static ArrayList<Reminder> reminderList;
    ArrayList<String> deleteList;
    static String loggedInUser;
    boolean isCreate;
    static ReminderAdapter myAdapter;
    BudgetManager budgetManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_menu_screen);
        budgetManager = BudgetManager.getInstance();
        loggedInUser = budgetManager.getUsername();
        //Initialize db
        try {
            db = new Database();
            reminderList = db.GetReminderList(loggedInUser);
        } catch (Exception e) {
            e.printStackTrace();
        }

        buttonLayout = (LinearLayout) findViewById(R.id.reminder_layout_buttons);
        //Initialize deleteList
        deleteList = new ArrayList<>();
        isCreate = true;


        //Initialize createOrDelete button
        createOrDelete = new Button(this);
        createOrDelete.setText(getResources().getString(R.string.create_reminder_label));
        //Change button appearance to match logout button
        createOrDelete.setTextColor(getResources().getColor(R.color.colorTextLight));
        createOrDelete.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT,1.0f);
        params.setMargins(1, 1, 1, 2);
        createOrDelete.setLayoutParams(params);
        buttonLayout.addView(createOrDelete);

        //Initialize cancelDelete button
        cancelDelete = new Button(this);
        cancelDelete.setText(getResources().getString(R.string.main_menu_cancel_label));
        //Change button appearance to match logout button
        cancelDelete.setTextColor(getResources().getColor(R.color.colorAccent));
        cancelDelete.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        cancelDelete.setLayoutParams(params);

        //Populate listview
        Collections.sort(reminderList);
        myAdapter = new ReminderAdapter();
        reminderListView = (ListView) findViewById(R.id.reminder_list);
        reminderListView.setAdapter(myAdapter);


        createOrDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCreate) {
                    Intent i = new Intent(ReminderMenuScreen.this, ReminderAddScreen.class);
                    startActivity(i);
                } else {
                    createPopup();
                }
            }
        });
        //Listener for cancelDeleteing delete selected
        cancelDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Empty the list
                deleteList.clear();
                //Change button from delete back to logout
                isCreate = true;
                createOrDelete.setText(getResources().getString(R.string.create_reminder_label));
                createOrDelete.setTextColor(getResources().getColor(R.color.colorTextLight));

                //Update listview
                myAdapter.notifyDataSetChanged();

                //Remove cancel button
                buttonLayout.removeView(cancelDelete);
            }
        });
    }

    private class ReminderAdapter extends BaseAdapter {
        @Override
        public int getCount() { return reminderList.size(); }

        @Override
        public String getItem(int position) { return reminderList.get(position).getReminderName(); }

        @Override
        public long getItemId(int position) { return position; }

        //This constructs the budget object in the menu
        @Override
        public View getView(final int position, View convertView, ViewGroup container) {
            //Not sure what this does
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.reminder_list_item, container, false);
            }

            //Set the colors to alternate
            if (position % 2 == 0) {
                convertView.setBackgroundColor(getResources().getColor(R.color.listViewColor1));
            } else {
                convertView.setBackgroundColor(getResources().getColor(R.color.listViewColor2));
            }

            //Update the budget name
            ((TextView) convertView.findViewById(R.id.reminder_list_name)).setText(getItem(position));
            //Format date
            DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
            String dateFormat = df.format(reminderList.get(position).getEndDate());
            ((TextView) convertView.findViewById(R.id.reminder_list_date)).setText(dateFormat);
            ((TextView) convertView.findViewById(R.id.reminder_list_message)).setText(
                    reminderList.get(position).getMessage());
            String repeats = reminderList.get(position).isRepeat() ? getResources().getString(R.string.reminder_repeat_label)
                    : getResources().getString(R.string.reminder_no_repeat_label);
            ((TextView) convertView.findViewById(R.id.reminder_list_repeat)).setText(repeats);
            ((TextView) convertView.findViewById(R.id.reminder_list_budget)).setText(
                    reminderList.get(position).getBudget());
            ((TextView) convertView.findViewById(R.id.reminder_list_category)).setText(
                    reminderList.get(position).getCategory());
            final RelativeLayout reminderRow = (RelativeLayout) convertView.findViewById(R.id.reminder_row);

            //This is the same view but it needs to be final to be sent into the button event listener
            final View convertViewFinal = convertView;

//            reminderRow.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent i = new Intent(MainMenuScreen.this, BudgetMenuScreen.class);
//                    i.putExtra("BUDGET_ID", position);
//                    startActivity(i);
//                }
//            });
            //Long click on budget deletes it
            reminderRow.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    //Search the deletelist
                    int i = 0;
                    boolean isFound = false;
                    while (!isFound && i < deleteList.size()) {
                        //Already in deletelist, so unselect it
                        if (deleteList.get(i) == reminderList.get(position).getReminderName()) {
                            isFound = true;
                            deleteList.remove(i);
                            if (position % 2 == 0) {
                                reminderRow.setBackgroundColor(getResources().getColor(R.color.listViewColor1));
                            } else {
                                reminderRow.setBackgroundColor(getResources().getColor(R.color.listViewColor2));
                            }
                        }
                        ++i;
                    }
                    // New position clicked
                    if (!isFound) {
                        reminderRow.setBackgroundResource(R.drawable.border_red);
                        deleteList.add(reminderList.get(position).getReminderName());
                    }
                    if (deleteList.size() > 0) {
                        //Add cancel button if not there already
                        if (isCreate) {
                            isCreate = false;
                            //Create cancel button
                            buttonLayout.addView(cancelDelete);
                        }

                        //Change create to delete
                        createOrDelete.setText(getResources().getString(R.string.main_menu_delete_label));
                        createOrDelete.setTextColor(getResources().getColor(R.color.colorAccent));

                    } else {
                        // List empty, change delete to create
                        isCreate = true;
                        createOrDelete.setText(getResources().getString(R.string.create_reminder_label));
                        createOrDelete.setTextColor(getResources().getColor(R.color.colorTextLight));
                        buttonLayout.removeView(cancelDelete);

                    }
                    //True makes sure long click doesn't also count as short click
                    return true;
                }
            });

            return convertView;
        }
    }
    //Used in ReminderAddScreen
    public static void updateReminderList() {
        try {
            db = new Database();
            reminderList = db.GetReminderList(loggedInUser);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Collections.sort(reminderList);
        myAdapter.notifyDataSetChanged();
    }
    void createPopup(){
        // get a reference to the already created main layout
        LinearLayout mainLayout = (LinearLayout) findViewById(R.id.activity_reminder_menu);

        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        final View popupView = inflater.inflate(R.layout.popup_window, null);


        // Deleting budgets

        int deleteNum = deleteList.size();
        String budgetStr = "reminders";
        if (deleteNum == 1) {
            budgetStr = "reminder";
        }
        String message = String.format(getResources().getString(R.string.main_menu_confirm_delete), deleteNum, budgetStr);
        ((TextView) popupView.findViewById(R.id.popup_window_id)).setText(message);


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
                isCreate = true;
                createOrDelete.setText(getResources().getString(R.string.create_reminder_label));
                createOrDelete.setTextColor(getResources().getColor(R.color.colorTextLight));

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
                // Delete the selected reminders
                for (int i = 0; i < deleteList.size(); ++i) {
                    int j =0;
                    boolean isFound = false;
                    while (!isFound && j < reminderList.size()) {
                        if (deleteList.get(i).equals(reminderList.get(j).getReminderName())){
                            isFound = true;
                            db.DeleteReminder(reminderList.get(j));
                            //reminderList.remove(j);
                        }
                        ++j;
                    }
                }
                // Reset list and flag
                deleteList.clear();
                isCreate = true;
                // Reset button to logout
                createOrDelete.setText(getResources().getString(R.string.create_reminder_label));
                createOrDelete.setTextColor(getResources().getColor(R.color.colorTextLight));
                //Remove cancel button
                buttonLayout.removeView(cancelDelete);
                updateReminderList();
                popupWindow.dismiss();
            }
        });
    }
}
