import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.GregorianCalendar;

/**
 * @author jlovoi
 * @version 2018-09-19
 * Lab 12
 * MapData class that takes in a time/date and an .mdf file, and parses the file to find pertinent weather
 * information for numerous data collection sites
 */
public class MapData {
	private ArrayList<Observation> sradData = new ArrayList<Observation>(); //List containing Solar Radiation Observation objects
	private ArrayList<Observation> tairData = new ArrayList<Observation>(); //List containing Air Temp at 1.5m Observation Objects
	private ArrayList<Observation> ta9mData = new ArrayList<Observation>(); //List containing Air temp at 9m Observation Objects
	private static final int NUMBER_OF_MISSING_OBSERVATIONS = 10; //Max number of invalid observations allowed
	private Integer numberOfStations = null; //count of stations/lines of data in .mdf file
	private int stidPosition = -1; //index of data location in lists
	private int tairPosition = -1; //index of Air Temp 1.5m in lists
	private int sradPosition = -1; //index of solar radiation in lists
	private int ta9mPosition = -1; //index of 9m air temp in lists
	private String MESONET = "MESONET"; //string to easily refer to Mesonet in output for total data averages
	private String directory; //string to navigate to data directory
	private String filename; //constructed filename for .mdf, based on the time and date entered
	private Statistics tairMin; //Air Temp min at 1.5m object
	private Statistics tairMax; //Air temp max at 1.5m object
	private Statistics tairAverage; //air temp avg at 1.5m object
	private Statistics ta9mMin; //air tem min at 9m object
	private Statistics ta9mMax; //air temp max at 9m object
	private Statistics ta9mAverage; //air temp avg at 9m object
	private Statistics sradMin; //solar radiation min object
	private Statistics sradMax; //solar radiation max object
	private Statistics sradAverage; //solar radiation avg object
	private Statistics sradTotal; //total solar radiation object
	//following strings are used for ease of formatting in toString()
	private String year;
	private String m; //month in form of string
	private String d; //day in form of string
	private String h; //hour in form of string
	private String min; //minute in form of string
	private GregorianCalendar utcDateTime = new GregorianCalendar(); //
	int invalidEntries = 0;
	
	/**
	 * Constructor for MapData, given the date and time
	 * @param year user given year
	 * @param month user given month
	 * @param day user given day
	 * @param hour user given hour
	 * @param minute user given minute
	 * @param directory String that tells MapData where to find .mdf file
	 * @throws IOException
	 */
	public MapData(int year, int month, int day, int hour, int minute, String directory) throws IOException
	{
		//Initialize all the variables from the parameters
		this.directory= directory;
		utcDateTime.set(year, month - 1, day, hour, minute);
		//generate the .mdf filename
		this.filename = createFileName(year, month, day, hour, minute, directory);
		
		parseFile(); //parse the file to create all the Observations
		//Use Observation objects to calculate statistics from the data
		calculateStatistics(sradData, "srad");
		calculateStatistics(ta9mData, "ta9m");
		calculateStatistics(tairData, "tair");
		
	}
	/**
	 * Getter for Directory
	 * @return directory String 
	 */
	public String getDirectory()
	{
		return directory;
	}
	/**
	 * Getter for tairMin
	 * @return Air Temp min at 1.5m Obs
	 */

	public Observation getTairMin()
	{
		return tairMin;
	}
	/**
	 * Getter for tairMax
	 * @return Air temp max at 1.5m Obs
	 */

	public Observation getTairMax()
	{
		return tairMax;
	}
	/**
	 * getter for tairAverage
	 * @return Air Temp avg at 1.5m Obs
	 */

	public Observation getTairAverage()
	{
		return tairAverage;
	}
	/**
	 * getter for ta9mMin
	 * @return Air Temp min at 9m Obs
	 */

	public Observation getTa9mMin()
	{
		return ta9mMin;
	}
	/**
	 * getter for ta9mMax
	 * @return Air Temp max at 9m Obs
	 */

	public Observation getTa9mMax()
	{
		return ta9mMax;
	}
	/**
	 * getter for ta9mAverge
	 * @return Air Temp avg at 9m Obs
	 */

	public Observation getTa9mAverage()
	{
		return ta9mAverage;
	}
	/**
	 * getter for sradMin
	 * @return Solar Radiation Min Obs
	 */

