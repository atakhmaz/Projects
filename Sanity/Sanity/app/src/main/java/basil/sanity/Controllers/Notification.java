package basil.sanity.Controllers;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import basil.sanity.Database.Database;
import basil.sanity.R;
import basil.sanity.Screens.BudgetMenuScreen;

/**
 * Created by kevinnguyen on 10/2/17.
 */

public class Notification  {
    public void setBothAlarms(Category category, String username, Context context, String budgetName){
        addCategoryCheckAlarm(category,username,context, budgetName);
        addCategoryIntervalAlarm(category,username,context, budgetName);
    }

    public void setRepeatingTransaction( String username, String budgetname, String categoryName, String transactionName,
                                         String memo, double price) {
        try {
            Context context = ContextSingleton.getInstance();
            Intent intent = transactionIntent(context, categoryName, username, transactionName,
                    price, Constant.REPEATING_TRANSACTION, budgetname, memo);
            Random rand = new Random();
            PendingIntent pendingIntent =
                    PendingIntent.getBroadcast(context, rand.nextInt(), intent, 0);
            AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
            Date startDate = new Date();
            Calendar cal = Calendar.getInstance();
            cal.setTime(startDate);
            cal.add(Calendar.MONTH, 1);
            Date endDate = cal.getTime();
            alarmManager.set(AlarmManager.RTC_WAKEUP, endDate.getTime(),
                    pendingIntent);

        } catch (Exception e) {
            return;
        }
    }
    public Intent transactionIntent(Context context, String categoryName, String username, String transactionName,
                                    double transactionPrice, String intentType, String budgetName, String memo) {
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(Constant.INTENT_TYPE, intentType);
        intent.putExtra(Constant.USERNAME, username);
        intent.putExtra(Constant.CATEGORY_NAME, categoryName);
        intent.putExtra(Constant.BUDGET_NAME, budgetName);
        intent.putExtra(Constant.TRANSACTION_NAME, transactionName);
        intent.putExtra(Constant.TRANSACTION_PRICE, transactionPrice);
        intent.putExtra(Constant.TRANSACTION_MEMO, memo);
        return intent;
    }

