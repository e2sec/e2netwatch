package de.e2security.netflow_flowaggregation.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

public final class TestUtil {

	public static List<String> readSampleDataFile(String fileName) {
		InputStream is = TestUtil.class.getResourceAsStream("/" + fileName);
		List<String> contentLines;
		if (is != null) {
			 contentLines = new BufferedReader(new InputStreamReader(is)).lines().collect(Collectors.toList());
		} else { throw new RuntimeException("cannot read file: " + fileName); }
		
		return contentLines;
	}
	
}
