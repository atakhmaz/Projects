package basil.sanity.Controllers;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by kevinnguyen on 10/2/17.
 */

public class CategoryPeriod  implements Comparable{

    private Date startDate;
    private Date endDate;

    public CategoryPeriod(){
        startDate = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        cal.add(Calendar.MONTH, 1);
        endDate = cal.getTime();
    }

    public CategoryPeriod(Date startDate, Date endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public CategoryPeriod(Date endDate) {
        startDate = new Date();
        this.endDate = endDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void rollover() {
        long timeDifference = endDate.getTime() - startDate.getTime();
        startDate = endDate;
        endDate = new Date(startDate.getTime() + timeDifference);
    }

    @Override
    public boolean equals(Object o) {
        return startDate.equals(((CategoryPeriod)o).startDate) &&
                endDate.equals(((CategoryPeriod)o).endDate);
    }

    public String toString() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yy");
        String startStr = simpleDateFormat.format(startDate);
        String endStr = simpleDateFormat.format(endDate);
        return startStr + " - " + endStr;
    }
    // allow sorting by date, newest date first
    public int compareTo(Object anotherPeriod) throws ClassCastException {
        if (!(anotherPeriod instanceof CategoryPeriod))
            throw new ClassCastException("A CategoryPeriod object expected.");
        Date anotherDate = ((CategoryPeriod) anotherPeriod).getEndDate();
        // negate compareTo so that newest date appears first when sorted
        return -this.endDate.compareTo(anotherDate);
    }
}