	public Observation getSradMin()
	{
		return sradMin;
	}
	/**
	 * getter for sradMax
	 * @return Solar Radiation Max Obs
	 */

	public Observation getSradMax()
	{
		return sradMax;
	}
	/**
	 * getter for sradAverage
	 * @return Solar Radiation avg Obs
	 */

	public Observation getSradAverage()
	{
		return sradAverage;
	}
	/**
	 * getter for sradTotal
	 * @return Solar Radiation Total Obs
	 */

	public Observation getSradTotal()
	{
		return sradTotal;
	}
	
	/**
	 * Creates the string that will be used as the file name, which will have the data for that date and time
	 * @return String formatted filename for .mdf
	 */
	public String createFileName(int year, int month, int day, int hour, int minute, String directory)
	{

		this.year = Integer.toString(year);
		//Check if a 0 needs to be added to the beginning of ints < 10
		if (month < 10)
		{
			m = "0" + Integer.toString(month);
		}
		else
		{
			m = Integer.toString(month);
		}
		if (day < 10)
		{
			d = "0" + Integer.toString(day);
		}
		else
		{
			d = Integer.toString(day);
		}
		if (hour < 10)
		{
			h = "0" + Integer.toString(hour);
		}
		else
		{
			h = Integer.toString(hour);
		}
		if (minute < 10)
		{
			min = "0" + Integer.toString(minute);
		}
		else
		{
			min = Integer.toString(minute);
		}
		//This is the filename that is used in parseFile()
		String str = Integer.toString(year) + m + d + h + min + ".mdf";
		return str;
	}
	
	/**
	 * Parses the line of the file that contains headers, assigns the index for the appropriate headers
	 * @param inParamStr
	 */
	private void parseParamHeader(String inParamStr)
	{
		String[] headerTokens = inParamStr.split("\\s+");
		
		for (int i = 0; i < headerTokens.length; i++)
		{
			switch (headerTokens[i])
			{
			case "STID":
				stidPosition = i - 1;
				continue;
			case "TAIR":
				tairPosition = i - 1;
				continue;
			case "SRAD":
				sradPosition = i - 1;
				continue;
			case "TA9M":
				ta9mPosition = i - 1;
				continue;
			default:
				continue;
			}
		}
	}
	
	/**
	 * Reads through each line of data in the file, and created an Observation object for each data point
	 * Adds these observations to the appropriate Observation[] list
	 * @throws IOException
	 */
	public void parseFile() throws IOException
	{
		BufferedReader br = new BufferedReader(new FileReader(this.directory + "/" + this.filename));
        String str;  // String of a line of data from the file
        String[] tokens = new String[24]; // List of Strings that will contain the split content of each line
        int count = 0; // This is used to keep track of index count
        
        // Skip the irrelevant lines at the top of the file
        str = br.readLine();
        str = br.readLine();
        str = br.readLine(); // Third line is the line of headers
        parseParamHeader(str); // Parse the headers line to assign parameter index positions
        str = br.readLine();
        while (str != null) //continue to read until no more data in file
        {
        	tokens = str.trim().split("\\s+"); //split the data by spaces
        	// Create the Observation objects and add them to the appropriate list
            sradData.add(new Observation(Double.parseDouble(tokens[sradPosition]), tokens[stidPosition]));
            tairData.add(new Observation(Double.parseDouble(tokens[tairPosition]), tokens[stidPosition]));
            ta9mData.add(new Observation(Double.parseDouble(tokens[ta9mPosition]), tokens[stidPosition]));
            str = br.readLine(); //read next line
            count++; //increment index
        }

        numberOfStations = count; // Set numberOfStations
        
        br.close();
    }
	
