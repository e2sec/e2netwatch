package de.e2security.netflow_flowaggregation.utils;

import java.io.Serializable;
import java.util.Map;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.kafka.common.serialization.Deserializer;

public class CustomDeserializer<T extends Serializable> implements Deserializer<T> {

	@Override
	public void configure(Map<String, ?> configs, boolean isKey) {
	}

	@SuppressWarnings("unchecked")
	@Override
	public T deserialize(String topic, byte[] objectData) {
		return (objectData == null) ? null : (T) SerializationUtils.deserialize(objectData);
	}

	@Override
	public void close() {
	}
}
