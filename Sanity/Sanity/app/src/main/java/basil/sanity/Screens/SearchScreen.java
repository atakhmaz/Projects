package basil.sanity.Screens;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import basil.sanity.Controllers.Budget;
import basil.sanity.Controllers.BudgetManager;
import basil.sanity.Controllers.Category;
import basil.sanity.Controllers.Transaction;
import basil.sanity.Database.Database;
import basil.sanity.R;
import basil.sanity.Util.ProgressBarUpdate;

public class SearchScreen extends AppCompatActivity {
    private static String username;
    private static String searchString;
    private static ArrayList<Budget> budgets;
    private static ArrayList<Category> categories;
    private static ArrayList<Transaction> transactions;

    private ListView budgetListView;
    private ListView categoryListView;
    private ListView transactionListView;

    private static BudgetAdapter budgetAdapter;
    private static CategoryAdapter categoryAdapter;
    private static TransactionAdapter transactionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_screen);
        username = getIntent().getStringExtra("USERNAME");
        searchString = "";
        searchString = getIntent().getStringExtra("SEARCH_STRING");
        Log.d("Search username", username);
        Log.d("Search string", searchString);
        Database db = null;
        try{
            db = new Database();
        }
        catch(Exception e){}
        budgets = db.SearchBudget(username, searchString);
        categories = db.SearchCategory(username, searchString);
        transactions = db.SearchTransaction(username, searchString);
        Log.d("Budget Size", Integer.toString(budgets.size()));
        Log.d("Category Size", Integer.toString(categories.size()));
        Log.d("Transaction Size", Integer.toString(transactions.size()));

        budgetListView = (ListView) findViewById(R.id.search_budget_list);
        categoryListView = (ListView) findViewById(R.id.search_category_list);
        transactionListView = (ListView) findViewById(R.id.search_transaction_list);

        budgetAdapter = new BudgetAdapter();
        budgetListView.setAdapter(budgetAdapter);
        categoryAdapter = new CategoryAdapter();
        categoryListView.setAdapter(categoryAdapter);
        transactionAdapter = new TransactionAdapter();
        transactionListView.setAdapter(transactionAdapter);
    }




    public static void updateBudgetList(){
        if(budgetAdapter != null) {
            Database db = null;
            try{
                db = new Database();
            }
            catch(Exception e){}
            budgets = db.SearchBudget(username, searchString);
            budgetAdapter.notifyDataSetChanged();
        }
    }

    public static void updateCategoryList(){
        if(categoryAdapter != null) {
            Database db = null;
            try{
                db = new Database();
            }
            catch(Exception e){}
            categories = db.SearchCategory(username, searchString);
            categoryAdapter.notifyDataSetChanged();
        }
    }

    public static void updateTransactionList(){
        if(categoryAdapter != null) {
            Database db = null;
            try{
                db = new Database();
            }
            catch(Exception e){}
            transactions = db.SearchTransaction(username, searchString);
            transactionAdapter.notifyDataSetChanged();
        }
    }








    //========================================================================================================================
    //Custom adapter that loads in the main menu items
    //========================================================================================================================
    private class BudgetAdapter extends BaseAdapter {
        @Override
        public int getCount() { return budgets.size(); }

        @Override
        public String getItem(int position) { return budgets.get(position).getBudgetName(); }

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
                    budgets.get(position).getBudgetPercentageSpent(),
                    budgets.get(position).getBudgetSpent(),
                    budgets.get(position).getBudgetLimit());
            final RelativeLayout budgetRow = (RelativeLayout) convertView.findViewById(R.id.budget_row);

            budgetRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(SearchScreen.this, BudgetMenuScreen.class);
                    i.putExtra("BUDGET_ID", BudgetManager.getInstance().findBudget(getItem(position)));
                    startActivity(i);
                }
            });
            return convertView;
        }
    }
    private class CategoryAdapter extends BaseAdapter {
        @Override
        public int getCount() { return categories.size(); }

        @Override
        public String getItem(int position) { return categories.get(position).getCategoryName(); }

        @Override
        public long getItemId(int position) { return position; }

        //This constructs the budget object in the menu
        @Override
        public View getView(final int position, View convertView, ViewGroup container) {
            //Not sure what this does
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.search_budget_list_item, container, false);
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
                    categories.get(position).getPercentageSpent(),
                    categories.get(position).getAmountSpent(),
                    categories.get(position).getCategoryLimit());

            final RelativeLayout budgetRow = (RelativeLayout) convertView.findViewById(R.id.budget_row);

            budgetRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(SearchScreen.this, TransactionsScreen.class);
                    Category category = categories.get(position);
                    int budgetIndex = BudgetManager.getInstance().findBudget(category.getBudgetName());
                    int categoryIndex = BudgetManager.getInstance().getBudgetByName(category.getBudgetName()).findCategory(category.getCategoryName());
                    i.putExtra("BUDGET_INDEX", budgetIndex);
                    i.putExtra("CATEGORY_INDEX", categoryIndex);
                    startActivity(i);
                }
            });

            return convertView;
        }
    }

    private class TransactionAdapter extends BaseAdapter {
        @Override
        public int getCount() { return transactions.size(); }

        @Override
        public String getItem(int position) { return transactions.get(position).getItemName(); }

        @Override
        public long getItemId(int position) { return position; }

        //This constructs the budget object in the menu
        @Override
        public View getView(final int position, View convertView, ViewGroup container) {
            //Not sure what this does
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.search_transaction_list_item, container, false);
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
            TextView budgetName = (TextView) convertView.findViewById(R.id.search_transaction_budget_name);
            TextView categoryName = (TextView) convertView.findViewById(R.id.search_transaction_category_name);
            TextView transactionName = (TextView) convertView.findViewById(R.id.search_transaction_list_name);
            TextView transactionAmount = (TextView) convertView.findViewById(R.id.search_transaction_list_amount);
            TextView transactionDate = (TextView) convertView.findViewById(R.id.search_transaction_list_date);
            TextView transactionMemo = (TextView) convertView.findViewById(R.id.search_transaction_list_memo);

            budgetName.setText("Budget: " + transactions.get(position).getBudgetName());
            categoryName.setText("Category: " + transactions.get(position).getCategoryName());
            transactionName.setText(transactions.get(position).getItemName());
            Double transactionTemp = transactions.get(position).getPrice();
            NumberFormat formatter = NumberFormat.getCurrencyInstance();
            transactionAmount.setText(formatter.format(transactionTemp));
            Date transDate = transactions.get(position).getTransactionDate();
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/YYYY");
            transactionDate.setText(sdf.format(transDate));
            String memo = transactions.get(position).getMemo();
            if(!memo.isEmpty())
                transactionMemo.setText(memo);
            else
                transactionMemo.setText(null);

            return convertView;
        }
    }
    //========================================================================================================================
}
