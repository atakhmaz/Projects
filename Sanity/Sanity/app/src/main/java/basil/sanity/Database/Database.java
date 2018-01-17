package basil.sanity.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;

import basil.sanity.Controllers.Budget;
import basil.sanity.Controllers.BudgetManager;
import basil.sanity.Controllers.Category;
import basil.sanity.Controllers.CategoryPeriod;
import basil.sanity.Controllers.ContextSingleton;
import basil.sanity.Controllers.Notification;
import basil.sanity.Controllers.Reminder;
import basil.sanity.Controllers.Settings;
import basil.sanity.Controllers.Transaction;
import basil.sanity.Controllers.User;
import basil.sanity.Crypt.BCrypt;
import basil.sanity.Util.RandomDate;
import basil.sanity.Util.RandomString;


public class Database extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 127;
    private static final String DATABASE_NAME = "Sanity";
    private static final String TABLE_CATEGORY_PERIOD = "category_period";
    private final Context context;
    private static final String TABLE_CATEGORY = "category";
    private static final String TABLE_TRANSACTION = "transactions";
    private static final String TABLE_REFUND = "refund";
    private static final String TABLE_CATEGORY_SETTING = "category_setting";
    private static final String TABLE_USER = "user";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_HASHED_PASSWORD = "hashed_password";
    private static final String KEY_CATEGORY_NAME = "category_name";
    private static final String KEY_MAX_BUDGET_AMOUNT = "max_budget_amount";
    private static final String KEY_AMOUNT_SPENT = "amount_spent";
    private static final String KEY_START_DATE = "start_date";
    private static final String KEY_END_DATE = "end_date";
    private static final String KEY_TRANSACTION_ID = "transaction_id";
    private static final String KEY_TRANSACTION_NAME = "transaction_name";
    private static final String KEY_Date_ADDED = "date_added";
    private static final String KEY_MEMO = "memo";
    private static final String KEY_REFUND_ID = "refund_id";
    private static final String KEY_REFUND_NAME = "refund_name";
    private static final String KEY_PRICE = "price";
    private static final String KEY_PERCENT_REMINDER = "percent_reminder";
    private static final String TABLE_BUDGET = "table_budget";
    private static final String TABLE_BUDGET_SETTING = "budget_setting";
    private static final String KEY_SECURITY_QUESTION_INDEX = "security_question_index";
    private static final String KEY_SECURITY_QUESTION_ANSWER = "security_question_answer";
    private static final String KEY_FREQUENCY_TYPE = "frequency_type";
    private static final String KEY_FREQUENCY_VALUE = "frequency_value";
    private static final String KEY_INTERVAL_CHECK_CODE = "interval_check_code";
    private static final String KEY_CATEGORY_CHECK_CODE = "category_check_code";
    private static final String KEY_EMAIL = "email_id";

    private static final String KEY_BUDGET_NAME = "budget_name";
    private static final String KEY_REMINDER_NAME = "reminder_name";
    private static final String TABLE_REMINDER = "table_reminder";
    private static final String KEY_IS_REPEAT = "is_repeat";

    /**
     * Initializes the Database.
     *
     * @param context getBaseContext() or getApplicationContext()
     */
    public Database(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        if (!checkUser("a")) {
            addUser(new User("dummy", "a", "$2a$10$EGI8dmp8xeqNi6tnunsKNujRJ0kMWLIY7JjuMFEbSeJBxl6ZY2sCK"));
        }

    }

    /**
     * Only call this after setting the Context in ContextSingleton
     * Database constructor without parameters.
     *
     * @throws Exception
     */
    public Database() throws Exception {

        super(ContextSingleton.getInstance(), DATABASE_NAME, null, DATABASE_VERSION);
        this.context = ContextSingleton.getInstance();
        //DEBUG MODE
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + KEY_USERNAME + " TEXT PRIMARY KEY,"
                + KEY_HASHED_PASSWORD + " TEXT,"
                + KEY_SECURITY_QUESTION_INDEX + " INT,"
                + KEY_SECURITY_QUESTION_ANSWER + " TEXT,"
                + KEY_EMAIL + " TEXT"

                + ")";
        String CREATE_TABLE_BUDGET = "CREATE TABLE " + TABLE_BUDGET + "("
                + KEY_BUDGET_NAME + " TEXT,"
                + KEY_MAX_BUDGET_AMOUNT + " REAL,"
                + KEY_AMOUNT_SPENT + " REAL,"
                + KEY_USERNAME + " TEXT,"
                + KEY_INTERVAL_CHECK_CODE + " INT,"
                + KEY_CATEGORY_CHECK_CODE + " INT,"
                + "PRIMARY KEY (" + KEY_USERNAME + "," + KEY_BUDGET_NAME + "),"
                + "FOREIGN KEY(" + KEY_USERNAME + ") REFERENCES " + TABLE_USER + "(" + KEY_USERNAME + ")"
                + ")";
        String CREATE_TABLE_BUDGET_SETTING = "CREATE TABLE " + TABLE_BUDGET_SETTING + "("
                + KEY_START_DATE + " REAL,"
                + KEY_END_DATE + " REAL,"
                + KEY_PERCENT_REMINDER + " REAL,"
                + KEY_USERNAME + " TEXT,"
                + KEY_FREQUENCY_TYPE + " TEXT,"
                + KEY_FREQUENCY_VALUE + " INTEGER,"
                + KEY_BUDGET_NAME + " TEXT,"

                + "PRIMARY KEY (" + KEY_USERNAME + "," + KEY_BUDGET_NAME + "),"
                + "FOREIGN KEY(" + KEY_USERNAME + ") REFERENCES " + TABLE_USER + "(" + KEY_USERNAME + ")"
                + ")";
        String CREATE_TABLE_CATEGORY = "CREATE TABLE " + TABLE_CATEGORY + "("
                + KEY_CATEGORY_NAME + " TEXT ,"
                + KEY_MAX_BUDGET_AMOUNT + " REAL,"
                + KEY_AMOUNT_SPENT + " REAL,"
                + KEY_USERNAME + " TEXT,"
                + KEY_BUDGET_NAME + " TEXT,"
                + KEY_INTERVAL_CHECK_CODE + " INT,"
                + KEY_CATEGORY_CHECK_CODE + " INT,"
                + "PRIMARY KEY (" + KEY_USERNAME + "," + KEY_BUDGET_NAME + "," + KEY_CATEGORY_NAME + "),"
                + "FOREIGN KEY(" + KEY_USERNAME + ") REFERENCES " + TABLE_USER + "(" + KEY_USERNAME + ")"
                + "FOREIGN KEY(" + KEY_BUDGET_NAME + ") REFERENCES " + TABLE_BUDGET + "(" + KEY_BUDGET_NAME + ")"

                + ")";
        String CREATE_TABLE_CATEGORY_SETTING = "CREATE TABLE " + TABLE_CATEGORY_SETTING + "("
                + KEY_CATEGORY_NAME + " TEXT,"
                + KEY_START_DATE + " REAL,"
                + KEY_END_DATE + " REAL,"
                + KEY_PERCENT_REMINDER + " REAL,"
                + KEY_USERNAME + " TEXT,"
                + KEY_FREQUENCY_TYPE + " TEXT,"
                + KEY_FREQUENCY_VALUE + " INTEGER,"
                + KEY_BUDGET_NAME + " TEXT,"

                + "PRIMARY KEY (" + KEY_USERNAME + "," + KEY_BUDGET_NAME + "," + KEY_CATEGORY_NAME + "),"
                + "FOREIGN KEY(" + KEY_BUDGET_NAME + ") REFERENCES " + TABLE_BUDGET + "(" + KEY_BUDGET_NAME + "),"
                + "FOREIGN KEY(" + KEY_USERNAME + ") REFERENCES " + TABLE_USER + "(" + KEY_USERNAME + "),"
                + "FOREIGN KEY(" + KEY_CATEGORY_NAME + ") REFERENCES " + TABLE_CATEGORY + "(" + KEY_CATEGORY_NAME + ")"
                + ")";

        String CREATE_TABLE_TRANSACTION = "CREATE TABLE " + TABLE_TRANSACTION + "("
                + KEY_TRANSACTION_NAME + " TEXT,"
                + KEY_Date_ADDED + " REAL,"
                + KEY_PRICE + " REAL,"
                + KEY_MEMO + " TEXT,"
                + KEY_CATEGORY_NAME + " TEXT,"
                + KEY_USERNAME + " TEXT,"
                + KEY_BUDGET_NAME + " TEXT,"

                + "FOREIGN KEY(" + KEY_BUDGET_NAME + ") REFERENCES " + TABLE_BUDGET + "(" + KEY_BUDGET_NAME + "),"
                + "FOREIGN KEY(" + KEY_USERNAME + ") REFERENCES " + TABLE_USER + "(" + KEY_USERNAME + "),"
                + "FOREIGN KEY(" + KEY_CATEGORY_NAME + ") REFERENCES " + TABLE_CATEGORY + "(" + KEY_CATEGORY_NAME + ")"
                + ")";
        String CREATE_TABLE_REMINDER = "CREATE TABLE " + TABLE_REMINDER + "("
                + KEY_REMINDER_NAME + " TEXT,"
                + KEY_Date_ADDED + " REAL,"
                + KEY_CATEGORY_NAME + " TEXT,"
                + KEY_MEMO + " TEXT,"
                + KEY_IS_REPEAT + " INTEGER,"
                + KEY_USERNAME + " TEXT,"
                + KEY_BUDGET_NAME + " TEXT,"

                + "FOREIGN KEY(" + KEY_BUDGET_NAME + ") REFERENCES " + TABLE_BUDGET + "(" + KEY_BUDGET_NAME + "),"
                + "FOREIGN KEY(" + KEY_USERNAME + ") REFERENCES " + TABLE_USER + "(" + KEY_USERNAME + "),"
                + "FOREIGN KEY(" + KEY_CATEGORY_NAME + ") REFERENCES " + TABLE_CATEGORY + "(" + KEY_CATEGORY_NAME + ")"
                + ")";
        String CREATE_TABLE_CATEGORY_PERIOD = "CREATE TABLE " + TABLE_CATEGORY_PERIOD + "( "
                + KEY_CATEGORY_NAME + " TEXT,"
                + KEY_BUDGET_NAME + " TEXT,"
                + KEY_USERNAME + " INTEGER,"
                + KEY_START_DATE + " REAL,"
                + KEY_END_DATE + " REAL,"

                + "FOREIGN KEY(" + KEY_BUDGET_NAME + ") REFERENCES " + TABLE_BUDGET + "(" + KEY_BUDGET_NAME + "),"
                + "FOREIGN KEY(" + KEY_USERNAME + ") REFERENCES " + TABLE_USER + "(" + KEY_USERNAME + "),"
                + "FOREIGN KEY(" + KEY_CATEGORY_NAME + ") REFERENCES " + TABLE_CATEGORY + "(" + KEY_CATEGORY_NAME + ")"
                + ")";
        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_TABLE_BUDGET);
        db.execSQL(CREATE_TABLE_BUDGET_SETTING);
        db.execSQL(CREATE_TABLE_CATEGORY);
        db.execSQL(CREATE_TABLE_CATEGORY_SETTING);
        db.execSQL(CREATE_TABLE_TRANSACTION);
        db.execSQL(CREATE_TABLE_REMINDER);
        db.execSQL(CREATE_TABLE_CATEGORY_PERIOD);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSACTION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REFUND);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY_SETTING);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BUDGET);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BUDGET_SETTING);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REMINDER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY_PERIOD);
        // Create tables again
        onCreate(db);

    }

    /**
     * Adds the category to the database and matches it to the user.
     *
     * @param username of the user logged in
     */
