package nw76;

import java.time.ZonedDateTime;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Assert;
import org.junit.Test;

import de.e2security.netflow_flowaggregation.esper.ProtocolRegisterTrigger;
import de.e2security.netflow_flowaggregation.esper.ProtocolRegisterTrigger.Host;
import de.e2security.netflow_flowaggregation.model.protocols.ProtocolRegister;

public class TestTrafficRecognitionByPortDistinction {
	//higher port = source host; lower port = dst hosts
	@Test public void higherPortAsSourceHostTestWithTheSameTimestamp() {
		//1. src_port is higher as dst_port
		ZonedDateTime now = ZonedDateTime.now();
		ProtocolRegister prtcl = new ProtocolRegister();
		prtcl.setIn_l4_src_port(7654);
		prtcl.setIn_l4_dst_port(4567);
		prtcl.setOut_14_src_port(4567);
		prtcl.setOut_14_dst_port(7654);
		prtcl.setIn_first_switched(now);
		prtcl.setOut_first_switched(now);
		ProtocolRegisterTrigger trigger = new ProtocolRegisterTrigger(null);
		Pair<Host,Host> src_dst = trigger.defineTrafficDirectionByPortOnEqualFirstSwitched(prtcl);
		Assert.assertEquals(src_dst.getLeft().getPort(), (Integer) 7654);
		Assert.assertEquals(src_dst.getRight().getPort(),(Integer) 4567);
		//2. src_port is lower as dst_port
		ProtocolRegister prtcl2 = new ProtocolRegister();
		prtcl2.setIn_l4_src_port(8080);
		prtcl2.setIn_l4_dst_port(9191);
		prtcl2.setOut_14_src_port(9191);
		prtcl2.setOut_14_dst_port(8080);
		prtcl2.setIn_first_switched(now);
		prtcl2.setOut_first_switched(now);
		ProtocolRegisterTrigger trigger2 = new ProtocolRegisterTrigger(null);
		Pair<Host,Host> src_dst2 = trigger2.defineTrafficDirectionByPortOnEqualFirstSwitched(prtcl2);
		Assert.assertEquals(src_dst2.getLeft().getPort(), (Integer) 9191);
		Assert.assertEquals(src_dst2.getRight().getPort(),(Integer) 8080);
	}
	
	@Test public void noHostExchangeIfTimestampNotTheSame() {
		ZonedDateTime before = ZonedDateTime.now();
		ZonedDateTime after = before.plusMinutes(1L);
		ProtocolRegister prtcl = new ProtocolRegister();
		prtcl.setIn_l4_src_port(3321);
		prtcl.setIn_l4_dst_port(5353);
		prtcl.setOut_14_src_port(5353);
		prtcl.setOut_14_dst_port(3321);
		prtcl.setIn_first_switched(before);
		prtcl.setOut_first_switched(after);
		ProtocolRegisterTrigger trigger = new ProtocolRegisterTrigger(null);
		Pair<Host,Host> src_dst = trigger.defineTrafficDirectionByPortOnEqualFirstSwitched(prtcl);
		//assert the higher port will be not the source host, since timestamp differs and Esper sort events within window properly
		Assert.assertEquals(src_dst.getLeft().getPort(),(Integer) 3321);
		Assert.assertEquals(src_dst.getRight().getPort(),(Integer) 5353);
	}
}
