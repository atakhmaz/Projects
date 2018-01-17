package basil.sanity.Controllers;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import java.util.Date;
import java.util.Random;
import java.text.MessageFormat;

import basil.sanity.Database.Database;
import basil.sanity.R;
import basil.sanity.Screens.BudgetMenuScreen;
import basil.sanity.Screens.MainMenuScreen;
import basil.sanity.Screens.SearchScreen;
import basil.sanity.Util.ProgressBarUpdate;

public class AlarmReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("Utsav", "Recieved alarm");
       Bundle bundle = intent.getExtras();
       String type =  intent.getStringExtra(Constant.INTENT_TYPE);
       if (type.equals(Constant.REMINDER))
       {
           displayReminder(context, intent);
           return;
       }

        String username =  intent.getStringExtra(Constant.USERNAME);
        String budgetType =  intent.getStringExtra(Constant.BUDGET_TYPE);

//        if(budgetType.equals(Constant.OVERALL)){
//            recievedOverallBudgetCheck(context,intent);
//        }else {
            String categoryName = intent.getStringExtra(Constant.CATEGORY_NAME);
            String budgetName = intent.getStringExtra(Constant.BUDGET_NAME);

            Database db = new Database(context);

            if(db.checkBudget(username,categoryName)) {
                //     Log.d("Utsav", username + " Recieved " + categoryName);

                //  ContextSingleton cSingleton = new ContextSingleton(context);
                //    db.sendManager(username);
                Category category = db.getCategory(budgetName, categoryName, username);

                long time_difference = category.getSettings().getCategoryPeriod().getEndDate().getTime()
                        - category.getSettings().getCategoryPeriod().getStartDate().getTime();
                long time_until_end = category.getSettings().getCategoryPeriod().getEndDate().getTime()
                        - System.currentTimeMillis();

                if (type.equals(Constant.CHECK_BUDGET_STATUS)) {

                    if (category.isOverThreshold()) {

                        Intent clickIntent = new Intent(context.getApplicationContext(), BudgetMenuScreen.class);
                        if (BudgetManager.isInitialized()) {
                            if (BudgetManager.getInstance().getUsername() != "" && BudgetManager.getInstance().getUsername() != null) {
                                //clickIntent.putExtra(BudgetMenuScreen.EXTRA_LOGINFLAG, true);
                                clickIntent.putExtra(BudgetMenuScreen.EXTRA_USER, BudgetManager.getInstance().getUsername());
                            }
                        }

                        String message = MessageFormat.format(Constant.NOTIFICATION_FORMATTED_MESSAGE,
                                username, categoryName, budgetName, category.getAmountSpent(),
                                category.getCategoryLimit(), category.getPercentageSpent() * 100,
                                time_until_end / Constant.DAY_MILLISECOND);

                        NotificationCompat.Builder b = new NotificationCompat.Builder(context);
                        b.setAutoCancel(true)
                                .setDefaults(NotificationCompat.DEFAULT_ALL)
                                .setWhen(System.currentTimeMillis())
                                .setSmallIcon(R.mipmap.ic_notification)
                                .setLargeIcon((BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher)))
                                .setContentTitle(Constant.OVER_BUDGET_LIMIT_MESSAGE)
                                .setContentText(message)
                                .setContentIntent(PendingIntent.getActivity(
                                        context,
                                        0, clickIntent,
                                        PendingIntent.FLAG_UPDATE_CURRENT))
                                        .setStyle(new NotificationCompat.BigTextStyle().bigText(message));

                        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                        Random rand = new Random();
                        nm.notify(rand.nextInt(Integer.MAX_VALUE), b.build());
                    }
                } else if (type.equals(Constant.RESET_INTERVAL)) {

                    Intent intervalIntent = new Intent(context, AlarmReceiver.class);
                    intervalIntent.putExtra(Constant.INTENT_TYPE, Constant.RESET_INTERVAL);
                    intervalIntent.putExtra(Constant.USERNAME, username);
                    intervalIntent.putExtra(Constant.CATEGORY_NAME, categoryName);
                    //    intervalIntent.putExtra(Constant.TIME_DIFFERENCE, time_difference);

//                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1, intervalIntent, 0);
//                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//                alarmManager.set(AlarmManager.RTC_WAKEUP,
//                        System.currentTimeMillis() + time_difference, pendingIntent);

                    Intent clickIntent = new Intent(context.getApplicationContext(), BudgetMenuScreen.class);
                    if (BudgetManager.isInitialized()) {
                        if (BudgetManager.getInstance().getUsername() != "" && BudgetManager.getInstance().getUsername() != null) {
                            //clickIntent.putExtra(BudgetMenuScreen.EXTRA_LOGINFLAG, true);
                            clickIntent.putExtra(BudgetMenuScreen.EXTRA_USER, BudgetManager.getInstance().getUsername());
                        }
                    }

                    NotificationCompat.Builder b = new NotificationCompat.Builder(context);
                    b.setAutoCancel(true)
                            .setDefaults(NotificationCompat.DEFAULT_ALL)
                            .setWhen(System.currentTimeMillis())
                            .setSmallIcon(R.mipmap.ic_notification)
                            .setLargeIcon((BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher)))
                            .setContentTitle(Constant.BUDGET_PERIOD_RESET_MESSAGE)
                            .setContentIntent(PendingIntent.getActivity(
                                    context,
                                    0, clickIntent,
                                    PendingIntent.FLAG_UPDATE_CURRENT))
                            .setStyle(new NotificationCompat.BigTextStyle()
                                    .bigText(username + " , " + categoryName + Constant.BUDGET_PERIOD_RESET_MESSAGE_V2 + (category.getAmountSpent()) + Constant.BUDGET_PERIOD_RESET_MESSAGE_V3 + (category.getCategoryLimit())));


                    Date currentTime = new Date(System.currentTimeMillis());
                    Date finalDate = new Date(System.currentTimeMillis() + time_difference);
                    NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    Random rand = new Random();
                    nm.notify(rand.nextInt(Integer.MAX_VALUE), b.build());
                    Budget budget = db.GetBudget(username,budgetName);
                    budget.setOverallSpent(budget.getBudgetSpent()-category.getAmountSpent());
                    db.UpdateBudget(budget, username);
                    category.setAmountSpent(0);
                    db.AddCategoryPeriod(username,categoryName,budgetName,currentTime,finalDate);
                    db.UpdateCategory(category,budgetName, username);

                    db.UpdateCategorySetting(new Settings(category.getSettings().getThreshold(),
                            new CategoryPeriod(currentTime, finalDate),
                            category.getSettings().getFrequencyType(),
                            category.getSettings().getFrequencyValue()
                    ), budgetName, categoryName, username);
                    if(BudgetManager.getInstance() != null) {
                        BudgetManager.getInstance().rollover(budgetName, categoryName);
                        BudgetMenuScreen.updateBudgetList();
                        MainMenuScreen.updateBudgetList();
                        SearchScreen.updateBudgetList();
                        SearchScreen.updateCategoryList();
                        SearchScreen.updateTransactionList();
                        ProgressBarUpdate.updateProgressBar(BudgetMenuScreen.mainMenuView,
                                R.id.overall_budget_progress,
                                budget.getBudgetPercentageSpent(),
                                budget.getBudgetSpent(),
                                budget.getBudgetLimit());
                    }
                } else if (type.equals(Constant.REPEATING_TRANSACTION)) {
                        addRepeatingTransaction(context,intent);
                }
                Log.d("Utsav", "Finished Rollover");

            }
        //}
    }

    public void addRepeatingTransaction(Context context, Intent intent) {
        String username = intent.getStringExtra(Constant.USERNAME);
        Double price = intent.getDoubleExtra(Constant.TRANSACTION_PRICE, 0.0);
        String memo = intent.getStringExtra(Constant.TRANSACTION_MEMO);
        String categoryName = intent.getStringExtra(Constant.CATEGORY_NAME);
        String budgetName = intent.getStringExtra(Constant.BUDGET_NAME);
        String transactionName = intent.getStringExtra(Constant.TRANSACTION_NAME);

        Database db = new Database(context);
        Transaction transaction = new Transaction(transactionName,new Date(System.currentTimeMillis()),price,memo,budgetName,categoryName);
        db.AddTransaction( transaction
                ,username,budgetName,categoryName);
        Notification nc = new Notification();
        nc.setRepeatingTransaction(username,budgetName,categoryName,transactionName,memo,price);
        if(BudgetManager.getInstance() != null) {
            Budget budget = BudgetManager.getInstance().getBudgetByName(budgetName);
            budget.addTransactionNotDB(transaction, BudgetManager.getInstance().findBudget(budgetName));
            BudgetMenuScreen.updateBudgetList();
            MainMenuScreen.updateBudgetList();
            SearchScreen.updateBudgetList();
            SearchScreen.updateCategoryList();
            SearchScreen.updateTransactionList();
            ProgressBarUpdate.updateProgressBar(BudgetMenuScreen.mainMenuView,
                    R.id.overall_budget_progress,
                    budget.getBudgetPercentageSpent(),
                    budget.getBudgetSpent(),
                    budget.getBudgetLimit());
        }
    }

