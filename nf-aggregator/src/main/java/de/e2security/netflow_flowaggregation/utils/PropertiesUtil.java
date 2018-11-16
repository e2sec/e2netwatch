package de.e2security.netflow_flowaggregation.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {

	Properties configs;
	
	public PropertiesUtil(Properties props) {
		this.configs = props;
	}
	
	public Properties read(String fileName) {
		try {
			InputStream is = PropertiesUtil.class.getClassLoader().getResourceAsStream(fileName);
			this.configs.load(is);
			is.close();
		} catch (IOException e) {
			System.err.println("Cannot load properties!");
			System.exit(1);
		}
		return this.configs;
	}	

}
