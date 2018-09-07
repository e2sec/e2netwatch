
/*
    Copyright (C) 2017 e-ito Technology Services GmbH
    e-mail: info@e-ito.de
    
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/


package de.e2security.e2netwatch.model.elasticquery.serializer;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;

import de.e2security.e2netwatch.model.elasticquery.QueryFieldDetailsBuilder;
import de.e2security.e2netwatch.model.elasticquery.QueryFieldLongInformation;
import de.e2security.e2netwatch.model.elasticquery.serializer.QueryFieldLongInformationSerializer;

/**
 * Tests the QueryFieldLongInformationSerializer.
 *
 * @author Hrvoje
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/config/app-config.xml" })
@ActiveProfiles("test")
public class QueryFieldLongInformationSerializerTest {

	/**
	 * Runs before the tests start.
	 */
	@BeforeClass
	public static void testStart() {}
	
	/**
	 * Runs after the tests end.
	 */
	@AfterClass
	public static void testEnd() {}

	/**
	 * Test default construction.
	 */
	@Test
	public void testDefaultCtor() {
		QueryFieldLongInformationSerializer qflis = new QueryFieldLongInformationSerializer();
		Assert.assertNotNull(qflis);
	}

	/**
	 * QueryFieldLongInformation construction.
	 */
	@Test
	public void testStringCtor() {
		QueryFieldLongInformationSerializer qflis = new QueryFieldLongInformationSerializer(QueryFieldLongInformation.class);
		Assert.assertNotNull(qflis);
	}

	/**
	 * Test value serialization.
	 */
	@Test
	public void testValueSerialization() throws IOException, JsonProcessingException {
		// Prepare all objects
		QueryFieldLongInformation queryFieldLongInformation = new QueryFieldLongInformation(
				"message",
				new QueryFieldDetailsBuilder()
				.setValue("1.1.1.1")
				.build()
		);
		// Prepare surrounding and serialize
		Writer jsonWriter = new StringWriter();
	    JsonGenerator jsonGenerator = new JsonFactory().createGenerator(jsonWriter);
	    jsonGenerator.setCodec(new ObjectMapper());
	    SerializerProvider serializerProvider = new ObjectMapper().getSerializerProvider();
	    
	    /*
	     * From JsonGenerator 2.8 jackson-core version, we must have start object
	     */
	    jsonGenerator.writeStartObject();
	    new QueryFieldLongInformationSerializer().serialize(queryFieldLongInformation, jsonGenerator, serializerProvider);
	    jsonGenerator.writeEndObject();
	    
	    jsonGenerator.flush();
	    
	    Assert.assertEquals("{\"message\":{\"value\":\"1.1.1.1\"}}", jsonWriter.toString());
	}
}