//    public boolean addCategory(Category category, String username) {
//        try {
//            SQLiteDatabase db = this.getWritableDatabase();
//            ContentValues values = new ContentValues();
//            values.put(KEY_CATEGORY_NAME, category.getCategoryName());
//            values.put(KEY_MAX_BUDGET_AMOUNT, category.getCategoryLimit());
//            values.put(KEY_AMOUNT_SPENT, category.getAmountSpent());
//            values.put(KEY_USERNAME, username);
//            Random rand = new Random();
//            int random = rand.nextInt(Integer.MAX_VALUE);
//            while(checkCode(random)){
//                random = rand.nextInt(Integer.MAX_VALUE);
//            }
//            int random2 = rand.nextInt(Integer.MAX_VALUE);
//            while(checkCode(random2)){
//                random2 = rand.nextInt(Integer.MAX_VALUE);
//            }
//            Log.d("Utsav",random+"|"+random2);
//
//            values.put(KEY_BUDGET_CHECK_CODE, random);
//            values.put(KEY_INTERVAL_CHECK_CODE, random2);
//
//            db.insert(TABLE_CATEGORY, null, values);
//            ContentValues settingsValue = new ContentValues();
//            settingsValue.put(KEY_CATEGORY_NAME, category.getCategoryName());
//
//
//            settingsValue.put(KEY_START_DATE, category.getSettings().getCategoryPeriod().getStartDate().getTime());
//            settingsValue.put(KEY_END_DATE, category.getSettings().getCategoryPeriod().getEndDate().getTime());
//            settingsValue.put(KEY_PERCENT_REMINDER, category.getSettings().getThreshold());
//            settingsValue.put(KEY_USERNAME, username);
//            settingsValue.put(KEY_FREQUENCY_VALUE, category.getSettings().getFrequencyValue());
//            settingsValue.put(KEY_FREQUENCY_TYPE, category.getSettings().getFrequencyType());
//
//            db.insert(TABLE_CATEGORY_SETTING, null, settingsValue);
//            Notification nc = new Notification();
//            nc.setBothAlarms(category,username,context);
//            updateOverallMaximumBudget(username, category.getCategoryLimit());
//            updateOverallBudgetAmountSpent(username, category.getAmountSpent());
//            return true;
//        }catch(Exception e){
//            return false;
//        }
//
//    }
    public boolean checkBudget(String username, String budgetname) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_CATEGORY, new String[]{KEY_CATEGORY_NAME},
                KEY_CATEGORY_NAME + "=?" + " and " + KEY_USERNAME + "=?", new String[]{budgetname, username},
                null, null, null, null);

        if (cursor.moveToFirst())
            return true;
        return false;

    }
    /**
     * Private function to initialize an overallbudget for the user.
     * @param username of the just created user.
     */
//    private void addOverallBudget(String username) {
//
//        Category category = new Category("Overall Category");
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put(KEY_MAX_BUDGET_AMOUNT,0);
//        values.put(KEY_AMOUNT_SPENT, category.getAmountSpent());
//        values.put(KEY_USERNAME,username);
//        Random rand = new Random();
//        int random = rand.nextInt(Integer.MAX_VALUE);
//        while(checkCode(random)){
//            random = rand.nextInt(Integer.MAX_VALUE);
//        }
//        int random2 = rand.nextInt(Integer.MAX_VALUE);
//        while(checkCode(random2)){
//            random2 = rand.nextInt(Integer.MAX_VALUE);
//        }
//        Log.d("Utsav",random+"|"+random2);
//        values.put(KEY_BUDGET_CHECK_CODE, random);
//        values.put(KEY_INTERVAL_CHECK_CODE, random2);
//        db.insert(TABLE_BUDGET, null, values);
//        ContentValues settingsValue = new ContentValues();
//        settingsValue.put(KEY_START_DATE, category.getSettings().getCategoryPeriod().getStartDate().getTime());
//        settingsValue.put(KEY_END_DATE, category.getSettings().getCategoryPeriod().getEndDate().getTime());
//        settingsValue.put(KEY_PERCENT_REMINDER, category.getSettings().getThreshold());
//        settingsValue.put(KEY_USERNAME,username);
//        settingsValue.put(KEY_FREQUENCY_VALUE, category.getSettings().getFrequencyValue());
//        settingsValue.put(KEY_FREQUENCY_TYPE, category.getSettings().getFrequencyType());
//        Notification nc = new Notification();
//        nc.setBothOverallAlarms(category,username,context);
//        db.insert(TABLE_BUDGET_SETTING, null, settingsValue);
//
//    }

    /**
     * Updates the overallbudget and its settings.
     *
     * @param category Overall category initialized as Category
     * @param username Username of the logged in user.
     */
    public void updateOverallBudget(Category category, String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_MAX_BUDGET_AMOUNT, category.getCategoryLimit());
        values.put(KEY_AMOUNT_SPENT, category.getAmountSpent());
        values.put(KEY_USERNAME, username);
        db.update(TABLE_BUDGET, values, KEY_USERNAME + "=?",
                new String[]{username});
        updateOverallSettings(username, category.getSettings());
    }

    /**
     * Gets the overall budget for the logged in user.
     *
     * @param username of the user logged in
     * @return Overall budget serialized as a budget.
     */
