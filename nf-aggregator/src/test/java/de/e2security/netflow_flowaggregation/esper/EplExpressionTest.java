package de.e2security.netflow_flowaggregation.esper;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPStatement;

import de.e2security.netflow_flowaggregation.netflow.NetflowEvent;
import de.e2security.netflow_flowaggregation.netflow.NetflowEventOrdered;
import de.e2security.netflow_flowaggregation.utils.EsperUtil;
import de.e2security.netflow_flowaggregation.utils.TestUtil;
import junit.framework.Assert;

@SuppressWarnings("deprecation")
public class EplExpressionTest {

	EPServiceProvider engine;
	List<String> nfGenSampleDataFromKafka;

	@Before
	public void init() {
		this.engine = EsperUtil.registerEvents(NetflowEvent.class, NetflowEventOrdered.class);
		this.nfGenSampleDataFromKafka = TestUtil.readSampleDataFile("nf_gen.output.sample");
	}
	
	@After
	public void stop() {
		this.engine.destroy();
	}
	
	@Test public void eventGettingIntoCorrectTimeOrder() {
		engine.getEPAdministrator().createEPL(TcpEplExpressions.eplSortByLastSwitched());
		EPStatement statement = engine.getEPAdministrator().createEPL(TcpEplExpressionsTest.selectNetStreamOrdered());
		NetflowEventsCorrectOrderTestListener testListener = new NetflowEventsCorrectOrderTestListener(false);
		statement.addListener(testListener);
		EsperTestUtil.sendDataToEsper(nfGenSampleDataFromKafka, engine);
		Queue<ZonedDateTime> dates = testListener.getDates();
		Assert.assertEquals(nfGenSampleDataFromKafka.size(), dates.size());
		List<Boolean> assertions = new ArrayList<>(); //assertion: the same number of successively sequenced data regarding their date time
		while (dates.size() >= 2) {
			ZonedDateTime theMostLatestDate = dates.poll(); // polling from the head: the most recent/latest data due to FIFO and ascending order
			ZonedDateTime dateBeforeTheMostLatestDate = dates.peek();
			if (dateBeforeTheMostLatestDate.compareTo(theMostLatestDate) <= 0) //compareTo results: negative int if this < that; 0 if this == that; positive int if this > that
				assertions.add(true);
		}
		Assert.assertEquals(nfGenSampleDataFromKafka.size() - 3, assertions.size()); 
	}
	
}