	/**
	 * Creates a Statistics object, to expand upon the observation by giving it a date and time and a stat type
	 * @param inData the relevant arraylist of observations
	 * @param paramId specifying what kind of data we are looking at
	 */
	private void calculateStatistics(ArrayList<Observation> inData, String paramId)
	{
		// Set first data point as both min and max to be compared while looping through list
		Observation min = inData.get(0);
		Observation max = inData.get(0);
		double total = 0; //Total used to calculate Average
		int n = 0; //Sample size, used to divide total for average
		for (int i = 0; i < inData.size(); i++)
		{
			if (inData.get(i).isValid()) // Check if this data point is usable, else skip to next iteration
			{
				total += inData.get(i).getValue();
				n++;
				
				//Check for Min
				if (inData.get(i).getValue() < min.getValue())
				{
					min = inData.get(i);
				}
				//Check for Max
				if (inData.get(i).getValue() > max.getValue())
				{
					max = inData.get(i);
				}
			}
			else
			{
				invalidEntries++;
			}
		}
		Observation avg = new Observation((total / n), MESONET);
		
		if (paramId == "tair")
		{
			tairMin = new Statistics(min.getValue(), min.getStid(), utcDateTime, numberOfStations, StatsType.MINIMUM);
			tairMax = new Statistics(max.getValue(), max.getStid(), utcDateTime, numberOfStations, StatsType.MAXIMUM);
			tairAverage = new Statistics(avg.getValue(), avg.getStid(), utcDateTime, numberOfStations, StatsType.AVERAGE);
		}
		else if (paramId == "ta9m")
		{
			ta9mMin = new Statistics(min.getValue(), min.getStid(), utcDateTime, numberOfStations, StatsType.MINIMUM);
			ta9mMax = new Statistics(max.getValue(), max.getStid(), utcDateTime, numberOfStations, StatsType.MAXIMUM);
			ta9mAverage = new Statistics(avg.getValue(), avg.getStid(), utcDateTime, numberOfStations, StatsType.AVERAGE);
		}
		else if (paramId == "srad")
		{
			sradMin = new Statistics(min.getValue(), min.getStid(), utcDateTime, numberOfStations, StatsType.MINIMUM);
			sradMax = new Statistics(max.getValue(), max.getStid(), utcDateTime, numberOfStations, StatsType.MAXIMUM);
			sradAverage = new Statistics(avg.getValue(), avg.getStid(), utcDateTime, numberOfStations, StatsType.AVERAGE);
			sradTotal = new Statistics(total, MESONET, utcDateTime, numberOfStations, StatsType.TOTAL);
		}

	}
	
	
	/**
	 * Formats the String that will be given to display the desired data
	 * @return formatted String
	 */
	public String toString()
	{
		//Check if there are 10 or fewer invalid entries, return formatted string if true
		if (invalidEntries <= NUMBER_OF_MISSING_OBSERVATIONS)
		{
			String str = "=========================================================\n";
			str += "=== " + year + "-" + m + "-" + d + " " + h + ":" + min + " ===\n";
			str += "=========================================================\n";
			str += "Maximum Air Temperature[1.5m] = " + tairMax.getValue() + " C at " + tairMax.getStid() + "\n";
			str += "Minimum Air Temperature[1.5m] = " + tairMin.getValue() + " C at " + tairMin.getStid() + "\n";
			str += "Average Air Temperature[1.5m] = " + tairAverage.getValue() + " C at " + tairAverage.getStid() + "\n";
			str += "=========================================================\n";
			str += "=========================================================\n";
			str += "Maximum Air Temperature[9.0m] = " + ta9mMax.getValue() + " C at " + ta9mMax.getStid() + "\n";
			str += "Minimum Air Temperature[9.0m] = " + ta9mMin.getValue() + " C at " + ta9mMin.getStid() + "\n";
			str += "Average Air Temperature[9.0m] = " + ta9mAverage.getValue() + " C at " + ta9mAverage.getStid() + "\n";
			str += "=========================================================\n";
			str += "=========================================================\n";
			str += "Maximum Solar Radiation[1.5m] = " + sradMax.getValue() + " W/m^2 at " + sradMax.getStid() + "\n";
			str += "Minimum Solar Radiation[1.5m] = " + sradMin.getValue() + " W/m^2 at " + sradMin.getStid() + "\n";
			str += "Average Solar Radiation[1.5m] = " + sradAverage.getValue() + " W/m^2 at " + sradAverage.getStid() + "\n";
			str += "=========================================================\n";
			
			return str;
		}
		//Return message if too many invalid observations
		else
		{
			return ("Too many invalid observations to calculate statistics! There were" + invalidEntries + " Invalid observations.");
		}
	}
	
}
