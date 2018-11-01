package de.e2security.netflow_flowaggregation;

import java.io.Serializable;
import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomKafkaProducer<K extends Serializable, V extends Serializable> {
	private static final Logger LOG = LoggerFactory.getLogger(CustomKafkaConsumer.class);

	private String clientId;
	private KafkaProducer<K, V> producer;
	private String topic;

	public CustomKafkaProducer(Properties config) {
		Properties configOut = new Properties();
		configOut.put("bootstrap.servers", config.get("bootstrap.servers"));
		configOut.put("client.id", config.get("producer.client.id"));
		configOut.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		configOut.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		this.clientId = config.getProperty(ProducerConfig.CLIENT_ID_CONFIG);
		this.topic = config.getProperty("producer.topic");
		this.producer = new KafkaProducer<>(config);
		LOG.info("Starting the Producer: {}", clientId);
		LOG.info("P: {}, Started to process records for topics : {}", clientId, topic);
	}

	public void send(String msg) {
		long time = System.currentTimeMillis();

		try {
			@SuppressWarnings("unchecked")
			final ProducerRecord<K, V> record = new ProducerRecord<K, V>(this.topic, (V) msg);
			producer.send(record, (metadata, exception) -> {
				long elapsedTime = System.currentTimeMillis() - time;
				if (metadata != null) {
					LOG.info("P: {}, sent record(key={} value={}) " +
							"meta(partition={}, offset={}) time={}\n",
							clientId, record.key(), record.value(),
							metadata.partition(), metadata.offset(), elapsedTime);
				} else {
					exception.printStackTrace();
				}
			});

		} catch (Exception e) {
			LOG.error("P: {}: Error while sending messages - {}", clientId, e.getMessage());
		}

	}

	public void flush() {
		LOG.info("P: {}, producer flushing", clientId);
		this.producer.flush();
	}

	public void close() {
		LOG.info("P: {}, producer exiting", clientId);
		this.producer.close();
	}
}
