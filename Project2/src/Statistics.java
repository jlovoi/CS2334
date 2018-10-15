import java.util.GregorianCalendar;
import java.util.TimeZone;
/**
 * Statistcs are extensions of observations that serve to provide more information on the observation
 * gives an observation an associated GregorianCalendar to denote time and a StatType
 * @author jlovoi
 * @version 2018-10-03
 */
public class Statistics extends Observation implements DateTimeComparable{
	protected String DATE_TIME_FORMAT = "yy-MM-dd'T'HH:mm:ss z";
	private GregorianCalendar utcDateTime;
	private int numberOfReportingStations;
	private StatsType statType;
	
	/**
	 * Constructor for Statistics given a string for datetime
	 * @param value of observation
	 * @param stid location of observation
	 * @param dateTime String containting tokens of our date and time
	 * @param numberOfValidStations validStations that are feeding valid data
	 * @param inStatType Type of statistic that is being calculated in this instance
	 */
	public Statistics(double value, String stid, String dateTimeStr, int numberOfValidStations,
			StatsType inStatType)
	{
		super(value, stid);
		statType = inStatType;
		numberOfReportingStations = numberOfValidStations;
		utcDateTime = createDateFromString(dateTimeStr);
		utcDateTime.setTime(utcDateTime.getTime());
	}
	
	/**
	 * Constructor for Statistics given a GregrianCalendar for time
	 * @param value of observation
	 * @param stid location of observation
	 * @param dateTime GregorianCalendar object
	 * @param numberOfValidStations validStations that are feeding valid data
	 * @param inStatType Type of statistic that is being calculated in this instance
	 */
	public Statistics(double value, String stid, GregorianCalendar dateTime, int numberOfValidStations,
			StatsType inStatType)
	{
		super(value, stid);
		utcDateTime = dateTime;
		utcDateTime.setTime(dateTime.getTime());
		numberOfReportingStations = numberOfValidStations;
		statType = inStatType;
	}
	
	/**
	 * Creates a GregorianCalendar object given a string of DateTime in the relevant format
	 * @param dateTimeStr
	 * @return A GregorianCalendar with this dateTime
	 */
	public GregorianCalendar createDateFromString(String dateTimeStr)
	{
		int[] tokens = new int[7]; // Holds the int representation of each DateTime token
		String[] dateTokens = new String[3]; // Holds the date tokens separated by '-'
		String[] timeTokens = new String[4]; // Holds the time tokens separated by ':'
		String dateAndTime[] = new String[2]; // Holds the DateTime tokens to the left and right of 'T'
		dateAndTime = dateTimeStr.split("'T'");
		
		dateTokens = dateAndTime[0].split("-");
		timeTokens = dateAndTime[1].split(":");
		String temp = timeTokens[2];
		timeTokens[2] = temp.substring(0, 1); //get rid of the ' z' at the end of the seconds token
		TimeZone zone = TimeZone.getTimeZone("PST");
		
		// Convert tokens to int
		for (int i = 0; i < 3; i++)
		{
			tokens[i] = Integer.parseInt(dateTokens[i]);
			tokens[i + 3] = Integer.parseInt(timeTokens[i]);
		}
		
		// create the Gregorian DateTime using the set method
		GregorianCalendar gc = new GregorianCalendar();
		gc.set(tokens[0], tokens[1] - 1, tokens[2], tokens[3] -1, tokens[4], tokens[5]); // Month and hour are 0 - based
		zone.setID("PST"); // TimeZone object nused to set time zone for gc
		gc.setTimeZone(zone);
		return gc;
	}
	
	/**
	 * Returns the default toString for a GregorianCalendar
	 * @param calendar GregorianCalendar Object fed to the method
	 * @return defaul toString for a GregorianCalendar
	 */
	public String createStringFromDate(GregorianCalendar calendar)
	{
		return calendar.toString();
	}
	
	public int getNumberOfReportingStations()
	{
		return numberOfReportingStations;
	}
	
	public String getUTCDateTimeString()
	{
		return utcDateTime.toString();
	}
	
	/**
	 * returns whether or not this stat is newer than a given dateTime
	 */
	public boolean newerThan(GregorianCalendar inDateTime)
	{
		return (utcDateTime.getTimeInMillis() > inDateTime.getTimeInMillis());
	}
	
	/**
	 * returns whether or not this stat is older than a given dateTime
	 */
	public boolean olderThan(GregorianCalendar inDateTime)
	{
		return (utcDateTime.getTimeInMillis() < inDateTime.getTimeInMillis());
		
	}
	
	/**
	 * returns if this stat is same age as given dateTime
	 */
	public boolean sameAs(GregorianCalendar inDateTime)
	{
		return (utcDateTime.getTimeInMillis() == inDateTime.getTimeInMillis());
	}
	
	public String toString()
	{
		return super.toString();
	}
}
