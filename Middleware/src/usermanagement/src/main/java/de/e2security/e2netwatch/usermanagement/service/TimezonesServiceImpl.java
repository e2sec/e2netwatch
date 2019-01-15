package de.e2security.e2netwatch.usermanagement.service;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Component;

import de.e2security.e2netwatch.usermanagement.dto.ListDTO;
import de.e2security.e2netwatch.usermanagement.dto.TimezoneDTO;

/**
 * TimezonesManagerImpl
 * 
 * @author Hrvoje
 *
 */
@Component
public class TimezonesServiceImpl implements TimezonesService {
	
	@Override
	public ListDTO<TimezoneDTO> getAll() {
		String[] ids = TimeZone.getAvailableIDs();
		List<TimezoneDTO> timezones = new ArrayList<>();
		for (String id : ids) {
			timezones.add(new TimezoneDTO(id, displayTimeZone(TimeZone.getTimeZone(id))));
		}
		return new ListDTO<>(timezones, null);
	}
	
	private String displayTimeZone(TimeZone tz) {
		long hours = TimeUnit.MILLISECONDS.toHours(tz.getRawOffset());
		long minutes = TimeUnit.MILLISECONDS.toMinutes(tz.getRawOffset()) 
                                  - TimeUnit.HOURS.toMinutes(hours);
		// avoid -4:-30 issue
		minutes = Math.abs(minutes);

		String result = "";
		if (hours > 0) {
			result = String.format("(GMT+%d:%02d) %s", hours, minutes, tz.getID());
		} else {
			result = String.format("(GMT%d:%02d) %s", hours, minutes, tz.getID());
		}
		return result;
	}
	
}
