/**

 * @author jlovoi
 * @version 2018-09-19
 * Lab 12
 * Class for each point of data in the .mdf files
 * Each line represents data from a given STID/location, each Observation is a different data point
 */
public class Observation extends AbstractObservation
{
	//data point in the .mdf file
	private double value;
	//Location of the given data
	String stid;
	
	/**
	 * Constructor for Observation data point
	 * @param value of given data
	 * @param stid location of data
	 */
	public Observation(double value, String stid)
	{
		this.value = value;
		this.stid = stid;
		this.checkValid();
	}
	
	/**
	 * Checks the value and sets valid as false if less than or equal to -900
	 */
	public void checkValid()
	{
		if (this.value <= -900)
		{
			this.valid = false;
		}
		else
		{
			this.valid = true;
		}
	}
	
	/**
	 * Getter for value
	 * @return the double, formatted to have just one decimal place
	 */
	public double getValue()
	{
		return Double.parseDouble(String.format("%.1f", this.value)); //Format into a Double to 1 decimal place
	}
	
	/**
	 * Getter for stid
	 * @return stid, the location from which the data came
	 */
	public String getStid()
	{
		return this.stid;
	}
	
	/**
	 * Presents this Observation in a formatted string
	 */
	public String toString()
	{
		return (this.stid + ": " + Double.toString(this.value));
	}
}
