package de.e2security.netflow_flowaggregation.utils;

import java.io.Serializable;
import java.util.Map;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.kafka.common.serialization.Serializer;

public class CustomSerializer<T extends Serializable> implements Serializer<T> {

	@Override
	public void configure(Map<String, ?> configs, boolean isKey) {
	}

	@Override
	public byte[] serialize(String topic, T data) {
		return SerializationUtils.serialize(data);
	}

	@Override
	public void close() {
	}

}

