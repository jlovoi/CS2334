import java.io.IOException;
import java.util.GregorianCalendar;

import org.junit.Assert;
import org.junit.Test;


public class JUnitTest {
	//Test All getters of default Constructor
	@Test
	public void abstractObservationConstructorTest() throws IOException {
		Observation obs = new Observation(2.2, "Stid");
		
		Assert.assertEquals(true, obs.isValid());
	}
	
	//Create and Test Observation Constructor and getters
	@Test
	public void observationConstructorTest() throws IOException {
		Observation obs = new Observation(2.2, "Stid");
		
		Assert.assertEquals(true, obs.isValid());
		Assert.assertEquals(2.2, obs.getValue(), 0.1);
		Assert.assertEquals("Stid", obs.getStid());
		Assert.assertEquals("Stid: 2.2", obs.toString());
	}
	
	//Create and Test Statistics Constructor and methods
	@Test
	public void statisticsConstructorTest() throws IOException {
		Statistics stats = new Statistics(2.2, "Stid", "2018-08-01'T'07:30:00 PST", 5, StatsType.MINIMUM);
		GregorianCalendar greg = new GregorianCalendar();
		
		Assert.assertEquals(greg.toString(), stats.createStringFromDate(greg));
		Assert.assertEquals(5, stats.getNumberOfReportingStations());
		Assert.assertEquals(false, stats.newerThan(greg));
		Assert.assertEquals(true, stats.olderThan(greg));
		Assert.assertEquals(false, stats.sameAs(greg));
		Assert.assertEquals("Stid: 2.2", stats.toString());
		}
		
	
	/**
	 * Test constructor with parameters, test getters
	 */
	@Test
	public void mapDataConstructorTest() throws IOException
	{
		MapData map = new MapData(2018, 8, 01, 07, 30, "data");
		
		//Test all getters
		Assert.assertEquals("data",  map.getDirectory());
		Assert.assertEquals(17.0, map.getTairMin().getValue(), 0.01);
		Assert.assertEquals(20.0, map.getTairMax().getValue(), 0.01);
		Assert.assertEquals(18.5, map.getTairAverage().getValue(), 0.01);
		Assert.assertEquals(1.0, map.getSradMin().getValue(), 0.01);
		Assert.assertEquals(4.0, map.getSradMax().getValue(), 0.01);
		Assert.assertEquals(2.5, map.getSradAverage().getValue(), 0.01);
		Assert.assertEquals(18.0, map.getTa9mMin().getValue(), 0.01);
		Assert.assertEquals(21.0, map.getTa9mMax().getValue(), 0.01);
		Assert.assertEquals(19.5, map.getTa9mAverage().getValue(), 0.01);
	}
	/**
	 * Test createFileName() creates the correct path
	 * @throws IOException 
	 */
	@Test
	public void mapDataCreateFileNameTest() throws IOException
	{
		MapData map = new MapData(2018, 8, 01, 07, 30, "data");
		
		Assert.assertEquals("201808010730.mdf", map.createFileName(2018, 8, 1, 7, 30, "data"));
	}
	/**
	 * Test toString()
	 * @throws IOException 
	 */
	@Test
	public void mapDataToStringTest() throws IOException
	{
		MapData map = new MapData(2018, 8, 01, 07, 30, "data");
		
		//Format string to be compared
		String str = "=========================================================\n";
		str += "=== 2018-08-01 07:30 ===\n";
		str += "=========================================================\n";
		str += "Maximum Air Temperature[1.5m] = 20.0 C at ALV2\n";
		str += "Minimum Air Temperature[1.5m] = 17.0 C at ACME\n";
		str += "Average Air Temperature[1.5m] = 18.5 C at MESONET\n";
		str += "=========================================================\n";
		str += "=========================================================\n";
		str += "Maximum Air Temperature[9.0m] = 21.0 C at ALV2\n";
		str += "Minimum Air Temperature[9.0m] = 18.0 C at ACME\n";
		str += "Average Air Temperature[9.0m] = 19.5 C at MESONET\n";
		str += "=========================================================\n";
		str += "=========================================================\n";
		str += "Maximum Solar Radiation[1.5m] = 4.0 W/m^2 at ALV2\n";
		str += "Minimum Solar Radiation[1.5m] = 1.0 W/m^2 at ACME\n";
		str += "Average Solar Radiation[1.5m] = 2.5 W/m^2 at MESONET\n";
		str += "=========================================================\n";
		
		//Now compare to actual
		Assert.assertEquals(str, map.toString());
	}
}
