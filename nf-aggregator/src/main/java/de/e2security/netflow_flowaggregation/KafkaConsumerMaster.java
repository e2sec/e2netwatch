package de.e2security.netflow_flowaggregation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.PartitionInfo;

import com.espertech.esper.client.EPServiceProvider;

public class KafkaConsumerMaster {

	private List<String> topics;
	private EPServiceProvider engine;
	private List<Callable<Integer>> consumerThreads;
	private KafkaConsumer consumerGeneral;
	private Properties configIn;
	private Map<String,ExecutorService> groupId_executor;
	private Map<String,KafkaConsumerCallable> topic_consumer;

	/*
	 * Per Group: multiple threads of KafkaConsumer for particular topic;
	 */
	public KafkaConsumerMaster(EPServiceProvider engine) {
		this.groupId_executor = new HashMap<>();
		this.topic_consumer = new HashMap<>();
		this.configIn = new Properties();
		this.consumerThreads = new ArrayList<>();
		this.engine = engine;
	}
	
	public KafkaConsumerMaster startWorkers(Properties config) {
		configIn.put("bootstrap.servers", config.get("bootstrap.servers"));
		configIn.put("enable.auto.commit", config.get("enable.auto.commit"));
		configIn.put("auto.commit.interval.ms", config.get("auto.commit.interval.ms"));
		configIn.put("session.timeout.ms", config.get("session.timeout.ms"));
		configIn.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		configIn.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		this.topics = new ArrayList<String>(Arrays.asList(config.getProperty("consumer.topics").split(",")));
		topics.forEach(topic -> {
			int partitionCount = getPartionsNumber(topic);
			ExecutorService exec = Executors.newFixedThreadPool(partitionCount);
			String groupId = configIn.get("consumer.group.id.prefix") + topic;
			configIn.put("group.id", groupId);
			this.groupId_executor.put(groupId, exec);
			IntStream.rangeClosed(1, partitionCount).forEach(partitionCounter -> {
				configIn.put("client.id", config.get("consumer.client.id") + String.valueOf(partitionCounter));
				KafkaConsumerCallable consumer = new KafkaConsumerCallable<>(configIn, topic, engine);
				consumerThreads.add(null);
				topic_consumer.put(topic, null);
				Future<Integer> future = exec.submit(consumer);
			});
		});
		return this;
	}

	public List<String> getKafkaGroups() {
		return groupId_executor.keySet().stream().collect(Collectors.toList());
	}

	private int getPartionsNumber(String topic) {
		int count;
		consumerGeneral = new KafkaConsumer<>(configIn);
		Map<String, List<PartitionInfo>> listTopics = (Map<String, List<PartitionInfo>>) consumerGeneral.listTopics();
		if (listTopics.isEmpty()) {
			throw new RuntimeException("No Topics are available to read from Kafka -> no any KafkaConsumer can be created");
		} else {
			count = listTopics.get(topic).size();
		}
		consumerGeneral.close();
		return count;
	}
	
	public void closeThreads(String groupId) {
		this.groupId_executor.get(groupId).shutdown();
	}

}