//    public Category getBudgetLimit(String username) {
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        Cursor settingCursor = db.query(TABLE_BUDGET_SETTING, new String[]{KEY_START_DATE,
//                        KEY_END_DATE, KEY_PERCENT_REMINDER, KEY_FREQUENCY_TYPE, KEY_FREQUENCY_VALUE},
//                KEY_USERNAME + "=?", new String[]{username},
//                null, null, null, null);
//        Settings settings = null;
//        if (settingCursor.moveToFirst()) {
//            do {
//
//                Date startDate = new java.util.Date(settingCursor.getLong(0));
//                Date endDate = new java.util.Date(settingCursor.getLong(1));
//                CategoryPeriod bp = new CategoryPeriod(startDate, endDate);
//                settings = new Settings(Double.parseDouble(settingCursor.getString(2)), bp, settingCursor.getString(3), settingCursor.getInt(4));
//            } while (settingCursor.moveToNext());
//        }
//        Cursor cursor = db.query(TABLE_BUDGET, new String[]{KEY_MAX_BUDGET_AMOUNT,
//                        KEY_AMOUNT_SPENT},
//                KEY_USERNAME + "=?", new String[]{username},
//                null, null, null, null);
//
//        if (cursor != null)
//            cursor.moveToFirst();
//
//        Category category = new Category("Overall Category",
//                Double.parseDouble(cursor.getString(0)),
//                Double.parseDouble(cursor.getString(1)),
//                settings);
//        cursor.close();
//        settingCursor.close();
//
//        return category;
//
//    }

    /**
     * Updates the category and the settings.
     *
     * @param category - serialzed category and setting to be updated
     * @param username - username of the user logged in.
     */
    public void updateBudget(Category category, String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_CATEGORY_NAME, category.getCategoryName());
        values.put(KEY_MAX_BUDGET_AMOUNT, category.getCategoryLimit());
        values.put(KEY_AMOUNT_SPENT, category.getAmountSpent());
        values.put(KEY_USERNAME, username);
        db.update(TABLE_CATEGORY, values, KEY_CATEGORY_NAME + "=?" + " and " + KEY_USERNAME + "=?",
                new String[]{category.getCategoryName(), username});
        updateSetting(category.getSettings(), category.getCategoryName(), username);
    }

    /**
     * Deletes the category from the database.
     *
     * @param category Category to be deleted.
     * @param username username of the user logged in.
     */
//    public void deleteBudget(Category category, String username) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.delete(TABLE_CATEGORY, KEY_CATEGORY_NAME + "=?" + " and " + KEY_USERNAME + "=?",
//                new String[]{category.getCategoryName(), username});
//        db.delete(TABLE_CATEGORY_SETTING, KEY_CATEGORY_NAME + "=?" + " and " + KEY_USERNAME + "=?",
//                new String[]{category.getCategoryName(), username});
//        updateOverallMaximumBudget(username, (-1 * category.getCategoryLimit()));
//        updateOverallBudgetAmountSpent(username, (-1 * category.getAmountSpent()));
//
//    }

    /**
     * @param categoryName of the budget to be added
     * @param username     of the user logged in
     * @return Category specified by budget name and username
     */
    public Category getCategory(String budgetName, String categoryName, String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Transaction> transactionList = new ArrayList<>();
        Cursor transactionCursor = db.query(TABLE_TRANSACTION, new String[]{KEY_TRANSACTION_NAME,
                        KEY_Date_ADDED, KEY_PRICE, KEY_MEMO},
                KEY_CATEGORY_NAME + "=?" + " and " + KEY_USERNAME + "=?", new String[]{categoryName, username},
                null, null, KEY_Date_ADDED + " DESC", null);

        if (transactionCursor.moveToFirst()) {
            do {
                Date date = new java.util.Date(transactionCursor.getLong(1));
                Transaction transaction = new Transaction(
                        transactionCursor.getString(0),
                        date,
                        Double.parseDouble(transactionCursor.getString(2)),
                        transactionCursor.getString(3),
                        budgetName,
                        categoryName);
                transactionList.add(transaction);
            } while (transactionCursor.moveToNext());
        }
        Cursor settingCursor = db.query(TABLE_CATEGORY_SETTING, new String[]{KEY_START_DATE,
                        KEY_END_DATE, KEY_PERCENT_REMINDER, KEY_FREQUENCY_TYPE, KEY_FREQUENCY_VALUE},
                KEY_CATEGORY_NAME + "=?" + " and " + KEY_USERNAME + "=?", new String[]{categoryName, username},
                null, null, null, null);
        Settings settings = null;
        if (settingCursor.moveToFirst()) {
            do {

                Date startDate = new java.util.Date(settingCursor.getLong(0));
                Date endDate = new java.util.Date(settingCursor.getLong(1));
                CategoryPeriod bp = new CategoryPeriod(startDate, endDate);
                settings = new Settings(Double.parseDouble(settingCursor.getString(2)), bp, settingCursor.getString(3),
                        settingCursor.getInt(4));
            } while (settingCursor.moveToNext());
        }
        Cursor cursor = db.query(TABLE_CATEGORY, new String[]{KEY_CATEGORY_NAME, KEY_MAX_BUDGET_AMOUNT,
                        KEY_AMOUNT_SPENT},
                KEY_CATEGORY_NAME + "=?" + " and " + KEY_USERNAME + "=?", new String[]{categoryName, username},
                null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        settingCursor.close();
        transactionCursor.close();
        Category category = new Category(cursor.getString(0),
                Double.parseDouble(cursor.getString(1)),
                Double.parseDouble(cursor.getString(2)),
                settings,
                transactionList,
                budgetName);
        cursor.close();
        return category;

    }


//    public ArrayList<Category> getCategoryList(String username) {
//        ArrayList<Category> categoryList = new ArrayList<>();
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.query(TABLE_CATEGORY, new String[]{KEY_CATEGORY_NAME, KEY_MAX_BUDGET_AMOUNT,
//                        KEY_AMOUNT_SPENT},
//                KEY_USERNAME + "=?", new String[]{username},
//                null, null, null, null);
//        if (cursor.moveToFirst()) {
//            do {
//                String categoryName = cursor.getString(0);
//                ArrayList<Transaction> transactionList = new ArrayList<>();
//                Cursor transactionCursor = db.query(TABLE_TRANSACTION, new String[]{KEY_TRANSACTION_NAME,
//                                KEY_Date_ADDED, KEY_PRICE, KEY_MEMO},
//                        KEY_CATEGORY_NAME + "=?" + " and " + KEY_USERNAME + "=?", new String[]{categoryName, username},
//                        null, null, KEY_Date_ADDED + " DESC", null);
//
//                if (transactionCursor.moveToFirst()) {
//                    do {
//                        Date date = new java.util.Date(transactionCursor.getLong(1));
//                        Transaction transaction = new Transaction(
//                                transactionCursor.getString(0),
//                                date,
//                                Double.parseDouble(transactionCursor.getString(2)),
//                                transactionCursor.getString(3));
//                        transactionList.add(transaction);
//                    } while (transactionCursor.moveToNext());
//                }
//                Cursor settingCursor = db.query(TABLE_CATEGORY_SETTING, new String[]{KEY_START_DATE,
//                                KEY_END_DATE, KEY_PERCENT_REMINDER, KEY_FREQUENCY_TYPE, KEY_FREQUENCY_VALUE},
//                        KEY_CATEGORY_NAME + "=?" + " and " + KEY_USERNAME + "=?", new String[]{categoryName, username},
//                        null, null, null, null);
//                Settings settings = null;
//                if (settingCursor.moveToFirst()) {
//                    do {
//                        Date startDate = new java.util.Date(settingCursor.getLong(0));
//                        Date endDate = new java.util.Date(settingCursor.getLong(1));
//                        CategoryPeriod bp = new CategoryPeriod(startDate, endDate);
//                        settings = new Settings(Double.parseDouble(settingCursor.getString(2)), bp, settingCursor.getString(3),
//                                settingCursor.getInt(4));
//                    } while (settingCursor.moveToNext());
//                }
//                Category category = new Category(cursor.getString(0),
//                        Double.parseDouble(cursor.getString(1)),
//                        Double.parseDouble(cursor.getString(2)),
//                        settings,
//                        transactionList);
//
//                categoryList.add(category);
//            } while (cursor.moveToNext());
//        }
//
//
//        return categoryList;
//    }


//    public ArrayList<Transaction> getTransactionListForAll(String username) {
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        ArrayList<Transaction> transactionList = new ArrayList<>();
//
//        Cursor transactionCursor = db.query(TABLE_TRANSACTION, new String[]{KEY_TRANSACTION_NAME,
//                        KEY_Date_ADDED, KEY_PRICE, KEY_MEMO},
//                KEY_USERNAME + "=?", new String[]{username},
//                null, null, null, KEY_Date_ADDED + " ASC");
//
//        if (transactionCursor.moveToFirst()) {
//
//            do {
//                Date date = new java.util.Date(Long.parseLong(transactionCursor.getString(1)));
//                Transaction transaction = new Transaction(
//                        transactionCursor.getString(0),
//                        date,
//                        Double.parseDouble(transactionCursor.getString(2)),
//                        transactionCursor.getString(3));
//                transactionList.add(transaction);
//            } while (transactionCursor.moveToNext());
//        }
//        return transactionList;
//    }

//    public ArrayList<Transaction> getTransactionListForBudget(String categoryName, String username) {
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        ArrayList<Transaction> transactionList = new ArrayList<>();
//
//        Cursor transactionCursor = db.query(TABLE_TRANSACTION, new String[]{KEY_TRANSACTION_NAME,
//                        KEY_Date_ADDED, KEY_PRICE, KEY_MEMO},
//                KEY_CATEGORY_NAME + "=?" + " and " + KEY_USERNAME + "=?", new String[]{categoryName, username},
//                null, null, KEY_Date_ADDED + " DESC", null);
//
//        if (transactionCursor.moveToFirst()) {
//
//            do {
//                Date date = new java.util.Date(transactionCursor.getLong(1));
//                Transaction transaction = new Transaction(
//                        transactionCursor.getString(0),
//                        date,
//                        Double.parseDouble(transactionCursor.getString(2)),
//                        transactionCursor.getString(3));
//                transactionList.add(transaction);
//            } while (transactionCursor.moveToNext());
//        }
//        return transactionList;
//    }

//    public ArrayList<Transaction> getTransactionListForDate(Date dateA, Date dateB, String username) {
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        ArrayList<Transaction> transactionList = new ArrayList<>();
//
//        Cursor transactionCursor = db.query(TABLE_TRANSACTION, new String[]{KEY_TRANSACTION_NAME,
//                        KEY_Date_ADDED, KEY_PRICE, KEY_MEMO},
//                KEY_Date_ADDED + " between ? and ? " +
//                        " and " + KEY_USERNAME + "=?", new String[]{dateA.getTime() + "", dateB.getTime() + ""
//                        , username},
//                null, null, KEY_Date_ADDED + " ASC", null);
//
//        if (transactionCursor.moveToFirst()) {
//
//            do {
//                Date date = new java.util.Date(transactionCursor.getLong(1));
//                Transaction transaction = new Transaction(
//                        transactionCursor.getString(0),
//                        date,
//                        Double.parseDouble(transactionCursor.getString(2)),
//                        transactionCursor.getString(3));
//                transactionList.add(transaction);
//            } while (transactionCursor.moveToNext());
//        }
//        return transactionList;
//    }

//    public ArrayList<Transaction> getTransactionListForDateAndBudget(Category category, Date dateA,
//                                                                     Date dateB, String username) {
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        ArrayList<Transaction> transactionList = new ArrayList<>();
//
//        Cursor transactionCursor = db.query(TABLE_TRANSACTION, new String[]{KEY_TRANSACTION_NAME,
//                        KEY_Date_ADDED, KEY_PRICE, KEY_MEMO},
//                KEY_Date_ADDED + " between ? and ? " +
//                        " and " + KEY_CATEGORY_NAME + "=?" +
//                        " and " + KEY_USERNAME + "=?", new String[]{dateA.getTime() + "", dateB.getTime() + "",
//                        category.getCategoryName(), username},
//                null, null, KEY_Date_ADDED + " ASC", null);
//
//        if (transactionCursor.moveToFirst()) {
//
//            do {
//                Date date = new java.util.Date(transactionCursor.getLong(1));
//                Transaction transaction = new Transaction(
//                        transactionCursor.getString(0),
//                        date,
//                        Double.parseDouble(transactionCursor.getString(2)),
//                        transactionCursor.getString(3));
//                transactionList.add(transaction);
//            } while (transactionCursor.moveToNext());
//        }
//        return transactionList;
//    }

    public boolean addUser(User user) {
        //TODO FIX THIS SOON!!!!
        if (checkUser(user.getUsername())) {
            return false;
        }
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues userValue = new ContentValues();
        userValue.put(KEY_USERNAME, user.getUsername());
        userValue.put(KEY_HASHED_PASSWORD, user.getHash());
        userValue.put(KEY_SECURITY_QUESTION_INDEX, user.getQuestionInt());
        userValue.put(KEY_SECURITY_QUESTION_ANSWER, user.getHashSecurityAns());
        userValue.put(KEY_EMAIL, user.getEmail());
        Log.d("DEBUG", user.getHash());
        db.insert(TABLE_USER, null, userValue);
        // addOverallBudget(user.getUsername());

        Budget budget = new Budget("My Budget", user.getUsername());

        AddBudget(budget, user.getUsername());

        if (user.getUsername().equals("a"))
            addDummyTransaction(user.getUsername());

        return true;
    }

    public void addDummyTransaction(String username) {
        Log.d("Adding dummy trans", " to database");
        Random rnd = new Random(System.currentTimeMillis());
        RandomString gen = new RandomString(20, rnd);
        Date newDate;
        DecimalFormat df = new DecimalFormat("####0.00");

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);

        AddCategoryPeriod(username, "Food", "My Budget", cal.getTime(), Calendar.getInstance().getTime());
        AddCategoryPeriod(username, "Other", "My Budget", cal.getTime(), Calendar.getInstance().getTime());
        AddCategoryPeriod(username, "Recreation", "My Budget", cal.getTime(), Calendar.getInstance().getTime());
        AddCategoryPeriod(username, "Rent", "My Budget", cal.getTime(), Calendar.getInstance().getTime());
        AddCategoryPeriod(username, "Transportation", "My Budget", cal.getTime(), Calendar.getInstance().getTime());

        for (int i = 0; i < 20; ++i) {
            newDate = RandomDate.nextDateInLastMonth();
            AddTransaction(new Transaction(gen.getPhrase(3), newDate, Double.parseDouble(df.format(rnd.nextDouble() * 10)), gen.getPhrase(8), "My Budget", "Food"), username, "My Budget", "Food");
        }
        for (int i = 0; i < 20; ++i) {
            newDate = RandomDate.nextDateInLastMonth();
            AddTransaction(new Transaction(gen.getPhrase(3), newDate, Double.parseDouble(df.format(rnd.nextDouble() * 10)), gen.getPhrase(8), "My Budget", "Other"), username, "My Budget", "Other");
        }
        for (int i = 0; i < 20; ++i) {
            newDate = RandomDate.nextDateInLastMonth();
            AddTransaction(new Transaction(gen.getPhrase(3), newDate, Double.parseDouble(df.format(rnd.nextDouble() * 10)), gen.getPhrase(8), "My Budget", "Recreation"), username, "My Budget", "Recreation");
        }
        for (int i = 0; i < 20; ++i) {
            newDate = RandomDate.nextDateInLastMonth();
            AddTransaction(new Transaction(gen.getPhrase(3), newDate, Double.parseDouble(df.format(rnd.nextDouble() * 10)), gen.getPhrase(8), "My Budget", "Rent"), username, "My Budget", "Rent");
        }
        for (int i = 0; i < 20; ++i) {
            newDate = RandomDate.nextDateInLastMonth();
            AddTransaction(new Transaction(gen.getPhrase(3), newDate, Double.parseDouble(df.format(rnd.nextDouble() * 10)), gen.getPhrase(8), "My Budget", "Transportation"), username, "My Budget", "Transportation");
        }

        for (int i = 0; i < 20; ++i) {
            newDate = RandomDate.nextDateInMonth();
            AddTransaction(new Transaction(gen.getPhrase(3), newDate, Double.parseDouble(df.format(rnd.nextDouble() * 10)), gen.getPhrase(8), "My Budget", "Food"), username, "My Budget", "Food");
        }
        for (int i = 0; i < 20; ++i) {
            newDate = RandomDate.nextDateInMonth();
            AddTransaction(new Transaction(gen.getPhrase(3), newDate, Double.parseDouble(df.format(rnd.nextDouble() * 10)), gen.getPhrase(8), "My Budget", "Other"), username, "My Budget", "Other");
        }
        for (int i = 0; i < 20; ++i) {
            newDate = RandomDate.nextDateInMonth();
            AddTransaction(new Transaction(gen.getPhrase(3), newDate, Double.parseDouble(df.format(rnd.nextDouble() * 10)), gen.getPhrase(8), "My Budget", "Recreation"), username, "My Budget", "Recreation");
        }
        for (int i = 0; i < 20; ++i) {
            newDate = RandomDate.nextDateInMonth();
            AddTransaction(new Transaction(gen.getPhrase(3), newDate, Double.parseDouble(df.format(rnd.nextDouble() * 10)), gen.getPhrase(8), "My Budget", "Rent"), username, "My Budget", "Rent");
        }
        for (int i = 0; i < 20; ++i) {
            newDate = RandomDate.nextDateInMonth();
            AddTransaction(new Transaction(gen.getPhrase(3), newDate, Double.parseDouble(df.format(rnd.nextDouble() * 10)), gen.getPhrase(8), "My Budget", "Transportation"), username, "My Budget", "Transportation");
        }
    }


    /**
     * @param username
     * @param password
     * @return success if username and password match in database
     */
    public boolean verifyUser(String username, String password) {
        if (username == null || password == null) {
            return false;
        }
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor userCursor = db.query(TABLE_USER, new String[]{KEY_USERNAME, KEY_HASHED_PASSWORD, KEY_EMAIL},
                KEY_USERNAME + "=?", new String[]{username},
                null, null, null, null);
        if (userCursor.moveToFirst()) {
            String hash = userCursor.getString(1);
            if (hash != null) {
                return BCrypt.checkpw(password, hash);
            }
        }
        return false;
    }

    public boolean checkEmail(String email) {
        if (email == null) return false;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor userCursor = db.query(TABLE_USER, new String[]{KEY_EMAIL},
                KEY_EMAIL + "=?", new String[]{email},
                null, null, null, null);
        if (userCursor.moveToFirst()) {
            return true;
        }
        return false;
    }

    public User getUser(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor userCursor = db.query(TABLE_USER, new String[]{KEY_EMAIL, KEY_USERNAME, KEY_HASHED_PASSWORD},
                KEY_USERNAME + "=?", new String[]{username},
                null, null, null, null);
        if (userCursor.moveToFirst()) {
            return new User(userCursor.getString(0), userCursor.getString(1), userCursor.getString(2));
        }
        return null;

    }

    public void updatePassword(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues userValue = new ContentValues();
        //Get a new hash
        String hash = BCrypt.hashpw(password, BCrypt.gensalt());
        userValue.put(KEY_USERNAME, username);
        userValue.put(KEY_HASHED_PASSWORD, hash);
        db.update(TABLE_USER, userValue, KEY_USERNAME + "=?"
                , new String[]{username});
    }

    public ArrayList<Category> sendMenuData() {
        //TODO
        return null;
    }

    public void addRefundDB(Transaction transaction) {
        //TODO

    }

//    public void addTransactionDB(Transaction transaction, String categoryName, String username) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        long dateAdded = transaction.getTransactionDate().getTime();
//        values.put(KEY_TRANSACTION_NAME, transaction.getItemName());
//        values.put(KEY_CATEGORY_NAME, categoryName);
//        values.put(KEY_Date_ADDED, dateAdded);
//        values.put(KEY_PRICE, transaction.getPrice());
//        values.put(KEY_MEMO, transaction.getMemo());
//        values.put(KEY_USERNAME, username);
//
//        db.insert(TABLE_TRANSACTION, null, values);
//        updateBudgetAmountSpent(username, categoryName, transaction.getPrice());
//        updateOverallBudgetAmountSpent(username, transaction.getPrice());
//
//    }

    public int getTransactionCount(String categoryName, String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor transactionCursor = db.query(TABLE_TRANSACTION, new String[]{KEY_TRANSACTION_NAME},
                KEY_USERNAME + "=?" + " and " + KEY_CATEGORY_NAME + "=?", new String[]{username, categoryName},
                null, null, null, null);
        int count = transactionCursor.getCount();
        transactionCursor.close();
        return count;
    }

    public void updateSetting(Settings settings, String categoryName, String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues settingsValue = new ContentValues();
        settingsValue.put(KEY_CATEGORY_NAME, categoryName);


        settingsValue.put(KEY_START_DATE, settings.getCategoryPeriod().getStartDate().getTime());
        settingsValue.put(KEY_END_DATE, settings.getCategoryPeriod().getEndDate().getTime());
        settingsValue.put(KEY_PERCENT_REMINDER, settings.getThreshold());
        settingsValue.put(KEY_USERNAME, username);
        settingsValue.put(KEY_FREQUENCY_VALUE, settings.getFrequencyValue());
        settingsValue.put(KEY_FREQUENCY_TYPE, settings.getFrequencyType());

        db.update(TABLE_CATEGORY_SETTING, settingsValue, KEY_CATEGORY_NAME + "=?" + " and " + KEY_USERNAME + "=?",
                new String[]{categoryName, username});
    }

    public void sendBudgetDataDB() {
        //TODO
    }

//    public BudgetManager sendManager(String username) {
////        return Budget.getInstance();
//        Category category = this.getBudgetLimit( username);
//        // Log.d("Debug"," MAX AMOUNT : "+category.getCategoryLimit());
//        BudgetManager bm = null;// new BudgetManager(getCategoryList(username),
//               // category.getCategoryLimit(), category.getAmountSpent(), category.getSettings(), username);
//        return bm;
//        // TODO: populate Budget instance with category list, limits, settings
//    }

    public void updateBudgetAmountSpent(String username, String categoryName, double amountSpent) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_CATEGORY, new String[]{KEY_AMOUNT_SPENT},
                KEY_USERNAME + "=?" + " and " + KEY_CATEGORY_NAME + "=?", new String[]{username, categoryName},
                null, null, null, null);
        double amount = 0.0;
        if (cursor.moveToFirst()) {
            amount = Double.parseDouble(cursor.getString(0));
        }
        ContentValues value = new ContentValues();
        value.put(KEY_AMOUNT_SPENT, amount + amountSpent);
        db.update(TABLE_CATEGORY, value, KEY_CATEGORY_NAME + "=?" + " and " + KEY_USERNAME + "=?",
                new String[]{categoryName, username});

    }

//    public void updateOverallBudgetAmountSpent(String username, double amountSpent) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor cursor = db.query(TABLE_BUDGET, new String[]{KEY_AMOUNT_SPENT},
//                KEY_USERNAME + "=?", new String[]{username},
//                null, null, null, null);
//        double amount = 0.0;
//        if (cursor.moveToFirst()) {
//            amount = Double.parseDouble(cursor.getString(0));
//        }
//        ContentValues value = new ContentValues();
//        double sum = amount + amountSpent;
//        //  Log.d("DEBUG", "maxAmount : " + amount);
//
//        //  Log.d("DEBUG", "updateOverallMaximumBudget: " + sum);
//        value.put(KEY_AMOUNT_SPENT, sum);
//        db.update(TABLE_BUDGET, value, KEY_USERNAME + "=?",
//                new String[]{username});
//        Category category = this.getBudgetLimit(username);
////      Log.d("DEBUG", "current category : " + category.getCategoryLimit());
//    }

//    public void updateOverallMaximumBudget(String username, double amountAdded) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor cursor = db.query(TABLE_BUDGET, new String[]{KEY_MAX_BUDGET_AMOUNT},
//                KEY_USERNAME + "=?", new String[]{username},
//                null, null, null, null);
//        double maxAmount = 0.0;
//        if (cursor.moveToFirst()) {
//            maxAmount = Double.parseDouble(cursor.getString(0));
//        }
//        ContentValues value = new ContentValues();
//        double sum = maxAmount + amountAdded;
//        // Log.d("DEBUG", "maxAmount : " + maxAmount);
//
//        //    Log.d("DEBUG", "updateOverallMaximumBudget: " + sum);
//        value.put(KEY_MAX_BUDGET_AMOUNT, sum);
//        db.update(TABLE_BUDGET, value, KEY_USERNAME + "=?",
//                new String[]{username});
//        Category category = this.getBudgetLimit(username);
//        //  Log.d("DEBUG", "current category : " + category.getCategoryLimit());
//
//    }

    public void removeUser(String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TRANSACTION, KEY_USERNAME + "=?",
                new String[]{username});
        db.delete(TABLE_CATEGORY_SETTING, KEY_USERNAME + "=?",
                new String[]{username});
        db.delete(TABLE_CATEGORY, KEY_USERNAME + "=?",
                new String[]{username});
        db.delete(TABLE_USER, KEY_USERNAME + "=?",
                new String[]{username});
        db.delete(TABLE_BUDGET, KEY_USERNAME + "=?",
                new String[]{username});
        db.delete(TABLE_BUDGET_SETTING, KEY_USERNAME + "=?",
                new String[]{username});
    }

    public void resetDatabase() {
        Log.d("Debug", "Resetting");
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSACTION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REFUND);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY_SETTING);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BUDGET_SETTING);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BUDGET);
        // Create tables again

        onCreate(db);
    }

    public void updateOverallSettings(String username, Settings settings) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues settingsValue = new ContentValues();
        settingsValue.put(KEY_START_DATE, settings.getCategoryPeriod().getStartDate().getTime());
        settingsValue.put(KEY_END_DATE, settings.getCategoryPeriod().getEndDate().getTime());
        settingsValue.put(KEY_PERCENT_REMINDER, settings.getThreshold());
        settingsValue.put(KEY_USERNAME, username);
        settingsValue.put(KEY_FREQUENCY_VALUE, settings.getFrequencyValue());
        settingsValue.put(KEY_FREQUENCY_TYPE, settings.getFrequencyType());
        db.update(TABLE_BUDGET_SETTING, settingsValue, KEY_USERNAME + "=?",
                new String[]{username});
    }

    public void setOverallBudgetLimit(String username, double amount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put(KEY_MAX_BUDGET_AMOUNT, amount);
        db.update(TABLE_BUDGET, value, KEY_USERNAME + "=?",
                new String[]{username});

    }

    public void setBudgetLimit(String username, String categoryName, double amount) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues value = new ContentValues();

        value.put(KEY_MAX_BUDGET_AMOUNT, amount);
        db.update(TABLE_CATEGORY, value, KEY_CATEGORY_NAME + "=?" + " and " + KEY_USERNAME + "=?",
                new String[]{categoryName, username});

    }

    public int getSecurityQuestion(String username) {
        int num = -1;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_USER, new String[]{KEY_SECURITY_QUESTION_INDEX},
                KEY_USERNAME + "=?", new String[]{username},
                null, null, null, null);
        double maxAmount = 0.0;
        if (cursor.moveToFirst()) {
            num = cursor.getInt(0);
        }

        return num;
    }

    public boolean checkSecurityAnswer(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor userCursor = db.query(TABLE_USER, new String[]{KEY_USERNAME, KEY_SECURITY_QUESTION_ANSWER},
                KEY_USERNAME + "=?", new String[]{username},
                null, null, null, null);
        if (userCursor.moveToFirst()) {
            String hash = userCursor.getString(1);
            if (hash != null) {
                return (BCrypt.checkpw(password, hash));


            }
        }
        return false;
    }

    public boolean checkUser(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor userCursor = db.query(TABLE_USER, new String[]{KEY_USERNAME},
                KEY_USERNAME + "=?", new String[]{username},
                null, null, null, null);
        if (userCursor.moveToFirst()) {
            return true;
        }
        return false;
    }

    public boolean checkCode(int code) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CATEGORY, new String[]{KEY_INTERVAL_CHECK_CODE},
                KEY_INTERVAL_CHECK_CODE + "=? or " + KEY_CATEGORY_CHECK_CODE + "=?",
                new String[]{String.valueOf(code), String.valueOf(code)},
                null, null, null, null);
        if (cursor.moveToFirst()) {
            return true;
        }
        return false;
    }

    public int getCategoryCheckCode(String username, String budgetname) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CATEGORY, new String[]{KEY_CATEGORY_CHECK_CODE},
                KEY_USERNAME + "=? and " + KEY_CATEGORY_NAME + "=?",
                new String[]{username, budgetname},
                null, null, null, null);
        if (cursor.moveToFirst()) {
            return cursor.getInt(0);
        }
        return -1;
    }

    public int getIntervalCheckCode(String username, String budgetname) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CATEGORY, new String[]{KEY_INTERVAL_CHECK_CODE},
                KEY_USERNAME + "=? and " + KEY_CATEGORY_NAME + "=?",
                new String[]{username, budgetname},
                null, null, null, null);
        if (cursor.moveToFirst()) {
            return cursor.getInt(0);
        }
        return -1;
    }

    public int getOverallIntervalCheckCode(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_BUDGET, new String[]{KEY_INTERVAL_CHECK_CODE},
                KEY_USERNAME + "=?",
                new String[]{username},
                null, null, null, null);
        if (cursor.moveToFirst()) {
            return cursor.getInt(0);
        }
        return -1;
    }

    public int getOverallCheckCode(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_BUDGET, new String[]{KEY_CATEGORY_CHECK_CODE},
                KEY_USERNAME + "=? ",
                new String[]{username},
                null, null, null, null);
        if (cursor.moveToFirst()) {
            return cursor.getInt(0);
        }
        return -1;
    }

    public boolean AddBudget(Budget budget, String username) {
        Log.d("DB: Adding budget", budget.getBudgetName());
        Log.d("To user ", username);
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_BUDGET_NAME, budget.getBudgetName());
            values.put(KEY_MAX_BUDGET_AMOUNT, budget.getBudgetLimit());
            values.put(KEY_AMOUNT_SPENT, budget.getBudgetSpent());
            values.put(KEY_USERNAME, username);

            Category food = new Category("Food", 350.0, 0.0, new Settings(), budget.getBudgetName());
            Category rent = new Category("Rent", 1000.0, 0.0, new Settings(), budget.getBudgetName());
            Category transportation = new Category("Transportation", 100, 0.0, new Settings(), budget.getBudgetName());
            Category recreation = new Category("Recreation", 150, 0.0, new Settings(), budget.getBudgetName());
            Category other = new Category("Other", 100, 0.0, new Settings(), budget.getBudgetName());
            AddCategory(food, budget.getBudgetName(), username);
            AddCategory(rent, budget.getBudgetName(), username);
            AddCategory(transportation, budget.getBudgetName(), username);
            AddCategory(recreation, budget.getBudgetName(), username);
            AddCategory(other, budget.getBudgetName(), username);

            db.insert(TABLE_BUDGET, null, values);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void AddBudgetSetting(Settings settings, String budgetname, String username, SQLiteDatabase db) {
        ContentValues settingsValue = new ContentValues();
        settingsValue.put(KEY_BUDGET_NAME, budgetname);
        settingsValue.put(KEY_START_DATE, settings.getCategoryPeriod().getStartDate().getTime());
        settingsValue.put(KEY_END_DATE, settings.getCategoryPeriod().getEndDate().getTime());
        settingsValue.put(KEY_PERCENT_REMINDER, settings.getThreshold());
        settingsValue.put(KEY_USERNAME, username);
        settingsValue.put(KEY_FREQUENCY_VALUE, settings.getFrequencyValue());
        settingsValue.put(KEY_FREQUENCY_TYPE, settings.getFrequencyType());

        db.insert(TABLE_BUDGET_SETTING, null, settingsValue);
    }

    public void UpdateBudget(Budget budget, String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_BUDGET_NAME, budget.getBudgetName());
        values.put(KEY_MAX_BUDGET_AMOUNT, budget.getBudgetLimit());
        values.put(KEY_AMOUNT_SPENT, budget.getBudgetSpent());
        values.put(KEY_USERNAME, username);
        db.update(TABLE_BUDGET, values, KEY_USERNAME + "=? and " + KEY_BUDGET_NAME + "=?",
                new String[]{username, budget.getBudgetName()});
        //UpdateBudgetSettings(budget.getSettings(),budget.getBudgetName(),username,db);

    }

    public void UpdateBudgetSettings(Settings settings, String budgetname, String username, SQLiteDatabase db) {
        ContentValues settingsValue = new ContentValues();
        settingsValue.put(KEY_BUDGET_NAME, budgetname);
        settingsValue.put(KEY_START_DATE, settings.getCategoryPeriod().getStartDate().getTime());
        settingsValue.put(KEY_END_DATE, settings.getCategoryPeriod().getEndDate().getTime());
        settingsValue.put(KEY_PERCENT_REMINDER, settings.getThreshold());
        settingsValue.put(KEY_USERNAME, username);
        settingsValue.put(KEY_FREQUENCY_VALUE, settings.getFrequencyValue());
        settingsValue.put(KEY_FREQUENCY_TYPE, settings.getFrequencyType());
        db.update(TABLE_BUDGET_SETTING, settingsValue, KEY_USERNAME + "=? and " + KEY_BUDGET_NAME + "=?",
                new String[]{username, budgetname});
    }


    public boolean AddCategory(Category category, String budgetname, String username) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_CATEGORY_NAME, category.getCategoryName());
            values.put(KEY_MAX_BUDGET_AMOUNT, category.getCategoryLimit());
            values.put(KEY_AMOUNT_SPENT, category.getAmountSpent());
            values.put(KEY_USERNAME, username);
            values.put(KEY_BUDGET_NAME, budgetname);
            Random rand = new Random();
            int random = rand.nextInt(Integer.MAX_VALUE);
            while (CheckCode(random)) {
                random = rand.nextInt(Integer.MAX_VALUE);
            }
            int random2 = rand.nextInt(Integer.MAX_VALUE);
            while (CheckCode(random2)) {
                random2 = rand.nextInt(Integer.MAX_VALUE);
            }
            values.put(KEY_INTERVAL_CHECK_CODE, random);
            values.put(KEY_CATEGORY_CHECK_CODE, random2);

            db.insert(TABLE_CATEGORY, null, values);
            AddCategorySetting(category.getSettings(), budgetname, category.getCategoryName(), username, db);
            Log.d("Adding category", category.getCategoryName());
            Log.d("With limit", Double.toString(category.getCategoryLimit()));
            UpdateBudgetLimit(username, budgetname, category.getCategoryLimit());
            Notification nc = new Notification();
            nc.setBothAlarms(category, username, context, budgetname);
            AddCategoryPeriod(username, category.getCategoryName(), budgetname, category.getStartDate(), category.getEndDate());
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public boolean UpdateCategory(Category category, String budgetName, String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_CATEGORY, new String[]{KEY_AMOUNT_SPENT},
                KEY_USERNAME + "=?" + " and " + KEY_CATEGORY_NAME + "=?" + " and " + KEY_BUDGET_NAME + "=?",
                new String[]{username, category.getCategoryName(), budgetName},
                null, null, null, null);
        double amount = 0.0;
        if (cursor.moveToFirst()) {
            amount = Double.parseDouble(cursor.getString(0));
        }
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_CATEGORY_NAME, category.getCategoryName());
            values.put(KEY_MAX_BUDGET_AMOUNT, category.getCategoryLimit());
            values.put(KEY_AMOUNT_SPENT, category.getAmountSpent());
            values.put(KEY_USERNAME, username);
            values.put(KEY_BUDGET_NAME, budgetName);
            Random rand = new Random();


            db.update(TABLE_CATEGORY, values, KEY_USERNAME + "=? and " + KEY_BUDGET_NAME + "=?" + " and " + KEY_CATEGORY_NAME + "=?",
                    new String[]{username, budgetName, category.getCategoryName()});
            UpdateCategorySetting(category.getSettings(), budgetName, category.getCategoryName(), username);
            //UpdateBudgetLimit(username, budgetName, category.getCategoryLimit() - amount);

        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public void AddCategorySetting(Settings settings, String budgetname, String categoryName, String username, SQLiteDatabase db) {
        ContentValues settingsValue = new ContentValues();
        settingsValue.put(KEY_CATEGORY_NAME, categoryName);
        settingsValue.put(KEY_START_DATE, settings.getCategoryPeriod().getStartDate().getTime());
        settingsValue.put(KEY_END_DATE, settings.getCategoryPeriod().getEndDate().getTime());
        settingsValue.put(KEY_PERCENT_REMINDER, settings.getThreshold());
        settingsValue.put(KEY_USERNAME, username);
        settingsValue.put(KEY_FREQUENCY_VALUE, settings.getFrequencyValue());
        settingsValue.put(KEY_FREQUENCY_TYPE, settings.getFrequencyType());
        settingsValue.put(KEY_BUDGET_NAME, budgetname);

        db.insert(TABLE_CATEGORY_SETTING, null, settingsValue);
    }

    public void UpdateCategorySetting(Settings settings, String budgetname, String categoryName, String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues settingsValue = new ContentValues();
        settingsValue.put(KEY_CATEGORY_NAME, categoryName);
        settingsValue.put(KEY_START_DATE, settings.getCategoryPeriod().getStartDate().getTime());
        settingsValue.put(KEY_END_DATE, settings.getCategoryPeriod().getEndDate().getTime());
        settingsValue.put(KEY_PERCENT_REMINDER, settings.getThreshold());
        settingsValue.put(KEY_USERNAME, username);
        settingsValue.put(KEY_FREQUENCY_VALUE, settings.getFrequencyValue());
        settingsValue.put(KEY_FREQUENCY_TYPE, settings.getFrequencyType());
        settingsValue.put(KEY_BUDGET_NAME, budgetname);

        db.update(TABLE_CATEGORY_SETTING, settingsValue, KEY_USERNAME + "=? and " + KEY_BUDGET_NAME + "=?" + " and " + KEY_CATEGORY_NAME + "=?",
                new String[]{username, budgetname, categoryName});
    }

    public void AddTransaction(Transaction transaction, String username, String budgetName, String categoryName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        long dateAdded = transaction.getTransactionDate().getTime();
        values.put(KEY_TRANSACTION_NAME, transaction.getItemName());
        values.put(KEY_CATEGORY_NAME, categoryName);
        values.put(KEY_Date_ADDED, dateAdded);
        values.put(KEY_PRICE, transaction.getPrice());
        values.put(KEY_MEMO, transaction.getMemo());
        values.put(KEY_USERNAME, username);
        values.put(KEY_BUDGET_NAME, budgetName);

        db.insert(TABLE_TRANSACTION, null, values);
       Category category =  this.GetCategory(categoryName,username,budgetName);
       if(dateAdded<category.getEndDate().getTime()&&dateAdded>category.getStartDate().getTime()){
           UpdateCategoryAmountSpent(username, categoryName, budgetName, transaction.getPrice());
           UpdateBudgetAmountSpent(username, budgetName, transaction.getPrice());

       }
    }

    private void UpdateCategoryAmountSpent(String username, String categoryName, String budgetName, double price) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_CATEGORY, new String[]{KEY_AMOUNT_SPENT},
                KEY_USERNAME + "=?" + " and " + KEY_CATEGORY_NAME + "=?" + " and " + KEY_BUDGET_NAME + "=?",
                new String[]{username, categoryName, budgetName},
                null, null, null, null);
        double amount = 0.0;
        if (cursor.moveToFirst()) {
            amount = Double.parseDouble(cursor.getString(0));
        }
        ContentValues value = new ContentValues();
        value.put(KEY_AMOUNT_SPENT, amount + price);
        db.update(TABLE_CATEGORY, value, KEY_CATEGORY_NAME + "=?" + " and " + KEY_USERNAME + "=?" + " and " + KEY_BUDGET_NAME + "=?",
                new String[]{categoryName, username, budgetName});
    }

    private void UpdateBudgetAmountSpent(String username, String budgetName, double price) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_BUDGET, new String[]{KEY_AMOUNT_SPENT},
                KEY_USERNAME + "=?" + " and " + KEY_BUDGET_NAME + "=?",
                new String[]{username, budgetName},
                null, null, null, null);
        double amount = 0.0;
        if (cursor.moveToFirst()) {
            amount = Double.parseDouble(cursor.getString(0));
        }
        Log.d("Amount", cursor.getString(0));
        Log.d("Price", Double.toString(price));
        ContentValues value = new ContentValues();
        value.put(KEY_AMOUNT_SPENT, amount + price);
        db.update(TABLE_BUDGET, value, KEY_USERNAME + "=?" + " and " + KEY_BUDGET_NAME + "=?",
                new String[]{username, budgetName});
    }

    public void UpdateBudgetLimit(String username, String budgetName, double update) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_BUDGET, new String[]{KEY_MAX_BUDGET_AMOUNT},
                KEY_USERNAME + "=?" + " and " + KEY_BUDGET_NAME + "=?",
                new String[]{username, budgetName},
                null, null, null, null);
        double amount = 0.0;
        if (cursor.moveToFirst()) {
            amount = Double.parseDouble(cursor.getString(0));
        }
        ContentValues value = new ContentValues();
        value.put(KEY_MAX_BUDGET_AMOUNT, amount + update);
        db.update(TABLE_BUDGET, value, KEY_USERNAME + "=?" + " and " + KEY_BUDGET_NAME + "=?",
                new String[]{username, budgetName});

    }

