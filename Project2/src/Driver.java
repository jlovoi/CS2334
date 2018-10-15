import java.io.IOException;
/**
 * Driver class that will run the Program using MapData and Observation classes
 * @author jlovoi
 * @version 2018-09-19
 * Lab 12
 * Main method to run the program.
 * Creates a MapData instance given the Date and Time parameters
 */
public class Driver {
	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		//Enter year, month, day, hour, min, directory parameters to get that specific data
		MapData map = new MapData(2018, 8, 30, 17, 45, "data");
		System.out.print(map);
	}
}
