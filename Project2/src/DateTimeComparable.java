import java.util.GregorianCalendar;

public interface DateTimeComparable {
	boolean newerThan(GregorianCalendar inDateTimeUTC);
	boolean olderThan(GregorianCalendar inDateTimeUTC);
	boolean sameAs(GregorianCalendar inDateTimeUTC);
}
