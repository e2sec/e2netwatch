package de.e2security.netflow_flowaggregation.utils;

import java.util.ConcurrentModificationException;
import java.util.Set;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.espertech.esper.client.EPServiceProvider;

import de.e2security.netflow_flowaggregation.kafka.CustomKafkaProducer;
import de.e2security.netflow_flowaggregation.kafka.KafkaConsumerMaster;

public final class ThreadUtil {

	private static final Logger LOG = LoggerFactory.getLogger(ThreadUtil.class);

	public static void manageShutdown(Object... objectToBeStoppedGracefully) {

		Stream.of(objectToBeStoppedGracefully).forEach(object -> {

			Runtime.getRuntime().addShutdownHook(new Thread( () -> {
				try {
					LOG.info("Exiting...");

					// Stop ESPER
					if (object instanceof EPServiceProvider ) {
						LOG.info("stopping ESPER...");
						((EPServiceProvider) object).destroy();
						//Stop Consumer
						// Stop Producer
					} else if (object instanceof CustomKafkaProducer) {
						LOG.info("stopping KafkaProducer...");
						((CustomKafkaProducer) object).flush();
						((CustomKafkaProducer) object).close();

					} else if (object instanceof KafkaConsumerMaster ) {
						LOG.info("stopping KafkaConsumer...");
						KafkaConsumerMaster consumer = (KafkaConsumerMaster) object;
						consumer.getKafkaGroups().forEach(consumer::closeThreads);
					} else {
						LOG.warn("Object {} has not been recognized object to make shutdown on it. You have to manage it within {} class", object.getClass().getName(),
								ThreadUtil.class.getName());
					}

				} catch (ConcurrentModificationException ignore) {
					LOG.error(ignore.getMessage());
				}
			}));
		});
	}
	
	
	public static void printThreads() {
		System.out.println("");
		System.out.println("Current threads:");

		Set<Thread> threads = Thread.getAllStackTraces().keySet();

		for (Thread t : threads) {
			String name = t.getName();
			Thread.State state = t.getState();
			int priority = t.getPriority();
			String type = t.isDaemon() ? "Daemon" : "Normal";
			System.out.printf("%-20s \t %s \t %d \t %s\n", name, state, priority, type);
		}
	}

}
