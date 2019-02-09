package nw83;

import static org.junit.Assert.assertTrue;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.Assert;
import org.junit.Test;

import de.e2security.netflow_flowaggregation.model.protocols.NetflowEvent;

public class ZonedDateTimeTest {
	
	@Test public void zdtToString() {
		NetflowEvent event1 = new NetflowEvent();
		event1.setLast_switched("2018-12-22T11:18:56.664Z");
		
		Assert.assertEquals("2018-12-22T11:18:56.664Z", DateTimeFormatter.ISO_INSTANT.format(event1.getLast_switched()));
	}

	@Test public void zdtTransformationTest() {
		NetflowEvent testEvent1 = new NetflowEvent();
		testEvent1.setLast_switched("2018-12-22T11:18:56.664Z");
		NetflowEvent testEvent2 = new NetflowEvent();
		testEvent2.setLast_switched("2018-12-22T11:18:56.663Z");
		
		long zdtToEpoch = testEvent1.getLast_switched().toEpochSecond();
		long zdtToNano = testEvent1.getLast_switched().getNano();
		long zdtToEpochPlusNano = zdtToEpoch + zdtToNano;
		
		long zdtToEpoch2 = testEvent2.getLast_switched().toEpochSecond();
		long zdtToNano2 = testEvent2.getLast_switched().getNano();
		long zdtToEpochPlusNano2 = zdtToEpoch2 + zdtToNano2;
		
		Assert.assertTrue(zdtToEpoch == zdtToEpoch2);
		Assert.assertTrue(zdtToEpochPlusNano > zdtToEpochPlusNano2);
		
	}
	
	/*
	 * can be used comparator instead the calculation Nano + Epochl;
	 * or before() method in EPL statement
	 */
	@Test public void internalComparatorOfZDTTest() {
		ZonedDateTime zdt1 = ZonedDateTime.parse("2018-12-22T11:18:56.664Z");
		ZonedDateTime zdt2 = ZonedDateTime.parse("2018-12-22T11:18:56.665Z");
		
		Assert.assertTrue(zdt1.compareTo(zdt2) < 0);
		Assert.assertTrue(zdt1.isBefore(zdt2));
	}
	
	@Test public void calculationsTest() {
		ZonedDateTime zdt1 = ZonedDateTime.parse("2018-12-22T11:18:56.663Z");
		ZonedDateTime zdt2 = ZonedDateTime.parse("2018-12-22T11:18:56.664Z");
		assertTrue(zdt1.toEpochSecond()*1000 - zdt2.toEpochSecond()*1000 <= 60000);
	}
}
