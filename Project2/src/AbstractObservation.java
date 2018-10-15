/**
 * @version 2018-10-03
 * @author jlovoi
 * This is the main Observation class, which simply gives every observation and statistics instance a valid or invalid setting
 */
public abstract class AbstractObservation {
	//if value is less than or equal to -900, the data is invalid from an error
	protected boolean valid = true;
	 
	public AbstractObservation()
	{
		/**
		  * constructor for our abstract class, so nothing really happens
		  */
	}
	
	/**
	 * getter for valid
	 * @return true or false for valid
	 */
	public boolean isValid()
	{
		return this.valid;
	}
}