//    public ArrayList<Transaction> GetTransactionList(String categoryName, String budgetName, String username) {
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        ArrayList<Transaction> transactionList = new ArrayList<>();
//
//        Cursor cursor = db.query(TABLE_TRANSACTION, new String[]{KEY_TRANSACTION_NAME,
//                        KEY_Date_ADDED, KEY_PRICE, KEY_MEMO},
//                KEY_CATEGORY_NAME + "=?" + " and " + KEY_USERNAME + "=?" + " and " + KEY_BUDGET_NAME + "=?",
//                new String[]{categoryName, username, budgetName},
//                null, null, KEY_Date_ADDED + " DESC", null);
//
//        if (cursor.moveToFirst()) {
//
//            do {
//                Date date = new java.util.Date(cursor.getLong(1));
//                Transaction transaction = new Transaction(
//                        cursor.getString(0),
//                        date,
//                        Double.parseDouble(cursor.getString(2)),
//                        cursor.getString(3));
//                transactionList.add(transaction);
//            } while (cursor.moveToNext());
//        }
//        return transactionList;
//    }

    public Settings GetCategorySettings(String categoryName, String username, String budgetName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor settingCursor = db.query(TABLE_CATEGORY_SETTING, new String[]{KEY_START_DATE,
                        KEY_END_DATE, KEY_PERCENT_REMINDER, KEY_FREQUENCY_TYPE, KEY_FREQUENCY_VALUE},
                KEY_CATEGORY_NAME + "=?" + " and " + KEY_USERNAME + "=?" + " and " + KEY_BUDGET_NAME + "=?", new String[]{categoryName, username, budgetName},
                null, null, null, null);
        Settings settings = null;
        if (settingCursor.moveToFirst()) {
            do {

                Date startDate = new java.util.Date(settingCursor.getLong(0));
                Date endDate = new java.util.Date(settingCursor.getLong(1));
                CategoryPeriod bp = new CategoryPeriod(startDate, endDate);
                settings = new Settings(Double.parseDouble(settingCursor.getString(2)), bp, settingCursor.getString(3),
                        settingCursor.getInt(4));
            } while (settingCursor.moveToNext());
        }
        return settings;
    }

    public Settings GetBudgetSettings(String username, String budgetName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor settingCursor = db.query(TABLE_BUDGET_SETTING, new String[]{KEY_START_DATE,
                        KEY_END_DATE, KEY_PERCENT_REMINDER, KEY_FREQUENCY_TYPE, KEY_FREQUENCY_VALUE},
                KEY_USERNAME + "=?" + " and " + KEY_BUDGET_NAME + "=?", new String[]{username, budgetName},
                null, null, null, null);
        Settings settings = null;
        if (settingCursor.moveToFirst()) {
            do {

                Date startDate = new java.util.Date(settingCursor.getLong(0));
                Date endDate = new java.util.Date(settingCursor.getLong(1));
                CategoryPeriod bp = new CategoryPeriod(startDate, endDate);
                settings = new Settings(Double.parseDouble(settingCursor.getString(2)), bp, settingCursor.getString(3),
                        settingCursor.getInt(4));
            } while (settingCursor.moveToNext());
        }
        return settings;
    }

    public Category GetCategory(String categoryName, String username, String budgetName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Log.d("category name", categoryName);
        Log.d("username", username);
        Log.d("budget name", budgetName);
        Settings settings = GetCategorySettings(categoryName, username, budgetName);
        Cursor cursor = db.query(TABLE_CATEGORY, new String[]{KEY_CATEGORY_NAME, KEY_MAX_BUDGET_AMOUNT,
                        KEY_AMOUNT_SPENT},
                KEY_CATEGORY_NAME + "=?" + " and " + KEY_USERNAME + "=?" + " and " + KEY_BUDGET_NAME + "=?", new String[]{categoryName, username, budgetName},
                null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();


        ArrayList<Transaction> transactionList = GetTransactionListForDate(settings.getCategoryPeriod().getStartDate(),
                settings.getCategoryPeriod().getEndDate(), username, budgetName, categoryName);


        Category category = new Category(cursor.getString(0),
                Double.parseDouble(cursor.getString(1)),
                Double.parseDouble(cursor.getString(2)),
                settings,
                transactionList,
                budgetName);
        cursor.close();
        return category;
    }

    public ArrayList<Category> GetCategoryList(String username, String budgetName) {
        Log.d("Getting categories for", budgetName);
        ArrayList<Category> categoryList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CATEGORY, new String[]{KEY_CATEGORY_NAME, KEY_MAX_BUDGET_AMOUNT,
                        KEY_AMOUNT_SPENT},
                KEY_USERNAME + "=?" + " and " + KEY_BUDGET_NAME + "=?", new String[]{username, budgetName},
                null, null, KEY_CATEGORY_NAME, null);
        if (cursor.moveToFirst()) {
            do {
                String categoryName = cursor.getString(0);

                Settings settings = GetCategorySettings(categoryName, username, budgetName);
                ArrayList<Transaction> transactionList = GetTransactionListForDate(settings.getCategoryPeriod().getStartDate(),
                        settings.getCategoryPeriod().getEndDate(), username, budgetName, categoryName);
                Category category = new Category(cursor.getString(0),
                        Double.parseDouble(cursor.getString(1)),
                        Double.parseDouble(cursor.getString(2)),
                        settings,
                        transactionList,
                        budgetName);

                categoryList.add(category);
            } while (cursor.moveToNext());
        }
        return categoryList;
    }

    public Budget GetBudget(String username, String budgetName) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Category> categoryList = GetCategoryList(username, budgetName);
        Settings settings = GetBudgetSettings(username, budgetName);
        Cursor cursor = db.query(TABLE_BUDGET, new String[]{KEY_MAX_BUDGET_AMOUNT,
                        KEY_AMOUNT_SPENT},
                KEY_BUDGET_NAME + "=?" + " and " + KEY_USERNAME + "=?", new String[]{budgetName, username},
                null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        Budget budget = new Budget(categoryList,
                Double.parseDouble(cursor.getString(0)),
                Double.parseDouble(cursor.getString(1)),
                username,
                budgetName);


        return budget;
    }

    public ArrayList<Budget> GetBudgetList(String username) {
        ArrayList<Budget> budgetList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_BUDGET, new String[]{KEY_MAX_BUDGET_AMOUNT,
                        KEY_AMOUNT_SPENT, KEY_BUDGET_NAME},
                KEY_USERNAME + "=?", new String[]{username},
                null, null, KEY_BUDGET_NAME, null);

        if (cursor.moveToFirst()) {
            do {
                String budgetName = cursor.getString(2);
                ArrayList<Category> categoryList = GetCategoryList(username, budgetName);


                Budget budget = new Budget(categoryList,
                        Double.parseDouble(cursor.getString(0)),
                        Double.parseDouble(cursor.getString(1)),
                        username,
                        budgetName);
                Log.d("Database budget limit", cursor.getString(0));
                Log.d("Database budget size", Integer.toString(budget.getCategoryListSize()));
                budgetList.add(budget);
            } while (cursor.moveToNext());
        }
        return budgetList;
    }

    public BudgetManager GetBudgetManager(String username) {
        BudgetManager bm = new BudgetManager(GetBudgetList(username), username);
        Log.d("Database manager size", Integer.toString(bm.getBudgetListSize()));
        return bm;
    }

    public void AddReminder(Reminder reminder) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        long date = reminder.getEndDate().getTime();
        int isRepeat = reminder.isRepeat() ? 1 : 0;
        values.put(KEY_REMINDER_NAME, reminder.getReminderName());
        values.put(KEY_Date_ADDED, date);
        values.put(KEY_CATEGORY_NAME, reminder.getCategory());
        values.put(KEY_MEMO, reminder.getMessage());
        values.put(KEY_USERNAME, reminder.getUsername());
        values.put(KEY_BUDGET_NAME, reminder.getBudget());
        values.put(KEY_IS_REPEAT, isRepeat);
        db.insert(TABLE_REMINDER, null, values);

    }

    public void UpdateReminder(Reminder reminder) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        long date = reminder.getEndDate().getTime();
        int isRepeat = reminder.isRepeat() ? 1 : 0;
        values.put(KEY_REMINDER_NAME, reminder.getReminderName());
        values.put(KEY_Date_ADDED, date);
        values.put(KEY_CATEGORY_NAME, reminder.getCategory());
        values.put(KEY_MEMO, reminder.getMessage());
        values.put(KEY_USERNAME, reminder.getUsername());
        values.put(KEY_BUDGET_NAME, reminder.getBudget());
        values.put(KEY_IS_REPEAT, isRepeat);
        db.update(TABLE_REMINDER, values, KEY_CATEGORY_NAME + "=?" + " and " + KEY_USERNAME + "=?" + " and "
                        + KEY_BUDGET_NAME + "=?" + " and " + KEY_REMINDER_NAME + "=?",
                new String[]{reminder.getCategory(), reminder.getUsername(), reminder.getBudget(), reminder.getReminderName()});

    }

    public void DeleteReminder(Reminder reminder) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_REMINDER, KEY_CATEGORY_NAME + "=?" + " and " + KEY_USERNAME + "=?" + " and "
                        + KEY_BUDGET_NAME + "=?" + " and " + KEY_REMINDER_NAME + "=?",
                new String[]{reminder.getCategory(), reminder.getUsername(), reminder.getBudget(), reminder.getReminderName()});

    }

    public Reminder GetReminder(String username, String budgetname, String categoryName, String reminderName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_REMINDER, new String[]{KEY_REMINDER_NAME, KEY_USERNAME,
                        KEY_BUDGET_NAME, KEY_CATEGORY_NAME, KEY_MEMO,
                        KEY_Date_ADDED, KEY_IS_REPEAT},
                KEY_CATEGORY_NAME + "=?" + " and " + KEY_USERNAME + "=?" + " and " + KEY_BUDGET_NAME + "=?" + " and " + KEY_REMINDER_NAME + "=?",
                new String[]{categoryName, username, budgetname, reminderName},
                null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        Reminder reminder = new Reminder(cursor.getString(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4),
                new java.util.Date(cursor.getLong(5)),
                cursor.getInt(6) == 1);
        cursor.close();
        return reminder;
    }

    public ArrayList<Reminder> GetReminderList(String username, String budgetName, String categoryName) {
        SQLiteDatabase db = this.getReadableDatabase();

        ArrayList<Transaction> transactionList = new ArrayList<>();

        Cursor cursor = db.query(TABLE_REMINDER, new String[]{KEY_REMINDER_NAME, KEY_USERNAME,
                        KEY_BUDGET_NAME, KEY_CATEGORY_NAME, KEY_MEMO,
                        KEY_Date_ADDED, KEY_IS_REPEAT},
                KEY_CATEGORY_NAME + "=?" + " and " + KEY_USERNAME + "=?" + " and " + KEY_BUDGET_NAME + "=?",
                new String[]{categoryName, username, budgetName},
                null, null, null, null);
        ArrayList<Reminder> reminderList = new ArrayList<>();
        if (cursor.moveToFirst()) {

            do {
                Reminder reminder = new Reminder(cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        new java.util.Date(cursor.getLong(5)),
                        cursor.getInt(6) == 1);
                reminderList.add(reminder);
            } while (cursor.moveToNext());
        }
        return reminderList;
    }

    public ArrayList<Reminder> GetReminderList(String username) {
        SQLiteDatabase db = this.getReadableDatabase();

        ArrayList<Transaction> transactionList = new ArrayList<>();

        Cursor cursor = db.query(TABLE_REMINDER, new String[]{KEY_REMINDER_NAME, KEY_USERNAME,
                        KEY_BUDGET_NAME, KEY_CATEGORY_NAME, KEY_MEMO,
                        KEY_Date_ADDED, KEY_IS_REPEAT},
                KEY_USERNAME + "=?",
                new String[]{username},
                null, null, null, null);
        ArrayList<Reminder> reminderList = new ArrayList<>();
        if (cursor.moveToFirst()) {

            do {
                Reminder reminder = new Reminder(cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        new java.util.Date(cursor.getLong(5)),
                        cursor.getInt(6) == 1);
                reminderList.add(reminder);
            } while (cursor.moveToNext());
        }
        return reminderList;
    }

    public void DeleteCategory(Category category, String budgetName, String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CATEGORY, KEY_CATEGORY_NAME + "=?" + " and " + KEY_USERNAME + "=?" + " and " + KEY_BUDGET_NAME + "=?",
                new String[]{category.getCategoryName(), username, budgetName});
        db.delete(TABLE_CATEGORY_SETTING, KEY_CATEGORY_NAME + "=?" + " and " + KEY_USERNAME + "=?" + " and " + KEY_BUDGET_NAME + "=?",
                new String[]{category.getCategoryName(), username, budgetName});
        db.delete(TABLE_TRANSACTION, KEY_CATEGORY_NAME + "=?" + " and " + KEY_USERNAME + "=?" + " and " + KEY_BUDGET_NAME + "=?",
                new String[]{category.getCategoryName(), username, budgetName});

        UpdateBudgetLimit(username, budgetName, (-1 * category.getCategoryLimit()));
        UpdateBudgetAmountSpent(username, budgetName, (-1 * category.getAmountSpent()));

    }

    public void DeleteBudget(Budget budget, String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TRANSACTION, KEY_USERNAME + "=?" + " and " + KEY_BUDGET_NAME + "=?",
                new String[]{username, budget.getBudgetName()});
        db.delete(TABLE_CATEGORY_SETTING, KEY_USERNAME + "=?" + " and " + KEY_BUDGET_NAME + "=?",
                new String[]{username, budget.getBudgetName()});
        db.delete(TABLE_CATEGORY, KEY_USERNAME + "=?" + " and " + KEY_BUDGET_NAME + "=?",
                new String[]{username, budget.getBudgetName()});
        db.delete(TABLE_BUDGET, KEY_USERNAME + "=?" + " and " + KEY_BUDGET_NAME + "=?",
                new String[]{username, budget.getBudgetName()});

    }

    public boolean CheckCode(int code) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CATEGORY, new String[]{KEY_INTERVAL_CHECK_CODE},
                KEY_INTERVAL_CHECK_CODE + "=? or " + KEY_CATEGORY_CHECK_CODE + "=?",
                new String[]{String.valueOf(code), String.valueOf(code)},
                null, null, null, null);
        if (cursor.moveToFirst()) {
            return true;
        }
        return false;
    }

    public int GetCategoryCheckCode(String username, String categoryName, String budgetName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CATEGORY, new String[]{KEY_CATEGORY_CHECK_CODE},
                KEY_USERNAME + "=? and " + KEY_CATEGORY_NAME + "=?" + " and " + KEY_BUDGET_NAME + "=?",
                new String[]{username, categoryName, budgetName},
                null, null, null, null);
        if (cursor.moveToFirst()) {
            return cursor.getInt(0);
        }
        return -1;
    }

    public int GetIntervalCheckCode(String username, String categoryName, String budgetName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CATEGORY, new String[]{KEY_INTERVAL_CHECK_CODE},
                KEY_USERNAME + "=? and " + KEY_CATEGORY_NAME + "=?" + " and " + KEY_BUDGET_NAME + "=?",
                new String[]{username, categoryName, budgetName},
                null, null, null, null);
        if (cursor.moveToFirst()) {
            return cursor.getInt(0);
        }
        return -1;
    }

    public boolean CheckReminder(String username, String reminderName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_REMINDER, new String[]{KEY_REMINDER_NAME},
                KEY_USERNAME + "=? and " + KEY_REMINDER_NAME + "=?",
                new String[]{username, reminderName},
                null, null, null, null);
        if (cursor.moveToFirst()) {
            return true;
        }
        return false;
    }

    public void AddCategoryPeriod(String username, String categoryName, String budgetName, Date startDate, Date endDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_CATEGORY_NAME, categoryName);
        values.put(KEY_BUDGET_NAME, budgetName);
        values.put(KEY_USERNAME, username);
        values.put(KEY_START_DATE, startDate.getTime());
        values.put(KEY_END_DATE, endDate.getTime());
        db.insert(TABLE_CATEGORY_PERIOD, null, values);

    }

    public void UpdateCategoryPeriod(String username, String categoryName, String budgetName,
                                     Date oldStartDate, Date oldEndDate, Date newStartDate, Date newEndDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_CATEGORY_NAME, categoryName);
        values.put(KEY_BUDGET_NAME, budgetName);
        values.put(KEY_USERNAME, username);
        values.put(KEY_START_DATE, newStartDate.getTime());
        values.put(KEY_END_DATE, newEndDate.getTime());
        db.update(TABLE_CATEGORY_PERIOD, values, KEY_CATEGORY_NAME + "=?" + " and " + KEY_USERNAME + "=?" + " and "
                        + KEY_BUDGET_NAME + "=?" + " and " + KEY_START_DATE + "=?"+ " and " + KEY_END_DATE + "=?",
                new String[]{categoryName, username, budgetName, oldStartDate.getTime()+"",oldEndDate.getTime()+""});

    }

    public ArrayList<CategoryPeriod> GetCategoryPeriod(String username, String categoryName, String budgetName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<CategoryPeriod> categoryPeriodList = new ArrayList<>();

        Cursor cursor = db.query(TABLE_CATEGORY_PERIOD, new String[]{KEY_START_DATE, KEY_END_DATE},
                KEY_USERNAME + " =? " + " and " + KEY_BUDGET_NAME + " =? " + " and " + KEY_CATEGORY_NAME + "=?",
                new String[]{username, budgetName, categoryName},
                null, null, null, null);
        if (cursor.moveToFirst()) {

            do {
                CategoryPeriod categoryPeriod = new CategoryPeriod(new java.util.Date(cursor.getLong(0)),
                        new java.util.Date(cursor.getLong(1)));
                categoryPeriodList.add(categoryPeriod);
            } while (cursor.moveToNext());
        }
        return categoryPeriodList;

    }

    public ArrayList<Transaction> GetTransactionListForDate(Date dateA, Date dateB, String username, String budgetName, String categoryName) {
        SQLiteDatabase db = this.getReadableDatabase();

        ArrayList<Transaction> transactionList = new ArrayList<>();
        long time = dateA.getTime();

        Cursor transactionCursor = db.query(TABLE_TRANSACTION, new String[]{KEY_TRANSACTION_NAME,
                        KEY_Date_ADDED, KEY_PRICE, KEY_MEMO},
                 KEY_USERNAME + "=?" + " and " + KEY_BUDGET_NAME + " =? " + " and " + KEY_CATEGORY_NAME + "=?"
                , new String[]{username, budgetName, categoryName},
                null, null, KEY_Date_ADDED + " ASC", null);

        if (transactionCursor.moveToFirst()) {

            do {

                Date date = new java.util.Date(transactionCursor.getLong(1));
                if(date.getTime()>=dateA.getTime()&&date.getTime()<=dateB.getTime()) {
                    Transaction transaction = new Transaction(
                            transactionCursor.getString(0),
                            date,
                            Double.parseDouble(transactionCursor.getString(2)),
                            transactionCursor.getString(3),
                            budgetName,
                            categoryName);
                    transactionList.add(transaction);
                }
            } while (transactionCursor.moveToNext());
        }
        return transactionList;

    }

    public ArrayList<Budget> SearchBudget(String username, String substring) {
        ArrayList<Budget> budgetList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_BUDGET, new String[]{KEY_MAX_BUDGET_AMOUNT,
                        KEY_AMOUNT_SPENT, KEY_BUDGET_NAME},
                KEY_USERNAME + "=? and " + KEY_BUDGET_NAME + " LIKE ? ", new String[]{username, "%" + substring + "%"},
                null, null, KEY_BUDGET_NAME, null);

        if (cursor.moveToFirst()) {
            do {
                String budgetName = cursor.getString(2);
                ArrayList<Category> categoryList = GetCategoryList(username, budgetName);


                Budget budget = new Budget(categoryList,
                        Double.parseDouble(cursor.getString(0)),
                        Double.parseDouble(cursor.getString(1)),
                        username,
                        budgetName);
                Log.d("Database budget limit", cursor.getString(0));
                Log.d("Database budget size", Integer.toString(budget.getCategoryListSize()));
                budgetList.add(budget);
            } while (cursor.moveToNext());
        }
        return budgetList;

    }

    public ArrayList<Category> SearchCategory(String username, String substring) {
        ArrayList<Category> categoryList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CATEGORY, new String[]{KEY_CATEGORY_NAME, KEY_MAX_BUDGET_AMOUNT,
                        KEY_AMOUNT_SPENT, KEY_BUDGET_NAME},
                KEY_USERNAME + "=? and " + KEY_CATEGORY_NAME + " LIKE ? ", new String[]{username, "%" + substring + "%"},
                null, null, KEY_CATEGORY_NAME, null);
        if (cursor.moveToFirst()) {
            do {
                String categoryName = cursor.getString(0);

                String budgetName = cursor.getString(3);


                Settings settings = GetCategorySettings(categoryName, username, budgetName);
                ArrayList<Transaction> transactionList = GetTransactionListForDate(settings.getCategoryPeriod().getStartDate(),
                        settings.getCategoryPeriod().getEndDate(), username, budgetName, username);
                Category category = new Category(cursor.getString(0),
                        Double.parseDouble(cursor.getString(1)),
                        Double.parseDouble(cursor.getString(2)),
                        settings,
                        transactionList,
                        budgetName);

                categoryList.add(category);
            } while (cursor.moveToNext());
        }
        return categoryList;
    }

    public ArrayList<Transaction> SearchTransaction(String username, String substring) {
        SQLiteDatabase db = this.getReadableDatabase();

        ArrayList<Transaction> transactionList = new ArrayList<>();

        Cursor cursor = db.query(TABLE_TRANSACTION, new String[]{KEY_TRANSACTION_NAME,
                        KEY_Date_ADDED, KEY_PRICE, KEY_MEMO,KEY_BUDGET_NAME,KEY_CATEGORY_NAME},
                KEY_USERNAME + "=? and " + KEY_TRANSACTION_NAME + " LIKE ? ", new String[]{username, "%" + substring + "%"},
                null, null, KEY_Date_ADDED + " DESC", null);

        if (cursor.moveToFirst()) {

            do {
                Date date = new java.util.Date(cursor.getLong(1));
                Transaction transaction = new Transaction(
                        cursor.getString(0),
                        date,
                        Double.parseDouble(cursor.getString(2)),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5));
                transactionList.add(transaction);
            } while (cursor.moveToNext());
        }
        return transactionList;
    }
}