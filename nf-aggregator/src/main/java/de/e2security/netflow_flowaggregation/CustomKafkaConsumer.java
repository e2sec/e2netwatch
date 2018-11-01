package de.e2security.netflow_flowaggregation;

import java.io.Serializable;
import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.espertech.esper.client.EPServiceProvider;

public class CustomKafkaConsumer<K extends Serializable, V extends Serializable> implements Callable<Integer> {
	private static final Duration duration = Duration.ofSeconds(1L);
	private static final Logger LOG = LoggerFactory.getLogger(CustomKafkaConsumer.class);

	private String clientId;
	private KafkaConsumer<K, V> consumer;
	private String topic;
	private EPServiceProvider engine;

	private AtomicBoolean closed = new AtomicBoolean();

	public CustomKafkaConsumer(Properties configs, String topic, EPServiceProvider engine) {
		this.clientId = configs.getProperty(ConsumerConfig.CLIENT_ID_CONFIG);
		this.topic = topic;
		this.engine = engine;
		this.consumer = new KafkaConsumer<>(configs);
	}

	@Override
	public Integer call() throws Exception {

		try {
			LOG.info("Starting the Consumer: {}", clientId);
			synchronized (consumer) {
				consumer.subscribe(Arrays.asList(topic)); //implied seek to the last 'commited position'
			}
			
			LOG.info("C: {}, Started to process records for topic : {}", clientId, topic);

			ConsumerRecords<K, V> records;
			while (!closed.get()) {
				if (Thread.currentThread().isInterrupted()) {
					closed.set(true);
				} else {
					synchronized (consumer) {
						records = consumer.poll(duration);
					}

					if (records.isEmpty()) {
						LOG.debug("C: {}, Found no records", clientId);
						continue;
					}
					try {
						LOG.info("C: {} Total No. of records received: {}", clientId, records.count());
						for (ConsumerRecord<K, V> record : records) {
							LOG.debug("C: {}, Record received topic: {}, partition: {}, key: {}, value: {}, offset: {}",
									clientId, record.topic(), record.partition(), record.key(), record.value(),
									record.offset());
							NetflowEvent netflowEvent = new NetflowEvent(record.value().toString());
							engine.getEPRuntime().sendEvent(netflowEvent);
						}
						consumer.commitAsync();
					} catch (NetflowEventException e) {
						LOG.error("No Esper event created. No commit for the recent ConsumerRecords has been done!");
					}
				}
			}
		} catch (org.apache.kafka.common.errors.InterruptException e) {
			LOG.info("C: {}, consumer interupted", clientId);
		} catch (Exception e) {
			LOG.error("Error while consuming messages", e);
			throw new Exception(e);
		} finally {
			LOG.info("C: {}, consumer exited", clientId);
			consumer.close(duration);
		}

		return 0;
	}

	public void commit() {
		consumer.commitSync();
	}

	public void close() {
		closed.set(true);
	}

}
