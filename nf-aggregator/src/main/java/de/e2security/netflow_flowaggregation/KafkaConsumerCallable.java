package de.e2security.netflow_flowaggregation;

import java.io.Serializable;
import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.Callable;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.espertech.esper.client.EPServiceProvider;

public class KafkaConsumerCallable<K extends Serializable, V extends Serializable> implements Callable<Integer> {
	private static final Duration duration = Duration.ofSeconds(1L);
	private static final Logger LOG = LoggerFactory.getLogger(KafkaConsumerCallable.class);
	final Properties configs;
	final String topic;
	final String clientId;
	final KafkaConsumer<K,V> consumer;
	final EPServiceProvider engine;

	public KafkaConsumerCallable(Properties configs, String topic, EPServiceProvider engine) {
		this.engine = engine;
		this.configs = configs;
		this.topic = topic;
		this.clientId = configs.getProperty(ConsumerConfig.CLIENT_ID_CONFIG);
		this.consumer = new KafkaConsumer<>(configs);
	}

	@Override
	public Integer call() throws Exception {
		try {
			LOG.info("Starting the Consumer Thread {} for topic: {}", Thread.currentThread().getName(), topic);
			consumer.subscribe(Arrays.asList(topic)); //implied seek to the last 'commited position'
			while (true) {
				ConsumerRecords<K, V> records;
				if (Thread.currentThread().isInterrupted()) {
					throw new RuntimeException("Thread " + Thread.currentThread().getName() + " has been interupted");
				} else {
					records = consumer.poll(duration);
					if (records.isEmpty()) {
						LOG.debug("C: {}, Found no records", clientId);
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

}
