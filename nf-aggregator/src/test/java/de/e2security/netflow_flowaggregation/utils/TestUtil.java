package de.e2security.netflow_flowaggregation.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public final class TestUtil {
	
	static DateTimeFormatter formater = DateTimeFormatter.ISO_INSTANT;	

	public static List<String> readSampleDataFile(String fileName) {
		InputStream is = TestUtil.class.getResourceAsStream("/" + fileName);
		List<String> contentLines;
		if (is != null) {
			 contentLines = new BufferedReader(new InputStreamReader(is)).lines().collect(Collectors.toList());
		} else { throw new RuntimeException("cannot read file: " + fileName); }
		
		return contentLines;
	}
	
	public static long getCurrentTimeEvent(String dateString) {
		Date date = Date.from(Instant.from(formater.parse(dateString)));
		return date.getTime();
	}
	
	public static long getCurrentTimeEvent(ZonedDateTime zdt) {
		long res = 0L;
		try {
			Date date = Date.from(Instant.from(formater.parse(zdt.toString())));
			res = date.getTime();
		} catch (Exception ex) {}
		return res;
	}	

}
