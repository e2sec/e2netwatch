package de.e2security.netflow_flowaggregation.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

public class PropertiesUtil {

	private Properties configs = new Properties();
	private File configFile;

	public PropertiesUtil readInt(String fileName) {
		try {
			InputStream is = PropertiesUtil.class.getClassLoader().getResourceAsStream(fileName);
			configs.load(is);
			is.close();
		} catch (IOException e) {
			System.err.println("Cannot load properties!");
			System.exit(1);
		}
		return this;
	}
	
	@Option(name = "-c", usage = "defines additional configuration file")
	public void setFile(File f) {
		if (f.exists()) {
			configFile = f;
		} else {
			System.err.println("cannot read config file" + f.getName());
			System.exit(1);
		}
	}
	
	public PropertiesUtil readExt(String[] args, Class clazz) {
		if (args.length > 0) {
			CmdLineParser parser = new CmdLineParser(clazz);
			try {
				parser.parseArgument(args);
				if (this.configFile != null) {
					try {
						InputStream is = new FileInputStream(configFile);
						configs.load(is);
					} catch (IOException e) {
						System.err.println("Cannot read config file '" + configFile.getName() + "'");
						System.exit(1);
					}
				}
			} catch (CmdLineException e) {
				System.err.println(e.getMessage());
				System.err.println("Available sptions:");
				parser.printUsage(System.err);
				System.exit(1);
			}
		}
		return this;
	}
	
	public Properties create() {
		return this.configs;
	}
	

}
