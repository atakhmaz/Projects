package basil.sanity.Screens;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import basil.sanity.Controllers.BudgetManager;
import basil.sanity.Controllers.Category;
import basil.sanity.Controllers.CategoryPeriod;
import basil.sanity.Controllers.Transaction;
import basil.sanity.Database.Database;
import basil.sanity.R;

public class TransactionsScreen extends ListActivity {
    int budgetIndex;
    int categoryIndex;
    Category category;

    TextView summary;
    TextView categoryName;
    Button periodButton;

    Database db;
    String usernameStr;
    String budgetNameStr;
    String categoryNameStr;
    Double amountSpent;

    TransactionAdapter myTransactionAdapter;
    ArrayList<CategoryPeriod> periodList;
    ArrayList<String> periodListStr;
    CategoryPeriod selectedPeriod;
    ArrayList<Transaction> transactionsInPeriod;

    public static View budgetScreenView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_screen);

        budgetIndex = getIntent().getIntExtra("BUDGET_INDEX", -1);
        categoryIndex = getIntent().getIntExtra("CATEGORY_INDEX", -1);

        db = new Database(getBaseContext());
        usernameStr = BudgetManager.getInstance().getUsername();
        category =BudgetManager.getInstance().get(budgetIndex).getCategory(categoryIndex);
        categoryNameStr = category.getCategoryName();
        budgetNameStr = BudgetManager.getInstance().get(budgetIndex).getBudgetName();

//        Log.d("Trans budget index", Integer.toString(budgetIndex));
//        Log.d("Trans cat index", Integer.toString(categoryIndex));
//        Log.d("Trans trans size", Integer.toString(category.getTransactionList().size()));

        categoryName = (TextView) findViewById(R.id.overall_budget_text);
        summary = (TextView) findViewById(R.id.overall_summary);
        periodButton = (Button) findViewById(R.id.transaction_period_button);

        categoryName.setText(categoryNameStr);


        budgetScreenView = this.getWindow().getDecorView();


        //Allow user to select budget period to view, default is current period
        // save the prompt for selecting period
        final String prompt = getResources().getString(R.string.select_period_label);
        //Get periods from database
        periodList = db.GetCategoryPeriod
                (usernameStr, categoryNameStr, budgetNameStr);
        //Show the newest periodList first
        Collections.sort(periodList);
        periodListStr = new ArrayList<>();
        loadTransactionsInPeriod(0);

        amountSpent = calculateSpent();
        summary.setText(String.format("$%.2f spent out of $%.2f limit",
                amountSpent, category.getCategoryLimit()));
        //Put period start and end dates in an arraylist
        for (int i=0; i < periodList.size(); ++i) {
            periodListStr.add(periodList.get(i).toString());
        }
        //Populate select period popup with period dates
        final ArrayAdapter<String> periodAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, periodListStr);

        //Show the transactions
        myTransactionAdapter = new TransactionAdapter();
        setListAdapter(myTransactionAdapter);

        periodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(v.getContext())
                        .setTitle(prompt)
                        .setAdapter(periodAdapter, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int position) {
                                // TODO: user specific action
                                loadTransactionsInPeriod(position);
                                amountSpent = calculateSpent();
                                summary.setText(String.format("$%.2f spent out of $%.2f limit",
                                        amountSpent, category.getCategoryLimit()));
                                myTransactionAdapter.notifyDataSetChanged();
                                dialog.dismiss();
                            }
                        }).create().show();
            }
        });

    }
    //Load the correct category period
    private void loadTransactionsInPeriod(int periodIndex) {
        selectedPeriod = periodList.get(periodIndex);
        Date start = selectedPeriod.getStartDate();
        Date end = selectedPeriod.getEndDate();
        transactionsInPeriod = db.GetTransactionListForDate(start, end, usernameStr, budgetNameStr, categoryNameStr);
        //Make the transactions appear newest first
        Collections.sort(transactionsInPeriod);
    }

    //Sum up the transactions in the period
    double calculateSpent() {
        double result = 0;
        for (int i = 0; i < transactionsInPeriod.size(); ++i) {
            result += transactionsInPeriod.get(i).getPrice();
        }
        return result;
    }
    private class TransactionAdapter extends BaseAdapter {
        @Override
        public int getCount() { return transactionsInPeriod.size(); }

        @Override
        public String getItem(int position) { return transactionsInPeriod.get(position).getItemName(); }

        @Override
        public long getItemId(int position) { return position; }

        @Override
        public View getView(final int position, View convertView, ViewGroup container) {

            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.transaction_list_item, container, false);
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

            TextView transactionName = (TextView) convertView.findViewById(R.id.transaction_list_name);
            TextView transactionAmount = (TextView) convertView.findViewById(R.id.transaction_list_amount);
            TextView transactionDate = (TextView) convertView.findViewById(R.id.transaction_list_date);
            TextView transactionMemo = (TextView) convertView.findViewById(R.id.transaction_list_memo);

            transactionName.setText(transactionsInPeriod.get(position).getItemName());
            Double transactionTemp = transactionsInPeriod.get(position).getPrice();
            NumberFormat formatter = NumberFormat.getCurrencyInstance();
            transactionAmount.setText(formatter.format(transactionTemp));
            Date transDate = transactionsInPeriod.get(position).getTransactionDate();
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/YYYY");
            transactionDate.setText(sdf.format(transDate));
            String memo = transactionsInPeriod.get(position).getMemo();
            if(!memo.isEmpty())
                transactionMemo.setText(memo);
            else
                transactionMemo.setText(null);

            return convertView;
        }
    }
}
