package nw76;

import java.time.ZonedDateTime;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Assert;
import org.junit.Test;

import de.e2security.netflow_flowaggregation.esper.ProtocolRegisterTrigger;
import de.e2security.netflow_flowaggregation.esper.ProtocolRegisterTrigger.Host;
import de.e2security.netflow_flowaggregation.model.protocols.ProtocolRegister;

public class TestTrafficRecognitionByPortDistinction {
	@Test public void higherPortAsSourceHostTest() {
		 //higher port = source host; lower port = dst hosts
		//1. src_port is higher as dst_port
		ProtocolRegister prtcl = new ProtocolRegister();
		prtcl.setIn_l4_src_port(7654);
		prtcl.setIn_l4_dst_port(4567);
		prtcl.setOut_14_src_port(4567);
		prtcl.setOut_14_dst_port(7654);
		prtcl.setIn_last_switched(ZonedDateTime.now());
		prtcl.setOut_last_switched(ZonedDateTime.now());
		ProtocolRegisterTrigger trigger = new ProtocolRegisterTrigger(null);
		Pair<Host,Host> src_dst = trigger.defineByPort(prtcl);
		Assert.assertEquals(src_dst.getLeft().getPort(), (Integer) 7654);
		Assert.assertEquals(src_dst.getRight().getPort(),(Integer) 4567);
		//2. src_port is lower as dst_port
		ProtocolRegister prtcl2 = new ProtocolRegister();
		prtcl2.setIn_l4_src_port(8080);
		prtcl2.setIn_l4_dst_port(9191);
		prtcl2.setOut_14_src_port(9191);
		prtcl2.setOut_14_dst_port(8080);
		prtcl2.setIn_last_switched(ZonedDateTime.now());
		prtcl2.setOut_last_switched(ZonedDateTime.now());
		ProtocolRegisterTrigger trigger2 = new ProtocolRegisterTrigger(null);
		Pair<Host,Host> src_dst2 = trigger2.defineByPort(prtcl2);
		Assert.assertEquals(src_dst2.getLeft().getPort(), (Integer) 9191);
		Assert.assertEquals(src_dst2.getRight().getPort(),(Integer) 8080);
	}
}
