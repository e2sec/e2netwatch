package de.e2security.netflow_flowaggregation;

import java.io.Serializable;
import java.time.Duration;
import java.util.List;
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
	private List<String> topics;
	private EPServiceProvider engine;

	private AtomicBoolean closed = new AtomicBoolean();

	public CustomKafkaConsumer(Properties configs, List<String> topics, EPServiceProvider engine) {
		this.clientId = configs.getProperty(ConsumerConfig.CLIENT_ID_CONFIG);
		this.topics = topics;
		this.engine = engine;
		this.consumer = new KafkaConsumer<>(configs);
	}

	@Override
	public Integer call() throws Exception {

		try {
			LOG.info("Starting the Consumer: {}", clientId);
			synchronized (consumer) {
				consumer.subscribe(topics);
			}
			// consumer.seek(partition, offset); // TODO: set offset

			LOG.info("C: {}, Started to process records for topics : {}", clientId, topics);

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

					LOG.info("C: {} Total No. of records received: {}", clientId, records.count());
					for (ConsumerRecord<K, V> record : records) {
						LOG.debug("C: {}, Record received topic: {}, partition: {}, key: {}, value: {}, offset: {}",
								clientId, record.topic(), record.partition(), record.key(), record.value(),
								record.offset());
						try {
							NetflowEvent netflowEvent = new NetflowEvent(record.value().toString());
							engine.getEPRuntime().sendEvent(netflowEvent);
						} catch (NetflowEventException e) {
							LOG.error("No Esper event created.");
						}
					}
					// TODO: commit offset
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
