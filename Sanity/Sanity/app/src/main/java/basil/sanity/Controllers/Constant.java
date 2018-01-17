package basil.sanity.Controllers;

/**
 * Created by Utsav on 10/15/2017.
 */

public class Constant {
    public static final String INTENT_TYPE = "type";
    public static final String CHECK_BUDGET_STATUS = "budget_status";
    public static final String CHECK_OVERALL_BUDGET_STATUS = "overall_budget_status";
    public static final String REMINDER = "reminder";

    public static final String RESET_INTERVAL = "reset_interval";
    public static final String USERNAME = "username";
    public static final String BUDGET_NAME = "budget_name";
    public static final String CATEGORY_NAME = "category_name";
    public static final String MESSAGE = "message";
    public static final String TITLE = "title";
    public static final String IS_REPEAT = "is_repeat";
    public static final String REMINDER_FORMATTED_MESSAGE = "This is a reminder for category {0} " +
            "in budget {1}: {2}";
    public static final String NOTIFICATION_FORMATTED_MESSAGE = "{0}, category {1} in budget {2} is over the budget limit. " +
            "It is currently at {3, number, currency} out of {4, number, currency} ({5, number, integer}%)." +
            "You have {6, number, integer} days remaining in the budget period.";

    public static final String REMINDER_MESSAGE_TITLE = "$anity Reminder";
    public static final String OVER_BUDGET_LIMIT_MESSAGE  = "Over Category Limit";
    public static final String INTERVAL_TYPE = "interval_type";
    public static final String INTERVAL_AMOUNT  = "interval_amount";
    public static final String DAY  = "day";
    public static final String WEEK = "week";
    public static final String MONTH  = "month";
    public static final String END_DATE_MILLIS = "end_date";
    public static final long   DAY_MILLISECOND = 86400000  ;
    public static final long   WEEK_MILLISECOND = 604800000 ;
    public static final String BUDGET_PERIOD_RESET_MESSAGE  = "Category Period is over. ";//TODO
    public static final String BUDGET_PERIOD_RESET_MESSAGE_V2  = "'s budget period is over. You spent $";
    public static final String BUDGET_PERIOD_RESET_MESSAGE_V3 = " out of a budget of $";
    public static final String TIME_DIFFERENCE = "time_difference";
    public static final String BUDGET_TYPE = "budget_type";
    public static final String OVERALL = "overall";
    public static final String REGULAR = "regular";
    public static final String TRANSACTION_NAME = "transaction_name";
    public static final String REPEATING_TRANSACTION = "repeating_transaction";
    public static final String TRANSACTION_PRICE = "transaction_price";
    public static final String TRANSACTION_MEMO = "transaction_memo";


}