//    private void recievedOverallBudgetCheck(Context context, Intent intent) {
//        Database db = new Database(context);
//        //    db.sendManager(username);
//        String type =  intent.getStringExtra(Constant.INTENT_TYPE);
//        String username =  intent.getStringExtra(Constant.USERNAME);
//        String budgetType =  intent.getStringExtra(Constant.BUDGET_TYPE);
//        String categoryName = "Overall Category";
//        if (type.equals(Constant.CHECK_BUDGET_STATUS)) {
//
//            Category category = db.getBudgetLimit(username);
//            long time =  category.getSettings().getCategoryPeriod().getEndDate().getTime()- category.getSettings().getCategoryPeriod().getEndDate().getTime();
//            long time_until_end =  category.getSettings().getCategoryPeriod().getEndDate().getTime()- System.currentTimeMillis();
//            if (category.isOverThreshold()) {
//
//                Intent clickIntent = new Intent(context.getApplicationContext(), BudgetMenuScreen.class);
//                if (BudgetManager.isInitialized()) {
//                    if (BudgetManager.getInstance().getUsername() != "" && BudgetManager.getInstance().getUsername() != null) {
//                        //clickIntent.putExtra(BudgetMenuScreen.EXTRA_LOGINFLAG, true);
//                        clickIntent.putExtra(BudgetMenuScreen.EXTRA_USER, BudgetManager.getInstance().getUsername());
//                    }
//                }
//
//                String message = MessageFormat.format(Constant.NOTIFICATION_FORMATTED_MESSAGE,
//                        username, categoryName, category.getAmountSpent(), category.getCategoryLimit(),
//                        category.getPercentageSpent() * 100, time_until_end / Constant.DAY_MILLISECOND);
//                NotificationCompat.Builder b = new NotificationCompat.Builder(context);
//                b.setAutoCancel(true)
//                        .setDefaults(NotificationCompat.DEFAULT_ALL)
//                        .setWhen(System.currentTimeMillis())
//                        .setSmallIcon(R.mipmap.ic_notification)
//                        .setLargeIcon((BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher)))
//                        .setContentTitle(Constant.OVER_BUDGET_LIMIT_MESSAGE)
//                        .setContentText(message)
//                        .setContentIntent(PendingIntent.getActivity(
//                                context,
//                                0, clickIntent,
//                                PendingIntent.FLAG_UPDATE_CURRENT))
//                        .setStyle(new NotificationCompat.BigTextStyle()
//                                .bigText(message));
//
//
//                NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//                Random rand = new Random();
//                nm.notify(rand.nextInt(Integer.MAX_VALUE), b.build());
//            }
//        } else if (type.equals(Constant.RESET_INTERVAL)) {
//          //  long time_difference = intent.getLongExtra(Constant.TIME_DIFFERENCE, 30 * Constant.DAY_MILLISECOND);
//
//            Intent intervalIntent = new Intent(context, AlarmReceiver.class);
//            intervalIntent.putExtra(Constant.INTENT_TYPE, Constant.RESET_INTERVAL);
//            intervalIntent.putExtra(Constant.USERNAME, username);
//            intervalIntent.putExtra(Constant.CATEGORY_NAME, categoryName);
//            //    intervalIntent.putExtra(Constant.TIME_DIFFERENCE, time_difference);
//
////            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1, intervalIntent, 0);
////            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
////            alarmManager.set(AlarmManager.RTC_WAKEUP,
////                    System.currentTimeMillis() + time_difference, pendingIntent);
//
//            Intent clickIntent = new Intent(context.getApplicationContext(), BudgetMenuScreen.class);
//            if (BudgetManager.isInitialized()) {
//                if (BudgetManager.getInstance().getUsername() != "" && BudgetManager.getInstance().getUsername() != null) {
//                    //clickIntent.putExtra(BudgetMenuScreen.EXTRA_LOGINFLAG, true);
//                    clickIntent.putExtra(BudgetMenuScreen.EXTRA_USER, BudgetManager.getInstance().getUsername());
//                }
//            }
//            Category category = db.getBudgetLimit(username);
//
//            NotificationCompat.Builder b = new NotificationCompat.Builder(context);
//            b.setAutoCancel(true)
//                    .setDefaults(NotificationCompat.DEFAULT_ALL)
//                    .setWhen(System.currentTimeMillis())
//                    .setSmallIcon(R.mipmap.ic_notification)
//                    .setLargeIcon((BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher)))
//                    .setContentTitle(Constant.BUDGET_PERIOD_RESET_MESSAGE)
//                    .setContentIntent(PendingIntent.getActivity(
//                            context,
//                            0, clickIntent,
//                            PendingIntent.FLAG_UPDATE_CURRENT))
//                    .setStyle(new NotificationCompat.BigTextStyle()
//                            .bigText(username + " , " + categoryName + Constant.BUDGET_PERIOD_RESET_MESSAGE_V2 + (category.getAmountSpent()) + Constant.BUDGET_PERIOD_RESET_MESSAGE_V3 + (category.getCategoryLimit())));
//
//            Date currentTime = new Date(System.currentTimeMillis());
//            long time =  category.getSettings().getCategoryPeriod().getEndDate().getTime()- category.getSettings().getCategoryPeriod().getStartDate().getTime();
//            Date finalDate = new Date(System.currentTimeMillis() + time);
//            NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//            Random rand = new Random();
//            nm.notify(rand.nextInt(Integer.MAX_VALUE), b.build());
//            category.setAmountSpent(0);
//            db.updateOverallBudget(category,username);
//            db.updateOverallSettings(username, new Settings(category.getSettings().getThreshold(),
//                    new CategoryPeriod(currentTime, finalDate),
//                    category.getSettings().getFrequencyType(),
//                    category.getSettings().getFrequencyValue()
//            ));
//        }
//
//
//    }

    public void displayReminder(Context context, Intent intent) {
        Intent clickIntent = new Intent(context.getApplicationContext(), BudgetMenuScreen.class);
        if (BudgetManager.isInitialized()) {
            if (BudgetManager.getInstance().getUsername() != "" && BudgetManager.getInstance().getUsername() != null) {
                clickIntent.putExtra(BudgetMenuScreen.EXTRA_USER, BudgetManager.getInstance().getUsername());
            }
        }

        String username = intent.getStringExtra(Constant.USERNAME);
        String message = intent.getStringExtra(Constant.MESSAGE);
        String title = intent.getStringExtra(Constant.TITLE);
        String budgetName = intent.getStringExtra(Constant.BUDGET_NAME);
        String categoryName = intent.getStringExtra(Constant.CATEGORY_NAME);
        Date endDate = new Date(intent.getLongExtra(Constant.END_DATE_MILLIS, System.currentTimeMillis()));
        boolean isRepeat = intent.getBooleanExtra(Constant.IS_REPEAT, false);

        String contentText =  MessageFormat.format(Constant.REMINDER_FORMATTED_MESSAGE,
                categoryName, budgetName, message);

        NotificationCompat.Builder b = new NotificationCompat.Builder(context);
        b.setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_notification)
                .setLargeIcon((BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher)))
                .setContentTitle(Constant.REMINDER_MESSAGE_TITLE + ": " + title)
                .setContentText(contentText)
                .setContentIntent(PendingIntent.getActivity(
                        context,
                        0, clickIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(contentText));

        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Random rand = new Random();
        nm.notify(rand.nextInt(Integer.MAX_VALUE), b.build());

        if (!isRepeat) {
            Database db = new Database(context);
            Reminder reminder = new Reminder(title, username, budgetName, categoryName, message, endDate, isRepeat);
            db.DeleteReminder(reminder);
        }
    }
}
