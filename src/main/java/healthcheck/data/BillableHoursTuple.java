package healthcheck.data;

import java.io.Serializable;
import java.time.YearMonth;

public class BillableHoursTuple implements Serializable {
    private YearMonth date;
    private double hourCount;

    public BillableHoursTuple (YearMonth date, double hourCount) {
        this.date = date;
        this.hourCount = hourCount;
    }

    public YearMonth getDate() {return this.date;}
    public double getHourCount() {return this.hourCount;}
}