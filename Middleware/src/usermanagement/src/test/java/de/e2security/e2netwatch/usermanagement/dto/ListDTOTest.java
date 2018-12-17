package de.e2security.e2netwatch.usermanagement.dto;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class ListDTOTest {
	
	@Test
	public void testContructor_1() {
		
		// when total received
		
		String first = "first";
		String second = "second";
		int total = 10;
		
		List<String> regularList = new ArrayList<>();
		regularList.add(first);
		regularList.add(second);
		ListDTO<String> list = new ListDTO<>(regularList, total);
		
		Assert.assertEquals("Number of retreved elements not correct", list.getRetrieved(), 2);
		Assert.assertEquals("Number of total elements not correct", list.getTotal(), total);
		Assert.assertEquals("First element not as expected", list.getContent().get(0), first);
	}
	
	@Test
	public void testContructor_2() {
		
		// when total not received
		
		String first = "first";
		String second = "second";
		
		List<String> regularList = new ArrayList<>();
		regularList.add(first);
		regularList.add(second);
		ListDTO<String> list = new ListDTO<>(regularList, null);
		
		Assert.assertEquals("Number of total elements not correct", list.getTotal(), 2);
	}

}
