package basil.sanity.Controllers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Date;
import java.util.Random;

/**
 * Created by Tri on 11/14/2017.
 */

public class Reminder implements Comparable{
    private  String reminderName;
    private String username;
    private String budget;
    private String category;
    private String message;
    private boolean isRepeat;

    public String getUsername() {
        return username;
    }

    public String getBudget() {
        return budget;
    }

    public String getCategory() {
        return category;
    }

    public String getMessage() {
        return message;
    }

    public Date getEndDate() {
        return endDate;
    }

    public String getReminderName() {
        return reminderName;
    }

    public boolean isRepeat() {
        return isRepeat;
    }

    public Date endDate;

    /*
    * Creates a reminder to go off at the time specified in endDate
    * */
    public Reminder(String reminderName, Context context, String username, String budget,
                    String category, String message, Date endDate, boolean isRepeat)
    {
        this.reminderName = reminderName;

        this.username = username;
        this.budget = budget;
        this.category = category;
        this.message = message;
        this.endDate = endDate;
        this.isRepeat = isRepeat;

        Intent intent = makeIntent(context);
        Random rand = new Random();
        PendingIntent pendingIntent =
                PendingIntent.getBroadcast(context, rand.nextInt(), intent, 0);

        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        if (!isRepeat) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, endDate.getTime(), pendingIntent);
        } else {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, endDate.getTime(),
                    Constant.DAY_MILLISECOND * 30, pendingIntent);
        }
    }

    public Reminder(String reminderName,  String username, String budget,
                    String category, String message, Date endDate, Boolean isRepeat)
    {
        this.reminderName = reminderName;

        this.username = username;
        this.budget = budget;
        this.category = category;
        this.message = message;
        this.endDate = endDate;
        this.isRepeat = isRepeat;
    }
    /*
    * Cancels the pending reminder
    * */
    public void cancel(Context context)
    {
        Intent intent = makeIntent(context);
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        PendingIntent pendingIntent =
                PendingIntent.getBroadcast(context, (int) Math.random(), intent, 0);
        alarmManager.cancel(pendingIntent);
    }

    /*
    * Changes the reminder to go off on a different time specified by newDate
    * */
    public void reschedule(Context context, Date newDate)
    {
        this.endDate = newDate;

        Intent intent = makeIntent(context);
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent =
                PendingIntent.getBroadcast(context, (int) Math.random(), intent, 0);

        alarmManager.cancel(pendingIntent);
        alarmManager.set(AlarmManager.RTC_WAKEUP, endDate.getTime(), pendingIntent);
    }

    /*
    * Utility function to make an intent for the alarmManager
    * */
    private Intent makeIntent(Context context)
    {
        Intent intent = new Intent(context, AlarmReceiver.class);

        intent.putExtra(Constant.INTENT_TYPE, Constant.REMINDER);
        intent.putExtra(Constant.USERNAME, username);
        intent.putExtra(Constant.BUDGET_NAME, budget);
        intent.putExtra(Constant.CATEGORY_NAME, category);
        intent.putExtra(Constant.MESSAGE, message);
        intent.putExtra(Constant.TITLE, reminderName);
        intent.putExtra(Constant.END_DATE_MILLIS, endDate.getTime());
        intent.putExtra(Constant.IS_REPEAT, isRepeat);

        return intent;
    }

    // allow sorting by date, oldest date first
    public int compareTo(Object anotherReminder) throws ClassCastException {
        if (!(anotherReminder instanceof Reminder))
            throw new ClassCastException("A Reminder object expected.");
        Date anotherDate = ((Reminder) anotherReminder).getEndDate();
        // negate compareTo so that newest date appears first when sorted
        return this.endDate.compareTo(anotherDate);
    }


}