    public void addCategoryIntervalAlarm(Category category, String username, Context context,String budgetName) {
        Log.d("Utsav",username+" Add category interval alarm "+ category.getCategoryName() );

        Intent intent = notificationIntent(context, category.getCategoryName(), username,
                Constant.REGULAR, Constant.RESET_INTERVAL, budgetName);

        Database db = new Database(context);
        int num = db.GetIntervalCheckCode(username, category.getCategoryName(),budgetName);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                num, intent, 0);
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Log.d("Utsav","Alarm code  "+num );

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                category.getSettings().getCategoryPeriod().getEndDate().getTime(),
                category.getSettings().getCategoryPeriod().getEndDate().getTime()- category.getSettings().getCategoryPeriod().getStartDate().getTime(),
                pendingIntent);
    }

    public  void addCategoryCheckAlarm(Category category, String username, Context context,String budgetName) {
        int howOften = category.getSettings().getFrequencyValue();
        String type  = category.getSettings().getFrequencyType();
        long time = 0;
        if(type.equals("Day")){
            time = Constant.DAY_MILLISECOND;
        }else if(type.equals("Week")){
            time = Constant.WEEK_MILLISECOND;
        }

        Intent intent = notificationIntent(context, category.getCategoryName(), username,
                Constant.REGULAR, Constant.CHECK_BUDGET_STATUS, budgetName);

        Database db = new Database(context);
        int num = db.GetCategoryCheckCode(username, category.getCategoryName(),budgetName);
        Log.d("Utsav","Alarm check code  "+num );

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                num, intent, 0);

        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis()+time*howOften,time*howOften, pendingIntent);

    }

    public void updateCategoryFrequency(String username,String categoryName,String budgetName, int frequencyValueInt,  String frequencyTypeString,Context context) {
        int howOften = frequencyValueInt;
        String type  = frequencyTypeString;
        long time = 0;
        if(type.equals("Day")){
            time = Constant.DAY_MILLISECOND;
        }else if(type.equals("Week")){
            time = Constant.WEEK_MILLISECOND;
        }

        Intent intent = notificationIntent(context, categoryName, username, Constant.REGULAR,
                Constant.CHECK_BUDGET_STATUS, budgetName);
        Database db = new Database(context);
        Category category = db.GetCategory(categoryName, username,budgetName);

        int num = db.GetCategoryCheckCode(username,categoryName,budgetName);
        Log.d("Utsav",username+" category update interval code  "+num );

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                num, intent, 0);

        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        alarmManager.cancel(pendingIntent);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis()+time*howOften,time*howOften, pendingIntent);
    }

    public void updateCategoryPeriod(String username, String categoryName,String budgetName, Date startDate, Date endDate, Context context) {

        Intent intent = notificationIntent(context, categoryName, username, Constant.REGULAR, Constant.RESET_INTERVAL, budgetName);

        Database db = new Database(context);
        Category category = db.GetCategory(categoryName, username,budgetName);

        deleteNotification(context, category, username, Constant.REGULAR, Constant.RESET_INTERVAL, budgetName);

        int num = db.GetIntervalCheckCode(username,categoryName,budgetName);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                num, intent, 0);
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
              endDate.getTime(),
               endDate.getTime()-startDate.getTime(),
                pendingIntent);
    }

    public void deleteNotification(Context context, Category category, String username,
                                   String budgetType, String intentType, String budgetname) {
        Intent intent = notificationIntent(context, category.getCategoryName(), username, budgetType, intentType, budgetname);
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        Database db = new Database(context);
        int num = (intentType == Constant.CHECK_BUDGET_STATUS) ?
                    db.getCategoryCheckCode(username, category.getCategoryName()) :
                    db.getIntervalCheckCode(username, category.getCategoryName());

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, num, intent, 0);

        alarmManager.cancel(pendingIntent);
    }

    public Intent notificationIntent(Context context, String categoryName, String username,
                                     String budgetType, String intentType, String budgetName) {
        Intent intent = new Intent(context, AlarmReceiver.class);

        intent.putExtra(Constant.INTENT_TYPE, intentType);
        intent.putExtra(Constant.USERNAME, username);
        intent.putExtra(Constant.CATEGORY_NAME, categoryName);
        intent.putExtra(Constant.BUDGET_NAME, budgetName);
        intent.putExtra(Constant.BUDGET_TYPE, budgetType);

        return intent;
    }

    public void overBudgetNotification(Context context,  String username,
                                            Category category) {
        long time_until_end = category.getSettings().getCategoryPeriod().getEndDate().getTime() - System.currentTimeMillis();
        if (category.isOverThreshold()) {
            Intent clickIntent = new Intent(context.getApplicationContext(), BudgetMenuScreen.class);
            if (BudgetManager.isInitialized()) {
                if (BudgetManager.getInstance().getUsername() != "" && BudgetManager.getInstance().getUsername() != null) {
                    //clickIntent.putExtra(BudgetMenuScreen.EXTRA_LOGINFLAG, true);
                    clickIntent.putExtra(BudgetMenuScreen.EXTRA_USER, BudgetManager.getInstance().getUsername());
                }
            }

            String message = MessageFormat.format(Constant.NOTIFICATION_FORMATTED_MESSAGE,
                    username, category.getCategoryName(), category.getBudgetName(), category.getAmountSpent(), category.getCategoryLimit(),
                    category.getPercentageSpent() * 100, time_until_end / Constant.DAY_MILLISECOND);
            android.support.v7.app.NotificationCompat.Builder b = new android.support.v7.app.NotificationCompat.Builder(context);
            b.setAutoCancel(true)
                    .setDefaults(android.support.v7.app.NotificationCompat.DEFAULT_ALL)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.mipmap.ic_notification)
                    .setLargeIcon((BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher)))
                    .setContentTitle(Constant.OVER_BUDGET_LIMIT_MESSAGE)
                    .setContentText(message)
                    .setContentIntent(PendingIntent.getActivity(
                            context,
                            0, clickIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT))
                    .setStyle(new android.support.v7.app.NotificationCompat.BigTextStyle()
                            .bigText(message));


            NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            Random rand = new Random();
            nm.notify(rand.nextInt(Integer.MAX_VALUE), b.build());

        }
    }
//    public void setRepeatingTransaction( String username, String budgetname, String categoryName, String transactionName,
//            String memo, double price,  long nextAlarmTime) {
//        try {
//            Context context = ContextSingleton.getInstance();
//            Intent intent = transactionIntent(context,categoryName,username,price,Constant.REPEATING_TRANSACTION,budgetname,memo);
//            Random rand = new Random();
//            PendingIntent pendingIntent =
//                    PendingIntent.getBroadcast(context, rand.nextInt(), intent, 0);
//
//        } catch (Exception e) {
//            return;
//        }
//
//    }
//    public Intent transactionIntent(Context context, String categoryName, String username,
//                                     double transactionPrice, String intentType, String budgetName,String memo) {
//        Intent intent = new Intent(context, AlarmReceiver.class);
//
//        intent.putExtra(Constant.INTENT_TYPE, intentType);
//        intent.putExtra(Constant.USERNAME, username);
//        intent.putExtra(Constant.CATEGORY_NAME, categoryName);
//        intent.putExtra(Constant.BUDGET_NAME, budgetName);
//        intent.putExtra(Constant.TRANSACTION_PRICE, transactionPrice);
//        intent.putExtra(Constant.TRANSACTION_MEMO, memo);
//
//        return intent;
//    }
}
